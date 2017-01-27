package xms.com.vodmobile.objects;

/**
 * Created by Elio on 1/27/2017.
 */

public class Genre {
    private String title;
    private int id;

    public Genre() {
    }

    public Genre(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle1(String name) {
        this.title = name;
    }

    public int getID() {
        return id;
    }

    public void setID(int year) {
        this.id = year;
    }

}