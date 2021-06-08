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

import java.io.File;
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
    public int last = 0;
    Intent intent;
    char check = 'a';
    public int[] child_num = new int[1000];
    public int[] teacher_num = new int[1000];
    Workbook workbook2;
    Sheet sheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_school);

        final LinearLayout main = (LinearLayout) findViewById(R.id.l);
        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);
        final LinearLayout lm2 = (LinearLayout) findViewById(R.id.ll2);


        //intent = getIntent();
        //check = intent.getCharExtra("last", 'a');

        // UserDB.xls 불러오기
        try {
            File dir = new File(getApplicationContext().getFilesDir().toString());
            File f = new File(dir, "UserDB.xls");

            if (!f.exists()) {
                f.createNewFile();
                workbook2 = Workbook.getWorkbook(getApplicationContext().getAssets().open("UserDB.xls"));
                //throw new Exception("file not found");
            } else {
                f = new File(String.valueOf(getApplicationContext().getFileStreamPath("UserDB.xls")));

                    workbook2 = Workbook.getWorkbook(f);

            }

            if (!f.canRead()) {
                throw new Exception("can't read file");
            }

            sheet = workbook2.getSheet(0);

            last = sheet.getRows();


        } catch (IOException e){
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // linearLayout params 정의
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        // LinearLayout 생성
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);


        for (int j = 1; j <= last; j++) {

            // 버튼 생성

            final Button btn = new Button(this);

            // setId 버튼에 대한 키값

            btn.setId(j);

            if (j != last) {
                btn.setText(sheet.getCell(1,j).getContents());
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

                // Todo : 고쳐야함.
                public void onClick(View v) {

                    Log.d("log", "position :" + position);
                    if (finalJ != last) {
                        Toast.makeText(getApplicationContext(), "클릭한 유치원번호 : " + position , Toast.LENGTH_LONG).show();


                    } else {

                    }


                }

            });

        }
        lm.addView(ll);


/////////////////////////////// PI chart ///////////////////////////// childhomedb 의 col 12

        PieChart pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> NoOfEmp = new ArrayList<>();
        for(int i = 1 ; i < last ; i++)
        {
            NoOfEmp.add(new PieEntry(Integer.parseInt(sheet.getCell(2,i).getContents()), i));
        }

        int[] colorArray = new int[] {Color.rgb(235,119,56), Color.rgb(236,202,76),
                Color.rgb(120,153,64), Color.rgb(171,111,72), Color.rgb(179,64,90) };
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "교사당 학생 수");
        PieData data = new PieData(dataSet);
        // MPAndroidChart v3.X 오류 발생
        pieChart.setData(data);
        data.setValueTextSize(30);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(false);
        data.setDrawValues(true);
        pieChart.setCenterText("교사당 학생 수");
        pieChart.setCenterTextSize(25);
        dataSet.setColors(colorArray);
        pieChart.invalidate();


        /////////////////////////////// Bar chart ///////////////////////////// childhomedb 의 col 14

        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        for(int i = 1 ; i < last ; i++)
        {
            if (sheet.getCell(8,i).getContents().contains("-"))
                NoOfEmp2.add(new BarEntry(i, 0));
            else
                NoOfEmp2.add(new BarEntry(i, Integer.parseInt(sheet.getCell(8,i).getContents())));
        }


        BarDataSet bardataset = new BarDataSet(NoOfEmp2, "CCTV 개수");
        BarData data2 = new BarData(bardataset);
        // MPAndroidChart v3.X 오류 발생
        bardataset.setColors(colorArray);

        data2.setDrawValues(true);
        data2.setValueTextSize(20);
        chart.setData(data2);


    }




}

