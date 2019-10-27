package Assembler;

import DataAccess.InstructionName;
import InstructionBase.InstructionList;
import InstructionBase.UnaryInstruction;

import java.util.ArrayList;
import java.util.List;

public class AssemblerManager {
    private static final int AMOUNT_OF_NOP_INSTRUCTIONS_NEEDED = 3;

    private static final String nopInstructionBinaryCode;
    static {
        UnaryInstruction nopInstruction = new UnaryInstruction(InstructionName.NOP, null);
        nopInstructionBinaryCode = nopInstruction.convertToBinaryCode();
    }

    private InstructionList instructionList;
    private List<Thread> workerThreads;
    private List<Assembler> assemblers;

    private AssemblerManager(InstructionList list) {
        this.instructionList = list;
    }

    // Singleton Design Pattern
    public static AssemblerManager singleton;
    public static AssemblerManager getInstance(InstructionList list) {
        if (singleton == null)
            singleton = new AssemblerManager(list);

        singleton.instructionList = list;
        singleton.workerThreads = null;
        singleton.assemblers = null;

        return singleton;
    }

    // TODO: Implement observers
    private void notifyObservers(ProcessState state, String message) {
        switch (state) {
            case SUCCEEDED:
                System.out.println("AssemblerManager: Code assembled.");
                System.out.println(message);
                break;
            case FAILED:
                System.out.println("AssemblerManager: " + message);
                break;
        }
    }

    public void assemble() {
        // Split instructions, 128 instructions per thread.
        int threadsNeeded = (instructionList.size() >> 7) + 1;
        assemblers = new ArrayList<>();

        int startIndex = 0;
        int endIndex = (1 << 7);

        workerThreads = new ArrayList<>();
        for (int i = 0; i < threadsNeeded; ++i) {
            InstructionList subList = (InstructionList) instructionList.subList(startIndex, endIndex);

            Assembler assembler = new Assembler(subList);
            Thread t = new Thread(assembler);

            workerThreads.add(t);
            assemblers.add(assembler);

            t.start();

            startIndex = endIndex + 1;
            endIndex += (1 << 7);
        }

        for (Thread t : workerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                notifyObservers(ProcessState.FAILED, "Threads were interrupted.");
                return;
            }
        }

        String result = new String("");
        int assemblerIndex = 0;
        for (Assembler assembler : assemblers) {
            switch (assembler.getState()) {
                case IDLE:
                case WORKING:
                    notifyObservers(ProcessState.FAILED, "AssemblerManager failed.");
                    return;
                case FAILED:
                    notifyObservers(ProcessState.FAILED, "Worker threads failed.");
                    return;
                case SUCCEEDED:
                    List<String> commandBlock = assembler.getResult();
                    for (String command : commandBlock) {
                        result += command + "\n";
                    }
                    if (assemblerIndex != assemblers.size() - 1) {
                        for (int i = 0; i < AMOUNT_OF_NOP_INSTRUCTIONS_NEEDED; ++i) {
                            result += nopInstructionBinaryCode + "\n";
                        }
                    }
                    break;
            }

            assemblerIndex++;
        }

        // If we reach this line, it means all assemblers succeeded in their task and the result is ready
        notifyObservers(ProcessState.SUCCEEDED, result);
    }
}
