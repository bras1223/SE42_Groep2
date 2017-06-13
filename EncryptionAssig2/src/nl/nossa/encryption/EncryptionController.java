package nl.nossa.encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Raymond
 */
public class EncryptionController {

    private static final Logger logger = Logger.getLogger(EncryptionController.class.getSimpleName());

    private static final int AES_KEYLENGTH = 128;
    private Cipher cipher;

    public EncryptionController() {
    }

    public void encrypt(String password) {
        char[] passwordArray = password.toCharArray();
        createCipher(passwordArray);
        encryptData("test");
    }

    private void createCipher(char[] password) {
        byte[] salt = generateSalt();
        try {
            cipher = Cipher.getInstance("AES");
            IvParameterSpec spec = cipher.getParameters().getParameterSpec(IvParameterSpec.class);;

            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password, salt), spec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | InvalidParameterSpecException ex) {
            logger.log(Level.SEVERE, "An error occured while creating the cipher", ex);
        }
    }

    private SecretKey generateKey(char[] password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password, generateSalt(), 100, 128);
            SecretKey key = factory.generateSecret(spec);
            return key;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, "An error occured while generating the Key", ex);
        }
        return null;
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[AES_KEYLENGTH / 8];	// Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(salt);
        return salt;
    }

    private void writeToFile() {

    }

    private void encryptData(String message) {
        try {
            byte[] byteDataToEncrypt = message.getBytes();
            byte[] byteCipherText = cipher.doFinal(byteDataToEncrypt);

            System.out.println("Cipher Text generated using AES is "
                    + byteCipherText);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            logger.log(Level.SEVERE, "An error occured while encrypting the Data.", ex);
        }
    }
}
