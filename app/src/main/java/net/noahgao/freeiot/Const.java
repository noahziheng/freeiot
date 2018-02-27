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

package net.noahgao.freeiot;

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
    public static String[] MSG_TYPE_STR = {
            "上传",
            "下控",
            "反馈",
            "系统"
    };
    public static int[] BADGE_COLOR = {
            0xff555555,0xffFC2C7B,0xffFF4500,0xff006400
    };
}
