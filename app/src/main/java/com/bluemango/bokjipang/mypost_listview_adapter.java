package com.bluemango.bokjipang;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//public class Post_RecyclerAdapter extends RecyclerView.Adapter<Post_RecyclerAdapter.ItemViewHolder>{
//
//    @NonNull
//    @Override
//    public Post_RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull Post_RecyclerAdapter.ItemViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
//class ItemViewHolder extends RecyclerView.ViewHolder{
//    private TextView title;
//    private TextView content;
//    private TextView like;
//
//    ItemViewHolder(View itemView){
//        super(itemView);
//        title = itemView.findViewById(R.id.post_title);
//        content = itemView.findViewById(R.id.post_content);
//        like = itemView.findViewById(R.id.post_like);
//    }
//    void onBind(mypost_listview_item data){
//        title.setText(data.get_title());
//        content.setText(data.get_content());
//        like.setText(data.get_like());
//    }
//}
public class mypost_listview_adapter extends BaseAdapter {
    ArrayList<mypost_listview_item> Post_list;
    private static final String TAG = "ListView";

    public mypost_listview_adapter(ArrayList<mypost_listview_item> datalist){
        if(datalist == null){
            Post_list = new ArrayList<mypost_listview_item>();
        }
        else{
            Post_list = datalist;
        }
    }
    @Override
    public int getCount() { return Post_list.size(); }
    @Override
    public Object getItem(int position) {
        return Post_list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sub_mypost, parent, false);
        }

        TextView title = convertView.findViewById(R.id.post_title);
        TextView content = convertView.findViewById(R.id.post_content);
        TextView like = convertView.findViewById(R.id.post_like);
        TextView comment = convertView.findViewById(R.id.post_comment);

        mypost_listview_item data = Post_list.get(position);
        title.setText(data.get_title());
        content.setText(data.get_content());
        like.setText(data.get_like());
        comment.setText(data.get_comment());
        return convertView;
    }
    public void addItem(String title, String content, String like, String comment){
        mypost_listview_item data = new mypost_listview_item();

        data.set_title(title);
        data.set_content(content);
        data.set_like(like);
        data.set_comment(comment);
        Post_list.add(data);
    }
}
