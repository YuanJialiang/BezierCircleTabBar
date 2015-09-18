package scut.yuan.jialiang.beziercircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by hero on 15/9/15.
 */
public class BezierCircle extends View implements ViewPager.OnPageChangeListener {

    private static final float C_RATE = 0.551915024494f;

    private Paint mPaint;
    private Path mPath;
    private float mHeight;
    private float mWidth;
    private float mCenterX;
    private float mCenterY;
    private float mRadius;
    private float mC;
    private float mCDistance;
    /**
     * 移动的最长距离
     */
    private float mMaxLength;
    private float mStretchDistance;
    private VerticalPoint mLeftPoint, mRightPoint;
    private HorizontalPoint mTopPoint, mBottomPoint;
    private int mPositionCount;
    private float mOffset;
    private boolean mWantToLeftMove;
    private boolean mChangeOrientation;
    private int mLastPosition;


    public BezierCircle(Context context) {
        this(context, null);
    }

    public BezierCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //TODO:初始化操作
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#61B6D2"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);

        mPath = new Path();
        mLeftPoint = new VerticalPoint();
        mRightPoint = new VerticalPoint();
        mTopPoint = new HorizontalPoint();
        mBottomPoint = new HorizontalPoint();

        mPositionCount = 3;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mWidth = getWidth();
        mCenterX = mWidth / mPositionCount / 2;
        mCenterY = mHeight / 2;

        mRadius = 50;
        mC = mRadius * C_RATE;
        mMaxLength = mWidth / mPositionCount;
        mStretchDistance = mRadius;
        mCDistance = mC * 0.45f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        canvas.translate(mCenterX, mCenterY);

        if (mWantToLeftMove) {
            wantToLeftMove();
//            wantToRightMove();
        } else {
            wantToRightMove();
//            wantToLeftMove();
        }

        mPath.moveTo(mBottomPoint.x, mBottomPoint.y);
        mPath.cubicTo(mBottomPoint.right.x, mBottomPoint.right.y, mRightPoint.bottom.x, mRightPoint.bottom.y, mRightPoint.x, mRightPoint.y);
        mPath.cubicTo(mRightPoint.top.x, mRightPoint.top.y, mTopPoint.right.x, mTopPoint.right.y, mTopPoint.x, mTopPoint.y);
        mPath.cubicTo(mTopPoint.left.x, mTopPoint.left.y, mLeftPoint.top.x, mLeftPoint.top.y, mLeftPoint.x, mLeftPoint.y);
        mPath.cubicTo(mLeftPoint.bottom.x, mLeftPoint.bottom.y, mBottomPoint.left.x, mBottomPoint.left.y, mBottomPoint.x, mBottomPoint.y);

        canvas.drawPath(mPath, mPaint);

    }

    private void wantToRightMove() {

        if (mOffset >= 0 && mOffset <= 0.2) {
            toRightModel1(mOffset);
        } else if (mOffset > 0.2 && mOffset <= 0.5) {
            toRightModel2(mOffset);
        } else if (mOffset > 0.5 && mOffset <= 0.8) {
            toRightModel3(mOffset);
        } else if (mOffset > 0.8 && mOffset <= 0.9) {
            toRightModel4(mOffset);
        } else if (mOffset > 0.9 && mOffset <= 1.0) {
            toRightModel5(mOffset);
        }

        float offset = mMaxLength * (mOffset - 0.2f);
        offset = offset > 0 ? offset : 0;
        mTopPoint.adjustAllX(offset);
        mBottomPoint.adjustAllX(offset);
        mLeftPoint.adjustAllX(offset);
        mRightPoint.adjustAllX(offset);
    }

    private void wantToLeftMove() {

        if (mOffset >= 0 && mOffset < 0.1) {
            toLeftModel5(mOffset);
        } else if (mOffset >= 0.1 && mOffset < 0.2) {
            toLeftModel4(mOffset);
        } else if (mOffset >= 0.2 && mOffset < 0.5) {
            toLeftModel3(mOffset);
        } else if (mOffset >= 0.5 && mOffset < 0.8) {
            toLeftModel2(mOffset);
        } else if (mOffset >= 0.8 && mOffset <= 1.0) {
            toLeftModel1(mOffset);
        }

        float offset = mMaxLength * (-mOffset + 0.8f);
        offset = offset > 0 ? mMaxLength - offset : mMaxLength;
        mTopPoint.adjustAllX(offset);
        mBottomPoint.adjustAllX(offset);
        mLeftPoint.adjustAllX(offset);
        mRightPoint.adjustAllX(offset);
    }

    private void model0() {
        mTopPoint.setY(-mRadius);
        mBottomPoint.setY(mRadius);
        mTopPoint.x = mBottomPoint.x = 0;
        mTopPoint.left.x = mBottomPoint.left.x = -mC;
        mTopPoint.right.x = mBottomPoint.right.x = mC;

        mLeftPoint.setX(-mRadius);
        mRightPoint.setX(mRadius);
        mLeftPoint.y = mRightPoint.y = 0;
        mLeftPoint.top.y = mRightPoint.top.y = -mC;
        mLeftPoint.bottom.y = mRightPoint.bottom.y = mC;

    }

    private void toRightModel1(float offset) {//0.0~0.2
        model0();
        mRightPoint.setX(mRadius + mStretchDistance * offset * 5);
    }

    private void toLeftModel1(float offset) {//1.0~0.8
        model0();
        offset = 1 - offset;
        mLeftPoint.setX(-mRadius - mStretchDistance * offset * 5);
    }

    private void toRightModel2(float offset) {//0.2~0.5
        toRightModel1(0.2f);
        offset = (offset - 0.2f) * (10f / 3);
        mBottomPoint.adjustAllX(mStretchDistance / 2 * offset);
        mTopPoint.adjustAllX(mStretchDistance / 2 * offset);
        mRightPoint.adjustY(mCDistance * offset);
        mLeftPoint.adjustY(mCDistance * offset);
    }

    private void toLeftModel2(float offset) {//0.8~0.5
        toLeftModel1(0.8f);
        offset = 1 - offset;
        offset = (offset - 0.2f) * (10f / 3);
        mBottomPoint.adjustAllX(-mStretchDistance / 2 * offset);
        mTopPoint.adjustAllX(-mStretchDistance / 2 * offset);
        mRightPoint.adjustY(mCDistance * offset);
        mLeftPoint.adjustY(mCDistance * offset);
    }

    private void toRightModel3(float offset) {//0.5~0.8
        toRightModel2(0.5f);
        offset = (offset - 0.5f) * (10f / 3);
        mBottomPoint.adjustAllX(mStretchDistance / 2 * offset);
        mTopPoint.adjustAllX(mStretchDistance / 2 * offset);
        mRightPoint.adjustY(-mCDistance * offset);
        mLeftPoint.adjustY(-mCDistance * offset);

        mLeftPoint.adjustAllX(mStretchDistance / 2 * offset);

    }

    private void toLeftModel3(float offset) {//0.5~0.2
        toLeftModel2(0.5f);
        offset = 1 - offset;
        offset = (offset - 0.5f) * (10f / 3);
        mBottomPoint.adjustAllX(-mStretchDistance / 2 * offset);
        mTopPoint.adjustAllX(-mStretchDistance / 2 * offset);
        mRightPoint.adjustY(-mCDistance * offset);
        mLeftPoint.adjustY(-mCDistance * offset);

        mRightPoint.adjustAllX(-mStretchDistance / 2 * offset);
    }

    private void toRightModel4(float offset) {//0.8~0.9
        toRightModel3(0.8f);
        offset = (offset - 0.8f) * 10;
        mLeftPoint.adjustAllX(mStretchDistance / 2 * offset);
    }

    private void toLeftModel4(float offset) {//0.2~0.1
        toLeftModel3(0.2f);
        offset = 1 - offset;
        offset = (offset - 0.8f) * 10;
        mRightPoint.adjustAllX(-mStretchDistance / 2 * offset);
    }

    private void toRightModel5(float offset) {//0.9~1.0
        toRightModel4(0.9f);
        offset = offset - 0.9f;
        mLeftPoint.adjustAllX((float) (Math.sin(Math.PI * offset * 10f) * (2 / 10f * mRadius)));

    }

    private void toLeftModel5(float offset) {//0.1~0.0
        toLeftModel4(0.1f);
        offset = 1 - offset;
        offset = offset - 0.9f;
        mRightPoint.adjustAllX(-(float) (Math.sin(Math.PI * offset * 10f) * (2 / 10f * mRadius)));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.e("onPageScrolled", "position:" + position + " positionOffset:" + positionOffset + " positionOffsetPixels:" + positionOffsetPixels);
        setOffset(positionOffset, position);
    }

    @Override
    public void onPageSelected(int position) {
//        Log.e("onPageSelected", "position:" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.e("onPageScrollStateChanged", "state:" + state);
        if (state == 1) {
            setOrientation();
        }
    }

    class VerticalPoint {
        float x;
        float y;
        PointF top = new PointF();
        PointF bottom = new PointF();

        public void setX(float x) {
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        public void adjustY(float offset) {
            top.y -= offset;
            bottom.y += offset;
        }

        public void adjustAllX(float offset) {
            x += offset;
            top.x += offset;
            bottom.x += offset;
        }
    }

    class HorizontalPoint {
        float x;
        float y;
        PointF left = new PointF();
        PointF right = new PointF();

        public void setY(float y) {
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset) {
            x += offset;
            right.x += offset;
            left.x += offset;
        }
    }

    public void setOffset(float offset, int position) {
        mOffset = offset;

        if (mChangeOrientation) {
            mChangeOrientation = false;
            if (position < mLastPosition) {
                mWantToLeftMove = true;
            } else {
                mWantToLeftMove = false;
            }
        }
        if (!mWantToLeftMove) {
            if (position > mLastPosition) {
                mCenterX = (int) (mWidth / mPositionCount / 2 + position * mMaxLength);
            }
            if (position < mLastPosition) {
                mWantToLeftMove = true;
            }
        }
        if (mWantToLeftMove) {
            if (position > mLastPosition) {
//                mOffset = 1.0f;
                mWantToLeftMove = false;
                mCenterX = (int) (mWidth / mPositionCount / 2 + position * mMaxLength);
            }
            if (position < mLastPosition) {
                mCenterX = (int) (mWidth / mPositionCount / 2 + position * mMaxLength);
            }
        }

        mLastPosition = position;
        invalidate();
    }

    public void setOrientation() {
        mChangeOrientation = true;
    }

    public void setViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(this);
    }

}
