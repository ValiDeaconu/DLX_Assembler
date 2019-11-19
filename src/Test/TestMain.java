package Test;

import Assembler.Assembler;
import Assembler.AssemblerManager;
import CodeReflection.CodeParser;
import CodeReflection.CodeParserState;
import FileManager.FileManager;
import InstructionBase.InstructionAbstract;
import Util.BinaryConverter;

import java.io.IOException;

public class TestMain {
    public static void main(String[] args) {
        System.out.println(BinaryConverter.convertTo(Integer.toBinaryString(7), 5));

        String code = "minune:\n" +
                "                    add r3,            r4, r6;comentariu rautacios\n" +
                "lbu r2,minune(r3)\n" +
                "\n" +
                "\n" +
                "nef f3,f6\n" +
                "snei r4,r5,#89\n" +
                ";nu mai vreau banane";

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

        AssemblerManager assembler = AssemblerManager.getInstance(parser.getInstructionList());
        assembler.assemble();

        try {
            FileManager.getVhdlWriter().performTask("output.txt", code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
