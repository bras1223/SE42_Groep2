package nl.nossa;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import nl.nossa.encryption.EncryptionController;

/**
 *
 * @author Raymond
 */
public class EncryptionFXMLController implements Initializable {

    @FXML
    private PasswordField lblPassword;
    
    
    EncryptionController encryption;

    @FXML
    private void encrypt(ActionEvent event) {
        encryption = new EncryptionController();
        encryption.encrypt(lblPassword.getText());
    }

    @FXML
    private void decrypt(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
