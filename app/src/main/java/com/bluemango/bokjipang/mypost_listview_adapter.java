package com.bluemango.bokjipang;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class mypost_listview_adapter extends RecyclerView.Adapter<mypost_listview_adapter.ViewHolder>{
    private ArrayList<mypost_listview_item> mData = null ;
    private Context context = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView content;
        TextView comment;
        TextView like;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.post_title) ;
            content = itemView.findViewById(R.id.post_content);
            like = itemView.findViewById(R.id.post_like);
            comment = itemView.findViewById(R.id.post_comment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mOnPopupClick(v, pos);
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public mypost_listview_adapter(Context context, ArrayList<mypost_listview_item> list) {
        this.context = context;
        mData = list ;
    }

    public void mOnPopupClick(View v, int pos) {
        ArrayList<mypost_listview_item> list = this.mData;
        Intent intent = new Intent(context, activity_community_post.class);

        mypost_listview_item dataitem = list.get(pos);
        intent.putExtra("data", dataitem.getIdx());
        context.startActivity(intent);
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public mypost_listview_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sub_mypost, parent, false) ;
        mypost_listview_adapter.ViewHolder vh = new mypost_listview_adapter.ViewHolder(view) ;

        return vh ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(mypost_listview_adapter.ViewHolder holder, int position) {
        mypost_listview_item text = mData.get(position) ;
        holder.title.setText(text.get_title());
        holder.content.setText(text.get_content());
        holder.like.setText(text.get_like());
        holder.comment.setText(text.get_comment());
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if(mData == null) {
            return 0;
        }
        else return mData.size() ;
    }


}