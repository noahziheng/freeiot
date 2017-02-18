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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.view.MenuItem;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.util.Auth;
import net.noahgao.freeiot.util.UpdateManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                finish();
                break;
        }
        return true;
    }

    public static class SettingsFragment extends PreferenceFragment {

        private static final String KEY_NOTIFICATION_SETTINGS = "key_notification_settings";
        private static final String KEY_ACCOUNT = "key_account";
        private static final String KEY_FEEDBACK = "key_feedback";
        private static final String KEY_UPDATE = "key_update";
        private static final String KEY_ABOUT = "key_about";
        private static final String KEY_LOGOUT = "key_logout";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_settings);
            try {
                PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                findPreference(KEY_UPDATE).setSummary(packageInfo.versionName + "." + packageInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (!Auth.check()) {
                findPreference(KEY_NOTIFICATION_SETTINGS).setEnabled(false);
                findPreference(KEY_ACCOUNT).setEnabled(false);
                findPreference(KEY_LOGOUT).setVisible(false);
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            Intent intent = null;
            switch (preference.getKey()) {
                case KEY_NOTIFICATION_SETTINGS:
                    startActivity(new Intent(getActivity(), NotificationSettingsActivity.class));
                    break;
                case KEY_ACCOUNT:
                    startActivity(new Intent(getActivity(), AccountActivity.class));
                    break;
                case KEY_FEEDBACK:
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.setType("message/rfc822");
                    email.putExtra(Intent.EXTRA_EMAIL, new String[] {"noahgaocn@outlook.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "FreeIOT Android端反馈");
                    PackageInfo packageInfo;
                    try {
                        packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                        email.putExtra(Intent.EXTRA_TEXT,
                                "应用版本: " + packageInfo.versionName + "." + packageInfo.versionCode +
                                "\n机型信息: " + android.os.Build.MODEL + ",Android" + android.os.Build.VERSION.RELEASE + "\n");
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    startActivity(Intent.createChooser(email, "请选择您的邮件客户端"));
                    break;
                case KEY_UPDATE:
                    UpdateManager.doUpdate(getActivity());
                    break;
                case KEY_ABOUT:
                    //intent = new Intent(getActivity(), AboutActivity.class);
                    break;
                case KEY_LOGOUT:
                    this.logout();
                    return false;
                default:
                    return false;
            }
            return true;
        }

        private void logout() {
            Auth.logout();
            Intent intentLogin= new Intent(getActivity(),LoginActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);//LoginActivity不添加到后退栈
            startActivity(intentLogin);
            getActivity().finish();
        }
    }

}
