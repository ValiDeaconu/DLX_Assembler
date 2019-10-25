package OperandBase;

import java.util.Objects;

public class LabelOperand extends OperandAbstract<String> implements Comparable<LabelOperand> {
    private String value;

    public LabelOperand(String value) {
        super(OperandType.LABEL);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LabelOperand other = (LabelOperand) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(LabelOperand other) {
        return value.compareTo(other.value);
    }
}
