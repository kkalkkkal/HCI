package com.example.hci_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CompareSchoolFragment extends Fragment {
    public CompareSchoolFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_compare_school, container, false);
        rootView.findViewById(R.id.start_compare).setOnClickListener(view->{
            startActivity(new Intent(getContext(), CompareSchoolActivity.class));
        });
        return rootView;
    }
}