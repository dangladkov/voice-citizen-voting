package voiceapp.project.dao;

import voiceapp.db.DBManager;
import voiceapp.project.model.Project;
import voiceapp.project.model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum ProjectDao {
    instance;

    public static final int PROJECT_POINTS = 100;

    /**
     * Get an instance of Project based on the ID from the project table
     * @param projectID the ID of a project
     * @return an instance of Project, otherwise null
     */
    public Project getProject(int projectID){

    Project project= null;
        List<Question> questions = new ArrayList<>();
    try {
        Connection c = DBManager.instance.getConnection();
        String query = "SELECT *\n" +
                "FROM voice4.question\n" +
                "WHERE projectid = ?";

        PreparedStatement stmt = c.prepareStatement(query);
        stmt.setInt(1, projectID);
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()){
            Question question = new Question(
                    resultSet.getInt("questionid"),
                    resultSet.getInt("projectid"),
                    resultSet.getString("title"),
                    resultSet.getString("answers")
            );
            questions.add(question); // appends all the questions to the array
        }

        query = "SELECT *\n" +
                "FROM voice4.project\n" +
                "WHERE projectid = ?";

        stmt = c.prepareStatement(query);
        stmt.setInt(1, projectID);
        resultSet = stmt.executeQuery();

        if (!resultSet.next()) {
            System.out.println("Project " + projectID + " was not found!");
            return null;
        }
// first get the List<Questions> and add them in the project
        project = new Project(
                resultSet.getInt("projectid"),
                resultSet.getInt("ideaid"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getDate("date"),
                resultSet.getString("location"),
                resultSet.getInt("proposedby"),
                resultSet.getTimestamp("deadline"),
                resultSet.getInt("pointstoaward"),
                resultSet.getInt("projectstatus"),
                resultSet.getString("categories"),
                questions
        );

        stmt.close();
        resultSet.close();

    } catch (SQLException e) {
        System.out.println(e);
        return null;
    }
    return project;
}

    /**
     * Return a list of all the projects available in the project table
     * @return the list of all projects
     */
    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        List<Question> allQuestions = new ArrayList<>(); // stores all the questions
        List<Question> questionsOption = new ArrayList<>(); // stores the questions for a specific project
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT *\n" +
                    "FROM voice4.question";

            PreparedStatement stmt = c.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                Question question = new Question(
                        resultSet.getInt("questionid"),
                        resultSet.getInt("projectid"),
                        resultSet.getString("title"),
                        resultSet.getString("answers")
                );
                allQuestions.add(question); // appends all the questions to the array
            }

            query = "SELECT * \n" +
                    "FROM voice4.project";

            stmt = c.prepareStatement(query);

            resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No ideas were found!");
                return new ArrayList<>();
            }
            while (resultSet.next()){

                for (Question question: questionsOption) {
                    if(resultSet.getInt("projectid") == question.getProjectID()) { // check if questions projectid equals to the one of the project
                        questionsOption.add(question);
                }
                    Project project = new Project(
                            resultSet.getInt("projectid"),
                            resultSet.getInt("ideaid"),
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getDate("date"),
                            resultSet.getString("location"),
                            resultSet.getInt("proposedby"),
                            resultSet.getTimestamp("deadline"),
                            resultSet.getInt("pointstoaward"),
                            resultSet.getInt("projectstatus"),
                            resultSet.getString("categories"),
                            questionsOption
                    );

                projects.add(project);
            }
        }
            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }

        return projects;

    }

    /**
     * Return the IDs of all the project
     * @return a list that contain all the IDs of the project
     */
    public List<Integer> getProjectIDs() {
        List<Integer> results = new ArrayList<>();
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT * \n" +
                    "FROM voice4.project\n";

            PreparedStatement stmt = c.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()){
                results.add(resultSet.getInt("projectid"));
            }

            stmt.close();
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return results;
    }

    /**
     * Remove a project based on its ID
     * @param projectID the ID of a project
     * @return true if the action is successful, otherwise false
     */
    public boolean removeProject(int projectID) {

        try{
            Connection c = DBManager.instance.getConnection();
            String query = "DELETE FROM voice4.question\n" +
                    "WHERE projectid = ?";

            PreparedStatement stmt = c.prepareStatement(query);


            stmt.setInt(1, projectID);

            stmt.executeUpdate();

            query = "DELETE FROM voice4.project\n" +
                    "WHERE projectid = ?";

            stmt = c.prepareStatement(query);

            stmt.setInt(1, projectID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Set the status of the project
     * @param projectID the ID of a project
     * @param status the new status
     * @return true if the action has been successful, otherwise false
     */
    public boolean setStatus(int projectID, int status){
        if (status < 0 || status > 2) return false; // status can only be 0, 1, 2
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.project\n" +
                    "SET projectstatus = ?\n" +
                    "WHERE projectid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, status);
            stmt.setInt(2, projectID);
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
     *
     * @param projectID the ID of a project
     * @param ideaID the ID of an idea
     * @param title the title of a project
     * @param description the description of a project
     * @param location the location of a project
     * @param deadline the deadline of a project
     * @param pointsToAward the points a user gets when voting for the specific project
     * @param categories the categories a project is part of
     * @return true if the action has been successful, otherwise false
     */
    public boolean updateProject(int projectID, int ideaID, String title, String description, String location, Timestamp deadline, int pointsToAward, String categories) {
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.project \n" +
                    "SET (ideaid, title, description, location, deadline, pointstoaward, categories) = (?, ?, ?, ?, ?, ?, ?)\n" +
                    "WHERE projectid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, ideaID);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, location);
            stmt.setTimestamp(5, deadline);
            stmt.setInt(6, pointsToAward);
            stmt.setString(7, categories);
            stmt.setInt(8, projectID);

            stmt.executeUpdate();

            query = "SELECT *\n" +
                    "FROM voice4.question\n" +
                    "WHERE projectid = ?";
            stmt = c.prepareStatement(query);
            stmt.setInt(1, projectID);

            ResultSet resultSet = stmt.executeQuery();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


    /**
     * add points to a specific user
     * @param userID the ID of a user
     * @return true if the action is successful, otherwise false
     */
    public boolean sendPoints(int userID) {

        try {
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.user\n" +
                    "SET points = points + ?, total_points = total_points + ?\n" +
                    "WHERE user_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, PROJECT_POINTS);
            stmt.setInt(2, PROJECT_POINTS);
            stmt.setInt(3, userID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Manually add a project (usually used by admins/municipality officials)
     * @param title the title of a project
     * @param description description of a project
     * @param location location of a project
     * @param proposedBy the user that created the project
     * @param deadline the deadline of a project
     * @param pointsToAward the points a user would get for voting
     * @param categories the categories the project is part of
     * @return the ID of the newly created project
     */
    public int addProject(String title, String description, String location, int proposedBy, Timestamp deadline, int pointsToAward, String categories){
        int projectID = -1;
        try{
            Connection c = DBManager.instance.getConnection();

                String query1 = "INSERT INTO voice4.project (title, description, date, location, proposedby, deadline, pointstoaward, categories)\n" +
                        "VALUES (?, ?, (SELECT CURRENT_TIMESTAMP), ?, ?, ?, ?, ?)\n" +
                        "RETURNING projectid;";
                PreparedStatement stmt = c.prepareStatement(query1);
                stmt.setString(1, title);
                stmt.setString(2, description);
                stmt.setString(3, location);
                stmt.setInt(4, proposedBy);
                stmt.setTimestamp(5, deadline);

                stmt.setInt(6, pointsToAward);
                stmt.setString(7, categories);

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

    /**
     * Add categories to a specific project
     * @param projectID the ID of a project
     * @param categories the categories of a project
     * @return true if the action has been successful, otherwise false
     */
    public boolean addCategories(int projectID, String categories) {
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.project\n" +
                    "SET categories = ?\n" +
                    "WHERE projectid = ?;";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, categories);
            stmt.setInt(2, projectID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }


}







