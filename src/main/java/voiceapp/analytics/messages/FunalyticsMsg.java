package voiceapp.analytics.messages;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FunalyticsMsg {
    private int totalProjects;
    private int totalPoints;
    private int totalNumberOfUser;
    private int totalNumberOfVotes;

    public FunalyticsMsg(int totalProjects, int totalPoints, int totalNumberOfUser, int totalNumberOfVotes) {
        this.totalProjects = totalProjects;
        this.totalPoints = totalPoints;
        this.totalNumberOfUser = totalNumberOfUser;
        this.totalNumberOfVotes = totalNumberOfVotes;
    }

    public int getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalNumberOfUser() {
        return totalNumberOfUser;
    }

    public void setTotalNumberOfUser(int totalNumberOfUser) {
        this.totalNumberOfUser = totalNumberOfUser;
    }

    public int getTotalNumberOfVotes() {
        return totalNumberOfVotes;
    }

    public void setTotalNumberOfVotes(int totalNumberOfVotes) {
        this.totalNumberOfVotes = totalNumberOfVotes;
    }
}
