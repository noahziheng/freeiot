package net.noahgao.freeiot.model;

import android.util.ArrayMap;

/**
 * Created by Noah Gao on 17-2-7.
 * By Android Studio
 */

public class UserModel extends Model {

    public boolean loginF = false;

    public String email;

    public void initUser(ArrayMap data) {
        email = (String) data.get("email");
        loginF = true;
    }

}
