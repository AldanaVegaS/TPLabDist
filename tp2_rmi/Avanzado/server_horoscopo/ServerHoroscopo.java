package TPLabDist.tp2_rmi.Avanzado.server_horoscopo;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import TPLabDist.tp2_rmi.Config;
import TPLabDist.tp2_rmi.Avanzado.server_central.InterfazHoroscopo;

public class ServerHoroscopo {
    
    public static void main(String[] args) {
        try {
            InterfazHoroscopoImpl obj = new InterfazHoroscopoImpl();
            InterfazHoroscopo stub = (InterfazHoroscopo) UnicastRemoteObject.exportObject(obj, 0);
            
            // Usamos el puerto 1101 para el Horóscopo
            Registry registry = LocateRegistry.createRegistry(Config.PORT_HOROSCOPO);
            registry.rebind("ServicioHoroscopo", stub);
            System.out.println("Servidor Horóscopo listo en el puerto " + Config.PORT_HOROSCOPO + "...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}