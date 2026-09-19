# alumnos-backend-hexagonal-ia

Backend para un **ABM de Alumnos con autenticación JWT** implementando **Arquitectura Hexagonal (Puertos y Adaptadores)**.

- **Java 21** · **Spring Boot 4.1.1** · **Spring Security** · **JJWT 0.13.0** · **Spring Data JPA** · **MySQL 8 (Docker)**
- Todo el código, comentarios y mensajes están en español.
- Paquete base: `com.escuela`.

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
                │   ├ AlumnoController  ──────┐    ├ AlumnoJpaEntity (@Entity)  │
                │   ├ UsuarioController       │    ├ UsuarioJpaEntity (@Entity)│──► MySQL 8
                │   └ GlobalExceptionHandler  │    ├ AlumnoRepository          │   (Docker
                │                             │    ├ UsuarioRepository         │    puerto
                │  security/                  │    ├ AlumnoAdapter ────────────┼►  3309)
                │   ├ JwtAuthenticationFilter │    └ UsuarioAdapter            │ alumnos_hexago_db
                │   ├ SecurityConfig          │                                │   tabla alumnos
                │   ├ JwtTokenAdapter ────────┤    mapper/                     │   tabla usuarios
                │   └ BCryptPasswordAdapter   │    ├ AlumnoJpaMapper           │
                │                             │    └ UsuarioJpaMapper          │
                └───────────────┬─────────────┴────────────────┬───────────────┘
                                │     puertos in / out         │
                ┌───────────────▼──────────────────────────────▼───────────────┐
                │              APPLICATION  (casos de uso)                     │
                │                                                              │
                │  port/in/   AlumnoUseCase          AuthUseCase               │
                │  port/out/  AlumnoOutPort            UsuarioOutPort          │
                │             PasswordEncoderPort    TokenProviderPort         │
                │                                                              │
                │  service/   AlumnoService  (implements AlumnoUseCase)        │
                │             AuthService    (implements AuthUseCase)          │
                │                                                              │
                │  dto/       AlumnoDTO (entrada)     AlumnoDtoResponse (salida)│
                │             LoginRequest  RegistroRequest  LoginResponse     │
                └──────────────────────────────┬───────────────────────────────┘
                                               │
                ┌──────────────────────────────▼───────────────────────────────┐
                │              DOMAIN  (núcleo de negocio, JAVA PURO)          │
                │                                                              │
                │  model/       Alumno (POJO)      Usuario (POJO)   Rol (enum) │
                │  exception/   EmailDuplicadoException (409)                  │
                │               AlumnoNoEncontradoException (404)              │
                │               CredencialesInvalidasException (401)           │
                │                                                              │
                │  Sin JPA, sin Spring, sin Lombok: cero dependencias.         │
                └──────────────────────────────────────────────────────────────┘
```

**Reglas del hexágono respetadas:**

1. `domain` es Java puro: sin anotaciones JPA, sin Spring, sin Lombok.
2. `application` solo conoce `domain` y sus propios puertos; los services inyectan **solo interfaces** (`AlumnoOutPort`, `UsuarioOutPort`, `PasswordEncoderPort`, `TokenProviderPort`).
3. Los DTOs (`application/dto`) atraviesan los puertos de entrada; los casos de uso firman con DTOs, no con entidades de dominio.
4. `infrastructure` es lo único que toca JPA, Spring Web y Spring Security: los controllers inyectan `AlumnoUseCase` / `AuthUseCase`; `AlumnoAdapter` / `UsuarioAdapter` implementan los puertos de salida; `JwtTokenAdapter` implementa `TokenProviderPort` y `BCryptPasswordAdapter` implementa `PasswordEncoderPort` (detalles intercambiables).
5. La entidad de dominio nunca se expone por el controller ni se persiste directo: siempre pasa por DTO (entrada/salida) o por el mapper JPA (persistencia).
6. La contraseña (ni su hash) nunca se devuelve en ningún DTO de salida.

---

## 2. Flujo de un request autenticado (ejemplo: `GET /api/alumnos`)

```
Cliente (React) ──► GET /api/alumnos
                    Header: Authorization: Bearer eyJhbGciOi...

 1. SecurityConfig        decide: /api/alumnos/** NO es pública → authenticated()
 2. JwtAuthenticationFilter (OncePerRequestFilter)
      lee el header "Authorization: Bearer <token>"
      ├─ TokenProviderPort.validarToken(token)  ← implementado por JwtTokenAdapter (JJWT, HS256)
      │    • firma inválida / token expirado → HTTP 401 {"mensaje":"Token invalido o expirado"}
      ├─ TokenProviderPort.extraerEmail(token)  → subject = email del usuario
      ├─ UsuarioOutPort.buscarPorEmail(email)  ← implementado por UsuarioAdapter (JPA → MySQL)
      │    • usuario inexistente o inactivo → HTTP 401
      └─ SecurityContext.setAuthentication(UsernamePasswordAuthenticationToken + ROLE_x)
 3. AlumnoController (adaptador REST) → llama a la INTERFAZ AlumnoUseCase (puerto de entrada)
 4. AlumnoService (implementa AlumnoUseCase) → aplica reglas de negocio usando SOLO puertos out
 5. AlumnoOutPort.listarTodos()              ← puerto de salida
 6. AlumnoAdapter → AlumnoRepository (Spring Data JPA) → Hibernate
 7. MySQL 8 (Docker puerto 3309, BD alumnos_hexago_db, tabla alumnos)
 8. Respuesta: entidad JPA → AlumnoJpaMapper → dominio Alumno → DTO AlumnoDtoResponse → JSON (200)
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
| Rutas protegidas | `/api/alumnos/**` y el resto → `authenticated()` |
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
| GET | `/api/alumnos` | JWT | Lista todos los alumnos |
| GET | `/api/alumnos/{id}` | JWT | Alumno por id (404 si no existe) |
| GET | `/api/alumnos/estado/{estado}` | JWT | Filtra por ACTIVO/INACTIVO |
| POST | `/api/alumnos` | JWT | Crea alumno (201; 400 validaciones; 409 email duplicado) |
| PUT | `/api/alumnos/{id}` | JWT | Actualiza alumno (404 si no existe / 409 email duplicado) |
| DELETE | `/api/alumnos/{id}` | JWT | Elimina alumno (204) |

---

## 5. Base de datos (Docker)

Contenedor: `mysql-app-alumnos-hexagonal` · puerto host **3309** → 3306 · BD: `alumnos_hexago_db` · root/root.
Las tablas `alumnos` y `usuarios` se crean al arrancar el backend (`spring.jpa.hibernate.ddl-auto=update`).

```powershell
# Crear el contenedor (si no existe)
docker run -d --name mysql-app-alumnos-hexagonal -p 3309:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=alumnos_hexago_db mysql:8.0

# Verificar que MySQL esté listo
docker logs mysql-app-alumnos-hexagonal 2>&1 | Select-String "ready for connections"

# Verificar tablas creadas
docker exec mysql-app-alumnos-hexagonal mysql -uroot -proot -e "SHOW TABLES FROM alumnos_hexago_db;"
```

Conexión (`application.properties`): `jdbc:mysql://localhost:3309/alumnos_hexago_db`

---

## 6. Comandos de ejecución

```powershell
# 1) Levantar MySQL (Docker) — si el contenedor ya existe
docker start mysql-app-alumnos-hexagonal

# 2) Arrancar el backend (desde d:\desarrollo\repo\alumnos-backend-hexagonal-ia)
.\mvnw.cmd spring-boot:run        # -> http://localhost:8080

# 3) Arrancar el frontend (desde d:\desarrollo\repo\alumnos-frontend-hexagonal-ia)
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
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/api/alumnos

# Registro -> 201 con token JWT
curl.exe -s -X POST -H "Content-Type: application/json" `
  -d "{`"nombre`":`"Admin`",`"email`":`"admin@escuela.com`",`"password`":`"secreto123`"}" `
  -w "`nSTATUS:%{http_code}" http://localhost:8080/api/usuarios/registro

# Listado con token -> 200
curl.exe -s -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/alumnos

# Login con contraseña incorrecta -> 401
# Registro duplicado -> 409 · Alumno inexistente -> 404 · DELETE -> 204
```

---

## 8. Estructura de archivos

```
src/main/java/com/escuela/
├── AlumnosHexagonalApplication.java   (arranque; única clase fuera de las 3 capas)
├── domain/                            (núcleo de negocio, Java puro)
│   ├── model/     Alumno, Usuario, Rol
│   └── exception/ EmailDuplicadoException, AlumnoNoEncontradoException,
│                  CredencialesInvalidasException
├── application/                       (casos de uso)
│   ├── dto/       AlumnoDTO, AlumnoDtoResponse, LoginRequest, RegistroRequest, LoginResponse
│   ├── port/in/   AlumnoUseCase, AuthUseCase
│   ├── port/out/  AlumnoOutPort, UsuarioOutPort, PasswordEncoderPort, TokenProviderPort
│   └── service/   AlumnoService, AuthService
└── infrastructure/                    (adaptadores)
    ├── in/        AlumnoController, UsuarioController, GlobalExceptionHandler
    ├── mapper/    AlumnoJpaMapper, UsuarioJpaMapper
    ├── security/  JwtTokenAdapter, BCryptPasswordAdapter, JwtAuthenticationFilter, SecurityConfig
    └── out/db/    AlumnoJpaEntity, AlumnoRepository, AlumnoAdapter,
                    UsuarioJpaEntity, UsuarioRepository, UsuarioAdapter
```


