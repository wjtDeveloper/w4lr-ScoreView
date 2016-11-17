package com.w4lr.scoreview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * 得分柱状图控件
 * Created by w4lr on 2016/10/10.
 */

public class ScoreView extends View {

    private Context mContext;

    private Paint mPaint;

    /**
     * 每个得分段的得分人数
     */
    private int[] mScoreCount;

    /**
     * 控件宽度
     */
    private float mWidth;

    /**
     * 控件高度
     */
    private float mHeight;

    /**
     * 每一个人数代表的柱状图高度
     */
    private float mEveryHeight;

    /**
     * 字体高度
     */
    private int mTextHeight;

    /**
     * 控件内边距
     */
    private int mPadding;

    /**
     * 得分段的显示文字
     */
    public String[] mDescTexts;

    private int[] colors = {Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.YELLOW, Color.BLACK};

    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPadding = SizeUtils.dp2px(mContext,18);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Random random = new Random();
        mPaint.setColor(colors[random.nextInt(6)]);
        mPaint.setAntiAlias(true);
        int strokeWidth = SizeUtils.dp2px(mContext, 5);
        mPaint.setStrokeWidth(strokeWidth);

        mTextHeight = SizeUtils.px2sp(mContext, (mWidth -  mPadding - strokeWidth) / 13 );

        //绘制轴
        canvas.drawLine(mPadding, mHeight - 2 * mPadding - mTextHeight,
                mWidth - mPadding, mHeight - 2 * mPadding - mTextHeight, mPaint);
        //绘制y轴
        canvas.drawLine(mPadding, mHeight - 2 * mPadding - mTextHeight,
                mPadding, mPadding, mPaint);


        //计算每个得分柱的高度
        calcEveryHeight();
        float[] heights = new float[mScoreCount.length];
        for (int i = 0; i < heights.length; i++) {
            heights[i] = mScoreCount[i] * mEveryHeight;
        }
        //计算每个得分柱的宽度
        float everyWidth = (mWidth - 2 * mPadding - strokeWidth) / (mScoreCount.length * 2 + 1);
        //绘制得分柱与文字
        for (int i = 0; i < mScoreCount.length; i++) {
            //右下角在x轴的偏移量
            float rb = (i + 1) * 2 * everyWidth;
            //左下角在x轴的偏移量
            float lb = rb - everyWidth;
            int left = (int) (mPadding + strokeWidth + lb);
            int right = (int) (left + everyWidth);
            int bottom = (int) (mHeight - 2 * mPadding - strokeWidth -mTextHeight);
            int top = (int) (bottom - heights[i]);
            Rect rect = new Rect(left, top, right, bottom);
            mPaint.setColor(colors[i % colors.length]);
            mPaint.setTextSize(mTextHeight);
            canvas.drawRect(rect, mPaint);
            mPaint.setColor(Color.BLACK);
            //绘制每个柱子上面的人数
            canvas.drawText(String.valueOf(mScoreCount[i]),left + everyWidth / 2,top - mTextHeight / 2,mPaint);
            //绘制每个柱子下面的的得分段
            canvas.drawText(mDescTexts[i],left + everyWidth / 2,mHeight - mPadding - mTextHeight ,mPaint);
        }

    }

    /**
     * 设置每个得分段的人数
     *
     * @param scoreCount
     */
    public void setScoreCount(int[] scoreCount) {
        mScoreCount = scoreCount;
    }

    /**
     * 设置描述信息
     * @param descTexts
     */
    public void setDescTexts(String[] descTexts) {
        mDescTexts = descTexts;
    }

    /**
     * 计算每一个人数表示的柱状图的高度
     */
    private void calcEveryHeight() {

        if (mScoreCount.length != mDescTexts.length) {
            throw new RuntimeException("scores length must be euqals to description texts length");
        }

        int max = 1;
        //计算人数最多的一组的人数
        for (int i = 0; i < mScoreCount.length; i++) {
            if (mScoreCount[i] > max) {
                max = mScoreCount[i];
            }
        }
        mEveryHeight = (mHeight - 4 * mPadding - mTextHeight) / max;
    }
}
