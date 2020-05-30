package controlpanel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class TestControlPanel {
    /*
    * The following test are for testing the Control Panel packages
    */
//    JFrame test;

    /* Test 1 though 4 test inputs in the Login sections */

    /*
    * Login Test 1 gives the correct username and password to the server
    * Username: "john1"
    * Password: "Password.1"
    */
    /* @Test
    * public void loginTest1() {
    *   assertEquals("Success Login", sendLoginInfo("john1", "Password.1");
    * }
    */

    /*
    * Login Test 2 gives the right username but gives the wrong password
    * Username: "john1"
    * Password: "wrong.1"
    */
    /* @Test
    * public void loginTest2() {
    *  assertEquals("Incorrect Username Or Password", sendLoginInfo("john1", "wrong.1");
     * }
    */

    /*
    * Login Test 3 gives the wong username
    * and gives the correct password
    * Username: "wrong"
    * Password: "Password.1"
    */
    /* @Test
    * public void loginTest3() {
    *   assertEquals("Incorrect Username Or Password", sendLoginInfo("wrong", "Password.1");
    * }
    */

    /*
     * Login Test 4 gives no username and no password
     * Username: ""
     * Password: ""
     */
    /* @Test
     * public void loginTest3() {
     *   assertEquals("Incorrect Username Or Password", sendLoginInfo("", "");
     * }
     */

    /* Hash Testing
    * Test 5 test: that NO two hash are the same.
    * By hashing "Password" twice it will return two different results.
    * Input: "Password"
    * Output: Hashed result
    */
    /* @Test
     * public void hashTest() {
     *  String hashA = new String(HashInformation("password"));
     *  String hashB = new String(HashInformation("password"));
     *  System.out.println(hashA);
     *  System.out.println(hashB);
     *  assertNotEquals(hashA, hashB);
     * }
     */

    // Test user Login successfully
//    @Test
//    public void testLogin() throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
//        Controller.authenticateUserLogin("admin","password");
//        assertEquals(true, Controller.loginSuccessful);
//    }
//
//    // Test user Login unsuccessfully
//    @Test
//    public void testLoginFail() throws IOException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException {
//        Controller.authenticateUserLogin("admin","passwofeferd");
//        assertEquals(false, Controller.loginSuccessful);
//    }

    // Test password is hashed before sent to server
    @Test
    public void testPasswordHash() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "password";

        String hashedPassword = Controller.PlaintextToHashedPassword(password);

        assertEquals(true, !password.equals(hashedPassword));

    }

    // New password is hashed before sent to server
    @Test
    public void testPasswordChange() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "password";

        String hashedPassword = Controller.HashNewPassword(password);

        assertEquals(true, !password.equals(hashedPassword));

    }

    // Update the details of the user before sent to server
    @Test
    public void testUpdateUserInfo() {
        String[] userDetails = {"John", "Smith"};

        String[] updatedUserDetails = Controller.UpdateUserInfo("Jack", "Doe");

        assertEquals(false, Arrays.equals(userDetails, updatedUserDetails));

    }

    // Checks that the user exists before sending delete request to server
    @Test
    public void testDeleteUser() {
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasUserInList = Controller.DeleteUser(listOfUsers, "User One");


        assertEquals(true, wasUserInList);

    }

    // Checks that the user doesn't exist and because it doesn't, work execute further to send the request to the server
    @Test
    public void testDeleteUserNoChange() {
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasUserInList = Controller.DeleteUser(listOfUsers, "User Four");


        assertEquals(false, wasUserInList);

    }

    // Checks that the user cannot delete themselves
    @Test
    public void testDeleteUserSelfRemoval() {
        Controller con = new Controller();
        con.loggedInUser = "ThisIsATestUser";
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");
        listOfUsers.add(con.loggedInUser);

        boolean wasUserInList = Controller.DeleteUser(listOfUsers, "ThisIsATestUser");


        assertEquals(false, wasUserInList);

    }

    @Test
    public void testupdateUserPermissions() {
        Controller con = new Controller();
        con.loggedInUser = "admin";
        boolean[] userPermissions = {false, true, true, false};
        boolean[] newUserPermissions = {true, true, true, true};
        String username = "test user";

        boolean[] updatedUserPermissions = Controller.UpdateUserPermissions(userPermissions, newUserPermissions, username);

        assertEquals(false, Arrays.equals(userPermissions, updatedUserPermissions));
    }

    @Test
    public void testupdateUserPermissionsUnchanged() {
        Controller con = new Controller();
        con.loggedInUser = "admin";
        boolean[] userPermissions = {false, true, true, false};
        boolean[] newUserPermissions = {false, true, true, false};
        String username = "test user";

        boolean[] updatedUserPermissions = Controller.UpdateUserPermissions(userPermissions, newUserPermissions, username);

        assertEquals(true, Arrays.equals(userPermissions, updatedUserPermissions));
    }

    // Test shows that a user cannot change their own permissions
    @Test
    public void testupdateUserPermissionsSelfChange() {
        Controller con = new Controller();
        con.loggedInUser = "ThisIsATestUser";
        boolean[] userPermissions = {false, true, true, false};
        boolean[] newUserPermissions = {true, true, true, true};
        String username = "ThisIsATestUser";

        boolean[] updatedUserPermissions = Controller.UpdateUserPermissions(userPermissions, newUserPermissions, username);

        assertEquals(true, Arrays.equals(userPermissions, updatedUserPermissions));
    }

    @Test
    public void testCreateNewUserAllCorrect() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        Controller con = new Controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.CreateNewUser(userData, listOfUsers);

        assertEquals(true, wasAllUserDataEntered);
    }

    @Test
    public void testCreateNewUserMissingFields() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        Controller con = new Controller();
        String[] userData = {"ThisIsATestUser", "", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.CreateNewUser(userData, listOfUsers);

        assertEquals(false, wasAllUserDataEntered);
    }

    // Test checks that the password is hashed before sent to the server
    @Test
    public void testCreateNewUserPasswordHashed() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        Controller con = new Controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        // Password hashed before validating user data
        System.out.println(userData[3]);

        con.CreateNewUser(userData, listOfUsers);

        // Password hashed after validated user data
        System.out.println(userData[3]);

        assertEquals(false, userData[3].equals("password"));
    }

    // Test checks that the username for the new user doesn't already exist
    @Test
    public void testCreateNewUserUsernameDoesNotExist() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        Controller con = new Controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.CreateNewUser(userData, listOfUsers);

        assertEquals(true, wasAllUserDataEntered);
    }

    // Test checks that the username for the new user already exists
    @Test
    public void testCreateNewUserUsernameAlreadyExist() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        Controller con = new Controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("ThisIsATestUser");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.CreateNewUser(userData, listOfUsers);

        assertEquals(false, wasAllUserDataEntered);
    }
}
