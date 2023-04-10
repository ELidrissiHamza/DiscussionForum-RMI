import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

public class ForumImpl extends UnicastRemoteObject implements Forum {
    int i;//id d'un client;
        String hostname;
        TreeMap<Integer,Proxy> ListClient;
        public ForumImpl() throws RemoteException {
            super();
            i=0;
            ListClient= new TreeMap<Integer,Proxy>();

            try{
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public String qui() throws RemoteException {
            dire(0,ListClient.keySet().toString());
            return "";
        }

        public int entrer(Proxy cb) throws RemoteException{
            i++;
            System.out.println("Client number "+i+" connected");
            cb.ecouter("client connected in "+hostname+ " with Id "+i);
            ListClient.put(i,cb);
            return i;
        }

        public void dire(int id, String msg) throws RemoteException{
            ListClient.forEach((k,v)->{
                try {
                    if(id==0)
                        v.ecouter("List of Clients Connected :  "+msg);
                    else
                    v.ecouter(id+":"+msg);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public void quitter(int id) throws RemoteException{
            Proxy cb;
            cb=ListClient.get(id);
            ListClient.remove(id);
            cb.ecouter("You are disconnected from "+hostname);
        }


        public static void main(String args[]) {
            try {
                Registry registry = LocateRegistry.createRegistry(1099);
                Forum server = new ForumImpl();
                String hostname = InetAddress.getLocalHost().getHostName();
                Naming.rebind("//"+hostname+"/IRCServer", server);
                System.out.println("Server listening on "+hostname+"...");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }