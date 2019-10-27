package CodeReflection;

import DataAccess.DataAccessConstants;
import DataAccess.InstructionInfo;
import DataAccess.InstructionType;
import InstructionBase.*;
import OperandBase.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeParser implements Runnable {
    private CodeParserState state;
    private InstructionList instructionList;
    private String codeBlock;
    private Map<Integer, String> instructionIdRequestLabelMap;
    private Map<String, Integer> labelToInstructionIdMap;

    public CodeParser(String codeBlock) {
        this.codeBlock = codeBlock;
        this.state = CodeParserState.IDLE;
        this.instructionList = null;
        this.instructionIdRequestLabelMap = null;
        this.labelToInstructionIdMap = null;
    }

    public CodeParserState getState() {
        return state;
    }

    public InstructionList getInstructionList() {
        switch (state) {
            case IDLE:
            case SUCCEEDED:
                return instructionList;
            case WORKING:
            case FAILED:
                throw new IllegalThreadStateException("Instruction list is not ready to return");
        }

        throw new IllegalThreadStateException("Parser state is unknown");
    }

    @Override
    public void run() {
        state = CodeParserState.WORKING;
        instructionList = new InstructionList();
        instructionIdRequestLabelMap = new HashMap<>();
        labelToInstructionIdMap = new HashMap<>();
        if (parse(codeBlock)) {
            state = CodeParserState.SUCCEEDED;
        } else {
            state = CodeParserState.FAILED;
        }
    }

    public boolean parse(String assemblyCode) {
        String[] lines = assemblyCode.split(DataAccessConstants.INSTRUCTION_SEPARATOR);
        for (int i = 0; i < lines.length; ++i) {
            String line = lines[i];

            ReturnInfo<LineParserMessages> returnInfo = parseLine(line, i);

            if (returnInfo.errors.size() > 0) {
                return false;
            }

            if (!returnInfo.messages.contains(LineParserMessages.CINSTR))
                instructionList.add(returnInfo.instruction);
        }

        int totalLabelReplaced = 0;
        // check labels
        for (Map.Entry<Integer, String> entry : instructionIdRequestLabelMap.entrySet()) {
            int requesterId = entry.getKey();
            String requestedLabel = entry.getValue();
            if (labelToInstructionIdMap.containsKey(requestedLabel)) {
                int labelPosition = labelToInstructionIdMap.get(requestedLabel);

                boolean status = instructionList.get(requesterId).replaceLabel(labelPosition);

                if (!status) {
                    notifyError("Wrong label request", requesterId);
                    return false;
                } else {
                    totalLabelReplaced++;
                }
            } else {
                notifyError("Unknown label (\"" + requestedLabel + "\")", requesterId);
                return false;
            }
        }

        if (totalLabelReplaced != instructionIdRequestLabelMap.entrySet().size()) {
            notifyError("Not all labels were replaced", 0);
            return false;
        }

        return true;
    }

    // TODO: Announce all observers that CodeParser catched an error
    private void notifyError(String message, int lineIndex) {
        System.out.println("Error found: " + message + ", at line=" + lineIndex);
    }

    // TODO: Announce all observers that CodeParser found a comment
    private void notifyComment(String comment, int lineIndex) {
        System.out.println("Comment found: " + comment + ", at line=" + lineIndex);
    }

    private ReturnInfo<LineParserMessages> parseLine(String line, int lineIndex) {
        ReturnInfo<LineParserMessages> ret = new ReturnInfo<>();

        String compressedLine = compressString(line);
        if (compressedLine.startsWith(DataAccessConstants.COMMENT_SEPARATOR)) {
            notifyComment(line.substring(line.indexOf(";") + 1), lineIndex);
            ret.addMessage(LineParserMessages.CINSTR);
            return ret;
        }

        String[] terms = line.split(DataAccessConstants.INSTRUCTION_NAME_SEPARATOR);

        String aux = terms[0];
        String command;
        String[] operands;


        if (aux.contains(DataAccessConstants.LABEL_SEPARATOR)) {
            String label;
            // there are 2 cases: label:instr and label: instr
            if (aux.endsWith(DataAccessConstants.LABEL_SEPARATOR)) {
                // label: instr, it means terms[1] = instructionName, terms[2] = operands
                label = aux.substring(0, aux.length() - 1);
                command = terms[1];

                String rest = terms[2];
                for (int i = 3; i < terms.length; ++i)
                    rest = rest + terms[i];

                String compressed = compressString(rest);
                operands = compressed.split(DataAccessConstants.OPERAND_SEPARATOR);
            } else {
                // label:instr
                String[] temp = terms[0].split(DataAccessConstants.LABEL_SEPARATOR);
                label = temp[0];
                command = temp[1];

                String rest = terms[1];
                for (int i = 2; i < terms.length; ++i)
                    rest = rest + terms[i];
                String compressed = compressString(rest);
                operands = compressed.split(DataAccessConstants.OPERAND_SEPARATOR);
            }

            labelToInstructionIdMap.put(label, lineIndex);
        } else {
            // no label, it means terms[0] = instructionName, terms[2] = operands
            command = terms[0];

            String rest = terms[1];
            for (int i = 2; i < terms.length; ++i)
                rest = rest + terms[i];
            String compressed = compressString(rest);
            operands = compressed.split(DataAccessConstants.OPERAND_SEPARATOR);
        }

        InstructionInfo instructionInfo = InstructionInfo.getInstruction(command);

        if (instructionInfo == null) {
            ret.addError(LineParserErrors.UNKNOWN_COMMAND);
            notifyError(LineParserErrors.UNKNOWN_COMMAND, lineIndex);
            return ret;
        } else {
            InstructionType type = instructionInfo.getInstructionType();
            switch (operands.length) {
                case 0: {
                    ret.addError(LineParserErrors.TOO_LITTLE_PARAMS);
                    notifyError(LineParserErrors.TOO_LITTLE_PARAMS, lineIndex);
                    return ret;
                }
                case 1: {
                    // unary instruction
                    ReturnInfo<TermParserMessages> returnInfo = parseTerms(operands[0]);

                    if (returnInfo.commentOperand != null) {
                        notifyComment(returnInfo.commentOperand.getValue(), lineIndex);
                    }

                    if (returnInfo.errors.size() > 0) {
                        ret.addError(LineParserErrors.INVALID_SYNTAX);
                        notifyError(LineParserErrors.INVALID_SYNTAX, lineIndex);
                        return ret;
                    }

                    if (returnInfo.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfo.operand).getValue());
                    }

                    UnaryInstruction instruction = new UnaryInstruction(command, returnInfo.operand);

                    if (InstructionType.testInstruction(instruction, type)) {
                        ret.instruction = instruction;
                        ret.addMessage(LineParserMessages.UINSTR);
                    } else {
                        ret.addError(LineParserErrors.INVALID_SYNTAX);
                        notifyError(LineParserErrors.INVALID_SYNTAX, lineIndex);
                    }

                    return ret;
                }
                case 2: {
                    // binary instruction
                    ReturnInfo<TermParserMessages> returnInfoOp1 = parseTerms(operands[0]);
                    ReturnInfo<TermParserMessages> returnInfoOp2 = parseTerms(operands[1]);

                    if (returnInfoOp1.commentOperand != null) {
                        notifyComment(returnInfoOp2.commentOperand.getValue(), lineIndex);
                    }

                    if (returnInfoOp2.commentOperand != null) {
                        notifyComment(returnInfoOp2.commentOperand.getValue(), lineIndex);
                    }

                    if (returnInfoOp1.errors.size() > 0 || returnInfoOp2.errors.size() > 0) {
                        ret.addError(LineParserErrors.INVALID_SYNTAX);
                        notifyError(LineParserErrors.INVALID_SYNTAX, lineIndex);
                        return ret;
                    }

                    if (returnInfoOp1.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp1.operand).getValue());
                    } else if (returnInfoOp2.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp2.operand).getValue());
                    }

                    BinaryInstruction instruction = new BinaryInstruction(command, returnInfoOp1.operand, returnInfoOp2.operand);

                    if (InstructionType.testInstruction(instruction, type)) {
                        ret.instruction = instruction;
                        ret.addMessage(LineParserMessages.BINSTR);
                    } else {
                        ret.addError(LineParserErrors.INVALID_SYNTAX);
                        notifyError(LineParserErrors.INVALID_SYNTAX, lineIndex);
                    }

                    return ret;
                }
                case 3: {
                    // ternary instruction
                    ReturnInfo<TermParserMessages> returnInfoOp1 = parseTerms(operands[0]);
                    ReturnInfo<TermParserMessages> returnInfoOp2 = parseTerms(operands[1]);
                    ReturnInfo<TermParserMessages> returnInfoOp3 = parseTerms(operands[2]);

                    if (returnInfoOp1.commentOperand != null) {
                        notifyComment(returnInfoOp2.commentOperand.getValue(), lineIndex);
                    }

                    if (returnInfoOp2.commentOperand != null) {
                        notifyComment(returnInfoOp2.commentOperand.getValue(), lineIndex);
                    }

                    if (returnInfoOp3.commentOperand != null) {
                        notifyComment(returnInfoOp3.commentOperand.getValue(), lineIndex);
                    }

                    if (returnInfoOp1.errors.size() > 0 || returnInfoOp2.errors.size() > 0 || returnInfoOp3.errors.size() > 0) {
                        ret.addError(LineParserErrors.INVALID_SYNTAX);
                        notifyError(LineParserErrors.INVALID_SYNTAX, lineIndex);
                        return ret;
                    }

                    if (returnInfoOp1.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp1.operand).getValue());
                    } else if (returnInfoOp2.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp2.operand).getValue());
                    } else if (returnInfoOp3.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp3.operand).getValue());
                    }

                    TernaryInstruction instruction = new TernaryInstruction(command, returnInfoOp1.operand, returnInfoOp2.operand, returnInfoOp3.operand);

                    if (InstructionType.testInstruction(instruction, type)) {
                        ret.instruction = instruction;
                        ret.addMessage(LineParserMessages.TINSTR);
                    } else {
                        ret.addError(LineParserErrors.INVALID_SYNTAX);
                        notifyError(LineParserErrors.INVALID_SYNTAX, lineIndex);
                    }

                    return ret;
                }
                case 4:
                default: {
                    ret.addError(LineParserErrors.TOO_MANY_PARAMS);
                    notifyError(LineParserErrors.TOO_MANY_PARAMS, lineIndex);
                    return ret;
                }
            }
        }
    }

    private ReturnInfo<TermParserMessages> parseTerms(String term) {
        ReturnInfo<TermParserMessages> ret = new ReturnInfo<>();

        String operandStr = term;

        if (term.contains(DataAccessConstants.COMMENT_SEPARATOR)) {
            if (term.startsWith(DataAccessConstants.COMMENT_SEPARATOR)) {
                CommentOperand operand = new CommentOperand(term.substring(1));
                ret.addMessage(TermParserMessages.OPERAND_IS_COMMENT);
                ret.setCommentOperand(operand);
                return ret;
            } else {
                String comment = term.substring(term.indexOf(DataAccessConstants.COMMENT_SEPARATOR) + 1);

                CommentOperand operand = new CommentOperand(comment);
                ret.setCommentOperand(operand);
                ret.addMessage(TermParserMessages.OPERAND_IS_COMMENT);

                operandStr = term.substring(0, term.indexOf(DataAccessConstants.COMMENT_SEPARATOR));
            }
        }

        if (operandStr.startsWith(DataAccessConstants.IREG_LOWER) ||
                operandStr.startsWith(DataAccessConstants.FREG_LOWER) ||
                operandStr.startsWith(DataAccessConstants.DREG_LOWER) ||
                operandStr.startsWith(DataAccessConstants.IREG_UPPER) ||
                operandStr.startsWith(DataAccessConstants.FREG_UPPER) ||
                operandStr.startsWith(DataAccessConstants.DREG_UPPER)) {
            String indexStr = operandStr.substring(1);
            int index;
            try {
                index = Integer.parseInt(indexStr);
            } catch (NumberFormatException e) {
                ret.addError(TermParserErrors.INVALID_REGISTER_INDEX);
                return ret;
            }

            if (operandStr.startsWith("r") || operandStr.startsWith("R")) {
                RegisterOperand operand1 = new RegisterOperand(OperandType.INTEGER_REGISTER, index);
                ret.setOperand(operand1);
                ret.addMessage(TermParserMessages.OPERAND_IS_IREG);
            } else if (operandStr.startsWith("f") || operandStr.startsWith("F")) {
                RegisterOperand operand1 = new RegisterOperand(OperandType.FLOAT_REGISTER, index);
                ret.setOperand(operand1);
                ret.addMessage(TermParserMessages.OPERAND_IS_FREG);
            } else if (operandStr.startsWith("d") || operandStr.startsWith("D")) {
                RegisterOperand operand1 = new RegisterOperand(OperandType.DOUBLE_REGISTER, index);
                ret.setOperand(operand1);
                ret.addMessage(TermParserMessages.OPERAND_IS_DREG);
            }
        } else if (stringStartsWithLetter(operandStr)) {
            // label
            LabelOperand operand = new LabelOperand(operandStr);
            ret.setOperand(operand);
            ret.addMessage(TermParserMessages.OPERAND_IS_LABEL);
        } else if (stringStartsWithNumber(operandStr)) {
            try {
                int integerNumber = Integer.parseInt(operandStr);

                if (integerNumber < 0) {
                    IntegerOperand operand = new IntegerOperand(integerNumber);
                    ret.setOperand(operand);
                    ret.addMessage(TermParserMessages.OPERAND_IS_INT);
                } else {
                    UnsignedIntegerOperand operand = new UnsignedIntegerOperand(integerNumber);
                    ret.setOperand(operand);
                    ret.addMessage(TermParserMessages.OPERAND_IS_UINT);
                }
            } catch (NumberFormatException eTryInt) {
                try {
                    float floatNumber = Float.parseFloat(operandStr);

                    FloatingOperand operand = new FloatingOperand(floatNumber);
                    ret.setOperand(operand);
                    ret.addMessage(TermParserMessages.OPERAND_IS_FLOAT);
                } catch (NumberFormatException eTryFloat) {
                    try {
                        double doubleNumber = Double.parseDouble(operandStr);

                        DoubleOperand operand = new DoubleOperand(doubleNumber);
                        ret.setOperand(operand);
                        ret.addMessage(TermParserMessages.OPERAND_IS_DOUBLE);
                    } catch (NumberFormatException eTryDouble) {
                        ret.addError(TermParserErrors.INVALID_NUMBERIC_OPERAND);
                        return ret;
                    }
                }
            }
        }

        return ret;
    }

    // HELPER CLASS AND METHODS

    private enum LineParserMessages {
        UINSTR, BINSTR, TINSTR, CINSTR
    }

    private class LineParserErrors {
        private static final String INVALID_SYNTAX = "Invalid syntax";
        private static final String UNKNOWN_COMMAND = "Unknown command";
        private static final String TOO_LITTLE_PARAMS = "Too little params";
        private static final String TOO_MANY_PARAMS = "Too many params";
        private static final String UNKNOWN_REASON = "Unknown reason";
    }

    private enum TermParserMessages {
        OPERAND_IS_COMMENT, OPERAND_IS_IREG, OPERAND_IS_FREG, OPERAND_IS_DREG, OPERAND_IS_INT, OPERAND_IS_UINT,
        OPERAND_IS_FLOAT, OPERAND_IS_DOUBLE, OPERAND_IS_LABEL
    }

    private class TermParserErrors {
        private static final String INVALID_REGISTER_INDEX = "Register index is not valid";
        private static final String INVALID_NUMBERIC_OPERAND = "Numeric operand is not valid";
    }

    private static class ReturnInfo<M> {
        public InstructionAbstract instruction;
        public OperandAbstract operand;
        public CommentOperand commentOperand;
        public List<String> errors;
        public List<M>  messages;

        public ReturnInfo() {
            operand = null;
            commentOperand = null;
            errors = new ArrayList<>();
            messages = new ArrayList<>();
            instruction = null;
        }

        public void setOperand(OperandAbstract operandAbstract) {
            this.operand = operandAbstract;
        }

        public void setCommentOperand(CommentOperand commentOperand) {
            this.commentOperand = commentOperand;
        }

        public void addError(String error) {
            errors.add(error);
        }

        public void addMessage(M message) {
            messages.add(message);
        }
    }

    private boolean stringStartsWithLetter(String str) {
        String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String firstCharAsString = str.substring(0, 1);
        return alpha.contains(firstCharAsString);
    }

    private boolean stringStartsWithNumber(String str) {
        String numeric = "0123456789";
        String firstCharAsString = str.substring(0, 1);
        return numeric.contains(firstCharAsString);
    }

    private String compressString(String uncompressed) {
        return uncompressed.replaceAll("\\s+","");
    }
}
