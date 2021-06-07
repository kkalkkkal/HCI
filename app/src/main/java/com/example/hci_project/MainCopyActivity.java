package com.example.hci_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hci_project.bean.School;
import com.example.hci_project.bean.School2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

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
    FrameLayout frame = (FrameLayout) findViewById(R.id.frame) ;

    if (frame.getChildCount() > 0) // 마커 클릭 화면 뒤로가기
    {
      frame.removeViewAt(0);
      findViewById(R.id.dragView).setVisibility(View.VISIBLE);
      SchoolOnMapFragment.infoWindow.close();
    }
    else {
      if (viewPager.getCurrentItem() != 0 || !schoolOnMapFragment.closeSlidePanel()) {
        if (System.currentTimeMillis() - lastBackBtnClicked > 1000) {
          Toast.makeText(this, "앱을 종료하려면 뒤로가기 버튼을 한번 더 누르세요", Toast.LENGTH_SHORT).show();
          lastBackBtnClicked = System.currentTimeMillis();
        } else {
          super.onBackPressed();
        }
      }
    }
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    schoolOnMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


  public void changeView(int index, School school){

    // LayoutInflater 초기화.
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
    if (frame.getChildCount() > 0) {
      // FrameLayout에서 뷰 삭제.
      frame.removeViewAt(0);
    }

    // XML에 작성된 레이아웃을 View 객체로 변환.
    View view = null ;
    switch (index) {
      case 0 :
        //frame.removeViewAt(0); // 삭제 (뒤로가기 버튼)
        break ;
      default:
        view = inflater.inflate(R.layout.fragment_common_recycler, frame, false);
        break;

    }

    // FrameLayout에 뷰 추가.
    if (view != null) {
      frame.addView(view);

      // id 매핑
      TextView the_name = (TextView) view.findViewById(R.id.the_name);
      TextView the_type = (TextView) view.findViewById(R.id.the_Type);
      TextView the_tel = (TextView) view.findViewById(R.id.the_phone);
      TextView the_add = (TextView) view.findViewById(R.id.the_add);
      ImageButton imageButton = (ImageButton) view.findViewById(R.id.addFavoriteList); // 즐겨찾기
      ImageButton imageButton2 = (ImageButton) view.findViewById(R.id.addCompareList); // 비교

      the_name.setText(school.getName());
      // Type 유형
      if(school.getType().contains("법인"))
      {
        the_type.setText("법인"); // 두 글자만
      } else if (school.getType().contains("가정")){
        the_type.setText("가정");
      } else if (school.getType().contains("민간")){
        the_type.setText("민간");
      } else if (school.getType().contains("협동")){
        the_type.setText("협동");
      } else if (school.getType().contains("사립")){
        the_type.setText("사립");
      } else if (school.getType().contains("병설")){
        the_type.setText("병설");
      } else {
        the_type.setText("공립");
      }

      the_tel.setText(school.getTel());//dataList.get(index - 1).getTel());
      the_add.setText(school.getAddr());//dataList.get(index - 1).getAddr());

      the_name.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //Todo : 제목을 누르면 검색 결과로 이동하기
          Intent intent = new Intent(getApplicationContext(), SchoolInfoActivity.class);
          intent.putExtra("school", school);
          startActivity(intent);
        }
      });

      imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // Todo : 즐겨찾기 버튼 누르면 추가


        }
      });

      imageButton2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // Todo: 비교버튼 누르면 추가

        }
      });
    }
  }


}

