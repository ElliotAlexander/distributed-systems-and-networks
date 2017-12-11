import java.math.BigInteger;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DH_Setup {

    private DH_Connection_128bit connection;

    // This method initiates DH key exchange. Once complete, a secure channel will have been setup.
    public DH_Setup(String hostname, int port){
        Registry reg = null;
        try {

            // Establiish a connection. Get the server-side stubs.
            // If no server is found, the program will exit at this point.
            reg = LocateRegistry.getRegistry(hostname, port);
            DH_MessageObject stub = (DH_MessageObject) reg.lookup("OpenConnection");
            stub.open_connection();
            Logger.Log("Connection established.");


            // Ask the server to suggest a pair of primes for DH key exchange.
            BigInteger[] pairs = stub.suggest();

            // Receive the primes, and open a new connection with them.
            Logger.Log("Receiving suggested pairs: p = " + pairs[0] + ", g = " + pairs[1]);
            DH_Connection_128bit connection = new DH_Connection_128bit(pairs[0], pairs[1], stub);

            // Once we've generated our public key, swap it with the servers.
            // Receive the servers public key (referred to as foreign key for simplicity).
            BigInteger foreign_key = stub.swap_public(connection.getPublicKey());
            Logger.Log("Sending public key " + connection.getPublicKey() + " to server.");
            Logger.Log("Receiving foreign key " + foreign_key + " back from client.");
            connection.setForeign_key(foreign_key);


            // once we've received the servers public key, we can build the secret key.
            // A secure channel has then been established.
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


    public DH_Connection_128bit getConnection(){
        return connection;
    }
}
