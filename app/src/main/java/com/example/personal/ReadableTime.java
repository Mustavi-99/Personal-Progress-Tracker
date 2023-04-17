package com.example.personal;
public class ReadableTime {
    int hour ,min;

    public ReadableTime()
    {

    }

    public ReadableTime(int hour ,int min)
    {
        this.hour=hour;
        this.min=min;
    }

    public String  convertTime(int hour ,int min)
    {
        String am_pm="";

        String h=String.valueOf(hour),m=String.valueOf(min);
        String time;

        if(hour>11)
        {
            am_pm="PM";

            hour %= 12;
            h=String.valueOf(hour);
        }
        else if(hour >= 0)
        {
            am_pm="AM";
        }

        if(h.length()<2)
        {
            h="0"+h;
        }
        if(h.equals("0"))
        {
            h="12";
        }
        if(m.length()<2)
        {
            m="0"+m;
        }

        time=" "+h+":"+m+" "+am_pm;

        return time;

    }
}