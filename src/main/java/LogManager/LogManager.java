package LogManager;

import FileManager.LogWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogManager {
    private LogType logType;
    private LogWriter logWriter;

    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");;

    public LogManager(LogType logType) {
        this.logType = logType;

        try {
            this.logWriter = new LogWriter(logType.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean write(String message) {
        String logMessage = "[";
        logMessage += logType.getName();
        logMessage += ", ";
        logMessage += dateFormatter.format(calendar.getTime());
        logMessage += "] ";
        logMessage += message;
        logMessage += "\n";

        try {
            logWriter.write(logMessage);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
