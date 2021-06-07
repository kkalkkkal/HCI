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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;

public class commonAdapter extends RecyclerView.Adapter<commonAdapter.ViewHolder> implements Serializable {
  
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
  
  commonAdapter(List<School> dataList)  {
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
    commonAdapter.ViewHolder viewHolder = new commonAdapter.ViewHolder(view);
    
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
    if(school.getType().contains("법인"))
    {
      holder.textView1.setText("법인"); // 두 글자만
    } else if (school.getType().contains("가정")){
      holder.textView1.setText("가정");
    } else if (school.getType().contains("민간")){
      holder.textView1.setText("민간");
    } else if (school.getType().contains("협동")){
      holder.textView1.setText("협동");
    } else if (school.getType().contains("사립")){
      holder.textView1.setText("사립");
    } else if (school.getType().contains("병설")){
      holder.textView1.setText("병설");
    } else {
      holder.textView1.setText("공립");
    }
    // Name
    holder.textView2.setText(school.getName());
    // 주소
    holder.textView3.setText(school.getAddr());
    // 전화번호
    holder.textView4.setText(school.getTel().contains("-") ? school.getTel() : "없음");
    holder.clickableLayout.setOnClickListener(v -> {
      Intent intent = new Intent(context, SchoolInfoActivity.class);

      intent.putExtra("school", school);
      context.startActivity(intent);

    });
    
    // 비교군 추가
    holder.addCompareBtn.setOnClickListener((v) -> {
    
    });
    
    // 즐겨찾기 추가
    holder.addFavoriteBtn.setOnClickListener((v) -> {
      FavoriteSchoolManager.Companion.getInstance().use(context, (manager) -> {
        handler.post(() -> {
          if (manager == null) {
            Toast.makeText(context, "즐겨찾기 리스트를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
          }
          manager.getList().add(school);
          Toast.makeText(context, "즐겨찾기 목록에 추가되었습니다", Toast.LENGTH_SHORT).show();
          manager.save(context);
        });
        return Unit.INSTANCE;
      });
    });
  }
  
  @Override
  public int getItemCount() {
    return myDataList.size();
  }
  
  
}
