# 🚀 Cómo iniciar y detener la app (MySQL + Backend + Frontend)

> **Orden obligatorio**: `Docker (MySQL)` → `Backend (8080)` → `Frontend (3000)`.
> El backend **necesita** la base de datos al arrancar: si el contenedor no está corriendo, el arranque falla con `Communications link failure`.

---

## ⚡ Arranque rápido (recomendado)

En la raíz del backend (`d:\desarrollo\repo\clientes-backend-hexagonal-ia`):

| Consola | Comando | Qué hace |
|---|---|---|
| PowerShell / cmd (Windows) | `.\start.bat` | Levanta Docker + backend + frontend en ventanas separadas |
| Git Bash | `./start.sh` | Igual, en segundo plano con logs (`backend-run.log` / `frontend-run.log`) |
| PowerShell / cmd | `.\stop.bat` | Detiene backend (8080) y frontend (3000) |
| Git Bash | `./stop.sh` | Igual |

`stop` **deja MySQL corriendo** (así el próximo arranque es más rápido). Para apagar también MySQL: `docker stop mysql-app-clientes-hexagonal`.

---

## 1. Paso previo: Docker (contenedor MySQL)

```powershell
# ¿Está corriendo el contenedor?
docker ps --filter "name=mysql-app-clientes-hexagonal"

# Si NO aparece en la lista (está detenido):
docker start mysql-app-clientes-hexagonal

# Esperar a que MySQL esté listo (repetir hasta ver la línea):
docker logs mysql-app-clientes-hexagonal 2>&1 | Select-String "ready for connections"
# → "... ready for connections. Version: '8.0.44' ..."
```

Si Docker Desktop está cerrado, abrilo primero (el contenedor vive ahí).

## 2. Backend (puerto 8080)

En una consola (PowerShell o cmd):

```powershell
cd d:\desarrollo\repo\clientes-backend-hexagonal-ia
.\mvnw.cmd spring-boot:run
```

Esperar en el log:

```
Tomcat started on port 8080 (http) with context path '/'
Started ClientesHexagonalApplication in X seconds
```

Health check rápido (en otra consola):

```powershell
curl.exe -s -w " STATUS:%{http_code}" http://localhost:8080/api/usuarios/ping
# → pong STATUS:200
```

## 3. Frontend (puerto 3000) — en OTRA consola

```powershell
cd d:\desarrollo\repo\clientes-frontend-hexagonal-ia
npm start
```

> `npm install` solo es necesario la primera vez o si cambia `package.json`.

Esperar:

```
Compiled successfully!
You can now view clientes-frontend-hexagonal in the browser.
  Local:            http://localhost:3000
```

## 4. Probar la app

- Navegador: **http://localhost:3000**
- Login de prueba: **admin@escuela.com / secreto123** (o registrate desde la UI)
- Smoke tests por consola:

```powershell
# Sin token en ruta protegida → 403 (esperado)
curl.exe -s -o NUL -w "%{http_code}" http://localhost:8080/api/clientes
```

## 5. Cómo detener todo

1. En la consola del **frontend**: `Ctrl + C` (y `S` si pregunta).
2. En la consola del **backend**: `Ctrl + C`.
3. Opcional — apagar MySQL:
   ```powershell
   docker stop mysql-app-clientes-hexagonal
   ```
   (Para volver a usar la app: `docker start mysql-app-clientes-hexagonal`, no `docker run`.)

O directo con los scripts: `.\stop.bat` (Windows) / `./stop.sh` (Git Bash).

## 6. Problemas comunes

| Síntoma | Causa | Solución |
|---|---|---|
| Backend no arranca: `Communications link failure` | MySQL/contenedor no está corriendo | Paso 1: `docker start mysql-app-clientes-hexagonal` |
| `docker ps` da error de conexión | Docker Desktop cerrado | Abrir Docker Desktop y esperar que quede "Running" |
| Puerto 8080 u 3000 ocupado | Otra app/proceso viejo | `netstat -ano \| findstr :8080` → `taskkill /F /PID <pid>` (o `.\stop.bat`) |
| `npm ERR` al levantar frontend | Dependencias sin instalar | `cd clientes-frontend-hexagonal-ia && npm install` y repetir `npm start` |
| Frontend 403/401 en pantalla | Token expirado o sin sesión | Volver a loguear (el logout es automático con 401) |
| Maven descarga mucho la primera vez | Caché vacía | Es normal; espera a que termine |

## 7. Referencia rápida (versión corta)

```powershell
# --- De cero a app corriendo (3 consolas) ---
docker start mysql-app-clientes-hexagonal        # 1) MySQL
cd d:\desarrollo\repo\clientes-backend-hexagonal-ia; .\mvnw.cmd spring-boot:run   # 2) Backend 8080
cd d:\desarrollo\repo\clientes-frontend-hexagonal-ia; npm start                    # 3) Frontend 3000

# --- Detener ---
# Ctrl+C en cada consola (frontend y backend)
docker stop mysql-app-clientes-hexagonal         # opcional
```
