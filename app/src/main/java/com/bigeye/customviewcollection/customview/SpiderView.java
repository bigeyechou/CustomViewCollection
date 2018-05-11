package com.bigeye.customviewcollection.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bigeye.customviewcollection.R;

/**
 * Created by 眼神 on 2018/5/8.
 * 蜘蛛网图 能力图
 */

public class SpiderView extends View {
    //边数
    private int count = 6;
    //圆周率 π*2/count
    private float angle = (float) (Math.PI*2/count);
    //最大半径
    private float radius;
    //中心X
    private int centerX;
    //中心Y
    private int centerY;
    //标题
    private String[] titles = {"能力1","能力2","能力3","能力4","能力5","能力6"};
    //各维度分支
    private double[] data = {80,40,60,60,75,66,100,60};
    //数据最大值
    private float maxValue = 100;
    //主画笔
    private Paint mainPaint;
    //数据填充画笔
    private Paint valuePaint;
    //文本画笔
    private Paint textPaint;


    public SpiderView(Context context) {
        super(context);
        init();
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setColor(getResources().getColor(R.color.black));
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        valuePaint.setColor(getResources().getColor(R.color.blue));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(getResources().getColor(R.color.red));
        textPaint.setTextSize(50);
    }

    /**
     * 测量后的长宽
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //最小一边半径 * 0.9
        radius = Math.min(h,w)/2 *0.7f;
        //确立中心点
        centerX = w/2;
        centerY = h/2;
        invalidate();//刷新绘制
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制多边形
        drawPolygon(canvas);
        //绘制直线
        drawLines(canvas);
        //绘制文字
        drawText(canvas);
        //绘制填充区域
        drawRegina(canvas);


    }

    /**
     * 绘制多边形
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        float distance = radius/(count - 1);//间距
        for (int i=1; i<count;i++){
            float curR = distance*i;//当前半径
            path.reset();
            for (int j=0;j<count;j++){
                if (j==0){
                    path.moveTo(centerX+curR ,centerY);
                }else {
                    //根据半径计算出多边形上的每个坐标点
                    float x = (float) (centerX + curR * Math.cos(angle * j));
                    float y = (float) (centerY + curR * Math.sin(angle * j));
                    path.lineTo(x,y);
                }
            }
            path.close();//闭合路径
            canvas.drawPath(path,mainPaint);
        }

    }

    /**
     * 绘制多边形中的直线
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i=0;i<count;i++){
            path.reset();
            path.moveTo(centerX,centerY);
            float x = (float) (centerX + radius*Math.cos(angle*i));
            float y = (float) (centerY + radius*Math.sin(angle*i));
            path.lineTo(x,y);
            canvas.drawPath(path,mainPaint);
        }
    }

    /**
     * 绘制文本
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i=0;i<count;i++){
            float x = (float) (centerX+(radius+fontHeight/2)*Math.cos(angle*i));
            float y = (float) (centerY+(radius+fontHeight/2)*Math.sin(angle*i));
            if(angle*i>=0&&angle*i<=Math.PI/2){//第4象限
                canvas.drawText(titles[i], x,y,textPaint);
            }else if(angle*i>=3*Math.PI/2&&angle*i<=Math.PI*2){//第3象限
                canvas.drawText(titles[i], x,y,textPaint);
            }else if(angle*i>Math.PI/2&&angle*i<=Math.PI){//第2象限
                float dis = textPaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i], x-dis,y,textPaint);
            }else if(angle*i>=Math.PI&&angle*i<3*Math.PI/2){//第1象限
                float dis = textPaint.measureText(titles[i]);//文本长度
                canvas.drawText(titles[i], x-dis,y,textPaint);
            }
        }
    }

    /**
     * 绘制能力区域
     */
    private void drawRegina(Canvas canvas) {
        Path path = new Path();
        valuePaint.setAlpha(255);
        for(int i=0;i<count;i++){
            double percent = data[i]/maxValue;
            float x = (float) (centerX+radius*Math.cos(angle*i)*percent);
            float y = (float) (centerY+radius*Math.sin(angle*i)*percent);
            if(i==0){
                path.moveTo(x, centerY);
            }else{
                path.lineTo(x,y);
            }
            //绘制小圆点
            canvas.drawCircle(x,y,10,valuePaint);
        }
        valuePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, valuePaint);
        valuePaint.setAlpha(127);
        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawPath(path, valuePaint);
    }

}
