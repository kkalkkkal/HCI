package com.example.hci_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

// Json 파싱
// 엑셀 파일 불러오기 라이브러리


public class MainCopyActivity extends AppCompatActivity {
  
  private ViewPager2 viewPager;
  private List<Fragment> viewPagerFragmentList = new ArrayList<>();
  private BookmarkFragment bookmarkFragment = new BookmarkFragment();
  private SchoolOnMapFragment schoolOnMapFragment = new SchoolOnMapFragment();
  
  // 네이버 자기 위치 디폴트 코드
  public static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_copy);
    
    init();
  }
  
  private void init() {
    viewPager = findViewById(R.id.main_viewPager);
    
    viewPagerFragmentList.add(schoolOnMapFragment.init());
    viewPagerFragmentList.add(new CompareSchoolFragment());
    viewPagerFragmentList.add(bookmarkFragment);
    
    initUI(); // UI 초기화
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
    
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      switch (item.getTitle().toString()) {
        case "주변":
          viewPager.setCurrentItem(0);
          break;
        case "비교":
          viewPager.setCurrentItem(1);
          break;
        case "즐겨찾기":
          viewPager.setCurrentItem(2);
          bookmarkFragment.refreshList();
          break;
      }
      return true;
    });
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    bookmarkFragment.refreshList();
  }
  
  private long lastBackBtnClicked = 0;
  
  @Override
  public void onBackPressed() {
    if (viewPager.getCurrentItem() != 0 || !schoolOnMapFragment.closeSlidePanel()) {
      if (System.currentTimeMillis() - lastBackBtnClicked > 1000) {
        Toast.makeText(this, "앱을 종료하려면 뒤로가기 버튼을 한번 더 누르세요", Toast.LENGTH_SHORT).show();
        lastBackBtnClicked = System.currentTimeMillis();
      } else {
        super.onBackPressed();
      }
    }
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    schoolOnMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}

