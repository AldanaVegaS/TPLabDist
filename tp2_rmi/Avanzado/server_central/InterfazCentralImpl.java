package TPLabDist.tp2_rmi.Avanzado.server_central;

import TPLabDist.tp2_rmi.Config;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import TPLabDist.tp2_rmi.Avanzado.cliente.InterfazCentral;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.function.Function;

public class InterfazCentralImpl implements InterfazCentral {

    private AtomicInteger contadorPeticiones = new AtomicInteger(0);

    public static Cache<String, String> cache;


    public InterfazCentralImpl() {
        super();
        this.contadorPeticiones = new AtomicInteger(0);
        this.cache = Caffeine.newBuilder()
                .maximumSize(5) // máximo 5 elementos
                .expireAfterWrite(2, TimeUnit.SECONDS) // expira en 2 segundos
                .build();
    }

    @Override
    public String consultarPredicciones(String signo, String fecha) {
        System.out.println("Servidor Central> Solicitud recibida. ");
        

        int numPeticiones = contadorPeticiones.incrementAndGet();
        
        // identificar el hilo que atiende la solicitud
        long idHilo = Thread.currentThread().getId();

        // 3. Imprimir el registro en la consola del servidor
        System.out.println("Petición #" + numPeticiones + " recibida. Atendida por el Hilo: " +  idHilo + " con datos: Signo=" + signo + ", Fecha=" + fecha);
    
        String cacheKey = signo+"|"+fecha;

        try {

            String responseCache = cache.getIfPresent(cacheKey);

            if (responseCache != null) {
                //cache hit, se devuelve el resultado almacenado
                System.out.println("HiloServer " + idHilo + "> Resultado en caché para: " + cacheKey);
                return responseCache;
            }else {
                return responseCache = cache.get(cacheKey, new Function<String, String>() {

                public String apply(String key) {

                    System.out.println("HiloServer " + idHilo + "> Resultado no se encuentra en caché. Consultando servidores...");
                    
                    // actua como cliente del Servidor Horóscopo
                    try {
                        Registry regHoroscopo = LocateRegistry.getRegistry(Config.SERVER, Config.PORT_HOROSCOPO);
                        
                        // actua como cliente del Servidor Clima
                        Registry regClima = LocateRegistry.getRegistry(Config.SERVER, Config.PORT_CLIMA);
                        InterfazClima stubClima = (InterfazClima) regClima.lookup("ServicioClima");
                        String resultadoClima = stubClima.obtenerPronostico(fecha);
                        InterfazHoroscopo stubHoroscopo = (InterfazHoroscopo) regHoroscopo.lookup("ServicioHoroscopo");
                        String resultadoHoroscopo = stubHoroscopo.obtenerPrediccion(signo);

                        return "Horóscopo para " + signo + ": " + resultadoHoroscopo + " | Clima: " + resultadoClima;

                    } catch (Exception ex) {
                        System.getLogger(InterfazCentralImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        return "Error: No se pudieron obtener los datos completos.";
                    } 
                }
            });           
        }
            

        } catch (Exception e) {
            System.err.println("Servidor Central> Error al contactar a los servidores base: " + e.getMessage());
            return "Error: No se pudieron obtener los datos completos.";
        }
    }
}
