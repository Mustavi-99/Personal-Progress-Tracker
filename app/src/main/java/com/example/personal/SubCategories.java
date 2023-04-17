package com.example.personal;

public class SubCategories {

    //public String Category;
    public String Subject;
    public String Description;
    public String Date;
    public String Time;


    public SubCategories() {

    }

    public SubCategories( String sub, String des, String date, String time) {
        //this.Category = cat;
        this.Subject = sub;
        this.Description = des;
        this.Date = date;
        this.Time = time;
    }

}
