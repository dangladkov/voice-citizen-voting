package voiceapp.auth.messages.incoming;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRegisterMsg {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String pwd;

    public UserRegisterMsg() {

    }

    public UserRegisterMsg(String name, String surname, String phoneNumber, String email, String pwd) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

}
