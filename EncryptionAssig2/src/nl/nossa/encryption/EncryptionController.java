package nl.nossa.encryption;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import nl.nossa.EncryptionValue;
import nl.nossa.constants.Constants;

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

    public void encrypt(String password, String message) {
        char[] passwordArray = password.toCharArray();
        byte[] salt = createCipher(password);
        byte[] encryptedMessage = encryptData(message);
        persistEncryptedMessage(salt, encryptedMessage);
    }

    private byte[] createCipher(String password) {
        byte[] salt = generateSalt();
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password, salt));
            return salt;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException ex) {
            logger.log(Level.SEVERE, "An error occured while creating the cipher", ex);
        }
        return null;
    }

    private SecretKey generateKey(String password, byte[] salt) {
        try {
            byte[] key = (password).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            return secretKey;
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[AES_KEYLENGTH / 8];	// Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(salt);
        return salt;
    }

    private void persistEncryptedMessage(byte[] salt, byte[] encryptedMessage) {
        EncryptionValue data = new EncryptionValue(salt, encryptedMessage);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Constants.ENCRYPTED_FILE_NAME))){
            oos.writeObject(data);
            logger.log(Level.INFO, "Persisted data.");
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error has occured while persisting the Private Key.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error has occured while persisting the Private Key.");
        } 
    }

    private byte[] encryptData(String message) {
        try {
            byte[] byteDataToEncrypt = message.getBytes();
            byte[] byteCipherText = cipher.doFinal(byteDataToEncrypt);

            return byteCipherText;
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            logger.log(Level.SEVERE, "An error occured while encrypting the Data.", ex);
        }
        return null;
    }
}
