package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class fragment_changefv extends Fragment {
    JSONObject user_info, user_interest;
    MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changefv, container, false);
        activity = (MainActivity) getActivity();

        String user_token = activity.Shared_user_info.getString("token", null);

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                AlertDialog.Builder auth_alert = new AlertDialog.Builder(getActivity());
                auth_alert.setTitle("관심분야 변경");
                auth_alert.setMessage("성공");
                auth_alert.setPositiveButton("예", null);
                auth_alert.create().show();
                fragment_setting fragment_setting = new fragment_setting();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_setting);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };



        CheckBox ck1 = (CheckBox)view.findViewById(R.id.ck1);
        CheckBox ck2 = (CheckBox)view.findViewById(R.id.ck2);
        CheckBox ck3 = (CheckBox)view.findViewById(R.id.ck3);
        CheckBox ck4 = (CheckBox)view.findViewById(R.id.ck4);
        CheckBox ck5 = (CheckBox)view.findViewById(R.id.ck5);

        String info_tmp = activity.Shared_user_info.getString("user_info",null);
        if(info_tmp != null){
            try{
                user_info = new JSONObject(info_tmp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            user_interest = user_info.getJSONObject("interest");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            ck1.setChecked((Boolean) user_interest.get("장애인"));
            ck2.setChecked((Boolean) user_interest.get("한부모"));
            ck3.setChecked((Boolean) user_interest.get("다문화"));
            ck4.setChecked((Boolean) user_interest.get("고령자"));
            ck5.setChecked((Boolean) user_interest.get("저소득"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btn_changefv = (Button) view.findViewById(R.id.btn_changefv);
        btn_changefv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject user_interest2 = new JSONObject();
                try {
                    user_interest2.put("고령자",ck4.isChecked());
                    user_interest2.put("다문화",ck3.isChecked());
                    user_interest2.put("한부모",ck2.isChecked());
                    user_interest2.put("저소득",ck5.isChecked());
                    user_interest2.put("장애인",ck1.isChecked());
                    user_info.put("interest",user_interest2);
                    activity.Shared_user_info.edit().putString("user_info",user_info.toString()).apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.site/user/interest/");
                            HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                            myconnection.setRequestMethod("POST");  //post, get 나누기
                            myconnection.setRequestProperty("Content-Type", "application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                            myconnection.setRequestProperty("x-access-token", user_token);
                            String str = "{\"interest\":{ \"장애인\": " + ck1.isChecked() + ", \"한부모\": " + ck2.isChecked() + ", \"다문화\": " + ck3.isChecked() + ", \"고령자\": " + ck4.isChecked() + ", \"저소득\": " + ck5.isChecked() + "}}";
                            byte[] outputInBytes = str.getBytes(StandardCharsets.UTF_8);    //post 인 경우 body 채우는 곳
                            OutputStream os = myconnection.getOutputStream();
                            os.write(outputInBytes);
                            os.close();
                            if (myconnection.getResponseCode() == 200) {
                                /** 리스폰스 데이터 받는 부분*/
                                InputStream responseBody = myconnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();
                                    if (key.equals("success")) {
                                        boolean success = jsonReader.nextBoolean();
                                        if (success) {
                                            Message msg = handler.obtainMessage();
                                            handler.sendMessage(msg);
                                            break;
                                        }
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                myconnection.disconnect();

                            } else {
                                Log.d("api 연결", "error 200아님");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("api 연결", "tru catch 에러뜸");
                        }
                    }
                });

            }
        });

        return view;
    }
}
