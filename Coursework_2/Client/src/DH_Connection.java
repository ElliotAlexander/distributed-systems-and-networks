import java.math.BigInteger;
import java.util.Random;

public class DH_Connection {

    private final BigInteger public_key;
    private BigInteger secret_key;
    private BigInteger foreign_key;
    private final BigInteger p,g;
    private Long b;

    public DH_Connection(BigInteger p, BigInteger g){

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

    public void compute_secret_key(){
        secret_key = (foreign_key.pow(b.intValue())).mod(p);
    }

    public BigInteger getSecret_key(){
        return secret_key;
    }

}
