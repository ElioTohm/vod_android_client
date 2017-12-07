package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Video;

/**
 * Created by Elio on 1/27/2017.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<Video> videoList;
    private List<Video> searchedvideoList;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults returnFilterd = new FilterResults();
                final ArrayList<Video> results = new ArrayList<Video>();
                if(searchedvideoList==null)
                    searchedvideoList = videoList;
                if(constraint !=null){
                    if (searchedvideoList != null && searchedvideoList.size()>0 ){
                        for (final Video g : searchedvideoList){
                            if (g.getTitle().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    returnFilterd.values = results;
                }

                return returnFilterd;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                videoList =(List<Video>)results.values;
                notifyDataSetChanged();

            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTitle);
            thumbnail = (ImageView) view.findViewById(R.id.itemImage);
        }
    }


    public VideosAdapter(Context mContext, List<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_card,null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.title.setText(video.getTitle());

        // loading video cover using Glide library
        Glide.with(mContext).load(video.getThumbnail())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public Video getItem(int position) {
        return videoList.get(position);
    }
}
