#!/usr/bin/env bash
# ============================================================
#  stop.sh - Detiene Backend (8080) y Frontend (3000)
#  Mata los procesos que esten ESCUCHANDO en esos puertos.
#  MySQL queda corriendo (ver linea final comentada).
#  Uso: ./stop.sh   (Git Bash o Linux)
# ============================================================

matar_puerto() {
    local puerto="$1"
    netstat -ano 2>/dev/null | grep "LISTENING" | grep ":$puerto " | awk '{print $5}' | sort -u | while read -r pid; do
        if [ -n "$pid" ] && [ "$pid" != "0" ]; then
            echo "  matando PID $pid (puerto $puerto)"
            taskkill //F //PID "$pid" >/dev/null 2>&1 || kill -9 "$pid" >/dev/null 2>&1
        fi
    done
}

echo "============================================"
echo " Deteniendo backend (puerto 8080)..."
echo "============================================"
matar_puerto 8080

echo "============================================"
echo " Deteniendo frontend (puerto 3000)..."
echo "============================================"
matar_puerto 3000

echo "============================================"
echo " [OK] Backend y frontend detenidos."
echo " MySQL sigue corriendo (recomendado)."
echo " Para apagar tambien MySQL: docker stop mysql-app-clientes-hexagonal"
echo "============================================"
