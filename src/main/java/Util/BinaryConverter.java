package Util;

public class BinaryConverter {
    private final static int MAXIMUM_BITS = 32;

    private static String reduceTo(String binary, int bitsCount) {
        return binary.substring(MAXIMUM_BITS - bitsCount);
    }

    private static String extendTo(String binary, int bitsCount) {
        StringBuilder result = new StringBuilder();

        result.append("0".repeat(Math.max(0, bitsCount - binary.length())));
        result.append(binary);

        return result.toString();
    }

    public static String convertTo(String binary, int bitsCount) {
        return (binary.length() != MAXIMUM_BITS) ? extendTo(binary, bitsCount) : reduceTo(binary, bitsCount);
    }
}
