package cesc.shang.notepaper.pop;

import android.view.View;

import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.baselib.base.popup.BasePopupWindow;
import cesc.shang.notepaper.R;

/**
 * Created by shanghaolongteng on 2016/9/6.
 */
public class ClosePop extends BasePopupWindow {

    public ClosePop(BaseActivity activity) {
        super(activity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.close_pop_layout;
    }

    public void setParam(View.OnClickListener listener) {
        View view = getContentView();
        view.findViewById(R.id.cancel_button).setOnClickListener(listener);
        view.findViewById(R.id.discard_button).setOnClickListener(listener);
    }
}
