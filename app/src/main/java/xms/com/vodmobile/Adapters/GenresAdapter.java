package xms.com.vodmobile.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xms.com.vodmobile.R;
import xms.com.vodmobile.objects.Genre;

/**
 * Created by Elio on 1/27/2017.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder> {

    private List<Genre> genresList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
        }
    }


    public GenresAdapter(List<Genre> genresList) {
        this.genresList = genresList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Genre genre = genresList.get(position);
        holder.title.setText(genre.getTitle());
        holder.genre.setText(genre.getGenre());
        holder.year.setText(genre.getYear());
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }
}