package net.noahgao.freeiot;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.util.Auth;

import java.net.URISyntaxException;

import im.fir.sdk.FIR;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

/**
 * Created by Noah Gao on 17-2-6.
 * By Android Studio
 */

public class MainApplication extends Application {

    public static String versionName;
    public static int versionCode;
    private Socket mSocket;

    {
        try {
            /*IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            mSocket = IO.socket("https://api.iot.noahgao.net",opts);*/
            mSocket = IO.socket("https://api.iot.noahgao.net");
        } catch (URISyntaxException e) {
            Toast.makeText(MainApplication.this,"实时数据服务初始化失败\n" + e.getMessage(),Toast.LENGTH_LONG).show();
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

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
