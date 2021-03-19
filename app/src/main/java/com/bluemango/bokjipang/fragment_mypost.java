package com.bluemango.bokjipang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_mypost extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_mypost, container, false);


        return inflater.inflate(R.layout.fragment_mypost, container, false);
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
