package com.bluemango.bokjipang;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class fragment_signup extends Fragment {

    private String mVerificationID;
    private EditText input_phone;
    private EditText confirm_code;
    private Activity activity;
    private static final String TAG = "SIGN_UP";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_signup, container, false);
        TextView back_login = view.findViewById(R.id.back);
        EditText first_password = view.findViewById(R.id.password);
        EditText second_password = view.findViewById(R.id.confirm_password);
        ImageView set_image = view.findViewById((R.id.setImage));
        confirm_code = view.findViewById(R.id.confirm_code);
        input_phone = view.findViewById(R.id.input_phone);

        /** 회원가입 뒤로가기 **/
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_login login = new fragment_login();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, login);
                transaction.commit();
            }
        });
        /** 비밀번호 일치 여부 **/
        second_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(first_password.getText().toString().equals(second_password.getText().toString())){
                    set_image.setImageResource(R.drawable.equal);
                }
                else{
                    set_image.setImageResource(R.drawable.not_equal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /** 전화번호 인증 **/

        view.findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            LinearLayout verify_layout = view.findViewById(R.id.verify_layout);

            @Override
            public void onClick(View v) {
                String phone_number = input_phone.getText().toString().trim();
                if (phone_number.isEmpty() || phone_number.length() < 10) {
                    input_phone.setError("잘못된 번호입력입니다.");
                    input_phone.requestFocus();
                    return;
                }
                if (phone_number.startsWith("0")){
                    phone_number = phone_number.substring(1);
                }
                verify_layout.setVisibility(View.VISIBLE);
                startPhoneNumberVerification(phone_number);
            }
        });
        view.findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = confirm_code.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    confirm_code.setError("잘못된 인증번호입니다.");
                    confirm_code.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });
        return view;
    }

    private void startPhoneNumberVerification(String phoneNumber) {


        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+82"+phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code = credential.getSmsCode();
            if (code != null) {
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.d(TAG, "Firebase auth credential Error!!");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.d(TAG, "Too many Firebase request!!");
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

            mVerificationID = verificationId;
//            mResendToken = token;
        }
    };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "전화 인증 성공");
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            Log.w(TAG, "전화 인증 실패", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // 있을 수 있는 인증 예외처리들 모음
                            }
                        }
                    }
                });
    }
}

