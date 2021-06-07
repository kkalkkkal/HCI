package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hci_project.bean.School;
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
import com.google.android.gms.common.server.converter.StringToIntConverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class CompareSchoolActivity extends AppCompatActivity {


    School school_data;
    public String[] adress = new String[400];
    public int last = 5;
    Intent intent;
    char check = 'a';
    public int[] child_num = new int[1000];
    public int[] teacher_num = new int[1000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_school);

        final LinearLayout main = (LinearLayout) findViewById(R.id.l);
        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);
        final LinearLayout lm2 = (LinearLayout) findViewById(R.id.ll2);

        comeonDB(); //DB 불러오기
        intent = getIntent();
       // String check = intent.getExtras().getString("last");
        check = intent.getCharExtra("last", 'a');
        school_data = (School)intent.getSerializableExtra("school"); /*클래스*/

        if(check=='b')
        {
            last=last+1;
         //   Toast.makeText(getApplicationContext(), "last=last+1" , Toast.LENGTH_LONG).show();
        }else if(check=='c'){
            last=last-1;
        //    Toast.makeText(getApplicationContext(), "last=last-1" , Toast.LENGTH_LONG).show();
        }else{
        //    Toast.makeText(getApplicationContext(), "else check = "+check , Toast.LENGTH_LONG).show();
        }






        // linearLayout params 정의

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(

                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        // LinearLayout 생성
        LinearLayout ll = new LinearLayout(this);

        ll.setOrientation(LinearLayout.HORIZONTAL);


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

            if (j != last) {
                btn.setText(school_data.getName() + (j+1));
                if(j==0){
                    adress[j] = "05100";
                }else if(j==1){
                    adress[j] = "05015";
                }else if(j==2){
                    adress[j] = "04957";
                }else if(j==3){
                    adress[j] = "05112";
                }else if(j==4){
                    adress[j] = "04956";
                }

            }
            else {
                btn.setText("+");

            }

            if((j+1)%5==1){
                btn.setBackgroundResource(R.drawable.round_btn);
                btn.setTextColor(getApplication().getResources().getColor(R.color.white));
             //   btn.setBackgroundColor(Color.rgb(177, 53, 83));

             //   Toast.makeText(getApplicationContext(), "진입 "+j%5, Toast.LENGTH_LONG).show();
            }else if((j+1)%5==2){
                btn.setBackgroundResource(R.drawable.round_btn2);
                btn.setTextColor(getApplication().getResources().getColor(R.color.white));
               // btn.setBackgroundColor(Color.rgb(237, 112, 45));
            }else if((j+1)%5==3){
                btn.setBackgroundResource(R.drawable.round_btn3);
                btn.setTextColor(getApplication().getResources().getColor(R.color.white));
               // btn.setBackgroundColor(Color.rgb(238, 201, 67));
            }else if((j+1)%5==4){
                btn.setBackgroundResource(R.drawable.round_btn4);
                btn.setTextColor(getApplication().getResources().getColor(R.color.white));
               // btn.setBackgroundColor(Color.rgb(115, 149, 54));
            }else{
                btn.setBackgroundResource(R.drawable.round_btn5);
                btn.setTextColor(getApplication().getResources().getColor(R.color.white));
              //  btn.setBackgroundColor(Color.rgb(168, 104, 63));
            }

            btn.setLayoutParams(params);
            //버튼 add

            ll.addView(btn);

            final int position = j;

            int finalJ = j;
            btn.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {

                    Log.d("log", "position :" + position);
                    if (finalJ != last) {
                        Toast.makeText(getApplicationContext(), "클릭한 유치원번호 : " + position + " 우편번호 : " + adress[position] , Toast.LENGTH_LONG).show();


                    } else {
                        last=last+1;
                        Intent intent2 = new Intent(CompareSchoolActivity.this, CompareSchoolActivity.class);
                        intent2.putExtra("last",last);
                        startActivity(intent2);

                        Intent intent = new Intent(CompareSchoolActivity.this, SearchSchoolActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }

            });


            //버튼 add

            //  ll.addView(btn);

            //LinearLayout 정의된거 add


        }
        lm.addView(ll);


/////////////////////////////// PI chart ///////////////////////////// childhomedb 의 col 12

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();
        for(int i = 0 ; i < last ; i++)
        {
            NoOfEmp.add(new PieEntry(teacher_num[i], i));
        }

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "총 교사수");
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


        /////////////////////////////// Bar chart ///////////////////////////// childhomedb 의 col 14

        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        for(int i = 0 ; i < last ; i++)
        {
            NoOfEmp2.add(new BarEntry(i, child_num[i]));
        }

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
        BarDataSet bardataset = new BarDataSet(NoOfEmp2, "총 유아수");
        chart.animateY(5000);
        BarData data2 = new BarData(bardataset);
        // MPAndroidChart v3.X 오류 발생
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data2);


    }



    public void comeonDB() { // DB 불러오기
        try {
            InputStream is = getBaseContext().getResources().getAssets().open("kindergardenDB.xls"); // 유치원 현황
            InputStream is2 = getBaseContext().getResources().getAssets().open("childhomeDB.xls"); // 어린이집 현황
            Workbook wb = Workbook.getWorkbook(is);
            Workbook wb2 = Workbook.getWorkbook(is2);

            if(wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }
            }

            if(wb2 != null) {
                Sheet sheet = wb2.getSheet(0);   // 시트 불러오기
                if(sheet != null) {
                    int colTotal = sheet.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet.getColumn(colTotal-1).length;

                    StringBuilder sb2;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb2 = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            sb2.append("col"+col+" : "+contents+" , ");
                            if(col==12){
                                teacher_num[row-1]=Integer.parseInt(contents);
                                sb2.append("teacher_num[row] = "+teacher_num[row]+"  row = "+row+" success ");
                            }else if(col==14){
                                child_num[row-1]=Integer.parseInt(contents);
                                sb2.append("child_num[row] = "+child_num[row]+"  row = "+row+" success ");
                            }
                        }
                        Log.i("test", sb2.toString()); // 가져오는지 로그 확인
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

}

