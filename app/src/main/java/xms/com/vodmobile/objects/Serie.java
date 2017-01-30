package xms.com.vodmobile.objects;

/**
 * Created by Elio on 1/27/2017.
 */

public class Serie {
    private String title, video_id, thumbnail, plot, actors, released, runtime, rated;

    public Serie() {
    }

    public Serie(String title, String video_id, String thumbnail,
                 String plot, String actors, String released, String runtime, String rated) {
        this.title = title;
        this.video_id = video_id;
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
