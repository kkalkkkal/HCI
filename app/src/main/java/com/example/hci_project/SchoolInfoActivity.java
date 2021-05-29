package com.example.hci_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.example.hci_project.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SchoolInfoActivity extends AppCompatActivity {


    //데이터베이스
    static Sheet sheet0; // 어린이집 기본 현황
    static Sheet sheet1; // 유치원 보험가입 현황
    static Sheet sheet2; // 유치원 공제회 가입 현황
    static Sheet sheet3; // 유치원 안전점검 및 안전점검 실시 현황
    static Sheet sheet4; // 유치원 환경 위생 관리 현황
    static Sheet sheet5; // 유치원 근속 연수 현황
    static Sheet sheet6; // 유치원 통학 차량 현황
    static Sheet sheet7; // 유치원 급식 운영 현황
    static Sheet sheet8; // 유치원 수업 일수 현황
    static Sheet sheet9; // 유치원 직위 자격별 교직원 현황
    static Sheet sheet10; // 유치원 교실 면적 현황
    static Sheet sheet11; // 유치원 기본 현황
    static Sheet sheet12; // 유치원 건물 현황
    static final int PERMISSIONS_REQUEST = 0x0000001;
    private TableLayout tableLayout, tableLayout2, tableLayout3;
    static MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_info);
        ImageButton like_on = findViewById(R.id.like_added);
        ImageButton like_off = findViewById(R.id.like_added_not);
        ImageButton compare_add = findViewById(R.id.compare_add);
        ImageButton compare_delete = findViewById(R.id.compare_delete);
        TextView school_name = findViewById(R.id.school_name);
        TextView address = findViewById(R.id.address);
        TextView opentime = findViewById(R.id.opentime);
        TextView homepage = findViewById(R.id.homepage);
        TextView callnumber = findViewById(R.id.callnumber);
        TextView bus_number = findViewById(R.id.bus_number);
        TextView unyoung = findViewById(R.id.unyoung);
        TextView youngyang = findViewById(R.id.youngyang);
        TextView cctv = findViewById(R.id.cctv);

        TextView hak_count = findViewById(R.id.hak_count);
        TextView ua_count = findViewById(R.id.ua_count);
        TextView hak_count2 = findViewById(R.id.hak_count2);
        TextView gyo_count = findViewById(R.id.gyo_count);

        comeonDB();










        School school = (School) getIntent().getSerializableExtra("school");


        if (school == null) {
            //invalid access
            finish();
            return;
        }   //school is not null… usable

        school_name.setText(school.getName());




        like_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Like_off", Toast.LENGTH_LONG).show();
                like_on.setVisibility(View.GONE);
                like_off.setVisibility(View.VISIBLE);
                //     Intent intent = new Intent(SchoolInfoActivity.this, BookmarkActivity.class);

                //    startActivity(intent);
                //    finish();
            }

        });

        like_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Like_on", Toast.LENGTH_LONG).show();
                like_on.setVisibility(View.VISIBLE);
                like_off.setVisibility(View.GONE);
                //      Intent intent = new Intent(SchoolInfoActivity.this, BookmarkActivity.class);

                //     startActivity(intent);
                //     finish();
            }

        });

        compare_add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "compare_added", Toast.LENGTH_LONG).show();
                compare_add.setVisibility(View.GONE);
                compare_delete.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
                intent.putExtra("last", 'b');
                startActivity(intent);
                finish();
            }

        });
        compare_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "compare_deleted", Toast.LENGTH_LONG).show();
                compare_add.setVisibility(View.VISIBLE);
                compare_delete.setVisibility(View.GONE);
                Intent intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
                intent.putExtra("last", 'c');
                startActivity(intent);
                finish();
            }

        });

        ///////////////////////// 받아온 데이터를 여기에서 표시

        address.setText(String.valueOf(school.getAddr()));
        opentime.setText(String.valueOf("0900~1800"));

        if (school.getHomePage() == "")
            homepage.setText(String.valueOf("없음"));
        else
            homepage.setText(String.valueOf(school.getHomePage()));


        callnumber.setText(String.valueOf(school.getTel()));

        if (school.isAvailableBus() == false)
            bus_number.setText(String.valueOf("없음"));
        else
            bus_number.setText(String.valueOf("있음"));


        if (school.getHomePage() == "")
            unyoung.setText(String.valueOf(school.getType()));
        else
            homepage.setText(String.valueOf(school.getHomePage()));
        unyoung.setText(String.valueOf("직영"));
        youngyang.setText(String.valueOf("2"));
        cctv.setText(String.valueOf("3"));


        /////////////////////////////// Bar chart /////////////////////////////

        float a = (float) (school.getTeacherCnt() / school.getRoomCnt());
        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        for (int i = 1; i <= school.getRoomCnt(); i++) {
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


            if (i == 0) {
                hak_count.setText(String.valueOf(school.getRoomCnt()));
            } else {
                ua_count.setText(String.valueOf(school.getRoomCnt()));
            }

        }


        tableLayout2 = (TableLayout) findViewById(R.id.tablelayout2);
        TableRow tableRow2 = new TableRow(this);     // tablerow 생성
        tableRow2.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        for (int i = 0; i < 2; i++) {

            if (i == 0) {
                hak_count2.setText(String.valueOf(school.getRoomCnt()));
            } else {
                gyo_count.setText(String.valueOf(school.getTeacherCnt()));
            }
        }


        tableLayout3 = (TableLayout) findViewById(R.id.tablelayout3);
        TableRow tableRow3 = new TableRow(this);     // tablerow 생성
        tableRow3.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            if (i == 0) {
                textView.setText(String.valueOf("소방안전점검"));
            } else if (i == 1) {
                textView.setText(String.valueOf("Y"));
            } else {
                textView.setText(String.valueOf("20210317"));
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            tableRow3.addView(textView);        // tableRow에 view 추가
        }
        tableLayout3.addView(tableRow3);        // tableLayout에 tableRow 추가


    }





    public void comeonDB() { // DB 불러오기
        try {
            InputStream is = getBaseContext().getResources().getAssets().open("kindergardenDB.xls"); // 유치원 현황
            InputStream is2 = getBaseContext().getResources().getAssets().open("childhomeDB.xls"); // 어린이집 현황
            Workbook wb = Workbook.getWorkbook(is);
            Workbook wb2 = Workbook.getWorkbook(is2);

            if (wb != null) {
                sheet1 = wb.getSheet(0);   // 0번 째  '유치원 보험가입 현황' 시트 불러오기
                if (sheet1 != null) {
                    int colTotal = sheet1.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet1.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet1.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet2 = wb.getSheet(1);   // 1번 째 시트 '유치원 공제회 가입 현황' 불러오기
                if (sheet2 != null) {
                    int colTotal = sheet2.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet2.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet2.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet3 = wb.getSheet(2);   // 2번 째 시트 '유치원 안전점검 및 안전점검 실시 현황' 불러오기
                if (sheet3 != null) {
                    int colTotal = sheet3.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet3.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet3.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet4 = wb.getSheet(3);   // 3번 째 시트 '유치원 환경 위생 관리 현황' 불러오기
                if (sheet4 != null) {
                    int colTotal = sheet4.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet4.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet4.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet5 = wb.getSheet(4);   // 4번 째 시트 '유치원 근속 연수 현황'불러오기
                if (sheet5 != null) {
                    int colTotal = sheet5.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet5.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet5.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet6 = wb.getSheet(5);   // 5번 째 시트 '유치원 통학 차량 현황' 불러오기
                if (sheet6 != null) {
                    int colTotal = sheet6.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet6.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet6.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet7 = wb.getSheet(6);   // 6번 째 시트 '급식 운영 현황' 불러오기
                if (sheet7 != null) {
                    int colTotal = sheet7.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet7.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet7.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet8 = wb.getSheet(7);   // 7번 째 시트 '유치원 수업일수 현황'
                if (sheet8 != null) {
                    int colTotal = sheet8.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet8.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet8.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet9 = wb.getSheet(8);   // 8번 째 시트 '유치원 직위 자격별 교직원 현황' 불러오기
                if (sheet9 != null) {
                    int colTotal = sheet9.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet9.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet9.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet10 = wb.getSheet(9);   // 9번 째 시트 '유치원 교실 면적 현황' 불러오기
                if (sheet10 != null) {
                    int colTotal = sheet10.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet10.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet10.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet11 = wb.getSheet(10);   // 10번 째 시트 '유치원 기본 현황' 불러오기
                if (sheet11 != null) {
                    int colTotal = sheet11.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet11.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet11.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet12 = wb.getSheet(11);   // 11번 째 시트 '유치원 건물 현황' 불러오기
                if (sheet12 != null) {
                    int colTotal = sheet12.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet12.getColumn(colTotal - 1).length;

                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet12.getCell(col, row).getContents();
                            sb.append("col" + col + " : " + contents + " , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }
            }

            if (wb2 != null) {
                sheet0 = wb2.getSheet(0);   // 어린이집 시트 불러오기
                if (sheet0 != null) {
                    int colTotal = sheet0.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet0.getColumn(colTotal - 1).length;

                    StringBuilder sb2;
                    for (int row = rowIndexStart; row < rowTotal; row++) {
                        sb2 = new StringBuilder();
                        for (int col = 0; col < colTotal; col++) {
                            String contents = sheet0.getCell(col, row).getContents();
                            sb2.append("col" + col + " : " + contents + " , ");
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