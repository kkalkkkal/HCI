package com.example.hci_project;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hci_project.bean.School;
import com.example.hci_project.bean.School2;
import com.example.hci_project.bean.SchoolManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationSource;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import org.jetbrains.annotations.NotNull;

// Json 파싱
import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;


import java.io.IOException;
import java.io.InputStream;

// 엑셀 파일 불러오기 라이브러리
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;

import static com.example.hci_project.MapFragment.naverMap2;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView schoolSearchTv;
    private ViewPager2 viewPager;
    private List<Fragment> viewPagerFragmentList = new ArrayList<>();
    public RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private LinearLayout linearLayout2;
    private TextView schoolTitle;
    private MapView mapView;

    // 메인 간이 필터
    private Chip kinder_chip; // 유치원 필터
    private Chip child_chip; // 어린이집 필터


    // 네이버 자기 위치 디폴트 코드
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public FusedLocationSource locationSource;
    static public NaverMap naverMap; // 네이버 맵 객체 - 다른 곳에서도 접근 가능하게 하려고 일단 전역변수화함.

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

    private ArrayList<School2> dataList = new ArrayList<>();

    static final int PERMISSIONS_REQUEST = 0x0000001;


    // 마커 선언
    // 마커는 최대 10개
    Marker marker1 = new Marker();
    Marker marker2 = new Marker();
    Marker marker3 = new Marker();
    Marker marker4 = new Marker();
    Marker marker5 = new Marker();
    Marker marker6 = new Marker();
    Marker marker7 = new Marker();
    Marker marker8 = new Marker();
    Marker marker9 = new Marker();
    Marker marker10 = new Marker();
    Marker marker11 = new Marker();
    Marker marker12 = new Marker();
    Marker marker13 = new Marker();
    Marker marker14 = new Marker();
    Marker marker15 = new Marker();
    Marker marker16 = new Marker();
    Marker marker17 = new Marker();
    Marker marker18 = new Marker();
    Marker marker19 = new Marker();
    Marker marker20 = new Marker();

    // Getter

    public Marker getMarker1() {
        return marker1;
    }

    public void setMarker1(Marker marker1) {
        this.marker1 = marker1;
    }

    public Marker getMarker2() {
        return marker2;
    }

    public void setMarker2(Marker marker2) {
        this.marker2 = marker2;
    }

    public Marker getMarker3() {
        return marker3;
    }

    public void setMarker3(Marker marker3) {
        this.marker3 = marker3;
    }

    public Marker getMarker4() {
        return marker4;
    }

    public void setMarker4(Marker marker4) {
        this.marker4 = marker4;
    }

    public Marker getMarker5() {
        return marker5;
    }

    public void setMarker5(Marker marker5) {
        this.marker5 = marker5;
    }

    public Marker getMarker6() {
        return marker6;
    }

    public void setMarker6(Marker marker6) {
        this.marker6 = marker6;
    }

    public Marker getMarker7() {
        return marker7;
    }

    public void setMarker7(Marker marker7) {
        this.marker7 = marker7;
    }

    public Marker getMarker8() {
        return marker8;
    }

    public void setMarker8(Marker marker8) {
        this.marker8 = marker8;
    }

    public Marker getMarker9() {
        return marker9;
    }

    public void setMarker9(Marker marker9) {
        this.marker9 = marker9;
    }

    public Marker getMarker10() {
        return marker10;
    }

    public void setMarker10(Marker marker10) {
        this.marker10 = marker10;
    }

    public Marker getMarker11() {
        return marker11;
    }

    public void setMarker11(Marker marker11) {
        this.marker11 = marker11;
    }

    public Marker getMarker12() {
        return marker12;
    }

    public void setMarker12(Marker marker12) {
        this.marker12 = marker12;
    }

    public Marker getMarker13() {
        return marker13;
    }

    public void setMarker13(Marker marker13) {
        this.marker13 = marker13;
    }

    public Marker getMarker14() {
        return marker14;
    }

    public void setMarker14(Marker marker14) {
        this.marker14 = marker14;
    }

    public Marker getMarker15() {
        return marker15;
    }

    public void setMarker15(Marker marker15) {
        this.marker15 = marker15;
    }

    public Marker getMarker16() {
        return marker16;
    }

    public void setMarker16(Marker marker16) {
        this.marker16 = marker16;
    }

    public Marker getMarker17() {
        return marker17;
    }

    public void setMarker17(Marker marker17) {
        this.marker17 = marker17;
    }

    public Marker getMarker18() {
        return marker18;
    }

    public void setMarker18(Marker marker18) {
        this.marker18 = marker18;
    }

    public Marker getMarker19() {
        return marker19;
    }

    public void setMarker19(Marker marker19) {
        this.marker19 = marker19;
    }

    public Marker getMarker20() {
        return marker20;
    }

    public void setMarker20(Marker marker20) {
        this.marker20 = marker20;
    }

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

    public static Sheet getSheet12() {
        return sheet12;
    }

    static InfoWindow infoWindow = new InfoWindow();

    static MapFragment mapFragment;

    // 카메라
    CameraUpdate cameraUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnCheckPermission(); // 권한 확인


        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); // 현재 위치 갱신

        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) { // 정보창
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag(); // 태그에 적힌 값이 출력됨.
            }
        });

        //System.out.println("hello");
        //String add = getAddress_DAUM("서울특별시 광진구 아차산로44길 26"); Test
        //System.out.println(add);

        // 네이버 지도 설정
        NaverMapOptions options = new NaverMapOptions() // 초기 화면 위치, 경도 생성자 설정
                .camera(new CameraPosition(new LatLng(37.543344020789625, 127.07557079824849), 13)) // 건국대 기준으로 지도를 연다.
                .mapType(NaverMap.MapType.Basic);

        options.locationButtonEnabled(true).tiltGesturesEnabled(false);

        mapFragment = MapFragment.newInstance(options); // 옵션 설정
        mapFragment.getMapAsync(this);



        comeonDB(); // DB 가져오기


        kinder_chip = findViewById(R.id.kinder_chips);
        child_chip = findViewById(R.id.child_chips);
        schoolSearchTv= findViewById(R.id.search_school_tv);
        viewPager= findViewById(R.id.main_viewPager);
        linearLayout = findViewById(R.id.dragView);
        linearLayout2 = findViewById(R.id.common_result);
        schoolTitle = findViewById(R.id.commonTitle);


        viewPagerFragmentList.add(new com.example.hci_project.MapFragment()); // 커스텀 맵 프래그먼트 : 기존 new MapFragment 대체
        viewPagerFragmentList.add(new CompareSchoolFragment());
        viewPagerFragmentList.add(new BookmarkActivity());


        initUI(); // UI 초기화

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



        kinder_chip.setChecked(false); // 처음에는 체크 X
        child_chip.setChecked(false);

        kinder_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kinder_chip.isChecked()) { // 체크되면 True
                    try {
                        marker11.setMap(null); // 기존 어린이집 마커 삭제
                        marker12.setMap(null); // 기존 마커 삭제
                        marker13.setMap(null); // 기존 마커 삭제
                        marker14.setMap(null); // 기존 마커 삭제
                        marker15.setMap(null); // 기존 마커 삭제
                        marker16.setMap(null); // 기존 마커 삭제
                        marker17.setMap(null); // 기존 마커 삭제
                        marker18.setMap(null); // 기존 마커 삭제
                        marker19.setMap(null); // 기존 마커 삭제
                        marker20.setMap(null); // 기존 마커 삭제
                        child_chip.setChecked(false);
                        //dataList.clear();
                        NearKinderMarker();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try { // 전체 출력
                        //dataList.clear();
                        NearKinderMarker();
                        NearChildMarker();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        child_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (child_chip.isChecked()) {
                    //dataList.clear();
                    try {
                        marker1.setMap(null); // 기존 유치원 마커 삭제
                        marker2.setMap(null); // 기존 마커 삭제
                        marker3.setMap(null); // 기존 마커 삭제
                        marker4.setMap(null); // 기존 마커 삭제
                        marker5.setMap(null); // 기존 마커 삭제
                        marker6.setMap(null); // 기존 마커 삭제
                        marker7.setMap(null); // 기존 마커 삭제
                        marker8.setMap(null); // 기존 마커 삭제
                        marker9.setMap(null); // 기존 마커 삭제
                        marker10.setMap(null); // 기존 마커 삭제
                        kinder_chip.setChecked(false); // 체크 X
                        NearChildMarker();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {

                        NearKinderMarker();
                        NearChildMarker();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            schoolSearchTv.setVisibility(View.GONE);

            switch (item.getTitle().toString()){
                case "주변":
                    kinder_chip.setVisibility(View.VISIBLE);
                    child_chip.setVisibility(View.VISIBLE);
                    schoolSearchTv.setVisibility(View.VISIBLE);

                    viewPager.setCurrentItem(0);
                    break;
                case "비교":
                    kinder_chip.setVisibility(View.INVISIBLE);
                    child_chip.setVisibility(View.INVISIBLE);


                    viewPager.setCurrentItem(1);
                    break;
                case "즐겨찾기":
                    kinder_chip.setVisibility(View.INVISIBLE);
                    child_chip.setVisibility(View.INVISIBLE);
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


                sheet7 = wb.getSheet(6);   // 6번 째 시트 '급식 운영 현황' 불러오기
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


                sheet8 = wb.getSheet(7);   // 7번 째 시트 '유치원 수업일수 현황'
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


                sheet9 = wb.getSheet(8);   // 8번 째 시트 '유치원 직위 자격별 교직원 현황' 불러오기
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


                sheet10 = wb.getSheet(9);   // 9번 째 시트 '유치원 교실 면적 현황' 불러오기
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


                sheet11 = wb.getSheet(10);   // 10번 째 시트 '유치원 기본 현황' 불러오기
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

                sheet12 = wb.getSheet(11);   // 11번 째 시트 '유치원 건물 현황' 불러오기
                if(sheet12 != null) {
                    int colTotal = sheet12.getColumns();    // 전체 컬럼
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    int rowTotal = sheet12.getColumn(colTotal-1).length;

                    StringBuilder sb;
                    for(int row=rowIndexStart;row<rowTotal;row++) {
                        sb = new StringBuilder();
                        for(int col=0;col<colTotal;col++) {
                            String contents = sheet12.getCell(col, row).getContents();
                            sb.append("col"+col+" : "+contents+" , ");
                        }
                        Log.i("test", sb.toString()); // 가져오는지 로그 확인
                    }
                }
            }

            if(wb2 != null) {
                sheet0 = wb2.getSheet(0);   // 어린이집 시트 불러오기
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

        // 네이버 지도 소스 권한
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }

        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);


    }

    @UiThread
    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) { // 맵 설정
        this.naverMap = naverMap;

        naverMap.setLocationSource(locationSource); // 자기 위치 설정 
        naverMap.setMapType(NaverMap.MapType.Basic); // 기본형 지도
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true); // 빌딩 그룹 생성

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); // 위치 추적 모드 실행


        //네이버 지도 추가 UI 설정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true); // 현 위치 버튼 활성화

/*
        // 마커 표시하기 (자기 위치 기준 검색)
        marker1 = new Marker();
        marker1.setMap(null); // 기존 마커 삭제
        marker1.setPosition(new LatLng(37.541680773674464, 127.07943250328056)); // 그냥 건국대 임시 마커
        marker1.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
        marker1.setMap(naverMap); // 마커 표시
*/
        // 첫 실행 시 자기 주변의 어린이집/유치원을 검색해서 마커 표시
        try { // 유치원
            NearKinderMarker();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try { // 어린이집
            NearChildMarker();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        // 클릭 리스너 :
        naverMap.setOnMapClickListener((point, coord) ->
                Toast.makeText(this, coord.latitude + ", " + coord.longitude,
                        Toast.LENGTH_SHORT).show());


        // 롱 클릭 리스너
        naverMap.setOnMapLongClickListener((point, coord) ->
                Toast.makeText(this, coord.latitude + ", " + coord.longitude,
                        Toast.LENGTH_SHORT).show());

        // 심벌 클릭 리스너
        naverMap.setOnSymbolClickListener(symbol -> {
            if ("서울특별시청".equals(symbol.getCaption())) {
                Toast.makeText(this, "서울시청 클릭", Toast.LENGTH_SHORT).show();
                // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
                return true;
            }
            // 이벤트 전파, OnMapClick 이벤트가 발생함
            return false;
        });




    }


    public void NearKinderMarker() throws ParseException, JSONException { // 화면에 있는 유치원의 마커를 표시함
        int count = 0 ; // 최대 10개만 표시 카운트
         // 유치원 기본 현황 시트
        int colTotal = sheet11.getColumns();    // 전체 컬럼
        String address = "";
        double m_y, y;
        double m_x, x;
        try {
            Location mlocationSource = locationSource.getLastLocation();
            m_y = mlocationSource.getLatitude(); // 자기 위치 위도
            m_x = mlocationSource.getLongitude();
        } catch (Exception e){
            m_y =  37.541680773674464; // 건국대 기준
            m_x = 127.07943250328056; //
        }

        ArrayList<String> name = new ArrayList<>();
        ArrayList<Integer> num = new ArrayList<>();



        for (int i = 3; i < colTotal; i++){


            address = sheet11.getCell(7, i).getContents(); // 유치원 주소는 7번째 열에 있음.(0번째 부터 시작) row는 줄. 유치원 위도는 22, 23번째에 있다.

            y = Double.parseDouble(sheet11.getCell(22, i).getContents()); // 위도 가져옴.
            x = Double.parseDouble(sheet11.getCell(23, i).getContents()); // 경도 가져옴.

            String kindername = sheet11.getCell(3,i).getContents(); // 유치원이름은 3번째 (0번째 부터 시작)에 있음.
            String kinderType = sheet11.getCell(4,i).getContents(); // 유치원 유형은 4번째 (0번째 부터 시작)에 있음.
            String kinderTel = sheet11.getCell(8,i).getContents(); // 유치원 전화번호는 8번째 (0번째 부터 시작)에 있음.



            if (count >= 10) // 검색 결과가 10개 이상이면 아웃
            {

                break;
            }


            if (distance(y,x,m_y,m_x,"kilometer") < 3.0){ // 자신의 주변 (3km 이내라면) 검색

                dataList.add(new School2(address, kindername, kinderType, kinderTel, x, y));
                name.add(kindername);
                num.add(i);
                switch (count) {
                    case 0:
                        marker1.setMap(null); // 기존 마커 삭제
                        marker1.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker1.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker1.setIconTintColor(Color.TRANSPARENT); //
                        marker1.setMap(naverMap2); // 마커 표시

                        break;
                    case 1:
                        marker2.setMap(null); // 기존 마커 삭제
                        marker2.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker2.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker2.setIconTintColor(Color.TRANSPARENT); //
                        marker2.setMap(naverMap2); // 마커 표시

                        break;
                    case 2:
                        marker3.setMap(null); // 기존 마커 삭제
                        marker3.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker3.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker3.setMap(naverMap2); // 마커 표시
                        marker3.setIconTintColor(Color.TRANSPARENT); //

                        break;
                    case 3:
                        marker4.setMap(null); // 기존 마커 삭제
                        marker4.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker4.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker4.setIconTintColor(Color.TRANSPARENT); //
                        marker4.setMap(naverMap2); // 마커 표시
                        infoWindow.open(marker4);
                        break;
                    case 4:
                        marker5.setMap(null); // 기존 마커 삭제
                        marker5.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker5.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker5.setIconTintColor(Color.TRANSPARENT); //
                        marker5.setMap(naverMap2); // 마커 표시

                        break;
                    case 5:
                        marker6.setMap(null); // 기존 마커 삭제
                        marker6.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker6.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker6.setIconTintColor(Color.TRANSPARENT); //
                        marker6.setMap(naverMap2); // 마커 표시

                        break;
                    case 6:
                        marker7.setMap(null); // 기존 마커 삭제
                        marker7.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker7.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker7.setIconTintColor(Color.TRANSPARENT); //
                        marker7.setMap(naverMap2); // 마커 표시

                        break;
                    case 7:
                        marker8.setMap(null); // 기존 마커 삭제
                        marker8.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker8.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker8.setIconTintColor(Color.TRANSPARENT); //
                        marker8.setMap(naverMap2); // 마커 표시

                        break;
                    case 8:
                        marker9.setMap(null); // 기존 마커 삭제
                        marker9.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker9.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker9.setMap(naverMap2); // 마커 표시

                        break;
                    case 9:
                        marker10.setMap(null); // 기존 마커 삭제
                        marker10.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker10.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
                        marker10.setIconTintColor(Color.TRANSPARENT); //
                        marker10.setMap(naverMap2); // 마커 표시

                        break;
                    default:
                        break;

                }

                count++;
            }
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = (RecyclerView)findViewById(R.id.commonRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        recyclerView.setAdapter(new commonAdapter(dataList));  // Adapter 등록

        // 각 마커 클릭 리스너
        marker1.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker1.setTag("마커 1");
                infoWindow.open(marker1);
                return false;
            }
        });
        marker2.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker2.setTag("마커 2");
                infoWindow.open(marker2);
                return true;
            }
        });
        marker3.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker3.setTag("마커 3");
                infoWindow.open(marker3);
                return true;
            }
        });
        marker4.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker4.setTag("마커 4");
                infoWindow.open(marker4);
                return false;
            }
        });
        marker5.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker5.setTag("마커 5");
                infoWindow.open(marker5);
                return false;
            }
        });
        marker6.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker6.setTag("마커 6");
                infoWindow.open(marker6);
                return false;
            }
        });
        marker7.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker7.setTag("마커 7");
                infoWindow.open(marker7);
                return false;
            }
        });
        marker8.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker8.setTag("마커 8");
                infoWindow.open(marker8);
                return false;
            }
        });
        marker9.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker9.setTag("마커 9");
                infoWindow.open(marker9);
                return false;
            }
        });
        marker10.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker10.setTag("마커 10");
                infoWindow.open(marker10);
                return true;
            }
        });

        /*
        // 네이버 API 주소 검색 요청
        String response = getAddress_DAUM(address);

        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray)parser.parse(response);

        for (int i = 0; i < jsonArray.size(); ++i) {
            JSONObject jo = (JSONObject)jsonArray.get(i);    // 첫번째 list를 꺼낸다
            JSONArray arrays = (JSONArray)jo.get("address");    // list 안의 list를 가져온다
            for (Object obj : arrays) {
                JSONObject childObj = (JSONObject)obj; // 리스트 하나하나 가져오기
                double x1 = (double) childObj.get("x"); // double 형으로 경도 추출
                double y1 = (double) childObj.get("y"); // double 형으로 위도 추출
                JSONArray arrays2 = (JSONArray)jo.get("addressElement"); // 주소 리스트 추출
                for (Object obj2 : arrays2){
                    JSONObject childObj2 = (JSONObject)obj2; // 리스트 하나하나 가져오기
                    String name1 = (String) childObj2.get("shortname");
                }
                System.out.println(childObj.get("x"));
                System.out.println(childObj.get("y"));
            }
        }
        Object obj = parser.parse( response );

        JSONObject jsonObj = (JSONObject) obj;

        //double x = (double) jsonObj.get("x"); // double 형으로 경도 추출
        //double y = (double) jsonObj.get("y"); // double 형으로 위도 추출
        String name = (String) jsonObj.get("name"); //

        */

    }


    public void NearChildMarker() throws JSONException, ParseException { // 화면에 있는 어린이집의 마커를 표시함
        int count = 0 ;
        //Sheet sheet = getSheet0(); // 어린이집 기본 현황 시트

        int colTotal = sheet0.getColumns();    // 전체 컬럼
        String address2 = "";
        String kindername2= "";
        String kinderTel2="";
        String kinderType2="";
        double m_y, y;
        double m_x, x;
        try {
            Location mlocationSource = locationSource.getLastLocation();
            m_y = mlocationSource.getLatitude(); // 자기 위치 위도
            m_x = mlocationSource.getLongitude();
        } catch (Exception e){
            m_y =  37.541680773674464; // 건국대 기준
            m_x = 127.07943250328056; //
        }

        for (int i = 1; i < colTotal; i++){


            address2 = sheet0.getCell(6, i).getContents(); // 어린이집 주소는 6번째 열에 있음.(0번째부터 시작) row는 줄.

            y = Double.parseDouble(sheet0.getCell(15, i).getContents()); // 위도 가져옴.
            x = Double.parseDouble(sheet0.getCell(16, i).getContents()); // 경도 가져옴.

            kindername2 = sheet0.getCell(2,i).getContents(); // 어린이집 이름은 2번째 (0번째 부터 시작)에 있음.
            kinderType2 = sheet0.getCell(3,i).getContents(); // 어린이집 유형은 3번째 (0번째 부터 시작)에 있음.
            kinderTel2 = sheet0.getCell(7,i).getContents(); // 어린이집 전화번호는 7번째 (0번째 부터 시작)에 있음.


            if (count >= 10) // 검색 결과가 10개 이상이면 아웃
            {

                break;
            }


            if (distance(y,x,m_y,m_x,"kilometer") < 3.0){ // 자신의 주변 (3km 이내라면) 검색

                dataList.add(new School2(address2, kindername2, kinderType2, kinderTel2, x, y));
                switch (count) {
                    case 0:
                        marker11.setMap(null); // 기존 마커 삭제
                        marker11.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker11.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker11.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker11.setMap(naverMap); // 마커 표시
                        break;
                    case 1:
                        marker12.setMap(null); // 기존 마커 삭제
                        marker12.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker12.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker12.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker12.setMap(naverMap); // 마커 표시

                        break;
                    case 2:
                        marker13.setMap(null); // 기존 마커 삭제
                        marker13.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker13.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker13.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker13.setMap(naverMap); // 마커 표시

                        break;
                    case 3:
                        marker14.setMap(null); // 기존 마커 삭제
                        marker14.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker14.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker14.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker14.setMap(naverMap); // 마커 표시

                        break;
                    case 4:
                        marker15.setMap(null); // 기존 마커 삭제
                        marker15.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker15.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker15.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker15.setMap(naverMap); // 마커 표시

                        break;
                    case 5:
                        marker16.setMap(null); // 기존 마커 삭제
                        marker16.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker16.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker16.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker16.setMap(naverMap); // 마커 표시

                        break;
                    case 6:
                        marker17.setMap(null); // 기존 마커 삭제
                        marker17.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker17.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker17.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker17.setMap(naverMap); // 마커 표시
                        break;
                    case 7:
                        marker18.setMap(null); // 기존 마커 삭제
                        marker18.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker18.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker18.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker18.setMap(naverMap); // 마커 표시
                        break;
                    case 8:
                        marker19.setMap(null); // 기존 마커 삭제
                        marker19.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker19.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker19.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker19.setMap(naverMap); // 마커 표시
                        break;
                    case 9:
                        marker20.setMap(null); // 기존 마커 삭제
                        marker20.setPosition(new LatLng(y, x)); // 마커 위치 재설정
                        marker20.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
                        marker20.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
                        marker20.setMap(naverMap); // 마커 표시
                        break;
                    default:
                        break;

                }

                count++;
            }
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = (RecyclerView)findViewById(R.id.commonRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        recyclerView.setAdapter(new commonAdapter(dataList));  // Adapter 등록

        /*
        // 네이버 API 주소 검색 요청
        String response = getAddress_DAUM(address);


        JSONParser parser = new JSONParser();
        Object obj = parser.parse( response );
        JSONObject jsonObj = (JSONObject) obj;

        double x = (double) jsonObj.get("x"); // double 형으로 경도 추출
        double y = (double) jsonObj.get("y"); // double 형으로 위도 추출
        */
        // 각 마커 클릭 리스너
        marker11.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker11.setTag("마커 1"); // 정보창
                marker11.setCaptionText("123"); // 캡션 설정
                marker11.setCaptionRequestedWidth(200);
                infoWindow.open(marker11);
                return true;
            }
        });
        marker12.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker12.setTag("마커 2");
                infoWindow.open(marker12);
                return true;
            }
        });
        marker13.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker13.setTag("마커 3");
                infoWindow.open(marker13);
                return true;
            }
        });
        marker14.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker14.setTag("마커 4");
                infoWindow.open(marker14);
                return true;
            }
        });
        marker15.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker15.setTag("마커 5");
                infoWindow.open(marker15);
                return true;
            }
        });
        marker16.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker16.setTag("마커 6");
                infoWindow.open(marker16);
                return true;
            }
        });
        marker17.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker17.setTag("마커 17");
                infoWindow.open(marker17);
                return true;
            }
        });
        marker18.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker18.setTag("마커 18");
                infoWindow.open(marker18);
                return true;
            }
        });
        marker19.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker19.setTag("마커 19");
                infoWindow.open(marker19);
                return true;
            }
        });
        marker20.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull @NotNull Overlay overlay) {
                marker20.setTag("마커 10");
                infoWindow.open(marker20);
                return true;
            }
        });

        
    }

    // 주소 검색
    public String getAddress_DAUM(String data) { // data에 구주소나 신주소를 입력함.

        final String Client_ID = "o82z0vth6u"; // 인증용 클라이언트 아이디
        final String Client_Secret = "LjN7euyVUTJlH2haz5RmIMmwwTVq77I6ODNipNrE"; // 클라이언트 인증키

        final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";

        try {
            String addr = URLEncoder.encode(data, "UTF-8");  // data : 주소입력
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr; //json
            //String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml

            // http 프로토콜로 api 호출
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", Client_ID);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", Client_Secret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 응답코드가 200이면 정상
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
            return response.toString(); // 응답 반환
        } catch (Exception e) {
            System.out.println(e);
            return null; // 오류면 주소값 없이 반환
        }
    }


    // 거리 계산 코드

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return
     */

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }



}

