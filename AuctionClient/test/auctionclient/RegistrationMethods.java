/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auctionclient;

/**
 *
 * @author Luuk
 */
public class RegistrationMethods {

    private static final RegistrationService service = new RegistrationService();
    private static final Registration port = service.getRegistrationPort();
    
    public static User getUser(String email) {
        return port.getUser(email);
    }

    public static User registerUser(String email) {
        return port.registerUser(email);
    }
    
}
