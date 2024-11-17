package com.example.beachapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class userRegisterUnitTest {
    public boolean validateCredentials(String email, String password) {
        return email != null && email.contains("@") && password != null && password.length() >= 6;
    }
    @Test
    public void testValidcredentials() {  //valid input
        String email="david@gmail.com";
        String password= "testPassword";
        assertTrue(validateCredentials(email, password));
    }
    @Test
    public void testInValidCredentials(){
        String email = "testexample.com"; // Missing '@'
        String password = "password123";

        assertFalse(validateCredentials(email, password));
    }
    @Test
    public void testInvalidPassword(){
        String email = "david@gmail.com";
        String password = "123";
        assertFalse(validateCredentials(email, password));
    }
}