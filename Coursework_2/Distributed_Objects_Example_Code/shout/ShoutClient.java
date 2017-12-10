package comp2207.shout;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

/**
 * A client that invokes the SHOUT service provided.
 * <p>Usage: java ShoutClient host
 * <p>where host is the machine the remote object is running on
 * @author Tim Norman, University of Southampton
 * @version 3.0
 */

public class ShoutClient
{
    public static void main(String[] args)
    {
	if (args.length < 1) {
	    System.err.println( "Usage:\njava ShoutClient <host>" ) ;
	    return;
	}
	String host = args[0];
        try {
	    // Get a stub for the registry on <host>
	    Registry reg = LocateRegistry.getRegistry( host );
	    // Lookup the remote object we want and get a stub
            ShoutInterface stub = (ShoutInterface) reg.lookup( "Shout" );

	    // Prompt the user for a message to send to the server.
            BufferedReader in = new BufferedReader(
                new InputStreamReader( System.in ));
            System.out.print( "Enter message: " );

	    // Invoke the server and print out the result.
            System.out.println( stub.shout( in.readLine() ) );
        }
	catch (java.io.IOException e) {
            System.err.println( "I/O error." );
	    System.err.println( e.getMessage() );
        }
	catch (java.rmi.NotBoundException e) {
            System.err.println( "Server not bound." );
	    System.err.println( e.getMessage() );
        }
    }
}
