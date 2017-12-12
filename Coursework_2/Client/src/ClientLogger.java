public class ClientLogger {

    public enum Level{INFO, WARNING, ERROR, MESSAGE}
    public static Integer uuid = -1;


    public static void Log(ClientLogger.Level l, String message){
        if (uuid == -1) {
            System.out.println("[CLIENT " + l + "] " + message);
        } else {
            System.out.println("[CLIENT " + uuid + " " + l + "] " + message);
        }
    }


    // Defaults to info
    public static void Log(String message){
        Log(Level.INFO, message);
    }

    public static void set_uuid(Integer i){
        uuid = i;
    }

}
