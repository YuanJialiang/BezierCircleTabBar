package scut.yuan.jialiang.beziercircle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by hero on 15/11/26.
 */
public class TabbarLayout extends ViewGroup {

    private BezierCircle mBezierCircle;
    private Context mContext;
    private int mRadius;

    public TabbarLayout(Context context) {
        this(context, null);
    }

    public TabbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabbarLayout);
        int n = typedArray.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.TabbarLayout_radius:
                    mRadius = (int) typedArray.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    break;
            }
        }

        typedArray.recycle();

        mBezierCircle = new BezierCircle(context, attrs);
        addView(mBezierCircle);

    }

    private Bitmap getBitmap(int tarSize, int resID) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resID);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scale = (float) tarSize / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return newbm;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int count = getChildCount();
        int width = getWidth();
        int height = getHeight();
        if (count > 0) {
            if (count > 1) {
                for (int x = 1; x < count; x++) {
                    getChildAt(x).layout(width / 3 * (x - 1), 0, width / 3 * x, height);
                }
            }
            getChildAt(0).layout(0, 0, width, height);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mBezierCircle.setViewPager(viewPager);
    }

    public void setImageResource(int[] resource){
        int size = resource.length;
        mBezierCircle.setPositionCount(size);

        for (int i = 0; i < size; i ++){
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageBitmap(getBitmap(mRadius,resource[i]));

            addView(imageView);
        }

    }
}
