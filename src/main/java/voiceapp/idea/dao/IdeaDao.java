package voiceapp.idea.dao;

import voiceapp.db.DBManager;
import voiceapp.db.DateParser;
import voiceapp.idea.model.Idea;
import voiceapp.idea.model.IdeaArchiveType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for all the queries related to idea creation and management
 */
public enum IdeaDao {
    instance;


    /**
     * Get an idea based on its ID from ideaproposal table
     * @param ideaID ID of an idea
     * @return an instance of the type Idea with all the attributes from the table
     */
    public Idea getIdea(int ideaID){
        Idea idea = null;
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT * \n" +
                    "FROM voice4.ideaproposal\n" +
                    "WHERE ideaid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, ideaID);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Idea " + ideaID + " was not found!");
                return null;
            }

            idea = new Idea(
                    resultSet.getInt("ideaid"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getTimestamp("date").toString(),
                    resultSet.getString("location"),
                    resultSet.getInt("proposedby"),
                    resultSet.getInt("ideastatus")
            );
            stmt.close();
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return idea;
    }

    /**
     * Add an idea in the ideaproposal table
     * @param idea instance of type Idea that is used to send the fields of an idea to the ideaproposal table
     * @return the ID of the newly generated idea
     */
    public int addIdea(Idea idea){
        int ideaID = -1;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.ideaproposal (title, description, date, location, proposedby)\n"
            +"VALUES(?, ?, ?, ?, ?) RETURNING ideaid";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, idea.getTitle());
            stmt.setString(2, idea.getDescription());
            stmt.setTimestamp(3, DateParser.getDate(idea.getDate()));
            stmt.setString(4, idea.getLocation());
            stmt.setInt(5, idea.getProposerUserID());
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                ideaID = resultSet.getInt("ideaid");
            }
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return ideaID;
        }
        return ideaID;
    }

    /**
     * Change the status of an idea
     * @param ideaID the ID of an idea
     * @param iat parameter used to change in which archive the idea goes
     * @return true if the action is successful, otherwise false
     */
    public boolean setStatus(int ideaID, IdeaArchiveType iat){
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.ideaproposal\n" +
                    "SET ideastatus = ?\n" +
                    "WHERE ideaid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, iat.ordinal());
            stmt.setInt(2, ideaID);
            stmt.executeUpdate();

            // if ist == 1, then it will become a project ( all the relevant data has been sent )
            if(iat.ordinal() == 1) {
                String query1 = "INSERT INTO voice4.project (ideaid, title, description, date, location, proposedby)\n" +
                        "SELECT ideaid, title, description, date, location, proposedby\n" +
                        "FROM voice4.ideaproposal " +
                        "WHERE ideaid = ?";
                stmt = c.prepareStatement(query1);
                stmt.setInt(1, ideaID);
                stmt.executeUpdate();
            }
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    /**
     * Return all the ideas stored in the ideaproposal
     * @return a list with all the ideas, otherwise null
     */
    public List<Idea> getIdeas(){
        List<Idea> ideas = new ArrayList<>();
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT * FROM voice4.ideaproposal\n";

            PreparedStatement stmt = c.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No ideas were found!");
                return new ArrayList<>();
            }
            while (resultSet.next()){
                Idea idea = new Idea(
                        resultSet.getInt("ideaid"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("date").toString(),
                        resultSet.getString("location"),
                        resultSet.getInt("proposedby"),
                        resultSet.getInt("ideastatus")
                );
                ideas.add(idea);
            }

            stmt.close();
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return ideas;

    }

    /**
     * Delete an idea based on its ID
     * @param ideaID the ID of an idea
     * @return true if the action is successful, otherwise false
     */
    public boolean deleteIdea(int ideaID) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "DELETE\n" +
                    "FROM voice4.ideaproposal\n" +
                    "WHERE ideaid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, ideaID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    /**
     * Check if a user is the creator/owner of an idea
     * @param ideaID the ID of an idea
     * @param userID the ID of a user
     * @return true if the action is successful, otherwise false
     */
    public boolean isOwner(int ideaID, int userID) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT * \n" +
                    "FROM voice4.user u, voice4.ideaproposal i\n" +
                    "WHERE u.user_id = i.proposedby AND i.ideaid = ? AND u.user_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, ideaID);
            stmt.setInt(2, userID);

            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return true;
            }
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return false;

    }

    /**
     * Convert an idea to a project, if it is sent to the 'SUCCESSFUL' archive
     * @param ideaID the ID of an idea
     * @return the ID of the newly created project
     */
    public int convertToProject(int ideaID){
        int projectID = -1;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.ideaproposal\n" +
                    "SET ideastatus = 1\n" +
                    "WHERE ideaid = ?";

            PreparedStatement stmt = c.prepareStatement(query);;
            stmt.setInt(1, ideaID);
            stmt.executeUpdate();
                String query1 = "INSERT INTO voice4.project (ideaid, title, description, date, location, proposedby)\n" +
                        "SELECT ideaid, title, description, date, location, proposedby\n" +
                        "FROM voice4.ideaproposal\n" +
                        "WHERE ideaid = ?\n" +
                        "RETURNING projectid;";
                stmt = c.prepareStatement(query1);
                stmt.setInt(1, ideaID);
                ResultSet resultSet = stmt.executeQuery();
                if(resultSet.next()) {
                    projectID = resultSet.getInt("projectid");
                }
                stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return projectID;
        }
        return projectID;

    }

}
