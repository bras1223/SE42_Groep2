/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.nossa.application2;

/**
 *
 * @author Raymond
 */
public class Main {
    
    private static final String SIGNER_NAME = "Raymond";
    
    public static void main(String[] args) {
        KeySigner signer = new KeySigner();
        
        signer.signKey(SIGNER_NAME);
    }
    
}
