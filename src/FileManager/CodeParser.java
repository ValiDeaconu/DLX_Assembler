package FileManager;

import InstructionBase.CommentInstruction;
import InstructionBase.InstructionAbstract;
import InstructionBase.InstructionList;

//-> Clasa CodeParser
//        - citeste fisierul .asm linie cu linie (se presupune ca fiecare linie reprezinta o instructiune)
//        - se asigura ca liniile nu au erori de sintaxa altfel arunca o exceptie
//        - asemanator IDE-urile ce te anunta in timp ce scrii ce erori ai
//        - ex.: incerci sa scrii in registrul 0, jump la o instructiune ce nu exista etc
//        - creeaza lista de instructiuni si o salveaza in memorie
public class CodeParser {

    public CodeParser() { }

    // TODO: Finish this code parser
    public InstructionList parse(String assemblyCode) {
        InstructionList list = new InstructionList();

        for (String line : assemblyCode.split("\n"))
        {
            if (line.startsWith("#")) {
                // use .substring(1) to remove '#'
                String commentContent = line.substring(1);
                CommentInstruction instruction = new CommentInstruction(commentContent);
                list.add(instruction);
            } else {
                String[] terms = line.split(",");
                String[] instructionNameAndDestination = terms[0].split(" ");
                String instructionName = instructionNameAndDestination[0];
                String destination = instructionNameAndDestination[1];

                if (terms.length == 3) {
                    // ternary instruction
                    // use .substring(1) to remove first char (a space) if there is any
                    String leftOperand = terms[1].startsWith(" ") ? terms[1].substring(1) : terms[1];
                    String rightOperand = terms[2].startsWith(" ") ? terms[2].substring(1) : terms[2];
                } else if (terms.length == 1) {
                    // binary instruction
                    // use .substring(1) to remove first char (a space) if there is any
                    String operand = terms[1].startsWith(" ") ? terms[1].substring(1) : terms[1];
                } else {
                    // unary instruction
                }
            }
        }

        return list;
    }
}
