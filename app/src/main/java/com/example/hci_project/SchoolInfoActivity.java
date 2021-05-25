package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hci_project.bean.School;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class SchoolInfoActivity extends AppCompatActivity {


    private TableLayout tableLayout,tableLayout2,tableLayout3;

    public class School{

        public String addr="서울특별시 광진구 뚝섬로52다길 17 (자양동)";
        public String name="착한어린이집";
        public String type="민간";
        public String postNum="05100";
        public String tel="02-453-3072";
        public int roomCnt=5;
        public int size=109;
        public int playgroundCnt=1;
        public int teacherCnt=9;
        public int maxStudentCnt=39;
        public int currentStudentCnt=19;
        public double lat=37.530254;
        public double lng=127.079926;
        public boolean isAvailableBus=false;
        public String homePage="";
        public String sinceDate="2005-05-10";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);
        ImageButton like_on = findViewById(R.id.like_added);
        ImageButton like_off = findViewById(R.id.like_added_not);
        ImageButton compare_add = findViewById(R.id.compare_add);
        ImageButton compare_delete = findViewById(R.id.compare_delete);
        TextView address = findViewById(R.id.address);
        TextView opentime = findViewById(R.id.opentime);
        TextView homepage = findViewById(R.id.homepage);
        TextView callnumber = findViewById(R.id.callnumber);
        TextView bus_number = findViewById(R.id.bus_number);
        TextView unyoung = findViewById(R.id.unyoung);
        TextView youngyang = findViewById(R.id.youngyang);
        TextView cctv = findViewById(R.id.cctv);

        School school = new School();

        like_on.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Like_off" , Toast.LENGTH_LONG).show();
                like_on.setVisibility(View.GONE);
                like_off.setVisibility(View.VISIBLE);
           //     Intent intent = new Intent(SchoolInfoActivity.this, BookmarkActivity.class);

            //    startActivity(intent);
            //    finish();
            }

        });

        like_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Like_on" , Toast.LENGTH_LONG).show();
                like_on.setVisibility(View.VISIBLE);
                like_off.setVisibility(View.GONE);
          //      Intent intent = new Intent(SchoolInfoActivity.this, BookmarkActivity.class);

           //     startActivity(intent);
           //     finish();
            }

        });

        compare_add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "compare_added" , Toast.LENGTH_LONG).show();
                compare_add.setVisibility(View.GONE);
                compare_delete.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
                intent.putExtra("last",'b');
                startActivity(intent);
                finish();
            }

        });
        compare_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "compare_deleted" , Toast.LENGTH_LONG).show();
                compare_add.setVisibility(View.VISIBLE);
                compare_delete.setVisibility(View.GONE);
                Intent intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
                intent.putExtra("last",'c');
                startActivity(intent);
                finish();
            }

        });

        ///////////////////////// 받아온 데이터를 여기에서 표시
        
        address.setText(String.valueOf(school.addr));
        opentime.setText(String.valueOf("0900~1800"));

        if(school.homePage=="")
            homepage.setText(String.valueOf("없음"));
        else
            homepage.setText(String.valueOf(school.homePage));
        
        
        callnumber.setText(String.valueOf(school.tel));

        if(school.isAvailableBus==false)
            bus_number.setText(String.valueOf("없음"));
        else
            bus_number.setText(String.valueOf("있음"));

        unyoung.setText(String.valueOf("직영"));
        youngyang.setText(String.valueOf("2"));
        cctv.setText(String.valueOf("3"));



        /////////////////////////////// Bar chart /////////////////////////////

        float a = (float)(school.teacherCnt/school.roomCnt);
        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        for(int i=1;i<=school.roomCnt;i++)
        {
            NoOfEmp2.add(new BarEntry(i, a));
        }
/*
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
 */
        BarDataSet bardataset = new BarDataSet(NoOfEmp2, "학급당 담당 교사수");
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
            if(i==0) {
                textView.setText(String.valueOf(school.roomCnt));
            }else
            {
                textView.setText(String.valueOf(school.currentStudentCnt));
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(36);
            tableRow.addView(textView);        // tableRow에 view 추가
        }
        tableLayout.addView(tableRow);        // tableLayout에 tableRow 추가



        tableLayout2 = (TableLayout) findViewById(R.id.tablelayout2);
        TableRow tableRow2 = new TableRow(this);     // tablerow 생성
        tableRow2.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < 2; i++) {
            TextView textView = new TextView(this);
            if(i==0) {
                textView.setText(String.valueOf(school.roomCnt));
            }else
            {
                textView.setText(String.valueOf(school.teacherCnt));
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(36);
            tableRow2.addView(textView);        // tableRow에 view 추가
        }
        tableLayout2.addView(tableRow2);        // tableLayout에 tableRow 추가




        tableLayout3 = (TableLayout) findViewById(R.id.tablelayout3);
        TableRow tableRow3 = new TableRow(this);     // tablerow 생성
        tableRow3.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            if(i==0) {
                textView.setText(String.valueOf("소방안전점검"));
            }else if(i==1)
            {
                textView.setText(String.valueOf("Y"));
            }
            else {
                textView.setText(String.valueOf("20210317"));
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            tableRow3.addView(textView);        // tableRow에 view 추가
        }
        tableLayout3.addView(tableRow3);        // tableLayout에 tableRow 추가



    }
}