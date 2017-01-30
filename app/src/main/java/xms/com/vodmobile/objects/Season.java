package xms.com.vodmobile.objects;

/**
 * Created by Elio on 1/27/2017.
 */

public class Season {
    private String title;
    private int id;

    public Season() {
    }

    public Season(String title, int id) {
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