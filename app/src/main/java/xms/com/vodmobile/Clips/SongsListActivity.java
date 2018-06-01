package xms.com.vodmobile.Clips;

import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.Adapters.SongsAdapter;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.Episode;
import xms.com.vodmobile.PlayerActivity;

public class SongsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongsAdapter adapter;
    private List<Episode> episodelist;

    Artist artist;
    View mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clips_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContentView = findViewById(R.id.content_main);
        hideNav();

        Intent intent = getIntent();
        Gson gson = new Gson();
        artist = gson.fromJson(intent.getStringExtra("artist"), Artist.class);
        Log.d("test", artist.getImage());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(artist.getName());

        try {
            Glide.with(this).load(artist.getImage()).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        episodelist = new ArrayList<>();
        adapter = new SongsAdapter(this, episodelist);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareAlbums();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Episode episode = episodelist.get(position);
                startPlayerActivity (episode);
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

    private void prepareAlbums () {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        ArrayList listartist = new ArrayList();
        Artist artistid = new Artist();
        artistid.setArtist_id(artist.getId());
        listartist.add(artistid);
        Call<List<Episode>> call = apiInterface.GetClips(listartist);
        call.enqueue(new Callback<List<Episode>>() {
            @Override
            public void onResponse(Call<List<Episode>> call, retrofit2.Response<List<Episode>> response) {
                episodelist.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Episode>> call, Throwable t) {

            }
        });
    }

    private void startPlayerActivity (Episode episode)
    {
        startActivity(new Intent(SongsListActivity.this, PlayerActivity.class)
                .putExtra("subtitle", episode.getSubtitle())
                .setData(Uri.parse(ApiService.BASE_URL + "videos/clips/" + episode.getStream()))
                .setAction(PlayerActivity.ACTION_VIEW));
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
