package nl.nossa;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nl.nossa.decryption.DecryptionController;
import nl.nossa.encryption.EncryptionController;

/**
 *
 * @author Raymond
 */
public class EncryptionFXMLController implements Initializable {

    @FXML
    private PasswordField lblPassword;
    @FXML
    private TextField lblMessage;
    
    
    EncryptionController encryption;
    DecryptionController decryption;

    @FXML
    private void encrypt(ActionEvent event) {   
        encryption.encrypt(lblPassword.getText(), lblMessage.getText());
    }

    @FXML
    private void decrypt(ActionEvent event) {  
        lblMessage.setText(decryption.decrypt(lblPassword.getText()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        encryption = new EncryptionController();
        decryption = new DecryptionController();    
    }

}
