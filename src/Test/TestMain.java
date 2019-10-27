package Test;

import CodeReflection.CodeParser;
import CodeReflection.CodeParserState;
import InstructionBase.InstructionAbstract;

public class TestMain {
    public static void main(String[] args) {
        String code = "minune: add r3, r4, r6 ;comentariu rautacios\nsubf f7, f4, f9\n;nu mai vreau banane";

        CodeParser parser = CodeParser.getInstance(code);
        Thread t = new Thread(parser);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (parser.getState() == CodeParserState.SUCCEEDED) {
            for (InstructionAbstract instruction : parser.getInstructionList()) {
                System.out.println(instruction.toString());
            }
        }
    }
}
