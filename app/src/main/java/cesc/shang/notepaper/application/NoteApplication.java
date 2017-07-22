package cesc.shang.notepaper.application;

import cesc.shang.baselib.base.application.BaseApplication;
import cesc.shang.baselib.base.crash.BaseCrashHandler;
import cesc.shang.baselib.support.manager.AppManager;
import cesc.shang.notepaper.controller.NoteController;
import cesc.shang.notepaper.manager.NoteManager;
import cesc.shang.notepaper.utils.NoteUtilsManager;

/**
 * Created by shanghaolongteng on 2017/7/22.
 */

public class NoteApplication extends BaseApplication<NoteController, NoteManager, NoteUtilsManager> {
    @Override
    protected NoteUtilsManager initUtilsManager() {
        return new NoteUtilsManager(this);
    }

    @Override
    protected NoteManager initAppManager() {
        return new NoteManager(this);
    }

    @Override
    protected NoteController initAppController() {
        return new NoteController(this);
    }

    @Override
    protected Thread.UncaughtExceptionHandler getCrashHandler() {
        return new BaseCrashHandler(this);
    }

    @Override
    protected boolean enableStrictMode() {
        return true;
    }
}
