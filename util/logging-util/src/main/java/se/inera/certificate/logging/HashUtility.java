package se.inera.certificate.logging;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class HashUtility {
    
    private static final Logger LOGGER = LoggerFactory
            .getLogger(HashUtility.class);
    
    private static final String DIGEST = "SHA-256";
    private static final MessageDigest msgDigest;

    public static final String EMPTY = "EMPTY";
    private static final String NO_HASH_VALUE = "NO-HASH-VALUE";

    static {
        MessageDigest tmp = null;
        try {
            tmp = MessageDigest.getInstance(DIGEST);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("MessageDigest instantiation failed", e);
        }
        msgDigest = tmp;
    }

    public static String hash(String payload) {
        if (StringUtils.isEmpty(payload)) {
            return EMPTY;
        }
        
        if (msgDigest == null) {
            LOGGER.error("Hashing not working due to MessageDigest not being instantiated");
            return NO_HASH_VALUE;
        }

        try {
            msgDigest.update(payload.getBytes("UTF-8"));
            byte[] digest = msgDigest.digest();
            return new String(Hex.encodeHex(digest));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
