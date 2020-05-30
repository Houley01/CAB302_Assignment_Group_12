package controlpanel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class TestControlPanel {

    // Test password is hashed before sent to server
    @Test
    public void PasswordHashTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "password";

        String hashedPassword = controller.plaintextToHashedPassword(password);

        assertEquals(true, !password.equals(hashedPassword));

    }

    // New password is hashed before sent to server
    @Test
    public void PasswordChangeTest() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "password";

        String hashedPassword = controller.hashNewPassword(password);

        assertEquals(true, !password.equals(hashedPassword));

    }

    // Update the details of the user before sent to server
    @Test
    public void UpdateUserInfoTest() {
        String[] userDetails = {"John", "Smith"};

        String[] updatedUserDetails = controller.updateUserInfo("Jack", "Doe");

        assertEquals(false, Arrays.equals(userDetails, updatedUserDetails));

    }

    // Checks that the user exists before sending delete request to server
    @Test
    public void DeleteUserTest() {
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasUserInList = controller.deleteUser(listOfUsers, "User One");


        assertEquals(true, wasUserInList);

    }

    // Checks that the user doesn't exist and because it doesn't, work execute further to send the request to the server
    @Test
    public void DeleteUserNoChangeTest() {
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasUserInList = controller.deleteUser(listOfUsers, "User Four");


        assertEquals(false, wasUserInList);

    }

    // Checks that the user cannot delete themselves
    @Test
    public void DeleteUserSelfRemovalTest() {
        controller con = new controller();
        con.loggedInUser = "ThisIsATestUser";
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");
        listOfUsers.add(con.loggedInUser);

        boolean wasUserInList = controller.deleteUser(listOfUsers, "ThisIsATestUser");


        assertEquals(false, wasUserInList);

    }

    @Test
    public void UpdateUserPermissionsTest() {
        controller con = new controller();
        con.loggedInUser = "admin";
        boolean[] userPermissions = {false, true, true, false};
        boolean[] newUserPermissions = {true, true, true, true};
        String username = "test user";

        boolean[] updatedUserPermissions = controller.updateUserPermissions(userPermissions, newUserPermissions, username);

        assertEquals(false, Arrays.equals(userPermissions, updatedUserPermissions));
    }

    @Test
    public void UpdateUserPermissionsUnchangedTest() {
        controller con = new controller();
        con.loggedInUser = "admin";
        boolean[] userPermissions = {false, true, true, false};
        boolean[] newUserPermissions = {false, true, true, false};
        String username = "test user";

        boolean[] updatedUserPermissions = controller.updateUserPermissions(userPermissions, newUserPermissions, username);

        assertEquals(true, Arrays.equals(userPermissions, updatedUserPermissions));
    }

    // Test shows that a user cannot change their own permissions
    @Test
    public void UpdateUserPermissionsSelfChangeTest() {
        controller con = new controller();
        con.loggedInUser = "ThisIsATestUser";
        boolean[] userPermissions = {false, true, true, false};
        boolean[] newUserPermissions = {true, true, true, true};
        String username = "ThisIsATestUser";

        boolean[] updatedUserPermissions = controller.updateUserPermissions(userPermissions, newUserPermissions, username);

        assertEquals(true, Arrays.equals(userPermissions, updatedUserPermissions));
    }

    @Test
    public void CreateNewUserAllCorrectTest() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        controller con = new controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.createNewUser(userData, listOfUsers);

        assertEquals(true, wasAllUserDataEntered);
    }

    @Test
    public void CreateNewUserMissingFieldsTest() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        controller con = new controller();
        String[] userData = {"ThisIsATestUser", "", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.createNewUser(userData, listOfUsers);

        assertEquals(false, wasAllUserDataEntered);
    }

    // Test checks that the password is hashed before sent to the server
    @Test
    public void CreateNewUserPasswordHashedTest() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        controller con = new controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        // Password hashed before validating user data
        //System.out.println(userData[3]);

        con.createNewUser(userData, listOfUsers);

        // Password hashed after validated user data
        //System.out.println(userData[3]);

        assertEquals(false, userData[3].equals("password"));
    }

    // Test checks that the username for the new user doesn't already exist
    @Test
    public void CreateNewUserUsernameDoesNotExistTest() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        controller con = new controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("User One");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.createNewUser(userData, listOfUsers);

        assertEquals(true, wasAllUserDataEntered);
    }

    // Test checks that the username for the new user already exists
    @Test
    public void CreateNewUserUsernameAlreadyExistTest() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException {
        controller con = new controller();
        String[] userData = {"ThisIsATestUser", "Test", "User", "password"};
        ArrayList<String> listOfUsers = new ArrayList<>();
        listOfUsers.add("ThisIsATestUser");
        listOfUsers.add("User Two");
        listOfUsers.add("User Three");

        boolean wasAllUserDataEntered = con.createNewUser(userData, listOfUsers);

        assertEquals(false, wasAllUserDataEntered);
    }
}
