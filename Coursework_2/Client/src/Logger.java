
public interface Logger {

    public enum Level{INFO, WARNING, ERROR}


    public static void Log(Logger.Level l, String message){
        System.out.println("[" + l + "] " + message);
    }


    // Defaults to info
    public static void Log(String message){
        System.out.println("[INFO] " + message);
    }

}
