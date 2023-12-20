package fr.phoenyx.adventofcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode20 {

    private enum PulseType {
        LOW, HIGH
    }

    private static class Pulse {
        PulseType type;
        String sourceModule;
        String targetModule;

        Pulse(PulseType type, String sourceModule, String targetModule) {
            this.type = type;
            this.sourceModule = sourceModule;
            this.targetModule = targetModule;
        }
    }

    private abstract static class Module {
        String name;
        List<String> outputs = new ArrayList<>();

        Module(String name) {
            this.name = name;
        }

        static Module buildModule(String line) {
            char firstChar = line.charAt(0);
            if (firstChar == '%') return new FlipFlop(line.split(" -> ")[0].substring(1));
            else if (firstChar == '&') return new Conjunction(line.split(" -> ")[0].substring(1));
            else return new Broadcaster(line.split(" -> ")[0]);
        }

        abstract List<Pulse> process(Pulse pulse);
        abstract void reset();
    }

    private static class FlipFlop extends Module {
        boolean isOff = true;

        FlipFlop(String name) {
            super(name);
        }

        @Override
        List<Pulse> process(Pulse pulse) {
            if (pulse.type == PulseType.HIGH) return Collections.emptyList();
            PulseType typeSent = isOff ? PulseType.HIGH : PulseType.LOW;
            isOff = !isOff;
            return outputs.stream().map(o -> new Pulse(typeSent, name, o)).toList();
        }

        @Override
        void reset() {
            isOff = true;
        }
    }

    private static class Conjunction extends Module {
        Map<String, PulseType> inputsHistory = new HashMap<>();

        Conjunction(String name) {
            super(name);
        }

        @Override
        List<Pulse> process(Pulse pulse) {
            inputsHistory.put(pulse.sourceModule, pulse.type);
            PulseType typeSent = inputsHistory.values().stream().allMatch(t -> t == PulseType.HIGH) ? PulseType.LOW : PulseType.HIGH;
            return outputs.stream().map(o -> new Pulse(typeSent, name, o)).toList();
        }

        @Override
        void reset() {
            inputsHistory.replaceAll((i, v) -> PulseType.LOW);
        }
    }

    private static class Broadcaster extends Module {
        Broadcaster(String name) {
            super(name);
        }

        @Override
        List<Pulse> process(Pulse pulse) {
            return outputs.stream().map(o -> new Pulse(pulse.type, name, o)).toList();
        }

        @Override
        void reset() {
            // The broadcaster has nothing to do to reset
        }
    }

    private static class Network {
        Map<String, Module> modules = new HashMap<>();
        long buttonPushes = 0;
        long lowPulses = 0;
        long highPulses = 0;
        Module exitModule;
        Map<String, Long> cycles = new HashMap<>();

        void setInputsOutputs(List<String> lines) {
            for (String line : lines) {
                String[] split = line.split(" -> ");
                String name = split[0];
                if (name.charAt(0) == '%' || name.charAt(0) == '&') name = name.substring(1);
                Module module = modules.get(name);
                String[] targets = split[1].split(", ");
                for (String target : targets) {
                    Module output = modules.get(target);
                    module.outputs.add(target);
                    if (output instanceof Conjunction conjunction) conjunction.inputsHistory.put(name, PulseType.LOW);
                }
            }
            String machineModuleName = modules.values().stream()
                .flatMap(m -> m.outputs.stream())
                .filter(name -> modules.values().stream().map(n -> n.name).noneMatch(n -> n.equals(name)))
                .findAny().orElseThrow();
            exitModule = modules.values().stream().filter(m -> m.outputs.contains(machineModuleName)).findAny().orElseThrow();
            cycles = ((Conjunction) modules.get(exitModule.name)).inputsHistory.keySet().stream().collect(Collectors.toMap(a -> a, a -> 0L));
        }

        void pushButton() {
            buttonPushes++;
            Queue<Pulse> pulsesToProcess = new LinkedList<>();
            pulsesToProcess.add(new Pulse(PulseType.LOW, "button", "broadcaster"));
            while (!pulsesToProcess.isEmpty()) {
                Pulse pulse = pulsesToProcess.remove();
                if (pulse.type == PulseType.LOW) lowPulses++;
                else highPulses++;
                if (pulse.targetModule.equals(exitModule.name) && pulse.type == PulseType.HIGH && cycles.get(pulse.sourceModule) == 0) {
                    cycles.put(pulse.sourceModule, buttonPushes);
                }
                if (modules.containsKey(pulse.targetModule)) pulsesToProcess.addAll(modules.get(pulse.targetModule).process(pulse));
            }
        }

        Optional<Long> getMachineTurnOnSteps() {
            if (cycles.values().stream().anyMatch(v -> v == 0)) return Optional.empty();
            return cycles.values().stream().reduce((a, b) -> lcm(a, b).orElseThrow());
        }

        long getValue() {
            return lowPulses * highPulses;
        }

        void reset() {
            buttonPushes = 0;
            modules.values().forEach(Module::reset);
        }

        private static long gcd(long a, long b) {
            return a == 0 ? b : gcd(b % a, a);
        }

        private static Optional<Long> lcm(long a, long b) {
            try {
                return Optional.of(a * b / gcd(a, b));
            } catch (ArithmeticException e) {
                return Optional.empty();
            }
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode20.class);
    private static final Network network = new Network();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/adventofcode20.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                Module module = Module.buildModule(currentLine);
                network.modules.put(module.name, module);
                lines.add(currentLine);
            }
            network.setInputsOutputs(lines);
            LOGGER.info("PART 1: {}", warmup());
            network.reset();
            LOGGER.info("PART 2: {}", turnMachineOn());
        }
    }

    private static long warmup() {
        for (int i = 0; i < 1000; i++) network.pushButton();
        return network.getValue();
    }

    private static long turnMachineOn() {
        while (true) {
            network.pushButton();
            Optional<Long> steps = network.getMachineTurnOnSteps();
            if (steps.isPresent()) return steps.get();
        }
    }
}
