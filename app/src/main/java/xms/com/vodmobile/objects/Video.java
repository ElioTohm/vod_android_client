package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("id")
    private String video_id;

    @SerializedName("Title")
    private String title;

    @SerializedName("Rated")
    private String rated;

    @SerializedName("Released")
    private String released;

    @SerializedName("Runtime")
    private String runtime;

    @SerializedName("Actors")
    private String actors;

    @SerializedName("Plot")
    private String plot;

    @SerializedName("Poster")
    private String thumbnail;

    @SerializedName("Subtitle")
    private String subtitle;

//    @SerializedName("imdbID")
//    @SerializedName("imdbRating")
//    @SerializedName("Type")
//    @SerializedName("Language")
//    @SerializedName("Country")
//    @SerializedName("Awards")
//    @SerializedName("Director")
//    @SerializedName("Writer")
//    @SerializedName("Year")


    public Video(String title, String video_id, String thumbnail,
                 String plot, String actors, String released, String runtime, String rated, String subtitle) {
        this.title = title;
        this.video_id = video_id;
        this.thumbnail = thumbnail;
        this.plot = plot;
        this.actors = actors;
        this.released = released;
        this.runtime =runtime;
        this.rated = rated;
        this.subtitle = subtitle;
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }


    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        rated = rated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

    public String getVideoID() {
        return video_id;
    }

    public void setVideoID(String video_id) {
        this.video_id = video_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
