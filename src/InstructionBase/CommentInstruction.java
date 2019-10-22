package InstructionBase;

import DataAccess.InstructionInfo;

import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;

import java.util.List;

public class CommentInstruction extends InstructionAbstract {
    private String content;

    public CommentInstruction(String content) {
        super(InstructionInfo.COMMENT_INSTRUCTION_STRING_NAME, null);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public RegisterOperand getDestination() {
        throw new IllegalCallerException("Comments does not have a destination register");
    }

    @Override
    public OperandAbstract getSource() {
        throw new IllegalCallerException("Comments does not have a source register");
    }

    @Override
    public List<OperandAbstract> getOperandsAsList() {
        throw new IllegalCallerException("Comments does not have operands");
    }

    /** Comments should not be converted in binary code */
    @Override
    public String convertToBinaryCode() {
        return "";
    }
}
