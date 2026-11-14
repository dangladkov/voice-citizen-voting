package voiceapp.project.dao;

import voiceapp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum QuestionDao {
    instance;

    /**
     * Add a new question in the question table
     * @param projectID the ID of a project
     * @param title the title/name of the question
     * @return the ID of the newly created question
     */
    public int addQuestion(int projectID,String title) {
        int questionID = -1;

        try{
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.question(projectid, title)\n" +
                    "VALUES(?, ?) RETURNING questionid";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, projectID);
            stmt.setString(2, title);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                questionID = resultSet.getInt("questionid");
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return -1;
        }
        return questionID;

    }

    /**
     * Check if the question exists
     * @param questionID the ID of the question
     * @return true if the action is successful, otherwise false
     */
    public boolean isEmpty(int questionID){
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT *\n" +
                    "FROM voice4.question\n" +
                    "WHERE questionid = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, questionID);

            ResultSet resultSet = stmt.executeQuery();



            if(resultSet.next()){
                if(resultSet.equals("")){
                    return true;
                }
            }

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return false;
    }

    /**
     * Add the answer choice for a question
     * @param questionID the ID of a question
     * @param answer the answer of a question
     * @return true if the action is successful, otherwise false
     */
    public boolean addAnswer(int questionID, String answer) {
        String query = "";
        try{
            Connection c = DBManager.instance.getConnection();
            if(isEmpty(questionID)) {
            query = "UPDATE voice4.question\n" +
                    "SET answers = ?\n" +
                    "WHERE questionid = ?;";

}           else {
                query = "UPDATE voice4.question\n" +
                        "SET answers = CONCAT_WS('|', ?)\n" +
                        "WHERE questionid = ?;";
            }
            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, answer);
            stmt.setInt(2, questionID);

            stmt.executeUpdate();

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Update all the fields of a particular question
     * @param questionID the ID of a question
     * @param projectID the ID of a project
     * @param title the title of a question
     * @param answers the answers of a question
     * @return true if the action is successful, otherwise false
     */
    public boolean updateQuestion(int questionID, int projectID, String title, String answers ) {

        try {
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.question \n" +
                    "SET (projectid, title, answers) = (?, ?, ?)\n" +
                    "WHERE projectid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, questionID);
            stmt.setString(2, title);
            stmt.setString(3, answers);
            stmt.setInt(4, projectID);

            stmt.executeUpdate();



            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


}
