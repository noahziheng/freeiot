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

import android.text.SpannableString;

import net.noahgao.freeiot.Const;

import cn.nekocode.badge.BadgeDrawable;

public class Badge {
    static public SpannableString buildRole(int tag) {
        return new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                        .badgeColor(Const.BADGE_COLOR[tag])
                        .text1(Const.ROLE_STR[tag])
                        .textSize(45)
                        .build().toSpannable();
    }

    static public SpannableString buildStatus(int tag) {
        return buildStatus(tag,35);
    }

    static public SpannableString buildStatus(int tag, int fontsize) {
        return new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                .badgeColor(Const.BADGE_COLOR[tag])
                .text1(Const.STATUS_STR[tag])
                .textSize(fontsize)
                .build().toSpannable();
    }

    static public SpannableString buildMsgType(int tag) {
        return new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                .badgeColor(Const.BADGE_COLOR[tag])
                .text1(Const.MSG_TYPE_STR[tag])
                .textSize(40)
                .build().toSpannable();
    }
}
