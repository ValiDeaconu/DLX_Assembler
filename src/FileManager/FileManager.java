package FileManager;

public final class FileManager {
    private static BinWriter binWriterSingleton;
    public static BinWriter getBinWriter() {
        if (binWriterSingleton == null)
            binWriterSingleton = new BinWriter();

        return binWriterSingleton;
    }

    private static VhdlWriter vhdlWriterSingleton;
    public static VhdlWriter getVhdlWriter() {
        if (vhdlWriterSingleton == null)
            vhdlWriterSingleton = new VhdlWriter();

        return vhdlWriterSingleton;
    }
}
