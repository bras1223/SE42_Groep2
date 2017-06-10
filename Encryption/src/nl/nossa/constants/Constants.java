package nl.nossa.constants;

import java.util.logging.Logger;
import nl.nossa.application1.KeyGenerator;

/**
 *
 * @author Raymond
 */
public class Constants {
    
    public static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    public static final String ALGORITHM = "RSA";
    public static final int KEY_BIT_SIZE = 1024;
    
    public static final String PUBLIC_KEY_FILE_NAME = "public.txt";
    public static final String PRIVATE_KEY_FILE_NAME = "private.txt";
    public static final String CONTENTS_FILE_NAME = "contents.txt";
    
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    
    
    private Constants() {
        throw new UnsupportedOperationException("Class may not be instantiated.");
    }
}
