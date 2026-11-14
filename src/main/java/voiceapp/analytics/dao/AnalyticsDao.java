package voiceapp.analytics.dao;

import voiceapp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum AnalyticsDao {
    instance;

    public int getTotalPoints(){
            int totalPoints = 0;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT SUM(total_points) AS total_points\n" +
                    "FROM voice4.user";

            PreparedStatement stmt = c.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                totalPoints = resultSet.getInt("total_points");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return totalPoints;
        }
        return totalPoints;
    }

    public int getNumberOfProjects(){
        int totalProjects = 0;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT count(*) AS total_projects\n" +
                    "FROM voice4.project";

            PreparedStatement stmt = c.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                totalProjects = resultSet.getInt("total_projects");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return totalProjects;
        }
        return totalProjects;
    }

    public int getNumberOfUsers(){
        int totalUsers = 0;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT count(*) AS total_users\n" +
                    "FROM voice4.user";

            PreparedStatement stmt = c.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                totalUsers = resultSet.getInt("total_users");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return totalUsers;
        }
        return totalUsers;
    }

    public int getNumberOfVotes() {
        int totalVotes = 0;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "SELECT count(*) AS total_votes\n" +
                    "FROM voice4.vote";

            PreparedStatement stmt = c.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                totalVotes = resultSet.getInt("total_votes");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return totalVotes;
        }
        return totalVotes;
    }


}
