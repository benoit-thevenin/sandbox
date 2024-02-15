package fr.phoenyx.adventofcode.aoc2016;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode10 {

    private record Microchip(int id) {}

    private static class Bot {
        final int id;
        boolean isLowBot;
        int low;
        boolean isHighBot;
        int high;
        Microchip first;
        Microchip second;

        Bot(int id) {
            this.id = id;
        }

        boolean isBotPart1() {
            return first.id == 61 && second.id == 17 || first.id == 17 && second.id == 61;
        }

        boolean isNext() {
            return first != null && second != null;
        }

        void receive(Microchip microchip) {
            if (first == null) first = microchip;
            else if (second == null) second = microchip;
            else throw new IllegalArgumentException("Bot " + id + " can't take more microchips");
        }
    }

    private static class Output {
        Microchip microchip;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode10.class);
    private static final Map<Integer, Bot> bots = new HashMap<>();
    private static final Map<Integer, Output> outputs = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2016/adventofcode10.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] split = currentLine.split(" ");
                int id = Integer.parseInt(split[1]);
                if (currentLine.startsWith("bot")) {
                    Bot bot = bots.getOrDefault(id, new Bot(id));
                    bot.isLowBot = "bot".equals(split[5]);
                    bot.low = Integer.parseInt(split[6]);
                    bot.isHighBot = "bot".equals(split[10]);
                    bot.high = Integer.parseInt(split[11]);
                    bots.put(id, bot);
                    if (!bot.isLowBot) outputs.put(bot.low, new Output());
                    if (!bot.isHighBot) outputs.put(bot.high, new Output());
                } else {
                    int botId = Integer.parseInt(split[5]);
                    Bot bot = bots.getOrDefault(botId, new Bot(Integer.parseInt(split[5])));
                    if (bot.first == null) bot.first = new Microchip(id);
                    else bot.second = new Microchip(id);
                    bots.put(botId, bot);
                }
            }
            LOGGER.info("PART 1: {}", getBotComparing().id);
            LOGGER.info("PART 2: {}", getOutputsValue());
        }
    }

    private static void processBot(Bot bot) {
        Microchip low = bot.first.id < bot.second.id ? bot.first : bot.second;
        Microchip high = bot.first.id > bot.second.id ? bot.first : bot.second;
        if (bot.isLowBot) bots.get(bot.low).receive(low);
        else outputs.get(bot.low).microchip = low;
        if (bot.isHighBot) bots.get(bot.high).receive(high);
        else outputs.get(bot.high).microchip =high;
        bot.first = null;
        bot.second = null;
    }

    private static Bot getBotComparing() {
        while (true) {
            Bot current = bots.values().stream().filter(Bot::isNext).findFirst().orElseThrow();
            if (current.isBotPart1()) return current;
            processBot(current);
        }
    }

    private static int getOutputsValue() {
        Bot next = bots.values().stream().filter(Bot::isNext).findFirst().orElse(null);
        while (next != null) {
            processBot(next);
            next = bots.values().stream().filter(Bot::isNext).findFirst().orElse(null);
        }
        return outputs.get(0).microchip.id * outputs.get(1).microchip.id * outputs.get(2).microchip.id;
    }
}
