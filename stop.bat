@echo off
REM ============================================================
REM  stop.bat - Detiene Backend (8080) y Frontend (3000) en Windows
REM  Mata los procesos que esten ESCUCHANDO en esos puertos,
REM  sin importar como fueron levantados.
REM  MySQL queda corriendo (linea opcional al final, descomentar
REM  si tambien queres apagar el contenedor).
REM  Uso: .\stop.bat
REM ============================================================
setlocal
echo ============================================
echo  Deteniendo backend (puerto 8080)...
echo ============================================
for /f "tokens=5" %%p in ('netstat -ano ^| findstr /C:":8080" ^| findstr /C:"LISTENING"') do (
    echo   matando PID %%p
    taskkill /F /PID %%p >nul 2>&1
)
echo ============================================
echo  Deteniendo frontend (puerto 3000)...
echo ============================================
for /f "tokens=5" %%p in ('netstat -ano ^| findstr /C:":3000" ^| findstr /C:"LISTENING"') do (
    echo   matando PID %%p
    taskkill /F /PID %%p >nul 2>&1
)
echo ============================================
echo  [OK] Backend y frontend detenidos.
echo  MySQL sigue corriendo (recomendado).
echo  Para apagar tambien MySQL:
echo    docker stop mysql-app-clientes-hexagonal
echo ============================================
REM docker stop mysql-app-clientes-hexagonal
endlocal
pause
