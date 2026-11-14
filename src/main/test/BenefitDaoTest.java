import org.junit.jupiter.api.Test;
import voiceapp.shop.dao.BenefitDao;
import voiceapp.shop.model.Benefit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class used for testing the key methods of class BenefitDao
 */
public class BenefitDaoTest {

    /**
     * Test adding an instance of a benefit and returns the ID of the created object, otherwise returns -1
     */
    @Test
    void addBenefitTest(){
        int benefitID = BenefitDao.instance.addBenefit("Lego", "If you step on them, it's gg", 25, "", "" );
        assertNotEquals(-1, benefitID);
    }

    /**
     * Test getting the benefit based on it's ID
     */
    @Test
    void getBenefitTest(){
        int benefitID = BenefitDao.instance.addBenefit("Lego", "If you step on them, it's gg", 25, "", "" );
        Benefit benefit = BenefitDao.instance.getBenefit(benefitID);
        if(benefitID != -1){
            assertNotNull(benefit);
        } else {
            assertNull(benefit);
        }
    }

    /**
     * Test deleting a benefit
     */
    @Test
    void deleteBenefitTest(){
        int benefitID = BenefitDao.instance.addBenefit("Lego", "If you step on them, it's gg", 25, "", "" );
        assertTrue(BenefitDao.instance.deleteBenefit(benefitID));
    }

    /**
     * Test getting all the benefits' IDs
     */
    @Test
    void getAllBenefitsTest(){
        List<Integer> benefits = BenefitDao.instance.getAllBenefits();
        if(benefits.size() > 0){
            assertNotNull(benefits);
        }else {
            assertNull(benefits);
        }
    }

}
