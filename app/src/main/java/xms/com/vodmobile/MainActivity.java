package xms.com.vodmobile;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import xms.com.vodmobile.Adapters.HomeAdapter;
import xms.com.vodmobile.Adapters.RecyclerTouchListener;
import xms.com.vodmobile.Clips.ArtistsList;
import xms.com.vodmobile.Genre.GenreActivity;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Type;
import xms.com.vodmobile.ui.GridSpacingItemDecoration;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<Type> typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        typeList = new ArrayList<>();
        adapter = new HomeAdapter(this, typeList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 1, 10, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        try {
            prepareAlbums();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Type type = typeList.get(position);
                StartIntent(type.getTitle());
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() throws JSONException {
        Type typeMovies = new Type("Movies", ApiService.BASE_URL + "videos/appimages/movies.png");
        typeList.add(typeMovies);
        Type typeSeries= new Type("Series", ApiService.BASE_URL + "videos/appimages/series.png");
        typeList.add(typeSeries);
        Type typeClips= new Type("Clips", ApiService.BASE_URL + "videos/appimages/clips.png");
        typeList.add(typeClips);
    }

    private void StartIntent (String type) {
        if (type.equals("Clips")) {
            startActivity(new Intent(MainActivity.this, ArtistsList.class));
        } else {
            startActivity(new Intent(MainActivity.this, GenreActivity.class).putExtra("genre_type", type));
        }

    }
}
