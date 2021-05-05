package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.naver.maps.map.NaverMapSdk;

import static java.lang.Thread.sleep;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 스플래시 화면 생성

        Intent mainintent = new Intent(this, MainActivity.class);
        startActivity(mainintent); // 메인 액티비티로 이동
        finish();
    }
}
