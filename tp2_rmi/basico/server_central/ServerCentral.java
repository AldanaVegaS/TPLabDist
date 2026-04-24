package TPLabDist.tp2_rmi.basico.server_central;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import TPLabDist.tp2_rmi.Config;
import TPLabDist.tp2_rmi.basico.cliente.InterfazCentral;

public class ServerCentral {

    public static void main(String[] args) {
        try {
            InterfazCentralImpl obj = new InterfazCentralImpl();
            InterfazCentral stub = (InterfazCentral) UnicastRemoteObject.exportObject(obj, 0);
            
            // registrar el stub en el registro RMI
            Registry registry = LocateRegistry.createRegistry(Config.PORT);
            registry.rebind("ServicioCentral", stub);
            System.out.println("Servidor Central listo en el puerto " + Config.PORT + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}