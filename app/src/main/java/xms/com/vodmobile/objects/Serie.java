package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

public class Serie {
    @SerializedName("Title")
    private String title;

    @SerializedName("id")
    private String id;

    @SerializedName("Poster")
    private String thumbnail;

    @SerializedName("Plot")
    private String plot;

    @SerializedName("Actors")
    private String actors;

    @SerializedName("Released")
    private String released;

    @SerializedName("Runtime")
    private String runtime;

    @SerializedName("Rated")
    private String rated;

    public Serie() {
    }

    public Serie(String title, String video_id, String thumbnail,
                 String plot, String actors, String released, String runtime, String rated) {
        this.title = title;
        this.id = video_id;
        this.thumbnail = thumbnail;
        this.plot = plot;
        this.actors = actors;
        this.released = released;
        this.runtime =runtime;
        this.rated = rated;

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
        return id;
    }

    public void setVideoID(String video_id) {
        this.id = video_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
