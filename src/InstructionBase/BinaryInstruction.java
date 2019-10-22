package InstructionBase;

import DataAccess.InstructionInfo;

import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

public class BinaryInstruction extends InstructionAbstract {

    public BinaryInstruction(InstructionInfo instructionInfo,
                             RegisterOperand destination,
                             OperandAbstract operand,
                             int safeDepth) {
        super(instructionInfo, destination, safeDepth);
        this.operands.add(operand);
    }

    public OperandAbstract getOperand() {
        return this.operands.get(0);
    }

    @Override
    public String toString() {
        return this.getInstructionInfo().toString() + " " + this.getDestination() + ", " + this.getOperand();
    }

    @Override
    public String convertToBinaryCode() {
        return null;
    }
}
