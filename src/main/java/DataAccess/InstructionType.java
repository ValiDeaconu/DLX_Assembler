package DataAccess;

import InstructionBase.InstructionAbstract;
import OperandBase.*;

import java.util.*;

public enum InstructionType {
    /** Unimplemented in processor, command for compiler */
    UNIMP,
    /** Format: 1 integer register + 1 label (16 bits) */
    REGLAB,
    /** Format: 1 integer register + 1 imm (16 bits) */
    REG1IMM,
    /** Format: 2 integer registers + 1 imm */
    REG2IMM,
    /** Format: 3 integer registers */
    REG3IMM,
    /** Format: 1 label (16 bits) */
    LEXP16,
    /** Format: 1 label (26 bits) */
    LEXP26,
    /** Format: 1 integer register */
    IREG1,
    /** Format: 1 integer register + label + 1 integer register (inside () -> memory load) */
    LOADI,
    /** Format: 1 float register + label + 1 integer register (inside () -> memory load) */
    LOADF,
    /** Format: 1 double register + label + 1 integer register (inside () -> memory load) */
    LOADD,
    /** Format: 1 label + 1 integer register (inside () -> memory store) + 1 integer register */
    STRI,
    /** Format: 1 label + 1 integer register (inside () -> memory store) + 1 float register */
    STRF,
    /** Format: 1 label + 1 integer register (inside () -> memory store) + 1 double register */
    STRD,
    /** Format: 1 imm (used for trap) */
    IMM1,
    /** Undefined */
    PSEUDO,
    /** No format, used for nop */
    NONEOP,
    /** Format: 2 float registers */
    FREG2a,
    /** Format: 2 double registers */
    DREG2a,
    /** Format: 2 float registers, used to set/unset floating point status bit */
    FREG2b,
    /** Format: 2 double registers, used to set/unset floating point status bit */
    DREG2b,
    /** Format: 3 float registers */
    FREG3,
    /** Format: 3 double registers */
    DREG3,
    /** Format: 1 integer register + 1 float register */
    IF2,
    /** Format: 1 float register + 1 integer register */
    FI2,
    /** Format: 1 float register + 1 double register */
    FD2,
    /** Format: 1 double register + 1 float register */
    DF2;

    private static Map<InstructionType, List<OperandType>> InstructionTypeToOperands;

    public static List<OperandType> getOperandsList(InstructionType type) {
        return InstructionTypeToOperands.get(type);
    }

    public static boolean testInstruction(InstructionAbstract instruction, InstructionType type) {
        if (instruction.getInstructionInfo().getInstructionType() != type)
            return false;

        List<OperandType> operandsType = getOperandsList(type);

        if (operandsType == null) {
            return instruction.getParametersNumber() == 0;
        }

        if (operandsType.size() != instruction.getParametersNumber()) {
            return false;
        }

        for (int i = 0; i < operandsType.size(); ++i) {
            OperandType operandType = operandsType.get(i);
            OperandAbstract operand = (i == 0) ? instruction.getDestination() : instruction.getOperandsAsList().get(i - 1);

            switch (operandType) {
                case INTEGER_REGISTER: {
                    if (!(operand instanceof RegisterOperand))
                        return false;

                    RegisterOperand registerOperand = (RegisterOperand) operand;
                    if (registerOperand.getRegisterType() != RegisterType.INTEGER)
                        return false;

                    break;
                }
                case FLOAT_REGISTER: {
                    if (!(operand instanceof RegisterOperand))
                        return false;

                    RegisterOperand registerOperand = (RegisterOperand) operand;
                    if (registerOperand.getRegisterType() != RegisterType.FLOAT)
                        return false;
                    break;
                }
                case UNSIGNED_INTEGER: {
                    if (!(operand instanceof UnsignedIntegerOperand))
                        return false;
                    break;
                }
                case INTEGER: {
                    if (!(operand instanceof IntegerOperand))
                        return false;
                    break;
                }
                case NUMERIC: {
                    if (!(operand instanceof UnsignedIntegerOperand ||
                            operand instanceof IntegerOperand))
                        return false;
                    break;
                }
                case LABEL: {
                    if (!(operand instanceof LabelOperand))
                        return false;
                    break;
                }
                default:
                    return false;
            }
        }

        return true;
    }

    static {
        Map<InstructionType, List<OperandType>> map = new HashMap<>();

        map.put(InstructionType.UNIMP, null);
        map.put(InstructionType.REGLAB, Arrays.asList(OperandType.INTEGER_REGISTER, OperandType.LABEL));
        map.put(InstructionType.REG1IMM, Arrays.asList(OperandType.INTEGER_REGISTER, OperandType.NUMERIC));
        map.put(InstructionType.REG2IMM, Arrays.asList(OperandType.INTEGER_REGISTER, OperandType.INTEGER_REGISTER, OperandType.NUMERIC));
        map.put(InstructionType.REG3IMM, Arrays.asList(OperandType.INTEGER_REGISTER, OperandType.INTEGER_REGISTER, OperandType.INTEGER_REGISTER));
        map.put(InstructionType.LEXP16, Collections.singletonList(OperandType.LABEL));
        map.put(InstructionType.LEXP26, Collections.singletonList(OperandType.LABEL));
        map.put(InstructionType.IREG1, Collections.singletonList(OperandType.INTEGER_REGISTER));
        map.put(InstructionType.LOADI, Arrays.asList(OperandType.INTEGER_REGISTER, OperandType.LABEL, OperandType.INTEGER_REGISTER));
        map.put(InstructionType.LOADF, Arrays.asList(OperandType.FLOAT_REGISTER, OperandType.LABEL, OperandType.FLOAT_REGISTER));
        map.put(InstructionType.LOADD, Arrays.asList(OperandType.DOUBLE_REGISTER, OperandType.LABEL, OperandType.DOUBLE_REGISTER));
        map.put(InstructionType.STRI, Arrays.asList(OperandType.LABEL, OperandType.INTEGER_REGISTER, OperandType.INTEGER_REGISTER));
        map.put(InstructionType.STRF, Arrays.asList(OperandType.LABEL, OperandType.FLOAT_REGISTER, OperandType.FLOAT_REGISTER));
        map.put(InstructionType.STRD, Arrays.asList(OperandType.LABEL, OperandType.DOUBLE_REGISTER, OperandType.DOUBLE_REGISTER));
        map.put(InstructionType.IMM1, Collections.singletonList(OperandType.NUMERIC));
        map.put(InstructionType.PSEUDO, null);
        map.put(InstructionType.NONEOP, null);
        map.put(InstructionType.FREG2a, Arrays.asList(OperandType.FLOAT_REGISTER, OperandType.FLOAT_REGISTER));
        map.put(InstructionType.DREG2a, Arrays.asList(OperandType.DOUBLE_REGISTER, OperandType.DOUBLE_REGISTER));
        map.put(InstructionType.FREG2b, Arrays.asList(OperandType.FLOAT_REGISTER, OperandType.FLOAT_REGISTER));
        map.put(InstructionType.DREG2b, Arrays.asList(OperandType.DOUBLE_REGISTER, OperandType.DOUBLE_REGISTER));
        map.put(InstructionType.FREG3, Arrays.asList(OperandType.FLOAT_REGISTER, OperandType.FLOAT_REGISTER, OperandType.FLOAT_REGISTER));
        map.put(InstructionType.DREG3, Arrays.asList(OperandType.DOUBLE_REGISTER, OperandType.DOUBLE_REGISTER, OperandType.DOUBLE_REGISTER));
        map.put(InstructionType.IF2, Arrays.asList(OperandType.INTEGER_REGISTER, OperandType.FLOAT_REGISTER));
        map.put(InstructionType.FI2, Arrays.asList(OperandType.FLOAT_REGISTER, OperandType.INTEGER_REGISTER));
        map.put(InstructionType.FD2, Arrays.asList(OperandType.FLOAT_REGISTER, OperandType.DOUBLE_REGISTER));
        map.put(InstructionType.DF2, Arrays.asList(OperandType.DOUBLE_REGISTER, OperandType.FLOAT_REGISTER));

        InstructionTypeToOperands = map;
    }
}
