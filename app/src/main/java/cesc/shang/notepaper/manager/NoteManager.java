package cesc.shang.notepaper.manager;

import cesc.shang.baselib.base.application.BaseApplication;
import cesc.shang.baselib.support.manager.AppManager;

/**
 * Created by shanghaolongteng on 2017/7/22.
 */

public class NoteManager extends AppManager {
    protected DBManager mDB;

    public NoteManager(BaseApplication app) {
        super(app);
    }

    public DBManager getDBManager() {
        if (mDB == null) {
            mDB = new DBManager(mApp);
        }
        return mDB;
    }
}
