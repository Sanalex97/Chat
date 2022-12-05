import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class Logger {
    protected int num = 1;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    protected static Logger logger;

    private Logger() {
    }

    public static Logger getInstance() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }

    public void log(String msg) {
        Date date = new Date(System.currentTimeMillis());

        try (PrintWriter writer = new PrintWriter(new FileWriter("Logger/src/main/resources/logger.txt", true), true)) {

            writer.println("[" + formatter.format(date) + " " + num++ + "] " + msg);


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
