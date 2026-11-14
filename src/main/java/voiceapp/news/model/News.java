package voiceapp.news.model;

/**
 * Class containing data for news.
 */
public class News {
    private int newsID;

    private String title;

    private String description;

    private int userID;

    public News(int newsID, String title, String description, int userID) {
        this.newsID = newsID;
        this.title = title;
        this.description = description;
        this.userID = userID;
    }

    public int getNewsID() {
        return newsID;
    }

    public void setNewsID(int newsID) {
        this.newsID = newsID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
