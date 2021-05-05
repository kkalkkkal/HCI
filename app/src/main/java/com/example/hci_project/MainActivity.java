package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.naver.maps.map.NaverMapSdk;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    TabLayout tablayout;
    LinearLayout frame_in_layout_1, frame_in_layout_2, frame_in_layout_3;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablayout = findViewById(R.id.tablayout);
        tablayout.addOnTabSelectedListener(onTabSelectedListener);

        frame_in_layout_1 = findViewById(R.id.frame_in_layout_1);
        frame_in_layout_2 = findViewById(R.id.frame_in_layout_2);
        frame_in_layout_3 = findViewById(R.id.frame_in_layout_3);

        context = this;

        // 네이버 지도 SDK 접속
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("o82z0vth6u"));


        //
    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
    // 탭의 상태가 선택상태로 변경(선택되지 않음 -> 선택으로)

    //            TabLayout.Tab 관련 중요 메소드
    //        int getPosition() : 탭의 순서를 리턴. (TabLayout에 추가된 순서)
    //        Object getTag() : 탭에 지정된 태그(tag) 객체를 리턴. (setTag()로 전달한 객체.)
    //        CharSequence getText() : 탭에에 표시된 텍스트 문자열 리턴. (setText()로 설정한 텍스트.)
    //        boolean isSelected() : Tab 선택 여부 리턴.

            int pos = tab.getPosition();
            changeView(pos);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
    // 탭의 상태가 선택되지 않음으로 변경(선택 -> 선택되지 않음으로)
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        // 이미 선택된 탭이 다시 선택됨(선택 -> 선택으로)
        }
    };

    private void changeView(int index) { // 숨기고 표시로 화면 전환을 만들어 둠
        switch (index) {
        case 0 :
        frame_in_layout_1.setVisibility(View.VISIBLE) ;
        frame_in_layout_2.setVisibility(View.INVISIBLE) ;
        frame_in_layout_3.setVisibility(View.INVISIBLE) ;
        break ;
        case 1 :
        frame_in_layout_1.setVisibility(View.INVISIBLE) ;
        frame_in_layout_2.setVisibility(View.VISIBLE) ;
        frame_in_layout_3.setVisibility(View.INVISIBLE) ;
        break ;
        case 2 :
        frame_in_layout_1.setVisibility(View.INVISIBLE) ;
        frame_in_layout_2.setVisibility(View.INVISIBLE) ;
        frame_in_layout_3.setVisibility(View.VISIBLE) ;
        break ;

        }
    }
}