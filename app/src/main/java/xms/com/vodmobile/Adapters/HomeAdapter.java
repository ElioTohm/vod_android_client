package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Type;

/**
 * Created by Elio on 1/28/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Type> videoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public HomeAdapter(Context mContext, List<Type> videoList) {
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
        Type video = videoList.get(position);
        holder.title.setText(video.getTitle());

        // loading video cover using Glide library
        Glide.with(mContext).load(video.getThumbnail()).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.thumbnail);

    }



    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
