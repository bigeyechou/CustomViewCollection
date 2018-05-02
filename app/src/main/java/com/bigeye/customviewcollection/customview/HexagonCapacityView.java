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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Paint paint_text = new Paint(Paint.ANTI_ALIAS_FLAG);

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

    @SuppressLint("ResourceAsColor")
    private void initPaint() {
        paint_border.setColor(R.color.darkviolet);
        paint_border.setStyle(Paint.Style.STROKE);
        paint_border.setStrokeWidth(10);
        paint_border.setPathEffect(new DashPathEffect(new float[]{20,10},5.0f));

        paint_line.setColor(R.color.yellow);
        paint_line.setStrokeWidth(10);

        paint_text.setColor(R.color.red);
        paint_text.setTextSize(90);
        paint_text.setTextAlign(Paint.Align.CENTER);
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
        int width = getWidth();
        int height = getHeight();
        drawText(canvas,width,height);
        drawBorder(canvas,width,height);
        drawCapacityLine(canvas,width,height);
    }

    /**
     * 能力名称
     * @param canvas
     */
    private void drawText(Canvas canvas,int width,int height) {
        canvas.drawText("一",0,height/4,paint_text);
        canvas.drawText("二",width/2,0,paint_text);
        canvas.drawText("三",width,height/4,paint_text);
        canvas.drawText("四",width,(height/4)*3,paint_text);
        canvas.drawText("五",width/2,height,paint_text);
        canvas.drawText("六",0,(height/4)*3,paint_text);
    }

    /**
     * 画五边形边框
     */
    private void drawBorder(Canvas canvas,int width,int height) {
        Path borderPath = new Path();
        int b_width = width/4;
        int b_height = height/4;
        borderPath.moveTo(0,b_height);
        borderPath.lineTo(2*b_width,0);
        borderPath.lineTo(4*b_width,b_height);
        borderPath.lineTo(4*b_width,3*b_height);
        borderPath.lineTo(2*b_width,4*b_height);
        borderPath.lineTo(0,3*b_height);
        borderPath.close();//闭合，链接起始点
        canvas.drawPath(borderPath,paint_border);
    }

    /**
     * 画能力图
     */
    private void drawCapacityLine(Canvas canvas,int width,int height) {
        Path linePath = new Path();
        int min_x = width/8;
        int min_y = height/16;
        int[] one_x = {4*min_x,3*min_x,2*min_x,1*min_x,0};
        int[] one_y = {8*min_y,7*min_y,6*min_y,5*min_y,4*min_y};

        int two_x = 4*min_x;
        int[] two_y = {8*min_y,6*min_y,4*min_y,2*min_y,0};

        int[] three_x = {4*min_x,5*min_x,6*min_x,7*min_x,8*min_x};
        int[] three_y = {8*min_y,7*min_y,6*min_y,5*min_y,4*min_y};

        int[] four_x = {4*min_x,5*min_x,6*min_x,7*min_x,8*min_x};
        int[] four_y = {8*min_y,9*min_y,10*min_y,11*min_y,12*min_y};

        int five_x = 4*min_x;
        int[] five_y = {8*min_y,10*min_y,12*min_y,14*min_y,16*min_y};

        int[] six_x = {4*min_x,3*min_x,2*min_x,1*min_x,0};
        int[] six_y = {8*min_y,9*min_y,10*min_y,11*min_y,12*min_y};


        /**
         * 设立对应关系（第一个能力：等级1的纵横坐标）
         */
        linePath.moveTo(one_x[4],one_y[4]);
        linePath.lineTo(two_x,two_y[3]);
        linePath.lineTo(three_x[1],three_y[1]);
        linePath.lineTo(four_x[2],four_y[2]);
        linePath.lineTo(five_x,five_y[4]);
        linePath.lineTo(six_x[3],six_y[3]);
        linePath.close();
        canvas.drawPath(linePath,paint_line);
    }

}
