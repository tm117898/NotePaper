package cesc.shang.notepaper.entity;

import android.os.Parcel;

import cesc.shang.baselib.base.entity.BaseEntity;

/**
 * Created by shanghaolongteng on 2016/9/2.
 */
public class PointEntity extends BaseEntity {
    private long mId;
    private long mPathId;
    private float mX;
    private float mY;

    public PointEntity(float x, float y) {
        mX = x;
        mY = y;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getPathId() {
        return mPathId;
    }

    public void setPathId(long mPathId) {
        this.mPathId = mPathId;
    }

    public float getX() {
        return mX;
    }


    public float getY() {
        return mY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeLong(this.mPathId);
        dest.writeFloat(this.mX);
        dest.writeFloat(this.mY);
    }

    protected PointEntity(Parcel in) {
        this.mId = in.readLong();
        this.mPathId = in.readLong();
        this.mX = in.readFloat();
        this.mY = in.readFloat();
    }

    public static final Creator<PointEntity> CREATOR = new Creator<PointEntity>() {
        @Override
        public PointEntity createFromParcel(Parcel source) {
            return new PointEntity(source);
        }

        @Override
        public PointEntity[] newArray(int size) {
            return new PointEntity[size];
        }
    };
}
