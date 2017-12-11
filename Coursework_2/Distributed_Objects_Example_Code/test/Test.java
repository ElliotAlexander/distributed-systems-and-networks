import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * @author Tim Norman, University of Southampton
 * @version 3.0
 */

public class Test
{
    public static void main(String args[]) {

	try {
	    System.setProperty( "java.security.policy", "mypolicy" );
	    if (System.getSecurityManager() == null) {
		System.setSecurityManager(new SecurityManager());
	    }
	    Registry ctreg = LocateRegistry.getRegistry( "svm-tjn1f15-comp2207.ecs.soton.ac.uk" );
	    CiphertextInterface ctstub = (CiphertextInterface) ctreg.lookup( "CiphertextProvider" );
	    System.out.println( ctstub.get( "tim", 23 ) );
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
