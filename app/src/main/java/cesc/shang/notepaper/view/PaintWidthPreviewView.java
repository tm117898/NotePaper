package cesc.shang.notepaper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import cesc.shang.baselib.base.view.BaseView;
import cesc.shang.notepaper.R;

/**
 * Created by shanghaolongteng on 2016/9/8.
 */
public class PaintWidthPreviewView extends BaseView {
    private Paint mPaint;

    public PaintWidthPreviewView(Context context) {
        super(context);
        init(context);
    }

    public PaintWidthPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaintWidthPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PaintWidthPreviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources().getColor(R.color.default_paint_color));
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
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float lineWith = mPaint.getStrokeWidth();
        float y = (getMeasuredHeight() - lineWith) / 2;
        canvas.drawLine(0, y, getMeasuredWidth(), y, mPaint);
    }

    public void setPaintWidth(float width) {
        if (mPaint.getStrokeWidth() != width) {
            mPaint.setStrokeWidth(width);
            postInvalidate();
        }
    }
}
