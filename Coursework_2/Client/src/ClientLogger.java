public class ClientLogger {

    public enum Level{INFO, WARNING, ERROR, MESSAGE}


    public static void Log(ClientLogger.Level l, String message){
        System.out.println("[CLIENT " + l + "] " + message);
    }


    // Defaults to info
    public static void Log(String message){
        System.out.println("[CLIENT INFO] " + message);
    }

}
