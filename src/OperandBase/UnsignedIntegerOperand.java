package OperandBase;

/**
 * Unsigned Integer Operand is just a rename for IntegerOperand, since Java does not have unsigned int.
 * Just assure that value given to constructor is positive
 */
public class UnsignedIntegerOperand extends IntegerOperand {
    public UnsignedIntegerOperand(int value) {
        super(OperandType.UNSIGNED_INTEGER, Math.abs(value));
    }
}
