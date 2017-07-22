package cesc.shang.notepaper.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.OnClick;
import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.view.PaintWidthPreviewView;

/**
 * Created by shanghaolongteng on 2016/9/8.
 */
public class SelectPaintWidthActivity extends NoteBaseActivity {
    public static final String WIDTH_KEY = "WIDTH_KEY";
    public static final int REQUEST_CODE = 120;

    @BindView(R.id.preview_view)
    PaintWidthPreviewView mPreviewView;
    @BindView(R.id.width_edit)
    EditText mWidthEdit;
    @BindView(R.id.width_seek_bar)
    SeekBar mWidthSeekBar;

    private float mWidth;
    private int mMaxWidth;
    private float mDefaultWidth;

    public static void startActivity(BaseActivity activity, float width) {
        Intent intent = new Intent(activity, SelectPaintWidthActivity.class);
        intent.putExtra(WIDTH_KEY, width);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public int getContentViewId() {
        return R.layout.select_paint_width_activity_layout;
    }

    @Override
    public void initData() {
        mDefaultWidth = getResources().getDimension(R.dimen.default_line_width);
        mMaxWidth = getResources().getInteger(R.integer.paint_max_width);

        mWidth = getIntent().getFloatExtra(WIDTH_KEY, getResources().getDimension(R.dimen.default_line_width));
        mPreviewView.setPaintWidth(mWidth);
        mWidthEdit.setText(String.valueOf(mWidth));
        mWidthSeekBar.setProgress((int) (mMaxWidth / mWidth));
    }

    @Override
    public void setupView() {
        super.setupView();
        mWidthEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mWidth = 0;
                } else {
                    mWidth = Float.valueOf(s.toString());
                }
                if (mWidth >= mMaxWidth) {
                    mWidthSeekBar.setProgress(mMaxWidth);
                } else {
                    mWidthSeekBar.setProgress((int) (mWidth));
                }
                mPreviewView.setPaintWidth(mWidth);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mWidthSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mWidth = mWidthSeekBar.getProgress() * 1.0F;
                    mPreviewView.setPaintWidth(mWidth);
                    mWidthEdit.setText(String.valueOf(mWidth));
                }
                return false;
            }
        });
    }

    @OnClick({R.id.restore_defaults_color_button, R.id.ok_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.restore_defaults_color_button:
                mWidth = mDefaultWidth;
                mWidthEdit.setText(String.valueOf(mWidth));
                mPreviewView.setPaintWidth(mWidth);
                break;
            case R.id.ok_button:
                Intent intent = new Intent();
                intent.putExtra(WIDTH_KEY, mWidth);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
