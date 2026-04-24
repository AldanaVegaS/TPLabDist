package TPLabDist.tp2_rmi.basico.server_central;

import TPLabDist.tp2_rmi.Config;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.atomic.AtomicInteger;

import TPLabDist.tp2_rmi.basico.cliente.InterfazCentral;

public class InterfazCentralImpl implements InterfazCentral {

    private AtomicInteger contadorPeticiones = new AtomicInteger(0);

    public InterfazCentralImpl() {
        super();
        this.contadorPeticiones = new AtomicInteger(0);
    }

    @Override
    public String consultarPredicciones(String signo, String fecha) {
        System.out.println("Servidor Central> Solicitud recibida. ");
        String resultadoHoroscopo = "";
        String resultadoClima = "";

        int numPeticiones = contadorPeticiones.incrementAndGet();
        
        // identificar el hilo que atiende la solicitud
        long idHilo = Thread.currentThread().getId();

        // 3. Imprimir el registro en la consola del servidor
        System.out.println("Petición #" + numPeticiones + " recibida. Atendida por el Hilo: " +  idHilo + " con datos: Signo=" + signo + ", Fecha=" + fecha);
    
        try {
            // actua como cliente del Servidor Horóscopo
            Registry regHoroscopo = LocateRegistry.getRegistry(Config.SERVER, Config.PORT_HOROSCOPO);
            InterfazHoroscopo stubHoroscopo = (InterfazHoroscopo) regHoroscopo.lookup("ServicioHoroscopo");
            resultadoHoroscopo = stubHoroscopo.obtenerPrediccion(signo);

            // actua como cliente del Servidor Clima
            Registry regClima = LocateRegistry.getRegistry(Config.SERVER, Config.PORT_CLIMA);
            InterfazClima stubClima = (InterfazClima) regClima.lookup("ServicioClima");
            resultadoClima = stubClima.obtenerPronostico(fecha);

        } catch (Exception e) {
            System.err.println("Servidor Central> Error al contactar a los servidores base: " + e.getMessage());
            return "Error: No se pudieron obtener los datos completos.";
        }

        // 3. Ensamblar la respuesta final
        return "Horóscopo: " + resultadoHoroscopo + " | Clima: " + resultadoClima;
    }
}
