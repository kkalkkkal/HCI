package com.example.hci_project;

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

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hci_project.bean.School;
import com.example.hci_project.bean.SchoolManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

// Json 파싱
// 엑셀 파일 불러오기 라이브러리


public class MainCopyActivity extends AppCompatActivity implements OnMapReadyCallback {
  
  private TextView schoolSearchTv;
  private ViewPager2 viewPager;
  private List<Fragment> viewPagerFragmentList = new ArrayList<>();
  
  private RecyclerView recyclerView;
  private LinearLayout linearLayout;
  private LinearLayout linearLayout2;
  private TextView schoolTitle;
  
  // 메인 간이 필터
  private Chip kinder_chip; // 유치원 필터
  private Chip child_chip; // 어린이집 필터
  
  
  // 네이버 자기 위치 디폴트 코드
  private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  public FusedLocationSource locationSource;
  static public NaverMap naverMap; // 네이버 맵 객체 - 다른 곳에서도 접근 가능하게 하려고 일단 전역변수화함.
  
  
  static final int PERMISSIONS_REQUEST = 0x0000001;
  
  
  // marker [0~9: 유치원, 10~19: 어린이집]
  Marker[] markers = new Marker[100];
  
  static InfoWindow infoWindow = new InfoWindow();
  static MapFragment mapFragment;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    if (checkRequiredPermissions()) {
      init();
    }
  }
  
  private void init() {
    locationSource =
        new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); // 현재 위치 갱신
    
    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) { // 정보창
      @NonNull
      @Override
      public CharSequence getText(@NonNull InfoWindow infoWindow) {
        return (CharSequence) infoWindow.getMarker().getTag(); // 태그에 적힌 값이 출력됨.
      }
    });
    
    // 네이버 지도 설정
    NaverMapOptions options = new NaverMapOptions() // 초기 화면 위치, 경도 생성자 설정
        .camera(new CameraPosition(new LatLng(37.543344020789625, 127.07557079824849), 13)) // 건국대 기준으로 지도를 연다.
        .mapType(NaverMap.MapType.Basic);
    options.locationButtonEnabled(true).tiltGesturesEnabled(false);
    
    mapFragment = MapFragment.newInstance(options); // 옵션 설정
    
    
    kinder_chip = findViewById(R.id.kinder_chips);
    child_chip = findViewById(R.id.child_chips);
    schoolSearchTv = findViewById(R.id.search_school_tv);
    viewPager = findViewById(R.id.main_viewPager);
    linearLayout = findViewById(R.id.dragView);
    linearLayout2 = findViewById(R.id.common_result);
    schoolTitle = findViewById(R.id.commonTitle);
    
    
    viewPagerFragmentList.add(mapFragment); // 커스텀 맵 프래그먼트 : 기존 new MapFragment 대체
    viewPagerFragmentList.add(new CompareSchoolFragment());
    viewPagerFragmentList.add(new BookmarkActivity());
    
    initUI(); // UI 초기화
  }
  
  private void removeAllMarkers(int start, int end) {
    for (int i = start; i <= end; i++) {
      useMarker(i).setMap(null);
    }
  }
  
  private Marker useMarker(int index) {
    //dynamic alloc
    if (markers[index] == null)
      markers[index] = new Marker();
    
    return markers[index];
  }
  
  // 처음 UI 설정
  private void initUI() {
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
    
    schoolSearchTv.setOnClickListener(v -> startActivity(new Intent(MainCopyActivity.this, SearchSchoolActivity.class)));
    
    
    kinder_chip.setChecked(false); // 처음에는 체크 X
    child_chip.setChecked(false);
    
    kinder_chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
      removeAllMarkers(0, markers.length - 1);
      showSchoolMarker();
    });
    child_chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
      removeAllMarkers(0, markers.length - 1);
      showSchoolMarker();
    });
    
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      schoolSearchTv.setVisibility(View.GONE);
      
      switch (item.getTitle().toString()) {
        case "주변":
          viewPager.setCurrentItem(0);
          break;
        case "비교":
          viewPager.setCurrentItem(1);
          break;
        case "즐겨찾기":
          viewPager.setCurrentItem(2);
          break;
      }
      if (item.getTitle().equals("주변")) {
        kinder_chip.setVisibility(View.VISIBLE);
        child_chip.setVisibility(View.VISIBLE);
        schoolSearchTv.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout2.setVisibility(View.VISIBLE);
        schoolTitle.setVisibility(View.VISIBLE);
        linearLayout.setClickable(true);
        linearLayout.setFocusable(true);
      } else {
        kinder_chip.setVisibility(View.INVISIBLE);
        child_chip.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
        linearLayout2.setVisibility(View.INVISIBLE);
        schoolTitle.setVisibility(View.INVISIBLE);
      }
      return true;
    });
    mapFragment.getMapAsync(this);
  }
  
  //위치 권한 설정
  public boolean checkRequiredPermissions() {
    boolean allGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    if (!allGranted) {
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
    
    return allGranted;
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
    
    showSchoolMarker();
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
    SchoolManager.Companion.getInstance().use(this, (schoolManager) -> {
      runOnUiThread(() -> {
        if (schoolManager == null) {
          Toast.makeText(this, "유치원 정보를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
          return;
        }
        int markIndex = 0;
        ArrayList<School> schoolList = schoolManager.sortByLocation(finalLatLng);
        for (School school : schoolList.subList(0, 20)) {
          if (markIndex < markers.length) {
            Marker marker = useMarker(markIndex);
            marker.setMap(null); // 기존 마커 삭제
            
            if (school.getOnlySchoolType().equals(School.Companion.getTYPE_CHILD()) && child_chip.isChecked())
              continue;
            else if (school.getOnlySchoolType().equals(School.Companion.getTYPE_KINDER()) && kinder_chip.isChecked())
              continue;
            
            marker.setPosition(new LatLng(school.getLat(), school.getLng())); // 마커 위치 재설정
            
            // 마커 색깔, 유치원은 노랑
            if (school.getOnlySchoolType().equals(School.Companion.getTYPE_CHILD())) {
              marker.setIcon(MarkerIcons.YELLOW);
            } else {
              marker.setIcon(MarkerIcons.RED);
            }
            marker.setIconTintColor(Color.TRANSPARENT);
            marker.setMap(naverMap); // 마커 표시
          }
          
          markIndex++;
        }
        
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.commonRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        recyclerView.setAdapter(new commonAdapter(schoolManager.getList()));  // Adapter 등록
        
        // 각 마커 클릭 리스너
        for (int i = 0; i <= 9; i++) {
          Marker marker = useMarker(i);
          marker.setOnClickListener(overlay -> {
            marker.setTag("마커 1");
            infoWindow.open(marker);
            return false;
          });
        }
      });
      return Unit.INSTANCE;
    });
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
          Toast.makeText(this, "위치 권한이 없으면 가까운 거리의 유치원을 확인할 수 없습니다", Toast.LENGTH_LONG).show();
        }
        init();
        break;
    }
    
    // 네이버 지도 소스 권한
    if (locationSource.onRequestPermissionsResult(
        requestCode, permissions, grantResults)) {
      if (!locationSource.isActivated()) { // 권한 거부됨
        naverMap.setLocationTrackingMode(LocationTrackingMode.None);
      }
    }
  }
}

