package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Elio on 5/15/2017.
 */

public class Artist {
    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("id")
    private Integer id;

    private Integer artist_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(Integer artist_id) {
        this.artist_id = artist_id;
    }

    public Artist() {
    }

    public Artist(Integer id, String name, String image ) {
        this.name = name;
        this.image = image;
        this.id = id;
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

}
