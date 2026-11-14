package voiceapp.auth.model;

public class AuthData {
    private int userID;
    private String token;
    private boolean isValid;

    public AuthData(String cookie) {
        try {
            String[] authDataSplit = cookie.split("/");
            // token is authDataSplit[0], userid is authDataSplit[1]
            this.userID = Integer.parseInt(authDataSplit[3]);
            this.token = authDataSplit[1];
            isValid = true;
        } catch (RuntimeException e) {
            this.isValid = false;
        }
    }

    public int getUserID() {
        return userID;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
