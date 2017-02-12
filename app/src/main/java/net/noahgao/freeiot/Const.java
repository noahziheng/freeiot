package net.noahgao.freeiot;

/**
 * Created by Noah Gao on 17-2-8.
 * By Android Studio
 */

public class Const {
    public static boolean DEBUG = true;
    public static String API_URL = "https://api.iot.noahgao.net";
    public static String VERSION_TAG = "Preview";
    public static String VERSION_NUM = "1.0";
    public static String[] STATUS_STR = {
            "预置",
            "等待入网",
            "离线",
            "在线"
    };
    public static String[] ROLE_STR = {
            "标准用户",
            "开发者（等待审核）",
            "开发者",
            "管理员"
    };
    public static int[] BADGE_COLOR = {
            0xff555555,0xffFC2C7B,0xffFF4500,0xff006400
    };
}
