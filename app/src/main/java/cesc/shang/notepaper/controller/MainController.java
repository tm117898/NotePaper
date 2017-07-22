package cesc.shang.notepaper.controller;

import android.content.Intent;
import android.os.Handler;

import cesc.shang.baselib.base.application.BaseApplication;
import cesc.shang.baselib.support.BaseManager;
import cesc.shang.baselib.support.callback.IGetDataSuccessCallBack;
import cesc.shang.notepaper.activity.SettingActivity;
import cesc.shang.notepaper.entity.PathEntity;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.entity.PointEntity;
import cesc.shang.notepaper.manager.DBManager;
import cesc.shang.notepaper.manager.NoteManager;
import cesc.shang.notepaper.utils.NoteUtilsManager;
import cesc.shang.notepaper.utils.SPUtils;
import cesc.shang.notepaper.view.NoteView;
import cesc.shang.notepaper.view.NoteViewCallBack;

/**
 * Created by shanghaolongteng on 2017/7/22.
 */

public class MainController extends BaseManager implements NoteViewCallBack {
    public static final String EXTRA_KEY = "EXTRA_KEY";
    public static final String MAIN_CONTROLLER_THREAD = "MainController";
    private Handler mWork = null;

    public MainController(BaseApplication app) {
        super(app);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onActivityCreate() {
        mWork = mApp.getUtilsManager().getThreadUtils().getHandlerThread(MAIN_CONTROLLER_THREAD);
    }

    public void onActivityDestroy() {
        mApp.getUtilsManager().getThreadUtils().quitHandlerThread(mWork);
        mWork = null;
    }

    public void getData(final Intent intent, final IGetDataSuccessCallBack<PictureEntity> callBack) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                getDataInThread(intent, callBack);
            }
        });
    }

    private void getDataInThread(Intent intent, IGetDataSuccessCallBack<PictureEntity> callBack) {
        PictureEntity pic = intent.getParcelableExtra(EXTRA_KEY);
        if (pic == null) {
            pic = getDbManager().selectCatchPictureEntity(mApp);
        }
        callBack.onSuccess(pic);
    }

    public void onSettingActivityResult(final Intent data, final NoteView noteView) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                int paintColor = data.getIntExtra(SettingActivity.PAINT_COLOR_KEY, noteView.getPaintColor());
                float paintWidth = data.getFloatExtra(SettingActivity.PAINT_WIDTH_KEY, noteView.getPaintWidth());
                int bgType = data.getIntExtra(SettingActivity.PAPER_TYPE_KEY, noteView.getBgType());
                String bgDescribe = data.getStringExtra(SettingActivity.PAPER_COLOR_KEY);

                noteView.setPaintColor(paintColor);
                noteView.setPaintWidth(paintWidth);
                noteView.setBg(bgType, bgDescribe);

                SPUtils sp = ((NoteUtilsManager) mApp.getUtilsManager()).getSPUtils();
                sp.setPaintColor(mApp, paintColor);
                sp.setPaintWidth(mApp, paintWidth);
                sp.setBgType(mApp, bgType);
                sp.setBgDescribe(mApp, bgDescribe);
            }
        });
    }

    private DBManager getDbManager() {
        return ((NoteManager) mApp.getAppManager()).getDBManager();
    }

    @Override
    public void onNewPicture(final PictureEntity picture) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.insertPicture(picture);
            }
        });
    }

    @Override
    public void onNewPath(final PictureEntity picture, final PathEntity pathEntity) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.insertPath(picture.getId(), pathEntity);
            }
        });
    }

    @Override
    public void onNewPoint(final PathEntity pathEntity, final PointEntity pointEntity) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.insertPoint(pathEntity.getId(), pointEntity);
            }
        });
    }


    @Override
    public void onPreviousStep(final PathEntity path) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.previousStep(path);
            }
        });
    }

    @Override
    public void onNextStep(final PathEntity path) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.nextStep(path);
            }
        });
    }

    @Override
    public void onClear(final PictureEntity picture) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.clear(picture);
            }
        });
    }

    @Override
    public void onReleaseCatch(final PictureEntity picture) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.releaseCatch(picture);
            }
        });
    }

    @Override
    public void onReleaseAll(final PictureEntity picture) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.releaseAll(picture);
            }
        });
    }

    @Override
    public void onSave(final PictureEntity picture) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.save(picture);
            }
        });
    }

    @Override
    public void onBgChanged(final PictureEntity picture) {
        mWork.post(new Runnable() {
            @Override
            public void run() {
                DBManager db = getDbManager();
                db.bgChanged(picture);
            }
        });
    }
}
