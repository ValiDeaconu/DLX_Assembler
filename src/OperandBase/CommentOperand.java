package OperandBase;

import java.util.Objects;

public class CommentOperand extends OperandAbstract<String> implements Comparable<CommentOperand> {
    private String value;

    public CommentOperand(String value) {
        super(OperandType.COMMENT);
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
        CommentOperand other = (CommentOperand) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(CommentOperand other) {
        return value.compareTo(other.value);
    }

}
