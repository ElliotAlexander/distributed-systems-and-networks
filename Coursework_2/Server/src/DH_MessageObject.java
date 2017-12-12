import java.math.BigInteger;
        import java.rmi.Remote;
        import java.rmi.RemoteException;

public interface DH_MessageObject extends Remote {

    public Integer open_connection() throws RemoteException;

    public BigInteger[] suggest(Integer uuid) throws RemoteException;

    public BigInteger swap_public(BigInteger public_key, Integer uuid) throws RemoteException;

    public byte[] send_message(byte[] text, Integer uuid) throws RemoteException;
}
