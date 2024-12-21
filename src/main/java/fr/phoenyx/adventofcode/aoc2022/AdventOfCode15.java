package fr.phoenyx.adventofcode.aoc2022;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.phoenyx.models.Range;
import fr.phoenyx.models.coords.Coord2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode15 {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final int MIN = 0;
    private static final int MAX = 4000000;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2022/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<Coord2> sensors = new ArrayList<>();
            List<Coord2> beacons = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine
                        .replace("Sensor at x=", "")
                        .replace(": closest beacon is at x=", ";")
                        .replace(", y=", ",");
                String[] coordinates = currentLine.split(";");
                String[] sensor = coordinates[0].split(",");
                String[] beacon = coordinates[1].split(",");
                sensors.add(new Coord2(Integer.parseInt(sensor[0]), Integer.parseInt(sensor[1])));
                beacons.add(new Coord2(Integer.parseInt(beacon[0]), Integer.parseInt(beacon[1])));
            }
            LOGGER.info("PART 1: {}", countImpossibleBeaconPositionAtRow2000000(sensors, beacons));
            LOGGER.info("PART 2: {}", getTuningFrequency(sensors, beacons));
        }
    }

    private static long countImpossibleBeaconPositionAtRow2000000(List<Coord2> sensors, List<Coord2> beacons) {
        Set<Range> impossibleBeaconsX = getImpossibleBeaconsXAtRow(2000000, sensors, beacons);
        return impossibleBeaconsX.stream().map(range -> range.length).reduce(0L, Long::sum) - beacons.stream().filter(b -> b.y == 2000000 && impossibleBeaconsX.stream().anyMatch(r -> r.isInRange(b.x))).distinct().count();
    }

    private static Set<Range> getImpossibleBeaconsXAtRow(int row, List<Coord2> sensors, List<Coord2> beacons) {
        Set<Range> impossibleBeaconsX = new HashSet<>();
        for (int i = 0; i < sensors.size(); i++) {
            Coord2 sensor = sensors.get(i);
            int distance = sensor.manhattanDistanceTo(beacons.get(i));
            Coord2 projection = new Coord2(sensor.x, row);
            int diff = distance - projection.manhattanDistanceTo(sensor);
            if (diff < 0) continue;
            Range range = Range.buildFromStartAndEndInclusive(projection.x - diff, projection.x + diff);
            addRangeWithUnions(impossibleBeaconsX, range);
        }
        return impossibleBeaconsX;
    }

    private static void addRangeWithUnions(Set<Range> ranges, Range range) {
        Range finalRange1 = range;
        Optional<Range> mergeable = ranges.stream().filter(r -> r.start <= finalRange1.end && r.end >= finalRange1.start).findAny();
        while (mergeable.isPresent()) {
            range = mergeable.get().union(range);
            ranges.remove(mergeable.get());
            Range finalRange2 = range;
            mergeable = ranges.stream().filter(r -> r.start <= finalRange2.end && r.end >= finalRange2.start).findAny();
        }
        ranges.add(range);
    }

    private static long getTuningFrequency(List<Coord2> sensors, List<Coord2> beacons) {
        for (int i = MIN; i <= MAX; i++) {
            Set<Range> ranges = getImpossibleBeaconsXAtRow(i, sensors, beacons);
            if (ranges.size() > 1) return 4000000L * ranges.stream().min(Comparator.comparingLong(r -> r.start)).map(r -> r.end).orElseThrow() + i;
        }
        throw new IllegalStateException("Didn't find tuning frequency");
    }
}
