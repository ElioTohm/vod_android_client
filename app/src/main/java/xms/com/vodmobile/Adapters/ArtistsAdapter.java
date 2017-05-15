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
import xms.com.vodmobile.objects.Artist;

/**
 * Created by Elio on 5/15/2017.
 */

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Artist> ArtistList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public ArtistsAdapter(Context mContext, List<Artist> ArtistList) {
        this.mContext = mContext;
        this.ArtistList = ArtistList;
    }

    @Override
    public ArtistsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        return new ArtistsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArtistsAdapter.MyViewHolder holder, int position) {
        Artist Artist = ArtistList.get(position);
        holder.title.setText(Artist.getName());

        // loading Artist cover using Glide library
        Glide.with(mContext).load(Artist.getImage()).into(holder.thumbnail);



    }

    @Override
    public int getItemCount() {
        return ArtistList.size();
    }


}
