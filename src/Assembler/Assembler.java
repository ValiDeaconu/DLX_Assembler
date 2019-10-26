package Assembler;

import DataAccess.InstructionName;
import InstructionBase.InstructionAbstract;
import InstructionBase.InstructionList;
import InstructionBase.UnaryInstruction;
import OperandBase.OperandAbstract;
import OperandBase.RegisterOperand;
import Util.Pair;

import java.util.*;

public class Assembler implements Runnable {
    private static final String nopInstructionBinaryCode;
    static {
        UnaryInstruction nopInstruction = new UnaryInstruction(InstructionName.NOP, null);
        nopInstructionBinaryCode = nopInstruction.convertToBinaryCode();
    }

    private AssemblerState state;
    private InstructionList instructionList;
    private Map<RegisterOperand, Integer> unsafeRegisters;
    private List<String> generatedInstructionList;
    private Queue<Map.Entry<InstructionAbstract, Integer>> laterInstructions;

    public Assembler(InstructionList instructionList) {
        this.instructionList = new InstructionList();
        this.state = AssemblerState.IDLE;
        this.unsafeRegisters = new HashMap<>();
        this.generatedInstructionList = new LinkedList<>();
        this.laterInstructions = new LinkedList<>();
    }

    public AssemblerState getState() {
        return state;
    }

    public List<String> getResult() {
        return generatedInstructionList;
    }

    @Override
    public void run() {
        int executeUntil = instructionList.size();
        for (int i = 0; i < executeUntil; ++i) {
            boolean generatedInstruction = false;

            // Update the queue and the map
            Queue<Map.Entry<InstructionAbstract, Integer>> updatedQueue = new LinkedList<>();
            for (Map.Entry<InstructionAbstract, Integer> entry : laterInstructions) {
                InstructionAbstract instruction = entry.getKey();
                int safeAt = entry.getValue();

                if (safeAt == i) {
                    if (instruction.getDestination() instanceof RegisterOperand) {
                        RegisterOperand dest = (RegisterOperand) instruction.getDestination();
                        unsafeRegisters.put(dest, safeAt);
                    }

                    generatedInstruction = true;
                    generatedInstructionList.add(instruction.convertToBinaryCode());
                } else {
                    updatedQueue.add(entry);
                }
            }
            laterInstructions = updatedQueue;

            Set<Map.Entry<RegisterOperand, Integer>> entrySet = unsafeRegisters.entrySet();
            for (Map.Entry<RegisterOperand, Integer> entry : entrySet) {
                RegisterOperand register = entry.getKey();
                int safeAt = entry.getValue();

                if (safeAt == i) {
                    unsafeRegisters.remove(register, safeAt);
                }
            }

            // If there are instructions left, compute them
            if (i < instructionList.size()) {
                // Get the new instruction
                InstructionAbstract instruction = instructionList.get(i);

                // Compute if this instruction is safe (not reads from an not updated register)
                int maximumUnsafe = 0;
                List<OperandAbstract> operands = instruction.getOperandsAsList();
                if (operands != null) {
                    for (OperandAbstract operand : operands) {
                        if (operand instanceof RegisterOperand) {
                            RegisterOperand register = (RegisterOperand) operand;
                            if (unsafeRegisters.containsKey(register)) {
                                int notSafeUntil = unsafeRegisters.get(register);
                                maximumUnsafe = Math.max(maximumUnsafe, notSafeUntil);
                            }
                        }
                    }
                }

                // If it's not safe to execute this instruction, then reserve it for later
                if (maximumUnsafe > 0) {
                    int safeDepth = instruction.getInstructionInfo().getSafeDepth();
                    int safeAt = i + safeDepth + 1;

                    updatedQueue.add(new Pair<InstructionAbstract, Integer>(instruction, safeAt));

                    // Update running until variable
                    executeUntil = Math.max(executeUntil, safeAt);
                } else {
                    generatedInstruction = true;
                    generatedInstructionList.add(instruction.convertToBinaryCode());
                }
            }

            // If no instruction were added at this time, add a NOP
            if (!generatedInstruction) {
                generatedInstructionList.add(nopInstructionBinaryCode);
            }
        }
    }
}
