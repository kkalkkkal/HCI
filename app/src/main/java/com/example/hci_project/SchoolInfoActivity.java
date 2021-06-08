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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hci_project.bean.FavoriteSchoolManager;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import kotlin.Unit;

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

        Button button1 = findViewById(R.id.btn1);
        Button button2 = findViewById(R.id.btn2);
        ImageButton like_on = findViewById(R.id.like_added);
        ImageButton like_off = findViewById(R.id.like_added_not);
        ImageButton compare_add = findViewById(R.id.compare_add);
        ImageButton compare_delete = findViewById(R.id.compare_delete);
        TextView shcool_name = findViewById(R.id.school_name);
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

        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                return false;
            }
        });

        // 즐겨찾기 추가
        like_off.setOnClickListener((v) -> {

            //like_off.setVisibility(View.VISIBLE);
            FavoriteSchoolManager.Companion.getInstance().use(getApplicationContext(), (manager) -> {
                handler.post(() -> {
                    if (manager == null) {
                        Toast.makeText(getApplicationContext(), "즐겨찾기 리스트를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    manager.getList().add(school);
                    Toast.makeText(getApplicationContext(), "즐겨찾기 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    manager.save(getApplicationContext());
                });
                return Unit.INSTANCE;
            });
        });
        
       /* // 즐겨찾기
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
*/


        // 즐겨찾기 해제
        like_on.setOnClickListener(new View.OnClickListener() {

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

                //compare_add.setVisibility(View.GONE);
                //compare_delete.setVisibility(View.VISIBLE);

                try {

                    File dir = new File(getApplicationContext().getFilesDir().toString());
                    File f = new File(dir, "UserDB.xls");

                    Workbook workbook;

                    if(!f.exists()){
                        f.createNewFile();
                        workbook = Workbook.getWorkbook(getApplicationContext().getAssets().open("UserDB.xls"));

                        //throw new Exception("file not found");
                    } else {
                        f = new File(String.valueOf(getApplicationContext().getFileStreamPath("UserDB.xls")));
                        workbook = Workbook.getWorkbook(f);
                    }

                    if(!f.canRead()){
                        throw new Exception("can't read file");
                    }

                    if(workbook == null){
                        throw new Exception("Workbook is null!!");
                    }else{
                        File newExcel = new File(dir, "UserDB.xls");
                        WritableWorkbook writeBook = Workbook.createWorkbook(newExcel, workbook);
                        WritableSheet writeSheet = writeBook.getSheet(0);

                        if(writeSheet != null) {
                            int colTotal = writeSheet.getColumns();    // 전체 컬럼
                            int rowIndexStart = 1;                  // row 인덱스 시작
                            int rowTotal = writeSheet.getRows(); // 행 개수 (이미 입력 되어있는 데이터 값)

                            WritableCell cell = null;
                            if (writeSheet.findCell(school.getName()) != null){ // 이미 있으면 삭제
                                writeSheet.removeRow(writeSheet.findCell(school.getName()).getRow()); // 해당 행 삭제
                                Toast.makeText(getApplicationContext(), "compare_delete", Toast.LENGTH_LONG).show();
                            } else { // 없으면 추가
                                Toast.makeText(getApplicationContext(), "compare_added", Toast.LENGTH_LONG).show();
                                Label label = new Label(0, rowTotal, Integer.toString(rowTotal)); // 번호
                                writeSheet.addCell(label);

                                Label label2 = new Label(1, rowTotal, school.getName()); // 유치원 or 어린이집 이름
                                writeSheet.addCell(label2);

                                Label label3 = new Label(2, rowTotal, Integer.toString(school.getKidsPerTeacher())); // 선생 당 학생 수 (int)
                                writeSheet.addCell(label3);

                                // 버스 운행 여부 & 자동차 수
                                if (school.getType().contains("어린이집")) {
                                    Cell cell2 = IntroActivity.sheet0.findCell(school.getName()); // 엑셀파일에서 cell 반환
                                    int row = cell2.getRow(); // 몇 번째 행인지 반환
                                    if (school.isAvailableBus() == false) {
                                        Label label4 = new Label(3, rowTotal, "0"); // 버스 운행 여부 & 자동차 수
                                        writeSheet.addCell(label4);
                                    } else {
                                        Label label4 = new Label(3, rowTotal, "1"); // 버스 운행 여부 & 자동차 수
                                        writeSheet.addCell(label4);
                                    }

                                    // 안전 점검 + CCTV -> 데이터 없음.
                                    Label label5 = new Label(4, rowTotal, "-");
                                    writeSheet.addCell(label5);

                                    Label label6 = new Label(5, rowTotal, "-");
                                    writeSheet.addCell(label6);

                                    Label label7 = new Label(6, rowTotal, "-");
                                    writeSheet.addCell(label7);

                                    Label label8 = new Label(7, rowTotal, "-");
                                    writeSheet.addCell(label8);

                                    Label label9 = new Label(8, rowTotal, "-");
                                    writeSheet.addCell(label9);


                                } else { // 유치원

                                    // 버스 운행 여부
                                    Cell cell2 = IntroActivity.sheet6.findCell(school.getName()); // 엑셀파일에서 cell 반환
                                    int row = cell2.getRow(); // 몇 번째 행인지 반환
                                    Label label4 = new Label(3, rowTotal, IntroActivity.sheet6.getCell(6, row).getContents());
                                    writeSheet.addCell(label4);

                                    // 소방 안전 점검
                                    if (school.getSafety().getFireCheck() == false) {
                                        Label label5 = new Label(4, rowTotal, "X");
                                        writeSheet.addCell(label5);
                                    } else {
                                        cell2 = IntroActivity.sheet3.findCell(school.getName());
                                        row = cell2.getRow();
                                        Label label5 = new Label(4, rowTotal, IntroActivity.sheet3.getCell(6, row).getContents());
                                        writeSheet.addCell(label5);
                                    }

                                    // 전기 안전 점검
                                    if (school.getSafety().getElectricCheck() == false) {
                                        Label label6 = new Label(5, rowTotal, "X");
                                        writeSheet.addCell(label6);

                                    } else {
                                        cell2 = IntroActivity.sheet3.findCell(school.getName());
                                        row = cell2.getRow();
                                        Label label6 = new Label(5, rowTotal, IntroActivity.sheet3.getCell(12, row).getContents());
                                        writeSheet.addCell(label6);
                                    }

                                    // 가스 안전 점검
                                    if (school.getSafety().getElectricCheck() == false) {
                                        Label label7 = new Label(6, rowTotal, "X");
                                        writeSheet.addCell(label7);

                                    } else {
                                        cell2 = IntroActivity.sheet3.findCell(school.getName());
                                        row = cell2.getRow();
                                        Label label7 = new Label(6, rowTotal, IntroActivity.sheet3.getCell(8, row).getContents());
                                        writeSheet.addCell(label7);
                                    }

                                    // 놀이시설 안전 점검
                                    if (school.getSafety().getElectricCheck() == false) {
                                        Label label8 = new Label(7, rowTotal, "X");
                                        writeSheet.addCell(label8);

                                    } else {
                                        cell2 = IntroActivity.sheet3.findCell(school.getName());
                                        row = cell2.getRow();
                                        Label label8 = new Label(7, rowTotal, IntroActivity.sheet3.getCell(14, row).getContents());
                                        writeSheet.addCell(label8);
                                    }

                                    // CCTV 개수
                                    if (school.getSafety().getElectricCheck() == false) {
                                        Label label9 = new Label(8, rowTotal, "X");
                                        writeSheet.addCell(label9);

                                    } else {
                                        cell2 = IntroActivity.sheet3.findCell(school.getName());
                                        row = cell2.getRow();
                                        Label label9 = new Label(8, rowTotal, IntroActivity.sheet3.getCell(17, row).getContents());
                                        writeSheet.addCell(label9);
                                    }
                                }
                            }
                            writeBook.write();//반드시 적어줘야 엑셀에 적용이 됨;
                            writeBook.close();

                        }
                    }

                } catch (IOException | BiffException e) {
                    e.printStackTrace();
                } catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*Intent intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
                intent.putExtra("school", school);
                intent.putExtra("last", 'b');
                startActivity(intent);
                finish();*/
            }

        });

        compare_delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "compare_deleted", Toast.LENGTH_LONG).show();
                compare_add.setVisibility(View.VISIBLE);
                compare_delete.setVisibility(View.GONE);
                Intent intent = new Intent(SchoolInfoActivity.this, CompareSchoolActivity.class);
                intent.putExtra("school", school);
                intent.putExtra("last", 'c');
                startActivity(intent);
                finish();
            }

        });

        ///////////////////////// 받아온 데이터를 여기에서 표시

        // 이름
        shcool_name.setText(school.getName());

        // 주소
        address.setText(school.getAddr());

        // 홈페이지
        if (school.getHomePage() == "")
            homepage.setText("없음");
        else
            homepage.setText(school.getHomePage());

        // 전화번호
        Cell cell0 = IntroActivity.sheet11.findCell(school.getName()); // 엑셀파일에서 cell 반환
        if (cell0 != null) {
            int row0 = cell0.getRow();
            callnumber.setText(IntroActivity.sheet11.getCell(8, row0).getContents());
        } else {
            callnumber.setText(school.getTel());
        }

        // 통학 버스 유무
        if (school.isAvailableBus() == false)
            bus_number.setText(String.valueOf("없음"));
        else
            bus_number.setText(String.valueOf("있음"));


        // School Data 기준으로 검색
        if(school.getType().contains("어린이집")) // 어린이집의 경우
        {
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.GONE);
            Cell cell = IntroActivity.sheet0.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row = cell.getRow(); // 몇 번째 행인지 반환

            // 운영시간
            opentime.setText(String.valueOf("월~금 : 12시간(07:30~19:30), 토요일 : 8시간(07:30~15:30)")); // 법령상 디폴트

            // 운영방식
            unyoung.setText(IntroActivity.sheet0.getCell(3,row).getContents());

            // 영양
            youngyang.setText("0");

            // CCTV 개수
            cctv.setText("0"); // 각 시도별 데이터를 찾아서 따로 구해야함.

        } else { // 유치원
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.VISIBLE);
            // 운영시간
            Cell cell = IntroActivity.sheet11.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row = cell.getRow();
            opentime.setText(IntroActivity.sheet11.getCell(10,row).getContents());

            // 급식 운영방식
            Cell cell2 = IntroActivity.sheet7.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row2 = cell2.getRow();
            if(IntroActivity.sheet7.getCell(5,row2).getContents().contains("위탁")){ // 위탁 업체도 같이 표기
                unyoung.setText(IntroActivity.sheet7.getCell(5,row2).getContents() + IntroActivity.sheet7.getCell(6,row2).getContents());
            }
            else {
                unyoung.setText(IntroActivity.sheet7.getCell(5, row2).getContents());
            }

            // 영양
            Cell cell3 = IntroActivity.sheet7.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row3 = cell3.getRow();
            youngyang.setText(Integer.toString(Integer.parseInt(IntroActivity.sheet7.getCell(10,row3).getContents())
                    + Integer.parseInt(IntroActivity.sheet7.getCell(11,row3).getContents())));

            // CCTV 개수
            Cell cell4 = IntroActivity.sheet3.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row4 = cell4.getRow();
            cctv.setText(IntroActivity.sheet3.getCell(18,row4).getContents());

        }



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
                ua_count.setText(String.valueOf(school.getCurrentStudentCnt()));
            }

        }


        tableLayout2 = (TableLayout) findViewById(R.id.tablelayout2);
        TableRow tableRow2 = new TableRow(this);     // tablerow 생성
        tableRow2.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        for (int i = 0; i < 2; i++) {

            if (i == 0) {
                hak_count2.setText(String.valueOf(school.getCurrentStudentCnt()));
            } else {
                gyo_count.setText(String.valueOf(school.getTeacherCnt()));
            }
        }


        tableLayout3 = (TableLayout) findViewById(R.id.tablelayout3);
        TableRow tableRow3 = new TableRow(this);     // tablerow 생성
        tableRow3.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // 시설 점검 여부
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            Cell cell4 = IntroActivity.sheet3.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row4 = 0;
            if (cell4 != null) {
                row4 = cell4.getRow();
            } else{
                break;
            }
            if (i == 0) {
                textView.setText(String.valueOf("소방안전점검"));
            } else if (i == 1) {
                row4 = cell4.getRow();
                textView.setText(IntroActivity.sheet3.getCell(9, row4).getContents());

            } else {
                    row4 = cell4.getRow();
                    textView.setText(IntroActivity.sheet3.getCell(10, row4).getContents());
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            tableRow3.addView(textView);        // tableRow에 view 추가

        }
        if(tableLayout3.getParent() != null)
        {
            ((ViewGroup)tableLayout3.getParent()).removeView(tableLayout3);
        }
        if(tableRow3 != null) {
            tableLayout3.addView(tableRow3);        // tableLayout에 tableRow 추가
        }

        // 전기 점검 여부
        for (int i = 0; i < 3; i++) {
            TextView textView = new TextView(this);
            Cell cell4 = IntroActivity.sheet3.findCell(school.getName()); // 엑셀파일에서 cell 반환
            int row4 = 0;
            if (cell4 != null) {
                row4 = cell4.getRow();
            } else{
                break;
            }
            if (i == 0) {
                textView.setText(String.valueOf("전기안전점검"));
            } else if (i == 1) {
                    textView.setText(IntroActivity.sheet3.getCell(11, row4).getContents());
            } else {
                row4 = cell4.getRow();
                textView.setText(IntroActivity.sheet3.getCell(12, row4).getContents());
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            tableRow3.addView(textView);        // tableRow에 view 추가
        }
        if(tableRow3.getParent() != null) // 부모가 있으면
        {
            ((ViewGroup)tableRow3.getParent()).removeView(tableRow3); // 끊어줌
        }
        if(tableRow3 != null) {
            tableLayout3.addView(tableRow3);        // tableLayout에 tableRow 추가
        }

    }





}