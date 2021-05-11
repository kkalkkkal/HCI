package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;


public class CompareSchoolActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_school);

        final LinearLayout main = (LinearLayout) findViewById(R.id.l);
        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);
        final LinearLayout lm2 = (LinearLayout) findViewById(R.id.ll2);


        // linearLayout params 정의

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(

                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        // LinearLayout 생성
        LinearLayout ll = new LinearLayout(this);

        ll.setOrientation(LinearLayout.HORIZONTAL);
        int last = 5;

        for (int j = 0; j <= last; j++) {

/*
            // TextView 생성

            TextView tvProdc = new TextView(this);

            tvProdc.setText("Name" + j + " ");

            ll.addView(tvProdc);



            // TextView 생성

            TextView tvAge = new TextView(this);

            tvAge.setText("   Age" + j + "  ");

            ll.addView(tvAge);

*/

            // 버튼 생성

            final Button btn = new Button(this);

            // setId 버튼에 대한 키값

            btn.setId(j + 1);
            if (j != last)
                btn.setText("유치원" + j);
            else
                btn.setText("+");

            btn.setLayoutParams(params);
            //버튼 add

            ll.addView(btn);

            final int position = j;


            int finalJ = j;
            btn.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    Log.d("log", "position :" + position);
                    if (finalJ != last) {
                        Toast.makeText(getApplicationContext(), "클릭한 유치원번호:" + position, Toast.LENGTH_LONG).show();


                    } else {
                        Intent intent = new Intent(CompareSchoolActivity.this, SearchSchoolActivity.class);
                        startActivity(intent);
                    }


                }

            });


            //버튼 add

            //  ll.addView(btn);

            //LinearLayout 정의된거 add


        }
        lm.addView(ll);


/////////////////////////////// PI chart /////////////////////////////

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();
        NoOfEmp.add(new PieEntry(945f, 0));
        NoOfEmp.add(new PieEntry(1040f, 1));
        NoOfEmp.add(new PieEntry(1133f, 2));
        NoOfEmp.add(new PieEntry(1240f, 3));
        NoOfEmp.add(new PieEntry(1369f, 4));
        NoOfEmp.add(new PieEntry(1487f, 5));
        NoOfEmp.add(new PieEntry(1501f, 6));
        NoOfEmp.add(new PieEntry(1645f, 7));
        NoOfEmp.add(new PieEntry(1578f, 8));
        NoOfEmp.add(new PieEntry(1695f, 9));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Employees");
        ArrayList year = new ArrayList();
        year.add("2008");
        year.add("2009");
        year.add("2010");
        year.add("2011");
        year.add("2012");
        year.add("2013");
        year.add("2014");
        year.add("2015");
        year.add("2016");
        year.add("2017");
        PieData data = new PieData(dataSet);
        // MPAndroidChart v3.X 오류 발생
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);


        /////////////////////////////// Bar chart /////////////////////////////

        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        NoOfEmp2.add(new BarEntry(0,945f));
        NoOfEmp2.add(new BarEntry(1,145f));
        NoOfEmp2.add(new BarEntry(2,345f));
        NoOfEmp2.add(new BarEntry(3,645f));
        NoOfEmp2.add(new BarEntry(4,925f));
        NoOfEmp2.add(new BarEntry(5,905f));
        NoOfEmp2.add(new BarEntry(6,345f));
        ArrayList year2 = new ArrayList();
        year2.add("2008");
        year2.add("2009");
        year2.add("2010");
        year2.add("2011");
        year2.add("2012");
        year2.add("2013");
        year2.add("2014");
        year2.add("2015");
        year2.add("2016");
        year2.add("2017");
        BarDataSet bardataset = new BarDataSet(NoOfEmp2, "No Of Employee");
        chart.animateY(5000);
        BarData data2 = new BarData(bardataset);
        // MPAndroidChart v3.X 오류 발생
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data2);


    }


}

