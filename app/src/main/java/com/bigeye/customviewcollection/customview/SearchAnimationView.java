package com.bigeye.customviewcollection.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 眼神 on 2018/5/12.
 */

public class SearchAnimationView extends View {

    private float centerX,centerY;
    private Paint mPaint;

    Path path_search;
    private PathMeasure pathMeasure;

    private State currentState = State.SEARCHING;
    // 默认的动效周期 2s
    private int defaultDuration = 2000;
    private ValueAnimator searchAnimator,endingAnimator;
    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;
    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    // 用于控制动画状态转换
    private Handler mAnimatorHandler;
    // 判断是否已经搜索结束
    private boolean isOver = true;

    /**
     * 搜索的状态
     */
    public static enum State{
        NONE,
        SEARCHING,
        ENDING;
    }

    public SearchAnimationView(Context context) {
        super(context);
        initAll();
    }

    public SearchAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAll();
    }

    public SearchAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAll();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w/2;
        centerY = h/2;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 初始化
     */
    private void initAll(){
        initPaint();
        initPath();
        initListener();
        initHandler();
        initAnimation();
    }


    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }

    private void initPath() {
        path_search = new Path();
        pathMeasure = new PathMeasure();
        /**
         * 放大镜圆圈
         */
        RectF oval_search = new RectF(-50,-50,50,50);
        path_search.addArc(oval_search,45,359.9f);

        path_search.lineTo(80,80);

    }
    private void initListener(){
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //通知动画状态更新
                mAnimatorHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };
    }
    private void initHandler(){
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (currentState){
                    case SEARCHING:
                        isOver = false;
                        currentState = State.ENDING;
                        endingAnimator.start();
                        break;
                    case ENDING:
                        isOver = true;
                        currentState = State.NONE;
                        break;
                }
            }
        };

    }
    private void initAnimation() {
        searchAnimator = ValueAnimator.ofFloat(0,1).setDuration(defaultDuration);
        endingAnimator = ValueAnimator.ofFloat(1,0).setDuration(defaultDuration);

        searchAnimator.addUpdateListener(mUpdateListener);
        endingAnimator.addUpdateListener(mUpdateListener);

        searchAnimator.addListener(mAnimatorListener);
        endingAnimator.addListener(mAnimatorListener);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //居中
        canvas.translate(centerX,centerY);
        //根据当前的状态绘制
        switch (currentState){
            case NONE://不做操作
                canvas.drawPath(path_search,mPaint);
                break;
            case SEARCHING://搜索中
                pathMeasure.setPath(path_search,false);
                Path dst_search = new Path();
                pathMeasure.getSegment(pathMeasure.getLength()*mAnimatorValue,pathMeasure.getLength(),dst_search,true);
                canvas.drawPath(dst_search,mPaint);
                break;
            case ENDING://结束搜索
                pathMeasure.setPath(path_search,false);
                Path dst_end = new Path();
                pathMeasure.getSegment(pathMeasure.getLength()*mAnimatorValue,pathMeasure.getLength(),dst_end,true);
                canvas.drawPath(dst_end,mPaint);
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (isOver){
                    // 进入开始动画
                    currentState = State.SEARCHING;
                    searchAnimator.start();
                }
                break;
        }
        return true;
    }
}
