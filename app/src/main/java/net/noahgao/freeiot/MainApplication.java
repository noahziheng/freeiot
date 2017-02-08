package net.noahgao.freeiot;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.noahgao.freeiot.util.Auth;

import okhttp3.OkHttpClient;

/**
 * Created by Noah Gao on 17-2-6.
 * By Android Studio
 */

public class MainApplication extends Application {

    public Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // Stetho Debug Init
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        mContext = getApplicationContext();
        Auth.initialize(getSharedPreferences("FREEIOT",MODE_PRIVATE));
    }
}
