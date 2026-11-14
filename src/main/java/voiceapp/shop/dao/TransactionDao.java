package voiceapp.shop.dao;

import voiceapp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public enum TransactionDao {
    instance;


    public boolean sendPoints(int userID, int points) {
        String query = "";
        try {
            Connection c = DBManager.instance.getConnection();
            PreparedStatement stmt;
            if (points > 0) {

                query = "UPDATE voice4.user\n" +
                        "SET points = points + ?, total_points = total_points + ?\n" +
                        "WHERE user_id = ?";
                stmt = c.prepareStatement(query);
                stmt.setInt(1, points);
                stmt.setInt(2, points);
                stmt.setInt(3, userID);
            } else {
                query = "UPDATE voice4.user SET points = points WHERE user_id = ?";
                stmt = c.prepareStatement(query);
                stmt.setInt(1, points);
                stmt.setInt(2, userID);
            }

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public int getPoints(int userID){
        int points = -1;
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT points\n" +
                    "FROM voice4.user\n" +
                    "WHERE user_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                points = resultSet.getInt("points");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return points;
        }
        return points;
    }

}

