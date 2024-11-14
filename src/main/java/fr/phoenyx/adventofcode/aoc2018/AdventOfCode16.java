package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.phoenyx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static class Device {
        Set<String> ops = Set.of("addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori", "setr", "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr");
        Map<Integer, Set<String>> opCodeMapping = new HashMap<>();
        int[] registers = new int[4];

        void setRegisters(int[] r) {
            System.arraycopy(r, 0, registers, 0, registers.length);
        }

        void apply(int[] instruction) {
            apply(opCodeMapping.get(instruction[0]).iterator().next(), instruction);
        }

        void apply(String op, int[] instruction) {
            int a = instruction[1];
            int b = instruction[2];
            int c = instruction[3];
            switch (op) {
                case "addr" -> addr(a, b, c);
                case "addi" -> addi(a, b, c);
                case "mulr" -> mulr(a, b, c);
                case "muli" -> muli(a, b, c);
                case "banr" -> banr(a, b, c);
                case "bani" -> bani(a, b, c);
                case "borr" -> borr(a, b, c);
                case "bori" -> bori(a, b, c);
                case "setr" -> setr(a, c);
                case "seti" -> seti(a, c);
                case "gtir" -> gtir(a, b, c);
                case "gtri" -> gtri(a, b, c);
                case "gtrr" -> gtrr(a, b, c);
                case "eqir" -> eqir(a, b, c);
                case "eqri" -> eqri(a, b, c);
                case "eqrr" -> eqrr(a, b, c);
                default -> throw new IllegalArgumentException("Unknown op: " + op);
            }
        }

        int testSample(Sample sample) {
            int opCode = sample.instruction[0];
            if (!opCodeMapping.containsKey(opCode)) opCodeMapping.put(opCode, new HashSet<>(ops));
            int count = 0;
            for (String op : ops) {
                setRegisters(sample.registersBefore);
                apply(op, sample.instruction);
                if (Arrays.equals(sample.registersAfter, registers)) count++;
                else opCodeMapping.get(opCode).remove(op);
            }
            return count;
        }

        void guessOpCodes() {
            Utils.filterMappingContentByUniqueness(opCodeMapping);
        }

        void addr(int a, int b, int c) {
            registers[c] = registers[a] + registers[b];
        }

        void addi(int a, int b, int c) {
            registers[c] = registers[a] + b;
        }

        void mulr(int a, int b, int c) {
            registers[c] = registers[a] * registers[b];
        }

        void muli(int a, int b, int c) {
            registers[c] = registers[a] * b;
        }

        void banr(int a, int b, int c) {
            registers[c] = registers[a] & registers[b];
        }

        void bani(int a, int b, int c) {
            registers[c] = registers[a] & b;
        }

        void borr(int a, int b, int c) {
            registers[c] = registers[a] | registers[b];
        }

        void bori(int a, int b, int c) {
            registers[c] = registers[a] | b;
        }

        void setr(int a, int c) {
            registers[c] = registers[a];
        }

        void seti(int a, int c) {
            registers[c] = a;
        }

        void gtir(int a, int b, int c) {
            registers[c] = a > registers[b] ? 1 : 0;
        }

        void gtri(int a, int b, int c) {
            registers[c] = registers[a] > b ? 1 : 0;
        }

        void gtrr(int a, int b, int c) {
            registers[c] = registers[a] > registers[b] ? 1 : 0;
        }

        void eqir(int a, int b, int c) {
            registers[c] = a == registers[b] ? 1 : 0;
        }

        void eqri(int a, int b, int c) {
            registers[c] = registers[a] == b ? 1 : 0;
        }

        void eqrr(int a, int b, int c) {
            registers[c] = registers[a] == registers[b] ? 1 : 0;
        }
    }

    private static class Sample {
        int[] registersBefore = new int[4];
        int[] instruction = new int[4];
        int[] registersAfter = new int[4];
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Sample> samples = new ArrayList<>();
            List<int[]> instructions = new ArrayList<>();
            Device device = new Device();
            Sample currentSample = new Sample();
            boolean isSamplePart = true;
            int emptyLineCount = 0;
            while ((currentLine = reader.readLine()) != null) {
                if (isSamplePart) {
                    if (currentLine.isEmpty()) {
                        emptyLineCount++;
                        if (emptyLineCount == 3) isSamplePart = false;
                    } else {
                        emptyLineCount = 0;
                        if (currentLine.startsWith("Before")) {
                            currentSample = new Sample();
                            samples.add(currentSample);
                            String[] split = currentLine.split("\\[")[1].split("]")[0].split(", ");
                            for (int i = 0; i < split.length; i++) currentSample.registersBefore[i] = Integer.parseInt(split[i]);
                        } else if (currentLine.startsWith("After")) {
                            String[] split = currentLine.split("\\[")[1].split("]")[0].split(", ");
                            for (int i = 0; i < split.length; i++) currentSample.registersAfter[i] = Integer.parseInt(split[i]);
                        } else {
                            String[] split = currentLine.split(" ");
                            for (int i = 0; i < split.length; i++) currentSample.instruction[i] = Integer.parseInt(split[i]);
                        }
                    }
                } else {
                    String[] split = currentLine.split(" ");
                    int[] instruction = new int[split.length];
                    for (int i = 0; i < split.length; i++) instruction[i] = Integer.parseInt(split[i]);
                    instructions.add(instruction);
                }
            }
            LOGGER.info("PART 1: {}", countSamplesBehavingLikeAtLeastThreeOpCodes(device, samples));
            LOGGER.info("PART 2: {}", execute(device, instructions));
        }
    }

    private static long countSamplesBehavingLikeAtLeastThreeOpCodes(Device device, List<Sample> samples) {
        return samples.stream()
                .filter(sample -> device.testSample(sample) > 2)
                .count();
    }

    private static int execute(Device device, List<int[]> instructions) {
        device.setRegisters(new int[]{0, 0, 0, 0});
        device.guessOpCodes();
        for (int[] instruction : instructions) device.apply(instruction);
        return device.registers[0];
    }
}
