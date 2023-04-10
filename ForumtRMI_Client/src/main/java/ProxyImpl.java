import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ProxyImpl extends UnicastRemoteObject implements Proxy {
    public User client;
    public ProxyImpl(User c) throws RemoteException {
        super();
        client = c;
        try{
            Registry registry = LocateRegistry.createRegistry(1100);
            //CallBack server = new CallBackImpl(client);
            String hostname = InetAddress.getLocalHost().getHostName();
            Naming.rebind("//"+hostname+"/IRCClient", this);
            System.out.println("Server listening on "+hostname+"...");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ecouter(String message) throws RemoteException{
        client.ecrire(message);
    }

    public static void main(String args[]) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}