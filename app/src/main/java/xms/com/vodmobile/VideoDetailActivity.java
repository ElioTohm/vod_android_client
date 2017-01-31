package xms.com.vodmobile;

import android.content.Intent;
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
import com.google.gson.Gson;

import xms.com.vodmobile.objects.Video;

public class VideoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        Gson gson = new Gson();
        final Video video = gson.fromJson(intent.getStringExtra("video"), Video.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VideoDetailActivity.this, PlayerActivity.class)
                        .putExtra("stream", video.getStream()));
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

        plot.setText(video.getPlot());
        runtime.setText(video.getRuntime());
        releasedate.setText(video.getReleased());
        actors.setText(video.getActors());

        try {
            Glide.with(this).load(video.getThumbnail()).into((ImageView) findViewById(R.id.backdrop));
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
}
