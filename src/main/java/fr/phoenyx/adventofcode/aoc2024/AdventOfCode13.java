package fr.phoenyx.adventofcode.aoc2024;

import fr.phoenyx.utils.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventOfCode13 {

    private record Button(long dx, long dy) {}

    private static class Machine {
        Button A, B;
        long x, y;

        long getMinTokenUsageForPrize() {
            long det = (long) MathUtils.getDeterminant(new double[][]{{A.dx, B.dx}, {A.dy, B.dy}});
            long aPush = (B.dy * x - B.dx * y) / det;
            long bPush = (A.dx * y - A.dy * x) / det;
            return isValid(aPush, bPush) ? 3 * aPush + bPush : 0;
        }

        boolean isValid(long aPush, long bPush) {
            return aPush * A.dx + bPush * B.dx == x && aPush * A.dy + bPush * B.dy == y;
        }

        void applyPositionConversion() {
            x += 10000000000000L;
            y += 10000000000000L;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode13.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2024/adventofcode13.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Machine> machines = new ArrayList<>();
            Machine current = new Machine();
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.isEmpty()) current = new Machine();
                else if (currentLine.startsWith("Button")) {
                    String[] split = currentLine.split("Button [AB]: ")[1].split(", ");
                    Button button = new Button(Integer.parseInt(split[0].split("\\+")[1]), Integer.parseInt(split[1].split("\\+")[1]));
                    if (currentLine.contains("A")) current.A = button;
                    else current.B = button;
                } else {
                    String[] split = currentLine.replace("Prize: ", "").split(", ");
                    current.x = Integer.parseInt(split[0].split("=")[1]);
                    current.y = Integer.parseInt(split[1].split("=")[1]);
                    machines.add(current);
                }
            }
            LOGGER.info("PART 1: {}", machines.stream().map(Machine::getMinTokenUsageForPrize).reduce(Long::sum).orElseThrow());
            machines.forEach(Machine::applyPositionConversion);
            LOGGER.info("PART 2: {}", machines.stream().map(Machine::getMinTokenUsageForPrize).reduce(Long::sum).orElseThrow());
        }
    }
}
