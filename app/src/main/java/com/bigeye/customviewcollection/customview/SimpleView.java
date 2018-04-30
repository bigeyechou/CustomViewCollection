package com.bigeye.customviewcollection.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bigeye.customviewcollection.R;

/**
 * Created by 眼神 on 2018/4/30.
 * 简单的图形：矩形、圆形、弧形，
 */

public class SimpleView extends View {
    /**
     * 实例化一个画笔；ANTI_ALIAS_FLAG抗锯齿
     */
    private Paint paint_rect = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_circle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_arc1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_arc2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_bitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SimpleView(Context context) {
        super(context);
        initPaint();
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public SimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    @SuppressLint("ResourceAsColor")
    private void initPaint() {
        paint_rect.setColor(getResources().getColor(android.R.color.holo_blue_light));
        paint_circle.setColor(getResources().getColor(android.R.color.holo_red_light));
        paint_arc1.setColor(getResources().getColor(android.R.color.holo_green_light));
        paint_arc2.setColor(getResources().getColor(android.R.color.holo_green_dark));

        paint_bitmap.setColor(R.color.colorAccent);

        paint_text.setColor(R.color.colorPrimary);
        paint_text.setTextSize(90);
        paint_text.setTextAlign(Paint.Align.CENTER);//设置文字画笔的基准为center
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置测量规格
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    /**
     * 测量宽度
     */
    private int measureWidth(int widthMeasureSpec){
        int result;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){//精准模式
            result = specSize;
        }else {
            result = 300;//给宽度一个默认值
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }
    /**
     * 测量高度
     */
    private int measureHeight(int heightMeasureSpec){
        int result;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画图形
         */
        drawRect(canvas);
        drawCircle(canvas);
        drawArc(canvas);
        /**
         * 画bitmap
         */
        drawBitmap(canvas);
        /**
         * 画TextView
         */
        drawTextView(canvas);
    }

    /**
     * 画矩形
     */
    private void drawRect(Canvas canvas) {
        float left = getLeft();
        float right = getRight();
        float top = getTop();
        float bottom = getBottom();
        //给定上下左右位置
        canvas.drawRect(left,top,right,bottom,paint_rect);
    }
    /**
     * 画圆形
     */
    private void drawCircle(Canvas canvas) {
        float width = getWidth();//圆心的X坐标
        float height = getHeight();//圆心的y坐标
        float radius = Math.min(width,height)/2;//半径
        canvas.drawCircle(width/2,height/2,radius,paint_circle);
    }
    /**
     * 画弧形
     */
    @SuppressLint("NewApi")
    private void drawArc(Canvas canvas) {
        int witch = getWidth();
        int height = getHeight();
        RectF rect = new RectF(0f,0f,witch,height);
        //有圆心的弧形（扇形）true
        canvas.drawArc(rect,0,60,true,paint_arc1);
        //无圆心的弧形 false
        canvas.drawArc(rect,60,60,false,paint_arc2);
        //另一种写法，sdk版本21以上的方法(坐标点左上右下、起始位置、扫过的角度，是否需要焦点，画笔)
        canvas.drawArc(0f,0f,witch,height,120,60,true,paint_arc1);
    }

    /**
     * 画bitmap
     */
    private void drawBitmap(Canvas canvas) {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_collected_yellow);
        float width = (getWidth() - mBitmap.getWidth())/2;
        float height = (getHeight() - mBitmap.getHeight())/2;
        //left和top确定绘制的位置
        canvas.drawBitmap(mBitmap, width, height, paint_circle);
    }

    /**
     * 画TextView
     */
    private void drawTextView(Canvas canvas) {
        Paint.FontMetrics fontMetrics = paint_text.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;

        int witch = getWidth();
        int height = getHeight();
        RectF rect = new RectF(0f,0f,witch,height);
        //文字基准线
        int baseLineY = (int) (rect.centerY() - top/2 - bottom/2);
        canvas.drawText("Hello   View", rect.centerX(), baseLineY, paint_text);
    }


}
