@echo off
REM ============================================================
REM  start.bat - Levanta la app completa en Windows
REM  1) MySQL en Docker (contenedor mysql-app-clientes-hexagonal)
REM  2) Backend Spring Boot (puerto 8080) en ventana propia
REM  3) Frontend React (puerto 3000) en ventana propia
REM  Uso: doble clic o .\start.bat desde la raiz del backend
REM  Para detener: .\stop.bat
REM ============================================================
setlocal
set "CONTAINER=mysql-app-clientes-hexagonal"
set "BACKEND_DIR=%~dp0"
set "FRONTEND_DIR=%~dp0..\clientes-frontend-hexagonal-ia"

REM ---------- 1) MySQL (Docker) ----------
echo ============================================
echo  1/3 ^) MySQL en Docker: %CONTAINER%
echo ============================================
docker info >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Docker Desktop no esta corriendo. Abrilo y volve a ejecutar.
    pause
    exit /b 1
)
docker ps --format "{{.Names}}" | findstr /C:"%CONTAINER%" >nul
if errorlevel 1 (
    echo Levantando contenedor...
    docker start %CONTAINER%
) else (
    echo [OK] El contenedor ya esta corriendo.
)
set /a intentos=0
:esperar_mysql
docker logs %CONTAINER% 2>&1 | findstr /C:"ready for connections" >nul && goto mysql_listo
set /a intentos+=1
if %intentos% GEQ 30 (
    echo [ERROR] MySQL no esta listo tras 60 segundos. Revisa: docker logs %CONTAINER%
    pause
    exit /b 1
)
timeout /t 2 /nobreak >nul
goto esperar_mysql
:mysql_listo
echo [OK] MySQL listo (puerto 3309).

REM ---------- 2) Backend (8080) ----------
echo ============================================
echo  2/3 ^) Backend Spring Boot (puerto 8080)
echo ============================================
netstat -ano | findstr /C:":8080" | findstr /C:"LISTENING" >nul
if errorlevel 1 (
    start "Backend - clientes (8080)" /D "%BACKEND_DIR%" cmd /k mvnw.cmd spring-boot:run
    echo [OK] Backend lanzado en ventana nueva. Espera el mensaje: Started ClientesHexagonalApplication
) else (
    echo [AVISO] Ya hay algo escuchando en 8080. No se vuelve a levantar.
)

REM ---------- 3) Frontend (3000) ----------
echo ============================================
echo  3/3 ^) Frontend React (puerto 3000)
echo ============================================
if not exist "%FRONTEND_DIR%\node_modules" (
    echo node_modules no existe. Ejecutando npm install...
    pushd "%FRONTEND_DIR%"
    call npm install
    popd
)
netstat -ano | findstr /C:":3000" | findstr /C:"LISTENING" >nul
if errorlevel 1 (
    start "Frontend - clientes (3000)" /D "%FRONTEND_DIR%" cmd /k npm start
    echo [OK] Frontend lanzado en ventana nueva. Espera el mensaje: Compiled successfully
) else (
    echo [AVISO] Ya hay algo escuchando en 3000. No se vuelve a levantar.
)

echo ============================================
echo  App lista!
echo    Frontend : http://localhost:3000
echo    Backend  : http://localhost:8080
echo    Login    : admin@escuela.com / secreto123
echo  Para detener todo: .\stop.bat
echo ============================================
endlocal
pause
