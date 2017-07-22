package cesc.shang.notepaper.view;

import cesc.shang.notepaper.entity.PathEntity;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.entity.PointEntity;

/**
 * Created by shanghaolongteng on 2016/9/5.
 */
public interface NoteViewCallBack {
    void onNewPicture(PictureEntity picture);

    void onNewPath(PictureEntity picture, PathEntity pathEntity);

    void onNewPoint(PathEntity pathEntity, PointEntity pointEntity);

    void onPreviousStep(PathEntity path);

    void onNextStep(PathEntity path);

    void onClear(PictureEntity picture);

    void onReleaseCatch(PictureEntity picture);

    void onReleaseAll(PictureEntity picture);

    void onSave(PictureEntity picture);

    void onBgChanged(PictureEntity picture);
}
