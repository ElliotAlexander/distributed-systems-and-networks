import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class DH_ConnectionInstance implements DH_MessageObject {

    private HashMap<Integer, DH_Connection_Server> connections = new HashMap<>();
    private int next_uuid = 0;

    @Override
    public Integer open_connection() {
        ServerLogger.Log("Using UUID : " + (next_uuid +1));
        next_uuid = next_uuid + 1;
        return next_uuid;
    }

    @Override
    public BigInteger[] suggest(Integer uuid) {
        BigInteger p = BigInteger.probablePrime(256, new Random());
        BigInteger g = BigInteger.probablePrime(128, new Random());

        ServerLogger.Log("Suggesting keypairs P = " + p + " and g = " + g);
        DH_Connection_Server current_connection = new DH_Connection_Server(p,g);
        connections.put(uuid, current_connection);
        return new BigInteger[]{p, g};
}

    @Override
    public BigInteger swap_public(BigInteger foreign_key, Integer uuid) throws RemoteException {
        DH_Connection_Server current_connection = connections.get(uuid);
        ServerLogger.Log("Receiving foreign key " + foreign_key + " from client.");
        current_connection.setForeign_key(foreign_key);
        ServerLogger.Log("Sending public key " + current_connection.getPublicKey() + " back to client.");
        ServerLogger.Log("Computing secret key...");
        current_connection.compute_secret_key();
        ServerLogger.Log("Secret key : " + current_connection.getSecret_key());
        return current_connection.getPublicKey();
    }

    @Override
    public byte[] send_message(byte[] s, Integer uuid) {
            DH_Connection_Server current_connection = connections.get(uuid);
            byte[] secret_key_long = current_connection.getSecret_key().toByteArray();

            // The JDK has a default 128 bit key length restriction (16 bytes).
            // The key generated is upto 32 bytes, so we could use that
            // depends on whether its enabled on both machined iirc

            // The client will now pass us a username, which will be used to access the ecs server.

            byte[] key = new byte[16];
            for (int i = 0; i < 16; i++) {
                key[i] = secret_key_long[i];
            }
            Key newkey = new SecretKeySpec(key, "AES");
            try {
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.DECRYPT_MODE, newkey);
                String username = new String(c.doFinal(s));
                ServerLogger.Log(ServerLogger.Level.MESSAGE, "Username : " + username);

                // Now we've decrypted the text from the client, we can return the cipher text.
                String return_text = current_connection.getCipherText(username);
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
            }

        return null;
    }
}
