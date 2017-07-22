package cesc.shang.notepaper.entity;

import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import cesc.shang.baselib.base.entity.BaseEntity;
import cesc.shang.notepaper.application.NoteApplication;
import cesc.shang.notepaper.constant.PictureConstant;
import cesc.shang.notepaper.utils.SPUtils;

/**
 * Created by shanghaolongteng on 2016/9/2.
 */
public class PictureEntity extends BaseEntity {
    private long mId = -1;
    private int mState = PictureConstant.PIC_CATCH_STATE;
    private int mBgType = PictureConstant.PIC_BG_COLOR_TYPE;
    private String mBgDescribe = "";
    private Drawable mBg;

    private ArrayList<PathEntity> mPicture;
    private ArrayList<PathEntity> mCatchPicture;

    private ArrayList<Path> mPath;
    private ArrayList<Path> mCatchPath;

    private PictureEntity() {
    }

    public PictureEntity(Context context) {
        mPicture = new ArrayList<PathEntity>();
        mCatchPicture = new ArrayList<PathEntity>();

        mPath = new ArrayList<Path>();
        mCatchPath = new ArrayList<Path>();

        NoteApplication app = (NoteApplication) context.getApplicationContext();
        SPUtils sp = app.getUtilsManager().getSPUtils();
        String bgDescribe = sp.getBgDescribe(context);
        int bgType = sp.getBgType(context);
        setBg(bgType, bgDescribe);
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        this.mState = mState;
    }

    public ArrayList<PathEntity> getPicture() {
        return mPicture;
    }

    public ArrayList<PathEntity> getCatchPicture() {
        return mCatchPicture;
    }

    public ArrayList<Path> getPath() {
        return mPath;
    }

    public ArrayList<Path> getCatchPath() {
        return mCatchPath;
    }

    public void setBg(int mBgType, String mBgDescribe) {
        this.mBgType = mBgType;
        this.mBgDescribe = mBgDescribe;
        this.mBg = getBg(mBgType, mBgDescribe);
    }

    public static Drawable getBg(int mBgType, String mBgDescribe) {
        Drawable drawable = null;
        switch (mBgType) {
            case PictureConstant.PIC_BG_COLOR_TYPE:
                drawable = new ColorDrawable(Integer.parseInt(mBgDescribe));
                drawable.setBounds(0, 0, 1, 1);
                break;
        }
        return drawable;
    }

    public int getBgType() {
        return mBgType;
    }

    public String getBgDescribe() {
        return mBgDescribe;
    }

    public Drawable getBg() {
        return mBg;
    }

    public PathEntity add(float x, float y, int color, float width) {
        PathEntity pe = new PathEntity();
        pe.setColor(color);
        pe.setWidth(width);
        pe.getPoints().add(new PointEntity(x, y));
        mPicture.add(pe);

        Path path = new Path();
        path.moveTo(x, y);
        mPath.add(path);

        return pe;
    }

    /**
     * call this method , points must be has point
     */
    public void add(PathEntity path) {
        ArrayList<PointEntity> points = path.getPoints();
        Path p = null;
        for (int i = 0; i < points.size(); i++) {
            PointEntity point = points.get(i);
            if (i == 0) {
                p = new Path();
                p.moveTo(point.getX(), point.getY());
                if (path.getState() == PictureConstant.PIC_VALID_STATE) {
                    mPicture.add(path);
                    mPath.add(p);
                } else {
                    mCatchPicture.add(path);
                    mCatchPath.add(p);
                }
            } else {
                p.lineTo(point.getX(), point.getY());
            }
        }
    }

    public PointEntity append(float x, float y) {
        PointEntity point = new PointEntity(x, y);

        PathEntity pe = mPicture.get(mPicture.size() - 1);
        pe.getPoints().add(point);

        Path path = mPath.get(mPath.size() - 1);
        path.lineTo(x, y);

        return point;
    }

    public PathEntity previousStep() {
        PathEntity path = null;
        int count = mPicture.size();
        if (count > 0) {
            int lastIndex = count - 1;
            path = mPicture.get(lastIndex);
            mCatchPicture.add(path);
            mPicture.remove(lastIndex);

            mCatchPath.add(mPath.get(lastIndex));
            mPath.remove(lastIndex);
        }
        return path;
    }

    public PathEntity nextStep() {
        PathEntity path = null;
        int count = mCatchPicture.size();
        if (count > 0) {
            int lastIndex = count - 1;
            path = mCatchPicture.get(lastIndex);
            mPicture.add(path);
            mCatchPicture.remove(lastIndex);

            mPath.add(mCatchPath.get(lastIndex));
            mCatchPath.remove(lastIndex);
        }
        return path;
    }

    public boolean clear() {
        int count = mPicture.size();
        if (count > 0) {
            mCatchPicture.addAll(mPicture);
            mPicture.clear();

            mCatchPath.addAll(mPath);
            mPath.clear();
            return true;
        }
        return false;
    }

    public boolean releaseAll() {
        releaseCatch();

        return releasePicture();
    }

    public boolean releasePicture() {
        int count = mPicture.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                mPicture.get(i).release();
            }
            mPicture.clear();

            count = mPath.size();
            for (int i = 0; i < count; i++) {
                mPath.get(i).close();
            }
            mPath.clear();

            return true;
        }
        return false;
    }

    public boolean releaseCatch() {
        int count = mCatchPicture.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                mCatchPicture.get(i).release();
            }
            mCatchPicture.clear();

            count = mCatchPath.size();
            for (int i = 0; i < count; i++) {
                mCatchPath.get(i).close();
            }
            mCatchPath.clear();

            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeInt(this.mState);
        dest.writeInt(this.mBgType);
        dest.writeString(this.mBgDescribe);
//        dest.writeParcelable(this.mBg, flags);
        dest.writeTypedList(this.mPicture);
        dest.writeTypedList(this.mCatchPicture);
//        dest.writeList(this.mPath);
//        dest.writeList(this.mCatchPath);
    }

    protected PictureEntity(Parcel in) {
        this.mId = in.readLong();
        this.mState = in.readInt();
        this.mBgType = in.readInt();
        this.mBgDescribe = in.readString();
//        this.mBg = in.readParcelable(Drawable.class.getClassLoader());
        this.mBg = getBg(mBgType, mBgDescribe);
        this.mPicture = in.createTypedArrayList(PathEntity.CREATOR);
        this.mCatchPicture = in.createTypedArrayList(PathEntity.CREATOR);

        this.mPath = new ArrayList<Path>();
        int count = mPicture.size();
        for (int i = 0; i < count; i++) {
            PathEntity pathEntity = mPicture.get(i);
            Path path = new Path();
            List<PointEntity> points = pathEntity.getPoints();
            int pointCount = points.size();
            for (int j = 0; j < pointCount; j++) {
                PointEntity pointEntity = points.get(j);
                if (j == 0) {
                    path.moveTo(pointEntity.getX(), pointEntity.getY());
                } else {
                    path.lineTo(pointEntity.getX(), pointEntity.getY());
                }
            }
            this.mPath.add(path);
        }
//        in.readList(this.mPath, Path.class.getClassLoader());

        this.mCatchPath = new ArrayList<Path>();
        count = mCatchPicture.size();
        for (int i = 0; i < count; i++) {
            PathEntity pathEntity = mCatchPicture.get(i);
            Path path = new Path();
            List<PointEntity> points = pathEntity.getPoints();
            int pointCount = points.size();
            for (int j = 0; j < pointCount; j++) {
                PointEntity pointEntity = points.get(j);
                if (j == 0) {
                    path.moveTo(pointEntity.getX(), pointEntity.getY());
                } else {
                    path.lineTo(pointEntity.getX(), pointEntity.getY());
                }
            }
            this.mCatchPath.add(path);
        }
//        in.readList(this.mCatchPath, Path.class.getClassLoader());
    }

    public static final Creator<PictureEntity> CREATOR = new Creator<PictureEntity>() {
        @Override
        public PictureEntity createFromParcel(Parcel source) {
            return new PictureEntity(source);
        }

        @Override
        public PictureEntity[] newArray(int size) {
            return new PictureEntity[size];
        }
    };
}
