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

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Serie;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> implements Filterable{

    private Context mContext;
    private List<Serie> serieList;
    private List<Serie> searchedserieList;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults returnFilterd = new FilterResults();
                final ArrayList<Serie> results = new ArrayList<Serie>();
                if(searchedserieList == null)
                    searchedserieList = serieList;
                if(constraint !=null){
                    if (searchedserieList != null && searchedserieList.size()>0 ){
                        for (final Serie g : searchedserieList){
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
                serieList =(List<Serie>)results.values;
                notifyDataSetChanged();

            }

        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, id;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
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

    public Serie getItem(int position) {
        return serieList.get(position);
    }
}
