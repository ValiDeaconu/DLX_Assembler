package InstructionBase;

import OperandBase.OperandAbstract;

public class UnaryInstruction extends InstructionAbstract {

    public UnaryInstruction(InstructionName name, int safeDepth, OperandAbstract operand) {
        super(name, safeDepth);
        this.operands.add(operand);
    }

    public OperandAbstract getOperand() {
        return this.operands.get(0);
    }

    @Override
    public String toString() {
        return this.getInstructionName().toString() + " " +
                this.getOperand().toString();
    }

    @Override
    public String convertToBinaryCode() {
        // TODO: Implement this method
        return null;
    }
}
