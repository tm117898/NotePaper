package cesc.shang.notepaper.activity;

import android.content.Intent;

import cesc.shang.baselib.base.activity.BaseActivity;
import cesc.shang.baselib.base.application.BaseApplication;
import cesc.shang.baselib.support.manager.AppManager;
import cesc.shang.baselib.support.utils.UtilsManager;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.application.NoteApplication;
import cesc.shang.notepaper.controller.NoteController;
import cesc.shang.notepaper.manager.NoteManager;
import cesc.shang.notepaper.utils.NoteUtilsManager;

/**
 * Created by shanghaolongteng on 2017/7/22.
 */

public abstract class NoteBaseActivity extends BaseActivity {
    @Override
    public NoteApplication getApp() {
        return (NoteApplication) super.getApp();
    }

    @Override
    public NoteController getControllerManager() {
        return this.getApp().getControllerManager();
    }

    @Override
    public NoteManager getAppManager() {
        return this.getApp().getAppManager();
    }

    @Override
    public NoteUtilsManager getUtilsManager() {
        return this.getApp().getUtilsManager();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_in_anim, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_in_anim, 0);
    }
}
