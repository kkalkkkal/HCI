package com.example.hci_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import org.jetbrains.annotations.NotNull;


public class MapFragment extends Fragment implements OnMapReadyCallback {
  
  private MapView mapView;
  
  public MapFragment() {
    // Required empty public constructor
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.mappage, container, false);
  }
  
  
  @Override
  public void onViewCreated(@NonNull View view,
                            @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //mapView = view.findViewById(R.id.map_view);
    mapView.onCreate(savedInstanceState);
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
  public void onDestroyView() {
    super.onDestroyView();
    mapView.onDestroy();
  }
  
  @Override
  public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
  }
  
  @Override
  public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
  
  }
}