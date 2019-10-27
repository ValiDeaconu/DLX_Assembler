package FileManager;

import java.io.IOException;

public class BinWriter extends FileWriter {
    public BinWriter(String fileName, String codeBlock) throws IOException {
        super(fileName, codeBlock);
    }

    @Override
    public void writeHeader() throws IOException { }

    @Override
    public void writeFooter() throws IOException { }
}
