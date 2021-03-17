package com.bluemango.bokjipang;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.zip.Inflater;

public class fragment_login extends Fragment {
    private EditText id_text, pw_text;
    private Button btn_login;
    private fragment_signup fragment_signup = new fragment_signup();

    Button kakaoLogin,kakaoLogout;
    LoginButton loginButton;
    private KaKaoCallBack kaKaoCallBack;

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_login, container, false);

        TextView signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {          //회원가입 페이질 이동
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_signup);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        /**KAKAO 로그인 */
        loginButton = view.findViewById(R.id.loginButton);
        kakaoLogin = view.findViewById(R.id.kakaoLogin);
        kakaoLogout = view.findViewById(R.id.kakaoLogout);

        kaKaoCallBack = new KaKaoCallBack();
        Session.getCurrentSession().addCallback(kaKaoCallBack);
        Session.getCurrentSession().checkAndImplicitOpen();

        kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
                MainActivity activity = (MainActivity) getActivity();
                activity.fm.beginTransaction().replace(R.id.fragment_container,fragment_signup).commit();
            }
        });

        kakaoLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Log.d("logout","되었음");
                    }
                });
            }
        });
        /**KAKAO 로그인 */
        return view;
    }

    /**KAKAO 로그인 함수 */

    public void kakaoError(String msg){
        Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kaKaoCallBack);
    }
    /**KAKAO 로그인 함수 */
}

