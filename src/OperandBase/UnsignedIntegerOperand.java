package OperandBase;

import Util.BinaryConverter;

import java.util.Objects;

public class UnsignedIntegerOperand extends OperandAbstract<Integer> implements Comparable<UnsignedIntegerOperand> {
    private int value;

    private boolean requires26bits;

    public UnsignedIntegerOperand(int value) {
        super(OperandType.UNSIGNED_INTEGER);

        if (value < 0 || value > 67108863)
            throw new IllegalArgumentException("Unsigned Integer value must be between 0 and 67108863.");

        this.requires26bits = (value < 65535) ? false : true;

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
        final int CAST_INDEX = (requires26bits) ? 26 : 16;
        return BinaryConverter.convertTo(Integer.toBinaryString(value), CAST_INDEX);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UnsignedIntegerOperand other = (UnsignedIntegerOperand) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(UnsignedIntegerOperand other) {
        return Integer.compare(value, other.value);
    }
}
