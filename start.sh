#!/usr/bin/env bash
# ============================================================
#  start.sh - Levanta la app completa (Git Bash / Linux)
#  1) MySQL en Docker (contenedor mysql-app-clientes-hexagonal)
#  2) Backend Spring Boot (8080) en segundo plano -> backend-run.log
#  3) Frontend React (3000) en segundo plano -> frontend-run.log
#  Uso: ./start.sh
#  Para detener: ./stop.sh
# ============================================================
set -u

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRONTEND_DIR="$SCRIPT_DIR/../clientes-frontend-hexagonal-ia"
CONTAINER="mysql-app-clientes-hexagonal"
BACKEND_LOG="$SCRIPT_DIR/backend-run.log"
FRONTEND_LOG="$SCRIPT_DIR/frontend-run.log"

puerto_ocupado() {
    netstat -ano 2>/dev/null | grep "LISTENING" | grep -q ":$1 "
}

echo "============================================"
echo " 1/3) MySQL en Docker: $CONTAINER"
echo "============================================"
if ! docker info >/dev/null 2>&1; then
    echo "[ERROR] Docker no esta corriendo. Abrilo y volve a ejecutar."
    exit 1
fi
if [ "$(docker inspect -f '{{.State.Running}}' "$CONTAINER" 2>/dev/null)" != "true" ]; then
    echo "Levantando contenedor..."
    docker start "$CONTAINER"
else
    echo "[OK] El contenedor ya esta corriendo."
fi
echo "Esperando a que MySQL este listo..."
for i in $(seq 1 30); do
    docker logs "$CONTAINER" 2>&1 | grep -q "ready for connections" && break
    sleep 2
done
if docker logs "$CONTAINER" 2>&1 | grep -q "ready for connections"; then
    echo "[OK] MySQL listo (puerto 3309)."
else
    echo "[ERROR] MySQL no esta listo. Revisa: docker logs $CONTAINER"
    exit 1
fi

echo "============================================"
echo " 2/3) Backend Spring Boot (puerto 8080)"
echo "============================================"
if puerto_ocupado 8080; then
    echo "[AVISO] Ya hay algo escuchando en 8080. No se vuelve a levantar."
else
    ( cd "$SCRIPT_DIR" && ./mvnw.cmd spring-boot:run > "$BACKEND_LOG" 2>&1 & )
    echo "Backend lanzado (log: backend-run.log). Esperando 'Started ClientesHexagonalApplication'..."
    for i in $(seq 1 90); do
        grep -q "Started ClientesHexagonalApplication" "$BACKEND_LOG" 2>/dev/null && break
        sleep 2
    done
    if grep -q "Started ClientesHexagonalApplication" "$BACKEND_LOG" 2>/dev/null; then
        echo "[OK] Backend arriba (http://localhost:8080)."
    else
        echo "[AVISO] El backend no confirmo arranque. Revisa backend-run.log"
    fi
fi

echo "============================================"
echo " 3/3) Frontend React (puerto 3000)"
echo "============================================"
if [ ! -d "$FRONTEND_DIR/node_modules" ]; then
    echo "node_modules no existe. Ejecutando npm install..."
    ( cd "$FRONTEND_DIR" && npm install )
fi
if puerto_ocupado 3000; then
    echo "[AVISO] Ya hay algo escuchando en 3000. No se vuelve a levantar."
else
    ( cd "$FRONTEND_DIR" && npm start > "$FRONTEND_LOG" 2>&1 & )
    echo "Frontend lanzado (log: frontend-run.log). Esperando 'Compiled successfully'..."
    for i in $(seq 1 60); do
        grep -q "Compiled successfully" "$FRONTEND_LOG" 2>/dev/null && break
        sleep 3
    done
    if grep -q "Compiled successfully" "$FRONTEND_LOG" 2>/dev/null; then
        echo "[OK] Frontend arriba (http://localhost:3000)."
    else
        echo "[AVISO] El frontend no confirmo compilacion. Revisa frontend-run.log"
    fi
fi

echo "============================================"
echo " App lista!"
echo "   Frontend : http://localhost:3000"
echo "   Backend  : http://localhost:8080"
echo "   Login    : admin@escuela.com / secreto123"
echo " Para detener: ./stop.sh"
echo "============================================"
