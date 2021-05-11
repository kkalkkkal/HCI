package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SchoolInfoActivity extends AppCompatActivity {


    private TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);


        /////////////////////////////// Bar chart /////////////////////////////

        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        NoOfEmp2.add(new BarEntry(945f, 0));
        NoOfEmp2.add(new BarEntry(1040f, 1));
        NoOfEmp2.add(new BarEntry(1133f, 2));
        NoOfEmp2.add(new BarEntry(1240f, 3));
        NoOfEmp2.add(new BarEntry(1369f, 4));
        NoOfEmp2.add(new BarEntry(1487f, 5));
        NoOfEmp2.add(new BarEntry(1501f, 6));
        NoOfEmp2.add(new BarEntry(1645f, 7));
        NoOfEmp2.add(new BarEntry(1578f, 8));
        NoOfEmp2.add(new BarEntry(1695f, 9));
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


        ////////////////////////// 테이블 동적생성 ///////////////////////


        tableLayout = (TableLayout) findViewById(R.id.tablelayout);
        TableRow tableRow = new TableRow(this);     // tablerow 생성
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < 2; i++) {
            TextView textView = new TextView(this);
            textView.setText(String.valueOf(i));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(36);
            tableRow.addView(textView);        // tableRow에 view 추가
        }
        tableLayout.addView(tableRow);        // tableLayout에 tableRow 추가


    }
}