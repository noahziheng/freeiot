package net.noahgao.freeiot.util;

import android.text.SpannableString;

import net.noahgao.freeiot.Const;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Noah Gao on 17-2-11.
 * By Android Studio
 */

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
        return new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                .badgeColor(Const.BADGE_COLOR[tag])
                .text1(Const.STATUS_STR[tag])
                .textSize(35)
                .build().toSpannable();
    }
}
