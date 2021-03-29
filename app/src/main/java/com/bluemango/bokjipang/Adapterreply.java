package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapterreply extends RecyclerView.Adapter<Adapterreply.ViewHolder> {
    private ArrayList<Datareply> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname ;
        TextView content;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            nickname = itemView.findViewById(R.id.nickname) ;
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);


            final LinearLayout backGround = itemView.findViewById(R.id.form_reply);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemView.getLayoutParams());
            params.leftMargin = 1000;
            backGround.setLayoutParams(params);
//            hi.leftMargin = 1000;
//            backGround.setLayoutParams(hi);

//            @SuppressLint("CutPasteId") TextView reply = itemView.findViewById(R.id.nickname);
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) reply.getLayoutParams();
//            params.leftMargin = 1000;
//            reply.setLayoutParams(params);  이거 하나만 마진 들어가서 form 자체에 마진 줄수 있는거 찾는중
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    Adapterreply(ArrayList<Datareply> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public Adapterreply.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.form_community_reply, parent, false) ;
        Adapterreply.ViewHolder vh = new Adapterreply.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(Adapterreply.ViewHolder holder, int position) {
        Datareply text = mData.get(position) ;
        holder.nickname.setText(text.getNickname());
        holder.content.setText(text.getContent());
        holder.date.setText(text.getDate());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}