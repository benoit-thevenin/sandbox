package fr.phoenyx.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    private Utils() {
        // Not mean't to be instanciated
    }

    public static String getHexadecimalMD5Hash(String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes());
        byte[] digest = md.digest();
        BigInteger bi = new BigInteger(1, digest);
        String format = "%0" + (digest.length << 1) + "x";
        return String.format(format, bi);
    }
}
