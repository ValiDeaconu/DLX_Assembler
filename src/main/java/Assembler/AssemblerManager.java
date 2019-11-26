package Assembler;

import DataAccess.InstructionName;
import InstructionBase.InstructionAbstract;
import InstructionBase.InstructionList;
import InstructionBase.UnaryInstruction;
import Util.Observable;
import Util.Observer;

import java.util.ArrayList;
import java.util.List;

public class AssemblerManager extends Observable {
    private static final int AMOUNT_OF_NOP_INSTRUCTIONS_NEEDED = 3;

    private static final String nopInstructionBinaryCode;
    static {
        UnaryInstruction nopInstruction = new UnaryInstruction(InstructionName.NOP, null);
        nopInstructionBinaryCode = nopInstruction.convertToBinaryCode();
    }

    private InstructionList instructionList;
    private List<Thread> workerThreads;
    private List<Assembler> assemblers;
    private String binaryCode;
    private ProcessState state;

    private AssemblerManager(InstructionList list) {
        this.instructionList = list;
    }

    // Singleton Design Pattern
    private static AssemblerManager singleton;
    public static AssemblerManager getInstance(InstructionList list) {
        if (singleton == null)
            singleton = new AssemblerManager(list);

        singleton.instructionList = list;
        singleton.workerThreads = null;
        singleton.assemblers = null;
        singleton.binaryCode = null;

        return singleton;
    }

    public String getAssembledCode() {
        return binaryCode;
    }

    public ProcessState getAssemblerManagerState() {
        return state;
    }

    public void assemble() {
        // Split instructions, 128 instructions per thread.
        int threadsNeeded = (instructionList.size() >> 7) + 1;
        assemblers = new ArrayList<>();

        int startIndex = 0;
        int endIndex = Math.min(instructionList.size() - 1, (1 << 7));

        workerThreads = new ArrayList<>();
        for (int i = 0; i < threadsNeeded; ++i) {
            List<InstructionAbstract> abstractSubList = instructionList.subList(startIndex, endIndex);
            InstructionList subList = new InstructionList();
            subList.addAll(abstractSubList);

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
                this.state = ProcessState.FAILED;
                notify("Threads were interrupted.");
                return;
            }
        }

        String result = new String("");
        int assemblerIndex = 0;
        for (Assembler assembler : assemblers) {
            switch (assembler.getState()) {
                case IDLE:
                case WORKING:
                    this.state = ProcessState.FAILED;
                    notify("AssemblerManager failed.");
                    return;
                case FAILED:
                    this.state = ProcessState.FAILED;
                    notify("Worker threads failed.");
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
        this.state = ProcessState.SUCCEEDED;
        this.binaryCode = result;
        notify("AssemblerManager: Code assembled.");
    }
}
