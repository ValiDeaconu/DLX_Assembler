package OperandBase;

public abstract class OperandAbstract<E> {
    private OperandType type;

    public OperandAbstract(OperandType type) {
        this.type = type;
    }

    public OperandType getType() {
        return type;
    }

    public abstract E getValue();

    public abstract String toString();

    public abstract String convertToBinaryCode();
}
