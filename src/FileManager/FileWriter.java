package FileManager;

public abstract class FileWriter {
    private java.io.BufferedWriter bufferedWriter;
    private java.io.FileWriter fileWriter;

    public FileWriter(String fileName, String codeBlock) throws java.io.IOException {
        fileWriter = new java.io.FileWriter(fileName);
        bufferedWriter = new java.io.BufferedWriter(fileWriter);

        writeHeader();
        write(codeBlock);
        writeFooter();
    }

    public abstract void writeHeader() throws java.io.IOException;
    public abstract void writeFooter() throws java.io.IOException;

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
