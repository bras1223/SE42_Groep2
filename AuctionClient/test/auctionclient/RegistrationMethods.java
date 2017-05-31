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
    
    public static User getUser(String email) {
        Registration port = service.getRegistrationPort();
        return port.getUser(email);
    }

    public static User registerUser(String email) {
        Registration port = service.getRegistrationPort();
        return port.registerUser(email);
    }
    
}
