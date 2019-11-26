package InstructionBase;

import DataAccess.InstructionName;
import OperandBase.OperandAbstract;

import java.util.List;

public class SimpleInstruction extends InstructionAbstract {
    public SimpleInstruction(String instructionName) {
        super(instructionName, null);
    }

    public SimpleInstruction(InstructionName instructionName) {
        super(instructionName, null);
    }

    @Override
    public OperandAbstract getDestination() {
        throw new IllegalCallerException("Simple Instructions does not have any operands.");
    }

    @Override
    public String toString() {
        return this.getInstructionInfo().toString();
    }
}
