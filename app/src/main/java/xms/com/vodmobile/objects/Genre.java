package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

public class Genre {

    @SerializedName("genre_name")
    private String title;

    @SerializedName("genre_id")
    private int id;

    private int genre;

    private String Type;

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public void setType(String type) {
        Type = type;
    }

    public Genre() {

    }

    public Genre(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public int getID() {
        return id;
    }

    public void setID(int year) {
        this.id = year;
    }

}