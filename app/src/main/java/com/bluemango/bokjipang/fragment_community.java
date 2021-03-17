package com.bluemango.bokjipang;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class fragment_community extends Fragment {
    private ArrayList<DataComu> community_list = new ArrayList<>();
    private ComuAdapter comuAdapter;
    Fragment fragment = this;
    private RecyclerView recyclerView;
    private Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        btn = view.findViewById(R.id.btn);


        ArrayList<DataComu> list = new ArrayList<>();
        for(int i = 0 ; i <10; i++){
            DataComu ad = new DataComu();
            ad.setTitle(Integer.toString(i));
            ad.setContent("asdfasdf");
            ad.setGood_num(Integer.toString(i));
            ad.setReply_num(Integer.toString(i));
            list.add(ad);
        }

//        ArrayList<String> list = new ArrayList<>();
//        for (int i=0; i<100; i++) {
//            list.add(String.format("TEXT %d", i)) ;
//        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = view.findViewById(R.id.recycler1) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        ComuAdapter adapter = new ComuAdapter(list) ;
        recyclerView.setAdapter(adapter) ;
//
//        ArrayList<DataComu> list = new ArrayList<>();
//        for(int i=0; i<10;i++){
//            DataComu dc = new DataComu();
//            dc.setTitle(Integer.toString(i));
//            dc.setContent("asdfasdfasdfasfd");
//            dc.setGood_num(i);
//            dc.setReply_num(i);
//            community_list.add(dc);
//        }
//        RecyclerView recyclerView = view.findViewById(R.id.community_form) ;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;
//
//        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
//        ComuAdapter adapter = new ComuAdapter(community_list) ;
//        recyclerView.setAdapter(adapter) ;


//        mmmmmmmmmm
//        recyclerView = view.findViewById(R.id.community_form);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        community_list = new ArrayList<>();
//        FragmentTransaction fgt = getFragmentManager().beginTransaction();
//        comuAdapter = new ComuAdapter(getActivity(), community_list, fragment ,fgt);
//        recyclerView.setAdapter(comuAdapter);

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for(int i= 0 ; i<10 ; i++){
//                    DataComu dc = new DataComu();
//                    dc.setTitle(Integer.toString(i));
//                    dc.setContent("asdfasdfasdfasfd");
//                    dc.setGood_num(i);
//                    dc.setReply_num(i);
//                    community_list.add(dc);
//                    comuAdapter.notifyDataSetChanged();
//                }
//            }
//        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.middle_top_navigation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mypage_btn :
                Intent intent = new Intent(getActivity(), activity_mypage.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
