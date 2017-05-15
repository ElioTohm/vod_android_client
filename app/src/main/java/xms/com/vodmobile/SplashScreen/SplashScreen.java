package xms.com.vodmobile.SplashScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import xms.com.vodmobile.MainActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.RequestQueuer.AppController;

import static android.os.Environment.getExternalStorageState;

public class SplashScreen extends AppCompatActivity {
    private String usermail;
    private String password;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private static String tag_json_obj = "authentication_request";
    private String url;
    private URL updateurl;
    private InputStream is;
    private File DownloadDir;
    ProgressDialog dialog;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.activity_splash_screen);
        mContentView = findViewById(R.id.fullscreen_content);
        dialog = new ProgressDialog(SplashScreen.this);

        url = getResources().getString(R.string.BASE_URL)+"/clientsingin";
        SharedPreferences prefs = getSharedPreferences("UserData", 0);
        usermail = prefs.getString("usermail", "0");
        password = prefs.getString("userpass", "0");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
            }
        }, SPLASH_TIME_OUT);

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
                                        if(checkUpdate(response.getString("appversion"))) {
                                            activated();
                                        } else {
                                            checkPermissions();
                                        }
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
                            } else {
                                register();
                            }

                        } catch (JSONException e) {

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VolleyError", "Error: " + error.getMessage());
                Log.d("VolleyError", "Error: " + error.getMessage());
                new AlertDialog.Builder(SplashScreen.this)
                        .setMessage("Please check that you are on Shareef's network and try again")
                        .setCancelable(false)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SplashScreen.this.finish();
                            }
                        })
                        .show();
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

    private boolean checkUpdate (String version) {
        try {
            PackageInfo appInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appversion = appInfo.versionName;
            if (!version.equals(appversion)) {

                Log.d("version",appversion);
                return  false;
            }else {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void installAPK() {
        dialog.setMessage("Updating, Please wait...");
        dialog.show();
        try {
            DownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/shareeftube.apk");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            updateurl = new URL( getResources().getString(R.string.BASE_URL) + "/apk//shareeftube.apk");
            HttpURLConnection c = (HttpURLConnection) updateurl.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            String PATH = Environment.getExternalStorageDirectory() + "/Download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "shareeftube.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
            promptInstall.setDataAndType(Uri.fromFile(DownloadDir), "application/vnd.android.package-archive");
            promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            dialog.dismiss();
            startActivity(promptInstall);

        } catch (IOException e) {
            ProgressDialog p = new ProgressDialog(SplashScreen.this);
            p.setMessage("Update error!" + e.toString() + getExternalStorageState());
            p.dismiss();
        }
    }
    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        installAPK();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length < 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(SplashScreen.this)
                            .setMessage("ShareefTube cannot update if permission is not granted")
                            .show();
                } else {
                    installAPK();
                }
                return;
            }
        }
    }
}
