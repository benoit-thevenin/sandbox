package fr.phoenyx.adventofcode.aoc2021;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdventOfCode16 {

    private static class Packet {
        private static final int VERSION_BITS = 3;
        private static final int ID_BITS = 3;
        private static final int VALUE_BLOCK_BITS = 4;
        private static final int LENGTH_BITS = 15;
        private static final int SUBPACKETS_BITS = 11;

        static String binaryTransmission;

        int version;
        int id;
        int startIndex;
        int endIndex;
        long value;
        List<Packet> subpackets = new ArrayList<>();

        Packet(int startIndex) {
            this.startIndex = startIndex;
            this.endIndex = startIndex;
            readPacket();
        }

        void readPacket() {
            readVersion();
            readId();
            if (id == 4) readValue();
            else readSubpackets();
        }

        void readVersion() {
            for (int i = 0; i < VERSION_BITS; i++) {
                if (binaryTransmission.charAt(endIndex) == '1') version += (1 << (VERSION_BITS - 1 - i));
                endIndex++;
            }
        }

        void readId() {
            for (int i = 0; i < ID_BITS; i++) {
                if (binaryTransmission.charAt(endIndex) == '1') id += (1 << (ID_BITS - 1 - i));
                endIndex++;
            }
        }

        void readValue() {
            StringBuilder sb = new StringBuilder();
            boolean isOver = false;
            while (!isOver) {
                isOver = binaryTransmission.charAt(endIndex) == '0';
                endIndex++;
                for (int i = 0; i < VALUE_BLOCK_BITS; i++) {
                    sb.append(binaryTransmission.charAt(endIndex));
                    endIndex++;
                }
            }
            value = Long.parseLong(sb.toString(), 2);
        }

        void readSubpackets() {
            boolean isLength = binaryTransmission.charAt(endIndex) == '0';
            endIndex++;
            int bits = isLength ? LENGTH_BITS : SUBPACKETS_BITS;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bits; i++) {
                sb.append(binaryTransmission.charAt(endIndex));
                endIndex++;
            }
            int length = Integer.parseInt(sb.toString(), 2);
            if (isLength) {
                int read = 0;
                while (read < length) {
                    Packet packet = new Packet(endIndex);
                    read += packet.endIndex - packet.startIndex;
                    endIndex = packet.endIndex;
                    subpackets.add(packet);
                }
            } else {
                for (int i = 0; i < length; i++) {
                    Packet packet = new Packet(endIndex);
                    endIndex = packet.endIndex;
                    subpackets.add(packet);
                }
            }
        }

        int getVersionSum() {
            return version + subpackets.stream().map(Packet::getVersionSum).reduce(0, Integer::sum);
        }

        long getValue() {
            if (id == 0) return subpackets.stream().map(Packet::getValue).reduce(0L, Long::sum);
            if (id == 1) return subpackets.stream().map(Packet::getValue).reduce(1L, (a, b) -> a * b);
            if (id == 2) return subpackets.stream().map(Packet::getValue).reduce(Math::min).orElseThrow();
            if (id == 3) return subpackets.stream().map(Packet::getValue).reduce(Math::max).orElseThrow();
            if (id == 4) return value;
            if (id == 5) return subpackets.get(0).getValue() > subpackets.get(1).getValue() ? 1 : 0;
            if (id == 6) return subpackets.get(0).getValue() < subpackets.get(1).getValue() ? 1 : 0;
            return subpackets.get(0).getValue() == subpackets.get(1).getValue() ? 1 : 0;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AdventOfCode16.class);

    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/fr/phoenyx/adventofcode/aoc2021/adventofcode16.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                Packet packet = buildPacket(currentLine);
                LOGGER.info("PART 1: {}", packet.getVersionSum());
                LOGGER.info("PART 2: {}", packet.getValue());
            }
        }
    }

    private static Packet buildPacket(String hexTransmission) {
        StringBuilder sb = new StringBuilder(new BigInteger(hexTransmission, 16).toString(2));
        while (sb.length() != 4 * hexTransmission.length()) sb.insert(0, '0');
        Packet.binaryTransmission = sb.toString();
        return new Packet(0);
    }
}
