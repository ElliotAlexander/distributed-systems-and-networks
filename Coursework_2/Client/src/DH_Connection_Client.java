import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Random;



// note that the client on the end of this class name is to differentiate it from a similar class in the client.
public class DH_Connection_Client {

    /**
     *
     * This class acts as an object for am outgoing connection.
     * Wrapped in here are keys, stubs for DH_MessageObject, and methods to call on the connection.
     *  This class also handles encrypting and decrypting from the 128 bit DH keys.
     */


    private final BigInteger public_key;
    private BigInteger secret_key;
    private BigInteger foreign_key;
    private Key shortened_secret_key;
    private final BigInteger p,g;
    private Long b;
    private final DH_MessageObject stub;
    private Cipher c;
    private Integer uuid;

    // This method generates the public key.
    public DH_Connection_Client(BigInteger p, BigInteger g, DH_MessageObject stub, Integer uuid){
        this.stub = stub;
        this.p = p;
        this.g = g;
        this.uuid = uuid;

        this.b = Long.valueOf((new Random()).nextInt(100000));
        ClientLogger.Log("Using b-value of " + b);
        BigInteger value = new BigInteger(String.valueOf(g));
        BigInteger gb = value.pow(b.intValue());
        // this is really massive.
        //ClientLogger.Log("Generated g^a value of " + gb);
        BigInteger B = gb.mod(p);
        ClientLogger.Log("Generated key : " + B);
        public_key = B;

    }

    public BigInteger getPublicKey(){return public_key;}

    public void setForeign_key(BigInteger foreign_key){
        this.foreign_key = foreign_key;
    }


    // Generate the secret key, only to be called once a foreign key is set.
    public void compute_secret_key() {

        if(foreign_key == null){
            ClientLogger.Log(ClientLogger.Level.ERROR, "Compute secret key called before foreign key received! Exiting...");
            return;
        }

        secret_key = foreign_key.pow(b.intValue()).mod(p);

        // Setup outgoing messages at the same time.
        try {
            c = Cipher.getInstance("AES");


            // We take the first 16 bytes of the key (128 bits)
            // the key generated is somewhere between 30-35 bits.
            byte[] fullkey = secret_key.toByteArray();
            byte[] shortkey = new byte[16];
            for(int i = 0; i < 16; i++){
                shortkey[i] = fullkey[i];
            }

            // Setup our cipher ready to use.
            shortened_secret_key = new SecretKeySpec(shortkey, "AES");
            c.init(Cipher.ENCRYPT_MODE, shortened_secret_key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public BigInteger getSecret_key(){
        return secret_key;
    }


    // Send a message to the server, encrypted with the already defined secret keys.
    // This message also receives the return value from the server.
    // This is the CIPHERTEXT, which is sent to be decrypted as per the algorithm defined in the spec.
    public void send_message(String s){
        byte[] outgoing = new byte[0];
        try {
            // UTF8 for platform independence.
            outgoing = s.getBytes("utf-8");
            byte[] encrypted = c.doFinal(outgoing);

            // Save the return value from the server.
            byte[] returnval = stub.send_message(encrypted, uuid);

            // decrypt the return value with the secret key.
            c.init(Cipher.DECRYPT_MODE, shortened_secret_key);

            // Send the return value to be decrypted (again)
            String out = new String(c.doFinal(returnval));
            Ciphertext_client ci = new Ciphertext_client();
            ci.decode(out, secret_key);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
