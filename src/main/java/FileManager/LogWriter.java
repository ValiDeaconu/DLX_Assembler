package FileManager;

import java.io.IOException;

public class LogWriter {
    private java.io.BufferedWriter bufferedWriter;
    private java.io.FileWriter fileWriter;

    public LogWriter(String fileName) throws IOException {
        fileWriter = new java.io.FileWriter(fileName);
        bufferedWriter = new java.io.BufferedWriter(fileWriter);
    }

    public void write(String content) throws java.io.IOException {
        bufferedWriter.write(content);
    }

    public void close() throws java.io.IOException {
        if (bufferedWriter != null)
            bufferedWriter.close();

        if (fileWriter != null)
            fileWriter.close();
    }
}
