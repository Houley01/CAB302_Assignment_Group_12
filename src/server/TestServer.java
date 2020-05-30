package server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestServer {

    @Test
    public void verifyCredentialsTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "admin";
        String password = "password";

        assertEquals(true, username.equals(userCredentials[0]));

        String hashedPassword = Server.PlaintextToHashedPassword(password);

        assertEquals(true, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyCredentialsWrongPasswordTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "admin";
        String password = "passwordwrong";

        assertEquals(true, username.equals(userCredentials[0]));

        String hashedPassword = Server.PlaintextToHashedPassword(password);

        assertEquals(false, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyCredentialsWrongUsernameTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "adminwrong";
        String password = "password";

        assertEquals(false, username.equals(userCredentials[0]));

        String hashedPassword = Server.PlaintextToHashedPassword(password);

        assertEquals(true, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyCredentialsWrongTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        Server serverTester = new Server();
        String[] userCredentials = {"admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0="};
        String username = "adminwrong";
        String password = "passwordwrong";

        assertEquals(false, username.equals(userCredentials[0]));

        String hashedPassword = Server.PlaintextToHashedPassword(password);

        assertEquals(false, Server.Check(hashedPassword, userCredentials[1]));
    }

    @Test
    public void verifyTokenTest() {
        Server serverTester = new Server();

        String username = "admin";
        String authToken = serverTester.GenerateAuthToken(username);

        assertEquals(true, serverTester.CheckTokenIsValid(username, authToken));
    }

    @Test
    public void verifyTokenUserNotAuthenticatedTest() {
        Server serverTester = new Server();

        String usernameFake = "adminFake";
        String authToken = serverTester.GenerateAuthToken("admin");

        assertEquals(false, serverTester.CheckTokenIsValid(usernameFake, authToken));
    }

    @Test
    public void verifyTokenFakeTest() {
        Server serverTester = new Server();

        String username = "admin";
        String authTokenFake = "hi";
        serverTester.GenerateAuthToken(username);

        assertEquals(false, serverTester.CheckTokenIsValid(username, authTokenFake));
    }

    @Test
    public void verifyTokenExpiredTest() {
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
    public void removeTokenOnLogoutTest() {
        Server serverTester = new Server();

        String username = "admin";
        serverTester.GenerateAuthToken(username);

        // Function to add
        //serverTester.removeToken(username);

        assertEquals(false, serverTester.usersAuthenticated.containsKey(username));
    }

    @Test
    public void editUserFirstNameTest() {
        String[] userCredentials = {"1", "admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0=", "John", "Smith"};
        String[] firstNameToBeEdited = {"1", "admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0=", "Jack", "Smith"};

        // Function to add
        //String[] userCredentialsModified = Server.editUserInfo(userCredentials, firstNameToBeEdited);

        //assertEquals(true, userCredentialsModified.equals(firstNameToBeEdited));
    }

    @Test
    public void editUserLastNameTest() {
        String[] userCredentials = {"1", "admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0=", "John", "Smith"};
        String[] lastNameToBeEdited = {"1", "admin", "TNBeZf5SCG+QzxWQt21TxY0orQOFZXUY6S0RHs7/TOA=$MrRy4VfAGiWGVBahO1o0iTTXjLnNhDF+OsX9Sgbzwu0=", "Jack", "Smithers"};

        // Function to add
        //String[] userCredentialsModified = Server.editUserInfo(userCredentials, lastNameToBeEdited);

        //assertEquals(true, userCredentialsModified.equals(lastNameToBeEdited));
    }

    // Check that a billboard can be created
//    @Test
//    public void createBillboardTest() throws ParseException {
//        String[] billboardInfoToCreate = {"this is the title", "1", "current Time", "", "file location"};
//        // Function to add
//        boolean wasBillboardCreated = Server.CreateBillboard(billboardInfoToCreate);
//        assertEquals(true, wasBillboardCreated);
//    }
//
//    // Check that a billboard can be created with no required data
//    @Test
//    public void createBillboardNoDataTest() throws ParseException {
//        String[] billboardInfoToCreate = {"", "1", "current Time", "", ""};
//        // Function to add
//        boolean wasBillboardCreated = Server.CreateBillboard(billboardInfoToCreate);
//        assertEquals(false, wasBillboardCreated);
//    }

    // Check that a billboard can be scheduled
    @Test
    public void scheduleBillboardTest() {
        String[] billboardInfoToSchedule = {"1", "this is the title", "1", "current Time", "Time Modified", "file location"};

        // Function to add
        //String[] scheduledBillboard = Server.scheduleBillboard(billboardInfoToSchedule);
        //assertNotNull(scheduledBillboard);
    }

    // edit the various billboard fields and check it was successfully edited
    @Test
    public void editBillboardTest() {
        String[] billboardToEdit = {"1", "testBillboard", "1", "current Time", "Time Modified", "This is the file location"};
        String[] newBillboardInfo = {"1", "testBillboardNew", "1", "current Time", "Time Modified", "This is the new file location"};

        // Function to add
        //String[] billboardToEditModified = Server.editBillboardInfo(billboardToEdit, newBillboardInfo);
        //assertEquals(true, billboardToEditModified.equals(newBillboardInfo));
    }

    // edit the billboard text and check it was successfully edited
    @Test
    public void editBillboardTitleTest() {
        String[] billboardToEdit = {"1", "this is the title", "1", "current Time", "Time Modified", "file location"};
        String[] newTitle = {"1", "this is the new title", "1", "current Time", "Time Modified", "file location"};

        // Function to add
        //String[] billboardToEditModified = Server.editBillboardInfo(billboardToEdit, newTitle);
        //assertEquals(true, billboardToEditModified.equals(newTitle));
    }
}
