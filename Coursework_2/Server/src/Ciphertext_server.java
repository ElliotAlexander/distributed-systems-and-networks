import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Key;

public class Ciphertext_server {

    public String GetCiphertext(Integer privatekey, String username){
        try {
            Registry ctreg = LocateRegistry.getRegistry( "svm-tjn1f15-comp2207.ecs.soton.ac.uk", 12345 );
            CiphertextInterface ctstub = (CiphertextInterface) ctreg.lookup( "CiphertextProvider" );
            String s = ctstub.get(username, privatekey);
            ServerLogger.Log("Received ciphertext");
            ServerLogger.Log(s);
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}
