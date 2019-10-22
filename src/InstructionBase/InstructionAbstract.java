package InstructionBase;

import DataAccess.InstructionInfo;
import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class InstructionAbstract implements Comparable<InstructionAbstract> {
    private InstructionInfo instructionInfo;
    private RegisterOperand dest;
    protected List<OperandAbstract> operands;

    private int safeDepth;

    public InstructionAbstract(InstructionInfo instructionInfo, RegisterOperand dest, int safeDepth) {
        this.instructionInfo = instructionInfo;
        this.dest = dest;
        this.operands = new ArrayList<OperandAbstract>();

        if (safeDepth < 0)
            throw new IllegalArgumentException("safeDepth cannot be negative");

        this.safeDepth = safeDepth;
    }

    public abstract String convertToBinaryCode();

    public InstructionInfo getInstructionInfo() {
        return instructionInfo;
    }

    public int getSafeDepth() {
        return safeDepth;
    }

    public RegisterOperand getDestination() { return dest; }

    public List<OperandAbstract> getOperandsAsList() {
        return operands;
    }

    public OperandAbstract getSource() { return operands.get(0); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InstructionAbstract other = (InstructionAbstract) obj;
        return safeDepth == other.safeDepth &&
                instructionInfo == other.instructionInfo &&
                Objects.equals(operands, other.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructionInfo, operands, safeDepth);
    }

    @Override
    public int compareTo(InstructionAbstract other) {
        return Integer.compare(safeDepth, other.safeDepth);
    }
}
