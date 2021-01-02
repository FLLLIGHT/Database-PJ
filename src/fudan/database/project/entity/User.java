package fudan.database.project.entity;

import java.util.Date;

public class User {
    private int uid;
    private String userName;
    private String pass;

    public User() {
        //constructor without parameter
    }

    public User(String userName, String pass) {
        this.userName = userName;
        this.pass = pass;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
