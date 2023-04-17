package com.example.personal;

public class Categories {

    public String General;
    public String Health;
    public String Work;
    public String Education;
    public String Events ;
    public String Food;
    //public String Alarms;

    public Categories(){

    }

    public Categories(String Gen,String hel,String wor,String edu, String eve,String foo){
        this.General = Gen;
        this.Health = hel;
        this.Work = wor;
        this.Education = edu;
        this.Events = edu;
        this.Food = foo;
        //this.Alarms = al;
    }
}