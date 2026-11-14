package voiceapp.shop.messages;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Message representing the attributes of a benefit/item.
 */
@XmlRootElement
public class BenefitAttributesMsg {
    public int count;
    public String size;
    public int color;
}
