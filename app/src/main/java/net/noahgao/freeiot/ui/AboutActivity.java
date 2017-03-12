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

package net.noahgao.freeiot.ui;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import net.noahgao.freeiot.R;

public class AboutActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setTitle("关于");
        }
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            ((TextView) findViewById(R.id.tv_version)).setText(packageInfo.versionName + "." + packageInfo.versionCode + " Beta");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.tv_copyright)).setText("Copyright (c) 2017.\n"+
                "Noah Gao,TUST\n"+
                "<noahgaocn@gmail.com>\n");
        ((TextView) findViewById(R.id.tv_thanks)).setText("感谢在本项目中广泛应用的开源项目的作者们\n\n"+
                "FreeIOT核心项目Github: \nhttps://github.com/noahziheng/freeiot\n\n" +
                "FreeIOT Android on Github:\n https://github.com/noahziheng/freeiot-android\n\n"+
                "FreeIOT 官方网站: https://iot.noahgao.net\n\n"+
                "作者博客: https://noahgao.net");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            AboutActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
