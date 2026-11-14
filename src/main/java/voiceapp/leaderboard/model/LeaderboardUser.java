package voiceapp.leaderboard.model;

/**
 * Leaderboard data class containing all attributes for a specific user in the leaderboard.
 */
public class LeaderboardUser {
    private String firstName;
    private String lastName;
    private int points;
    private boolean isAnonymous;

    public LeaderboardUser(String firstName, String lastName, int points, boolean isAnonymous) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.points = points;
        this.isAnonymous = isAnonymous;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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


