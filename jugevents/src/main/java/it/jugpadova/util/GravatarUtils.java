package it.jugpadova.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for building Gravatar URLs.
 *
 * @author lucio
 */
public class GravatarUtils {

    public static final String RATING_G = "g";
    public static final String RATING_PG = "pg";
    public static final String RATING_R = "r";
    public static final String RATING_X = "x";
    public static final String DEFAULT_IDENTICON = "identicon";
    public static final String DEFAULT_MONSTERID = "monsterid";
    public static final String DEFAULT_WAVATAR = "wavatar";

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String getUrl(String email, Integer size, String rating, String defaultAvatar) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder("http://www.gravatar.com/avatar/");
        sb.append(md5Hex(email)).append(".jpg");
        Utilities.appendUrlParameter(sb, "s", size.toString(), true);
        Utilities.appendUrlParameter(sb, "r", rating, true);
        Utilities.appendUrlParameter(sb, "d", defaultAvatar, true);
        return sb.toString();
    }

}
