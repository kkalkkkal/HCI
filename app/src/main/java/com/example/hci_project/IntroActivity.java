package com.example.hci_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.naver.maps.map.NaverMapSdk;

import static java.lang.Thread.sleep;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_page);


        Intent mainintent = new Intent(IntroActivity.this, MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//뒤로가기 버튼 눌렀을때 메인에서 로그인으로 못가게?
        IntroActivity.this.startActivity(mainintent); // 메인 액티비티로 이동
        finish();
    }
}
