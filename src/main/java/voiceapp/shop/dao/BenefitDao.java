package voiceapp.shop.dao;

import voiceapp.db.DBManager;
import voiceapp.shop.model.Benefit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public enum BenefitDao {
    instance;

    public Benefit getBenefit(int benefitID){
        Benefit benefit = null;
        try{
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT *\n" +
                    "FROM voice4.benefit\n" +
                    "WHERE item_id = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, benefitID);
            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Benefit " + benefitID + " was not found!");
                return null;
            }

            benefit = new Benefit(
                    resultSet.getInt("item_id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getInt("points"),
                    resultSet.getString("category"),
                    resultSet.getString("attributes")
            );
            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return benefit;
    }

    public int addBenefit(String name, String description, int points, String category, String attributes){
        int benefitID = -1;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.benefit (name, description, points, category, attributes)\n"
                    +"VALUES(?, ?, ?, ?, to_jsonb(?)) RETURNING item_id";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, points);
            stmt.setString(4, category);
            stmt.setString(5, attributes);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                benefitID = resultSet.getInt("item_id");
            }
            stmt.close();

        }
        catch (SQLException e) {
            System.out.println(e);
            return benefitID;
        }
        return benefitID;
    }

    public boolean updateName(int benefitID, String name) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.benefit\n" +
                    "SET name = ?\n" +
                    "WHERE item_id = ?;";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, benefitID);

            stmt.executeUpdate();

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }
    public boolean updateDescription(int benefitID, String description) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.benefit\n" +
                    "SET description = ?\n" +
                    "WHERE item_id = ?;";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, description);
            stmt.setInt(2, benefitID);

            stmt.executeUpdate();

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    public boolean updatePoints(int benefitID, int points) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.benefit\n" +
                    "SET points = ?\n" +
                    "WHERE item_id = ?;";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, points);
            stmt.setInt(2, benefitID);

            stmt.executeUpdate();

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    public boolean updateCategory(int benefitID, String category) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "UPDATE voice4.benefit\n" +
                    "SET category = ?\n" +
                    "WHERE item_id = ?;";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, category);
            stmt.setInt(2, benefitID);

            stmt.executeUpdate();

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }

    //adds custom attribute
    public boolean addAttributes(int benefitID, String attribute, String value) {

        String query = "";

        try{
            Connection c = DBManager.instance.getConnection();

            // query needs to be changed
            // UPDATE voice4.benefit SET attributes = jsonb_set(attributes, '{color}', '"green"')
            //WHere item_id = 1 (--> for first time attribute/value)

            //UPDATE voice4.benefit SET attributes = jsonb_set(attributes, '{color}',
            //        attributes -> 'color' || '"red"')
            //WHere item_id = 1 (for further things)
            if(containsAttribute(benefitID, attribute)){

                //used to append additional values (if ever required) for attributes, example {'color' : 'green'} --> {'color' : ['green', 'red']}
                query = "UPDATE voice4.benefit SET attributes = jsonb_set(attributes, '{?}',\n" +
                        "attributes -> '?' || '?')\n" +
                        "WHERE item_id = ? ";
                PreparedStatement stmt = c.prepareStatement(query);
                stmt.setString(1, attribute);
                stmt.setString(2, attribute);
                stmt.setString(3, value);
                stmt.setInt(4, benefitID);

                stmt.executeUpdate();

                stmt.close();
            } else {
                query = "UPDATE voice4.benefit SET attributes = jsonb_set(attributes, '{?}', '\"?\"')\n" +
                        "WHere item_id = ?";
            }

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, attribute);
            stmt.setString(2, value);
            stmt.setInt(3, benefitID);


            stmt.executeUpdate();

            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;

    }
    // checks if an attribute ( in json "attribute" : "value" ), used in above method
    public boolean containsAttribute(int benefitID, String attribute){
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT item_id\n" +
                    "FROM voice4.benefit b\n" +
                    "WHERE attributes -> ? IS NOT NULL AND item_id = ?";
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, attribute);
            stmt.setInt(2, benefitID);

            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()){
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

    public boolean deleteAttribute(int benefitID, String attribute){
        try{
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.benefit\n" +
                    "SET attributes = attributes - ?\n" +
                    "WHERE item_id = ?;";

            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, attribute);
            stmt.setInt(2, benefitID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean deleteBenefit(int benefitID){
        try{
            Connection c = DBManager.instance.getConnection();
            String query = "DELETE FROM voice4.benefit\n" +
                           "WHERE item_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);


            stmt.setInt(1, benefitID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean updateBenefit(int benefitID, String name, String description, int points, String category, String attributes){
        try{
            Connection c = DBManager.instance.getConnection();
            String query = "UPDATE voice4.benefit\n" +
                    "SET (name, description, points, category, attributes) = (?, ?, ?, ?, ?)\n" +
                    "WHERE item_id = ?";

            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, points);
            stmt.setString(4, category);
            stmt.setString(5, attributes);
            stmt.setInt(6, benefitID);

            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public List<Integer> getAllBenefits() {
        List<Integer> results = new ArrayList<>();
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT item_id " +
                    "FROM voice4.benefit";
            PreparedStatement stmt = c.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            while(resultSet.next()){
                results.add(resultSet.getInt("item_id"));
            }

            stmt.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return results;
    }
}
