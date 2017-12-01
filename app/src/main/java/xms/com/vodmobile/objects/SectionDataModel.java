package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Elio on 11/23/2017.
 */

public class SectionDataModel {
    @SerializedName("type")
    private String Type;

    @SerializedName("title")
    private String headerTitle;

    private List<Object> objectList;

    @SerializedName("movies_result")
    private List<Video> videoList;

    @SerializedName("series_result")
    private List<Serie> serieList;

    @SerializedName("artists_result")
    private List<Artist> artistList;


    public SectionDataModel(String headerTitle,String type, List<Object> objectList) {
        this.headerTitle = headerTitle;
        this.objectList= objectList;
        this.Type = type;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public List<Serie> getSerieList() {
        return serieList;
    }

    public void setSerieList(List<Serie> serieList) {
        this.serieList = serieList;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getType() {
        return Type;
    }

    public List<Object> getAllItemsInSection() {
        return this.objectList;
    }
    public void setType(String type) {
        Type = type;
    }
}
