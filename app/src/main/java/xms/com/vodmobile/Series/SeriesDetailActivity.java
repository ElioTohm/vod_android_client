package xms.com.vodmobile.Series;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.Adapters.SeasonsAdapter;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Season;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.ui.GridSpacingItemDecoration;

public class SeriesDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SeasonsAdapter adapter;
    private List<Season> seasonList;

    private static String tag_json_obj = "season_request";
    Serie series;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = new ProgressDialog(SeriesDetailActivity.this);
        dialog.setMessage("Loading..");
        dialog.show();
        Intent intent = getIntent();
        Gson gson = new Gson();
        series = gson.fromJson(intent.getStringExtra("serie"), Serie.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(series.getTitle());

        TextView plot = (TextView)findViewById(R.id.plot);
        TextView runtime = (TextView)findViewById(R.id.Runtime);
        TextView releasedate = (TextView)findViewById(R.id.releaseDate);
        TextView actors = (TextView)findViewById(R.id.actors);

        plot.setText(series.getPlot());
        runtime.setText(series.getRuntime());
        releasedate.setText(series.getReleased());
        actors.setText(series.getActors());

        try {
            Glide.with(this).load(series.getThumbnail()).into((ImageView) findViewById(R.id.backdrop));
            Log.d("thumbnail", series.getThumbnail());
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        seasonList = new ArrayList<>();
        adapter = new SeasonsAdapter(this, seasonList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 1, 2, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Season season = seasonList.get(position);
                stratEpisodeListActivity(season);
            }

            @Override
            public void onLongClick(View view, int position) {}
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

    private void prepareAlbums() {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        Serie seriesid = new Serie();
        seriesid.setVideoID(series.getVideoID());
        ArrayList arrayList = new ArrayList();
        arrayList.add(seriesid);
        Call<List<Season>> call = apiInterface.GetSeasons(arrayList);
        call.enqueue(new Callback<List<Season>>() {
            @Override
            public void onResponse(Call<List<Season>> call, Response<List<Season>> response) {
                seasonList.addAll(response.body());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Season>> call, Throwable t) {
                Log.d("TEST", t.toString());
            }
        });
    }

    private void stratEpisodeListActivity (Season season)
    {
        startActivity(new Intent(this, EpisodesListActivity.class)
                .putExtra("season", season.getID())
                .putExtra("serieID", series.getVideoID()));
    }
}

