package voiceapp.project.dao;

import voiceapp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum VoteDao {
    instance;

    /**
     * Creates a vote by a given user for a specified question of a project
     * @param userID the ID of a user
     * @param questionID the ID of a question
     * @param choice the choice made by the user
     * @return true if the action is successful, otherwise false
     */
    public boolean vote(int userID, int questionID, String choice) {

        try {
            Connection c = DBManager.instance.getConnection();
            // check if the question exists
            String query = "SELECT * \n" +
                    "FROM voice4.question\n" +
                    "WHERE questionid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, questionID);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                //check if there is the question answered
                if (hasVoted(userID, questionID)) {
                    return false; // return false (the question has been answered and  can't be anymore)
                } else {
                    // retrieves all the answers (each one in its own cell)
                    String query2 = "SELECT unnest(string_to_array(answers, '|')) AS answer\n" +
                            "FROM voice4.question\n" +
                            "WHERE questionid = ?";
                    stmt = c.prepareStatement(query2);
                    stmt.setInt(1, questionID);
                    resultSet = stmt.executeQuery();
                    while (resultSet.next()) {
                        if (choice.equals(resultSet.getString("answer"))) { // check for each answer
                            String query3 = "INSERT INTO voice4.vote(userid, questionid, choice)\n" +
                                    "VALUES(?, ?, ?)";
                            stmt = c.prepareStatement(query3);
                            stmt.setInt(1, userID);
                            stmt.setInt(2, questionID);
                            stmt.setString(3, choice);
                            stmt.executeUpdate();
                        }
                    }
                }


            }

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Add the points to the user after they voted for a project
     * @param userID the ID of a user
     * @param projectID the ID of a project
     * @return true if the action is successful, otherwise false
     */
    public boolean addPoints(int userID, int projectID) {
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.user \n" +
                    "SET points = points + (SELECT pointstoaward FROM voice4.project WHERE projectid = ?), total_points = total_points + (SELECT pointstoaward FROM voice4.project WHERE projectid = ?)\n" +
                    "WHERE user_id = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, projectID);
            stmt.setInt(2, projectID);
            stmt.setInt(3, userID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Check if a user has voted on a specific question
     * @param userID the user ID
     * @param questionID the ID of a question
     * @return true if the action is successful, otherwise false
     */
    public boolean hasVoted(int userID, int questionID) {
        try {
            Connection c = DBManager.instance.getConnection();

            String query1 = "SELECT *\n" +
                    "FROM voice4.vote\n" +
                    "WHERE userid = ? AND questionid = ?";

            PreparedStatement stmt = c.prepareStatement(query1);
            stmt = c.prepareStatement(query1);
            stmt.setInt(1, userID);
            stmt.setInt(2, questionID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return true; // return true (the question has been answered and  can't be anymore)
            }
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return false;


    }

    /**
     * Get the count of all the results matching a specific choice
     * @param questionID the ID of a question
     * @param projectID the ID of a project
     * @param choice the choice of the vote
     * @return the number of votes with the same choice
     */
    public int getResult(int questionID, int projectID, String choice) {
            int count = 0;
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT COUNT(*) AS vote_count\n" +
                    "FROM voice4.vote v, voice4.question q\n" +
                    "WHERE v.questionid = ? AND v.questionid = q.questionid \n" +
                    "  AND choice = ? and q.projectid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, questionID);
            stmt.setString(2, choice);
            stmt.setInt(3, projectID);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("vote_count");
            }
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e);
            return count;
        }
        return count;
    }

    /**
     * Check if a user has previously voted on a project
     * @param userID the ID of a user
     * @param projectID the ID of a project
     * @return true if the action is successful, otherwise false
     */
    public boolean hasVotedOnProject(int userID, int projectID){
        try {
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT DISTINCT p.projectid\n" +
                    "FROM voice4.vote v , voice4.project p, voice4.user u, voice4.question q\n" +
                    "WHERE p.projectid = q.projectid AND v.questionid = q.questionid AND v.userid = u.user_id AND u.user_id = ? AND p.projectid = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);
            stmt.setInt(2, projectID);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return false;
    }
}