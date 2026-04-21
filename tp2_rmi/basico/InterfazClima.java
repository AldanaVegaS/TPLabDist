import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazClima extends Remote {
    String obtenerPronostico(String fecha) throws RemoteException;
}