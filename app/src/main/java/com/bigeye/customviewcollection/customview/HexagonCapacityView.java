package com.bigeye.customviewcollection.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bigeye.customviewcollection.R;

/**
 * Created by 眼神 on 2018/4/30.
 * 六边形的能力图
 */

public class HexagonCapacityView extends View{
    /**
     * 实例化；六边形边框和能力线的画笔
     */
    private Paint paint_border = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint_line = new Paint(Paint.ANTI_ALIAS_FLAG);

    public HexagonCapacityView(Context context) {
        super(context);
        initPaint();
    }

    public HexagonCapacityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public HexagonCapacityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }
    private float mPhase=5;

    @SuppressLint("ResourceAsColor")
    private void initPaint() {
        paint_border.setColor(R.color.darkviolet);
        paint_border.setStyle(Paint.Style.STROKE);
        paint_border.setStrokeWidth(10);
        paint_border.setPathEffect(new DashPathEffect(new float[]{20,10},5.0f));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specModel = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specModel == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 200;
            if (specModel == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result;
        int specModel = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specModel == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 200;
            if (specModel == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBorder(canvas);
    }

    /**
     * 画五边形边框
     */
    private void drawBorder(Canvas canvas) {
        Path borderPath = new Path();
        int width = getWidth()/4;
        int height = getHeight()/4;
        borderPath.moveTo(0,height);
        borderPath.lineTo(2*width,0);
        borderPath.lineTo(4*width,height);
        borderPath.lineTo(4*width,3*height);
        borderPath.lineTo(2*width,4*height);
        borderPath.lineTo(0,3*height);
        borderPath.lineTo(0,height);
        canvas.drawPath(borderPath,paint_border);
    }
}
