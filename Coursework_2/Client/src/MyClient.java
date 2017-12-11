public class MyClient {

    public static void main(String[] args) {
        new MyClient(args);
    }


    public MyClient(String[] args){

            String hostname, username;

            if(args.length < 2){
                Logger.Log(Logger.Level.ERROR, "Couldn't parse arguments!");
                return;
            } else {
                hostname = args[0];
                username = args[1];
            }


            System.setProperty( "java.security.policy", "mypolicy" );
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }

            DH_Setup setup = new DH_Setup(hostname, 2020);
            DH_Connection_128bit c = setup.getConnection();
            c.send_message("Requesting ciphertext");

    }
}
