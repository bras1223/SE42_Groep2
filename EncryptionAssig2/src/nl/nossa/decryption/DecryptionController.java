package nl.nossa.decryption;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import nl.nossa.encryption.EncryptionController;

/**
 *
 * @author Raymond
 */
public class DecryptionController {

    private static final Logger logger = Logger.getLogger(EncryptionController.class.getSimpleName());
        
    private Cipher cipher;
    
    public DecryptionController() {
        
    }
    
    public String decrypt(String password) {
        return createCipher(password);
    }
    
    private String createCipher(String password) {
        EncryptionValue encryptionValue = readFile();
    
        try {
            cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, generateKey(password, encryptionValue.getSalt()));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException ex) {
            logger.log(Level.SEVERE, "An error occured while creating the cipher", ex);
        }
        
        return decryptData(encryptionValue.getMessage());
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
    
    private EncryptionValue readFile() {
        EncryptionValue data = null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Constants.ENCRYPTED_FILE_NAME))) {
            data = (EncryptionValue) ois.readObject();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error occured while reading the Private Key File.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error occured while reading the Private Key File.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DecryptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    private String decryptData(byte[] message) {
        try {
            byte[] byteDecryptedText = cipher.doFinal(message);
            System.out.println(" Decrypted Text message is " + new String(byteDecryptedText));
            return new String(byteDecryptedText);	
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            logger.log(Level.SEVERE, "An error occured while encrypting the Data.", ex);
        }
        return null;
    }
}
