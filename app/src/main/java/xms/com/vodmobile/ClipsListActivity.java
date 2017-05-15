package xms.com.vodmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xms.com.vodmobile.Adapters.EpisodesAdapter;
import xms.com.vodmobile.Adapters.VideosAdapter;
import xms.com.vodmobile.RequestQueuer.AppController;
import xms.com.vodmobile.Series.SeriesListActivity;
import xms.com.vodmobile.objects.Artist;
import xms.com.vodmobile.objects.Episode;
import xms.com.vodmobile.objects.Serie;
import xms.com.vodmobile.objects.Video;

public class ClipsListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EpisodesAdapter adapter;
    private List<Episode> episodelist;

    private static String tag_json_obj = "clips_request";
    private String url;
    ProgressDialog dialog;
    int genre_id;
    Artist artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clips_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getResources().getString(R.string.BASE_URL)+"getclips";
        dialog = new ProgressDialog(ClipsListActivity.this);
        dialog.setMessage("Loading..");
        dialog.show();
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
        adapter = new EpisodesAdapter(this, episodelist);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new ClipsListActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
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

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() throws JSONException {
        final JSONArray bodyrequest = new JSONArray("[{\"genre\":"+genre_id+", \"artist_id\": "+ artist.getID() +"}]");
        Log.d("test", artist.getID().toString());

        // Tag used to cancel the request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                url, bodyrequest,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Request", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Episode episode= new Episode(obj.getString("Title"), "",
                                        "", obj.getString("stream"),
                                        "","","",
                                        "","", obj.getString("Subtitle")
                                );
                                episodelist.add(episode);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
                Log.d("VolleyError", "Error: " + error.getMessage());
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonArrayRequest , tag_json_obj);

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

    private void startPlayerActivity (Episode episode)
    {
        startActivity(new Intent(ClipsListActivity.this, PlayerActivity.class)
                .putExtra("stream", episode.getStream())
                .putExtra("type", "clips")
                .putExtra("subtitle", episode.getSubtitle()));
    }

}
