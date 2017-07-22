package cesc.shang.notepaper.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.constant.PictureConstant;

/**
 * Created by shanghaolongteng on 2016/9/7.
 */
public class SettingActivity extends NoteBaseActivity {
    public static final int REQUEST_CODE = 117;
    public static final String PAINT_COLOR_KEY = "PAINT_COLOR_KEY";
    public static final String PAINT_WIDTH_KEY = "PAINT_WIDTH_KEY";
    public static final String PAPER_COLOR_KEY = "PAPER_COLOR_KEY";
    public static final String PAPER_TYPE_KEY = "PAPER_TYPE_KEY";

    @BindView(R.id.paint_color_preview_view)
    View mPaintColorPreviewView;
    @BindView(R.id.paint_width_preview_view)
    TextView mPaintWidthPreviewView;
    @BindView(R.id.bg_color_preview_view)
    View mBgColorPreviewView;
    @BindView(R.id.e_mail_add)
    TextView mEmailAdd;

    private int mPaintColor;
    private int mPaperColor;
    private float mPaintWidth;
    private boolean mHasConfigChanged = false;

    public static void startActivity(BaseActivity activity, int paintColor, float paintWidth, int paperColor) {
        Intent intent = new Intent(activity, SettingActivity.class);
        intent.putExtra(PAINT_COLOR_KEY, paintColor);
        intent.putExtra(PAINT_WIDTH_KEY, paintWidth);
        intent.putExtra(PAPER_COLOR_KEY, paperColor);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public int getContentViewId() {
        return R.layout.setting_activity_layout;
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mPaintColor = intent.getIntExtra(PAINT_COLOR_KEY, getResources().getColor(R.color.default_paint_color));
        mPaintWidth = intent.getFloatExtra(PAINT_WIDTH_KEY, getResources().getDimension(R.dimen.default_line_width));
        mPaperColor = intent.getIntExtra(PAPER_COLOR_KEY, getResources().getColor(R.color.default_pager_color));

        mPaintColorPreviewView.setBackgroundColor(mPaintColor);
        mPaintWidthPreviewView.setText(String.valueOf(mPaintWidth));
        mBgColorPreviewView.setBackgroundColor(mPaperColor);
        mEmailAdd.setText("279391654@QQ.com");
    }

    @OnClick({R.id.select_paint_color_frame, R.id.select_paint_width_frame, R.id.select_bg_color_frame, R.id.contact_frame})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_paint_color_frame:
                SelectPaintColorActivity.startActivity(this, mPaintColor);
                break;
            case R.id.select_paint_width_frame:
                SelectPaintWidthActivity.startActivity(this, mPaintWidth);
                break;
            case R.id.select_bg_color_frame:
                SelectBackGroundColorActivity.startActivity(this, mPaperColor);
                break;
            case R.id.contact_frame:
                getUtilsManager().getActivityUtils().startEmailActivity(this, getResources().getString(R.string.select_email_client), "", "", "");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SelectPaintColorActivity.REQUEST_CODE:
                    mPaintColor = data.getIntExtra(SelectPaintColorActivity.COLOR_KEY, mPaintColor);
                    mPaintColorPreviewView.setBackgroundColor(mPaintColor);
                    mHasConfigChanged = true;
                    break;
                case SelectBackGroundColorActivity.REQUEST_CODE:
                    mPaperColor = data.getIntExtra(SelectPaintColorActivity.COLOR_KEY, mPaperColor);
                    mBgColorPreviewView.setBackgroundColor(mPaperColor);
                    mHasConfigChanged = true;
                    break;
                case SelectPaintWidthActivity.REQUEST_CODE:
                    mPaintWidth = data.getFloatExtra(SelectPaintWidthActivity.WIDTH_KEY, mPaintWidth);
                    mPaintWidthPreviewView.setText(String.valueOf(mPaintWidth));
                    mHasConfigChanged = true;
                    break;
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mHasConfigChanged && keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(PAINT_COLOR_KEY, mPaintColor);
            intent.putExtra(PAINT_WIDTH_KEY, mPaintWidth);
            intent.putExtra(PAPER_COLOR_KEY, String.valueOf(mPaperColor));
            intent.putExtra(PAPER_TYPE_KEY, PictureConstant.PIC_BG_COLOR_TYPE);
            setResult(RESULT_OK, intent);
        }
        return super.onKeyUp(keyCode, event);
    }
}
