package OperandBase;

import Util.BinaryConverter;

import java.util.Objects;

public class IntegerOperand extends OperandAbstract<Integer> implements Comparable<IntegerOperand> {
    private int value;

    public IntegerOperand(int value) {
        super(OperandType.INTEGER);

        if (value < -32768 || value > 32767)
            throw new IllegalArgumentException("Integer value must be between -32768 and 32767.");

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
    public String convertToBinaryCode() {
        return BinaryConverter.convertTo(Integer.toBinaryString(value), 16);
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
