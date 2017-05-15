package xms.com.vodmobile.objects;

/**
 * Created by Elio on 5/15/2017.
 */

public class Artist {
    private String name, image;
    private Integer ID;
    public Artist() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Artist(Integer ID, String name, String image) {
        this.name = name;
        this.image = image;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
