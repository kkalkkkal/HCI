package com.example.hci_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hci_project.bean.FavoriteSchoolManager;
import com.example.hci_project.bean.SchoolManager;
import com.example.hci_project.bean.SearchResultManager;

import kotlin.Unit;

public class IntroActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // 스플래시 화면 생성
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_intro);
    if (getSupportActionBar() != null)
      getSupportActionBar().hide();
    
    new Thread(() -> {
      //ready data
      SchoolManager.Companion.getInstance().use(this, (n) -> Unit.INSTANCE);
      SearchResultManager.Companion.getInstance().use(this, (n) -> Unit.INSTANCE);
      FavoriteSchoolManager.Companion.getInstance().use(this, (n) -> Unit.INSTANCE);
      
      //wait 1.5s
      try {
        Thread.sleep((long) (1.5 * 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      runOnUiThread(() -> {
        Intent mainintent = new Intent(this, MainCopyActivity.class);
//        Intent mainintent = new Intent(this, MainActivity.class);
        startActivity(mainintent); // 메인 액티비티로 이동
        finish();
      });
    }).start();
  }
}
