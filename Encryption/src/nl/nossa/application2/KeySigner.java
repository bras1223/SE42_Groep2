package nl.nossa.application2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.nossa.constants.Constants;

/**
 *
 * @author Raymond
 */
public class KeySigner {

    private static final Logger logger = Logger.getLogger(KeySigner.class.getName());

    public KeySigner() {

    }

    public void signKey(String signerName) {
        try {
            KeyFactory factory = KeyFactory.getInstance(Constants.ALGORITHM);
            byte[] privateKeyBytes = readPrivateKeyFile();
            PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            
            Signature signature = Signature.getInstance(Constants.SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            String contents = readContentsFile();
            signature.update(contents.getBytes());
            byte[] bytes = signature.sign();
            
            persistSignedFile(signerName, bytes, contents);
            
        } catch (InvalidKeySpecException | InvalidKeyException | NoSuchAlgorithmException | SignatureException ex) {
            logger.log(Level.SEVERE, "An error occured while creating a signature.");
        }
    }

    private byte[] readPrivateKeyFile() {
        byte[] bytes = null;

        try (FileInputStream fis = new FileInputStream(Constants.PRIVATE_KEY_FILE_NAME)) {
            bytes = new byte[(int) fis.getChannel().size()];
            fis.read(bytes);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error occured while reading the Private Key File.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error occured while reading the Private Key File.");
        }
        return bytes;
    }

    private String readContentsFile() {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader budRead = new BufferedReader(new FileReader(Constants.CONTENTS_FILE_NAME))) {
            String readedLine;
            while ((readedLine = budRead.readLine()) != null) {
                builder.append(readedLine);
            }
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error occured while reading the Contents File.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error occured while reading the Contents File.");
        }

        return builder.toString();
    }
    
    private void persistSignedFile(String name, byte[] bytes, String content) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(String.format("contents_Signed by %s.txt", name)))) {
            oos.writeObject(bytes);
            oos.writeObject(content);
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "An error occured while persisting the signed File.");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error occured while persisting the signed File.");
        }
    }

}
