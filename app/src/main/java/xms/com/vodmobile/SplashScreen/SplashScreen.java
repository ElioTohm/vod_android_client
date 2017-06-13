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


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import xms.com.vodmobile.MainActivity;
import xms.com.vodmobile.R;
import xms.com.vodmobile.network.ApiClient;
import xms.com.vodmobile.network.ApiInterface;
import xms.com.vodmobile.objects.Client;

import static android.os.Environment.getExternalStorageState;

public class SplashScreen extends AppCompatActivity {
    private String usermail;
    private String password;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
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
                    } catch (Exception e) {
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

    private void SendAuthRequest () {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
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
                            checkPermissions();
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
                    .setMessage("Please check that you are on Shareef's network and try again")
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

    private void installAPK() {
        dialog.setMessage("Updating, Please wait...");
        dialog.show();
        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/shareeftube.apk");
            if(downloadDir.exists()) {
                downloadDir.delete();
            }
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL updateurl = new URL(getResources().getString(R.string.BASE_URL) + "/apk//shareeftube.apk");
            HttpURLConnection c = (HttpURLConnection) updateurl.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            String PATH = Environment.getExternalStorageDirectory() + "/Download/";
            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, "shareeftube.apk");
            FileOutputStream fos = new FileOutputStream(outputFile);

            InputStream is = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.close();
            is.close();

            Intent promptInstall = new Intent(Intent.ACTION_VIEW);
            promptInstall.setDataAndType(Uri.fromFile(downloadDir), "application/vnd.android.package-archive");
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
