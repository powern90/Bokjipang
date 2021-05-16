package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class fragment_notification extends Fragment {

    MainActivity activity;
    String noti_list;
    String user_token;
    JSONObject responseJson = null;
    private AdapterNoti adapterNoti;
    RecyclerView recyclerView;
    ArrayList<DataNoti> noti_item_list;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        activity = (MainActivity)  getActivity();
        noti_list = activity.Shared_noti_list.getString("noti_list",null);
        user_token = activity.Shared_user_info.getString("token",null);
        noti_item_list = new ArrayList<DataNoti>();


        recyclerView = view.findViewById(R.id.recycler3) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;

        ArrayList<String> list;
        if(noti_list != null){
            Gson gson2 = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            list = gson2.fromJson(noti_list, type);

        }
        else{
            list  = new ArrayList<String>();
        }

        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                recyclerView = view.findViewById(R.id.recycler3) ;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;
                adapterNoti = new AdapterNoti(getActivity(), noti_item_list);
                recyclerView.setAdapter(adapterNoti);

                if(adapterNoti != null){
                    adapterNoti.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterNoti);
                    return;
                }
                else {
                    adapterNoti = new AdapterNoti(getActivity(), noti_item_list);
                }
                recyclerView.setAdapter(adapterNoti);
            }
        };

        for(int i = 0 ; i<list.size() ; i++){
            int judge=0;
            if(i == (list.size()-1)){
                judge=1;
            }
            run_api(handler, list.get(i), judge);
        }

//        for(int i = 0 ; i <10; i++){
//            DataNoti ad = new DataNoti();
//            ad.setTitle(Integer.toString(i));
//            ad.setContent("qaasdfasdfasdfasdfasdfasdfasdf123124123123123");
//            ad.setType("장애인");
//            ad.setRead(true);
//            list.add(ad);
//        }
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // destroy all menu and re-call onCreateOptionsMenu\

        recyclerView.setAdapter(adapterNoti);
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


    public ArrayList<DataNoti> make_support_item(JSONObject jsonObject, ArrayList<DataNoti> tmp) throws IOException, JSONException {
        DataNoti dataNoti = new DataNoti();

        dataNoti.setTitle(jsonObject.getString("title"));
        dataNoti.setContent(jsonObject.getString("content").replace("\n","<br>").replace("\t",""));
        dataNoti.setIdx(Integer.toString(jsonObject.getInt("id")));
        dataNoti.setRead(true);
        tmp.add(dataNoti);
        return tmp;
    }

    public void run_api(Handler handler, String idx, int judge){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String param = "?index="+ idx;
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support/post"+param);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        try {
                            JSONObject object = new JSONObject();
                            object = responseJson.getJSONObject("post");
                            noti_item_list = make_support_item(object, noti_item_list);
                            if(judge==1){
                                Message msg = handler.obtainMessage();
                                handler.sendMessage(msg);
                            }

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.d("api 연결","error : " + Integer.toString(myconnection.getResponseCode()));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.d("api 연결","tru catch 에러뜸");
                }
            }
        });
    }

}