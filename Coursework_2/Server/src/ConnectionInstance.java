import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.Key;
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
        BigInteger p = BigInteger.probablePrime(256, new Random());
        BigInteger g = BigInteger.probablePrime(128, new Random());

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
    public byte[] send_message(byte[] s) {

        byte[] secret_key_long = current_connection.getSecret_key().toByteArray();

        // The JDK has a default 128 bit key length restriction (16 bytes).
        // The key generated is upto 32 bytes, so we could use that
        // depends on whether its enabled on both machined iirc

        byte[] key = new byte[16];
        for(int i = 0; i < 16; i++){
            key[i] = secret_key_long[i];
        }
        Key newkey = new SecretKeySpec(key, "AES");
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, newkey);
            String decrypted = new String(c.doFinal(s));
            Logger.Log(Logger.Level.MESSAGE, decrypted);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return "HelloWorld".getBytes();
    }
}
