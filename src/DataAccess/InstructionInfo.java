package DataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InstructionInfo implements Comparable<InstructionInfo> {
    public static final String COMMENT_INSTRUCTION_STRING_NAME = "COMMENT";
    public static final InstructionName  COMMENT_INSTRUCTION_NAME = InstructionName.COMMENT;

    public final String name;
    public final int opcode;
    public final int func;
    public final InstructionType type;
    public final int safeDepth;

    public InstructionInfo(String name, int opcode, int func, InstructionType type, int safeDepth) {
        this.name = name;
        this.opcode = opcode;
        this.func = func;
        this.type = type;
        this.safeDepth = safeDepth;
    }

    public String getInstructionName() {
        return name;
    }

    public int getOperationCode() {
        return opcode;
    }

    public int getFunctionCode() {
        return func;
    }

    public InstructionType getInstructionType() {
        return type;
    }

    public int getSafeDepth() {
        return safeDepth;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InstructionInfo other = (InstructionInfo) obj;
        return opcode == other.opcode &&
                func == other.func &&
                safeDepth == other.safeDepth &&
                Objects.equals(name, other.name) &&
                type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, opcode, func, type, safeDepth);
    }

    @Override
    public int compareTo(InstructionInfo other) {
        return name.compareTo(other.name);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static InstructionInfo getInstruction(InstructionName instructionName) {
        if (InstructionNameToInstruction.containsKey(instructionName))
            return InstructionNameToInstruction.get(instructionName);

        return null;
    }

    public static InstructionInfo getInstruction(String instructionNameString) {
        if (StringToInstructionNameMap.containsKey(instructionNameString)) {
            InstructionName instructionName = StringToInstructionNameMap.get(instructionNameString);
            return getInstruction(instructionName);
        }

        return null;
    }

    private static final Map<String, InstructionName> StringToInstructionNameMap;
    private static final Map<InstructionName, String> InstructionNameToStringMap;
    private static final Map<InstructionName, InstructionInfo> InstructionNameToInstruction;

    static {
        StringToInstructionNameMap = generateStringToInstructionNameMap();
        InstructionNameToStringMap = generateInstructionNameToStringMap();
        InstructionNameToInstruction = generateInstructionNameToInstructionMap();
    }

    private static Map<String, InstructionName> generateStringToInstructionNameMap()
    {
        Map<String, InstructionName> map = new HashMap<>();
        map.put("SPECIAL", InstructionName.SPECIAL);
        map.put("FPARITH", InstructionName.FPARITH);
        map.put("ADDI", InstructionName.ADDI);
        map.put("ADDUI", InstructionName.ADDUI);
        map.put("ANDI", InstructionName.ANDI);
        map.put("BEQZ", InstructionName.BEQZ);
        map.put("BFPF", InstructionName.BFPF);
        map.put("BFPT", InstructionName.BFPT);
        map.put("BNEZ", InstructionName.BNEZ);
        map.put("J", InstructionName.J);
        map.put("JAL", InstructionName.JAL);
        map.put("JALR", InstructionName.JALR);
        map.put("JR", InstructionName.JR);
        map.put("LB", InstructionName.LB);
        map.put("LBU", InstructionName.LBU);
        map.put("LD", InstructionName.LD);
        map.put("LF", InstructionName.LF);
        map.put("LH", InstructionName.LH);
        map.put("LHI", InstructionName.LHI);
        map.put("LHU", InstructionName.LHU);
        map.put("LW", InstructionName.LW);
        map.put("ORI", InstructionName.ORI);
        map.put("RFE", InstructionName.RFE);
        map.put("SB", InstructionName.SB);
        map.put("SD", InstructionName.SD);
        map.put("SEQI", InstructionName.SEQI);
        map.put("SF", InstructionName.SF);
        map.put("SGEI", InstructionName.SGEI);
        map.put("SGEUI", InstructionName.SGEUI);
        map.put("SGTI", InstructionName.SGTI);
        map.put("SGTUI", InstructionName.SGTUI);
        map.put("SH", InstructionName.SH);
        map.put("SLEI", InstructionName.SLEI);
        map.put("SLEUI", InstructionName.SLEUI);
        map.put("SLLI", InstructionName.SLLI);
        map.put("SLTI", InstructionName.SLTI);
        map.put("SLTUI", InstructionName.SLTUI);
        map.put("SNEI", InstructionName.SNEI);
        map.put("SRAI", InstructionName.SRAI);
        map.put("SRLI", InstructionName.SRLI);
        map.put("SUBI", InstructionName.SUBI);
        map.put("SUBUI", InstructionName.SUBUI);
        map.put("SW", InstructionName.SW);
        map.put("TRAP", InstructionName.TRAP);
        map.put("XORI", InstructionName.XORI);
        map.put("LA", InstructionName.LA);
        map.put("NOP", InstructionName.NOP);
        map.put("ADD", InstructionName.ADD);
        map.put("ADDU", InstructionName.ADDU);
        map.put("AND", InstructionName.AND);
        map.put("MOVD", InstructionName.MOVD);
        map.put("MOVF", InstructionName.MOVF);
        map.put("MOVFP2I", InstructionName.MOVFP2I);
        map.put("MOVI2FP", InstructionName.MOVI2FP);
        map.put("MOVI2S", InstructionName.MOVI2S);
        map.put("MOVS2I", InstructionName.MOVS2I);
        map.put("OR", InstructionName.OR);
        map.put("SEQ", InstructionName.SEQ);
        map.put("SGE", InstructionName.SGE);
        map.put("SGEU", InstructionName.SGEU);
        map.put("SGT", InstructionName.SGT);
        map.put("SGTU", InstructionName.SGTU);
        map.put("SLE", InstructionName.SLE);
        map.put("SLEU", InstructionName.SLEU);
        map.put("SLL", InstructionName.SLL);
        map.put("SLT", InstructionName.SLT);
        map.put("SLTU", InstructionName.SLTU);
        map.put("SNE", InstructionName.SNE);
        map.put("SRA", InstructionName.SRA);
        map.put("SRL", InstructionName.SRL);
        map.put("SUB", InstructionName.SUB);
        map.put("SUBU", InstructionName.SUBU);
        map.put("XOR", InstructionName.XOR);
        map.put("ADDD", InstructionName.ADDD);
        map.put("ADDF", InstructionName.ADDF);
        map.put("CVTD2F", InstructionName.CVTD2F);
        map.put("CVTD2I", InstructionName.CVTD2I);
        map.put("CVTF2D", InstructionName.CVTF2D);
        map.put("CVTF2I", InstructionName.CVTF2I);
        map.put("CVTI2D", InstructionName.CVTI2D);
        map.put("CVTI2F", InstructionName.CVTI2F);
        map.put("DIV", InstructionName.DIV);
        map.put("DIVD", InstructionName.DIVD);
        map.put("DIVF", InstructionName.DIVF);
        map.put("DIVU", InstructionName.DIVU);
        map.put("EQD", InstructionName.EQD);
        map.put("EQF", InstructionName.EQF);
        map.put("GED", InstructionName.GED);
        map.put("GEF", InstructionName.GEF);
        map.put("GTD", InstructionName.GTD);
        map.put("GTF", InstructionName.GTF);
        map.put("LED", InstructionName.LED);
        map.put("LEF", InstructionName.LEF);
        map.put("LTD", InstructionName.LTD);
        map.put("LTF", InstructionName.LTF);
        map.put("MULT", InstructionName.MULT);
        map.put("MULTD", InstructionName.MULTD);
        map.put("MULTF", InstructionName.MULTF);
        map.put("MULTU", InstructionName.MULTU);
        map.put("NED", InstructionName.NED);
        map.put("NEF", InstructionName.NEF);
        map.put("SUBD", InstructionName.SUBD);
        map.put("SUBF", InstructionName.SUBF);

        map.put(";", InstructionName.COMMENT);

        return map;
    }

    private static Map<InstructionName, String> generateInstructionNameToStringMap()
    {
        Map<InstructionName, String> map = new HashMap<>();

        map.put(InstructionName.SPECIAL, "SPECIAL");
        map.put(InstructionName.FPARITH, "FPARITH");
        map.put(InstructionName.ADDI, "ADDI");
        map.put(InstructionName.ADDUI, "ADDUI");
        map.put(InstructionName.ANDI, "ANDI");
        map.put(InstructionName.BEQZ, "BEQZ");
        map.put(InstructionName.BFPF, "BFPF");
        map.put(InstructionName.BFPT, "BFPT");
        map.put(InstructionName.BNEZ, "BNEZ");
        map.put(InstructionName.J, "J");
        map.put(InstructionName.JAL, "JAL");
        map.put(InstructionName.JALR, "JALR");
        map.put(InstructionName.JR, "JR");
        map.put(InstructionName.LB, "LB");
        map.put(InstructionName.LBU, "LBU");
        map.put(InstructionName.LD, "LD");
        map.put(InstructionName.LF, "LF");
        map.put(InstructionName.LH, "LH");
        map.put(InstructionName.LHI, "LHI");
        map.put(InstructionName.LHU, "LHU");
        map.put(InstructionName.LW, "LW");
        map.put(InstructionName.ORI, "ORI");
        map.put(InstructionName.RFE, "RFE");
        map.put(InstructionName.SB, "SB");
        map.put(InstructionName.SD, "SD");
        map.put(InstructionName.SEQI, "SEQI");
        map.put(InstructionName.SF, "SF");
        map.put(InstructionName.SGEI, "SGEI");
        map.put(InstructionName.SGEUI, "SGEUI");
        map.put(InstructionName.SGTI, "SGTI");
        map.put(InstructionName.SGTUI, "SGTUI");
        map.put(InstructionName.SH, "SH");
        map.put(InstructionName.SLEI, "SLEI");
        map.put(InstructionName.SLEUI, "SLEUI");
        map.put(InstructionName.SLLI, "SLLI");
        map.put(InstructionName.SLTI, "SLTI");
        map.put(InstructionName.SLTUI, "SLTUI");
        map.put(InstructionName.SNEI, "SNEI");
        map.put(InstructionName.SRAI, "SRAI");
        map.put(InstructionName.SRLI, "SRLI");
        map.put(InstructionName.SUBI, "SUBI");
        map.put(InstructionName.SUBUI, "SUBUI");
        map.put(InstructionName.SW, "SW");
        map.put(InstructionName.TRAP, "TRAP");
        map.put(InstructionName.XORI, "XORI");
        map.put(InstructionName.LA, "LA");
        map.put(InstructionName.NOP, "NOP");
        map.put(InstructionName.ADD, "ADD");
        map.put(InstructionName.ADDU, "ADDU");
        map.put(InstructionName.AND, "AND");
        map.put(InstructionName.MOVD, "MOVD");
        map.put(InstructionName.MOVF, "MOVF");
        map.put(InstructionName.MOVFP2I, "MOVFP2I");
        map.put(InstructionName.MOVI2FP, "MOVI2FP");
        map.put(InstructionName.MOVI2S, "MOVI2S");
        map.put(InstructionName.MOVS2I, "MOVS2I");
        map.put(InstructionName.OR, "OR");
        map.put(InstructionName.SEQ, "SEQ");
        map.put(InstructionName.SGE, "SGE");
        map.put(InstructionName.SGEU, "SGEU");
        map.put(InstructionName.SGT, "SGT");
        map.put(InstructionName.SGTU, "SGTU");
        map.put(InstructionName.SLE, "SLE");
        map.put(InstructionName.SLEU, "SLEU");
        map.put(InstructionName.SLL, "SLL");
        map.put(InstructionName.SLT, "SLT");
        map.put(InstructionName.SLTU, "SLTU");
        map.put(InstructionName.SNE, "SNE");
        map.put(InstructionName.SRA, "SRA");
        map.put(InstructionName.SRL, "SRL");
        map.put(InstructionName.SUB, "SUB");
        map.put(InstructionName.SUBU, "SUBU");
        map.put(InstructionName.XOR, "XOR");
        map.put(InstructionName.ADDD, "ADDD");
        map.put(InstructionName.ADDF, "ADDF");
        map.put(InstructionName.CVTD2F, "CVTD2F");
        map.put(InstructionName.CVTD2I, "CVTD2I");
        map.put(InstructionName.CVTF2D, "CVTF2D");
        map.put(InstructionName.CVTF2I, "CVTF2I");
        map.put(InstructionName.CVTI2D, "CVTI2D");
        map.put(InstructionName.CVTI2F, "CVTI2F");
        map.put(InstructionName.DIV, "DIV");
        map.put(InstructionName.DIVD, "DIVD");
        map.put(InstructionName.DIVF, "DIVF");
        map.put(InstructionName.DIVU, "DIVU");
        map.put(InstructionName.EQD, "EQD");
        map.put(InstructionName.EQF, "EQF");
        map.put(InstructionName.GED, "GED");
        map.put(InstructionName.GEF, "GEF");
        map.put(InstructionName.GTD, "GTD");
        map.put(InstructionName.GTF, "GTF");
        map.put(InstructionName.LED, "LED");
        map.put(InstructionName.LEF, "LEF");
        map.put(InstructionName.LTD, "LTD");
        map.put(InstructionName.LTF, "LTF");
        map.put(InstructionName.MULT, "MULT");
        map.put(InstructionName.MULTD, "MULTD");
        map.put(InstructionName.MULTF, "MULTF");
        map.put(InstructionName.MULTU, "MULTU");
        map.put(InstructionName.NED, "NED");
        map.put(InstructionName.NEF, "NEF");
        map.put(InstructionName.SUBD, "SUBD");
        map.put(InstructionName.SUBF, "SUBF");

        map.put(InstructionName.COMMENT, ";");

        return map;
    }

    // TODO: Add InstructionType and safeDepth for each instruction, according to the instruction set
    // Resource: http://users.utcluj.ro/~baruch/resources/DLX/DLX-Instruction-Set.htm
    private static Map<InstructionName, InstructionInfo> generateInstructionNameToInstructionMap()
    {
        Map<InstructionName, InstructionInfo> map = new HashMap<>();

        map.put(InstructionName.SPECIAL, 	new InstructionInfo("SPECIAL",	0,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.FPARITH, 	new InstructionInfo("FPARITH",	1,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ADDI, 	new InstructionInfo("ADDI",	2,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ADDUI, 	new InstructionInfo("ADDUI",	3,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ANDI, 	new InstructionInfo("ANDI",	4,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.BEQZ, 	new InstructionInfo("BEQZ",	5,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.BFPF, 	new InstructionInfo("BFPF",	6,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.BFPT, 	new InstructionInfo("BFPT",	7,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.BNEZ, 	new InstructionInfo("BNEZ",	8,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.J, 	    new InstructionInfo("J",	9,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.JAL, 	new InstructionInfo("JAL",	10,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.JALR, 	new InstructionInfo("JALR",	11,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.JR, 	new InstructionInfo("JR",	12,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LB, 	new InstructionInfo("LB",	13,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LBU, 	new InstructionInfo("LBU",	14,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LD, 	new InstructionInfo("LD",	15,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LF, 	new InstructionInfo("LF",	16,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LH, 	new InstructionInfo("LH",	17,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LHI, 	new InstructionInfo("LHI",	18,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LHU, 	new InstructionInfo("LHU",	19,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LW, 	new InstructionInfo("LW",	20,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ORI, 	new InstructionInfo("ORI",	21,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.RFE, 	new InstructionInfo("RFE",	22,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SB, 	new InstructionInfo("SB",	23,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SD, 	new InstructionInfo("SD",	24,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SEQI, 	new InstructionInfo("SEQI",	25,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SF, 	new InstructionInfo("SF",	26,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGEI, 	new InstructionInfo("SGEI",	27,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGEUI, 	new InstructionInfo("SGEUI",	28,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGTI, 	new InstructionInfo("SGTI",	29,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGTUI, 	new InstructionInfo("SGTUI",	30,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SH, 	new InstructionInfo("SH",	31,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLEI, 	new InstructionInfo("SLEI",	32,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLEUI, 	new InstructionInfo("SLEUI",	33,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLLI, 	new InstructionInfo("SLLI",	34,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLTI, 	new InstructionInfo("SLTI",	35,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLTUI, 	new InstructionInfo("SLTUI",	36,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SNEI, 	new InstructionInfo("SNEI",	37,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SRAI, 	new InstructionInfo("SRAI",	38,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SRLI, 	new InstructionInfo("SRLI",	39,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SUBI, 	new InstructionInfo("SUBI",	40,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SUBUI, 	new InstructionInfo("SUBUI",	41,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SW, 	new InstructionInfo("SW",	42,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.TRAP, 	new InstructionInfo("TRAP",	43,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.XORI, 	new InstructionInfo("XORI",	44,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LA, 	new InstructionInfo("LA",	48,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.NOP, 	new InstructionInfo("NOP",	0,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ADD, 	new InstructionInfo("ADD",	0,	1,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ADDU, 	new InstructionInfo("ADDU",	0,	2,	InstructionType.UNIMP,	0));
        map.put(InstructionName.AND, 	new InstructionInfo("AND",	0,	3,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MOVD, 	new InstructionInfo("MOVD",	0,	4,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MOVF, 	new InstructionInfo("MOVF",	0,	5,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MOVFP2I, 	new InstructionInfo("MOVFP2I",	0,	6,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MOVI2FP, 	new InstructionInfo("MOVI2FP",	0,	7,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MOVI2S, 	new InstructionInfo("MOVI2S",	0,	8,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MOVS2I, 	new InstructionInfo("MOVS2I",	0,	9,	InstructionType.UNIMP,	0));
        map.put(InstructionName.OR, 	new InstructionInfo("OR",	0,	10,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SEQ, 	new InstructionInfo("SEQ",	0,	11,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGE, 	new InstructionInfo("SGE",	0,	12,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGEU, 	new InstructionInfo("SGEU",	0,	13,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGT, 	new InstructionInfo("SGT",	0,	14,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SGTU, 	new InstructionInfo("SGTU",	0,	15,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLE, 	new InstructionInfo("SLE",	0,	16,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLEU, 	new InstructionInfo("SLEU",	0,	17,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLL, 	new InstructionInfo("SLL",	0,	18,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLT, 	new InstructionInfo("SLT",	0,	19,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SLTU, 	new InstructionInfo("SLTU",	0,	20,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SNE, 	new InstructionInfo("SNE",	0,	21,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SRA, 	new InstructionInfo("SRA",	0,	22,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SRL, 	new InstructionInfo("SRL",	0,	23,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SUB, 	new InstructionInfo("SUB",	0,	24,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SUBU, 	new InstructionInfo("SUBU",	0,	25,	InstructionType.UNIMP,	0));
        map.put(InstructionName.XOR, 	new InstructionInfo("XOR",	0,	26,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ADDD, 	new InstructionInfo("ADDD",	1,	0,	InstructionType.UNIMP,	0));
        map.put(InstructionName.ADDF, 	new InstructionInfo("ADDF",	1,	1,	InstructionType.UNIMP,	0));
        map.put(InstructionName.CVTD2F, 	new InstructionInfo("CVTD2F",	1,	2,	InstructionType.UNIMP,	0));
        map.put(InstructionName.CVTD2I, 	new InstructionInfo("CVTD2I",	1,	3,	InstructionType.UNIMP,	0));
        map.put(InstructionName.CVTF2D, 	new InstructionInfo("CVTF2D",	1,	4,	InstructionType.UNIMP,	0));
        map.put(InstructionName.CVTF2I, 	new InstructionInfo("CVTF2I",	1,	5,	InstructionType.UNIMP,	0));
        map.put(InstructionName.CVTI2D, 	new InstructionInfo("CVTI2D",	1,	6,	InstructionType.UNIMP,	0));
        map.put(InstructionName.CVTI2F, 	new InstructionInfo("CVTI2F",	1,	7,	InstructionType.UNIMP,	0));
        map.put(InstructionName.DIV, 	new InstructionInfo("DIV",	1,	8,	InstructionType.UNIMP,	0));
        map.put(InstructionName.DIVD, 	new InstructionInfo("DIVD",	1,	9,	InstructionType.UNIMP,	0));
        map.put(InstructionName.DIVF, 	new InstructionInfo("DIVF",	1,	10,	InstructionType.UNIMP,	0));
        map.put(InstructionName.DIVU, 	new InstructionInfo("DIVU",	1,	11,	InstructionType.UNIMP,	0));
        map.put(InstructionName.EQD, 	new InstructionInfo("EQD",	1,	12,	InstructionType.UNIMP,	0));
        map.put(InstructionName.EQF, 	new InstructionInfo("EQF",	1,	13,	InstructionType.UNIMP,	0));
        map.put(InstructionName.GED, 	new InstructionInfo("GED",	1,	14,	InstructionType.UNIMP,	0));
        map.put(InstructionName.GEF, 	new InstructionInfo("GEF",	1,	15,	InstructionType.UNIMP,	0));
        map.put(InstructionName.GTD, 	new InstructionInfo("GTD",	1,	16,	InstructionType.UNIMP,	0));
        map.put(InstructionName.GTF, 	new InstructionInfo("GTF",	1,	17,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LED, 	new InstructionInfo("LED",	1,	18,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LEF, 	new InstructionInfo("LEF",	1,	19,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LTD, 	new InstructionInfo("LTD",	1,	20,	InstructionType.UNIMP,	0));
        map.put(InstructionName.LTF, 	new InstructionInfo("LTF",	1,	21,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MULT, 	new InstructionInfo("MULT",	1,	22,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MULTD, 	new InstructionInfo("MULTD",	1,	23,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MULTF, 	new InstructionInfo("MULTF",	1,	24,	InstructionType.UNIMP,	0));
        map.put(InstructionName.MULTU, 	new InstructionInfo("MULTU",	1,	25,	InstructionType.UNIMP,	0));
        map.put(InstructionName.NED, 	new InstructionInfo("NED",	1,	26,	InstructionType.UNIMP,	0));
        map.put(InstructionName.NEF, 	new InstructionInfo("NEF",	1,	27,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SUBD, 	new InstructionInfo("SUBD",	1,	28,	InstructionType.UNIMP,	0));
        map.put(InstructionName.SUBF, 	new InstructionInfo("SUBF",	1,	29,	InstructionType.UNIMP,	0));

        map.put(InstructionName.COMMENT,    new InstructionInfo(";",	0,	0,	InstructionType.UNIMP,	0));

        return map;
    }

}
