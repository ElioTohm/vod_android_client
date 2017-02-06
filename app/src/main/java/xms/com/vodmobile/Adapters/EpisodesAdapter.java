package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Episode;

/**
 * Created by Elio on 2/6/2017.
 */

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Episode> EpisodeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }


    public EpisodesAdapter(Context mContext, List<Episode> EpisodeList) {
        this.mContext = mContext;
        this.EpisodeList = EpisodeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episode_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Episode Episode = EpisodeList.get(position);
        holder.title.setText(Episode.getTitle());

    }

    @Override
    public int getItemCount() {
        return EpisodeList.size();
    }
}
