package xms.com.vodmobile.objects;

import com.google.gson.annotations.SerializedName;

/*
* User Class to get user info and app version
* */
public class Client {

    @SerializedName("usermail")
    private String usermail;

    @SerializedName("Token")
    private String TOKEN;

    @SerializedName("Refreshtoken")
    private String REFRESHTOKEN;

    @SerializedName("TokenTTL")
    private String TOKENTTL;

    @SerializedName("registered")
    private int registered;

    @SerializedName("active")
    private int active;

    @SerializedName("appversion")
    private String appversion;

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public  Client (String email) {
        this.usermail = email;
    }

    public Client(String Email, int registered, int active, String appversion) {
        this.usermail = Email;
        this.registered = registered;
        this.active = active;
        this.appversion = appversion;
    }

    public int getRegistered() {
        return registered;
    }

    public String getAppVersion() {
        return appversion;
    }

    public int getActive() {
        return active;
    }

}

