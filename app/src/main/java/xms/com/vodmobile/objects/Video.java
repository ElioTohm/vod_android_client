package xms.com.vodmobile.objects;

public class Video {
    private String title;
    private int video_id;
    private String thumbnail;

    public Video() {
    }

    public Video(String title, int video_id, String thumbnail) {
        this.title = title;
        this.video_id = video_id;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

    public int getVideoID() {
        return video_id;
    }

    public void setVideoID(int video_id) {
        this.video_id = video_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
