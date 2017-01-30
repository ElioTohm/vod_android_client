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
import xms.com.vodmobile.objects.Season;

/**
 * Created by Elio on 1/30/2017.
 */

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Season> seasonList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public SeasonsAdapter(Context mContext, List<Season> seasonList) {
        this.mContext = mContext;
        this.seasonList = seasonList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Season season = seasonList.get(position);
        holder.title.setText(season.getTitle());

        // loading season cover using Glide library
//        Glide.with(mContext).load(season.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return seasonList.size();
    }
}
