package com.bigeye.customviewcollection.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bigeye.customviewcollection.R;

/**
 * Created by 眼神 on 2018/5/1.
 * 刮刮卡的view，相关技术点Xfermode，PorterDuff
 */

public class ScratchCardView extends View{
    //绘制范围
    private Rect mBound = new Rect();
    //绘制的paint
    private Paint track_paint = new Paint();
    //绘制字体的paint
    private Paint text_paint = new Paint();
    //绘制的路径
    private Path track_path = new Path();
    //内存中创建的Canvas
    private Canvas mCanvas;
    //canvas在上面绘制内容
    private Bitmap mBitmap;

    private int track_lastX;
    private int track_lastY;

    private String text_content = "恭喜您中奖啦";

    //显示文字是1，图片是2；默认显示文字
    private final static int SHOW_TEXT =1;
    private final static int SHOW_BITMAP =2;
    private int showType = 1;
    //是否完全显示
    private Boolean isComplete = false;

    public ScratchCardView(Context context) {
        super(context);
    }

    public ScratchCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScratchCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //初始化Bitmap
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        setTrackPaint();
        setTextPaint();
    }

    private void setTextPaint(){
        text_paint.setColor(Color.BLACK);
        text_paint.setTextSize(40);
        text_paint.setStyle(Paint.Style.FILL);
        text_paint.setTextScaleX(2f);
        text_paint.getTextBounds(text_content,0,text_content.length(),mBound);
    }

    private void setTrackPaint() {
        track_paint.setColor(Color.RED);
        track_paint.setAntiAlias(true);//抗锯齿
        track_paint.setDither(true);//防抖动
        track_paint.setStyle(Paint.Style.STROKE);//仅描边
        track_paint.setStrokeJoin(Paint.Join.ROUND);//线段连接处样式：圆弧
        track_paint.setStrokeCap(Paint.Cap.ROUND);//圆形线帽
        track_paint.setStrokeWidth(50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (showType == SHOW_TEXT){
            canvas.drawText(text_content,getWidth()/2 - mBound.width()/2,getHeight()/2 + mBound.height()/2,text_paint);
        }else {
            Bitmap mBackBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_lu);
            canvas.drawBitmap(mBackBitmap, 0, 0, null);
        }
        if (!isComplete){
            drawPath();
            canvas.drawBitmap(mBitmap,0,0,null);
        }
    }

    private void drawPath() {
        track_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(track_path,track_paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                //按下屏幕的点为起始点
                track_lastX = x;
                track_lastY = y;
                track_path.moveTo(track_lastX,track_lastY);
                break;

            case MotionEvent.ACTION_MOVE:
                //算出移动的距离，如果小于规定的值不算移动
                int dx = Math.abs(x - track_lastX);
                int dy = Math.abs(y - track_lastY);
                if (dx>3 || dy>3){
                    track_path.lineTo(x,y);
                }
                //invalidate不断绘制界面时不断更改起始点的值
                track_lastX = x;
                track_lastY = y;
                break;

            case MotionEvent.ACTION_UP:
                new Thread(mRunnable).start();
                break;
        }
        invalidate();
        return true;
    }

    private Runnable mRunnable = new Runnable() {
        private int[] mPixels;
        @Override
        public void run() {
            int width = getWidth();
            int height = getHeight();
            float wipeArea = 0;
            float totalArea = width * height;
            Bitmap bitmap = mBitmap;
            mPixels = new int[width * height];
            /**
             * 拿到bitmap全部的像素信息
             */
            bitmap.getPixels(mPixels,0,width,0,0,width,height);
            for (int i= 0; i< width ;i++){
                for (int j = 0; j< height; j++){
                    int index = i + j * width;
                    if (mPixels[index] == 0)
                    {
                        wipeArea++;
                    }
                }
            }
            /**
             * 根据所占百分比，进行一些操作
             */
            if (wipeArea > 0 && totalArea > 0)
            {
                int percent = (int) (wipeArea * 100 / totalArea);
                Log.e("TAG", percent + "");

                if (percent > 60)
                {
                    isComplete = true;
                    postInvalidate();
                }
            }
        }
    };
}
