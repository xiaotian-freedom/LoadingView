package com.loadingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * 加载动画
 *
 * @author tianshutong
 *         Created by tianshutong on 2017/1/17.
 */

public class LoadingView extends View {

    //定义外圆动画路径
    private Path mCircleAnimPath;

    //定义对号动画路径
    private Path mSuccessAnimPath;

    //定义左侧叉号动画路径
    private Path mFailLeftAnimPath;

    //定义右侧叉号动画路径
    private Path mFailRightAnimPath;

    //定义外圆路径
    private Path mCircleSourcePath;

    //定义对号路径
    private Path mSuccessSourcePath;

    //定义左侧叉号路径
    private Path mFailLeftSourcePath;

    //定义右侧叉号路径
    private Path mFailRightSourcePath;

    //定义外圆画笔
    private Paint mCirclePaint;

    //定义对号画笔
    private Paint mSuccessPaint;

    //定义叉号画笔
    private Paint mFailPaint;

    //外圆画笔颜色
    private int mCirclePaintColor;

    //对号画笔颜色
    private int mSuccessPaintColor;

    //叉号画笔颜色
    private int mFailPaintColor;

    //画笔宽度
    private int mPaintWidth;

    //画笔风格
//    private int mStyle;

    //动画时长
    private int mDuration;

    //动画是否循环
    private boolean mIsInfinite;

    //是否成功
    private boolean mIsSuccess;

    //是否失败
    private boolean mIsFail;

    //是否清除画布
    private boolean isReset;

    //默认动画时长
    private static final int mDefaultDuration = 2000;

    //默认外圆画笔颜色
    private static final int mDefaultColor = Color.BLUE;

    //默认对号画笔颜色
    private static final int mDefaultSuccessColor = Color.BLUE;

    //默认叉号画笔颜色
    private static final int mDefaultFailColor = Color.RED;

    //默认画笔宽度
    private static final int mDefaultStrokeWidth = 10;

    //默认外圆宽高
    private static final int mDefaultWidth = 100;
    private static final int mDefaultHeight = 100;

    //外圆宽高
    private int mWidth;
    private int mHeight;

    //外圆半径
    private int circleRadius;

    //圆心坐标
    private float circleX;

    private float circleY;

    //动画帮助类
    private PathAnimHelper pathAnimHelper;

    public LoadingView(Context context) {
        super(context);
    }

    //设置attrs时系统会调用这个构造函数
    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mCirclePaintColor = typedArray.getColor(R.styleable.LoadingView_loading_circle_color, mDefaultColor);
        mSuccessPaintColor = typedArray.getColor(R.styleable.LoadingView_loading_success_color, mDefaultSuccessColor);
        mFailPaintColor = typedArray.getColor(R.styleable.LoadingView_loading_fail_color, mDefaultFailColor);
        mPaintWidth = typedArray.getDimensionPixelOffset(R.styleable.LoadingView_loading_stroke_width, mDefaultStrokeWidth);
        mDuration = typedArray.getInteger(R.styleable.LoadingView_loading_duration, mDefaultDuration);
        mWidth = typedArray.getDimensionPixelOffset(R.styleable.LoadingView_loading_width, mDefaultWidth);
        mHeight = typedArray.getDimensionPixelOffset(R.styleable.LoadingView_loading_height, mDefaultHeight);
        typedArray.recycle();

        mIsFail = false;
        mIsSuccess = false;
        initPaint();

        //上面的宽高已经经过换算为dp
        // mWidth = DensityUtil.dip2px(context, mWidth);
        // mHeight = DensityUtil.dip2px(context, mHeight);
        // circleRadius = mWidth / 2;
        //放到onLayout中确定圆心坐标后再初始化
        // initSourcePath();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCirclePaintColor);
        mCirclePaint.setStrokeWidth(mPaintWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mSuccessPaint = new Paint();
        mSuccessPaint.setAntiAlias(true);
        mSuccessPaint.setColor(mSuccessPaintColor);
        mSuccessPaint.setStrokeWidth(mPaintWidth);
        mSuccessPaint.setStyle(Paint.Style.STROKE);

        mFailPaint = new Paint();
        mFailPaint.setAntiAlias(true);
        mFailPaint.setColor(mFailPaintColor);
        mFailPaint.setStrokeWidth(mPaintWidth);
        mFailPaint.setStyle(Paint.Style.STROKE);
    }

    private void initSourcePath() {
        initCircleSourcePath();
        initSuccessSourcePath();
        initFailSourcePath();
        initPathAnimHelper();
    }

    /**
     * 初始化外圆源路径
     */
    private void initCircleSourcePath() {
        mCircleAnimPath = new Path();
        mCircleSourcePath = new Path();
        mCircleSourcePath.addCircle(circleX, circleY, circleRadius - mPaintWidth, Path.Direction.CW);
    }

    /**
     * 初始化加载成功路径
     */
    private void initSuccessSourcePath() {
        mSuccessAnimPath = new Path();
        mSuccessSourcePath = new Path();
        mSuccessSourcePath.moveTo(circleX - circleRadius / 2, circleY);
        mSuccessSourcePath.lineTo(circleX, circleY + circleRadius / 2);
        mSuccessSourcePath.lineTo(circleX + circleRadius / 2, circleY - circleRadius / 2 + mPaintWidth / 2);
    }

    /**
     * 初始化加载失败路径
     */
    private void initFailSourcePath() {
        mFailLeftAnimPath = new Path();
        mFailRightAnimPath = new Path();
        mFailLeftSourcePath = new Path();
        mFailRightSourcePath = new Path();
        mFailLeftSourcePath.moveTo(circleX - circleRadius / 2, circleY - circleRadius / 2 + mPaintWidth / 2);
        mFailLeftSourcePath.lineTo(circleX + circleRadius / 2, circleY + circleRadius / 2 + mPaintWidth / 2);
        mFailRightSourcePath.moveTo(circleX + circleRadius / 2, circleY - circleRadius / 2 + mPaintWidth / 2);
        mFailRightSourcePath.lineTo(circleX - circleRadius / 2, circleY + circleRadius / 2 + mPaintWidth / 2);
    }

    /**
     * 设置所有图像的源path
     *
     * @param circleSourcePath
     * @param successSourcePath
     * @param failLeftSourcePath
     * @param failRightSourcePath
     * @return
     */
    public LoadingView setSourcePath(Path circleSourcePath, Path successSourcePath,
                                     Path failLeftSourcePath, Path failRightSourcePath) {
        mCircleSourcePath = circleSourcePath;
        mSuccessSourcePath = successSourcePath;
        mFailLeftSourcePath = failLeftSourcePath;
        mFailRightSourcePath = failRightSourcePath;
        initPathAnimHelper();
        return this;
    }

    /**
     * 设置外圆画笔颜色
     *
     * @param color
     */
    public void setCirclePaintColor(int color) {
        mCirclePaint.setColor(color);
    }

    /**
     * 初始化Path动画工具类
     */
    private void initPathAnimHelper() {
        pathAnimHelper = getPathAnimHelper();
    }

    /**
     * 获取path动画工具类
     *
     * @return
     */
    private PathAnimHelper getPathAnimHelper() {
        return new PathAnimHelper(this, mCircleSourcePath, mCircleAnimPath,
                mSuccessSourcePath, mSuccessAnimPath, mFailLeftSourcePath, mFailLeftAnimPath,
                mFailRightSourcePath, mFailRightAnimPath, mDuration);
    }

    /**
     * 开始动画
     */
    public void startAnim() {
        pathAnimHelper.startAnim();
    }

    /**
     * 加载成功
     */
    public void success() {
        mIsSuccess = true;
        pathAnimHelper.loadingSuccess();
    }

    /**
     * 加载失败
     */
    public void fail() {
        mIsFail = true;
        setCirclePaintColor(mFailPaintColor);
        pathAnimHelper.loadingFail();
    }

    /**
     * 重置
     */
    public void reset() {
        isReset = true;
        mIsSuccess = false;
        mIsFail = false;
        setCirclePaintColor(mCirclePaintColor);
        //无需初始化,加上它会造成卡顿以及其他异常
        // initPathAnimHelper();
        pathAnimHelper.resetAnim(mDuration);
        //需要重绘一次界面
        postInvalidate();
    }

    /**
     * 清除画布
     *
     * @param canvas
     */
    private void clearPaint(Canvas canvas) {
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(mCirclePaint);
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mSuccessPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(mSuccessPaint);
        mSuccessPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        mFailPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(mFailPaint);
        mFailPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        circleRadius = getWidth() / 2;
        //圆心坐标是相对于view的而不是相对于整个屏幕，否则不显示
        if (circleX == 0 && circleY == 0) {
            circleX = (right - left) / 2;
            circleY = (bottom - top) / 2;
            initSourcePath();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReset) {
            isReset = false;
            clearPaint(canvas);
            initPaint();
        }
        canvas.drawPath(mCircleAnimPath, mCirclePaint);
        if (mIsSuccess && mSuccessAnimPath != null) {
            canvas.drawPath(mSuccessAnimPath, mSuccessPaint);
        }
        if (mIsFail && mFailLeftAnimPath != null && mFailRightAnimPath != null) {
            canvas.drawPath(mFailLeftAnimPath, mFailPaint);
            canvas.drawPath(mFailRightAnimPath, mFailPaint);
        }
    }
}
