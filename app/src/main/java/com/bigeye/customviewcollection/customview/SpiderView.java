package com.bigeye.customviewcollection.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 眼神 on 2018/5/8.
 * 蜘蛛网图 能力图
 */

public class SpiderView extends View {
    /**
     * 画笔（抗锯齿）
     */
    private Paint mPain = new Paint(Paint.ANTI_ALIAS_FLAG);
    //边数
    private int count = 6;
    //圆周率
    private float angle = (float) (Math.PI*2/count);

    private float radius;

    private int centerX;

    private int centerY;

    //标题
    private String[] titles;
    private double[] data = {100,60,60,60,100,50,10,20};
    private float maxValue = 100;
    private Paint mainPaint;
    private Paint valuePaint;
    private Paint textPaint;


    public SpiderView(Context context) {
        super(context);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
