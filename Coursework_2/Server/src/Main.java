import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {

    public static void main(String[] args) {
        new Main(args);
    }


    public Main(String[] args){

        int portnumber = 2020;

        if(args.length == 0){
            Logger.Log(Logger.Level.WARNING, "No port number entered! Defaulting to 7000");
        } else {
            try {
                Integer i = Integer.parseInt(args[0]);
                Logger.Log("Using port number " + i);
                portnumber = i;
            } catch (NumberFormatException e){
                Logger.Log(Logger.Level.WARNING, "Error parsing port number from [" + args[0] + "]. Defaulting to 7000.");
            }
        }

        System.setProperty( "java.security.policy", "mypolicy" );
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LocateRegistry.createRegistry(portnumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {

            Registry reg = LocateRegistry.getRegistry("127.0.0.1", portnumber);

            // Setup messageobject instance.
            ConnectionInstance dh_connection = new ConnectionInstance();
            MessageObject dh_request_stub = (MessageObject) UnicastRemoteObject.exportObject(dh_connection, portnumber);
            reg.rebind("OpenConnection", dh_request_stub);
            Logger.Log("Server running!");
        } catch (RemoteException e) {
            Logger.Log(Logger.Level.ERROR, "Failed to register! Is the port open / registry running?");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
