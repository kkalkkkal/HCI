package com.example.hci_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hci_project.bean.FavoriteSchoolManager;
import com.example.hci_project.bean.School;
import com.example.hci_project.bean.School2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import kotlin.Unit;

// Json 파싱
// 엑셀 파일 불러오기 라이브러리


public class MainCopyActivity extends AppCompatActivity {
  
  private ViewPager2 viewPager;
  private List<Fragment> viewPagerFragmentList = new ArrayList<>();
  private BookmarkFragment bookmarkFragment = new BookmarkFragment();
  private SchoolOnMapFragment schoolOnMapFragment = new SchoolOnMapFragment();
  
  // 네이버 자기 위치 디폴트 코드
  public static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_copy);
    
    init();
  }
  
  private void init() {
    viewPager = findViewById(R.id.main_viewPager);
    
    viewPagerFragmentList.add(schoolOnMapFragment.init());
    viewPagerFragmentList.add(new CompareSchoolFragment());
    viewPagerFragmentList.add(bookmarkFragment);
    
    initUI(); // UI 초기화
  }
  
  
  // 처음 UI 설정
  private void initUI() {
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
    
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);
    bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
      switch (item.getTitle().toString()) {
        case "주변":
          viewPager.setCurrentItem(0);
          break;
        case "비교":
          viewPager.setCurrentItem(1);
          break;
        case "즐겨찾기":
          viewPager.setCurrentItem(2);
          bookmarkFragment.refreshList();
          break;
      }
      return true;
    });
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    bookmarkFragment.refreshList();
  }
  
  private long lastBackBtnClicked = 0;
  
  @Override
  public void onBackPressed() {
    FrameLayout frame = (FrameLayout) findViewById(R.id.frame) ;

    if (frame.getChildCount() > 0) // 마커 클릭 화면 뒤로가기
    {
      frame.removeViewAt(0);
      findViewById(R.id.dragView).setVisibility(View.VISIBLE);
      SchoolOnMapFragment.infoWindow.close();
    }
    else {
      if (viewPager.getCurrentItem() != 0 || !schoolOnMapFragment.closeSlidePanel()) {
        if (System.currentTimeMillis() - lastBackBtnClicked > 1000) {
          Toast.makeText(this, "앱을 종료하려면 뒤로가기 버튼을 한번 더 누르세요", Toast.LENGTH_SHORT).show();
          lastBackBtnClicked = System.currentTimeMillis();
        } else {
          super.onBackPressed();
        }
      }
    }
  }
  
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    schoolOnMapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }


  public void changeView(int index, School school){

    // LayoutInflater 초기화.
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
    if (frame.getChildCount() > 0) {
      // FrameLayout에서 뷰 삭제.
      frame.removeViewAt(0);
    }

    // XML에 작성된 레이아웃을 View 객체로 변환.
    View view = null ;
    switch (index) {
      case 0 :
        //frame.removeViewAt(0); // 삭제 (뒤로가기 버튼)
        break ;
      default:
        view = inflater.inflate(R.layout.fragment_common_recycler, frame, false);
        break;

    }

    // FrameLayout에 뷰 추가.
    if (view != null) {
      frame.addView(view);

      // id 매핑
      TextView the_name = (TextView) view.findViewById(R.id.the_name);
      TextView the_type = (TextView) view.findViewById(R.id.the_Type);
      TextView the_tel = (TextView) view.findViewById(R.id.the_phone);
      TextView the_add = (TextView) view.findViewById(R.id.the_add);
      ImageButton imageButton = (ImageButton) view.findViewById(R.id.addFavoriteList); // 즐겨찾기
      ImageButton imageButton2 = (ImageButton) view.findViewById(R.id.addCompareList); // 비교

      the_name.setText(school.getName());
      // Type 유형
      if(school.getType().contains("법인"))
      {
        the_type.setText("법인"); // 두 글자만
      } else if (school.getType().contains("가정")){
        the_type.setText("가정");
      } else if (school.getType().contains("민간")){
        the_type.setText("민간");
      } else if (school.getType().contains("협동")){
        the_type.setText("협동");
      } else if (school.getType().contains("사립")){
        the_type.setText("사립");
      } else if (school.getType().contains("병설")){
        the_type.setText("병설");
      } else {
        the_type.setText("공립");
      }

      the_tel.setText(school.getTel());//dataList.get(index - 1).getTel());
      the_add.setText(school.getAddr());//dataList.get(index - 1).getAddr());

      the_name.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //Todo : 제목을 누르면 검색 결과로 이동하기
          Intent intent = new Intent(getApplicationContext(), SchoolInfoActivity.class);
          intent.putExtra("school", school);
          startActivity(intent);
        }
      });

      /*imageButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // Todo : 즐겨찾기 버튼 누르면 추가


        }
      });*/

      Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
          return false;
        }
      });

      imageButton.setOnClickListener((v) -> {
        FavoriteSchoolManager.Companion.getInstance().use(getApplicationContext(), (manager) -> {
          handler.post(() -> {
            if (manager == null) {
              Toast.makeText(getApplicationContext(), "즐겨찾기 리스트를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
              return;
            }
            manager.getList().add(school);
            Toast.makeText(getApplicationContext(), "즐겨찾기 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
            manager.save(getApplicationContext());
          });
          return Unit.INSTANCE;
        });
      });


      imageButton2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          // Todo: 비교버튼 누르면 추가
          try {

            File dir = new File(getApplicationContext().getFilesDir().toString());
            File f = new File(dir, "UserDB.xls");

            Workbook workbook;

            if(!f.exists()){
              f.createNewFile();
              workbook = Workbook.getWorkbook(getApplicationContext().getAssets().open("UserDB.xls"));

              //throw new Exception("file not found");
            } else {
              f = new File(String.valueOf(getApplicationContext().getFileStreamPath("UserDB.xls")));
              workbook = Workbook.getWorkbook(f);
            }

            if(!f.canRead()){
              throw new Exception("can't read file");
            }

            if(workbook == null){
              throw new Exception("Workbook is null!!");
            }else{
              File newExcel = new File(dir, "UserDB.xls");
              WritableWorkbook writeBook = Workbook.createWorkbook(newExcel, workbook);
              WritableSheet writeSheet = writeBook.getSheet(0);

              if(writeSheet != null) {
                int colTotal = writeSheet.getColumns();    // 전체 컬럼
                int rowIndexStart = 1;                  // row 인덱스 시작
                int rowTotal = writeSheet.getRows(); // 행 개수 (이미 입력 되어있는 데이터 값)

                WritableCell cell = null;

                if (writeSheet.findCell(school.getName()) != null){ // 이미 있으면 삭제
                  writeSheet.removeRow(writeSheet.findCell(school.getName()).getRow()); // 해당 행 삭제
                } else { // 없으면 추가
                  Label label = new Label(0, rowTotal, Integer.toString(rowTotal)); // 번호
                  writeSheet.addCell(label);

                  Label label2 = new Label(1, rowTotal, school.getName()); // 유치원 or 어린이집 이름
                  writeSheet.addCell(label2);

                  Label label3 = new Label(2, rowTotal, Integer.toString(school.getKidsPerTeacher())); // 선생 당 학생 수 (int)
                  writeSheet.addCell(label3);

                  // 버스 운행 여부 & 자동차 수
                  if (school.getType().contains("어린이집")) {
                    Cell cell2 = IntroActivity.sheet0.findCell(school.getName()); // 엑셀파일에서 cell 반환
                    int row = cell2.getRow(); // 몇 번째 행인지 반환
                    if (school.isAvailableBus() == false) {
                      Label label4 = new Label(3, rowTotal, "0"); // 버스 운행 여부 & 자동차 수
                      writeSheet.addCell(label4);
                    } else {
                      Label label4 = new Label(3, rowTotal, "1"); // 버스 운행 여부 & 자동차 수
                      writeSheet.addCell(label4);
                    }

                    // 안전 점검 + CCTV -> 데이터 없음.
                    Label label5 = new Label(4, rowTotal, "-");
                    writeSheet.addCell(label5);

                    Label label6 = new Label(5, rowTotal, "-");
                    writeSheet.addCell(label6);

                    Label label7 = new Label(6, rowTotal, "-");
                    writeSheet.addCell(label7);

                    Label label8 = new Label(7, rowTotal, "-");
                    writeSheet.addCell(label8);

                    Label label9 = new Label(8, rowTotal, "-");
                    writeSheet.addCell(label9);


                  } else { // 유치원

                    // 버스 운행 여부
                    Cell cell2 = IntroActivity.sheet6.findCell(school.getName()); // 엑셀파일에서 cell 반환
                    int row = cell2.getRow(); // 몇 번째 행인지 반환
                    Label label4 = new Label(3, rowTotal, IntroActivity.sheet6.getCell(6, row).getContents());
                    writeSheet.addCell(label4);

                    // 소방 안전 점검
                    if (school.getSafety().getFireCheck() == false) {
                      Label label5 = new Label(4, rowTotal, "X");
                      writeSheet.addCell(label5);
                    } else {
                      cell2 = IntroActivity.sheet3.findCell(school.getName());
                      row = cell2.getRow();
                      Label label5 = new Label(4, rowTotal, IntroActivity.sheet3.getCell(6, row).getContents());
                      writeSheet.addCell(label5);
                    }

                    // 전기 안전 점검
                    if (school.getSafety().getElectricCheck() == false) {
                      Label label6 = new Label(5, rowTotal, "X");
                      writeSheet.addCell(label6);

                    } else {
                      cell2 = IntroActivity.sheet3.findCell(school.getName());
                      row = cell2.getRow();
                      Label label6 = new Label(5, rowTotal, IntroActivity.sheet3.getCell(12, row).getContents());
                      writeSheet.addCell(label6);
                    }

                    // 가스 안전 점검
                    if (school.getSafety().getElectricCheck() == false) {
                      Label label7 = new Label(6, rowTotal, "X");
                      writeSheet.addCell(label7);

                    } else {
                      cell2 = IntroActivity.sheet3.findCell(school.getName());
                      row = cell2.getRow();
                      Label label7 = new Label(6, rowTotal, IntroActivity.sheet3.getCell(8, row).getContents());
                      writeSheet.addCell(label7);
                    }

                    // 놀이시설 안전 점검
                    if (school.getSafety().getElectricCheck() == false) {
                      Label label8 = new Label(7, rowTotal, "X");
                      writeSheet.addCell(label8);

                    } else {
                      cell2 = IntroActivity.sheet3.findCell(school.getName());
                      row = cell2.getRow();
                      Label label8 = new Label(7, rowTotal, IntroActivity.sheet3.getCell(14, row).getContents());
                      writeSheet.addCell(label8);
                    }

                    // CCTV 개수
                    if (school.getSafety().getElectricCheck() == false) {
                      Label label9 = new Label(8, rowTotal, "X");
                      writeSheet.addCell(label9);

                    } else {
                      cell2 = IntroActivity.sheet3.findCell(school.getName());
                      row = cell2.getRow();
                      Label label9 = new Label(8, rowTotal, IntroActivity.sheet3.getCell(17, row).getContents());
                      writeSheet.addCell(label9);
                    }
                  }
                }
                writeBook.write();//반드시 적어줘야 엑셀에 적용이 됨;
                writeBook.close();

              }
            }

          } catch (IOException | BiffException e) {
            e.printStackTrace();
          } catch (RowsExceededException e) {
            e.printStackTrace();
          } catch (WriteException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    }
  }


}

