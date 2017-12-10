import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageObject extends Remote {

    public void open_connection() throws RemoteException;

    public BigInteger[] suggest() throws RemoteException;

    public BigInteger swap_public(BigInteger public_key) throws RemoteException;

    public void send_message(String s) throws RemoteException;
}
