package xms.com.vodmobile.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {
    /**
     * An array of sample (video) items.
     */
    public static final List<VideoItem> ITEMS = new ArrayList<VideoItem>();

    /**
     * A map of sample (video) items, by ID.
     */
    public static final Map<String, VideoItem> ITEM_MAP = new HashMap<String, VideoItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createVideoItem(i));
        }
    }

    private static void addItem(VideoItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static VideoItem createVideoItem(int position) {
        return new VideoItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details : ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\n12312312.");
        }
        return builder.toString();
    }

    /**
     * A video item representing a piece of content.
     */
    public static class VideoItem {
        public final String id;
        public final String content;
        public final String details;

        public VideoItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}

