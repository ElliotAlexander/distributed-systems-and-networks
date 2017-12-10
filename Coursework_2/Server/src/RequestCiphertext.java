import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Key;

public class RequestCiphertext {

    public RequestCiphertext(Key privatekey){

        try {
            Registry ctreg = LocateRegistry.getRegistry( "svm-tjn1f15-comp2207.ecs.soton.ac.uk", 12345 );
            CiphertextInterface ctstub = (CiphertextInterface) ctreg.lookup( "CiphertextProvider" );
            String s = ctstub.get("ep1e16", 100);
            Logger.Log("Received ciphertext");

            int privatekeyint = privatekey.

            return;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }


}
