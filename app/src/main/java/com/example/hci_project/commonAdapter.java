package com.example.hci_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hci_project.bean.School;
import com.example.hci_project.bean.School2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class commonAdapter extends RecyclerView.Adapter<commonAdapter.ViewHolder> {

    private ArrayList<School2> myDataList = null;

    private ImageButton button2; // 비교하기 버튼

    private ImageButton button1; // 즐겨찾기 버튼

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, boolean isUser);
    }

    private OnItemClickListener onItemClickListener;


    public commonAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ImageButton getButton2() {
        return button2;
    }

    public ImageButton getButton1() {
        return button1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        ImageView imgView1;


        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.the_Type) ;
            textView2 = itemView.findViewById(R.id.the_name) ;
            textView3 = itemView.findViewById(R.id.the_add) ;
            textView4 = itemView.findViewById(R.id.the_phone) ;
            imgView1 = itemView.findViewById(R.id.the_img_type);

            button1  = (ImageButton)itemView.findViewById(R.id.bookmarkbtn);

            button2  = (ImageButton)itemView.findViewById(R.id.imageButton2);


        }
    }

    commonAdapter(ArrayList<School2> dataList)
    {
        myDataList = dataList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //전개자(Inflater)를 통해 얻은 참조 객체를 통해 뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.fragment_common_recycler, parent, false);
        commonAdapter.ViewHolder viewHolder = new commonAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        holder.textView1.setText(myDataList.get(position).getType());
        holder.textView2.setText(myDataList.get(position).getName());
        holder.textView3.setText(myDataList.get(position).getAddr());
        holder.textView4.setText(myDataList.get(position).getTel());
        holder.imgView1.setImageResource(R.drawable.seekbar_thumb);


    }



    @Override
    public int getItemCount() {
        return myDataList.size();
    }


}
