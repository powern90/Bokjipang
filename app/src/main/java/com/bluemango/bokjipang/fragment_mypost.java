package com.bluemango.bokjipang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class fragment_mypost extends Fragment {
    private static final String TAG = "ListView";
    boolean hold = true;
    mypost_listview_adapter adapter;
    ArrayList<mypost_listview_item> datalist = new ArrayList<mypost_listview_item>();
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_mypost, container, false);
        adapter = new mypost_listview_adapter(datalist);
        ListView listview = view.findViewById(R.id.post_listView);
        listview.setAdapter(adapter);
        if(hold) {
            adapter.addItem("제목1", "내용1", "30", "30");
            adapter.addItem("제목2", "내용2", "40", "40");
            adapter.addItem("제목3", "내용3", "50", "50");
            adapter.addItem("제목4", "내용4", "60", "60");
            hold=false;
        }
//        adapter.notifyDataSetChanged();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {    // 메뉴에 '내 게시물'페이지 상단바 만든뒤에 넣어둠 (검색 버튼)
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mypost_navigation, menu);
    }
//    @Override
//    public boolean onQueryTextSubmit(String query) { // 검색버튼 클릭시에 쿼리실행
//
//        return true;
//
//    }
}



