package fr.phoenyx.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    private Utils() {
        // Not mean't to be instanciated
    }

    public static String getHexadecimalMD5Hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            BigInteger bi = new BigInteger(1, digest);
            String format = "%0" + (digest.length << 1) + "x";
            return String.format(format, bi);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("The MD5 algorithm should exist");
        }
    }

    public static Map<Character, Integer> getLetterCount(String string) {
        Map<Character, Integer> letterCount = new HashMap<>();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (letterCount.containsKey(c)) letterCount.put(c, letterCount.get(c) + 1);
            else letterCount.put(c, 1);
        }
        return letterCount;
    }

    public static void filterMappingContentByUniqueness(Map<Integer, Set<String>> map) {
        while (map.values().stream().anyMatch(fields -> fields.size() > 1)) {
            Set<String> found = map.values().stream()
                    .filter(o -> o.size() == 1).flatMap(Collection::stream).collect(Collectors.toSet());
            map.values().stream().filter(o -> o.size() > 1).forEach(o -> o.removeAll(found));
        }
    }
}
