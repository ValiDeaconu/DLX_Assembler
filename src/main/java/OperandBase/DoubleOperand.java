package OperandBase;

import java.util.Objects;

public class DoubleOperand extends OperandAbstract<Double> implements Comparable<DoubleOperand>  {
    private double value;

    public DoubleOperand(double value) {
        super(OperandType.DOUBLE);
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DoubleOperand{" +
                "value=" + value +
                '}';
    }

    @Override
    public String convertToBinaryCode() {
        // TODO: Cast floating point (double precision - 64 bits into 16 bits)
        long bits = Double.doubleToLongBits(value);
        return Long.toBinaryString(bits);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DoubleOperand other = (DoubleOperand) obj;
        return Double.compare(other.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(DoubleOperand other) {
        return Double.compare(value, other.value);
    }
}
