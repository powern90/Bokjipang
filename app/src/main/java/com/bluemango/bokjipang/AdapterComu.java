package com.bluemango.bokjipang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterComu extends RecyclerView.Adapter<AdapterComu.ViewHolder>{

    private ArrayList<DataComu> mData = null ;
    private Context context = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView content;
        TextView good;
        TextView reply;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title) ;
            content = itemView.findViewById(R.id.content);
            good = itemView.findViewById(R.id.good);
            reply = itemView.findViewById(R.id.reply);

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
    public AdapterComu(Context context, ArrayList<DataComu> list) {
        this.context = context;
        mData = list ;
    }

    public void mOnPopupClick(View v, int pos) {
        ArrayList<DataComu> list = this.mData;
        Intent intent = new Intent(context, activity_community_post.class);

        DataComu dataComu = list.get(pos);
        intent.putExtra("data", dataComu.getId());
        context.startActivity(intent);
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AdapterComu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.form_community, parent, false) ;
        AdapterComu.ViewHolder vh = new AdapterComu.ViewHolder(view) ;

        return vh ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(AdapterComu.ViewHolder holder, int position) {
        DataComu text = mData.get(position) ;
        holder.title.setText(text.getTitle());
        holder.content.setText(text.getContent());
        holder.good.setText(text.getGood_num());
        holder.reply.setText(text.getReply_num());
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