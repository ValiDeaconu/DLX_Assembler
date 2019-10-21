package OperandBase;

import java.util.Objects;

public class IntegerOperand extends OperandAbstract<Integer> implements Comparable<IntegerOperand> {
    private int value;

    public IntegerOperand(int value) {
        super(OperandType.INTEGER);
        this.value = value;
    }

    protected IntegerOperand(OperandType operandType, int value) {
        super(operandType);
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IntegerOperand other = (IntegerOperand) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(IntegerOperand other) {
        return Integer.compare(value, other.value);
    }
}
