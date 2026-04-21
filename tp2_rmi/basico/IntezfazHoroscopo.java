import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntezfazHoroscopo extends Remote {
    String obtenerPrediccion(String signo) throws RemoteException;
}