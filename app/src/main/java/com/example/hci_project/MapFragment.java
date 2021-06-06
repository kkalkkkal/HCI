package com.example.hci_project;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hci_project.bean.School2;
import com.google.android.material.chip.Chip;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

import static com.example.hci_project.MainActivity.*;

public class MapFragment extends Fragment implements OnMapReadyCallback {
  private MapView mapView;
  public static NaverMap naverMap2; // 네이버 맵 객체 - 다른 곳에서도 접근 가능하게 하려고 일단 전역변수화함.
  private LinearLayout linearLayout;
  MainActivity mainActivity = new MainActivity();

  // 네이버 자기 위치 디폴트 코드
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  public FusedLocationSource locationSource;

  // 마커 선언
  // 마커는 최대 10개
  Marker marker1 = mainActivity.getMarker1();
  Marker marker2 = mainActivity.getMarker2();
  Marker marker3 = mainActivity.getMarker3();
  Marker marker4 = mainActivity.getMarker4();
  Marker marker5 = mainActivity.getMarker5();
  Marker marker6 = mainActivity.getMarker6();
  Marker marker7 = mainActivity.getMarker7();
  Marker marker8 = mainActivity.getMarker8();
  Marker marker9 = mainActivity.getMarker9();
  Marker marker10 = mainActivity.getMarker10();

  Marker marker11 = mainActivity.getMarker11();
  Marker marker12 = mainActivity.getMarker12();
  Marker marker13 = mainActivity.getMarker13();
  Marker marker14 = mainActivity.getMarker14();
  Marker marker15 = mainActivity.getMarker15();
  Marker marker16 = mainActivity.getMarker16();
  Marker marker17 = mainActivity.getMarker17();
  Marker marker18 = mainActivity.getMarker18();
  Marker marker19 = mainActivity.getMarker19();
  Marker marker20 = mainActivity.getMarker20();

  ArrayList<School2> dataList = new ArrayList<>();
  //RecyclerView recyclerView = mainActivity.recyclerView;
  com.naver.maps.map.MapFragment mapFragment2;

  CameraPosition cameraPosition;
  CameraUpdate cameraUpdate;

  // 메인 간이 필터
  private Chip kinder_chip; // 유치원 필터
  private Chip child_chip; // 어린이집 필터

  InfoWindow infoWindow2;

  public MapFragment() {
  }

  public MapFragment(int contentLayoutId) {
    super(contentLayoutId);
  }

  @Override
  public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // 네이버 지도 설정
    cameraPosition = new CameraPosition(new LatLng(37.543344020789625, 127.07557079824849), 13);// 건국대 기준으로 지도를 연다.

    cameraUpdate = CameraUpdate.scrollTo(new LatLng(37.543344020789625, 127.07557079824849));
    cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition);


    locationSource =
            new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); // 현재 위치 갱신



  }

  @Override

  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


    switch (requestCode) {

      case PERMISSIONS_REQUEST :

        if (grantResults.length > 0

                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          Toast.makeText(mainActivity.getBaseContext(), "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_LONG).show();

        } else {

          Toast.makeText(super.getContext(), "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_LONG).show();

        }

        break;

    }

    // 네이버 지도 소스 권한
    if (locationSource.onRequestPermissionsResult(
            requestCode, permissions, grantResults)) {
      if (!locationSource.isActivated()) { // 권한 거부됨
        naverMap2.setLocationTrackingMode(LocationTrackingMode.None);
      }
      return;
    }

    super.onRequestPermissionsResult(
            requestCode, permissions, grantResults);


  }

  @Override
  public void onViewCreated(@NonNull View view,
                            @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mapView = view.findViewById(R.id.map_view);
    mapView.onCreate(savedInstanceState);
    mapView.getMapAsync(this);
  }

  @Nullable
  @org.jetbrains.annotations.Nullable
  @Override
  public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.mappage, container, false);
    rootView.findViewById(R.id.sliding_layout);
    return rootView; //super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  public void onMapReady(@NonNull @NotNull NaverMap naverMap) { // 실행되면 여기가 호출됩니다! 2021.05.25
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
    uiSettings.setTiltGesturesEnabled(false);


/*
        // 마커 표시하기 (자기 위치 기준 검색)
        marker1 = new Marker();
        marker1.setMap(null); // 기존 마커 삭제
        marker1.setPosition(new LatLng(37.541680773674464, 127.07943250328056)); // 그냥 건국대 임시 마커
        marker1.setIcon(MarkerIcons.YELLOW); // 마커 색깔, 유치원은 노랑
        marker1.setMap(naverMap); // 마커 표시
*/



    // 클릭 리스너 :
    naverMap2.setOnMapClickListener((point, coord) ->{
      //Toast.makeText(super.getActivity(), coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show()
      System.out.println("click");

    });



    // 롱 클릭 리스너
    naverMap2.setOnMapLongClickListener((point, coord) -> {
      Toast.makeText(super.getActivity(), coord.latitude + ", " + coord.longitude,
              Toast.LENGTH_SHORT).show();
    });

    // 롱 클릭 리스너
    naverMap2.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
      @Override
      public void onMapLongClick(@NonNull @NotNull PointF pointF, @NonNull @NotNull LatLng latLng) {

        // 길게 클릭하면 카메라 위치 기준으로 검색
        //   Toast.makeText(this, coord.latitude + ", " + coord.longitude, Toast.LENGTH_SHORT).show();

      }
    });

    // 심벌 클릭 리스너
    naverMap2.setOnSymbolClickListener(symbol -> {
      if ("서울특별시청".equals(symbol.getCaption())) {
        Toast.makeText(super.getActivity(), "서울시청 클릭", Toast.LENGTH_SHORT).show();
        // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
        return true;
      }
      // 이벤트 전파, OnMapClick 이벤트가 발생함
      return false;
    });

    // 카메라 이동 리스너
    naverMap2.addOnCameraChangeListener((reason, animated) -> {
      Log.i("NaverMap", "카메라 변경 - reson: " + reason + ", animated: " + animated);

    });




  }

  @Override
  public void onStart() {
    super.onStart();
    mapView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    mapView.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
    mapView.onPause();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  @Override
  public void onStop() {
    super.onStop();
    mapView.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }

  //여기 이하 호출 안됨.
  public void NearKinderMarker() throws ParseException, JSONException { // 화면에 있는 유치원의 마커를 표시함
    int count = 0; // 최대 10개만 표시 카운트
    // 유치원 기본 현황 시트
    int colTotal = sheet11.getColumns();    // 전체 컬럼
    String address = "";
    double m_y, y;
    double m_x, x;
    try {
      Location mlocationSource = locationSource.getLastLocation();
      m_y = mlocationSource.getLatitude(); // 자기 위치 위도
      m_x = mlocationSource.getLongitude();
    } catch (Exception e) {
      m_y = 37.541680773674464; // 건국대 기준
      m_x = 127.07943250328056; //
    }

    ArrayList<String> name = new ArrayList<>();
    ArrayList<Integer> num = new ArrayList<>();


    for (int i = 3; i < colTotal; i++) {


      address = sheet11.getCell(7, i).getContents(); // 유치원 주소는 7번째 열에 있음.(0번째 부터 시작) row는 줄. 유치원 위도는 22, 23번째에 있다.

      y = Double.parseDouble(sheet11.getCell(22, i).getContents()); // 위도 가져옴.
      x = Double.parseDouble(sheet11.getCell(23, i).getContents()); // 경도 가져옴.

      String kindername = sheet11.getCell(3, i).getContents(); // 유치원이름은 3번째 (0번째 부터 시작)에 있음.
      String kinderType = sheet11.getCell(4, i).getContents(); // 유치원 유형은 4번째 (0번째 부터 시작)에 있음.
      String kinderTel = sheet11.getCell(8, i).getContents(); // 유치원 전화번호는 8번째 (0번째 부터 시작)에 있음.


      if (count >= 10) // 검색 결과가 10개 이상이면 아웃
      {

        break;
      }


      if (distance(y, x, m_y, m_x, "kilometer") < 3.0) { // 자신의 주변 (3km 이내라면) 검색

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




    // 각 마커 클릭 리스너
    marker1.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker1.setTag(dataList.get(0).getName()); // 정보창
        marker1.setCaptionText(dataList.get(0).getName()); // 캡션 설정
        marker1.setCaptionRequestedWidth(200);
        infoWindow.open(marker1);
        return false;
      }
    });
    marker2.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {

        marker2.setTag(dataList.get(1).getName()); // 정보창
        marker2.setCaptionText(dataList.get(1).getName()); // 캡션 설정
        marker2.setCaptionRequestedWidth(200);
        infoWindow.open(marker2);
        return true;
      }
    });
    marker3.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker3.setTag(dataList.get(2).getName()); // 정보창
        marker3.setCaptionText(dataList.get(2).getName()); // 캡션 설정

        marker3.setCaptionRequestedWidth(200);
        infoWindow.open(marker3);
        return true;
      }
    });
    marker4.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker4.setTag(dataList.get(3).getName()); // 정보창
        marker4.setCaptionText(dataList.get(3).getName()); // 캡션 설정
        marker11.setCaptionRequestedWidth(200);
        infoWindow.open(marker4);
        return false;
      }
    });
    marker5.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker5.setTag(dataList.get(4).getName()); // 정보창
        marker5.setCaptionText(dataList.get(4).getName()); // 캡션 설정
        infoWindow.open(marker5);
        return false;
      }
    });
    marker6.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker6.setTag(dataList.get(6).getName()); // 정보창
        marker6.setCaptionText(dataList.get(6).getName()); // 캡션 설정
        infoWindow.open(marker6);
        return false;
      }
    });
    marker7.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker7.setTag(dataList.get(6).getName()); // 정보창
        marker7.setCaptionText(dataList.get(6).getName()); // 캡션 설정
        infoWindow.open(marker7);
        return false;
      }
    });
    marker8.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker8.setTag(dataList.get(7).getName()); // 정보창
        marker8.setCaptionText(dataList.get(7).getName()); // 캡션 설정
        infoWindow.open(marker8);
        return false;
      }
    });
    marker9.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker9.setTag(dataList.get(8).getName()); // 정보창
        marker9.setCaptionText(dataList.get(8).getName()); // 캡션 설정
        infoWindow.open(marker9);
        return false;
      }
    });
    marker10.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        marker10.setTag(dataList.get(9).getName()); // 정보창
        marker19.setCaptionText(dataList.get(9).getName()); // 캡션 설정
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
    int count = 0;
    //Sheet sheet = getSheet0(); // 어린이집 기본 현황 시트

    int colTotal = sheet0.getColumns();    // 전체 컬럼
    String address2 = "";
    String kindername2 = "";
    String kinderTel2 = "";
    String kinderType2 = "";
    double m_y, y;
    double m_x, x;
    try {
      Location mlocationSource = locationSource.getLastLocation();
      m_y = mlocationSource.getLatitude(); // 자기 위치 위도
      m_x = mlocationSource.getLongitude();
    } catch (Exception e) {
      m_y = 37.541680773674464; // 건국대 기준
      m_x = 127.07943250328056; //
    }

    for (int i = 1; i < colTotal; i++) {


      address2 = sheet0.getCell(6, i).getContents(); // 어린이집 주소는 6번째 열에 있음.(0번째부터 시작) row는 줄.

      y = Double.parseDouble(sheet0.getCell(15, i).getContents()); // 위도 가져옴.
      x = Double.parseDouble(sheet0.getCell(16, i).getContents()); // 경도 가져옴.

      kindername2 = sheet0.getCell(2, i).getContents(); // 어린이집 이름은 2번째 (0번째 부터 시작)에 있음.
      kinderType2 = sheet0.getCell(3, i).getContents(); // 어린이집 유형은 3번째 (0번째 부터 시작)에 있음.
      kinderTel2 = sheet0.getCell(7, i).getContents(); // 어린이집 전화번호는 7번째 (0번째 부터 시작)에 있음.


      if (count >= 10) // 검색 결과가 10개 이상이면 아웃
      {

        break;
      }


      if (distance(y, x, m_y, m_x, "kilometer") < 3.0) { // 자신의 주변 (3km 이내라면) 검색

        dataList.add(new School2(address2, kindername2, kinderType2, kinderTel2, x, y));
        switch (count) {
          case 0:
            marker11.setMap(null); // 기존 마커 삭제
            marker11.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker11.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker11.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker11.setMap(naverMap2); // 마커 표시
            break;
          case 1:
            marker12.setMap(null); // 기존 마커 삭제
            marker12.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker12.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker12.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker12.setMap(naverMap2); // 마커 표시

            break;
          case 2:
            marker13.setMap(null); // 기존 마커 삭제
            marker13.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker13.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker13.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker13.setMap(naverMap2); // 마커 표시

            break;
          case 3:
            marker14.setMap(null); // 기존 마커 삭제
            marker14.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker14.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker14.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker14.setMap(naverMap2); // 마커 표시

            break;
          case 4:
            marker15.setMap(null); // 기존 마커 삭제
            marker15.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker15.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker15.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker15.setMap(naverMap2); // 마커 표시

            break;
          case 5:
            marker16.setMap(null); // 기존 마커 삭제
            marker16.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker16.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker16.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker16.setMap(naverMap2); // 마커 표시

            break;
          case 6:
            marker17.setMap(null); // 기존 마커 삭제
            marker17.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker17.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker17.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker17.setMap(naverMap2); // 마커 표시
            break;
          case 7:
            marker18.setMap(null); // 기존 마커 삭제
            marker18.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker18.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker18.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker18.setMap(naverMap2); // 마커 표시
            break;
          case 8:
            marker19.setMap(null); // 기존 마커 삭제
            marker19.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker19.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker19.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker19.setMap(naverMap2); // 마커 표시
            break;
          case 9:
            marker20.setMap(null); // 기존 마커 삭제
            marker20.setPosition(new LatLng(y, x)); // 마커 위치 재설정
            marker20.setIcon(MarkerIcons.RED); // 마커 색깔, 어린이집은 보라
            marker20.setIconTintColor(Color.BLUE); // 빨간 색 + 파란색 = 보라색
            marker20.setMap(naverMap2); // 마커 표시
            break;
          default:
            break;

        }

        count++;
      }
    }



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

        if (dataList.size() <= 10)
        {
          marker11.setTag(dataList.get(0).getName()); // 정보창
          marker11.setCaptionText(dataList.get(0).getName()); // 캡션 설정
        }
        else {
          marker11.setTag(dataList.get(10).getName()); // 정보창
          marker11.setCaptionText(dataList.get(10).getName()); // 캡션 설정

        }
        marker11.setCaptionRequestedWidth(200);
        infoWindow.open(marker11);
        return true;
      }
    });
    marker12.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker12.setTag(dataList.get(1).getName());
          marker12.setCaptionText(dataList.get(1).getName()); // 캡션 설정

        }
        else {
          marker12.setTag(dataList.get(11).getName());
          marker12.setCaptionText(dataList.get(11).getName()); // 캡션 설정

        }
        infoWindow.open(marker12);
        return true;
      }
    });
    marker13.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker13.setTag(dataList.get(3).getName());
          marker13.setCaptionText(dataList.get(3).getName()); // 캡션 설정

        }
        else {
          marker13.setTag(dataList.get(13).getName());
          marker13.setCaptionText(dataList.get(13).getName()); // 캡션 설정
        }
        infoWindow.open(marker13);
        return true;
      }
    });
    marker14.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker14.setTag(dataList.get(3).getName());
        }
        else {
          marker14.setTag(dataList.get(13).getName());
        }
        infoWindow.open(marker14);
        return true;
      }
    });
    marker15.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker15.setTag(dataList.get(4).getName());
        }
        else {
          marker15.setTag(dataList.get(14).getName());
        }
        infoWindow.open(marker15);
        return true;
      }
    });
    marker16.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker16.setTag(dataList.get(5).getName());
        }
        else {
          marker16.setTag(dataList.get(15).getName());
        }
        infoWindow.open(marker16);
        return true;
      }
    });
    marker17.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker14.setTag(dataList.get(6).getName());
        }
        else {
          marker14.setTag(dataList.get(16).getName());
        }
        infoWindow.open(marker17);

        return true;
      }
    });
    marker18.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker14.setTag(dataList.get(7).getName());
        }
        else {
          marker14.setTag(dataList.get(17).getName());
        }
        infoWindow.open(marker18);

        return true;
      }
    });
    marker19.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker14.setTag(dataList.get(8).getName());
        }
        else {
          marker14.setTag(dataList.get(18).getName());
        }
        infoWindow.open(marker19);
        return true;
      }
    });
    marker20.setOnClickListener(new Overlay.OnClickListener() {
      @Override
      public boolean onClick(@NonNull @NotNull Overlay overlay) {
        if (dataList.size() <= 10)
        {
          marker14.setTag(dataList.get(9).getName());
        }
        else {
          marker14.setTag(dataList.get(19).getName());
        }
        infoWindow.open(marker20);
        return true;
      }
    });

  }




}

