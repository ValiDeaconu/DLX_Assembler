package InstructionBase;

import OperandBase.OperandAbstract;

import java.util.List;
import java.util.Objects;

public abstract class InstructionAbstract implements Comparable<InstructionAbstract> {
    private InstructionType type;
    private InstructionName name;
    protected List<OperandAbstract> operands;

    private int safeDepth;

    public InstructionAbstract(InstructionName name, int safeDepth) {
        this.name = name;
        this.type = generateInsturctionType(name);

        if (safeDepth < 0)
            throw new IllegalArgumentException("safeDepth cannot be negative");

        this.safeDepth = safeDepth;
    }

    public abstract String convertToBinaryCode();

    private InstructionType generateInsturctionType(InstructionName name) {
        // TODO: Complete this switch when InstructionName enum is complete
        return InstructionType.R_TYPE;
    }

    public InstructionType getInstructionType() {
        return type;
    }

    public InstructionName getInstructionName() {
        return name;
    }

    public int getSafeDepth() {
        return safeDepth;
    }

    public List<OperandAbstract> getOperandsAsList() {
        return operands;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InstructionAbstract other = (InstructionAbstract) obj;
        return safeDepth == other.safeDepth &&
                type == other.type &&
                name == other.name &&
                Objects.equals(operands, other.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, operands, safeDepth);
    }

    @Override
    public int compareTo(InstructionAbstract other) {
        return Integer.compare(safeDepth, other.safeDepth);
    }
}
