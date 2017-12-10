import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ConnectionInstance implements MessageObject {


    private DH_Connection current_connection;

    @Override
    public void open_connection() {
        Logger.Log("Attempting to open new connection.");
        if(current_connection != null) { Logger.Log("Clearing previous connection attempts."); current_connection = null; }
    }

    @Override
    public BigInteger[] suggest() {
        BigInteger p = BigInteger.probablePrime(512, new Random());
        BigInteger g = BigInteger.probablePrime(50, new Random());

        Logger.Log("Suggesting keypairs P = " + p + " and g = " + g);
        current_connection = new DH_Connection(p,g);
        return new BigInteger[]{p, g};
    }

    @Override
    public BigInteger swap_public(BigInteger foreign_key) throws RemoteException {
        Logger.Log("Receiving foreign key " + foreign_key + " from client.");
        current_connection.setForeign_key(foreign_key);
        Logger.Log("Sending public key " + current_connection.getPublicKey() + " back to client.");
        Logger.Log("Computing secret key...");
        current_connection.compute_secret_key();
        Logger.Log("Secret key : " + current_connection.getSecret_key());
        return current_connection.getPublicKey();
    }

    @Override
    public void send_message(String s) {
        try {
            Cipher c = Cipher.getInstance("AES/ECB/PCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
}
