package OperandBase;

import java.util.Objects;

public class FloatingOperand extends OperandAbstract<Double> implements Comparable<FloatingOperand> {
    private double value;

    protected FloatingOperand(OperandType operandType, double value) {
        super(operandType);
        this.value = value;
    }

    public FloatingOperand(double value) {
        super(OperandType.FLOAT);
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatingOperand{" +
                "value=" + value +
                '}';
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
