package nl.nossa.application3;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.nossa.constants.Constants;

/**
 *
 * @author Raymond
 */
public class SignedChecker {

    private static final Logger logger = Logger.getLogger(SignedChecker.class.getName());

    private String content;
    private byte[] signature;

    public SignedChecker() {
        // Nothing in constructor is nice.
    }

    public boolean checkIfSigned(String fileName) {

        try {
            readSignedFile(fileName);
            PublicKey publicKey = readPublicKeyFile();
            Signature sig = Signature.getInstance(Constants.SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(content.getBytes());
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            logger.log(Level.SEVERE, "En error occured while checking if the the given input is Signed.", ex);
        }
        return false;
    }

    private void readSignedFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            signature = (byte[]) ois.readObject();
            content = (String) ois.readObject();
        } catch (FileNotFoundException ex) {
            logger.log(Level.SEVERE, "En error occured while checking if the File is Signed", ex);
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "En error occured while checking if the File is Signed", ex);
        }
    }

    private PublicKey readPublicKeyFile() {
        try {
            byte[] data = Files.readAllBytes(Paths.get(Constants.PUBLIC_KEY_FILE_NAME));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance(Constants.ALGORITHM);
            return kf.generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.log(Level.SEVERE, "En error occured while reading the public key file.", ex);
        }
        return null;
    }

}
