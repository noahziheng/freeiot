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
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.view.MenuItem;

import net.noahgao.freeiot.R;
import net.noahgao.freeiot.util.Auth;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        setContentView(R.layout.activity_account);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(AccountActivity.this,SettingsActivity.class));
                finish();
                break;
        }
        return true;
    }


    public static class AccountFragment extends PreferenceFragment {

        private static final String KEY_EMAIL = "key_email";
        private static final String KEY_MODIFY_PASSWORD = "key_modify_password";

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.pref_account);

            if (Auth.check()) {
                findPreference(KEY_EMAIL).setSummary(Auth.getUser().getEmail());
            } else {
                findPreference(KEY_EMAIL).setSummary("");
            }
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if (KEY_MODIFY_PASSWORD.equals(preference.getKey())) {
                Intent intent = new Intent(getActivity(), ModifyPasswordActivity.class);
                startActivity(intent);
            }
            return true;
        }
    }
}
