package voiceapp.auth.messages.outgoing;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDataMsg {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private int accessLevel;
    private int points;
    private boolean isAnonymous;

    public UserDataMsg(String name, String surname, String phoneNumber, String email, int accessLevel, int points, boolean isAnonymous) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accessLevel = accessLevel;
        this.points = points;
        this.isAnonymous = isAnonymous;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
