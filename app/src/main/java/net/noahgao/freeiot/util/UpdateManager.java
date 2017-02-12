package net.noahgao.freeiot.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

/**
 * Created by Noah Gao on 17-2-12.
 * By Android Studio
 */

public class UpdateManager {
    static public void doUpdate(final Activity context) {
        doUpdate(context,true);
    }

    static public void doUpdate(final Activity context, final boolean noupdateToast) {
        PgyUpdateManager.unregister();
        PgyUpdateManager.register(context,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(context)
                                .setTitle("有更新！")
                                .setMessage("版本号:" + appBean.getVersionName() + "." + appBean.getVersionCode() + "\n日志:" + appBean.getReleaseNote())
                                .setPositiveButton(
                                        "确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startDownloadTask(context,appBean.getDownloadURL());
                                            }
                                        })
                                .setNegativeButton(
                                        "取消", null).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        if(noupdateToast) Toast.makeText(context,"当前版本已是最新！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
