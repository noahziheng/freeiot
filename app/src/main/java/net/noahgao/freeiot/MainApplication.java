package net.noahgao.freeiot;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.util.Auth;

import im.fir.sdk.FIR;
import okhttp3.OkHttpClient;

/**
 * Created by Noah Gao on 17-2-6.
 * By Android Studio
 */

public class MainApplication extends Application {

    public static String versionName;
    public static int versionCode;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Stetho Debug Init
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        FIR.init(this);
        FIR.sendCrashManually(new Exception("Non-fatal"));


        ApiClient.initialize();
        Auth.initialize(getSharedPreferences("FREEIOT",MODE_PRIVATE));

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
            versionName = packInfo.versionName;
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
