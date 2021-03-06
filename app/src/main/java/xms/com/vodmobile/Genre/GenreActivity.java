package xms.com.vodmobile.Genre;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.Adapters.GenresAdapter;
import xms.com.vodmobile.Clips.ClipsListActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.Series.SeriesListActivity;
import xms.com.vodmobile.VideoListActivity;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.objects.Genre;
import xms.com.vodmobile.ui.GridSpacingItemDecoration;

public class GenreActivity extends AppCompatActivity {
    private List<Genre> genreList;
    private RecyclerView recyclerView;
    private GenresAdapter mAdapter;
    ProgressDialog dialog;
    private static String tag_json_obj = "genre_request";
    String genre_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(GenreActivity.this);
        dialog.setMessage("Loading..");
        dialog.show();

        Intent intent = getIntent();
        genre_type = intent.getStringExtra("genre_type");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_genre);
        genreList = new ArrayList<>();
        mAdapter = new GenresAdapter(genreList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 1, 2, false));

        recyclerView.setAdapter(mAdapter);

        prepareGenreData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Genre genre = genreList.get(position);
                gotoVideoList(genre);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void  prepareGenreData() {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        final ArrayList listgenre = new ArrayList();
        Genre gernetype = new Genre();
        gernetype.setType(genre_type);
        listgenre.add(gernetype);
        Call<List<Genre>> call = apiInterface.GetGenres(listgenre);
        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, retrofit2.Response<List<Genre>> response) {
                genreList.add(new Genre("All",9999));
                genreList.addAll(response.body());
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Log.e("MovieGenreRequest", t.toString());
            }
        });

    }

    private void gotoVideoList (Genre genre)
    {
        if (genre_type.equals("Movies")) {
            Intent intent = new Intent(this, VideoListActivity.class);
            intent.putExtra("genre_id", genre.getID());
            startActivity(intent);
        } else if (genre_type.equals("Series")) {
            Intent intent = new Intent(this, SeriesListActivity.class);
            intent.putExtra("genre_id", genre.getID());
            startActivity(intent);
        } else if (genre_type.equals("Clips")) {
            Intent intent = new Intent(this, ClipsListActivity.class);
            intent.putExtra("genre_id", genre.getID());
            startActivity(intent);
        }

    }

}
