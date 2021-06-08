package com.example.hci_project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_project.bean.School;
import com.example.hci_project.bean.SchoolManager;
import com.google.android.material.chip.Chip;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import kotlin.Unit;

public class SchoolOnMapFragment extends Fragment implements OnMapReadyCallback {
  
  static final int PERMISSIONS_REQUEST = 0x0000001;
  
  private TextView schoolSearchTv;
  
  private SlidingUpPanelLayout slidingUpPanelLayout;
  private RecyclerView recyclerView;
  private LinearLayout linearLayout;
  private LinearLayout linearLayout2;
  private TextView schoolTitle;
  private TextView resultCntText;
  private NaverMap naverMap;
  
  // 메인 간이 필터
  private Chip kinder_chip; // 유치원 필터
  private Chip child_chip; // 어린이집 필터
  
  private MapFragment mapFragment;
  static InfoWindow infoWindow = new InfoWindow();
  public FusedLocationSource locationSource;
  
  
  // marker [0~9: 유치원, 10~19: 어린이집]
  Marker[] markers = new Marker[100];
  
  private Runnable waitingInit = null;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_school_on_map, container, false);
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    
    if (waitingInit != null && checkRequiredPermissions()) {
      waitingInit.run();
      waitingInit = null;
    }
  }
  
  public SchoolOnMapFragment init() {
    //entry point from activity
    waitingInit = () -> {
      if (getView() == null)
        return;
      
      locationSource =
          new FusedLocationSource(this, MainCopyActivity.LOCATION_PERMISSION_REQUEST_CODE); // 현재 위치 갱신
      
      infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getContext()) { // 정보창
        @NonNull
        @Override
        public CharSequence getText(@NonNull InfoWindow infoWindow) {
          School school = (School) infoWindow.getMarker().getTag();
          return school.getName();// 태그에 적힌 값이 출력됨.
        }
      });
      NaverMapOptions options = new NaverMapOptions() // 초기 화면 위치, 경도 생성자 설정
          .camera(new CameraPosition(new LatLng(37.543344020789625, 127.07557079824849), 13)) // 건국대 기준으로 지도를 연다.
          .mapType(NaverMap.MapType.Basic);
      options.locationButtonEnabled(true).tiltGesturesEnabled(false);
      
      mapFragment = MapFragment.newInstance(options); // 옵션 설정
      getChildFragmentManager().beginTransaction().add(R.id.__naverMap_container, mapFragment).commit();
      
      slidingUpPanelLayout = getView().findViewById(R.id.sliding_layout);
      kinder_chip = getView().findViewById(R.id.kinder_chips);
      child_chip = getView().findViewById(R.id.child_chips);
      schoolSearchTv = getView().findViewById(R.id.search_school_tv);
      linearLayout = getView().findViewById(R.id.dragView);
      linearLayout2 = getView().findViewById(R.id.common_result);
      schoolTitle = getView().findViewById(R.id.commonTitle);
      resultCntText = getView().findViewById(R.id.resultCntText);
      
      
      schoolSearchTv.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchSchoolActivity.class)));
      
      
      kinder_chip.setChecked(true); // 처음에는 체크 X
      child_chip.setChecked(true);
      
      kinder_chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
        removeAllMarkers(0, markers.length - 1);
        showSchoolMarker();
        
        if (isChecked) {
          Toast.makeText(getContext(), "유치원 위치도 표시합니다", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getContext(), "유치원 위치를 표시하지 않습니다", Toast.LENGTH_SHORT).show();
        }
      });
      child_chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
        removeAllMarkers(0, markers.length - 1);
        showSchoolMarker();
        
        if (isChecked) {
          Toast.makeText(getContext(), "어린이집 위치도 표시합니다", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getContext(), "어린이집 위치를 표시하지 않습니다", Toast.LENGTH_SHORT).show();
        }
      });
      // 리사이클러뷰에 LinearLayoutManager 객체 지정.
      recyclerView = getView().findViewById(R.id.commonRecycler);
      LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
      recyclerView.setLayoutManager(manager); // LayoutManager 등록
      
      mapFragment.getMapAsync(this);
    };
    
    if (getView() != null && checkRequiredPermissions()) {
      waitingInit.run();
      waitingInit = null;
    }
    return this;
  }
  
  
  private void removeAllMarkers(int start, int end) {
    for (int i = start; i <= end; i++) {
      if (markers[i] != null)
        markers[i].setMap(null);
    }
  }
  
  private Marker useMarker(int index) {
    //dynamic alloc
    if (markers[index] == null)
      markers[index] = new Marker();
    
    return markers[index];
  }
  
  public void showSchoolMarker() {
    LatLng latLng;
    Location mlocationSource = locationSource.getLastLocation();
    if (mlocationSource != null) {
      latLng = new LatLng(mlocationSource.getLatitude(), mlocationSource.getLongitude());
    } else {
      // 건국대 기준
      latLng = new LatLng(37.541680773674464, 127.07943250328056);
    }
    
    LatLng finalLatLng = latLng;
    SchoolManager.Companion.getInstance().use(getContext(), (schoolManager) -> {
      getActivity().runOnUiThread(() -> {
        if (schoolManager == null) {
          Toast.makeText(getContext(), "유치원 정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
          return;
        }
        int markIndex = 0;
        ArrayList<School> schoolList = schoolManager.sortByLocation(finalLatLng);
        ArrayList<School> shownSchoolList = new ArrayList<>();
        for (School school : schoolList.subList(0, 20)) {
          if (markIndex < markers.length) {
            Marker marker = useMarker(markIndex);
            marker.setMap(null); // 기존 마커 삭제
            
            if (school.getOnlySchoolType().equals(School.Companion.getTYPE_CHILD()) && !child_chip.isChecked()) // 어린이집
              continue;
            else if (school.getOnlySchoolType().equals(School.Companion.getTYPE_KINDER()) && !kinder_chip.isChecked()) // 유치원
              continue;
            
            marker.setPosition(new LatLng(school.getLat(), school.getLng())); // 마커 위치 재설정
            marker.setTag(school);
            // 마커 색깔, 유치원은 노랑
            if (school.getOnlySchoolType().equals(School.Companion.getTYPE_CHILD())) { // 어린이집
              marker.setIcon(MarkerIcons.BLACK);
              marker.setIconTintColor(Color.rgb(170,0,170)); // 빨간 색 + 파란색 = 보라색
              marker.setCaptionText(school.getName()); // 캡션 설정
            } else { // 유치원
              marker.setIcon(MarkerIcons.YELLOW);
              marker.setIconTintColor(Color.TRANSPARENT);
              marker.setCaptionText(school.getName()); // 캡션 설정
            }
            marker.setCaptionRequestedWidth(200);
            marker.setMap(naverMap); // 마커 표시
          }
          shownSchoolList.add(school);




          markIndex++;
        }
        
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        recyclerView.setAdapter(new CommonAdapter(shownSchoolList));  // Adapter 등록
        resultCntText.setText(String.format("근처에 %d개의 유치원/어린이집이 있습니다", shownSchoolList.size()));

        // 각 마커 클릭 리스너
        for (int i = 0; i <= markIndex; i++) {
          Marker marker = useMarker(i);
          int finalI = i;
          marker.setOnClickListener(overlay -> {
            infoWindow.open(marker);
//            getView().findViewById(R.id.dragView).setVisibility(View.INVISIBLE);
//            ((MainCopyActivity)getActivity()).changeView(finalI + 1, shownSchoolList.get(finalI));
            infoWindow.setOnClickListener(overlay1 -> {
              Intent intent = new Intent(getContext(), SchoolInfoActivity.class);
              intent.putExtra("school", (School) marker.getTag());
              startActivity(intent);
              
              return false;
            });
            return false;
          });
        }
      });
      return Unit.INSTANCE;
    });
  }

  //위치 권한 설정
  public boolean checkRequiredPermissions() {
    boolean allGranted = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    if (!allGranted) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
        Toast.makeText(getContext(), "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(getActivity(),
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
            PERMISSIONS_REQUEST);
      } else {
        ActivityCompat.requestPermissions(getActivity(),
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
            PERMISSIONS_REQUEST);
      }
    }
    
    return allGranted;
  }
  
  @Override
  public void onMapReady(@NonNull NaverMap naverMap) {
    this.naverMap = naverMap;
    
    naverMap.setMapType(NaverMap.MapType.Basic); // 기본형 지도
    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true); // 빌딩 그룹 생성
    
    
    // 네이버 지도 소스 권한
    if (!checkRequiredPermissions()) { // 권한 거부됨
      naverMap.setLocationTrackingMode(LocationTrackingMode.None);
    } else {
      naverMap.setLocationSource(locationSource);
      naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); // 위치 추적 모드 실행
    }
    
    
    //네이버 지도 추가 UI 설정
    UiSettings uiSettings = naverMap.getUiSettings();
    uiSettings.setLocationButtonEnabled(true); // 현 위치 버튼 활성화
    
    showSchoolMarker();
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(
        requestCode, permissions, grantResults);
    
    switch (requestCode) {
      case PERMISSIONS_REQUEST:
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
          Toast.makeText(getContext(), "위치 권한이 없으면 가까운 거리의 유치원을 확인할 수 없습니다", Toast.LENGTH_LONG).show();
        }
        init();
        break;
    }
    
    // 네이버 지도 소스 권한
    if (locationSource != null && locationSource.onRequestPermissionsResult(
        requestCode, permissions, grantResults)) {
      if (!locationSource.isActivated() && naverMap != null) { // 권한 거부됨
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
      }
    }
  }
  
  public boolean closeSlidePanel() {
    if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
      slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
      return true;
    }
    return false;
  }




}
