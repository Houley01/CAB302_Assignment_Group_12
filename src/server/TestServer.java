package server;

import controlpanel.controller;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServer {

    @Test
    public void verifyCredentials() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "admin";
        String password = "password";

        assertEquals(true, username.equals(userCredentials[0]));

        String hashedPassword = Server.plaintextToHashedPassword(password);

        assertEquals(true, Server.check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyCredentialsWrongPassword() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "admin";
        String password = "passwordwrong";

        assertEquals(true, username.equals(userCredentials[0]));

        String hashedPassword = Server.plaintextToHashedPassword(password);

        assertEquals(false, Server.check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyCredentialsWrongUsername() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "adminwrong";
        String password = "password";

        assertEquals(false, username.equals(userCredentials[0]));

        String hashedPassword = Server.plaintextToHashedPassword(password);

        assertEquals(true, Server.check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyCredentialsWrong() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "adminwrong";
        String password = "passwordwrong";

        assertEquals(false, username.equals(userCredentials[0]));

        String hashedPassword = Server.plaintextToHashedPassword(password);

        assertEquals(false, Server.check(hashedPassword, userCredentials[1]));
    }
}
