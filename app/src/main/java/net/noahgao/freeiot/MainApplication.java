/*
 * Copyright (c) 2017. Noah Gao <noahgaocn@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.noahgao.freeiot;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.bugtags.library.Bugtags;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.util.Auth;

import java.net.URISyntaxException;

import cn.jpush.android.api.JPushInterface;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

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

        Bugtags.start(BuildConfig.BUGTAGS_APP_KEY, this, Bugtags.BTGInvocationEventShake);


        ApiClient.initialize();
        Auth.initialize(getSharedPreferences("FREEIOT",MODE_PRIVATE));
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);

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
