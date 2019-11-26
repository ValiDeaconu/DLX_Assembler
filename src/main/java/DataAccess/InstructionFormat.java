package DataAccess;

import InstructionBase.InstructionAbstract;

public enum InstructionFormat {
    R_TYPE,
    I_TYPE,
    J_TYPE,
    U_TYPE; // Undefined

    public static String convertToBinaryCode(InstructionAbstract instructionAbstract) {
        String result = "";
        switch (instructionAbstract.getInstructionInfo().getFormat()) {
            case R_TYPE:
                result = convertRType(instructionAbstract);
                break;
            case I_TYPE:
                result = convertIType(instructionAbstract);
                break;
            case J_TYPE:
                result = convertJType(instructionAbstract);
                break;
            case U_TYPE:
                return null;
        }

        if (result.length() != 32) {
            System.out.println(instructionAbstract.toString() + " result=" + result);
            throw new IllegalStateException("Converter failed to achieve 32 bits instruction.");
        }

        return result;
    }

    private static String convertRType(InstructionAbstract instructionAbstract) {
        String result = instructionAbstract.getInstructionInfo().getBinaryOperationCode();

        final int params = instructionAbstract.getParametersNumber();

        switch (params) {
            case 0: {
                result += "00000" + // empty RS
                        "00000" + // empty RT
                        "00000" + // empty RD
                        instructionAbstract.getInstructionInfo().getBinaryFunctionCode();
                break;
            }
            case 1: {
                result += "00000" + // empty RS
                        "00000" + // empty RT
                        instructionAbstract.getDestination().convertToBinaryCode() +
                        instructionAbstract.getInstructionInfo().getBinaryFunctionCode();
                break;
            }
            case 2: {
                result += instructionAbstract.getOperandsAsList().get(0).convertToBinaryCode() +
                        "00000" + // empty RT
                        instructionAbstract.getDestination().convertToBinaryCode() +
                        instructionAbstract.getInstructionInfo().getBinaryFunctionCode();
                break;
            }
            case 3: {
                result += instructionAbstract.getOperandsAsList().get(0).convertToBinaryCode() +
                        instructionAbstract.getOperandsAsList().get(1).convertToBinaryCode() +
                        instructionAbstract.getDestination().convertToBinaryCode() +
                        instructionAbstract.getInstructionInfo().getBinaryFunctionCode();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + params);
        }

        return result;
    }

    private static String convertIType(InstructionAbstract instructionAbstract) {
        String result = instructionAbstract.getInstructionInfo().getBinaryOperationCode();

        final int params = instructionAbstract.getParametersNumber();

        switch (params) {
            case 0: {
                result += "00000" + // empty RS
                        "00000" + // empty RT
                        "0000000000000000"; // empty imm16
                break;
            }
            case 1: {
                result += instructionAbstract.getDestination().convertToBinaryCode() +
                        "00000" + // empty RT
                        "0000000000000000"; // empty imm16
                break;
            }
            case 2: {
                result += instructionAbstract.getDestination().convertToBinaryCode() +
                        instructionAbstract.getOperandsAsList().get(0).convertToBinaryCode() +
                        "0000000000000000"; // empty imm16
                break;
            }
            case 3: {
                result += instructionAbstract.getDestination().convertToBinaryCode() +
                        instructionAbstract.getOperandsAsList().get(0).convertToBinaryCode() +
                        instructionAbstract.getOperandsAsList().get(1).convertToBinaryCode();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + params);
        }

        return result;
    }

    private static String convertJType(InstructionAbstract instructionAbstract) {
        StringBuilder result = new StringBuilder(instructionAbstract.getInstructionInfo().getBinaryOperationCode());

        final int params = instructionAbstract.getParametersNumber();

        switch (params) {
            case 0: {
                result.append("00000000000000000000000000"); // empty imm26
                break;
            }
            case 1: {
                String temp = instructionAbstract.getDestination().convertToBinaryCode();
                if (temp.length() == 26) {
                    result.append(temp);
                } else {
                    // extend to 26 bits
                    result.append("0".repeat(Math.max(0, 26 - temp.length())));
                    result.append(temp);
                }
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + params);
        }

        return result.toString();
    }
}
