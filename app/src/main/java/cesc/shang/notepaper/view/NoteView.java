package cesc.shang.notepaper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

import cesc.shang.baselib.base.view.BaseView;
import cesc.shang.notepaper.R;
import cesc.shang.notepaper.application.NoteApplication;
import cesc.shang.notepaper.entity.PathEntity;
import cesc.shang.notepaper.entity.PictureEntity;
import cesc.shang.notepaper.entity.PointEntity;
import cesc.shang.notepaper.utils.NoteUtilsManager;
import cesc.shang.notepaper.utils.SPUtils;

/**
 * Created by shanghaolongteng on 2016/9/1.
 */
public class NoteView extends BaseView {

    private Paint mTextPaint = null;
    private PictureEntity mPicture;
    private NoteViewCallBack mCallBack = null;

    private float mScale = 1;
    private float mLastX, mLastY;

    private int mPaintColor;
    private float mPaintWidth;

    private boolean mEnableTouch = true;

    public NoteView(Context context) {
        super(context);
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NoteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NoteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setAntiAlias(true);

        NoteApplication app = (NoteApplication) context.getApplicationContext();
        SPUtils sp = app.getUtilsManager().getSPUtils();
        int color = sp.getPaintColor(context);
        float width = sp.getPaintWidth(context);
        setPaintColor(color);
        setPaintWidth(width);

        int scale = 1;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.note_view_arrt);
        try {
            scale = a.getInt(R.styleable.note_view_arrt_scale, scale);
            if (scale != 1)
                mScale = 1.0F / scale;
        } finally {
            a.recycle();
        }
    }

    public void setCallBack(NoteViewCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public void setPicture(PictureEntity picture) {
        mPicture = picture;
        getUtilsManager().getViewUtils().setBackground(NoteView.this, mPicture.getBg());
        postInvalidate();
    }

    public void setEnableTouch(boolean enable) {
        mEnableTouch = enable;
    }

    @Override
    protected boolean enableTouchUtils() {
        return false;
    }

    @Override
    protected int getSizeByAtMost(int i) {
        return i;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPicture == null) {
            mPicture = new PictureEntity(getContext());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPicture != null) {
            mPicture.releaseAll();
            mPicture = null;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        if (mScale != 1)
            canvas.scale(mScale, mScale);
        ArrayList<Path> path = mPicture.getPath();
        ArrayList<PathEntity> picture = mPicture.getPicture();
        int count = path.size();
        for (int i = 0; i < count; i++) {
            PathEntity pe = picture.get(i);
            mTextPaint.setColor(pe.getColor());
            mTextPaint.setStrokeWidth(pe.getWidth());
            canvas.drawPath(path.get(i), mTextPaint);
        }
        canvas.restore();
    }

    @Override
    protected boolean onTouchingEvent(final MotionEvent event) {
        if (!mEnableTouch)
            return super.onTouchingEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                PathEntity pathEntity = mPicture.add(mLastX, mLastY, mPaintColor, mPaintWidth);
                PointEntity pointEntity = pathEntity.getPoints().get(0);
                mPicture.releaseCatch();
                if (mCallBack != null && mPicture.getId() > -1) {
                    mCallBack.onReleaseCatch(mPicture);
                }
                if (mCallBack != null) {
                    if (mPicture.getId() < 0) {
                        mCallBack.onNewPicture(mPicture);
                    }
                    mCallBack.onNewPath(mPicture, pathEntity);
                    mCallBack.onNewPoint(pathEntity, pointEntity);
                }
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                float x = event.getX(), y = event.getY();
                if (x != mLastX && y != mLastY) {
                    mLastX = x;
                    mLastY = y;
                    pointEntity = mPicture.append(x, y);
                    if (mCallBack != null) {
                        ArrayList<PathEntity> paths = mPicture.getPicture();
                        mCallBack.onNewPoint(paths.get(paths.size() - 1), pointEntity);
                    }

                    postInvalidate();
                }
                break;
        }
        return true;
    }

    public void setPaintWidth(float width) {
        if (mPaintWidth != width) {
            mPaintWidth = width;
        }
    }

    public float getPaintWidth() {
        return mPaintWidth;
    }

    public void setPaintColor(int color) {
        if (mPaintColor != color) {
            mPaintColor = color;
        }
    }

    public void setBg(int type, String describe) {
        if (mPicture.getBgType() != type || !mPicture.getBgDescribe().equals(describe)) {
            mPicture.setBg(type, describe);
            post(new Runnable() {
                @Override
                public void run() {
                    getUtilsManager().getViewUtils().setBackground(NoteView.this, mPicture.getBg());
                }
            });
            if (mCallBack != null)
                mCallBack.onBgChanged(mPicture);
        }
    }

    public int getPaintColor() {
        return mPaintColor;
    }

    public int getBgType() {
        return mPicture.getBgType();
    }

    public String getBgDescribe() {
        return mPicture.getBgDescribe();
    }

    public long getPicId() {
        return mPicture.getId();
    }

    public void previousStep() {
        PathEntity path = mPicture.previousStep();
        if (path != null) {
            postInvalidate();
            if (mCallBack != null)
                mCallBack.onPreviousStep(path);
        }
    }

    public void nextStep() {
        PathEntity path = mPicture.nextStep();
        if (path != null) {
            postInvalidate();
            if (mCallBack != null)
                mCallBack.onNextStep(path);
        }
    }

    public void clear() {
        if (mPicture.clear()) {
            postInvalidate();
            if (mCallBack != null)
                mCallBack.onClear(mPicture);
        }
    }

    public void releaseAll() {
        if (mPicture.releaseAll()) {
            postInvalidate();
            if (mCallBack != null)
                mCallBack.onReleaseAll(mPicture);
        }
    }

    public void save() {
        if (mPicture.releaseAll())
            postInvalidate();
        if (mCallBack != null)
            mCallBack.onSave(mPicture);
        setPicture(new PictureEntity(getContext()));
    }
}
