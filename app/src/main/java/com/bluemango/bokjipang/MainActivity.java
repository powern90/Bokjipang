package com.bluemango.bokjipang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm = getSupportFragmentManager();
    private fragment_community fragment_comu = new fragment_community();
    private fragment_home fragment_home = new fragment_home();
    private fragment_notification fragment_noti = new fragment_notification();
    private fragment_setting fragment_set = new fragment_setting();
    private fragment_support fragment_sup = new fragment_support();
    Fragment active;
    BottomNavigationView bottomNavigationView;
    Fragment firstFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstFragment = new fragment_home();
        fm.beginTransaction().replace(R.id.fragment_container,fragment_home).commit();
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation); //탭바 장착
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener); //탭바 리스너

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int count;
                    count = getSupportFragmentManager().getBackStackEntryCount();
                    Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    switch (menuItem.getItemId()){
                        case R.id.home: //홈화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++) //기존 스택에 남아있던 화면들 다 삭제
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_home).commit(); //홈화면으로 화면변경
                            active = firstFragment;
                            break;

                        case R.id.community: //펫프렌즈 매칭 화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_comu).commit();
                            active = fragment_comu;
                            break;

                        case R.id.support: //펫 파트너 매칭 화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_sup).commit();
                            active = fragment_sup;
                            break;

                        case R.id.notification: //채팅화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_noti).commit();
                            active = fragment_noti;
                            break;

                        case R.id.setting: //마이페이지화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_set).commit();
                            active = fragment_set;
                            break;
                    }
                    return true;
                }
            };
}
