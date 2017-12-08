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
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Artist;

/**
 * Created by Elio on 5/15/2017.
 */

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.MyViewHolder> implements Filterable{

    private Context mContext;
    private List<Artist> ArtistList;
    private List<Artist> filteredArtistList;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults returnFilterd = new FilterResults();
                final ArrayList<Artist> results = new ArrayList<Artist>();
                if(filteredArtistList == null)
                    filteredArtistList = ArtistList;
                if(constraint !=null){
                    if (filteredArtistList != null && filteredArtistList.size()>0 ){
                        for (final Artist g : filteredArtistList){
                            if (g.getName().toLowerCase().contains(constraint.toString()))
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
                ArtistList =(List<Artist>)results.values;
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


    public ArtistsAdapter(Context mContext, List<Artist> ArtistList) {
        this.mContext = mContext;
        this.ArtistList = ArtistList;
    }

    @Override
    public ArtistsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_card, parent, false);

        return new ArtistsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArtistsAdapter.MyViewHolder holder, int position) {
        Artist Artist = ArtistList.get(position);
        holder.title.setText(Artist.getName());

        // loading Artist cover using Glide library
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(mContext).load(Artist.getImage())
                .apply(requestOptions)
                .into(holder.thumbnail);



    }

    @Override
    public int getItemCount() {
        return ArtistList.size();
    }

    public Artist getItem(int position) {
        return ArtistList.get(position);
    }
}
