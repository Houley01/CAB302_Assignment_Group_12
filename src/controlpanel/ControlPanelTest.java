package controlpanel;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import javax.swing.*;

public class ControlPanelTest {
    /*
    * The following test are for testing the Control Panel packages
    */
//    JFrame test;

    /* Test 1 though 4 test inputs in the login sections */

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


}
