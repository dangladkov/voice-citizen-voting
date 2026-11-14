package voiceapp.news.dao;

import voiceapp.db.DBManager;
import voiceapp.idea.model.Idea;
import voiceapp.news.model.News;
import voiceapp.shop.model.Benefit;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Class responsible for all the queries related to managing the news page
 */
public enum NewsDao {

   instance;

    /**
     * Retrieve an instance of News based on its ID in news
     * @param newsID the ID of a news
     * @return an instance of News
     */
   public News getNews(int newsID) {
       News news = null;
       try {
           Connection c = DBManager.instance.getConnection();
           String query = "SELECT * \n" +
                   "FROM voice4.news\n" +
                   "WHERE newsid = ?";

           PreparedStatement stmt = c.prepareStatement(query);
           stmt.setInt(1, newsID);
           ResultSet resultSet = stmt.executeQuery();

           if (!resultSet.next()) {
               System.out.println("Idea " + newsID + " was not found!");
               return null;
           }

           news = new News(
                   resultSet.getInt("newsID"),
                   resultSet.getString("title"),
                   resultSet.getString("description"),
                   resultSet.getInt("userid")
           );
           stmt.close();
           resultSet.close();

       } catch (SQLException e) {
           System.out.println(e);
           return null;
       }
       return news;
   }

    /**
     * Add a new News in the news table
     * @param title the title of the news
     * @param description the description of the news
     * @param userID the user that created the news
     * @return
     */
    public int addNews(String title, String description, int userID) {
        int newsID = -1;
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "INSERT INTO voice4.news (title, description, userid)\n"
                    +"VALUES(?, ?, ?) RETURNING newsid";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setInt(3, userID);

            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()){
                newsID = resultSet.getInt("newsid");
            }
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
            return newsID;
        }
        return newsID;
    }

    /**
     * Return a list with all the news from the news table
     * @return a list of all available news, otherwise false
     */
    public List<News> getNews(){
        List<News> news = new ArrayList<>();
        try {
            Connection c = DBManager.instance.getConnection();
            String query = "SELECT * \n" +
                    "FROM voice4.news\n";

            PreparedStatement stmt = c.prepareStatement(query);

            ResultSet resultSet = stmt.executeQuery();

            if (!resultSet.next()) {
                System.out.println("No ideas were found!");
                return new ArrayList<>();
            }
            while (resultSet.next()){
                News newsObject = new News(
                        resultSet.getInt("newsID"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getInt("userid")
                );
                news.add(newsObject);
            }

            stmt.close();
            resultSet.close();

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return news;

    }

    /**
     * Delete a news from the table based on its ID
     * @param newsID the ID of a news
     * @return true if the action is successful, otherwise false
     */
    public boolean deleteNews(int newsID) {
        try{
            Connection c = DBManager.instance.getConnection();

            String query = "DELETE\n" +
                    "FROM voice4.news\n" +
                    "WHERE newsid = ?";

            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setInt(1, newsID);

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
   }


}
