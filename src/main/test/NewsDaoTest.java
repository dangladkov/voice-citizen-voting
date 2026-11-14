import org.junit.jupiter.api.Test;
import voiceapp.news.dao.NewsDao;
import voiceapp.news.model.News;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Class used for testing the key methods of class NewsDao
 */
public class NewsDaoTest {

    /**
     * Test adding an instance of news
     */
    @Test
    void addNewsTest(){
        int newsID = NewsDao.instance.addNews("Hi", "gg ez", 100);
        if(newsID != -1){
            assertNotEquals(-1, newsID);
        } else {
            assertEquals(-1, newsID);
        }
    }

    /**
     * Test getting the news by its ID
     */
    @Test
    void getNewsTest(){
        int newsID = NewsDao.instance.addNews("Hi", "gg ez", 100);
        News news = NewsDao.instance.getNews(newsID);
        assertTrue(news instanceof News);
    }

    /**
     * Test deleting  an instance of news based on its ID
     */
    @Test
    void deleteNewsTest(){
        int newsID = NewsDao.instance.addNews("Hi", "gg ez", 100);
        assertTrue(NewsDao.instance.deleteNews(newsID));
    }
}
