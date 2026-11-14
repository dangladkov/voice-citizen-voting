package voiceapp.auth.messages.incoming;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserLoginMsg {
    private String userIdentifier;

    private String pwd;

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
