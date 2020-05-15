package server;

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

    @Test
    public void verifyToken() {
        Server serverTester = new Server();

        String username = "admin";
        String authToken = serverTester.generateAuthToken(username);

        assertEquals(true, serverTester.checkTokenIsValid(username, authToken));
    }

    @Test
    public void verifyTokenUserNotAuthenticated() {
        Server serverTester = new Server();

        String usernameFake = "adminFake";
        String authToken = serverTester.generateAuthToken("admin");

        assertEquals(false, serverTester.checkTokenIsValid(usernameFake, authToken));
    }

    @Test
    public void verifyTokenFake() {
        Server serverTester = new Server();

        String username = "admin";
        String authTokenFake = "hi";
        serverTester.generateAuthToken(username);

        assertEquals(false, serverTester.checkTokenIsValid(username, authTokenFake));
    }

    @Test
    public void verifyTokenExpired() {
        Server serverTester = new Server();

        String username = "admin";
        String authToken = "x8HpfGwROzSEQOaD2sWTW3wgKq_3d2RB";

        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Expiry time of auth token. This will be less than the current time of the time to check for fail
        long authTokenExpiryTime = currentTime - 1;

        String expiry = String.valueOf(authTokenExpiryTime);

        serverTester.usersAuthenticated.put(username,new String[] {authToken, expiry});

        assertEquals(false, serverTester.checkTokenIsValid(username, authToken));
    }
}
