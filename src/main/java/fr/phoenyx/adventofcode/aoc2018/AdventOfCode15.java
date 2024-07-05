package fr.phoenyx.adventofcode.aoc2018;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.phoenyx.models.coords.Coord2;
import fr.phoenyx.models.coords.Dir;

public class AdventOfCode15 {

    private static class Unit {
        boolean isElve;
        int hp;
        int attackPower;
        Point pos;

        Unit(boolean isElve, int hp, int attackPower, Point pos) {
            this.isElve = isElve;
            this.hp = hp;
            this.attackPower = attackPower;
            this.pos = pos;
        }

        Unit(Unit other) {
            this(other.isElve, other.hp, other.attackPower, other.pos);
        }

        void moveTo(Point target) {
            target.unit = this;
            pos.unit = null;
            pos = target;
        }

        void attack(Unit target) {
            target.hp -= attackPower;
            if (!target.isAlive()) target.pos.unit = null;
        }

        boolean isAlive() {
            return hp > 0;
        }
    }

    private static class Point extends Coord2 {
        boolean isWall;
        Unit unit;

        Point(int x, int y, boolean isWall) {
            super(x, y);
            this.isWall = isWall;
        }

        boolean isWalkable() {
            return !isWall && unit == null;
        }
    }

    private static class PointReaderOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            int compare = Integer.compare(o1.y, o2.y);
            return compare == 0 ? Integer.compare(o1.x, o2.x) : compare;
        }
    }

    private static class UnitReaderOrderComparator implements Comparator<Unit> {
        @Override
        public int compare(Unit o1, Unit o2) {
            return new PointReaderOrderComparator().compare(o1.pos, o2.pos);
        }
    }

    private static class UnitFightOrderComparator implements Comparator<Unit> {
        @Override
        public int compare(Unit o1, Unit o2) {
            int compare = Integer.compare(o1.hp, o2.hp);
            return compare == 0 ? new UnitReaderOrderComparator().compare(o1, o2) : compare;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode15.class);
    private static final int BASE_ATTACK_POWER = 3;
    private static final Set<Unit> initialUnits = new HashSet<>();
    private static final Set<Unit> units = new HashSet<>();

    private static int width;
    private static int height;
    private static Point[][] map;

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2018/adventofcode15.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            List<String> lines = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) lines.add(currentLine);
            readMap(lines);
            LOGGER.info("PART 1: {}", getBattleResult(BASE_ATTACK_POWER));
            LOGGER.info("PART 2: {}", getFirstElveWinWithoutDeathBattleResult());
        }
    }

    private static void readMap(List<String> lines) {
        width = lines.get(0).length();
        height = lines.size();
        map = new Point[width][height];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char c = line.charAt(j);
                map[j][i] = new Point(j, i, c == '#');
                if (!map[j][i].isWall && c != '.') initialUnits.add(new Unit(c == 'E', 200, BASE_ATTACK_POWER, map[j][i]));
            }
        }
    }

    private static int getBattleResult(int elveAttackModifier) {
        prepareData(elveAttackModifier);
        int round = 0;
        while (true) {
            List<Unit> sortedUnits = units.stream()
                .filter(Unit::isAlive)
                .sorted(new UnitReaderOrderComparator())
                .toList();
            for (Unit unit : sortedUnits) {
                if (!unit.isAlive()) continue;
                Set<Unit> targets = units.stream()
                    .filter(Unit::isAlive)
                    .filter(u -> unit.isElve != u.isElve)
                    .collect(Collectors.toSet());
                if (targets.isEmpty()) return units.stream().filter(Unit::isAlive).map(u -> u.hp).reduce(Integer::sum).orElseThrow() * round;
                playTurn(unit, targets);
            }
            round++;
        }
    }

    private static void prepareData(int elveAttackModifier) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) map[i][j].unit = null;
        }
        units.clear();
        for (Unit initialUnit : initialUnits) {
            Unit unit = new Unit(initialUnit);
            if (unit.isElve) unit.attackPower = elveAttackModifier;
            unit.pos.unit = unit;
            units.add(unit);
        }
    }

    private static void playTurn(Unit unit, Set<Unit> targets) {
        getAttackTarget(unit, targets).ifPresentOrElse(unit::attack, () -> getMove(unit, targets).ifPresent(target -> {
            unit.moveTo(target);
            getAttackTarget(unit, targets).ifPresent(unit::attack);
        }));
    }

    private static Optional<Unit> getAttackTarget(Unit unit, Set<Unit> targets) {
        return targets.stream()
            .filter(u -> u.pos.manhattanDistanceTo(unit.pos) == 1)
            .min(new UnitFightOrderComparator());
    }

    private static Optional<Point> getMove(Unit unit, Set<Unit> targets) {
        Set<Point> destinations = getDestinations(targets);
        Set<Point> toVisit = new HashSet<>();
        toVisit.add(unit.pos);
        Set<Point> visited = new HashSet<>(toVisit);
        while (!toVisit.isEmpty()) {
            Set<Point> next = getNextSteps(toVisit, visited);
            Optional<Point> target = next.stream()
                .filter(destinations::contains)
                .min(new PointReaderOrderComparator());
            if (target.isPresent()) return getMoveToTarget(unit, target.get());
            visited.addAll(next);
            toVisit = next;
        }
        return Optional.empty();
    }

    private static Set<Point> getDestinations(Set<Unit> targets) {
        Set<Point> destinations = new HashSet<>();
        for (Unit target : targets) {
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                int x = target.pos.x + dir.dx;
                int y = target.pos.y + dir.dy;
                if (map[x][y].isWalkable()) destinations.add(map[x][y]);
            }
        }
        return destinations;
    }

    private static Set<Point> getNextSteps(Set<Point> toVisit, Set<Point> visited) {
        Set<Point> next = new HashSet<>();
        for (Point current : toVisit) {
            for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
                int x = current.x + dir.dx;
                int y = current.y + dir.dy;
                if (map[x][y].isWalkable() && !visited.contains(map[x][y])) next.add(map[x][y]);
            }
        }
        return next;
    }

    private static Optional<Point> getMoveToTarget(Unit unit, Point target) {
        if (unit.pos.manhattanDistanceTo(target) == 1) return Optional.of(target);
        int minDistance = Integer.MAX_VALUE;
        Set<Point> best = new HashSet<>();
        for (Dir dir : Dir.FOUR_NEIGHBOURS_VALUES) {
            int x = unit.pos.x + dir.dx;
            int y = unit.pos.y + dir.dy;
            if (!map[x][y].isWalkable()) continue;
            Set<Point> toVisit = new HashSet<>();
            toVisit.add(map[x][y]);
            Set<Point> visited = new HashSet<>(toVisit);
            int steps = 0;
            while (!toVisit.isEmpty()) {
                steps++;
                Set<Point> next = getNextSteps(toVisit, visited);
                if (next.contains(target)) {
                    if (steps < minDistance) {
                        minDistance = steps;
                        best.clear();
                        best.add(map[x][y]);
                    } else if (steps == minDistance) {
                        best.add(map[x][y]);
                    }
                    break;
                }
                visited.addAll(next);
                toVisit = next;
            }
        }
        return best.stream().min(new PointReaderOrderComparator());
    }

    private static int getFirstElveWinWithoutDeathBattleResult() {
        int elveAttackPowerModifier = BASE_ATTACK_POWER;
        while (true) {
            elveAttackPowerModifier++;
            int result = getBattleResult(elveAttackPowerModifier);
            Set<Unit> aliveUnits = units.stream().filter(Unit::isAlive).collect(Collectors.toSet());
            if (aliveUnits.stream().anyMatch(u -> u.isElve) && aliveUnits.size() == initialUnits.stream().filter(u -> u.isElve).count()) return result;
        }
    }
}
