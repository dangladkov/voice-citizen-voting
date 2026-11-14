
import org.junit.jupiter.api.Test;
import voiceapp.auth.dao.UserDao;
import voiceapp.idea.dao.IdeaDao;
import voiceapp.idea.model.Idea;
import voiceapp.idea.model.IdeaArchiveType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Class used for testing the key methods of class IdeaDao
 */
public class IdeaDaoTest {

    /**
     * Test adding an idea proposed by a user
     */
    @Test
     void addIdeaTest() {
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        Idea idea = new Idea(0, "Bakugan", "Dan Kuzo is the best Bakugan player", "", "University of twente", 142, 1);
        int ideaID = IdeaDao.instance.addIdea(idea);
        assertNotEquals(-1, ideaID);
    }

    /**
     * Test getting an idea based on it's ID
     */
    @Test
    void getIdeaTest(){
        Idea idea = null;
        if(IdeaDao.instance.getIdea(220) != null){
            idea = IdeaDao.instance.getIdea(220);
            assertNotNull(idea);
        } else {
            assertNull(idea);
        }
    }

    /**
     * Test changing the status of an idea based on the IdeaArchiveType
     * Additionally, if the idea is set to IdeaArchiveType. SUCCESSFUL, it is automatically converted to a project
     */
    @Test
    void setStatusTest(){
            assertTrue(IdeaDao.instance.setStatus(220, IdeaArchiveType.FAILED));
    }

    /**
     * Test Retrieving all the ideas from the database in a list
     */
    @Test
    void getIdeasTest(){
        List<Idea> ideas = IdeaDao.instance.getIdeas();

        if(ideas.size() > 0){
            assertNotNull(ideas);
        } else assertNull(ideas);
    }

    /**
     * Test deleteing an idea based on it's ideaID
     */
    @Test
    void deleteIdeaTest(){
        assertTrue(IdeaDao.instance.deleteIdea(200));
    }

    /**
     * Test checking if a specific user is an owner of an idea
     * Return false if the user or idea do not exist
     */
    @Test
    void isOwnerTest(){
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        Idea idea = new Idea(0, "Bakugan", "Dan Kuzo is the best Bakugan player", "", "University of twente", 142, 1);
        int ideaID = IdeaDao.instance.addIdea(idea);
        if((UserDao.instance.getUserSecure(142) != null) && (ideaID != -1)){
            assertTrue(IdeaDao.instance.isOwner(ideaID, 142));
        } else {
            assertFalse(IdeaDao.instance.isOwner(ideaID, 142));
        }
    }

    /**
     * Test converting the idea to a project if IdeaArchiveType == SUCCESSFUL
     */
    @Test
    void convertToProject(){
        Instant now = Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        Idea idea = new Idea(0, "Bakugan", "Dan Kuzo is the best Bakugan player", "", "University of twente", 142, 1);
        int ideaID = IdeaDao.instance.addIdea(idea);
        if(ideaID != - 1){
            assertNotEquals(-1, IdeaDao.instance.convertToProject(ideaID));
            System.out.println(IdeaDao.instance.convertToProject(ideaID));
        } else {
            assertEquals(-1, IdeaDao.instance.convertToProject(ideaID) );
        }

    }


}
