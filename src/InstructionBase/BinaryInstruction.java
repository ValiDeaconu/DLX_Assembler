package InstructionBase;

import OperandBase.OperandAbstract;

public class BinaryInstruction extends InstructionAbstract {

    public BinaryInstruction(InstructionName name, int safeDepth, OperandAbstract leftOperand, OperandAbstract rightOperand) {
        super(name, safeDepth);
        this.operands.add(leftOperand);
        this.operands.add(rightOperand);
    }

    public OperandAbstract getLeftOperand() {
        return this.operands.get(0);
    }
    public OperandAbstract getRightOperand() { return this.operands.get(1); }

    @Override
    public String toString() {
        return this.getInstructionName().toString() + " " +
                this.getLeftOperand().toString() + ", " +
                this.getRightOperand().toString();
    }

    @Override
    public String convertToBinaryCode() {
        return null;
    }
}
