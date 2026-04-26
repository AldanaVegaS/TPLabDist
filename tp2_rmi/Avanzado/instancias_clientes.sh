#!/usr/bin/env bash

# Carpeta destino (dentro de basico)
LOG_DIR="TPLabDist/tp2_rmi/Avanzado/logs"
mkdir -p "$LOG_DIR"

signos=("acuario" "tauro" "leo" "tauro" "virgo")
fechas=("01/01/2000" "04/04/2003" "03/03/2002" "04/04/2003" "05/05/2004" )

for i in "${!signos[@]}"; do
  (
    {
      echo "================================="
      echo "Cliente ID: $i"
      echo "Fecha ejecución: $(date)"
      echo "Signo enviado: ${signos[$i]}"
      echo "Fecha enviada: ${fechas[$i]}"
      echo "================================="
      
      # Ejecutar cliente con input controlado
      printf "%s\n%s\nexit\n" "${signos[$i]}" "${fechas[$i]}" | \
      java -cp "lib/*:." TPLabDist.tp2_rmi.Avanzado.cliente.Cliente

      echo ""
      echo "===== FIN CLIENTE $i ====="
    } > "$LOG_DIR/cliente_$i.log" 2>&1
  ) &
done

wait

echo "Todos los clientes terminaron"
echo "Logs disponibles en carpeta: $LOG_DIR/"