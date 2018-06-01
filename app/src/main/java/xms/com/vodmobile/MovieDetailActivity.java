package xms.com.vodmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.objects.Video;

public class MovieDetailActivity extends AppCompatActivity {
    View mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContentView = findViewById(R.id.content_main);
        hideNav();

        Intent intent = getIntent();
        Gson gson = new Gson();
        final Video video = gson.fromJson(intent.getStringExtra("video"), Video.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MovieDetailActivity.this, PlayerActivity.class)
                        .putExtra("subtitle", video.getSubtitle())
                        .setData(Uri.parse(ApiService.BASE_URL + "videos/movies/" + video.getStream()))
                        .setAction(PlayerActivity.ACTION_VIEW)
                );
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(video.getTitle());

        TextView plot = (TextView)findViewById(R.id.plot);
        TextView runtime = (TextView)findViewById(R.id.Runtime);
        TextView releasedate = (TextView)findViewById(R.id.releaseDate);
        TextView actors = (TextView)findViewById(R.id.actors);


        plot.setText(video.getPlot() == null ? "N/A" : video.getPlot());
        runtime.setText(video.getRuntime() == null ? "N/A" : video.getRuntime());
        releasedate.setText(video.getReleased() == null  ? "N/A" : video.getReleased());
        actors.setText(video.getActors() == null  ? "N/A" : video.getActors());

        try {
            RequestOptions requestOptions  = new RequestOptions();
            requestOptions.optionalFitCenter();

            Glide.with(this)
                    .load(video.getThumbnail())
                    .apply(requestOptions)
                    .into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
