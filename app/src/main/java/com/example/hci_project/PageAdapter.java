package com.example.hci_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class PageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;
    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        //페이지 항목들을 ArrayList에 추가
        mData = new ArrayList<>();
        mData.add(new MapFragment());
        mData.add(new ComparisonActivity());
        mData.add(new BookmarkActivity());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (position+1)+ "번째";
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
