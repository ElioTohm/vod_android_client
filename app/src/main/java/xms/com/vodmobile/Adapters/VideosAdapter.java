package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Video;

/**
 * Created by Elio on 1/27/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {

    private Context mContext;
    private List<Video> videoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public VideosAdapter(Context mContext, List<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.title.setText(video.getTitle());

        // loading video cover using Glide library
        Glide.with(mContext).load(video.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
