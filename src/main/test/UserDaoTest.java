import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import voiceapp.auth.dao.UserDao;
import voiceapp.auth.model.User;
import voiceapp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Class used for testing the key methods of class UserDao
 */
public class UserDaoTest {

    /**
     * Test if a user has been added to the Data
     * Must satisfy the following constraints: name and surname must be between 2 and 32 characters,
     * the email must be of the following format %_@_%._%,
     * the phone number must be between 7 and 15 numbers, can't contain letters or characters,
     * the email & phone_number must be unique,
     * passwordHash must be at least 4 characters long
     */
    @Test
    void addUserTest(){
        User user = new User(0, "Jesse", "Pinkman", "4234324", "jesse", 0, 0, false, "");
        // throws false since it does not abide by the constraints
        assertFalse(UserDao.instance.addUser(user));

        User user1 = new User(0, "Dan", "Kuzo", "234234242", "dan@kuzo.com", 0, 0, false, "ddddd");

        // check if such user exists (based on either phone_number or email)
        if(UserDao.instance.userExists("dan@kuzo.com")){
            assertFalse(UserDao.instance.addUser(user1));
        } else {
            assertTrue(UserDao.instance.addUser(user1));
        }
    }

    /**
     * Test getting the user based on their userID
     */
    @Test
    void getUserSecureTest(){
        User user = UserDao.instance.getUserSecure(142); // user_id of Dan Kuzo
        assertNotNull(user);
    }

    /**
     * Test getting the userID based on one of the identifiers (either phoneNumber or email)
     */
    @Test
    void getUserIDSecureTest() {
        if(UserDao.instance.userExists("dan@kuzon.com") || UserDao.instance.userExists("234234242")){
            assertEquals(142, UserDao.instance.getUserIDSecure("dan@kuzo.com"));
            assertEquals(142, UserDao.instance.getUserIDSecure("234234242"));
        }
    }

    /**
     * Test making a regular user an admin or revoke the admin capability
     */
    @Test
    void modifyUserPermissionsTest(){
        // check if the user is an admin
        if(UserDao.instance.isAdmin(142)){
            assertFalse(UserDao.instance.modifyUserPermission(142, UserDao.AccessLevel.ADMIN)); //already an admin
            assertTrue(UserDao.instance.isAdmin(142));
        } else {
            assertTrue(UserDao.instance.modifyUserPermission(142, UserDao.AccessLevel.ADMIN)); // make Dan Kuzo an admin
            assertTrue(UserDao.instance.isAdmin(142));
            assertFalse(UserDao.instance.modifyUserPermission(142, UserDao.AccessLevel.REGULAR)); // revoke admin
            assertFalse(UserDao.instance.isAdmin(142));
        }
    }

    /**
     * Test if the user table is empty or not
     */
    @Test
    void isUserTableEmptyTest(){

        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT * FROM voice4.user";

            PreparedStatement stmt = c.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();
            // if the table is not empty
            if(resultSet.next()){
                assertFalse(UserDao.instance.isUserTableEmpty());
            } else {
                assertTrue(UserDao.instance.isUserTableEmpty());
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            assertFalse(UserDao.instance.isUserTableEmpty());
        }
    }

    /**
     * Test the ability to update the fields of a user based on it's ID
     */
    @Test
    void updateUserTest(){

        if(UserDao.instance.getUserSecure(142) != null){
            assertTrue(UserDao.instance.updateUser(142, "Dan", "Kuzo", "234234242", "dan@kuzo.com", true));
        } else {
            assertFalse(UserDao.instance.updateUser(142, "Dan", "Kuzo", "234234242", "dan@kuzo.com", true));

        }
        }

    /**
     * Test logging out a user base on their ID
      */
    @Test
    void logOutTest(){
        assertTrue(UserDao.instance.logOut(142));
    }
}
