package InstructionBase;

public enum InstructionName {
    ADD ("ADD"),
    SUB ("SUB"),
    MUL ("MUL"),
    DIV ("DIV"),
    AND ("AND"),
    OR ("OR"),
    XOR ("XOR");

    private final String name;

    private InstructionName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
