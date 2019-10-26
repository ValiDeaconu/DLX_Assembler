package InstructionBase;

import DataAccess.InstructionInfo;
import DataAccess.InstructionName;

import OperandBase.LabelOperand;
import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;
import OperandBase.UnsignedIntegerOperand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class InstructionAbstract implements Comparable<InstructionAbstract> {
    private InstructionInfo instructionInfo;
    private OperandAbstract dest;
    protected List<OperandAbstract> operands;

    public InstructionAbstract(String instructionName, OperandAbstract dest) {
        this.instructionInfo = InstructionInfo.getInstruction(instructionName);
        this.dest = dest;
        this.operands = new ArrayList<OperandAbstract>();
    }

    public InstructionAbstract(InstructionName instructionName, OperandAbstract dest) {
        this.instructionInfo = InstructionInfo.getInstruction(instructionName);
        this.dest = dest;
        this.operands = new ArrayList<OperandAbstract>();
    }

    public abstract String convertToBinaryCode();

    public InstructionInfo getInstructionInfo() {
        return instructionInfo;
    }

    public OperandAbstract getDestination() {
        return dest;
    }

    public List<OperandAbstract> getOperandsAsList() {
        return operands;
    }

    public OperandAbstract getSource() {
        return operands.get(0);
    }

    public int getParametersNumber() {
        return (dest != null) ? operands.size() + 1 : operands.size();
    }

    public boolean replaceLabel(int labelPosition) {
        if (operands == null) return false;
        if (dest instanceof LabelOperand) {
            dest = new UnsignedIntegerOperand(labelPosition);
            return true;
        }

        for (int i = 0; i < operands.size(); ++i) {
            if (operands.get(i) instanceof LabelOperand) {
                operands.remove(i);
                operands.add(new UnsignedIntegerOperand(labelPosition));
                return true;
            }
        }

        return false;
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
