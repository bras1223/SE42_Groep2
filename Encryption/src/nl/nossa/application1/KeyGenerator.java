package nl.nossa.application1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.nossa.constants.Constants;

/**
 *
 * @author Raymond
 */
public class KeyGenerator {

    private static final Logger logger = Logger.getLogger(KeyGenerator.class.getName());
    
    /**
     * Intializes the Keygenrator.
     * 
     */
    public KeyGenerator() {

    }

    /**
     * Method that generates the Public and Private RSA key.
     * 
     */
    public void generateKeys() {
        try {
            SecureRandom random = SecureRandom.getInstance(Constants.SECURE_RANDOM_ALGORITHM);
            KeyPairGenerator generator = KeyPairGenerator.getInstance(Constants.ALGORITHM);
            generator.initialize(Constants.KEY_BIT_SIZE, random);
            
            KeyPair keys = generator.genKeyPair();
            
            persistPrivateKey(keys.getPrivate().getEncoded());
            persistPublicKey(keys.getPublic().getEncoded());
            logger.log(Level.INFO, "Done with generating persisting Public and Private Key.");
            
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, "En error occured while generating the Public and Private Key");
        }
    }

    private void persistPrivateKey(byte[] privateKeyBytes) {
        try (OutputStream fos = new FileOutputStream(Constants.PRIVATE_KEY_FILE_NAME)){
            fos.write(privateKeyBytes);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error has occured while persisting the Private Key.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error has occured while persisting the Private Key.");
        } 
    }
    
    private void persistPublicKey(byte[] publicKeyBytes) {
        try (OutputStream fos = new FileOutputStream(Constants.PUBLIC_KEY_FILE_NAME)){
            fos.write(publicKeyBytes);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error has occured while persisting the Private Key.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error has occured while persisting the Private Key.");
        } 
    }

}
