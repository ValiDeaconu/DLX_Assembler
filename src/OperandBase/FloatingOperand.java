package OperandBase;

import java.util.Objects;

public class FloatingOperand extends OperandAbstract<Float> implements Comparable<FloatingOperand> {
    private float value;

    public FloatingOperand(float value) {
        super(OperandType.FLOAT);
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatingOperand{" +
                "value=" + value +
                '}';
    }

    @Override
    public String convertToBinaryCode() {
        // TODO: Cast floating point (single precision - 32 bits into 16 bits)
        int bits = Float.floatToIntBits(value);
        return Integer.toBinaryString(bits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FloatingOperand other = (FloatingOperand) obj;
        return Double.compare(other.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(FloatingOperand other) {
        return Double.compare(value, other.value);
    }
}
