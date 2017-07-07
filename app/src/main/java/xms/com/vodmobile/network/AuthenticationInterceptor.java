package xms.com.vodmobile.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header("Accept","application/json")
                .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjZiYTJiZDcyZmQ1MTYwN2ViOTljNThkNzRkNmY0MTNkNzRkMzZhNzYxMWM5NzBlYTVmMmNiNmU3YzYzODAyZTQ0NmEwMTMyNzgwOTkzNGU5In0.eyJhdWQiOiIxIiwianRpIjoiNmJhMmJkNzJmZDUxNjA3ZWI5OWM1OGQ3NGQ2ZjQxM2Q3NGQzNmE3NjExYzk3MGVhNWYyY2I2ZTdjNjM4MDJlNDQ2YTAxMzI3ODA5OTM0ZTkiLCJpYXQiOjE0OTkyNTE0OTcsIm5iZiI6MTQ5OTI1MTQ5NywiZXhwIjoxNTAwNTQ3NDk3LCJzdWIiOiIxIiwic2NvcGVzIjpbIioiXX0.QGA6bdztCKSvNFBnsrGB59Vp1d0JSmDwDEOnGW3sYTRc2OwOan_F5odvJzyF9UHKxx_rKldeXW79lZxC3M5e4juxRBlaZ3A27B6Ih9ynyNAz3lMzoRkH6dhK3-9TOfq-k0Syf1A3NIIh-tmIuWixwni9NWppbRzYqzxAWhAMkj1qei-R48QPocf8YyhzwXTMDK4IA4Vh99DixdOQDBE4mZpudOLYOIkPutogTJ7TJqr3r65iVxzlj-_SMYFnWfV6Tv5xYxLm8FGPogl8BJugzpvT9ftOTaf_k2_9BuWWcCB5XMjIHrIdiQ6GvDqHYqqjQ2QEWExPxGQINBJo8I02h-K0cJ6YXCCB4EGJA6f7gBWKeb59X3fQiVf3hV0tQs-lv2sCgSAyfcOlwbfLzp33EJAgEjd4fkJLBnUffddezX9tfJuf4-nWvuHvMmh4eQVh-C2j0xqtIyssiaBtp-bcQiuVcXZIvs8CJWmiqmQpWxCqS2sHVoCuyVsPtiqnBKizA4HMzFEiYIFdoxsoUzfSEPWRIECybDR8mQWo0VARjn-uTbPCcApQqHX3BaRKsYWZ4wHT5gdIhhQx6Qrq9qHmhJVI6LQQwrC-feMKBNSdC-cIKi7cmfrsOpF_5nlYshQnwmfm1_uyjVFq2Z-4x8TmW4z-rYsWbT9WBobe5SR4sBw");

        Request request = builder.build();
        return chain.proceed(request);
    }
}
