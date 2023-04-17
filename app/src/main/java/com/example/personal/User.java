package com.example.personal;

public class User {
    public String username;
    public String emailid ;
    public String pass ;
    public String levelup;
    public String xp;
    public String workdone;

    public User(){

    }

    public User(String username,String emailid, String pass, String levelup, String xp, String workdone){
        this.username = username ;
        this.emailid = emailid ;
        this.pass = pass ;
        this.levelup = levelup;
        this.workdone = workdone;
        this.xp = xp;
    }
}
