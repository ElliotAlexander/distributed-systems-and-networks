import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Key;

public class Ciphertext {

    public String GetCiphertext(Integer privatekey){
        try {
            Registry ctreg = LocateRegistry.getRegistry( "svm-tjn1f15-comp2207.ecs.soton.ac.uk", 12345 );
            CiphertextInterface ctstub = (CiphertextInterface) ctreg.lookup( "CiphertextProvider" );
            String s = ctstub.get("ep1e16", privatekey);
            Logger.Log("Received ciphertext");
            Logger.Log(s);
            return s;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}