package com.example.hci_project;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_project.bean.FavoriteSchoolManager;
import com.example.hci_project.bean.School;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
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

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> implements Serializable {

    private List<School> myDataList = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        LinearLayout clickableLayout;

        ImageButton addFavoriteBtn;
        ImageButton addCompareBtn;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.the_Type);
            textView2 = itemView.findViewById(R.id.the_name);
            textView3 = itemView.findViewById(R.id.the_add);
            textView4 = itemView.findViewById(R.id.the_phone);
            clickableLayout = itemView.findViewById(R.id.clickable);

            addFavoriteBtn = itemView.findViewById(R.id.addFavoriteList);
            addCompareBtn = itemView.findViewById(R.id.addCompareList);
        }
    }

    CommonAdapter(List<School> dataList) {
        this.myDataList = dataList;
    }

    private Context context = null;

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.fragment_common_recycler, parent, false);
        CommonAdapter.ViewHolder viewHolder = new CommonAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                return false;
            }
        });
        School school = myDataList.get(position);

        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        // Type 유형
        holder.textView1.setText(String.format("[%s]", school.getServiceType()));
        // Name
        holder.textView2.setText(school.getName());
        // 주소
        holder.textView3.setText(school.getAddr());
        // 전화번호
        holder.textView4.setText(school.getTel().contains("-") ? school.getTel() : "없음");
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SchoolInfoActivity.class);

            intent.putExtra("school", school);
            context.startActivity(intent);

        });

        // 비교군 추가
        holder.addCompareBtn.setOnClickListener((v) -> {
            try {

                File dir = new File(context.getFilesDir().toString());
                File f = new File(dir, "UserDB.xls");

                Workbook workbook;

                if (!f.exists()) {
                    f.createNewFile();
                    workbook = Workbook.getWorkbook(context.getAssets().open("UserDB.xls"));

                    //throw new Exception("file not found");
                } else {
                    f = new File(String.valueOf(context.getFileStreamPath("UserDB.xls")));
                    workbook = Workbook.getWorkbook(f);
                }

                if (!f.canRead()) {
                    throw new Exception("can't read file");
                }

                if (workbook == null) {
                    throw new Exception("Workbook is null!!");
                } else {
                    File newExcel = new File(dir, "UserDB.xls");
                    WritableWorkbook writeBook = Workbook.createWorkbook(newExcel, workbook);
                    WritableSheet writeSheet = writeBook.getSheet(0);

                    if (writeSheet != null) {
                        int colTotal = writeSheet.getColumns();    // 전체 컬럼
                        int rowIndexStart = 1;                  // row 인덱스 시작
                        int rowTotal = writeSheet.getRows(); // 행 개수 (이미 입력 되어있는 데이터 값)

                        WritableCell cell = null;

                        if (writeSheet.findCell(school.getName()) != null) { // 이미 있으면 삭제
                            writeSheet.removeRow(writeSheet.findCell(school.getName()).getRow()); // 해당 행 삭제
                            Toast.makeText(context.getApplicationContext(), "compare_delete", Toast.LENGTH_LONG).show();

                        } else { // 없으면 추가
                            Toast.makeText(context.getApplicationContext(), "compare_added", Toast.LENGTH_LONG).show();
                            Label label = new Label(0, rowTotal, Integer.toString(rowTotal)); // 번호
                            writeSheet.addCell(label);

                            Label label2 = new Label(1, rowTotal, school.getName()); // 유치원 or 어린이집 이름
                            writeSheet.addCell(label2);

                            Label label3 = new Label(2, rowTotal, Float.toString(school.getKidsPerTeacher())); // 선생 당 학생 수 (int)
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

        });

        // 즐겨찾기 추가
        FavoriteSchoolManager.Companion.getInstance().use(context, manager -> {
            if (manager == null)
                return Unit.INSTANCE;

            if (manager.isFavorite(school)) {
                holder.addFavoriteBtn.setColorFilter(context.getResources().getColor(R.color.yellow));
            } else {
                holder.addFavoriteBtn.setColorFilter(context.getResources().getColor(R.color.gray_light));
            }
            holder.addFavoriteBtn.setOnClickListener(v -> {
                //action
                if (manager.isFavorite(school)) {
                    Iterator<School> iterator = manager.getList().iterator();
                    while (iterator.hasNext()) {
                        School s = iterator.next();
                        if (s.getName().equals(school.getName())) {
                            iterator.remove();
                        }
                    }
                } else {
                    manager.getList().add(school);
                }
                //ui update
                if (manager.isFavorite(school)) {
                    Toast.makeText(context, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                    holder.addFavoriteBtn.setColorFilter(context.getResources().getColor(R.color.yellow));
                } else {
                    Toast.makeText(context, "즐겨찾기에서 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    holder.addFavoriteBtn.setColorFilter(context.getResources().getColor(R.color.gray_light));
                }
                //save action result
                manager.save(context);
            });
            return Unit.INSTANCE;
        });
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }


    public class CommonHolder extends RecyclerView.ViewHolder{
        public CommonHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

}
