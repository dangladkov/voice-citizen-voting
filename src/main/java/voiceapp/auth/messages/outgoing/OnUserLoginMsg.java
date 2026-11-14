package voiceapp.auth.messages.outgoing;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OnUserLoginMsg {
    public int userID;
    public String token;

    public OnUserLoginMsg(int userID, String token) {
        this.userID = userID;
        this.token = token;
    }
}
