package com.example.mpdcoursework;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/*

Name: Peter Lunardi
Student ID: S1636120

 */

public class RoadTrafficItem {
    private String title = "";
    private String description;
    private String link;
    private float latitude;
    private float longitude;
    private Date startDate;
    private Date endDate;
    private String stringStartDate;
    private String stringEndDate;
    private float delayTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String newTitle = title.replace("NB", "Northbound")
                .replace("SB", "Southbound")
                .replace("WB", "Westbound")
                .replace("EB", "Eastbound")
                .replace("N/B", "Northbound")
                .replace("S/B", "Southbound")
                .replace("W/B", "Westbound")
                .replace("E/B", "Eastbound")
                .replace("Jnct", "Junction ")
                .replace("TTL", "Temporary traffic lights");
        this.title = newTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description.contains("<br /"))
        {
            String[] splitString = description.split("<br />");

            String strStart = splitString[0];
            String strEnd = splitString[1];

            this.stringStartDate = strStart;
            this.stringEndDate = strEnd;

            String[] start = splitString[0].split("Start Date: ");
            String[] end = splitString[1].split("End Date: ");

            setStartDate(stringToDateFormat(start[1]));
            setEndDate(stringToDateFormat(end[1]));

            long diffInMillies = Math.abs(getEndDate().getTime() - getStartDate().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            this.delayTime = Math.round(diff);

            if(splitString.length > 2)
            {
                this.description = splitString[2];
            }
            else
            {
                this.description = "No description provided.";
            }
        }
        else
        {
            //setStartDate("No start date provided.");
            //setEndDate("No end date provided");
            this.description = description;
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStringStartDate() {
        return stringStartDate;
    }

    public String getStringEndDate() {
        return stringEndDate;
    }

    public float getDelayTime() {
        return delayTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    //rewrite this so it changes to a specific colour based on how long the roadworks will last
    public int getImage()
    {

        if(getDelayTime() <= 2)
        {
            return R.drawable.ic_warning_green;
        }
        else if (getDelayTime() <= 7 && getDelayTime() > 2)
        {
            return R.drawable.ic_warning_amber;
        }
        else if (getDelayTime() > 7)
        {
            return R.drawable.ic_warning_red;
        }
        return R.drawable.ic_warning_red;
    }

    private Date stringToDateFormat(String string)
    {
        DateFormat format = new SimpleDateFormat("EEEE, dd MMMMM y - kk:mm", Locale.UK);
        try {
            return format.parse(string.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNumericalStartDate(Date dt)
    {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

        String strDt = simpleDate.format(dt);

        return strDt;
    }

    public String getNumericalEndDate(Date dt)
    {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

        String strDt = simpleDate.format(dt);

        return strDt;
    }

    public List<String> getDates()
    {
        ArrayList<Date> dates = new ArrayList<Date>();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(getStartDate());


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(getEndDate());

        while(!cal1.after(cal2))
        {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }

        ArrayList<String> strDates = new ArrayList<>();
        for(Date date: dates)
        {
            String s = getNumericalEndDate(date);
            strDates.add(s);
        }

        return strDates;
    }
}
