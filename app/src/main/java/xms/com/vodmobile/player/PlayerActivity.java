package xms.com.vodmobile.player;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

import okhttp3.OkHttpClient;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiService;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayerActivity extends AppCompatActivity implements  ExoPlayer.EventListener,
        PlaybackControlView.VisibilityListener {
    private static final boolean shouldAutoPlay = true;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private SimpleExoPlayerView simpleExoPlayerView;
    private Uri mp4VideoUri;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private TrackSelectionHelper trackSelectionHelper;
    private Uri subtitleUri = null;
    boolean doubleBackToExitPressedOnce;
    private ProgressDialog pDialog;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 1000;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pDialog = new ProgressDialog(PlayerActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        mVisible = true;

        mContentView = findViewById(R.id.content_exo_main);

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        simpleExoPlayerView = (SimpleExoPlayerView)findViewById(R.id.SimpleExoPlayerView);
        Intent intent = getIntent();
        mp4VideoUri = Uri.parse(ApiService.BASE_URL
                            + "/api/stream/"+ intent.getStringExtra("type")
                            + "/" + intent.getStringExtra("id"));
        Log.d("URI", String.valueOf(mp4VideoUri));
        if (intent.getStringExtra("subtitle") != null && !intent.getStringExtra("subtitle").isEmpty()) {
            subtitleUri = Uri.parse(ApiService.BASE_URL + "/api/stream/subtitles/" + intent.getStringExtra("subtitle").replace(" ", "%20"));
        }

        Log.d("Subtitle", String.valueOf(subtitleUri));
        createDefaultTrackSelector();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void createDefaultTrackSelector () {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        OkHttpDataSourceFactory okHttpDataSourceFactory = new OkHttpDataSourceFactory(new OkHttpClient(),
                Util.getUserAgent(this, "XmsTube"), bandwidthMeter);
        okHttpDataSourceFactory.setDefaultRequestProperty("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjZiYTJiZDcyZmQ1MTYwN2ViOTljNThkNzRkNmY0MTNkNzRkMzZhNzYxMWM5NzBlYTVmMmNiNmU3YzYzODAyZTQ0NmEwMTMyNzgwOTkzNGU5In0.eyJhdWQiOiIxIiwianRpIjoiNmJhMmJkNzJmZDUxNjA3ZWI5OWM1OGQ3NGQ2ZjQxM2Q3NGQzNmE3NjExYzk3MGVhNWYyY2I2ZTdjNjM4MDJlNDQ2YTAxMzI3ODA5OTM0ZTkiLCJpYXQiOjE0OTkyNTE0OTcsIm5iZiI6MTQ5OTI1MTQ5NywiZXhwIjoxNTAwNTQ3NDk3LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.QGA6bdztCKSvNFBnsrGB59Vp1d0JSmDwDEOnGW3sYTRc2OwOan_F5odvJzyF9UHKxx_rKldeXW79lZxC3M5e4juxRBlaZ3A27B6Ih9ynyNAz3lMzoRkH6dhK3-9TOfq-k0Syf1A3NIIh-tmIuWixwni9NWppbRzYqzxAWhAMkj1qei-R48QPocf8YyhzwXTMDK4IA4Vh99DixdOQDBE4mZpudOLYOIkPutogTJ7TJqr3r65iVxzlj-_SMYFnWfV6Tv5xYxLm8FGPogl8BJugzpvT9ftOTaf_k2_9BuWWcCB5XMjIHrIdiQ6GvDqHYqqjQ2QEWExPxGQINBJo8I02h-K0cJ6YXCCB4EGJA6f7gBWKeb59X3fQiVf3hV0tQs-lv2sCgSAyfcOlwbfLzp33EJAgEjd4fkJLBnUffddezX9tfJuf4-nWvuHvMmh4eQVh-C2j0xqtIyssiaBtp-bcQiuVcXZIvs8CJWmiqmQpWxCqS2sHVoCuyVsPtiqnBKizA4HMzFEiYIFdoxsoUzfSEPWRIECybDR8mQWo0VARjn-uTbPCcApQqHX3BaRKsYWZ4wHT5gdIhhQx6Qrq9qHmhJVI6LQQwrC-feMKBNSdC-cIKi7cmfrsOpF_5nlYshQnwmfm1_uyjVFq2Z-4x8TmW4z-rYsWbT9WBobe5SR4sBw");

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON;
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
                null, extensionRendererMode);

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        trackSelectionHelper = new TrackSelectionHelper(trackSelector, adaptiveTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
        player.addListener(this);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
        MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, okHttpDataSourceFactory, extractorsFactory, null, null);

        if (subtitleUri != null) {
            Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP,
                    null, Format.NO_VALUE, Format.NO_VALUE, "en", null);
            MediaSource textMediaSource = new SingleSampleMediaSource(subtitleUri, okHttpDataSourceFactory,
                    textFormat, C.TIME_UNSET);
            MediaSource mediaSourceWithText = new MergingMediaSource(videoSource, textMediaSource);
            player.prepare(mediaSourceWithText);
        } else {
            player.prepare(videoSource);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            player.release();
            player = null;
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit video", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        hide();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            finish();
        }
        if (playbackState == ExoPlayer.STATE_READY) {
            pDialog.dismiss();
            hide();
        }
        if (playbackState == ExoPlayer.STATE_BUFFERING) {
            pDialog.show();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onVisibilityChange(int visibility) {

    }
}
