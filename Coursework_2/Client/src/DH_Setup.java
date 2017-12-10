import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DH_Setup {

    private DH_Connection connection;

    public DH_Setup(String hostname, int port){
        Registry reg = null;
        try {
            reg = LocateRegistry.getRegistry("127.0.0.1", 2020);
            System.out.println("Connection established.");
            MessageObject stub = (MessageObject) reg.lookup("OpenConnection");
            stub.open_connection();

            Logger.Log("Connection established.");
            BigInteger[] pairs = stub.suggest();
            Logger.Log("Receiving suggested pairs: p = " + pairs[0] + ", g = " + pairs[1]);
            DH_Connection connection = new DH_Connection(pairs[0], pairs[1], stub);
            BigInteger foreign_key = stub.swap_public(connection.getPublicKey());
            Logger.Log("Sending public key " + connection.getPublicKey() + " to server.");
            Logger.Log("Receiving foreign key " + foreign_key + " back from client.");
            connection.setForeign_key(foreign_key);
            connection.compute_secret_key();
            Logger.Log("Computing secret key...");
            Logger.Log("Secret key : " + connection.getSecret_key());
            this.connection = connection;
            Logger.Log("Secure connection established.");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


    public DH_Connection getConnection(){
        return connection;
    }
}
