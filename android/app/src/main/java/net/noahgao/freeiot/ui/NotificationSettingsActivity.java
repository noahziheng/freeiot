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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.api.ApiClient;
import net.noahgao.freeiot.model.UserModel;
import net.noahgao.freeiot.util.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        private static final String KEY_NOTIFICATION_SYSTEM   = "key_notification_system";
        private static final String KEY_NOTIFICATION_NORMAL = "key_notification_normal";
        private static final String KEY_NOTIFICATION_SPECIAL    = "key_notification_special";
        private static final String KEY_NOTIFICATION_WARNING    = "key_notification_warning";
        private UserModel.SettingBean.PushSettingBean t = null;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_notification_settings);
            setHasOptionsMenu(true);

            if (Auth.check()) {
                Call<UserModel> call = ApiClient.API.getUser(Auth.getUser().get_id(), Auth.getToken());
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        t = response.body().getSetting().getPush();
                        ((SwitchPreference) findPreference(KEY_NOTIFICATION_SYSTEM)).setChecked(checkNull(t.getSystem()));
                        ((SwitchPreference) findPreference(KEY_NOTIFICATION_NORMAL)).setChecked(checkNull(t.getNormal()));
                        ((SwitchPreference) findPreference(KEY_NOTIFICATION_SPECIAL)).setChecked(checkNull(t.getSpecial()));
                        ((SwitchPreference) findPreference(KEY_NOTIFICATION_WARNING)).setChecked(checkNull(t.getWarning()));
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(getActivity(), "推送设置抓取失败，请检查网络！",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }
        }

        private Boolean checkNull(Boolean t) {
            if(t == null) return false;
            else return t;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_save, menu);
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(t == null) return false;
                    UserModel tmp = Auth.getUser();
                    tmp.initSetting();
                    t.setSystem(((SwitchPreference) findPreference(KEY_NOTIFICATION_SYSTEM)).isChecked());
                    t.setNormal(((SwitchPreference) findPreference(KEY_NOTIFICATION_NORMAL)).isChecked());
                    t.setSpecial(((SwitchPreference) findPreference(KEY_NOTIFICATION_SPECIAL)).isChecked());
                    t.setWarning(((SwitchPreference) findPreference(KEY_NOTIFICATION_WARNING)).isChecked());
                    tmp.getSetting().setPush(t);
                    Call<Object> call = ApiClient.API.modifyPushSetting(Auth.getUser().get_id(), tmp, Auth.getToken());
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });
            super.onCreateOptionsMenu(menu,inflater);
        }
    }
}
