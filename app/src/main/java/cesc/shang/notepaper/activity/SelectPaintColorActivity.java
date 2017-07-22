package cesc.shang.notepaper.activity;

import android.content.Intent;

import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.notepaper.R;

/**
 * Created by shanghaolongteng on 2016/9/8.
 */
public class SelectPaintColorActivity extends BaseSelectColorActivity {
    public static final int REQUEST_CODE = 118;

    public static void startActivity(BaseActivity activity, int color) {
        Intent intent = new Intent(activity, SelectPaintColorActivity.class);
        intent.putExtra(COLOR_KEY, color);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public int getDefaultColor() {
        return getResources().getColor(R.color.default_paint_color);
    }
}
