package comp2207.shout;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * The server mainline that generates and registers an instance of
 * the Shout service.
 * <p>Usage: java ShoutMainline
 * <p>where there is an rmiregistry listening at the default port, 1099
 * @author Tim Norman, University of Southampton
 * @version 3.0
 */

public class ShoutMainline
{
    public static void main(String args[]) {

	try {
	    // Generate the remote object(s) that will reside on this server.
            ShoutImpl serv = new ShoutImpl();
	    // Export the remote object so that it can receive remote calls.
	    ShoutInterface stub = (ShoutInterface) UnicastRemoteObject.exportObject( serv, 0 );

	    // Get a stub to the registry and try to register the remote object.
	    Registry reg = LocateRegistry.getRegistry();
            reg.rebind( "Shout", stub );

	    // Note the server will not shut down!
	}
	catch (java.rmi.RemoteException e) {
            System.err.println( "Failed to register; is the rmiregistry running?" );
	    System.err.println( e.getMessage() );
        }
    }
}
