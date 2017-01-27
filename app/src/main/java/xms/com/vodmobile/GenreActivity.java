package xms.com.vodmobile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.Adapters.GenresAdapter;
import xms.com.vodmobile.objects.Genre;

public class GenreActivity extends AppCompatActivity {
    private List<Genre> genreList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GenresAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new GenresAdapter(genreList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        prepareGenreData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Genre movie = genreList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareGenreData() {
        Genre genre = new Genre("Mad Max: Fury Road", "Action & Adventure", "2015");
        genreList.add(genre);

        genre = new Genre("Inside Out", "Animation, Kids & Family", "2015");
        genreList.add(genre);

        genre = new Genre("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        genreList.add(genre);

        genre = new Genre("Shaun the Sheep", "Animation", "2015");
        genreList.add(genre);

        genre = new Genre("The Martian", "Science Fiction & Fantasy", "2015");
        genreList.add(genre);

        genre = new Genre("Mission: Impossible Rogue Nation", "Action", "2015");
        genreList.add(genre);

        genre = new Genre("Up", "Animation", "2009");
        genreList.add(genre);

        genre = new Genre("Star Trek", "Science Fiction", "2009");
        genreList.add(genre);

        genre = new Genre("The LEGO Genre", "Animation", "2014");
        genreList.add(genre);

        genre = new Genre("Iron Man", "Action & Adventure", "2008");
        genreList.add(genre);

        genre = new Genre("Aliens", "Science Fiction", "1986");
        genreList.add(genre);

        genre = new Genre("Chicken Run", "Animation", "2000");
        genreList.add(genre);

        genre = new Genre("Back to the Future", "Science Fiction", "1985");
        genreList.add(genre);

        genre = new Genre("Raiders of the Lost Ark", "Action & Adventure", "1981");
        genreList.add(genre);

        genre = new Genre("Goldfinger", "Action & Adventure", "1965");
        genreList.add(genre);

        genre = new Genre("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        genreList.add(genre);

        mAdapter.notifyDataSetChanged();
    }
}
