package com.bluemango.bokjipang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class home_listview_adapter extends BaseAdapter {
    private final Context context;
    private ArrayList<home_listview_item> listViewItemList;
    SharedPreferences Shared_user_info;
    int size;
    Gson gson;
    public home_listview_adapter(ArrayList<home_listview_item> itemlist, Context context){
        if (itemlist == null) {
            listViewItemList = new ArrayList<home_listview_item>() ;
        } else {
            listViewItemList = itemlist ;
        }
        this.context = context;

        Shared_user_info = context.getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = context.getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = context.getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = context.getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = context.getSharedPreferences("home_interest", MODE_PRIVATE);
        Shared_user_info = context.getSharedPreferences("sup_zzim_list", MODE_PRIVATE);
        gson = new GsonBuilder().create();

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.home_board_listview, viewGroup, false);
        }

        ImageView star = (ImageView) view.findViewById(R.id.star);
        TextView boardname = (TextView) view.findViewById(R.id.board_name) ;
        TextView boardtitle = (TextView) view.findViewById(R.id.board_title) ;


        home_listview_item listViewItem = listViewItemList.get(i);
        star.setImageResource(listViewItem.getBoard_image());
        boardname.setText(listViewItem.getBoard_name());
        boardtitle.setText(listViewItem.getBoard_title());

        Comparator<home_listview_item> noAsc = new Comparator<home_listview_item>(){
            @Override
            public int compare(home_listview_item item1, home_listview_item item2){
                int ret;
                if(item1.getNo() < item2.getNo())
                    ret = -1;
                else if(item1.getNo() == item2.getNo()){
                    ret = 0;
                }
                else
                    ret = 1;
                return ret;
            }
        };
        listViewItemList.sort(noAsc);
        store_listviewitemlist();
        notifyDataSetChanged();

        /**별 누른 경우,,, 여기서 api 통신으로 즐겨찾기도 변경해줘야함*/
        star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(listViewItem.getBoard_image() == R.drawable.star_white) {
                    star.setImageResource(R.drawable.star_black);
                    listViewItem.setBoard_image(R.drawable.star_black);
                    int a = listViewItem.getNo();
                    listViewItem.setNo(0);
                    for(int i=0; i<a; i++){
                        listViewItemList.get(i).setNo(i+1);
                    }
                }
                else {
                    star.setImageResource(R.drawable.star_white);
                    listViewItem.setBoard_image(R.drawable.star_white);
                    int a = listViewItem.getNo();
                    listViewItem.setNo(5);
                    for(int i=a+1; i<=5; i++){
                        listViewItemList.get(i).setNo(i-1);
                    }
                }

              store_listviewitemlist();

                Comparator<home_listview_item> noAsc = new Comparator<home_listview_item>(){
                    @Override
                    public int compare(home_listview_item item1, home_listview_item item2){
                        int ret;
                        if(item1.getNo() < item2.getNo())
                            ret = -1;
                        else if(item1.getNo() == item2.getNo()){
                            ret = 0;
                        }
                        else
                            ret = 1;
                        return ret;
                    }
                };
                listViewItemList.sort(noAsc);
                notifyDataSetChanged();
            }
        });


        boardtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_community_post.class);
                intent.putExtra("data", Integer.toString(listViewItem.getIdx()));
                context.startActivity(intent);
            }
        });
        return view;
    }



    public ArrayList<home_listview_item> getItemList() {
        return listViewItemList ;
    }
    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int no, int image,String name, String title, int idx) {
        home_listview_item item = new home_listview_item();

        item.setNo(no);
        item.setIdx(idx);
        item.setBoard_image(image);
        item.setBoard_name(name);
        item.setBoard_title(title);
        listViewItemList.add(item);
    }

    public void store_listviewitemlist(){
        String user_info = gson.toJson(listViewItemList);
        Shared_user_info.edit().putString("home_interest", user_info).apply();
    }
}
