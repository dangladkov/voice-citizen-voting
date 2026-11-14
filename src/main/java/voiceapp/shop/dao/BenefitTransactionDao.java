package voiceapp.shop.dao;

import voiceapp.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public enum BenefitTransactionDao {
    instance;

    public int addTransaction(int userID, int itemID){
        int transactionID = -1;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.benefittransaction(user_id, selection, benefit_name, value)\n" +
                    "SELECT u.user_id, b.attributes, b.name, b.points\n" +
                    "FROM voice4.user u, voice4.benefit b\n" +
                    "WHERE u.user_id = ? AND b.item_id = ?" +
                    "RETURNING bt_id";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, userID);
            stmt.setInt(2, itemID);

            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                transactionID = resultSet.getInt("bt_id");
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return transactionID;
        }
        return transactionID;
    }

    // no idea how to make this work
    public boolean decrementCount(int ItemID, int numberOfItem){
        List<String> stringDivider;
        String json;
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT attributes\n" +
                    "FROM voice4.benefit\n" +
                    "WHERE item_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, ItemID);
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()){
                json = resultSet.getString("attributes");


            }


        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

}
