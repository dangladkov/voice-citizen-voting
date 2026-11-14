import org.junit.jupiter.api.Test;
import voiceapp.auth.dao.UserDao;
import voiceapp.leaderboard.dao.LeaderboardDao;
import voiceapp.leaderboard.model.LeaderboardUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used for testing the key methods of class LeaderboardDao
 */
public class LeaderboardDaoTest {

    /**
     * Test getting the top 10 users (if available) with the most total_points
     */
    @Test
    void getTopUsersTest(){
        List<LeaderboardUser> topUsers = LeaderboardDao.instance.getTopUsers();

        if(topUsers.size() > 0) {
            assertNotNull(topUsers);
        } else {
            assertNull(topUsers);
        }
    }

    /**
     * Test checking if a user is anonymous on the leaderboard
     */
    @Test
    void isAnonymousTest(){
        int userID = 0;

        if(UserDao.instance.getUserIDSecure("something") != -1){
            userID = UserDao.instance.getUserIDSecure("something");
            assertTrue(LeaderboardDao.instance.isAnonymous(userID));
        } else {
            assertFalse(LeaderboardDao.instance.isAnonymous(userID));
        }

    }

    /**
     * Test changing the anonymous status of a user
     */
    @Test
    void changeAnonymousTest(){
        assertTrue(LeaderboardDao.instance.changeAnonymous(100));
    }
}
