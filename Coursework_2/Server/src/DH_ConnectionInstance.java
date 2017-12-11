import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DH_ConnectionInstance implements DH_MessageObject {


    private DH_Connection_Server current_connection;
    private Lock lock = new ReentrantLock();


    @Override
    public void open_connection() {
        lock.lock();
        if(current_connection != null) { Logger.Log("Clearing previous connection attempts."); current_connection = null; }
    }

    @Override
    public BigInteger[] suggest() {
        BigInteger p = BigInteger.probablePrime(256, new Random());
        BigInteger g = BigInteger.probablePrime(128, new Random());

        Logger.Log("Suggesting keypairs P = " + p + " and g = " + g);
        synchronized (lock){
            current_connection = new DH_Connection_Server(p,g);
        }
        return new BigInteger[]{p, g};
    }

    @Override
    public BigInteger swap_public(BigInteger foreign_key) throws RemoteException {
        Logger.Log("Receiving foreign key " + foreign_key + " from client.");
        synchronized (lock){
            current_connection.setForeign_key(foreign_key);
            Logger.Log("Sending public key " + current_connection.getPublicKey() + " back to client.");
            Logger.Log("Computing secret key...");
            current_connection.compute_secret_key();
            Logger.Log("Secret key : " + current_connection.getSecret_key());
            return current_connection.getPublicKey();
        }
    }

    @Override
    public byte[] send_message(byte[] s) {

        synchronized (lock) {
            byte[] secret_key_long = current_connection.getSecret_key().toByteArray();

            // The JDK has a default 128 bit key length restriction (16 bytes).
            // The key generated is upto 32 bytes, so we could use that
            // depends on whether its enabled on both machined iirc

            byte[] key = new byte[16];
            for (int i = 0; i < 16; i++) {
                key[i] = secret_key_long[i];
            }
            Key newkey = new SecretKeySpec(key, "AES");
            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.DECRYPT_MODE, newkey);
                String decrypted = new String(c.doFinal(s));
                Logger.Log(Logger.Level.MESSAGE, decrypted);

                // Now we've decrypted the text from the client, we can return the cipher text.
                String return_text = current_connection.getCipherText();
                // We need to re-encrypt the ciphertext though :)
                c.init(Cipher.ENCRYPT_MODE, newkey);
                byte[] encrypted = c.doFinal(return_text.getBytes());

                return encrypted;
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
            } finally {
                lock.unlock();
            }
        }
        return null;
    }
}
