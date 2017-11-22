package xms.com.vodmobile.Series;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.Adapters.SeriesAdapter;
import xms.com.vodmobile.R;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.objects.Genre;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.ui.GridSpacingItemDecoration;

public class SeriesListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private SeriesAdapter adapter;
    private List<Serie> serieList;
    ProgressDialog dialog;

    int genre_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(SeriesListActivity.this);
        dialog.setMessage("Loading..");
        dialog.show();

        Intent intent = getIntent();
        genre_id = intent.getIntExtra("genre_id", 9999);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        serieList = new ArrayList<>();
        adapter = new SeriesAdapter(this, serieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 2, 10, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startSerieDetailActivity(adapter.getItem(position));
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
        ArrayList genrelist = new ArrayList();
        Genre genreid = new Genre();
        genreid.setGenre(genre_id);
        genrelist.add(genreid);

        Call<List<Serie>> call = apiInterface.GetSeries(genrelist);
        call.enqueue(new Callback<List<Serie>>() {
            @Override
            public void onResponse(Call<List<Serie>> call, retrofit2.Response<List<Serie>> response) {
                serieList.addAll(response.body());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Serie>> call, Throwable t) {

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

    private void startSerieDetailActivity (Serie serie)
    {
        Gson gson = new Gson();
        String objstring = gson.toJson(serie);
        startActivity(new Intent(SeriesListActivity.this, SeriesDetailActivity.class)
                .putExtra("serie",objstring));
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
