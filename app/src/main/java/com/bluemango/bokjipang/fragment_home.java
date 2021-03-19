package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class fragment_home extends Fragment {
    ViewFlipper viewFlipper;
    float xAtDown, xAtUp;
    ImageView sarang, bokjiro, jeongbu;
    BottomNavigationView bottomNavigationView;
    ArrayList<String> zzim;
    ListView listview;
    home_listview_adapter adapter;
    boolean gojung=true;
    ArrayList<home_listview_item> itemList = new ArrayList<home_listview_item>();

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /** 사진 클릭 하이퍼링크*/
        imageclick(view);

        /**찜한 지원사업*/
        zzim = new ArrayList<String>();
        zzim.add("찜 지원사업 첫번째");
        zzim.add("찜 지원사업 두번째");
        zzim.add("찜 지원사업 세번째");
        TextView zzim1 = (TextView) view.findViewById(R.id.zzim1);
        TextView zzim2 = (TextView) view.findViewById(R.id.zzim2);
        TextView zzim3 = (TextView) view.findViewById(R.id.zzim3);
        try{
            zzim1.setText(zzim.get(0));
            zzim2.setText(zzim.get(1));
            zzim3.setText(zzim.get(2));
        }catch(IndexOutOfBoundsException e){
            if(zzim.size()==0)
                zzim1.setText("아직 찜한 목록이 없습니다.");
        }

        /** 게시판*/
        adapter = new home_listview_adapter(itemList);
        listview = (ListView)view.findViewById(R.id.home_listview);
        listview.setAdapter(adapter);
        if(gojung) {
            /** 즐겨찾기 인경우 먼저 숫자를 부여 -> 코드 수정 필요*/
            adapter.addItem(0, R.drawable.star_white, "자유게시판", "자유게시판 게시1");
            adapter.addItem(1, R.drawable.star_white, "장애인 게시판", "장애인 게시판 게시1");
            adapter.addItem(2, R.drawable.star_white, "저소득 게시판", "저소득 게시판 게시1");
            adapter.addItem(3, R.drawable.star_white, "한부모 게시판", "한부모 게시판 게시1");
            adapter.addItem(4, R.drawable.star_white, "고령자 게시판", "고령자 게시판 게시1");
            adapter.addItem(5, R.drawable.star_white, "다문화 게시판", "다문화 게시판 게시1");
            gojung=false;
        }

        /** 여기 실시간 인기사업 두개 받아서 settext로 넣어주기*/


        /**자동 이미지 배너*/
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper1);
        viewFlipper.setAutoStart(true);
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != viewFlipper) return false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xAtDown = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    xAtUp = event.getX();
                    if (xAtUp < xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out));
                        viewFlipper.showNext();
                    } else if (xAtUp > xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out));
                        viewFlipper.showPrevious();
                    }
                }
                return true;
            }
        });
        viewFlipper.setFlipInterval(3000);      //3초마다 자동 이미지 전환
        viewFlipper.startFlipping();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // destroy all menu and re-call onCreateOptionsMenu
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_top_navigation, menu);
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
    /** 사진 클릭 하이퍼링크*/
    public void imageclick(View view){
        bokjiro = (ImageView)view.findViewById(R.id.bokjiro);
        bokjiro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://bokjiro.go.kr"));
                startActivity(intent);
            }
        });
        jeongbu = (ImageView)view.findViewById(R.id.jeongbu);
        jeongbu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://gov.kr"));
                startActivity(intent);
            }
        });
        sarang = (ImageView)view.findViewById(R.id.sarang);
        sarang.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://chest.or.kr"));
                startActivity(intent);
            }
        });

    }
}
