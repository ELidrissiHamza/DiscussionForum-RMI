import java.rmi.Remote;
import java.rmi.RemoteException;
public interface Proxy extends Remote {
    public void ecouter(String message) throws RemoteException;
}
