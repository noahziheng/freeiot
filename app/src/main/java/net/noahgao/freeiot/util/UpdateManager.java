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

package net.noahgao.freeiot.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bugtags.library.Bugtags;

import net.noahgao.freeiot.BuildConfig;
import net.noahgao.freeiot.MainApplication;
import net.noahgao.freeiot.service.UpdateService;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateManager {

    static public void doUpdate(Activity context) {
        doUpdate(context, true);
    }

    static public void doUpdate(final Activity context, final boolean noupdateToast) {
        final Request request = new Request.Builder().url("http://api.fir.im/apps/latest/58a00c09ca87a8462900008a?api_token=" + BuildConfig.FIR_API_TOKEN).build();
        OkHttpClient yOkHttpClient = new OkHttpClient();
        yOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context,"更新获取失败！", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Bugtags.sendException(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Toast.makeText(context,"更新获取失败！", Toast.LENGTH_SHORT).show();
                    throw new IOException("" + response);
                }

                final JSONObject version = JSON.parseObject(response.body().string());
                if (Float.parseFloat(MainApplication.versionName) < Float.parseFloat((String) version.get("versionShort")) || (Float.parseFloat(MainApplication.versionName) == Float.parseFloat(version.getString("versionShort")) && MainApplication.versionCode < Integer.parseInt(version.getString("build")))) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(context)
                                    .setTitle("有更新！")
                                    .setMessage("版本号:" + version.getString("versionShort") + "." + version.getString("build") + "\n日志:" + version.getString("changelog"))
                                    .setPositiveButton(
                                            "确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                        Toast.makeText(context,"更新权限不足！",Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    Intent updataService = new Intent(context,  UpdateService.class);
                                                    updataService.putExtra("downloadurl",version.getString("installUrl"));
                                                    context.startService(updataService);
                                                }
                                            })
                                    .setNegativeButton(
                                            "取消", null).show();
                        }
                    });

                } else {
                    if(noupdateToast) Toast.makeText(context,"当前版本已是最新！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
