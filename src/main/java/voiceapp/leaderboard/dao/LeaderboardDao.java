package voiceapp.leaderboard.dao;

import voiceapp.db.DBManager;
import voiceapp.leaderboard.model.LeaderboardUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for all the queries related to managing the leaderboard page
 */
public enum LeaderboardDao {
    instance;

    /**
     * Return the top users (limit to 10) based on their total amount of points
     * @return a list of these users
     */
    public List<LeaderboardUser> getTopUsers(){

        List<LeaderboardUser> users = new ArrayList<>();
        int number = 1;

        try {
            Connection c = DBManager.instance.getConnection();
            Statement s = c.createStatement();
            String query = "SELECT u.first_name, u.last_name, u.total_points, u.anonymous\n" +
                    "FROM voice4.user u\n" +
                    "ORDER BY u.total_points DESC\n" +
                    "LIMIT 10";
            ResultSet resultSet = s.executeQuery(query);
            while(resultSet.next()){

                String name = resultSet.getString("first_name");
                String surname = resultSet.getString("last_name");
                int points = resultSet.getInt("total_points");
                boolean anonymous = resultSet.getBoolean("anonymous");

                if(anonymous){
                    name = "Anonymous";
                    surname = Integer.toString(number);
                    number++;
                }
                LeaderboardUser leaderBoardUser = new LeaderboardUser(name, surname, points, anonymous);
                users.add(leaderBoardUser);
            }



            s.close();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return users;
    }

    /**
     * Check if a user decided to stay anonymous
     * @param userID the ID of a user
     * @return true if they are anonymous, otherwise false
     */
    public boolean isAnonymous(int userID) {
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT CONCAT(u.first_name, ' ', u.last_name) " +
                    "FROM voice4.user u " +
                    "WHERE u.anonymous = true AND u.user_id = ?";

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
     * Change a user's anonymity status
     * @param userID the ID of a user
     * @return true if the action is successful, otherwise false
     */
    public boolean changeAnonymous(int userID){
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.user \n" +
                    "SET anonymous = true\n" +
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

