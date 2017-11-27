package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.List;

import xms.com.vodmobile.Clips.ClipsListActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.Series.SeriesDetailActivity;
import xms.com.vodmobile.VideoDetailActivity;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

/**
 * Created by Elio on 11/23/2017.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private List<Object> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, List<Object> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        Object item = itemsList.get(i);
        holder.setVideo(item);
        if (item instanceof Video)
        {
            holder.tvTitle.setText(((Video)item).getTitle());
            Glide.with(mContext)
                    .load(((Video)item).getThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.itemImage);
        } else if (item instanceof Artist){
            holder.tvTitle.setText(((Artist) item).getName());
            Glide.with(mContext)
                    .load(((Artist) item).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.itemImage);
        } else {
            holder.tvTitle.setText(((Serie) item).getTitle());
            Glide.with(mContext)
                    .load(((Serie) item).getThumbnail())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.itemImage);
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView tvTitle;

        protected ImageView itemImage;

        protected Object item;

        public void setVideo(Object item) {
            this.item = item;
        }

        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Gson gson = new Gson();
            String objstring = gson.toJson(this.item);
            if (this.item instanceof Video) {
                v.getContext().startActivity(new Intent(v.getContext(), VideoDetailActivity.class)
                        .putExtra("video",objstring));
            } else if (this.item instanceof Serie) {
                v.getContext().startActivity(new Intent(v.getContext(), SeriesDetailActivity.class)
                        .putExtra("serie",objstring));
            } else if (this.item instanceof Artist){
                v.getContext().startActivity(new Intent(v.getContext(), ClipsListActivity.class)
                        .putExtra("artist",objstring));
            }
        }
    }

}