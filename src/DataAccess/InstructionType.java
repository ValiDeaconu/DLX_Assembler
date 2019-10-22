package DataAccess;

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
    FI2
}
