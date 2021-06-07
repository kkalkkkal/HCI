package com.example.hci_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;


import com.example.hci_project.IntroActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SchoolInfoActivity extends AppCompatActivity implements OnMapReadyCallback {


    private TableLayout tableLayout, tableLayout2, tableLayout3;
    private NaverMap naverMap2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    public FusedLocationSource locationSource;
    CameraPosition cameraPosition;
    CameraUpdate cameraUpdate;
    private Marker marker; // 마커
    private School school;


    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
        this.naverMap2 = naverMap;


        naverMap2.setLocationSource(locationSource); // 자기 위치 설정
        naverMap2.setMapType(NaverMap.MapType.Basic); // 기본형 지도
        naverMap2.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true); // 빌딩 그룹 생성

        naverMap2.setLocationTrackingMode(LocationTrackingMode.Follow); // 위치 추적 모드 실행


        naverMap2.moveCamera(cameraUpdate);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); // 현재 위치 갱신

        //네이버 지도 추가 UI 설정
        UiSettings uiSettings = naverMap2.getUiSettings();
        uiSettings.setLocationButtonEnabled(true); // 현 위치 버튼 활성화
        uiSettings.setTiltGesturesEnabled(false); // 특수 제스처 기능 봉인

        // 마커 표시하기 (자기 위치 기준 검색)
        marker = new Marker();
        marker.setMap(null); // 기존 마커 삭제
        marker.setPosition(new LatLng(school.getLat(), school.getLng())); // 그냥 건국대 임시 마커
        if (school.getType().contains("어린이집"))
        {
            marker.setIcon(MarkerIcons.BLACK);
            marker.setIconTintColor(Color.rgb(170,0,170)); // 빨간 색 + 파란색 = 보라색
        } else {
            marker.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
        }
        marker.setMap(naverMap2); // 마커 표시

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

        TextView hak_count = findViewById(R.id.hak_count);
        TextView ua_count = findViewById(R.id.ua_count);
        TextView hak_count2 = findViewById(R.id.hak_count2);
        TextView gyo_count = findViewById(R.id.gyo_count);

        Intent intent = getIntent(); /*데이터 수신*/

        school = (School)intent.getSerializableExtra("school"); /*클래스*/

        // 네이버 지도 설정
        cameraPosition = new CameraPosition(new LatLng(school.getLat(), school.getLng()), 13);// 넘겨받은 위치 기준으로 연다

        cameraUpdate = CameraUpdate.scrollTo(new LatLng(school.getLat(), school.getLng()));
        cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition); // 맵 카메라 이동


        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); // 현재 위치 갱신


        // 네이버 지도 호출하기
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

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

        // 주소
        address.setText(school.getAddr());

        // 홈페이지
        if (school.getHomePage() == "")
            homepage.setText("없음");
        else
            homepage.setText(String.valueOf(school.getHomePage()));

        // 전화번호
        callnumber.setText(String.valueOf(school.getTel()));

        // 통학 버스 유무
        if (school.isAvailableBus() == false)
            bus_number.setText(String.valueOf("없음"));
        else
            bus_number.setText(String.valueOf("있음"));


        // School Data 기준으로 검색
        if(school.getType().contains("어린이집")) // 어린이집의 경우
        {
            IntroActivity.sheet0.findCell("");

        } else { // 유치원

        }
        // 운영시간
        opentime.setText(String.valueOf("0900~1800"));

        // 운영방식
        unyoung.setText(String.valueOf("직영"));

        // 영양
        youngyang.setText(String.valueOf("2"));

        // CCTV 개수
        cctv.setText("");


        /////////////////////////////// Bar chart /////////////////////////////

        float a = (float) (school.getTeacherCnt() / school.getRoomCnt());
        BarChart chart = findViewById(R.id.barchart);
        ArrayList NoOfEmp2 = new ArrayList();
        for (int i = 1; i <= school.getRoomCnt(); i++) {
            NoOfEmp2.add(new BarEntry(i, a));
        }

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





}