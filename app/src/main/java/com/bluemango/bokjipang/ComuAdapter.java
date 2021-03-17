package com.bluemango.bokjipang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//public class ComuAdapter extends RecyclerView.Adapter<ComuAdapter.DatacomuHolder> {
//    private ArrayList<DataComu> mList;
//    private Context context= null;
//    private String title=null;
//    private String content = null;
//    private int good_num;
//    private int reply_num;
//    private Fragment fg;
//    private static FragmentTransaction fragT = null;
//
//    class DatacomuHolder extends RecyclerView.ViewHolder{
//
//        protected TextView title;
//        protected TextView content;
//        protected TextView good;
//        protected TextView reple;
//
//
//        public DatacomuHolder(@NonNull View view) {
//            super(view);
//            this.title = view.findViewById(R.id.list_title);
//            this.content = view.findViewById(R.id.list_content);
//            this.good = view.findViewById(R.id.list_goodnum);
//            this.reple = view.findViewById(R.id.list_replenum);
//        }
//    }
//
//
//    private String mJsonString;
//    public ComuAdapter(Context context, ArrayList<DataComu> list, final Fragment fragment, final FragmentTransaction fgT){
//        this.context=context;
//        this.mList = list;
//        fg = fragment;
//        fragT = fgT;
//    }
//
//    ComuAdapter(ArrayList<DataComu> list){
//        mList = list;
//    }
//
//    @NonNull
//    @Override
//    public DatacomuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.community_form, parent, false);
//        DatacomuHolder dataHolder = new DatacomuHolder(view);
//        return dataHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DatacomuHolder holder, int position) {
//        holder.title.setText(mList.get(position).getTitle());
//        holder.content.setText(mList.get(position).getContent());
//        holder.good.setText(mList.get(position).getGood_num());
//        holder.reple.setText(mList.get(position).getReply_num());
//    }
//
//    @Override
//    public int getItemCount() {
//        return (null != mList ? mList.size() : 0);
//    }
//
//
//}

public class ComuAdapter extends RecyclerView.Adapter<ComuAdapter.ViewHolder> {

    private ArrayList<DataComu> mData = null ;

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
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    ComuAdapter(ArrayList<DataComu> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public ComuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.community_form, parent, false) ;
        ComuAdapter.ViewHolder vh = new ComuAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ComuAdapter.ViewHolder holder, int position) {
        DataComu text = mData.get(position) ;
        holder.title.setText(text.getTitle());
        holder.content.setText(text.getContent());
        holder.good.setText(text.getGood_num());
        holder.reply.setText(text.getReply_num());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}