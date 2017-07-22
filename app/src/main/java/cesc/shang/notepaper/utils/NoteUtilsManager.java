package cesc.shang.notepaper.utils;

import cesc.shang.baselib.base.application.BaseApplication;
import cesc.shang.baselib.support.utils.UtilsManager;

/**
 * Created by shanghaolongteng on 2017/7/22.
 */

public class NoteUtilsManager extends UtilsManager {
    protected SPUtils mSp;

    public NoteUtilsManager(BaseApplication app) {
        super(app);
    }

    public SPUtils getSPUtils() {
        if (mSp == null) {
            mSp = new SPUtils();
        }
        return mSp;
    }
}
