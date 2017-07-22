package cesc.shang.notepaper.activity;

import android.content.Intent;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import butterknife.BindView;
import butterknife.OnClick;
import cesc.shang.notepaper.R;

/**
 * Created by shanghaolongteng on 2016/9/8.
 */
public abstract class BaseSelectColorActivity extends NoteBaseActivity {
    public static final String COLOR_KEY = "COLOR_KEY";

    @BindView(R.id.picker)
    ColorPicker mPicker;
    @BindView(R.id.sv_bar)
    SVBar mSvBar;
    @BindView(R.id.opacity_bar)
    OpacityBar mOpacityBar;
    @BindView(R.id.saturation_bar)
    SaturationBar mSaturationBar;
    @BindView(R.id.value_bar)
    ValueBar mValueBar;

    private int mDefaultColor;

    @Override
    public int getContentViewId() {
        return R.layout.select_color_activity_layout;
    }

    @Override
    public void setupView() {
        super.setupView();
        mPicker.addSVBar(mSvBar);
        mPicker.addOpacityBar(mOpacityBar);
        mPicker.addSaturationBar(mSaturationBar);
        mPicker.addValueBar(mValueBar);
        mPicker.setShowOldCenterColor(true);
    }

    @Override
    public void setAdapter() {

    }

    @Override
    public void initData() {
        mDefaultColor = getDefaultColor();
        int color = getIntent().getIntExtra(COLOR_KEY, mDefaultColor);
        mPicker.setOldCenterColor(color);
    }

    public abstract int getDefaultColor();

    @OnClick({R.id.restore_defaults_color_button, R.id.ok_button})
    public void onClick(View view) {
        int color = 0;
        switch (view.getId()) {
            case R.id.ok_button:
                color = mPicker.getColor();
                break;
            case R.id.restore_defaults_color_button:
            default:
                color = mDefaultColor;
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(COLOR_KEY, color);
        setResult(RESULT_OK, intent);
        finish();
    }
}
