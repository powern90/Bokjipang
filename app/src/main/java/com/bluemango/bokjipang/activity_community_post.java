package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class activity_community_post extends AppCompatActivity {
//    private DataComu dataComu = new DataComu();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_post);

        Intent intent = getIntent();
        String title = intent.getStringExtra("data");
        Log.d("씨발", title);
        ArrayList<Datareply> list = new ArrayList<>();
        for(int i = 0 ; i <10; i++){
            Datareply ad = new Datareply();
            ad.setNickname("nickname"+Integer.toString(i));
            ad.setContent("content. content");
            ad.setDate(Integer.toString(i+1));
            list.add(ad);
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.community_reply) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        Adapterreply adapter = new Adapterreply(list) ;
        recyclerView.setAdapter(adapter) ;

    }
}