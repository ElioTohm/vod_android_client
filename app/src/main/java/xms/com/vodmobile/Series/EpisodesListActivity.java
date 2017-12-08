package xms.com.vodmobile.Series;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xms.com.vodmobile.Adapters.EpisodesAdapter;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Episode;
import xms.com.vodmobile.objects.Season;
import xms.com.vodmobile.player.PlayerActivity;

public class EpisodesListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EpisodesAdapter adapter;
    private List<Episode> episodeList;

    private static String tag_json_obj = "episode_request";
    String serieID;
    int seasonid;
    View mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentView = findViewById(R.id.content_main);
        hideNav();

        Intent intent = getIntent();
        seasonid = intent.getIntExtra("season", 1);
        serieID = intent.getStringExtra("serieID");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        episodeList = new ArrayList<>();
        adapter = new EpisodesAdapter(this, episodeList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Episode episode = episodeList.get(position);
                startPlayerActivity(episode);
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

    private void prepareAlbums() {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        Season season = new Season();
        season.setSerieID(serieID);
        season.setSeason(seasonid);
        ArrayList arrayList = new ArrayList();
        arrayList.add(season);
        Call<List<Episode>> call = apiInterface.GetEpisodes(arrayList);
        call.enqueue(new Callback<List<Episode>>() {
            @Override
            public void onResponse(Call<List<Episode>> call, Response<List<Episode>> response) {
                episodeList.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Episode>> call, Throwable t) {

            }
        });
    }

    private void startPlayerActivity (Episode episode)
    {
        startActivity(new Intent(EpisodesListActivity.this, PlayerActivity.class)
                .putExtra("stream", episode.getStream())
                .putExtra("type", "series")
                .putExtra("subtitle",episode.getSubtitle()));
    }

    @Override
    protected void onResume(){
        super.onResume();
        hideNav();
    }

    private void hideNav() {
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}