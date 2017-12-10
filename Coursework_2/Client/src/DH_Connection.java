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

public class DH_Connection {

    private final BigInteger public_key;
    private BigInteger secret_key;
    private BigInteger foreign_key;
    private Key shortened_secret_key;
    private final BigInteger p,g;
    private Long b;
    private final MessageObject stub;
    private Cipher c;

    public DH_Connection(BigInteger p, BigInteger g, MessageObject stub){
        this.stub = stub;
        this.p = p;
        this.g = g;

        this.b = Long.valueOf((new Random()).nextInt(100000));
        Logger.Log("Using b-value of " + b);
        BigInteger value = new BigInteger(String.valueOf(g));
        BigInteger gb = value.pow(b.intValue());
        Logger.Log("Generated g^a value of " + gb);
        BigInteger B = gb.mod(p);
        Logger.Log("Generated key : " + B);
        public_key = B;

    }

    public BigInteger getPublicKey(){return public_key;}

    public void setForeign_key(BigInteger foreign_key){
        this.foreign_key = foreign_key;
    }

    public void compute_secret_key() {
        secret_key = foreign_key.pow(b.intValue()).mod(p);

        // Setup outgoing messages at the same time.

        try {
            c = Cipher.getInstance("AES");
            byte[] fullkey = secret_key.toByteArray();
            byte[] shortkey = new byte[16];
            for(int i = 0; i < 16; i++){
                shortkey[i] = fullkey[i];
            }
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

    public void send_message(String s){
        byte[] outgoing = new byte[0];
        try {
            outgoing = s.getBytes("utf-8");
            byte[] encrypted = c.doFinal(outgoing);
            byte[] returnval = stub.send_message(encrypted);

            c.init(Cipher.DECRYPT_MODE, shortened_secret_key);
            String out = new String(c.doFinal(returnval));
            Logger.Log(Logger.Level.MESSAGE, out);
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
