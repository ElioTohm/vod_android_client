package xms.com.vodmobile.SplashScreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xms.com.vodmobile.LoginActivity;
import xms.com.vodmobile.MainActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.RequestQueuer.AppController;

public class SplashScreen extends AppCompatActivity {
    private String usermail;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private static String tag_json_obj = "authentication_request";
    private static String url = "http://192.168.33.235/clientsingin";//"http://192.168.88.237/clientsingin";
    // Splash screen timer
//    private static int SPLASH_TIME_OUT = 1500;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.activity_splash_screen);

        mContentView = findViewById(R.id.fullscreen_content);

        SharedPreferences prefs = getSharedPreferences("UserData", 0);
        usermail = prefs.getString("usermail", "0");
        String password = prefs.getString("userpass", "0");

        if (usermail == "0" && password == "0")
        {
            register();

        } else {
            try {
                SendAuthRequest();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        hide();

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    public void SendAuthRequest() throws JSONException {
        // Tag used to cancel the request
        final JSONObject bodyrequest = new JSONObject("{\"usermail\":\""+usermail+"\"}");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, bodyrequest,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Request", response.toString());

                        // Parsing json check if response is empty
                        try {
                            //if response is not empty check if the following is active
                            int IsActive = response.getInt("active");
                            int IsRegister = response.getInt("registered");
                            if (IsRegister == 1) {
                                switch (IsActive) {
                                    case 1:
                                        activated();
                                        break;
                                    case 0:
                                        new AlertDialog.Builder(SplashScreen.this)
                                                .setMessage("Your account is not active yet please check with your service provider")
                                                .setCancelable(false)
                                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        SplashScreen.this.finish();
                                                    }
                                                })
                                                .show();
                                        break;
                                }
                            }

                        } catch (JSONException e) {
//                                    e.printStackTrace();
                        }

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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void register ()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }



    private void activated ()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SplashScreen Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
