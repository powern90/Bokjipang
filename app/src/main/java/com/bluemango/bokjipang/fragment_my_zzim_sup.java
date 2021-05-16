package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;


public class fragment_my_zzim_sup extends Fragment {
    private static final String TAG = "ListView";
    AdapterMySup adapter;
    String user_token;
    MainActivity activity;
    private ArrayList<mypost_listview_item> list;
    JSONObject responseJson;
    private RecyclerView recyclerView;
    Fragment fragment = this;
    FragmentTransaction ft;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_my_zzim_list, container, false);
        activity = (MainActivity) getActivity();
        user_token = activity.Shared_user_info.getString("token",null);
        ft = getFragmentManager().beginTransaction();

        /** 설정 뒤로가기 */
        fragment_setting fragment_setting = new fragment_setting();
        TextView go_setting = view.findViewById(R.id.go_setting);
        go_setting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_setting);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        list = new ArrayList<mypost_listview_item>();
        recyclerView = view.findViewById(R.id.post_mysup_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {    // 메뉴에 '내 게시물'페이지 상단바 만든뒤에 넣어둠 (검색 버튼)
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mypost_navigation, menu);
    }

    public ArrayList<mypost_listview_item> make_comunity_item(JSONArray json_array, ArrayList<mypost_listview_item> tmp) throws IOException, JSONException {
//        ArrayList<DataComu> tmp = new ArrayList<DataComu>();
        for(int i = 0 ; i< json_array.length(); i++){
            JSONObject tt = json_array.getJSONObject(i);
            mypost_listview_item dataitem = new mypost_listview_item();

            dataitem.setIdx(Integer.toString(tt.getInt("id")));
            dataitem.set_title(tt.getString("title"));
            tmp.add(dataitem);
        }
        return tmp;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();

        list.clear();
        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                int pos=0;
                if(adapter != null){
                    adapter.update_mysup_list(list);
                    adapter.update_fragmentaction(ft);
                    recyclerView.scrollToPosition(adapter.getItemCount()-22);              //이 부분 나중에 맞춰서 바꿔줘야할듯, 여기 위로 새로고침할때도 들어옴
                    recyclerView.setAdapter(adapter);
                    return;
                }
                else {
                    adapter = new AdapterMySup(getActivity(), list,user_token, fragment, ft);
                }
                recyclerView.setVerticalScrollbarPosition(pos-1);                   //이 부분때문에 무조건 처음으로 가게 된것
                recyclerView.setAdapter(adapter);
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support/zzim");
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
                            JSONArray array = responseJson.getJSONArray("zzim");
                            list = make_comunity_item(array, list);
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
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



