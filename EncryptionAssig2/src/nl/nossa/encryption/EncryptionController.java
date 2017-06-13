package nl.nossa.encryption;

import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.store;

/**
 *
 * @author Raymond
 */
public class EncryptionController {

    static final int AES_KEYLENGTH = 128;
    Cipher aesCipherForEncryption;

    public EncryptionController() {
        String alias = "aeskey";
        char[] password = "password".toCharArray();
    }

    private Cipher createCipher() {
        byte[] salt = generateSalt();
        try {
            aesCipherForEncryption = Cipher.getInstance("AES");
            aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, generateKey(password, salt), new IvParameterSpec(salt));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(EncryptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private SecretKey generateKey(char[] password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password, generateSalt(), 65536, 256);
            SecretKey key = factory.generateSecret(spec);
            return key;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private byte[] generateSalt() {

        byte[] salt = new byte[AES_KEYLENGTH / 8];	// Save the IV bytes or send it in plaintext with the encrypted data so you can decrypt the data later
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(salt);
    }

    private void writeToFile() {

    }

    private String encryptData(String message) {
        byte[] byteDataToEncrypt = message.getBytes();
        byte[] byteCipherText = aesCipherForEncryption
                .doFinal(byteDataToEncrypt);
        // b64 is done differently on Android
        strCipherText = new BASE64Encoder().encode(byteCipherText);
        System.out.println("Cipher Text generated using AES is "
                + strCipherText);
    }
}
