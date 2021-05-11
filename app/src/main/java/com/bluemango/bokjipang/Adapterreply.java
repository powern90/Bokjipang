package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Adapterreply extends RecyclerView.Adapter<Adapterreply.ViewHolder> {
    private ArrayList<Datareply> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname;
        TextView content;
        TextView date;
        TextView rereply;
        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            nickname = itemView.findViewById(R.id.nickname) ;
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
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
        Context context = ApplicationClass.getContext();
        SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        old_format.setTimeZone(TimeZone.getTimeZone("KST"));
        SimpleDateFormat new_format = new SimpleDateFormat("MM/dd HH:mm");
        Date old_date = null;
        try {
            old_date = old_format.parse(text.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String new_date =  new_format.format(old_date);
        holder.date.setText(new_date);
        if(text.getRereply().equals("1")) {
                final LinearLayout linear = holder.itemView.findViewById(R.id.reply_all);
                final LinearLayout content = holder.itemView.findViewById(R.id.reply_content);
                final LinearLayout backGround = holder.itemView.findViewById(R.id.form_reply);
                final ImageView comment = holder.itemView.findViewById(R.id.reply_img);
                final View standard = holder.itemView.findViewById(R.id.reply_standard);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                linear.removeView(standard);
                params.setMargins(20,20,0,0);
                params2.setMargins(20,0,0,0);
                ImageView double_reply = new ImageView(context);
                comment.setImageResource(0);
                double_reply.setImageResource(R.drawable.reply_arrow);
                double_reply.setLayoutParams(params);
                content.setLayoutParams(params2);
                backGround.addView(double_reply, params);
        }
    }
    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}