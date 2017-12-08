package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.Clips.SongsListActivity;
import xms.com.vodmobile.MovieDetailActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.Series.SeriesDetailActivity;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

/**
 * CustomCardAdapter that shows image and text at the bottom respectively
 */

public class CustomCardAdapter extends RecyclerView.Adapter<CustomCardAdapter.SingleItemRowHolder> implements Filterable {

    private List<Object> itemsList;
    private Context mContext;
    private List<Object> searchedList;

    public CustomCardAdapter(Context context, List<Object> itemsList) {
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

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();
        String image;
        String title;

        if (item instanceof Video)
        {
            title = ((Video)item).getTitle();
            image = ((Video)item).getThumbnail();
        } else if (item instanceof Artist){
            title = ((Artist) item).getName();
            image = ((Artist) item).getImage();
        } else {
            title = ((Serie) item).getTitle();
            image = ((Serie) item).getThumbnail();
        }
        holder.tvTitle.setText(title);
        Glide.with(mContext)
                .load(image)
                .apply(requestOptions)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults returnFilterd = new FilterResults();
                final ArrayList<Object> results = new ArrayList<Object>();
                String title;
                if(searchedList == null)
                    searchedList = itemsList;
                if(constraint !=null){
                    if ( searchedList != null && searchedList.size()>0 ){
                        for (final Object item : searchedList){
                            if (item instanceof Video)
                            {
                                title = ((Video)item).getTitle();
                            } else if (item instanceof Artist){
                                title = ((Artist) item).getName();
                            } else {
                                title = ((Serie) item).getTitle();
                            }

                            if (title.toLowerCase().contains(constraint.toString()))
                            {
                                results.add(item);
                            }
                        }
                    }
                    returnFilterd.values = results;
                }

                return returnFilterd;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsList =(List<Object>)results.values;
                notifyDataSetChanged();
            }
        };
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
                v.getContext().startActivity(new Intent(v.getContext(), MovieDetailActivity.class)
                        .putExtra("video",objstring));
            } else if (this.item instanceof Serie) {
                v.getContext().startActivity(new Intent(v.getContext(), SeriesDetailActivity.class)
                        .putExtra("serie",objstring));
            } else if (this.item instanceof Artist){
                v.getContext().startActivity(new Intent(v.getContext(), SongsListActivity.class)
                        .putExtra("artist",objstring));
            }
        }
    }

}