package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elio on 1/27/2017.
 */

public class Season {

    @SerializedName("season")
    private int season;

    private String serieID;


    private String title;

    public Season() {
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setSerieID(String serieID) {
        this.serieID = serieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public int getID() {
        return season;
    }

}