package xms.com.vodmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.Adapters.VideosAdapter;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Genre;
import xms.com.vodmobile.objects.Video;
import xms.com.vodmobile.ui.GridSpacingItemDecoration;

public class VideoListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private VideosAdapter adapter;
    private List<Video> videoList;

    ProgressDialog dialog;
    int genre_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(VideoListActivity.this);
        dialog.setMessage("Loading..");
        dialog.show();

        Intent intent = getIntent();
        genre_id = intent.getIntExtra("genre_id", 9999);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        videoList = new ArrayList<>();
        adapter = new VideosAdapter(this, videoList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startVideoDetailActivity(adapter.getItem(position));
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
        ArrayList Listgenre = new ArrayList();
        Genre genreid = new Genre();
        genreid.setGenre(genre_id);
        Listgenre.add(genreid);
        Call<List<Video>> call = apiInterface.GetVideos(Listgenre);
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, retrofit2.Response<List<Video>> response) {
                videoList.addAll(response.body());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("VideoListRequest", t.toString());
            }
        });


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }


    private void startVideoDetailActivity (Video video)
    {
        Gson gson = new Gson();
        String objstring = gson.toJson(video);
        startActivity(new Intent(VideoListActivity.this, VideoDetailActivity.class)
                .putExtra("video",objstring));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);

        return true;
    }
}
