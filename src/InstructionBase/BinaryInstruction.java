package InstructionBase;

import DataAccess.InstructionInfo;

import DataAccess.InstructionName;
import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

public class BinaryInstruction extends InstructionAbstract {

    public BinaryInstruction(String instructionName, RegisterOperand destination, OperandAbstract operand) {
        super(instructionName, destination);
        this.operands.add(operand);
    }

    public BinaryInstruction(InstructionName instructionName, RegisterOperand destination, OperandAbstract operand) {
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

    @Override
    public String convertToBinaryCode() {
        return null;
    }
}
