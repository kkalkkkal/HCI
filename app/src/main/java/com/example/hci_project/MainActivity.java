package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.naver.maps.map.NaverMapSdk;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 네이버 지도 SDK 접속
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("o82z0vth6u"));

        //
    }
}