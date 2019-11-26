package CodeReflection;

public class CodeParserConstants {
    // Regex
    protected static final String LINE_REGEX = "(\\s*(\\w*)[:])?\\s*(\\w*)\\s*([-]?\\w*)\\s*[,]?\\s*[(]?[#]?([-]?\\w*)?[)]?\\s*[,]?\\s*[(]?[#]?([-]?\\w*)?[)]?";

    // Matcher Constants
    protected static final int LABEL_WITH_DELIMITER_GROUP_ID = 1;
    protected static final int LABEL_GROUP_ID = 2;
    protected static final int INSTRUCTION_NAME_GROUP_ID = 3;
    protected static final int OPERAND_1_GROUP_ID = 4;
    protected static final int OPERAND_2_GROUP_ID = 5;
    protected static final int OPERAND_3_GROUP_ID = 6;

    // Language Constants
    protected class LineParserErrors {
        protected static final String SYNTAX_ERROR = "Syntax error";
        protected static final String UNDEFINED_FUNCTION = "Undefined function";
    }

    protected class TermParserErrors {
        protected static final String INVALID_REGISTER_INDEX = "Register index is not valid";
        protected static final String INVALID_NUMBERIC_OPERAND = "Numeric operand is not valid";
    }

}
