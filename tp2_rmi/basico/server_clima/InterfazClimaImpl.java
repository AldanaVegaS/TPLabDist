package TPLabDist.tp2_rmi.basico.server_clima;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.atomic.AtomicInteger;

import TPLabDist.tp2_rmi.basico.server_central.InterfazClima;

public class InterfazClimaImpl implements InterfazClima {

    private AtomicInteger contadorPeticiones;

    public InterfazClimaImpl() {
        super();
        this.contadorPeticiones = new AtomicInteger(0);
    }

    @Override
    public String obtenerPronostico(String fecha) {
       
        String[] predicciones = {
            "El clima de hoy es soleado con temperaturas agradables.",
            "Se esperan lluvias ligeras durante la tarde.",
            "El clima de hoy es nublado con posibilidad de tormentas.",
            "Se pronostica un día caluroso con alta humedad."
        };

        String respuesta;

        System.out.println("Servidor Clima> Conexión entrante aceptada.");

        // contador de peticiones para identificar cada solicitud
        int numPeticiones = contadorPeticiones.incrementAndGet();
        
        // identificar el hilo que atiende la solicitud
        long idHilo = Thread.currentThread().getId();

        // 3. Imprimir el registro en la consola del servidor
        System.out.println("Petición #" + numPeticiones + " recibida. Atendida por el Hilo: " +  idHilo + " con fecha=" + fecha);

        fecha = fecha.toUpperCase();

        if (!esFechaValida(fecha)) {
            respuesta = "Error: Fecha no válida.";
            System.out.println("Servidor Clima> Pronóstico enviado: Fecha no válida.");
        } else {
            respuesta = predicciones[obtenerIndice(fecha, predicciones.length)];
            System.out.println("Servidor Clima> Pronóstico enviado: \"" + respuesta + "\"");
        }
        
        return respuesta;
    }
    
    private boolean esFechaValida(String fechaStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechaStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private int obtenerIndice(String fechaStr, int tamanio) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(fechaStr, formatter);

        long numero = fecha.toEpochDay();

        return (int) (Math.abs(numero) % tamanio);
    }

}
