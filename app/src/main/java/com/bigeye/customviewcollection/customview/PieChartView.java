package com.bigeye.customviewcollection.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.renderscript.Sampler;
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

  private Paint paintChart, paintText, paintMasking;
  private RectF mRectF = new RectF();
  private List<PieData> mPieDatas = new ArrayList<>();
  private float mSumValue;

  public PieChartView(Context context) {
    super(context);
    initAll();
  }

  public PieChartView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    initAll();
  }

  public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAll();
  }

  private void initAll() {
    initPain();
  }

  private void initPain() {
    paintChart = new Paint();

    paintMasking = new Paint();
    paintMasking.setColor(Color.WHITE);
    paintMasking.setStyle(Paint.Style.FILL);

    paintText = new Paint();
    paintText.setColor(Color.BLACK);
    paintText.setStyle(Paint.Style.FILL);
    paintText.setStrokeWidth(8);
    paintText.setTextSize(20);
  }

  public void setPieData(List<PieData> pieData) {
    if (pieData.size() > 0) {
      mPieDatas = pieData;
      if (mPieDatas.size() > 0) {
        int sumValue = 0;
        for (PieData data : mPieDatas) {
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
    mRectF.set(0, 0, getWidth(), getHeight());
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mPieDatas == null || mPieDatas.size() <= 0) {
      return;
    }
    float startAngle = 0;
    float sweepAngle = 0;
    for (PieData data : mPieDatas) {
      paintChart.setColor(data.getColor());
      sweepAngle = (data.getValue() / mSumValue) * 360f;
      canvas.drawArc(mRectF, startAngle, sweepAngle - 3f, true, paintChart);
      startAngle += sweepAngle;
    }
    canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 4, paintMasking);
  }

}
