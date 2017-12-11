import java.math.BigInteger;
import java.util.Random;


// Note that the server on the end of this classname is to differentiate it from a similar class in the client,
public class DH_Connection_Server {

    private final BigInteger public_key;
    private BigInteger secret_key;
    private BigInteger foreign_key;
    private final BigInteger p,g;
    private Long a;

    public DH_Connection_Server(BigInteger p, BigInteger g){

        this.p = p;
        this.g = g;

        this.a = Long.valueOf((new Random()).nextInt(100000));
        Logger.Log("Using a-value of " + a);
        BigInteger value = new BigInteger(String.valueOf(g));
        BigInteger ga = value.pow(a.intValue());
        Logger.Log("Generated g^a value of " + ga);
        BigInteger B = ga.mod(p);
        Logger.Log("Generated key : " + B);
        public_key = B;

    }

    public BigInteger getPublicKey(){return public_key;}

    public void setForeign_key(BigInteger foreign_key){
        this.foreign_key = foreign_key;
    }

    public void compute_secret_key(){
        secret_key = (foreign_key.pow(a.intValue())).mod(p);
    }

    public BigInteger getSecret_key(){
        return secret_key;
    }

    public String getCipherText(){
        int private_key_int = ((secret_key.toByteArray()[0] & 0xff) << 8) | (secret_key.toByteArray()[1] & 0xff);
        Logger.Log("Using ECS-Server-side private key " + private_key_int);
        Ciphertext_server ci = new Ciphertext_server();
        Logger.Log("Returning Ciphertext_server to client.");
        return ci.GetCiphertext(private_key_int);
    }

}
