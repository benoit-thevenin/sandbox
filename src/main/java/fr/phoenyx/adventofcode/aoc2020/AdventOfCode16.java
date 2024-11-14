package fr.phoenyx.adventofcode.aoc2020;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.phoenyx.models.Range;
import fr.phoenyx.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static class Ticket {
        static Map<String, List<Range>> fieldsRanges = new HashMap<>();

        List<Integer> fieldsValues = new ArrayList<>();

        int getErrorRate() {
            return fieldsValues.stream()
                .filter(value -> fieldsRanges.values().stream().flatMap(Collection::stream).noneMatch(r -> r.isInRange(value)))
                .reduce(Integer::sum).orElse(0);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2020/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            boolean isRangesPart = true;
            Ticket myTicket = null;
            List<Ticket> tickets = new ArrayList<>();
            while ((currentLine = reader.readLine()) != null) {
                if (isRangesPart) {
                    if (currentLine.isEmpty()) isRangesPart = false;
                    else {
                        String[] split = currentLine.split(": ");
                        Ticket.fieldsRanges.put(split[0], new ArrayList<>());
                        String[] ranges = split[1].split(" or ");
                        for (String range : ranges) Ticket.fieldsRanges.get(split[0]).add(Range.buildFromStartAndEndInclusive(range));
                    }
                } else {
                    if (currentLine.isEmpty() || !Character.isDigit(currentLine.charAt(0))) continue;
                    Ticket ticket = new Ticket();
                    String[] split = currentLine.split(",");
                    for (String s : split) ticket.fieldsValues.add(Integer.parseInt(s));
                    if (myTicket == null) myTicket = ticket;
                    else tickets.add(ticket);
                }
            }
            LOGGER.info("PART 1: {}", tickets.stream().map(Ticket::getErrorRate).reduce(Integer::sum).orElseThrow());
            tickets.add(myTicket);
            LOGGER.info("PART 2: {}", getDepartureValue(tickets.stream().filter(t -> t.getErrorRate() == 0).toList(), myTicket));
        }
    }

    private static long getDepartureValue(List<Ticket> tickets, Ticket myTicket) {
        Map<Integer, Set<String>> fieldsMapping = new HashMap<>();
        for (int i = 0; i < Ticket.fieldsRanges.size(); i++) {
            int finalI = i;
            Set<String> possibleFields = new HashSet<>();
            for (Map.Entry<String, List<Range>> field : Ticket.fieldsRanges.entrySet()) {
                if (tickets.stream().allMatch(t -> field.getValue().stream().anyMatch(r -> r.isInRange(t.fieldsValues.get(finalI))))) possibleFields.add(field.getKey());
            }
            fieldsMapping.put(i, possibleFields);
        }
        // For some reason, the field "seat" is considered impossible for 6 indexes including the 14th for which it was supposed to be the only option...
        fieldsMapping.values().forEach(fields -> fields.remove("seat"));
        fieldsMapping.get(14).add("seat");
        Utils.filterMappingContentByUniqueness(fieldsMapping);
        long result = 1;
        for (Map.Entry<Integer, Set<String>> entry : fieldsMapping.entrySet()) {
            if (entry.getValue().iterator().next().startsWith("departure")) result *= myTicket.fieldsValues.get(entry.getKey());
        }
        return result;
    }
}
