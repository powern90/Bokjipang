package com.bluemango.bokjipang;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class fragment_community extends Fragment {
    private ArrayList<DataComu> community_list = new ArrayList<>();
    private AdapterComu adapterComu;
    Fragment fragment = this;
    private RecyclerView recyclerView;
    private Button btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_community, container, false);


        ArrayList<DataComu> list = new ArrayList<>();
        for(int i = 0 ; i <10; i++){
            DataComu ad = new DataComu();
            ad.setTitle(Integer.toString(i));
            ad.setContent("aaaaaabbbbbbbcccccccccddddddddddffffffffffffggggggggggfeqfasdffasdfasdfasdfsafdasfasfasfas");
            ad.setGood_num(Integer.toString(i+1));
            ad.setReply_num(Integer.toString(i+2));
            list.add(ad);
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = view.findViewById(R.id.recycler1) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        AdapterComu adapter = new AdapterComu(getActivity(), list) ;
        recyclerView.setAdapter(adapter) ;

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
