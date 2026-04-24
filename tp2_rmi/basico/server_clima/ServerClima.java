package TPLabDist.tp2_rmi.basico.server_clima;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import TPLabDist.tp2_rmi.Config;
import TPLabDist.tp2_rmi.basico.server_central.InterfazClima;

public class ServerClima {
    
    public static void main(String[] args) {
        try {
            InterfazClimaImpl obj = new InterfazClimaImpl();
            InterfazClima stub = (InterfazClima) UnicastRemoteObject.exportObject(obj, 0);
            
            // registrar el stub en el registro RMI
            Registry registry = LocateRegistry.createRegistry(Config.PORT_CLIMA);
            registry.rebind("ServicioClima", stub);
            System.out.println("Servidor Clima listo en el puerto " + Config.PORT_CLIMA + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
