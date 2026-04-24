package TPLabDist.tp2_rmi.basico.server_central;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazHoroscopo extends Remote {
    String obtenerPrediccion(String signo) throws RemoteException;
}