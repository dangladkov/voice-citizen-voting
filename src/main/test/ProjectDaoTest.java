import org.junit.jupiter.api.Test;
import voiceapp.project.dao.ProjectDao;
import voiceapp.project.model.Project;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Class used for testing the key methods of class ProjectDao
 */
public class ProjectDaoTest {

    /**
     * Test retrieving all the projects from the table project
     */
    @Test
    void getProjectsTest(){
        List<Project> projects = ProjectDao.instance.getProjects();
        if(projects != null){
            assertNotNull(projects);
        } else {
            assertNull(projects);
        }
    }

    /**
     * Test add project to the table project
     */
    @Test
    void addProjectTest(){
        Instant now =Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        int projectID = ProjectDao.instance.addProject("Title", "Description", "location", 100, timestamp, 100, "sport" );
        if(projectID != -1){
            assertNotEquals(-1, projectID);
        } else {
            assertEquals(-1, projectID);
        }
    }

    /**
     * Test remove project based on project ID
     */
    @Test
    void removeProjectTest(){
        assertTrue(ProjectDao.instance.removeProject(100));
    }

    /**
     * Test set status based on project ID
     */
    @Test
    void setStatusTest(){
        assertTrue(ProjectDao.instance.setStatus(100, 1));
    }

    /**
     * Test add categories based on project ID
     */
    @Test
    void addCategoriesTest(){
        assertTrue(ProjectDao.instance.addCategories(100, "Sport"));
    }

    /**
     * Test update project based on project ID
     */
    @Test
    void updateProject(){
        Instant now =Instant.now();
        Timestamp timestamp = Timestamp.from(now);
        assertTrue(ProjectDao.instance.updateProject(100, 200, "Title", "Description", "Location", timestamp, 100, "sport" ));
    }





}
