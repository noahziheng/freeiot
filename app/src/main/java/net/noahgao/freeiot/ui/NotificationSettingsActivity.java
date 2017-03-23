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

import android.support.v14.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.util.Auth;

public class NotificationSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        setContentView(R.layout.activity_notification_settings);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(NotificationSettingsActivity.this,SettingsActivity.class));
                finish();
                break;
        }
        return true;
    }

    @SuppressWarnings({"UnusedParameters","unused"})
    public static class NotificationSettingsFragment extends PreferenceFragment {

        private static final String KEY_NOTIFICATION_FOLLOWED   = "key_notification_followed";
        private static final String KEY_NOTIFICATION_SUBSCRIBED = "key_notification_subscribed";
        private static final String KEY_NOTIFICATION_UPVOTED    = "key_notification_upvoted";
        private static final String KEY_NOTIFICATION_COMMENT    = "key_notification_comment";
        private static final String KEY_NOTIFICATION_MENTION    = "key_notification_mention";


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_notification_settings);
            setHasOptionsMenu(true);

            if (Auth.check()) {
                //TODO: 读取原有通知设置
                Log.i("TAG","READ PREF");
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_save, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.action_save) {
                boolean notificationFollowed = ((SwitchPreference) findPreference(KEY_NOTIFICATION_FOLLOWED)).isChecked();
                boolean notificationSubscribed = ((SwitchPreference) findPreference(KEY_NOTIFICATION_SUBSCRIBED)).isChecked();
                boolean notificationUpvoted = ((SwitchPreference) findPreference(KEY_NOTIFICATION_UPVOTED)).isChecked();
                boolean notificationComment = ((SwitchPreference) findPreference(KEY_NOTIFICATION_COMMENT)).isChecked();
                boolean notificationMention = ((SwitchPreference) findPreference(KEY_NOTIFICATION_MENTION)).isChecked();
                //TODO: 提交用户通知设置到服务器
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private boolean isCheckedByKey(String key) {
            /*key = key.replace("key_", "");
            if (mUserConfigs == null) {
                return false;
            }
            for (UserConfig userConfig : mUserConfigs) {
                if (key.equals(userConfig.getKey())) {
                    return "true".equals(userConfig.getValue());
                }
            }*/
            //TODO: 根据设置数组选中对应选项
            return true;
        }
    }
}
