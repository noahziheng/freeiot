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
                .setNegativeButton(negative, null)
                .create();
    }
}
