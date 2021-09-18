package com.bigeye.customviewcollection.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import com.bigeye.customviewcollection.R;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.btn_scratch_card).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this,ScratchCardActivity.class));
      }
    });
    findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this,SearchActivity.class));
      }
    });
    findViewById(R.id.btn_shade_text).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this,ShadeTextActivity.class));
      }
    });
    findViewById(R.id.btn_pie_char).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this,PieChartActivity.class));
      }
    });
  }

}
