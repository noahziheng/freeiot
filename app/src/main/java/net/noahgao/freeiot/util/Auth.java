package net.noahgao.freeiot.util;

import android.content.SharedPreferences;
import android.util.ArrayMap;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.noahgao.freeiot.model.UserModel;

/**
 * Created by Noah Gao on 17-2-6.
 * By Android Studio
 */

public class Auth {

    private static String json;
    private static UserModel mUser;
    private static boolean sIsInitialized = false;
    private static SharedPreferences ssharedPref;

    public static synchronized void initialize(SharedPreferences sharedPref) {
        if (sIsInitialized) {
            return;
        }
        sIsInitialized = true;
        ssharedPref = sharedPref;
    }

    public static boolean check() {
        Log.i("AUTH","CHECK");
        if(mUser == null) mUser = new UserModel();
        if (mUser.loginF) return true;
        else {
            if(ssharedPref != null) {
                String authstr = ssharedPref.getString("AUTH",null);
                if(authstr != null) {
                    ArrayMap authobj = JSON.parseObject(authstr,new TypeReference<ArrayMap>() {});
                    mUser.initUser(authobj);
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean login(String email,String password) {
        ArrayMap<String, String> data = new ArrayMap<String, String>();
        data.put("email",email);
        mUser.initUser(data);
        if(ssharedPref != null) {
            SharedPreferences.Editor editor = ssharedPref.edit();
            editor.putString("AUTH", JSON.toJSONString(data));
            editor.apply();
        }
        return true;
    }

    public static void logout() {
        if(ssharedPref != null) {
            SharedPreferences.Editor editor = ssharedPref.edit();
            editor.putString("AUTH", null);
            editor.apply();
        }
        mUser = null;
    }

    static public UserModel getUser() {
        return mUser;
    }

}
