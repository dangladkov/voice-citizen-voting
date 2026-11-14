package voiceapp.auth.dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import voiceapp.auth.model.User;
import voiceapp.db.DBManager;

import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for all the queries related to user creation and management
 */
public enum UserDao {
    instance;

    public enum AccessLevel {
    REGULAR, ADMIN
    }
    public static final Duration SESSION_LIFE = Duration.ofHours(2);


    UserDao() {
    }

    /**
     * Adds a user to the user database
     * @param user contains all the associated fields of an object User that are sent to be stored in the user table
     * @return true if the task has been successful, otherwise false
     */
    public boolean addUser(User user) {
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.user (first_name, last_name, phone_number,password_hash, email)\n" +
                    "VALUES( ?, ?, ?, ?, ?)";
            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, user.getUrm().getName());
            stmt.setString(2, user.getUrm().getSurname());
            stmt.setString(3, user.getUrm().getPhoneNumber());
            stmt.setString(4, user.getUrm().getPwd());
            stmt.setString(5, user.getUrm().getEmail());

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }

        return true;

    }


    /**
     * Check if a user exists in the user table
     * @param userIdentifier is either the unique phoneNumber or email associated to each user
     * @return true if such user exists, otherwise false
     */
    public boolean userExists(String userIdentifier) {
        try {
            Connection c = DBManager.instance.getConnection();
            String query;
            if(isInputEmail(userIdentifier)) {
                query = "SELECT *\n" +
                        "FROM voice4.user\n" +
                        "WHERE email = ?";
            } else {
                query = "SELECT *\n" +
                        "FROM voice4.user\n" +
                        "WHERE phone_number = ?";
            }
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, userIdentifier);
            ResultSet resultSet = stmt.executeQuery();


            if (!resultSet.next()) {
                return false;
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Return an instance of a user from the table based on their ID
     * @param userID is the ID of a specifc user stored in user table
     * @return an object of type User that contains all of its attributes from the user table
     */
    public User getUserSecure(int userID) {
        User user = null;
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT * " +
                    "FROM voice4.user u " +
                    "WHERE u.user_id = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("This shouldn't happen! Failed to find user " + userID + ". Was the request verified beforehand?");
                return null;
            }

            user = new User(resultSet.getInt("user_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("email"),
                    resultSet.getInt("points"),
                    resultSet.getInt("total_points"),
                    resultSet.getBoolean("anonymous"),
                    ""
            );

            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return user;
    }

    /**
     * Return the ID of a user from the table
     * @param userIdentifier is an unique identifier (phoneNumber or email)
     * @return the ID of the user
     */
    public int getUserIDSecure(String userIdentifier) {
        int result;
        try {
            Connection c = DBManager.instance.getConnection();

            String query;

            if(isInputEmail(userIdentifier)) {
                query = "SELECT u.user_id " +
                        "FROM voice4.user u " +
                        "WHERE u.email = ?";
            } else {
                query = "SELECT u.user_id " +
                        "FROM voice4.user u " +
                        "WHERE u.phone_number = ?";
            }

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, userIdentifier);

            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                return -1;
            }

            result = resultSet.getInt("user_id");

            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        }
        return result;
    }

    /**
     * Check if the session the user is in, is still valid
     * @param userID the ID of the user
     * @param token authorization credential used to verify the identity of the user
     * @return true if the seesion is still valid, otherwise false
     */
    public boolean isValidSession(int userID, String token) {
        try {
            Connection c = DBManager.instance.getConnection();


            String query = "SELECT * " +
                    "FROM voice4.user u, voice4.user_session us " +
                    "WHERE us.user_id = ? AND u.user_id = ? AND us.token = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);
            stmt.setString(3, token);

            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                return false;
            }

            Timestamp t = resultSet.getTimestamp("issue_date");
            Time sessionIssueDate = new Time(t.getTime());
            Duration timePassed = Duration.between(sessionIssueDate.toLocalTime(), LocalTime.now());

            int val = timePassed.compareTo(SESSION_LIFE);
            if (val > 0) { // timePassed is greater than SESSION_LIFE
                // Session has expired.
                System.out.println("Session had expired for " + userID);
                return false;
            }

            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Check if a specific password is valid
     * @param userIdentifier the phoneNumber or email of the user
     * @param plainPwd the raw password
     * @return true if it's valid (abides by constraints), otherwise false
     */
    public boolean isValidPassword(String userIdentifier, String plainPwd) {
        try {
            Connection c = DBManager.instance.getConnection();
            String query;

            if(isInputEmail(userIdentifier)) {
                query = "SELECT u.password_hash " +
                        "FROM voice4.user u " +
                        "WHERE u.email = ?";
            } else {
                query = "SELECT u.password_hash " +
                        "FROM voice4.user u " +
                        "WHERE u.phone_number = ?";
            }
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, userIdentifier);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String hashedPwd = resultSet.getString("password_hash");

                if (!BCrypt.verifyer().verify(plainPwd.toCharArray(), hashedPwd).verified) {
                    return false;
                }
            }

            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Renews the session of a user
     * @param userID the ID of a user
     * @param token the new authentication token of a new session
     * @return true if a new token has been added, otherwise false
     */
    public boolean renewSession(int userID, String token){
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.user_session (token, issue_date, user_id) " +
                    "VALUES (?, (SELECT CURRENT_TIMESTAMP), ?) " +
                    "ON CONFLICT (user_id) DO UPDATE " +
                    "  SET token = ?, " +
                    "      issue_date = (SELECT CURRENT_TIMESTAMP), " +
                    "      user_id = ?;";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, token);
            stmt.setInt(2, userID);
            stmt.setString(3, token);
            stmt.setInt(4, userID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


    /**
     * Modifies the user permission from regular user to admin or vice-versa
     * @param userID the ID of a user
     * @param accessLevel the access level that wants to be granted
     * @return true if the change was successful, otherwise false
     */
    public boolean modifyUserPermission(int userID, AccessLevel accessLevel) {
        try {
            Connection c = DBManager.instance.getConnection();

            String query;
            if(accessLevel == AccessLevel.ADMIN) {
                query = "INSERT INTO voice4.administrator(adminid)" +
                            "VALUES(?)";
            } else {
                query = "DELETE FROM voice4.administrator" +
                        "WHERE adminid = ?";
            }
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Check if a user is an admin
     * @param userID the ID of a user
     * @return true if the user is an admin, otherwise false
     */
    public boolean isAdmin(int userID) {
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT * " +
                    "FROM voice4.administrator a " +
                    "WHERE a.adminid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);
            ResultSet resultSet = stmt.executeQuery();


            if (!resultSet.next()) {
                return false;
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

}

    /**
     * Check if the email contains the element '@'
     * @param input the email
     * @return true if the input complied with the rule stated above, otherwise false
     */
    private boolean isInputEmail(String input) {
        return input.indexOf('@') != -1;
    }

    /**
     * Check if the user table is empty
     * @return true if it is empty, otherwise false
     */
    public boolean isUserTableEmpty(){
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT * FROM voice4.user";

            PreparedStatement stmt = c.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();


            if (!resultSet.next()) {
                return true;
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return false;
    }

    /**
     * Update the parameters of a user
     * @param userID the ID of a user, used to check if they exist
     * @param firstName the attribute representing the name of the user
     * @param lastName the attribute representing the lastName of the user
     * @param phoneNumber the attribute representing the phoneNumber of the user
     * @param email the attribute representing the email of the user
     * @param isAnonymous the attribute representing if the user is anonymity status for leaderboard
     * @return
     */
    public boolean updateUser(int userID, String firstName, String lastName, String phoneNumber, String email, boolean isAnonymous) {
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.user \n" +
                    "SET (first_name, last_name, phone_number, email, anonymous) = (?, ?, ?, ?, ?)\n" +
                    "WHERE user_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, email);
            stmt.setBoolean(5, isAnonymous);
            stmt.setInt(6, userID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Logs out a user based on their ID
     * @param userID the ID of a user
     * @return true if the user has been successfully logged out, otherwise false
     */
    public boolean logOut(int userID){
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.user_session \n" +
                    "SET token = NULL\n" +
                    "WHERE user_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}

