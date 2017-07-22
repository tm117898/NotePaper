package cesc.shang.notepaper.controller;

import cesc.shang.baselib.base.application.BaseApplication;
import cesc.shang.baselib.support.controller.ControllerManager;

/**
 * Created by shanghaolongteng on 2017/7/22.
 */

public class NoteController extends ControllerManager {
    protected MainController mMain;

    public NoteController(BaseApplication app) {
        super(app);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMain = null;
    }

    public MainController getMainController() {
        if (mMain == null) {
            mMain = new MainController(mApp);
        }
        return mMain;
    }
}
