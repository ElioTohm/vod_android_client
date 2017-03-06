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
import xms.com.vodmobile.Genre.ClipsGenreActivity;
import xms.com.vodmobile.Genre.MovieGenreActivity;
import xms.com.vodmobile.Genre.SerieGenreActivity;
import xms.com.vodmobile.objects.Type;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private List<Type> typeList;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        url = getResources().getString(R.string.BASE_URL);

        typeList = new ArrayList<>();
        adapter = new HomeAdapter(this, typeList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
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

                if (type.getTitle().equals("Movies")) {
                    StartMoviesGenreActivity();
                }else if (type.getTitle().equals("Series")) {
                    StartSeriesGenreActivity();
                } else {
                    StartClipsGenreActivity();
                }

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
        Type typeMovies = new Type("Movies", url + "videos/VODappimages/movies.png");
        typeList.add(typeMovies);
        Type typeSeries= new Type("Series", url + "videos/VODappimages/series.png");
        typeList.add(typeSeries);
        Type typeClips= new Type("Clips", url + "videos/VODappimages/clips.png");
        typeList.add(typeClips);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void StartMoviesGenreActivity(){
        startActivity(new Intent(MainActivity.this, MovieGenreActivity.class));
    }

    private void StartSeriesGenreActivity(){
        startActivity(new Intent(MainActivity.this, SerieGenreActivity.class));
    }

    private void StartClipsGenreActivity() {
        startActivity(new Intent(MainActivity.this, ClipsGenreActivity.class));
    }
}
