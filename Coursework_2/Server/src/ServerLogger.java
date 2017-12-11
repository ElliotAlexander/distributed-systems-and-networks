import java.rmi.Remote;

public class ServerLogger {

    public enum Level{INFO, WARNING, ERROR, MESSAGE}


    public static void Log(Level l, String message){
        System.out.println("[SERVER " + l + "] " + message);
    }


    // Defaults to info
    public static void Log(String message){
        System.out.println("[SERVER INFO] " + message);
    }

}
