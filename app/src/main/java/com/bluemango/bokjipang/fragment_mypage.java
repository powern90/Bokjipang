package com.bluemango.bokjipang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class fragment_mypage extends Fragment {
    fragment_changeui fragment_changeui;
    fragment_changefv fragment_changefv;
    TextView back_btn;
    TextView btn_logout;
    TextView mypage_name, mypage_phone;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        fragment_changeui = new fragment_changeui();
        fragment_changefv = new fragment_changefv();

        mypage_name = view.findViewById(R.id.mypage_name);
        mypage_phone = view.findViewById(R.id.mypage_phone);
        activity_mypage activity_mypage = (activity_mypage) getActivity();
        mypage_name.setText(activity_mypage.Shared_user_info.getString("name", null));
        mypage_phone.setText(activity_mypage.Shared_user_info.getString("phone", null));

        /** 내 정보 뒤로가기 */
        back_btn = view.findViewById(R.id.mypage_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragment_home home = new fragment_home();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, home);
                transaction.commit();
            }
        });
        /** 비밀번호 변경 액티비티 이동 */
        TextView go_changePW = view.findViewById(R.id.change_password);
        go_changePW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragment_changepw fragment_changepw = new fragment_changepw();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_changepw);
                transaction.commit();
            }
        });
        /** 개인정보 변경 페이지 이동*/
        TextView change_userInfo = view.findViewById(R.id.change_userInfo);
        change_userInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_changeui);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) MainActivity.context;
                activity.Shared_auto_login.edit().putBoolean("login",false).apply();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return view;
    }
}
