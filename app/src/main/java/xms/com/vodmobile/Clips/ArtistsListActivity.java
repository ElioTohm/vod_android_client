package xms.com.vodmobile.Clips;

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
import xms.com.vodmobile.Adapters.ArtistsAdapter;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.Genre;

public class ArtistsListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private ArtistsAdapter adapter;
    private List<Artist> artistList;
    View mContentView;
    int genre_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContentView = findViewById(R.id.content_main);
        hideNav();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        genre_id = intent.getIntExtra("genre_id", 9999);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        artistList = new ArrayList<>();
        adapter = new ArtistsAdapter(this, artistList);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startArtistDetailActivity(adapter.getItem(position));
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
        final ArrayList listgenre = new ArrayList();
        Genre Genretype = new Genre();
        Genretype.setType("Clips");
        listgenre.add(Genretype);
        Call<List<Artist>> call = apiInterface.GetArtists(listgenre);
        call.enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, retrofit2.Response<List<Artist>> response) {
                artistList.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                Log.e("retrofit", t.toString());
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

    private void startArtistDetailActivity (Artist artist)
    {
        Gson gson = new Gson();
        String objstring = gson.toJson(artist);
        startActivity(new Intent(ArtistsListActivity.this, SongsListActivity.class)
                .putExtra("artist",objstring));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);

        return true;
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
