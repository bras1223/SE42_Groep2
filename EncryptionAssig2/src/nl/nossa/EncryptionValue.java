package nl.nossa;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luuk
 */
public class EncryptionValue implements Serializable{
    private byte[] salt;
    private byte [] message;
    
    public EncryptionValue (byte[] salt, byte[] message) {
        this.salt = salt;
        this.message = message;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getMessage() {
        return message;
    }
    
    
}
