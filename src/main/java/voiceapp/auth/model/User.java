package voiceapp.auth.model;

import voiceapp.auth.dao.UserDao;
import voiceapp.auth.messages.incoming.UserRegisterMsg;

public class User {
    private int uid;

    private int points;

    private int totalPoints;
    private boolean isAnonymous;

    private UserDao.AccessLevel accessLevel;
    private UserRegisterMsg urm;

    public User(int uid, String name, String surname, String phoneNumber, String email, int points, int totalPoints, boolean isAnonymous, String pwd) {
        this.uid = uid;
        this.points = points;
        this.totalPoints = totalPoints;
        this.isAnonymous = isAnonymous;
        this.urm = new UserRegisterMsg(name, surname, phoneNumber, email, pwd);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public UserRegisterMsg getUrm() {
        return urm;
    }

    public void setUrm(UserRegisterMsg urm) {
        this.urm = urm;
    }

    public UserDao.AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(UserDao.AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }
}
