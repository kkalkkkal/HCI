package com.example.hci_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.map.MapFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView schoolSearchTv;
    private ViewPager2 viewPager;
    private List<Fragment> viewPagerFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schoolSearchTv= findViewById(R.id.search_school_tv);
        viewPager= findViewById(R.id.main_viewPager);

        viewPagerFragmentList.add(new MapFragment());
        viewPagerFragmentList.add(new CompareSchoolFragment());
        viewPagerFragmentList.add(new BookmarkActivity());

        initUI();
    }
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
}