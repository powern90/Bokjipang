package com.bluemango.bokjipang;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterNoti extends RecyclerView.Adapter<AdapterNoti.ViewHolder>{
    private ArrayList<DataNoti> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView content;
        TextView type;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title) ;
            content = itemView.findViewById(R.id.content);
            type = itemView.findViewById(R.id.type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        final LinearLayout backGround = v.findViewById(R.id.notification_background);
                        backGround.setBackgroundColor(0xB0C9FA); //클릭시 배경 바꿈 이거 디비값 같은거 0으로 바꿔서 다시 로딩되도록 해야될듯
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    AdapterNoti(ArrayList<DataNoti> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AdapterNoti.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

//        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.read);
//        final
        View view = inflater.inflate(R.layout.form_notification, parent, false) ;
        /** 백그라운드 값 세팅해주는 부분*/
        final LinearLayout backGround = view.findViewById(R.id.notification_background);
        backGround.setBackgroundColor(0xff011111);
        /** 여기까지 백그라운드 조건에 따라서 넣어주면*/










        AdapterNoti.ViewHolder vh = new AdapterNoti.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(AdapterNoti.ViewHolder holder, int position) {
        DataNoti text = mData.get(position) ;
        holder.title.setText(text.getTitle());
        holder.content.setText(text.getContent());
        holder.type.setText(text.getType());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
