package voiceapp.auth.messages.incoming;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserAuthMsg {
    private int userID;
    private String token;

    public UserAuthMsg(int userID, String token) {
        this.userID = userID;
        this.token = token;
    }

    public int getUserID() {
        return userID;
    }

    public String getToken() {
        return token;
    }
}
