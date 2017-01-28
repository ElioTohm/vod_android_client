package xms.com.vodmobile.objects;

/**
 * Created by Elio on 1/28/2017.
 */

public class Type {
    private String title, thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Type(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

}
