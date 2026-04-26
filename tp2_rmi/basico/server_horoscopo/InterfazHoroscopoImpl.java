package TPLabDist.tp2_rmi.basico.server_horoscopo;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import TPLabDist.tp2_rmi.basico.server_central.InterfazHoroscopo;

public class InterfazHoroscopoImpl implements InterfazHoroscopo {

    private AtomicInteger contadorPeticiones;

    public InterfazHoroscopoImpl() {
        super();
        this.contadorPeticiones = new AtomicInteger(0);
    }

    @Override
    public String obtenerPrediccion(String signo) {
        Set<String> signosValidos = Set.of(
            "ARIES", "TAURO", "GEMINIS", "CANCER",
            "LEO", "VIRGO", "LIBRA", "ESCORPIO",
            "SAGITARIO", "CAPRICORNIO", "ACUARIO", "PISCIS"
        );

        String[] predicciones = {
            "Hoy es un buen día para tomar decisiones importantes.",
            "En el trabajo, la paciencia será tu mejor aliada.",
            "La salud es tu prioridad, cuida de ti mismo.",
            "Un encuentro inesperado traerá alegría a tu vida."
        }; 

        String respuesta;

        System.out.println("Servidor Horoscopo> Conexión entrante aceptada.");

        // contador de peticiones para identificar cada solicitud
        int numPeticiones = contadorPeticiones.incrementAndGet();
        
        // identificar el hilo que atiende la solicitud
        long idHilo = Thread.currentThread().getId();

        // 3. Imprimir el registro en la consola del servidor
        System.out.println("Petición #" + numPeticiones + " recibida. Atendida por el Hilo: " +  idHilo + " con signo=" + signo);

        signo = signo.toUpperCase();
                
        if (!signosValidos.contains(signo)) {
            respuesta = "Error:Signo zodiacal no válido";
            System.out.println("Servidor Horoscopo> Horóscopo enviado: Signo zodiacal no válido.");
        }else{
            respuesta = predicciones[new Random().nextInt(predicciones.length)];
            System.out.println("Servidor Horoscopo> Horóscopo enviado: \"" + respuesta + "\"");
        }
        return respuesta;
    }
    
}