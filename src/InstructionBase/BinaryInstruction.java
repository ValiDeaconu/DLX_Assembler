package InstructionBase;

import DataAccess.InstructionName;
import OperandBase.OperandAbstract;

public class BinaryInstruction extends InstructionAbstract {

    public BinaryInstruction(String instructionName, OperandAbstract destination, OperandAbstract operand) {
        super(instructionName, destination);
        this.operands.add(operand);
    }

    public BinaryInstruction(InstructionName instructionName, OperandAbstract destination, OperandAbstract operand) {
        super(instructionName, destination);
        this.operands.add(operand);
    }

    public OperandAbstract getOperand() {
        return this.operands.get(0);
    }

    @Override
    public String toString() {
        return this.getInstructionInfo().toString() + " " + this.getDestination() + ", " + this.getOperand();
    }
}
