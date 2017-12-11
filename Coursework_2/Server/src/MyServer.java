import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MyServer {

    public static void main(String[] args) {
        new MyServer(args);
    }


    public MyServer(String[] args){


        System.setProperty( "java.security.policy", "mypolicy" );
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        DH_ServerStart(2020);

    }


    private void DH_ServerStart(int portnumber){
        try {
            LocateRegistry.createRegistry(portnumber);

            Registry reg = LocateRegistry.getRegistry("127.0.0.1", portnumber);

            // Setup messageobject instance.
            DH_ConnectionInstance dh_connection = new DH_ConnectionInstance();
            DH_MessageObject dh_request_stub = (DH_MessageObject) UnicastRemoteObject.exportObject(dh_connection, portnumber);
            reg.rebind("OpenConnection", dh_request_stub);
            Logger.Log("Server running!");
        } catch (RemoteException e) {
            Logger.Log(Logger.Level.ERROR, "Failed to register! Is the port open / registry running?");
            e.printStackTrace();
        }
    }
}
