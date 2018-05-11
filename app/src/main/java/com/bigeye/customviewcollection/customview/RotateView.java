package com.bigeye.customviewcollection.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bigeye.customviewcollection.R;

/**
 * Created by 眼神 on 2018/5/11.
 */

public class RotateView extends View{
    private Paint paint = new Paint();
    private float currentValue = 0;//当前位置
    private float[] pos;//记录点的实际位置
    private float[] tan; //计算旋转的角度
    private Bitmap mBitmap; //旋转图片
    private Matrix mMatrix; //矩阵。对图片进行一些操作

    private int centerY;
    private int centerX;

    private Boolean isRun = true;

    public RotateView(Context context) {
        super(context);
        init();
    }

    public RotateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2 ;//缩放
        mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_collected_yellow,options);
        mMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w/2;
        centerY = h/2;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRotate(canvas);
    }

    private void drawRotate(Canvas canvas) {

        canvas.translate(centerX,centerY);
        Path path = new Path();
        path.addCircle(0,0,200,Path.Direction.CW);

        PathMeasure measure = new PathMeasure(path,false);

        //当前位置不断增加，如果超过1则从头开始
        currentValue += 0.005;
        if (currentValue>=1){
            currentValue = 0;
        }

        /**
         * 第一种方法
         */
//        measure.getPosTan(measure.getLength()*currentValue,pos,tan);
//        mMatrix.reset();
//        float degrees = (float) (Math.atan2(tan[1],tan[0])*180/Math.PI);
//        mMatrix.postRotate(degrees,mBitmap.getWidth()/2,mBitmap.getHeight()/2);
//        mMatrix.postTranslate(pos[0] - mBitmap.getWidth()/2,pos[1]-mBitmap.getHeight()/2);
        /**
         * 第二种方法 简写
         */
        measure.getMatrix(measure.getLength() * currentValue, mMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);   // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)

        canvas.drawPath(path,paint);
        canvas.drawBitmap(mBitmap,mMatrix,paint);
        if (isRun){
            invalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isRun = !isRun;
                break;

            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                new Thread(runnable).start();
                break;
        }
        return true;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isRun){
                postInvalidate();
            }
        }
    };
}
