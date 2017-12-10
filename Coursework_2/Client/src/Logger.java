import java.rmi.Remote;

public interface Logger extends Remote {

    public enum Level{INFO, WARNING, ERROR, MESSAGE}


    public static void Log(Logger.Level l, String message){
        System.out.println("[" + l + "] " + message);
    }


    // Defaults to info
    public static void Log(String message){
        System.out.println("[INFO] " + message);
    }

}
