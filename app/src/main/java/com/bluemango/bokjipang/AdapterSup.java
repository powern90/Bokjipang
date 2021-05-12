package com.bluemango.bokjipang;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterSup extends RecyclerView.Adapter<AdapterSup.ViewHolder>{
    private ArrayList<DataSup> mData = null ;
    private Context context = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView content;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title) ;
            content = itemView.findViewById(R.id.content);
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

    public void mOnPopupClick(View v, int pos) {
        ArrayList<DataSup> list = this.mData;
        Intent intent = new Intent(context, activity_support_post.class);

        DataSup dataSup = list.get(pos);
        intent.putExtra("data", dataSup.getIdx());
        context.startActivity(intent);
    }

    public AdapterSup(Context context, ArrayList<DataSup> list) {
        this.context = context;
        mData = list ;
    }
    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AdapterSup.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.form_support, parent, false) ;
        AdapterSup.ViewHolder vh = new AdapterSup.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(AdapterSup.ViewHolder holder, int position) {
        DataSup text = mData.get(position);
        String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
        holder.title.setText(text.getTitle().replaceAll(match, ""));
        holder.content.setText(text.getContent().replaceAll(match, "").replaceAll("br", ""));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
