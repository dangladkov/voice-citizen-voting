package voiceapp.shop.messages;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Class containing all benefit IDs.
 */
@XmlRootElement
public class CompactBenefitsMsg {
    private List<Integer> benefitIDs;

    public CompactBenefitsMsg(List<Integer> benefitIDs) {
        this.benefitIDs = benefitIDs;
    }

    public CompactBenefitsMsg() {
    }

    public List<Integer> getBenefitIDs() {
        return benefitIDs;
    }

    public void setBenefitIDs(List<Integer> benefitIDs) {
        this.benefitIDs = benefitIDs;
    }

}
