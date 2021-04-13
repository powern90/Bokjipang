package com.bluemango.bokjipang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    final FragmentManager fm = getSupportFragmentManager();
    private fragment_community fragment_comu = new fragment_community();
    private fragment_home fragment_home = new fragment_home();
    private fragment_notification fragment_noti = new fragment_notification();
    private fragment_setting fragment_set = new fragment_setting();
    private fragment_support fragment_sup = new fragment_support();
    private fragment_login fragment_login = new fragment_login();
    SharedPreferences Shared_auto_login;
    SharedPreferences Shared_user_info;

    Fragment active;
    BottomNavigationView bottomNavigationView;
    Fragment firstFragment = null;

    final

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Shared_auto_login = getSharedPreferences("login",MODE_PRIVATE); //세션유지에 쓸 sharedPreferences
        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);




//        Intent intent = getIntent();
//        if(intent != null){
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Shared_auto_login.edit().putBoolean("login",false).apply();
//            intent = null;
//        }


        setContentView(R.layout.activity_main);        //세션 없으니 바로 로그인으로 가게 해놓아놨음, 만약 필요하면 여기 바꿔서 각자
        bottomNavigationView = findViewById(R.id.bottom_navigation); //탭바 장착
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener); //탭바 리스너

        if(Shared_auto_login.getBoolean("login",false)){
            bottomNavigationView.setVisibility(View.VISIBLE);
            firstFragment = new fragment_home();
            fm.beginTransaction().replace(R.id.fragment_container,fragment_home).commit();
        }
        else{
            bottomNavigationView.setVisibility(View.GONE);
            fm.beginTransaction().replace(R.id.fragment_container,fragment_login).commit();
        }




//        bottomNavigationView.setVisibility(View.GONE);
//
//        /**KAKAO hash key 얻기*/
//        try {
//            PackageInfo pkinfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : pkinfo.signatures) {
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                String result = new String(Base64.encode(messageDigest.digest(), 0));
//                Log.d("해시", result);
//            }
//        } catch (Exception e) {
//        }
//        /**KAKAO hash key 얻기*/



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
