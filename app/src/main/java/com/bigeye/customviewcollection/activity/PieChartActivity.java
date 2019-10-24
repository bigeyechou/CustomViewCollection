package com.bigeye.customviewcollection.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.bigeye.customviewcollection.R;
import com.bigeye.customviewcollection.customview.PieChartView;
import com.bigeye.customviewcollection.data.PieData;
import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

  private PieChartView pieChartView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_piechart_layout);
    initView();
  }

  private void initView() {
    pieChartView = findViewById(R.id.pie_char);
    List<PieData> datas = new ArrayList<>();
    PieData data1 = new PieData();
    data1.setName("红");
    data1.setValue(10);
    data1.setColor(getResources().getColor(R.color.crimson));

    PieData data2 = new PieData();
    data2.setName("绿");
    data2.setValue(15);
    data2.setColor(getResources().getColor(R.color.forestgreen));

    PieData data3 = new PieData();
    data3.setName("篮");
    data3.setValue(17);
    data3.setColor(getResources().getColor(R.color.deepskyblue));

    PieData data4 = new PieData();
    data4.setValue(12);
    data4.setName("黄");
    data4.setColor(getResources().getColor(R.color.darkorange));
    datas.add(data1);
    datas.add(data2);
    datas.add(data3);
    datas.add(data4);

    pieChartView.setPieData(datas);
  }

}
