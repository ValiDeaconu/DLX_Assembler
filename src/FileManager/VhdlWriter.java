package FileManager;

import java.io.IOException;

// TODO: Inspect DLX processor architecture and complete this class' writeHeader() and writeFooter() methods
public class VhdlWriter extends FileWriter {
    public VhdlWriter(String fileName, String codeBlock) throws IOException {
        super(fileName, codeBlock);
    }

    @Override
    public void writeHeader() throws IOException {
        write("-- DLX Assembler auto generated VHDL code for IR");
    }

    @Override
    public void writeFooter() throws IOException {
        write("-- End of auto generated VHDL code");
    }
}
