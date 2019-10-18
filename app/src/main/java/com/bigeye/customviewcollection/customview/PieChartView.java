package com.bigeye.customviewcollection.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.bigeye.customviewcollection.data.PieData;
import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 */
public class PieChartView extends View {

  private Paint mPaint = new Paint();
  private RectF mRectF = new RectF();
  private List<PieData> mPieDatas = new ArrayList<>();
  private float mSumValue;

  public PieChartView(Context context) {
    super(context);
  }

  public PieChartView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setPieData(List<PieData> pieData){
    if (pieData.size()>0){
      mPieDatas = pieData;
      if (mPieDatas.size() > 0) {
        int sumValue = 0;
        for (PieData data : mPieDatas){
          sumValue += data.getValue();
        }
        mSumValue = sumValue;
      }
    }
  }


  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mRectF.set(0,0,getWidth(),getHeight());
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mPieDatas==null || mPieDatas.size()<=0){
      return;
    }
    float startArc = 0;
    float endArc=0;
    for (PieData data : mPieDatas){
      mPaint.setColor(data.getColor());
      endArc = (data.getValue()/mSumValue)*360f;
      mPaint.setColor(data.getColor());
      canvas.drawArc(mRectF,startArc,endArc,true,mPaint);
      startArc += endArc;
    }

  }

}
