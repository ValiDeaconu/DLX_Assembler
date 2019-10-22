package InstructionBase;

import DataAccess.InstructionInfo;

import DataAccess.InstructionName;
import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

public class TernaryInstruction extends InstructionAbstract {

    public TernaryInstruction(String instructionName, RegisterOperand dest, OperandAbstract leftOperand, OperandAbstract rightOperand) {
        super(instructionName, dest);
        this.operands.add(leftOperand);
        this.operands.add(rightOperand);
    }

    public TernaryInstruction(InstructionName instructionName, RegisterOperand dest, OperandAbstract leftOperand, OperandAbstract rightOperand) {
        super(instructionName, dest);
        this.operands.add(leftOperand);
        this.operands.add(rightOperand);
    }


    public OperandAbstract getLeftOperand() {
        return this.operands.get(0);
    }

    public OperandAbstract getRightOperand() {
        return this.operands.get(1);
    }

    @Override
    public String toString() {
        return this.getInstructionInfo().toString() + " " +
                this.getDestination() + ", " +
                this.getLeftOperand() + ", " +
                this.getRightOperand();
    }

    @Override
    public String convertToBinaryCode() {
        return null;
    }
}
