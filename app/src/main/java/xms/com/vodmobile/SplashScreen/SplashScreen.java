package xms.com.vodmobile.SplashScreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xms.com.vodmobile.MainActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiService;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.objects.Client;

public class SplashScreen extends AppCompatActivity {
    private String usermail;
    private String password;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private TextView updatetextview;

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
        updatetextview = (TextView) findViewById(R.id.updatetextview);

        SharedPreferences prefs = getSharedPreferences("UserData", 0);
        usermail = prefs.getString("usermail", "0");
        password = prefs.getString("userpass", "0");
        if (usermail == "0" && password == "0")
        {
            register();
        } else {
            try {
                SendAuthRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void SendAuthRequest () {
        ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);
        Call<Client> call = apiInterface.CheckUserEmail(new Client(usermail));
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, retrofit2.Response<Client> response) {
                Client client = new Client(
                        null,
                        response.body().getRegistered(),
                        response.body().getActive(),
                        response.body().getAppVersion()
                        );
                if (client.getRegistered() == 1) {
                    if (client.getActive() == 1) {
                        if(checkUpdate(client.getAppVersion())) {
                            activated();
                        } else {
                            updatetextview.setVisibility(View.VISIBLE);
                            installAPK();
                        }
                    } else {
                        new AlertDialog.Builder(SplashScreen.this)
                            .setMessage("Your account is not active yet please check with your service provider")
                            .setCancelable(false)
                            .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SplashScreen.this.finish();
                                }
                            })
                            .show();
                    }
                } else {
                    register();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                new AlertDialog.Builder(SplashScreen.this)
                    .setMessage("Please check that you are connected")
                    .setCancelable(false)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SplashScreen.this.finish();
                        }
                    })
                    .show();
            }
        });
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

    private void installAPK () {
        final ApiInterface apiInterface = ApiService.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.DownloadUpdate();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TEST", "server contacted and has file");

                    File apkpdate = new File(SplashScreen.this.getExternalCacheDir().getAbsolutePath() + "/shareeftube.apk");
                    if (apkpdate.exists()) {
                        apkpdate.delete();
                    }

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            writeResponseBodyToDisk(response.body());
                            // start apk as intent to update code
                            try {
                                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                StrictMode.setVmPolicy(builder.build());

                                File apkpdate = new File(SplashScreen.this.getExternalCacheDir().getAbsolutePath() + "/shareeftube.apk");
                                Intent promptInstall = new Intent(Intent.ACTION_VIEW);
                                promptInstall.setDataAndType(Uri.fromFile(apkpdate), "application/vnd.android.package-archive");
                                promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(promptInstall);
                            } catch (Exception e) {
                                new AlertDialog.Builder(SplashScreen.this)
                                        .setMessage("ShareefTube Could not update please download latest version manually")
                                        .setCancelable(false)
                                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                SplashScreen.this.finish();
                                            }
                                        })
                                        .show();
                            }

                            return null;
                        }
                    }.execute();
                }
                else {
                    Log.d("TEST", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                new AlertDialog.Builder(SplashScreen.this)
                        .setMessage("Please check that you are connected")
                        .setCancelable(false)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SplashScreen.this.finish();
                            }
                        })
                        .show();
            }
        });
    }

    private void writeResponseBodyToDisk(ResponseBody body) {
        try {
            File apkpdate = new File(SplashScreen.this.getExternalCacheDir().getAbsolutePath() + "/shareeftube.apk");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(apkpdate);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TEST", "file download: " + (fileSize/fileSizeDownloaded) + "%");
                }

                outputStream.flush();

            } catch (IOException e) {

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {

        }
    }
}
