package FileManager;

import java.io.IOException;

// TODO: Inspect DLX processor architecture and complete this class' writeHeader() and writeFooter() methods
public final class VhdlWriter extends FileWriter {
    protected VhdlWriter() { super(); }

    @Override
    public void writeHeader() throws IOException {
        write("-- DLX Assembler auto generated VHDL code for IR");
        write("\n"); // header shall end with a new line
    }

    @Override
    public void writeFooter() throws IOException {
        write("\n"); // footer shall start with a new line
        write("-- End of auto generated VHDL code");
    }
}
