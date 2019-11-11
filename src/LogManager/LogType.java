package LogManager;

import LogManager.LogManager;

public class LogType {
    private static final String LOG_DIR = "logs/";

    private String name;
    private String fileName;

    private LogType(String name, String fileName) {
        this.name = name;
        this.fileName = LOG_DIR + fileName;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public static final LogType CodeParser = new LogType("CodeParser", "codeParser.log");
}
