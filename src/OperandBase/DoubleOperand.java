package OperandBase;

/**
 * Double Operand is just a rename for FloatingOperand, since Floating Operand is implemented using double
 */
public class DoubleOperand extends FloatingOperand {
    public DoubleOperand(double value) {
        super(OperandType.DOUBLE, value);
    }
}
