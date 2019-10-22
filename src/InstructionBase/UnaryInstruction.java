package InstructionBase;

import DataAccess.InstructionInfo;

import DataAccess.InstructionName;
import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

import java.util.List;

public class UnaryInstruction extends InstructionAbstract {

    public UnaryInstruction(String instructionName, RegisterOperand destination) {
        super(instructionName, destination);
    }

    public UnaryInstruction(InstructionName instructionName, RegisterOperand destination) {
        super(instructionName, destination);
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
