package com.bluemango.bokjipang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class mypost_listview_adapter extends BaseAdapter {
    private ArrayList<mypost_listview_item> Post_list;
    public mypost_listview_adapter(ArrayList<mypost_listview_item> list){
        if (list == null){
            Post_list = new ArrayList<mypost_listview_item>();
        }
        else{
            Post_list = list;
        }
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

        mypost_listview_item data = Post_list.get(position);
        title.setText(data.get_title());
        content.setText(data.get_content());
        like.setText(data.get_like());
        return convertView;
    }
    @Override
    public int getCount() {
        return Post_list.size();
    }
    @Override
    public Object getItem(int position) {
        return Post_list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(String title, String content, String like){
        mypost_listview_item data = new mypost_listview_item();

        data.set_title(title);
        data.set_content(content);
        data.set_like(like);
        Post_list.add(data);
    }
}
