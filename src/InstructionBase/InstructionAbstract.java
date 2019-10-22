package InstructionBase;

import DataAccess.InstructionInfo;
import DataAccess.InstructionName;

import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class InstructionAbstract implements Comparable<InstructionAbstract> {
    private InstructionInfo instructionInfo;
    private RegisterOperand dest;
    protected List<OperandAbstract> operands;

    public InstructionAbstract(String instructionName, RegisterOperand dest) {
        this.instructionInfo = InstructionInfo.getInstruction(instructionName);
        this.dest = dest;
        this.operands = new ArrayList<OperandAbstract>();
    }

    public InstructionAbstract(InstructionName instructionName, RegisterOperand dest) {
        this.instructionInfo = InstructionInfo.getInstruction(instructionName);
        this.dest = dest;
        this.operands = new ArrayList<OperandAbstract>();
    }

    public abstract String convertToBinaryCode();

    public InstructionInfo getInstructionInfo() {
        return instructionInfo;
    }

    public RegisterOperand getDestination() {
        return dest;
    }

    public List<OperandAbstract> getOperandsAsList() {
        return operands;
    }

    public OperandAbstract getSource() {
        return operands.get(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InstructionAbstract other = (InstructionAbstract) obj;
        return instructionInfo == other.instructionInfo &&
                Objects.equals(operands, other.operands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructionInfo, operands);
    }

    @Override
    public int compareTo(InstructionAbstract other) {
        return instructionInfo.compareTo(other.instructionInfo);
    }
}
