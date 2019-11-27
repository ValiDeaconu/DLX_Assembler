package CodeReflection;

import DataAccess.DataAccessConstants;
import DataAccess.InstructionInfo;
import DataAccess.InstructionName;
import DataAccess.InstructionType;
import InstructionBase.*;
import LogManager.LogManager;
import LogManager.LogType;
import OperandBase.*;
import Util.Observable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeParser extends Observable implements Runnable {
    private CodeParserState state;
    private InstructionList instructionList;
    private String codeBlock;
    private Map<Integer, String> instructionIdRequestLabelMap;
    private Map<String, Integer> labelToInstructionIdMap;

    private LogManager logManager;

    private CodeParser(String codeBlock) {
        this.codeBlock = codeBlock;
        this.state = CodeParserState.IDLE;
        this.instructionList = null;
        this.instructionIdRequestLabelMap = null;
        this.labelToInstructionIdMap = null;
        this.logManager = new LogManager(LogType.CodeParser);
    }

    // Singleton Design Pattern
    public static CodeParser singleton;
    public static CodeParser getInstance(String codeBlock) {
        if (singleton == null)
            singleton = new CodeParser(codeBlock);

        singleton.codeBlock = codeBlock;
        singleton.state = CodeParserState.IDLE;
        singleton.instructionList = null;
        singleton.instructionIdRequestLabelMap = null;
        singleton.labelToInstructionIdMap = null;
        singleton.logManager = new LogManager(LogType.CodeParser);

        return singleton;
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

            if (!returnInfo.messages.contains(LineParserMessages.CINSTR) &&
                    !returnInfo.messages.contains(LineParserMessages.LINSTR) &&
                    !returnInfo.messages.contains(LineParserMessages.NINSTR))
                instructionList.add(returnInfo.instruction);
            else
                instructionList.add(new SimpleInstruction(InstructionName.NOP));
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

    private void notifyError(String message, int lineIndex) {
        String notification = "Error found: " + message + ", at line=" + lineIndex;
        logManager.write(notification);
        notify(notification);
    }

    private void notifyComment(String comment, int lineIndex, int commentIndex) {
        String notification = "Comment found: " + comment + ", at line=" + lineIndex + ", comment index=" + commentIndex;
        logManager.write(notification);
        notify(notification);
    }

    private ReturnInfo<LineParserMessages> parseLine(String rawLine, int lineIndex) {
        ReturnInfo<LineParserMessages> ret = new ReturnInfo<>();

        String compressedLine = compressString(rawLine);
        if (compressedLine.equals("")) {
            logManager.write("Empty line found at lineIndex=" + lineIndex);
            ret.addMessage(LineParserMessages.NINSTR);
            return ret;
        } else if (compressedLine.startsWith(DataAccessConstants.COMMENT_SEPARATOR)) {
            int commentIndex = rawLine.indexOf(DataAccessConstants.COMMENT_SEPARATOR);
            notifyComment(rawLine.substring(commentIndex + 1), lineIndex, commentIndex);
            ret.addMessage(LineParserMessages.CINSTR);
            return ret;
        }

        String line = rawLine;
        if (rawLine.contains(DataAccessConstants.COMMENT_SEPARATOR)) {
            int commentIndex = rawLine.indexOf(DataAccessConstants.COMMENT_SEPARATOR);
            line = rawLine.substring(0, commentIndex);
            notifyComment(rawLine.substring(commentIndex + 1), lineIndex, commentIndex);
        }

        line = line.toUpperCase();

        Pattern pattern = Pattern.compile(CodeParserConstants.LINE_REGEX);
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) {
            ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
            notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
            return ret;
        }

        boolean labelFound = false;
        if (groupNotEmpty(matcher, CodeParserConstants.LABEL_WITH_DELIMITER_GROUP_ID) &&
                groupNotEmpty(matcher, CodeParserConstants.LABEL_GROUP_ID) &&
                matcher.group(CodeParserConstants.LABEL_WITH_DELIMITER_GROUP_ID).startsWith(matcher.group(CodeParserConstants.LABEL_GROUP_ID))) {
            labelFound = true;
            logManager.write("Label (" + matcher.group(CodeParserConstants.LABEL_GROUP_ID) + ") found at line=" + lineIndex);
            labelToInstructionIdMap.put(matcher.group(CodeParserConstants.LABEL_GROUP_ID), lineIndex);
        }

        InstructionInfo instructionInfo;
        InstructionType instructionType;
        if (groupNotEmpty(matcher, CodeParserConstants.INSTRUCTION_NAME_GROUP_ID)) {
            instructionInfo = InstructionInfo.getInstruction(matcher.group(CodeParserConstants.INSTRUCTION_NAME_GROUP_ID));

            if (instructionInfo == null) {
                ret.addError(CodeParserConstants.LineParserErrors.UNDEFINED_FUNCTION);
                notifyError(CodeParserConstants.LineParserErrors.UNDEFINED_FUNCTION, lineIndex);
                return ret;
            } else {
                if (instructionInfo.equals(InstructionInfo.getInstruction(InstructionName.NOP))) {
                    ret.addMessage(LineParserMessages.NINSTR);
                    return ret;
                }
                instructionType = instructionInfo.getInstructionType();
            }
        } else {
            if (labelFound) {
                ret.addMessage(LineParserMessages.LINSTR);
                return ret;
            } else {
                ret.addError(CodeParserConstants.LineParserErrors.UNDEFINED_FUNCTION);
                notifyError(CodeParserConstants.LineParserErrors.UNDEFINED_FUNCTION, lineIndex);
                return ret;
            }
        }

        if (groupNotEmpty(matcher, CodeParserConstants.OPERAND_1_GROUP_ID)) {
            // first operand
            ReturnInfo<TermParserMessages> returnInfoOp1 = parseTerms(matcher.group(CodeParserConstants.OPERAND_1_GROUP_ID));

            if (returnInfoOp1.errors.size() > 0) {
                ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
                notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
                return ret;
            }

            if (returnInfoOp1.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp1.operand).getValue());
            }

            if (groupNotEmpty(matcher, CodeParserConstants.OPERAND_2_GROUP_ID)) {
                // second operand
                ReturnInfo<TermParserMessages> returnInfoOp2 = parseTerms(matcher.group(CodeParserConstants.OPERAND_2_GROUP_ID));

                if (returnInfoOp2.errors.size() > 0) {
                    ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
                    notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
                    return ret;
                }

                if (returnInfoOp2.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                    instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp2.operand).getValue());
                }

                if (groupNotEmpty(matcher, CodeParserConstants.OPERAND_3_GROUP_ID)) {
                    // third operand

                    ReturnInfo<TermParserMessages> returnInfoOp3 = parseTerms(matcher.group(CodeParserConstants.OPERAND_3_GROUP_ID));

                    if (returnInfoOp3.errors.size() > 0) {
                        ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
                        notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
                        return ret;
                    }

                    if (returnInfoOp3.messages.contains(TermParserMessages.OPERAND_IS_LABEL)) {
                        instructionIdRequestLabelMap.put(lineIndex, ((LabelOperand) returnInfoOp3.operand).getValue());
                    }

                    // ternary instruction
                    TernaryInstruction instruction = new TernaryInstruction(matcher.group(CodeParserConstants.INSTRUCTION_NAME_GROUP_ID), returnInfoOp1.operand, returnInfoOp2.operand, returnInfoOp3.operand);

                    if (InstructionType.testInstruction(instruction, instructionType)) {
                        ret.instruction = instruction;
                        ret.addMessage(LineParserMessages.TINSTR);
                    } else {
                        ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
                        notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
                    }
                } else {
                    // binary instruction
                    BinaryInstruction instruction = new BinaryInstruction(matcher.group(CodeParserConstants.INSTRUCTION_NAME_GROUP_ID), returnInfoOp1.operand, returnInfoOp2.operand);

                    if (InstructionType.testInstruction(instruction, instructionType)) {
                        ret.instruction = instruction;
                        ret.addMessage(LineParserMessages.BINSTR);
                    } else {
                        ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
                        notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
                    }
                }
            } else {
                // unary instruction
                UnaryInstruction instruction = new UnaryInstruction(matcher.group(CodeParserConstants.INSTRUCTION_NAME_GROUP_ID), returnInfoOp1.operand);

                if (InstructionType.testInstruction(instruction, instructionType)) {
                    ret.instruction = instruction;
                    ret.addMessage(LineParserMessages.UINSTR);
                } else {
                    ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
                    notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
                }
            }
        } else {
            notifyError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR, lineIndex);
            ret.addError(CodeParserConstants.LineParserErrors.SYNTAX_ERROR);
        }
        return ret;
    }

    private ReturnInfo<TermParserMessages> parseTerms(String term) {
        ReturnInfo<TermParserMessages> ret = new ReturnInfo<>();

        String operandStr = term;

        if (term.contains(DataAccessConstants.COMMENT_SEPARATOR)) {
            if (term.startsWith(DataAccessConstants.COMMENT_SEPARATOR)) {
                String operand = term.substring(1);
                ret.addMessage(TermParserMessages.OPERAND_IS_COMMENT);
                ret.setCommentOperand(operand);
                return ret;
            } else {
                String comment = term.substring(term.indexOf(DataAccessConstants.COMMENT_SEPARATOR) + 1);

                ret.setCommentOperand(comment);
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
                ret.addError(CodeParserConstants.TermParserErrors.INVALID_REGISTER_INDEX);
                return ret;
            }

            if (operandStr.startsWith(DataAccessConstants.IREG_LOWER) || operandStr.startsWith(DataAccessConstants.IREG_UPPER)) {
                RegisterOperand operand1 = new RegisterOperand(OperandType.INTEGER_REGISTER, index);
                ret.setOperand(operand1);
                ret.addMessage(TermParserMessages.OPERAND_IS_IREG);
            } else if (operandStr.startsWith(DataAccessConstants.FREG_LOWER) || operandStr.startsWith(DataAccessConstants.FREG_UPPER)) {
                RegisterOperand operand1 = new RegisterOperand(OperandType.FLOAT_REGISTER, index);
                ret.setOperand(operand1);
                ret.addMessage(TermParserMessages.OPERAND_IS_FREG);
            } else if (operandStr.startsWith(DataAccessConstants.DREG_LOWER) || operandStr.startsWith(DataAccessConstants.DREG_UPPER)) {
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
                ret.addError(CodeParserConstants.TermParserErrors.INVALID_NUMBERIC_OPERAND);
                return ret;
            }
        }

        return ret;
    }

    // HELPER CLASS AND METHODS

    private enum LineParserMessages {
        UINSTR, // Unary Instruction
        BINSTR, // Binary Instruction
        TINSTR, // Ternary Instruction
        CINSTR, // Comment Instruction (line with comment)
        LINSTR, // Label Instruction (line with label)
        NINSTR // No Instruction (empty line)
    }

    private enum TermParserMessages {
        OPERAND_IS_COMMENT, OPERAND_IS_IREG, OPERAND_IS_FREG, OPERAND_IS_DREG,
        OPERAND_IS_INT, OPERAND_IS_UINT, OPERAND_IS_LABEL
    }

    private static class ReturnInfo<M> {
        public InstructionAbstract instruction;
        public OperandAbstract operand;
        public String commentOperand;
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

        public void setCommentOperand(String commentOperand) {
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
        if (str == null)
            return false;

        String alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String firstCharAsString = str.substring(0, 1);
        return alpha.contains(firstCharAsString);
    }

    private boolean stringStartsWithNumber(String str) {
        if (str == null)
            return false;

        String numeric = "0123456789";
        String firstCharAsString = str.substring(0, 1);
        return numeric.contains(firstCharAsString);
    }

    private String compressString(String uncompressed) {
        return uncompressed.replaceAll("\\s+","");
    }

    private boolean groupNotEmpty(Matcher m, int groupIndex) {
        try {
            String s = m.group(groupIndex);
            return s != null && !s.equals("");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
    }
}
