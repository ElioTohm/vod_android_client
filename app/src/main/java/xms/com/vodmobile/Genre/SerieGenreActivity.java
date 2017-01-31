package xms.com.vodmobile.Genre;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xms.com.vodmobile.Adapters.GenresAdapter;
import xms.com.vodmobile.DividerItemDecoration;
import xms.com.vodmobile.R;
import xms.com.vodmobile.RecyclerTouchListener;
import xms.com.vodmobile.RequestQueuer.AppController;
import xms.com.vodmobile.Series.SeriesListActivity;
import xms.com.vodmobile.objects.Genre;

public class SerieGenreActivity extends AppCompatActivity {
    private List<Genre> genreList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GenresAdapter mAdapter;

    private static String tag_json_obj = "genre_request";
    private static String url = "http://192.168.33.235/getgenres";//"http://192.168.88.237/getgenres";//
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_genre);

        mAdapter = new GenresAdapter(genreList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        try {
            prepareGenreData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Genre genre = genreList.get(position);
                gotoSeriesList(genre);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareGenreData() throws JSONException {
        Genre genre = new Genre("All", 9999);
        genreList.add(genre);

        final JSONArray bodyrequest = new JSONArray("[{\"Type\":\"Series\"}]");

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
                                Genre genre = new Genre(obj.getString("genre_name"), obj.getInt("genre_id"));
                                genreList.add(genre);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();

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

    private void gotoSeriesList (Genre genre)
    {
        Intent intent = new Intent(this, SeriesListActivity.class);
        intent.putExtra("genre_id", genre.getID());
        startActivity(intent);
    }

}