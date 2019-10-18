package com.bigeye.customviewcollection.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.ViewGroup;
import com.bigeye.customviewcollection.R;
import com.bigeye.customviewcollection.customview.PieChartView;
import com.bigeye.customviewcollection.data.PieData;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PieChartView pieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        pieChartView = findViewById(R.id.pie_char);
        List<PieData> datas = new ArrayList<>();
        PieData data1 = new PieData();
        data1.setValue(10);
        data1.setColor(Color.RED);

        PieData data2 = new PieData();
        data2.setValue(15);
        data2.setColor(Color.GREEN);

        PieData data3 = new PieData();
        data3.setValue(17);
        data3.setColor(Color.BLACK);

        PieData data4 = new PieData();
        data4.setValue(12);
        data4.setColor(Color.YELLOW);
        datas.add(data1);
        datas.add(data2);
        datas.add(data3);
        datas.add(data4);

        pieChartView.setPieData(datas);
    }
}
