package FileManager;

import java.io.IOException;

public final class BinWriter extends FileWriter {
    protected BinWriter() { super(); }

    @Override
    public void writeHeader() throws IOException { }

    @Override
    public void writeFooter() throws IOException { }
}
