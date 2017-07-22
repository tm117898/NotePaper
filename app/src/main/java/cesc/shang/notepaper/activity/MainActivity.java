package cesc.shang.notepaper.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import cesc.shang.baselib.support.callback.IGetDataSuccessCallBack;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.controller.MainController;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.pop.ClosePop;
import cesc.shang.notepaper.view.NoteView;

public class MainActivity extends NoteBaseActivity {
    private ClosePop mClosePop = null;

    @BindView(R.id.note_view)
    NoteView mNoteView;

    public static void startActivity(NoteBaseActivity activity, PictureEntity pic) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(MainController.EXTRA_KEY, pic);
        activity.startActivity(intent);
    }

    @Override
    public int getContentViewId() {
        return R.layout.main_activity_layout;
    }

    @Override
    public void setAdapter() {
        mNoteView.setCallBack(getMainController());
    }

    @Override
    public void initData() {
        getMainController().onActivityCreate();
        getMainController().getData(getIntent(), mGetDataCallBack);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getMainController().getData(getIntent(), mGetDataCallBack);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMainController().onActivityDestroy();
        if (mClosePop != null) {
            mClosePop.release();
        }
    }

    private MainController getMainController() {
        return getControllerManager().getMainController();
    }

    private IGetDataSuccessCallBack<PictureEntity> mGetDataCallBack = new IGetDataSuccessCallBack<PictureEntity>() {
        @Override
        public void onSuccess(PictureEntity pictureEntity) {
            mNoteView.setPicture(pictureEntity);
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mClosePop != null && mClosePop.isShowing()) {
                mClosePop.dismiss();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @OnClick({R.id.previous_step_image, R.id.next_step_image, R.id.clear_image,/* R.id.switch_screen_image,*/
            R.id.close_image, R.id.save_image, R.id.history_image, R.id.setting_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_step_image:
                mNoteView.previousStep();
                break;
            case R.id.next_step_image:
                mNoteView.nextStep();
                break;
            case R.id.clear_image:
                mNoteView.clear();
                break;
//            case R.id.switch_screen_image:
//                break;
            case R.id.close_image:
                if (mClosePop == null) {
                    mClosePop = new ClosePop(this);
                    mClosePop.setParam(mOnClickListener);
                }
                mClosePop.show(mNoteView);
                break;
            case R.id.save_image:
                mNoteView.save();
                break;
            case R.id.history_image:
                HistoryActivity.startActivity(this);
                break;
            case R.id.setting_image:
                SettingActivity.startActivity(this, mNoteView.getPaintColor(), mNoteView.getPaintWidth(), Integer.parseInt(mNoteView.getBgDescribe()));
                break;
            case R.id.discard_button:
                mLog.i("shlt , mainactivity , discard_button");
                mNoteView.releaseAll();
            case R.id.cancel_button:
                mClosePop.dismiss();
                break;
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.this.onClick(v);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SettingActivity.REQUEST_CODE:
                    getMainController().onSettingActivityResult(data, mNoteView);
                    break;
                case HistoryActivity.REQUEST_CODE:
                    String deleteIds = data.getStringExtra(HistoryActivity.DELETE_IDS_KEY);
                    if (!TextUtils.isEmpty(deleteIds) && deleteIds.contains(String.valueOf(mNoteView.getPicId()))) {
                        mNoteView.setPicture(new PictureEntity(MainActivity.this));
                    }
                    break;
            }
        }
    }
}
