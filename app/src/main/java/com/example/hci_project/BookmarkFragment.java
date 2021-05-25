package com.example.hci_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_project.bean.FavoriteSchoolManager;
import com.example.hci_project.bean.SearchResult;

import kotlin.Unit;

/**
 * A fragment representing a list of Items.
 */
public class BookmarkFragment extends Fragment {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bookmark_activity_list, container, false);
    return view;
  }
  
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    refreshList();
  }
  
  public void refreshList() {
    if (getView() == null)
      return;
  
    Handler handler= new Handler(new Handler.Callback() {
      @Override
      public boolean handleMessage(@NonNull Message msg) {
        return false;
      }
    });
    FavoriteSchoolManager.Companion.getInstance().use(getContext(), (manager) -> {
      handler.post(()->{
        if (manager == null) {
          Toast.makeText(getContext(), "즐겨찾기 리스트를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
          return;
        }
  
        SearchResultRecyclerViewAdapter adapter = new SearchResultRecyclerViewAdapter(SearchResult.Companion.convert(manager.getList()), school -> {
          Intent intent = new Intent(getContext(), SchoolInfoActivity.class);
          intent.putExtra("school", school);
          getContext().startActivity(intent);
          return Unit.INSTANCE;
        });
  
        RecyclerView recyclerView = getView().findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
  
        TextView msgView = getView().findViewById(R.id.msg);
        msgView.setText(String.format("%d개의 즐겨찾기 유치원/어린이집이 있습니다", manager.getList().size()));
      });
      return Unit.INSTANCE;
    });
  }
  
  
}