package com.bigeye.customviewcollection.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bigeye.customviewcollection.R;
import com.bigeye.customviewcollection.data.PieData;

import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 */
public class PieChartView extends View {

    private Paint paintChart, paintText, paintLine, paintMasking;
    private RectF mRectF = new RectF();
    //饼状图的数据（如：name、value数值、颜色等）
    private List<PieData> mPieDatas = new ArrayList<>();
    //总数，方便算百分比
    private float mSumValue;
    //半径
    private int radius;
    //直径
    private int diameter;
    //中心坐标
    private int centerX, centerY;
    //间隔的距离
    private float space;

    private boolean showTip;
    private int tipSize;
    private boolean openAnimation;
    private ValueAnimator mAnimator;

    // 默认的动效周期 2s
    private int defaultDuration = 1000;
    //动画的执行的百分比mAnimatorValue
    private float mAnimatorValue = 1f;

    public PieChartView(Context context) {
        super(context);
        initAll();
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChartView, defStyleAttr, 0);
        diameter = typedArray.getDimensionPixelSize(R.styleable.PieChartView_pie_size, 0);
        space = typedArray.getFloat(R.styleable.PieChartView_pie_space, 0f);
        openAnimation = typedArray.getBoolean(R.styleable.PieChartView_open_animation, false);
        showTip = typedArray.getBoolean(R.styleable.PieChartView_show_tip, false);
        tipSize = showTip ? 100 : 0;//如果显示tip，则预留出tip控件的间距
        typedArray.recycle();
        initAll();
    }

    private void initAll() {
        initPain();
        if (openAnimation) {
            startAnimation();
        }
    }

    private void initPain() {
        paintChart = new Paint();
        paintChart.setAntiAlias(true);
        paintChart.setColor(Color.BLACK);

        paintMasking = new Paint();
        paintMasking.setColor(Color.WHITE);
        paintMasking.setStyle(Paint.Style.FILL);
        paintMasking.setAntiAlias(true);

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);
        paintLine.setAntiAlias(true);

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setStrokeWidth(20);
        paintText.setTextSize(30);
    }

    /**
     * 设置数据源
     *
     * @param pieData 饼状图的数据
     */
    public void setPieData(List<PieData> pieData) {
        if (pieData.size() > 0) {
            mPieDatas = pieData;
            if (mPieDatas.size() > 0) {
                int sumValue = 0;
                for (PieData data : mPieDatas) {
                    sumValue += data.getValue();
                }
                mSumValue = sumValue;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置尺寸，没有设置pie_size或者设置的size + 预留给tip控件的距离>=最小边时不使用xml里设置的直径尺寸
        if (diameter <= 0 || diameter + tipSize >= getWidth() || diameter + tipSize >= getHeight()) {
            diameter = (getWidth() > getHeight() ? getHeight() : getWidth()) - tipSize;
        }
        radius = diameter / 2;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    private float startAngle = -90;
    private float sweepAngle;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPieDatas == null || mPieDatas.size() <= 0) {
            canvas.drawArc(mRectF, 0, 360 - space, true, paintChart);
            canvas.drawCircle(centerX, centerY, radius >> 2, paintMasking);
            return;
        }
        for (PieData data : mPieDatas) {
            paintChart.setColor(data.getColor());
            paintLine.setColor(data.getColor());
            sweepAngle = (data.getValue() / mSumValue) * 360f;
            canvas.drawArc(mRectF, startAngle, sweepAngle * mAnimatorValue - space, true, paintChart);
            if (showTip) {
                //是否画标签说明的小部件
                drawTip(canvas, data);
            }
            startAngle += sweepAngle;
        }
        canvas.drawCircle(centerX, centerY, radius >> 2, paintMasking);
    }

    /**
     * 画tip控件
     */
    private void drawTip(Canvas canvas, PieData data) {
        Path pathLine = new Path();
        PathMeasure measure = new PathMeasure();

        float textPadding = 10;
        float textHigh = (paintText.getFontMetrics().bottom - paintText.getFontMetrics().top) / 2;
        float padding = 20;
        float dotSize = 10;
        float dotX = (float) (centerX + (radius + padding) * Math.cos(Math.toRadians((startAngle + sweepAngle / 2))));
        float dotY = (float) (centerY + (radius + padding) * Math.sin(Math.toRadians((startAngle + sweepAngle / 2))));

        float lineX = (float) (centerX + (radius + padding * 2) * Math.cos(Math.toRadians((startAngle + sweepAngle / 2))));
        float lineY = (float) (centerY + (radius + padding * 2) * Math.sin(Math.toRadians((startAngle + sweepAngle / 2))));
        canvas.drawCircle(dotX, dotY, dotSize, paintChart);

        if (dotX > centerX) {
            //右侧
            pathLine.moveTo(dotX, dotY);
            pathLine.lineTo(lineX, lineY);
            pathLine.lineTo(getRight(), lineY);
            measure.setPath(pathLine, false);
            Path dst = new Path();
            measure.getSegment(0, measure.getLength() * mAnimatorValue, dst, true);
            canvas.drawPath(dst, paintLine);

            if (mAnimatorValue == 1) {
                paintText.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText((data.getValue() / mSumValue) * 100 + "%", getWidth() - textPadding, lineY - textPadding, paintText);
                canvas.drawText(data.getName(), getWidth() - textPadding, lineY + textHigh + textPadding, paintText);
            }
        } else {
            //左侧
            measure = new PathMeasure();
            pathLine.moveTo(dotX, dotY);
            pathLine.lineTo(lineX, lineY);
            pathLine.lineTo(0, lineY);
            measure.setPath(pathLine, false);
            Path dst = new Path();
            measure.getSegment(0, measure.getLength() * mAnimatorValue, dst, true);
            canvas.drawPath(dst, paintLine);

            if (mAnimatorValue == 1) {
                paintText.setTextAlign(Paint.Align.LEFT);
                canvas.drawText((data.getValue() / mSumValue) * 100 + "%", 0 + textPadding, lineY - textPadding, paintText);
                canvas.drawText(data.getName(), 0 + textPadding, lineY + textHigh + textPadding, paintText);
            }
        }
    }

    /**
     * 开启动画
     */
    private void startAnimation() {
        mAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.start();
    }

}
