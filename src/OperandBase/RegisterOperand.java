package OperandBase;

import java.util.Objects;

public class RegisterOperand extends OperandAbstract<Integer> implements Comparable<RegisterOperand> {
    private RegisterType registerType;
    private int registerIndex;

    public RegisterOperand(RegisterType registerType, int registerIndex) {
        super(OperandType.REGISTER);
        this.registerType = registerType;
        this.registerIndex = registerIndex;
    }

    public RegisterType getRegisterType()
    {
        return registerType;
    }

    public Integer getRegisterIndex() {
        return registerIndex;
    }

    @Override
    public Integer getValue() {
        return getRegisterIndex();
    }

    @Override
    public String toString() {
        switch (registerType) {
            case INTEGER:
                return "R" + registerIndex;

            case FLOAT:
                return "F" + registerIndex;

            case DOUBLE:
                return "D" + registerIndex;
        }

        assert false; // should never reach this
        return "U" + registerIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RegisterOperand other = (RegisterOperand) obj;
        return registerIndex == other.registerIndex && registerType == other.registerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(registerType, registerIndex);
    }

    @Override
    public int compareTo(RegisterOperand other) {
        return Integer.compare(registerIndex, other.registerIndex);
    }
}
