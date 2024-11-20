package com.example.beachapp;

import org.junit.Test;
import com.example.beachapp.RegisterActivity;
import static org.junit.Assert.*;

import java.util.Random;

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
    @Test
    public void testInvalidCredentialsAndPassword(){
        String email = "gmail.com";
        String password = "000";
        assertFalse(validateCredentials(email, password));
    }
    @Test
    public void testRandomInvalidCredentials() {
        String invalidEmail = generateRandomInvalidEmail();
        String invalidPassword = generateRandomInvalidPassword();
        System.out.println("Testing with invalid email: " + invalidEmail);
        System.out.println("Testing with invalid password: " + invalidPassword);
        assertFalse(validateCredentials(invalidEmail, invalidPassword));
    }
    private String generateRandomInvalidEmail() {
        Random random = new Random();
        int choice = random.nextInt(3);
        String username = "user" + random.nextInt(1000);
        String domain = "example" + random.nextInt(1000) + ".com";
        switch (choice) {
            case 0:
                return username + domain;
            case 1:
                return username + "@";
            case 2:
                return "@" + domain;
            default:
                return "";
        }
    }

    private String generateRandomInvalidPassword() {
        Random random = new Random();
        int passwordLength = random.nextInt(6);
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            char c = (char) (random.nextInt(94) + 33);
            password.append(c);
        }
        return password.toString();
    }
}