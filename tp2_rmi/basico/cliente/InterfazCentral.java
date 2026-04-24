package TPLabDist.tp2_rmi.basico.cliente;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazCentral extends Remote {
    String consultarPredicciones(String signo, String fecha) throws RemoteException;
}