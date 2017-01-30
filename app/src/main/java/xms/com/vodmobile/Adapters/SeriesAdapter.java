package xms.com.vodmobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.RequestQueuer.AppController;
import xms.com.vodmobile.objects.Serie;

/**
 * Created by Elio on 1/30/2017.
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Serie> serieList;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public SeriesAdapter(Context mContext, List<Serie> serieList) {
        this.mContext = mContext;
        this.serieList = serieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.serie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Serie serie = serieList.get(position);
        holder.title.setText(serie.getTitle());

        // loading serie cover using Glide library
        Glide.with(mContext).load(serie.getThumbnail()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return serieList.size();
    }
}
