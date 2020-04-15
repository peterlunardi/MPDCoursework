package com.example.mpdcoursework;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RoadTrafficItem {
    private String title = "";
    private String description;
    private String link;
    private float latitude;
    private float longitude;
    private String startDate;
    private String endDate;

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

            String start = splitString[0];
            String end = splitString[1];

            setStartDate(start);
            setEndDate(end);

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
            setStartDate("No start date provided.");
            setEndDate("No end date provided");
            this.description = description;
        }
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getImage()
    {
        return R.drawable.ic_warning;
    }
}
