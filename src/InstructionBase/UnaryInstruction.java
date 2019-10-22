package InstructionBase;

import DataAccess.InstructionInfo;

import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

import java.util.List;

public class UnaryInstruction extends InstructionAbstract {

    public UnaryInstruction(InstructionInfo instructionInfo,
                            RegisterOperand destination,
                            int safeDepth) {
        super(instructionInfo, destination, safeDepth);
    }

    @Override
    public List<OperandAbstract> getOperandsAsList() {
        throw new IllegalCallerException("Unary Instructions does not have any operands.");
    }

    @Override
    public String toString() {
        return this.getInstructionInfo().toString() + " " + this.getDestination().toString();
    }

    @Override
    public String convertToBinaryCode() {
        // TODO: Implement this method
        return null;
    }
}
