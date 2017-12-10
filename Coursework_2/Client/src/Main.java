import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {
        new Main(args);
    }


    public Main(String[] args){


            System.setProperty( "java.security.policy", "mypolicy" );
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            DH_Setup setup = new DH_Setup("127.0.0.1", 2020);
            DH_Connection c = setup.getConnection();
            c.send_message("What a cunt");
    }
}
