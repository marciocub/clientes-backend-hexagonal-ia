# clientes-backend-hexagonal-ia

Backend para un **ABM de Clientes (agregado: tarjetas de crédito y facturas con items) y de Cuentas**, con autenticación JWT, implementando **Arquitectura Hexagonal (Puertos y Adaptadores)**.

- **Java 21** · **Spring Boot 4.1.1** · **Spring Security** · **JJWT 0.13.0** · **Spring Data JPA** · **MySQL 8 (Docker)**
- Todo el código, comentarios y mensajes están en español.
- Paquete base: `com.banco`.

---

## 1. Arquitectura Hexagonal (ASCII)

```
                ┌───────────────────────────────────────────────────────────────┐
                │              INFRASTRUCTURE  (adaptadores)                    │
                │                                                               │
                │  ADAPTADORES DE ENTRADA          ADAPTADORES DE SALIDA        │
                │  (driving)                       (driven)                     │
                │                                                               │
                │  in/                             out/db/                      │
                │   ├ ClienteController  ──────┐    ├ ClienteJpaEntity (@Entity)  │
                │   ├ UsuarioController       │    ├ UsuarioJpaEntity (@Entity)│──► MySQL 8
                │   └ GlobalExceptionHandler  │    ├ ClienteRepository          │   (Docker
                │                             │    ├ UsuarioRepository         │    puerto
                │  security/                  │    ├ ClienteAdapter ────────────┼►  3309)
                │   ├ JwtAuthenticationFilter │    └ UsuarioAdapter            │ clientes_hexago_db
                │   ├ SecurityConfig          │                                │   tabla clientes
                │   ├ JwtTokenAdapter ────────┤    mapper/                     │   tabla usuarios
                │   └ BCryptPasswordAdapter   │    ├ ClienteJpaMapper           │
                │                             │    └ UsuarioJpaMapper          │
                └───────────────┬─────────────┴────────────────┬───────────────┘
                                │     puertos in / out         │
                ┌───────────────▼──────────────────────────────▼───────────────┐
                │              APPLICATION  (casos de uso)                     │
                │                                                              │
                │  port/in/   ClienteUseCase          AuthUseCase               │
                │  port/out/  ClienteOutPort            UsuarioOutPort          │
                │             PasswordEncoderPort    TokenProviderPort         │
                │                                                              │
                │  service/   ClienteService  (implements ClienteUseCase)        │
                │             AuthService    (implements AuthUseCase)          │
                │                                                              │
                │  dto/       ClienteDTO (entrada)     ClienteDtoResponse (salida)│
                │             LoginRequest  RegistroRequest  LoginResponse     │
                └──────────────────────────────┬───────────────────────────────┘
                                               │
                ┌──────────────────────────────▼───────────────────────────────┐
                │              DOMAIN  (núcleo de negocio, JAVA PURO)          │
                │                                                              │
                │  model/       Cliente (POJO)      Usuario (POJO)   Rol (enum) │
                │  exception/   EmailDuplicadoException (409)                  │
                │               ClienteNoEncontradoException (404)              │
                │               CredencialesInvalidasException (401)           │
                │                                                              │
                │  Sin JPA, sin Spring, sin Lombok: cero dependencias.         │
                └──────────────────────────────────────────────────────────────┘
```

**Reglas del hexágono respetadas:**

1. `domain` es Java puro: sin anotaciones JPA, sin Spring, sin Lombok.
2. `application` solo conoce `domain` y sus propios puertos; los services inyectan **solo interfaces** (`ClienteOutPort`, `UsuarioOutPort`, `PasswordEncoderPort`, `TokenProviderPort`).
3. Los DTOs (`application/dto`) atraviesan los puertos de entrada; los casos de uso firman con DTOs, no con entidades de dominio.
4. `infrastructure` es lo único que toca JPA, Spring Web y Spring Security: los controllers inyectan `ClienteUseCase` / `AuthUseCase`; `ClienteAdapter` / `UsuarioAdapter` implementan los puertos de salida; `JwtTokenAdapter` implementa `TokenProviderPort` y `BCryptPasswordAdapter` implementa `PasswordEncoderPort` (detalles intercambiables).
5. La entidad de dominio nunca se expone por el controller ni se persiste directo: siempre pasa por DTO (entrada/salida) o por el mapper JPA (persistencia).
6. La contraseña (ni su hash) nunca se devuelve en ningún DTO de salida.

---

## 2. Flujo de un request autenticado (ejemplo: `GET /api/clientes`)

```
Cliente (React) ──► GET /api/clientes
                    Header: Authorization: Bearer eyJhbGciOi...

 1. SecurityConfig        decide: /api/clientes/** NO es pública → authenticated()
 2. JwtAuthenticationFilter (OncePerRequestFilter)
      lee el header "Authorization: Bearer <token>"
      ├─ TokenProviderPort.validarToken(token)  ← implementado por JwtTokenAdapter (JJWT, HS256)
      │    • firma inválida / token expirado → HTTP 401 {"mensaje":"Token invalido o expirado"}
      ├─ TokenProviderPort.extraerEmail(token)  → subject = email del usuario
      ├─ UsuarioOutPort.buscarPorEmail(email)  ← implementado por UsuarioAdapter (JPA → MySQL)
      │    • usuario inexistente o inactivo → HTTP 401
      └─ SecurityContext.setAuthentication(UsernamePasswordAuthenticationToken + ROLE_x)
 3. ClienteController (adaptador REST) → llama a la INTERFAZ ClienteUseCase (puerto de entrada)
 4. ClienteService (implementa ClienteUseCase) → aplica reglas de negocio usando SOLO puertos out
 5. ClienteOutPort.listarTodos()              ← puerto de salida
 6. ClienteAdapter → ClienteRepository (Spring Data JPA) → Hibernate
 7. MySQL 8 (Docker puerto 3309, BD clientes_hexago_db, tabla clientes)
 8. Respuesta: entidad JPA → ClienteJpaMapper → dominio Cliente → DTO ClienteDtoResponse → JSON (200)
```

---

## 3. Seguridad JWT

| Regla | Detalle |
|---|---|
| Algoritmo | HS256 (JJWT), secreto en `app.jwt.secret` |
| Expiración | `app.jwt.expiration=86400000` (24 horas) |
| Subject | email del usuario |
| Contraseñas | BCrypt (`BCryptPasswordAdapter`); jamás en texto plano ni en respuestas |
| Rutas públicas | `POST /api/usuarios/registro`, `POST /api/usuarios/login`, `GET /api/usuarios/ping` |
| Rutas protegidas | `/api/clientes/**`, `/api/cuentas/**` y el resto → `authenticated()` |
| Sin token en ruta protegida | **403** (entry point por defecto de Spring Security) |
| Token inválido/expirado | **401** (lo resuelve `JwtAuthenticationFilter`) |
| Sesiones | `STATELESS`, CSRF deshabilitado |
| CORS | `http://localhost:3000` (vía `CorsConfigurationSource` en `SecurityConfig`) |

---

## 4. API REST

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/api/usuarios/registro` | Pública | Registra usuario, devuelve token JWT (201) |
| POST | `/api/usuarios/login` | Pública | Autentica, devuelve token JWT (200) |
| GET | `/api/usuarios/ping` | Pública | Health check → `"pong"` |
| GET | `/api/clientes` | JWT | Lista todos los clientes con su agregado completo (tarjetas + facturas) |
| GET | `/api/clientes/{id}` | JWT | Cliente por id con tarjetas y facturas (404 si no existe) |
| POST | `/api/clientes` | JWT | Crea cliente (201; 400 validaciones) |
| PUT | `/api/clientes/{id}` | JWT | Actualiza nombre/cuit conservando el agregado (404 si no existe) |
| DELETE | `/api/clientes/{id}` | JWT | Elimina el cliente y su agregado en cascada (204) |
| POST | `/api/clientes/{id}/tarjetas` | JWT | Solicita tarjeta (201; **409 si el límite acumulado supera los $ 2.000.000**) |
| POST | `/api/clientes/{id}/facturas` | JWT | Emite factura con items; el monto total lo calcula el dominio (201; 400 si no hay items) |
| GET | `/api/cuentas` | JWT | Lista todas las cuentas |
| GET | `/api/cuentas/{id}` | JWT | Cuenta por id (404 si no existe) |
| GET | `/api/cuentas/estado/{estado}` | JWT | Filtra por ACTIVO/INACTIVO (400 si el estado es inválido) |
| POST | `/api/cuentas` | JWT | Crea cuenta (201; 400 validaciones; 409 número de cuenta duplicado) |
| PUT | `/api/cuentas/{id}` | JWT | Actualiza cuenta (404 si no existe; 409 número duplicado) |
| DELETE | `/api/cuentas/{id}` | JWT | Elimina cuenta (204) |

### Ejemplos rápidos del agregado

```powershell
# Alta de cliente
$cli = Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/clientes -Headers $h `
  -ContentType 'application/json' -Body '{"nombre":"Acme SA","cuit":"20-12345678-9"}'

# Solicitar tarjeta (409 si el acumulado supera el tope de 2.000.000)
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/clientes/$($cli.id)/tarjetas" -Headers $h `
  -ContentType 'application/json' -Body '{"marca":"VISA","limiteSolicitado":1500000,"ultimoCuatro":"4321"}'

# Emitir factura con items (montoTotal lo calcula el dominio: 2 x 1000,50 + 1 x 500 = 2501,00)
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/clientes/$($cli.id)/facturas" -Headers $h `
  -ContentType 'application/json' -Body '{"numeroFactura":"F-0001-00000001","items":[{"descripcion":"Servicio A","cantidad":2,"precioUnitario":1000.50},{"descripcion":"Servicio B","cantidad":1,"precioUnitario":500}]}'
```


---

## 5. Base de datos (Docker)

Contenedor: `mysql-app-clientes-hexagonal` · puerto host **3309** → 3306 · BD: `clientes_hexago_db` · root/root.
Las tablas se crean al arrancar el backend (`spring.jpa.hibernate.ddl-auto=update`):
`clientes`, `tarjetas_credito`, `facturas`, `factura_items`, `cuentas`, `usuarios`.

```powershell
# Crear el contenedor (si no existe)
docker run -d --name mysql-app-clientes-hexagonal -p 3309:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=clientes_hexago_db mysql:8.0

# Verificar que MySQL esté listo
docker logs mysql-app-clientes-hexagonal 2>&1 | Select-String "ready for connections"

# Verificar tablas creadas
docker exec mysql-app-clientes-hexagonal mysql -uroot -proot -e "SHOW TABLES FROM clientes_hexago_db;"
```

Conexión (`application.properties`): `jdbc:mysql://localhost:3309/clientes_hexago_db`

---

## 6. Comandos de ejecución

```powershell
# 1) Levantar MySQL (Docker) — si el contenedor ya existe
docker start mysql-app-clientes-hexagonal

# 2) Arrancar el backend (desde d:\desarrollo\repo\clientes-backend-hexagonal-ia)
.\mvnw.cmd spring-boot:run        # -> http://localhost:8080

# 3) Arrancar el frontend (desde d:\desarrollo\repo\clientes-frontend-hexagonal-ia)
#    npm install   (solo la primera vez)
#    npm start     -> http://localhost:3000
```

> Importante: el contenedor MySQL debe estar levantado **antes** de arrancar el backend.

---

## 7. Pruebas rápidas (PowerShell + curl.exe)

```powershell
# Health check público -> 200 "pong"
curl.exe -s -w "`nSTATUS:%{http_code}" http://localhost:8080/api/usuarios/ping

# Sin token en ruta protegida -> 403
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/api/clientes

# Registro -> 201 con token JWT
curl.exe -s -X POST -H "Content-Type: application/json" `
  -d "{`"nombre`":`"Admin`",`"email`":`"admin@escuela.com`",`"password`":`"secreto123`"}" `
  -w "`nSTATUS:%{http_code}" http://localhost:8080/api/usuarios/registro

# Listado con token -> 200
curl.exe -s -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/clientes

# Agregar tarjeta a un cliente (409 si el acumulado supera 2.000.000)
curl.exe -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" ^
  -d "{\"marca\":\"VISA\",\"limiteSolicitado\":1500000,\"ultimoCuatro\":\"4321\"}" ^
  http://localhost:8080/api/clientes/1/tarjetas

# Emitir factura con items (el total lo calcula el dominio)
curl.exe -s -X POST -H "Content-Type: application/json" -H "Authorization: Bearer <TOKEN>" ^
  -d "{\"numeroFactura\":\"F-0001-00000001\",\"items\":[{\"descripcion\":\"Servicio A\",\"cantidad\":2,\"precioUnitario\":1000.50}]}" ^
  http://localhost:8080/api/clientes/1/facturas

# Login con contraseña incorrecta -> 401
# Registro duplicado -> 409 · Cliente/Cuenta inexistente -> 404 · DELETE -> 204
```

---

## 8. Estructura de archivos

```
src/main/java/com/banco/
├── ClientesHexagonalApplication.java   (arranque; única clase fuera de las 3 capas)
├── domain/                            (núcleo de negocio, Java puro)
│   ├── model/     Cliente (AGGREGATE ROOT), TarjetaCredito, Factura, ItemFactura,
│   │              Cuenta, Usuario, Rol
│   ├── Enum/      Moneda, EstadoCuenta
│   └── exception/ ClienteNoEncontradoException, LimiteTarjetasExcedidoException,
│                  ConsumoExcedeDisponibleException, FacturaYaPagadaException,
│                  CuentaNoEncontradoException, NumeroCuentaDuplicadoException,
│                  EmailDuplicadoException, CredencialesInvalidasException
├── application/                       (casos de uso)
│   ├── dto/       ClienteDTO, ClienteDtoResponse, SolicitarTarjetaDTO, CrearFacturaDTO,
│   │              ItemFacturaDTO, TarjetaCreditoDtoResponse, FacturaDtoResponse,
│   │              CuentaDTO, CuentaDtoResponse, LoginRequest, RegistroRequest, LoginResponse
│   ├── port/in/   ClienteUseCase, CuentaUseCase, AuthUseCase
│   ├── port/out/  ClienteOutPort, CuentaOutPort, UsuarioOutPort,
│   │              PasswordEncoderPort, TokenProviderPort
│   └── service/   ClienteService, CuentaService, AuthService
└── infrastructure/                    (adaptadores)
    ├── in/        ClienteController, CuentaController, UsuarioController,
    │              GlobalExceptionHandler
    ├── mapper/    ClienteJpaMapper, CuentaJpaMapper, UsuarioJpaMapper
    ├── security/  JwtTokenAdapter, BCryptPasswordAdapter, JwtAuthenticationFilter, SecurityConfig
    └── out/db/    ClienteJpaEntity, TarjetaCreditoJpaEntity, FacturaJpaEntity,
                    ItemFacturaJpaEntity, ClienteRepository, ClienteAdapter,
                    CuentaJpaEntity, CuentaRepository, CuentaAdapter,
                    UsuarioJpaEntity, UsuarioRepository, UsuarioAdapter
```

### Agregado `Cliente` (modelo de dominio)

```
Cliente (AGGREGATE ROOT)  id · nombre · cuit
 ├── tarjetas  List<TarjetaCredito>   id · numeroMascara · marca · limiteCredito ·
 │                                   saldoUtilizado · fechaVencimiento · estado(ACTIVA|BLOQUEADA)
 │                                   getCreditoDisponible() · registrarConsumo(monto)
 └── facturas  List<Factura>          id · numeroFactura · fechaEmision · montoTotal ·
                                     estado(PENDIENTE|PAGADA) · calcularTotal() · pagar()
      └── items  List<ItemFactura>   id · descripcion · cantidad · precioUnitario · subtotal

Reglas de negocio EN EL DOMINIO:
 · solicitarTarjeta(marca, limiteSolicitado, ultimoCuatro) → el límite acumulado de todas
   las tarjetas no puede superar el TOPE de $ 2.000.000 (si lo supera: LimiteTarjetasExcedido → 409).
 · emitirFactura(numeroFactura, items) → calcula montoTotal sumando los items y la agrega.
```



