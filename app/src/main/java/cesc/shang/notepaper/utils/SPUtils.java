package cesc.shang.notepaper.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cesc.shang.notepaper.R;
import cesc.shang.notepaper.constant.PictureConstant;

/**
 * Created by shanghaolongteng on 2016/9/8.
 */
public class SPUtils {
    private final String SP_NAME = "config";
    private final String PAINT_COLOR_NAME = "pc";
    private final String PAINT_WIDTH_NAME = "pw";
    private final String BG_TYPE_NAME = "bt";
    private final String BG_DESCRIBE_NAME = "bd";

    public int getBgType(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getInt(BG_TYPE_NAME, PictureConstant.PIC_BG_COLOR_TYPE);
    }

    public String getBgDescribe(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getString(BG_DESCRIBE_NAME, String.valueOf(context.getResources().getColor(R.color.default_pager_color)));
    }

    public int getPaintColor(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getInt(PAINT_COLOR_NAME, context.getResources().getColor(R.color.default_paint_color));
    }

    public float getPaintWidth(Context context) {
        SharedPreferences sp = getSP(context);
        return sp.getFloat(PAINT_WIDTH_NAME, context.getResources().getDimension(R.dimen.default_line_width));
    }

    public SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setBgType(Context context, int type) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putInt(BG_TYPE_NAME, type).commit();
    }

    public void setBgDescribe(Context context, String describe) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putString(BG_DESCRIBE_NAME, describe).commit();
    }

    public void setPaintColor(Context context, int color) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putInt(PAINT_COLOR_NAME, color).commit();
    }

    public void setPaintWidth(Context context, float width) {
        SharedPreferences.Editor edit = getEdit(context);
        edit.putFloat(PAINT_WIDTH_NAME, width).commit();
    }

    public SharedPreferences.Editor getEdit(Context context) {
        return getSP(context).edit();
    }
}
