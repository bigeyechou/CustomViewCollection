package com.bigeye.customviewcollection.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bigeye.customviewcollection.R;

/**
 * Created by 眼神 on 2018/5/12.
 * 渐变的TextView
 *
 */

public class ShadeTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint = new Paint();
    private int mTextViewWidth;
    private LinearGradient mLinearGradient;//线性渐变
    private Matrix mMatrix;
    private int mTranslation;//平移

    public ShadeTextView(Context context) {
        super(context);
    }

    public ShadeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mTextViewWidth == 0){
            mTextViewWidth = getMeasuredWidth();
            if (mTextViewWidth > 0){
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(0,
                        0,
                        mTextViewWidth,
                        0,
                        new int[]{getResources().getColor(R.color.deeppink),getResources().getColor(R.color.lavenderblush)},
                        null,
                        Shader.TileMode.MIRROR);
                mPaint.setShader(mLinearGradient);
                mMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMatrix != null){
            mTranslation += mTextViewWidth/10;
//            if (mTranslation > 2*mTextViewWidth){
//                mTranslation -= mTextViewWidth;
//            }
            mMatrix.setTranslate(mTranslation,0);
            mLinearGradient.setLocalMatrix(mMatrix);
            postInvalidateDelayed(100);
        }
    }
}
