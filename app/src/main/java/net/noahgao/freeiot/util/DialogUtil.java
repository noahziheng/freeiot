package net.noahgao.freeiot.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Noah Gao on 17-2-17.
 * By Android Studio
 */

public class DialogUtil {

    public static void showSingleDialog(Context context, String title, String msg, String positive,
                                        DialogInterface.OnClickListener listener) {
        createSingleDialog(context, title, msg, positive, listener).show();
    }

    public static void showDoubleDialog(Context context, String title, String msg, String positive,
                                        String negative, DialogInterface.OnClickListener listener) {

        createDoubleDialog(context, title, msg, positive, negative, listener).show();
    }

    public static AlertDialog createSingleDialog(Context context, String title, String msg, String positive,
                                                 DialogInterface.OnClickListener listener) {

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positive, listener)
                .create();
    }

    public static AlertDialog createDoubleDialog(Context context, String title, String msg, String positive,
                                                 String negative, DialogInterface.OnClickListener listener) {

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(positive, listener)
                .setNegativeButton(negative, listener)
                .create();
    }
}
