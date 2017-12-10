package comp2207.shout;

import java.rmi.RemoteException;

/**
 * Converts messages to uppercase.
 * @see ShoutInterface
 * @author Tim Norman, University of Southampton
 * @version 3.0
 */

public class ShoutImpl
    implements ShoutInterface
{
    /**
     * The constructor.  Nothing to be done here.
     */
    public ShoutImpl()
	throws RemoteException
    {
    }
    
    /**
     * Convert the message to uppercase.
     */
    public String shout( String s )
	throws RemoteException
    {
        return s.toUpperCase();
    }
}
