package Test;

import Assembler.Assembler;
import Assembler.AssemblerManager;
import Assembler.ProcessState;
import CodeReflection.CodeParser;
import CodeReflection.CodeParserState;
import FileManager.FileManager;
import InstructionBase.InstructionAbstract;
import Util.BinaryConverter;
import Util.Observer;

import java.io.IOException;

public class TestMain implements Observer {
    public TestMain() {

    }

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

        if (assembler.getAssemblerManagerState() == ProcessState.SUCCEEDED) {
            String vhdlCode = "";
            for (String instruction : assembler.getAssembledCode().split("\n")) {
                vhdlCode += "\t\"" + instruction + "\",\n";
            }

            try {
                FileManager.getVhdlWriter().performTask("output.vhdl", vhdlCode);
                System.out.println("File Manager worked");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("File Manager failed");
            }
        } else {
            System.out.println("File Manager failed");
        }
    }

    @Override
    public void update(String notification) {
        System.out.println(notification);
    }
}
