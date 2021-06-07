package com.example.hci_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hci_project.bean.FavoriteSchoolManager;
import com.example.hci_project.bean.SchoolManager;
import com.example.hci_project.bean.SearchResultManager;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import kotlin.Unit;

public class IntroActivity extends AppCompatActivity {
  //데이터베이스
  static public Sheet sheet0; // 어린이집 기본 현황
  static public Sheet sheet1; // 유치원 보험가입 현황
  static public Sheet sheet2; // 유치원 공제회 가입 현황
  static public Sheet sheet3; // 유치원 안전점검 및 안전점검 실시 현황
  static public Sheet sheet4; // 유치원 환경 위생 관리 현황
  static public Sheet sheet5; // 유치원 근속 연수 현황
  static public Sheet sheet6; // 유치원 통학 차량 현황
  static public Sheet sheet7; // 유치원 급식 운영 현황
  static public Sheet sheet8; // 유치원 수업 일수 현황
  static public Sheet sheet9; // 유치원 직위 자격별 교직원 현황
  static public Sheet sheet10; // 유치원 교실 면적 현황
  static public Sheet sheet11; // 유치원 기본 현황
  static public Sheet sheet12; // 유치원 건물 현황
  
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

      comeonDB();
      
      //wait 1.5s
      try {
        Thread.sleep((long) (1.5 * 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      runOnUiThread(() -> {
        Intent mainintent = new Intent(this, MainCopyActivity.class);
        startActivity(mainintent); // 메인 액티비티로 이동
        finish();
      });
    }).start();
  }


  public void comeonDB() { // DB 불러오기
    try {
      InputStream is = getBaseContext().getResources().getAssets().open("kindergardenDB.xls"); // 유치원 현황
      InputStream is2 = getBaseContext().getResources().getAssets().open("childhomeDB.xls"); // 어린이집 현황
      Workbook wb = Workbook.getWorkbook(is);
      Workbook wb2 = Workbook.getWorkbook(is2);

      if(wb != null) {
        sheet1 = wb.getSheet(0);   // 0번 째  '유치원 보험가입 현황' 시트 불러오기
        if(sheet1 != null) {
          int colTotal = sheet1.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet1.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet1.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }

        sheet2 = wb.getSheet(1);   // 1번 째 시트 '유치원 공제회 가입 현황' 불러오기
        if(sheet2 != null) {
          int colTotal = sheet2.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet2.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet2.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }

        sheet3 = wb.getSheet(2);   // 2번 째 시트 '유치원 안전점검 및 안전점검 실시 현황' 불러오기
        if(sheet3 != null) {
          int colTotal = sheet3.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet3.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet3.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }

        sheet4 = wb.getSheet(3);   // 3번 째 시트 '유치원 환경 위생 관리 현황' 불러오기
        if(sheet4 != null) {
          int colTotal = sheet4.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet4.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet4.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet5 = wb.getSheet(4);   // 4번 째 시트 '유치원 근속 연수 현황'불러오기
        if(sheet5 != null) {
          int colTotal = sheet5.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet5.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet5.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet6 = wb.getSheet(5);   // 5번 째 시트 '유치원 통학 차량 현황' 불러오기
        if(sheet6 != null) {
          int colTotal = sheet6.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet6.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet6.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet7 = wb.getSheet(6);   // 6번 째 시트 '급식 운영 현황' 불러오기
        if(sheet7 != null) {
          int colTotal = sheet7.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet7.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet7.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet8 = wb.getSheet(7);   // 7번 째 시트 '유치원 수업일수 현황'
        if(sheet8 != null) {
          int colTotal = sheet8.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet8.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet8.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet9 = wb.getSheet(8);   // 8번 째 시트 '유치원 직위 자격별 교직원 현황' 불러오기
        if(sheet9 != null) {
          int colTotal = sheet9.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet9.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet9.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet10 = wb.getSheet(9);   // 9번 째 시트 '유치원 교실 면적 현황' 불러오기
        if(sheet10 != null) {
          int colTotal = sheet10.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet10.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet10.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }


        sheet11 = wb.getSheet(10);   // 10번 째 시트 '유치원 기본 현황' 불러오기
        if(sheet11 != null) {
          int colTotal = sheet11.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet11.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet11.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }

        sheet12 = wb.getSheet(11);   // 11번 째 시트 '유치원 건물 현황' 불러오기
        if(sheet12 != null) {
          int colTotal = sheet12.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet12.getColumn(colTotal-1).length;

          StringBuilder sb;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet12.getCell(col, row).getContents();
              sb.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb.toString()); // 가져오는지 로그 확인
          }
        }
      }

      if(wb2 != null) {
        sheet0 = wb2.getSheet(0);   // 어린이집 시트 불러오기
        if(sheet0 != null) {
          int colTotal = sheet0.getColumns();    // 전체 컬럼
          int rowIndexStart = 1;                  // row 인덱스 시작
          int rowTotal = sheet0.getColumn(colTotal-1).length;

          StringBuilder sb2;
          for(int row=rowIndexStart;row<rowTotal;row++) {
            sb2 = new StringBuilder();
            for(int col=0;col<colTotal;col++) {
              String contents = sheet0.getCell(col, row).getContents();
              sb2.append("col"+col+" : "+contents+" , ");
            }
            Log.i("test", sb2.toString()); // 가져오는지 로그 확인
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (BiffException e) {
      e.printStackTrace();
    }
  }
}
