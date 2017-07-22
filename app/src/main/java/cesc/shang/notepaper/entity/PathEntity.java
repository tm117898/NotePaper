package cesc.shang.notepaper.entity;

import android.os.Parcel;

import java.util.ArrayList;

import cesc.shang.baselib.base.entity.BaseEntity;

/**
 * Created by shanghaolongteng on 2016/9/2.
 */
public class PathEntity extends BaseEntity {
    private long mId;
    private int mState;
    private long mPictureId;
    private ArrayList<PointEntity> mPoints;
    private int mColor;
    private float mWidth;

    public PathEntity() {
        mPoints = new ArrayList<PointEntity>();
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public long getPictureId() {
        return mPictureId;
    }

    public void setPictureId(long pictureId) {
        this.mPictureId = pictureId;
    }

    public ArrayList<PointEntity> getPoints() {
        return mPoints;
    }

    public void setPoints(ArrayList<PointEntity> mPoints) {
        this.mPoints = mPoints;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float mWidth) {
        this.mWidth = mWidth;
    }


    public void release() {
        mPoints.clear();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeInt(this.mState);
        dest.writeLong(this.mPictureId);
        dest.writeTypedList(this.mPoints);
        dest.writeInt(this.mColor);
        dest.writeFloat(this.mWidth);
    }

    protected PathEntity(Parcel in) {
        this.mId = in.readLong();
        this.mState = in.readInt();
        this.mPictureId = in.readLong();
        this.mPoints = in.createTypedArrayList(PointEntity.CREATOR);
        this.mColor = in.readInt();
        this.mWidth = in.readFloat();
    }

    public static final Creator<PathEntity> CREATOR = new Creator<PathEntity>() {
        @Override
        public PathEntity createFromParcel(Parcel source) {
            return new PathEntity(source);
        }

        @Override
        public PathEntity[] newArray(int size) {
            return new PathEntity[size];
        }
    };
}
