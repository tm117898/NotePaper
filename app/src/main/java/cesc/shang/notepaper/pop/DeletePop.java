package cesc.shang.notepaper.pop;

import android.view.View;

import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.baselib.base.popup.BasePopupWindow;
import cesc.shang.notepaper.R;


/**
 * Created by shanghaolongteng on 2016/9/9.
 */
public class DeletePop extends BasePopupWindow {
    public DeletePop(BaseActivity activity) {
        super(activity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.delete_pop_layout;
    }

    public void setParam(View.OnClickListener clickListener, Object tag) {
        View view = getContentView();
        view.findViewById(R.id.cancel_button).setOnClickListener(clickListener);
        view = view.findViewById(R.id.delete_button);
        view.setTag(tag);
        view.setOnClickListener(clickListener);
    }
}
