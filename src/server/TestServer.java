package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestServer {

    @Test
    public void VerifyCredentialsTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "admin";
        String password = "password";

        assertEquals(true, username.equals(userCredentials[0]));

        String hashedPassword = serverTester.PlaintextToHashedPassword(password);

        assertEquals(true, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void VerifyCredentialsWrongPasswordTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "admin";
        String password = "passwordwrong";

        assertEquals(true, username.equals(userCredentials[0]));

        String hashedPassword = serverTester.PlaintextToHashedPassword(password);

        assertEquals(false, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void VerifyCredentialsWrongUsernameTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "adminwrong";
        String password = "password";

        assertEquals(false, username.equals(userCredentials[0]));

        String hashedPassword = serverTester.PlaintextToHashedPassword(password);

        assertEquals(true, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void VerifyCredentialsWrongTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "adminwrong";
        String password = "passwordwrong";

        assertEquals(false, username.equals(userCredentials[0]));

        String hashedPassword = serverTester.PlaintextToHashedPassword(password);

        assertEquals(false, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void VerifyTokenTest() {
        Server serverTester = new Server();

        String username = "admin";
        String authToken = serverTester.GenerateAuthToken(username);

        assertEquals(true, serverTester.CheckTokenIsValid(username, authToken));
    }

    @Test
    public void VerifyTokenUserNotAuthenticatedTest() {
        Server serverTester = new Server();

        String usernameFake = "adminFake";
        String authToken = serverTester.GenerateAuthToken("admin");

        assertEquals(false, serverTester.CheckTokenIsValid(usernameFake, authToken));
    }

    @Test
    public void VerifyTokenFakeTest() {
        Server serverTester = new Server();

        String username = "admin";
        String authTokenFake = "hi";
        serverTester.GenerateAuthToken(username);

        assertEquals(false, serverTester.CheckTokenIsValid(username, authTokenFake));
    }

    @Test
    public void VerifyTokenExpiredTest() {
        Server serverTester = new Server();

        String username = "admin";
        String authToken = "x8HpfGwROzSEQOaD2sWTW3wgKq_3d2RB";

        // Current time in milliseconds
        long currentTime = System.currentTimeMillis();

        // Expiry time of auth token. This will be less than the current time of the time to check for fail
        long authTokenExpiryTime = currentTime - 1;

        String expiry = String.valueOf(authTokenExpiryTime);

        serverTester.usersAuthenticated.put(username,new String[] {authToken, expiry});

        assertEquals(false, serverTester.CheckTokenIsValid(username, authToken));
    }


    // Test confirms that the token was removed after logout
    @Test
    public void RemoveTokenOnLogoutTest() throws IOException {
        Server serverTester = new Server();

        String username = "admin";
        serverTester.GenerateAuthToken(username);

        // Function to add
        serverTester.Logout(username);

        assertEquals(false, serverTester.usersAuthenticated.containsKey(username));
    }
}
