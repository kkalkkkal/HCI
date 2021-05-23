package com.example.hci_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.map.MapFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;


import java.io.IOException;
import java.io.InputStream;

// 엑셀 파일 불러오기 라이브러리
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    private TextView schoolSearchTv;
    private ViewPager2 viewPager;
    private List<Fragment> viewPagerFragmentList = new ArrayList<>();

    //데이터베이스
    static Sheet sheet0; // 어린이집 기본 현황
    static Sheet sheet1; // 유치원 보험가입 현황
    static Sheet sheet2; // 유치원 공제회 가입 현황
    static Sheet sheet3; // 유치원 안전점검 및 안전점검 실시 현황
    static Sheet sheet4; // 유치원 환경 위생 관리 현황
    static Sheet sheet5; // 유치원 근속 연수 현황
    static Sheet sheet6; // 유치원 통학 차량 현황
    static Sheet sheet7; // 유치원 수업 일수 현황
    static Sheet sheet8; // 유치원 직위 자격별 교직원 현황
    static Sheet sheet9; // 유치원 교실 면적 현황
    static Sheet sheet10; // 유치원 기본 현황
    static Sheet sheet11; // 유치원 건물 현황

    static final int PERMISSIONS_REQUEST = 0x0000001;
    
    
    // Getter
    public static Sheet getSheet0() {
        return sheet0;
    }

    public static Sheet getSheet1() {
        return sheet1;
    }

    public static Sheet getSheet2() {
        return sheet2;
    }

    public static Sheet getSheet3() {
        return sheet3;
    }

    public static Sheet getSheet4() {
        return sheet4;
    }

    public static Sheet getSheet5() {
        return sheet5;
    }

    public static Sheet getSheet6() {
        return sheet6;
    }

    public static Sheet getSheet7() {
        return sheet7;
    }

    public static Sheet getSheet8() {
        return sheet8;
    }

    public static Sheet getSheet9() {
        return sheet9;
    }

    public static Sheet getSheet10() {
        return sheet10;
    }

    public static Sheet getSheet11() {
        return sheet11;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comeonDB();

        schoolSearchTv= findViewById(R.id.search_school_tv);
        viewPager= findViewById(R.id.main_viewPager);

        viewPagerFragmentList.add(new MapFragment());
        viewPagerFragmentList.add(new CompareSchoolFragment());
        viewPagerFragmentList.add(new BookmarkActivity());

        initUI();
        OnCheckPermission();
    }

    // 처음 UI 설정
    private void initUI(){
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @NotNull
            @Override
            public Fragment createFragment(int position) {
                return viewPagerFragmentList.get(position);
            }

            @Override
            public int getItemCount() {
                return viewPagerFragmentList.size();
            }
        });
        viewPager.setOffscreenPageLimit(viewPagerFragmentList.size());
        viewPager.setUserInputEnabled(false);

        schoolSearchTv.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchSchoolActivity.class)));

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            schoolSearchTv.setVisibility(View.GONE);

            switch (item.getTitle().toString()){
                case "주변":
                    schoolSearchTv.setVisibility(View.VISIBLE);
                    viewPager.setCurrentItem(0);
                    break;
                case "비교":
                    viewPager.setCurrentItem(1);
                    break;
                case "즐겨찾기":
                    viewPager.setCurrentItem(2);
                    break;
            }
            return true;
        });
    }

    private long backButtonClickTime= 0;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(System.currentTimeMillis()- backButtonClickTime>= 1000){
            Toast.makeText(this, "앱을 종료하시려면 뒤로가기 버튼을 한번 더 누르세요", Toast.LENGTH_SHORT).show();
            backButtonClickTime= System.currentTimeMillis();
        }else{
            finish();
        }
    }

    public void comeonDB() { // DB 불러오기
        try {
            InputStream is = getBaseContext().getResources().getAssets().open("kindergardenDB.xls"); // 유치원 현황
            InputStream is2 = getBaseContext().getResources().getAssets().open("childhomeDB.xls"); // 어린이집 현황
            Workbook wb = Workbook.getWorkbook(is);
            Workbook wb2 = Workbook.getWorkbook(is2);

            if(wb != null) {
                sheet1 = wb.getSheet(0);   // 0번 째  '유치원 보험가입 현황' 시트 불러오기
                if(sheet1 != null) {
                    int colTotal = sheet1.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet1.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet1.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet2 = wb.getSheet(1);   // 1번 째 시트 '유치원 공제회 가입 현황' 불러오기
                if(sheet2 != null) {
                    int colTotal = sheet2.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet2.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet2.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet3 = wb.getSheet(2);   // 2번 째 시트 '유치원 안전점검 및 안전점검 실시 현황' 불러오기
                if(sheet3 != null) {
                    int colTotal = sheet3.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet3.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet3.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }

                sheet4 = wb.getSheet(3);   // 3번 째 시트 '유치원 환경 위생 관리 현황' 불러오기
                if(sheet4 != null) {
                    int colTotal = sheet4.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet4.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet4.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet5 = wb.getSheet(4);   // 4번 째 시트 '유치원 근속 연수 현황'불러오기
                if(sheet5 != null) {
                    int colTotal = sheet5.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet5.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet5.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet6 = wb.getSheet(5);   // 5번 째 시트 '유치원 통학 차량 현황' 불러오기
                if(sheet6 != null) {
                    int colTotal = sheet6.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet6.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet6.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet7 = wb.getSheet(6);   // 6번 째 시트 '유치원 직위 자격별 교직원 현황' 불러오기
                if(sheet7 != null) {
                    int colTotal = sheet7.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet7.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet7.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet8 = wb.getSheet(7);   // 7번 째 시트 '유치원 직위 자격별 교직원 현황' 불러오기
                if(sheet8 != null) {
                    int colTotal = sheet8.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet8.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet8.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet9 = wb.getSheet(8);   // 8번 째 시트 '유치원 교실 면적 현황' 불러오기
                if(sheet9 != null) {
                    int colTotal = sheet9.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet9.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet9.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet10 = wb.getSheet(9);   // 9번 째 시트 '유치원 기본 현황' 불러오기
                if(sheet10 != null) {
                    int colTotal = sheet10.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet10.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet10.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }


                sheet11 = wb.getSheet(3);   // 10번 째 시트 '유치원 건물 현황' 불러오기
                if(sheet11 != null) {
                    int colTotal = sheet11.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet11.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet11.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }




            }

            if(wb2 != null) {
                Sheet sheet0 = wb2.getSheet(0);   // 어린이집 시트 불러오기
                if(sheet0 != null) {
                    int colTotal = sheet0.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet0.getColumn(colTotal-1).length;

                    StringBuilder sb2;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb2 = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet0.getCell(col, row).getContents();
                            sb2.append("col"+col+" : "+contents+" , ");
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

    //위치 권한 설정
    public void OnCheckPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED

                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},

                        PERMISSIONS_REQUEST);

            } else {

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},

                        PERMISSIONS_REQUEST);

            }
        }
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST :

                if (grantResults.length > 0

                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_LONG).show();

                }

                break;

        }
    }

}