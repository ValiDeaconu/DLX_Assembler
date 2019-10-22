package InstructionBase;

import DataAccess.InstructionInfo;

import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

public class TernaryInstruction extends InstructionAbstract {

    public TernaryInstruction(InstructionInfo instructionInfo,
                              RegisterOperand dest,
                              OperandAbstract leftOperand,
                              OperandAbstract rightOperand,
                              int safeDepth) {
        super(instructionInfo, dest, safeDepth);
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
