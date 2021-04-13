package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class fragment_community extends Fragment {
    private AdapterComu adapterComu;
    Fragment fragment = this;
    private RecyclerView recyclerView;
    private Button btn;
    JSONObject responseJson = null;
    String user_token;
    private ArrayList<DataComu> list;
    int count;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        MainActivity activity = (MainActivity)  getActivity();
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        list = new ArrayList<DataComu>();
        recyclerView = view.findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        user_token = activity.Shared_user_info.getString("token",null);
        count = 1;
        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                int pos=0;
                if(adapterComu != null){
                    adapterComu.notifyDataSetChanged();
                }
                else {
                    adapterComu = new AdapterComu(getActivity(), list);
                }
                recyclerView.setVerticalScrollbarPosition(pos-1);
                recyclerView.setAdapter(adapterComu);

            }
        };


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if(!recyclerView.canScrollVertically(-1)){
//                    Log.d("up","up");
//                }
                if(!recyclerView.canScrollVertically(1)){ //맨 밑으로 내려갈때
                    count++;
                    ExecutorService add_list = Executors.newSingleThreadExecutor();
                    String param = "?board=0&page="+Integer.toString(count);
                    add_list.execute(new Runnable(){
                        @Override
                        public void run(){
                            try {
                                /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                                URL url = new URL("https://api.bluemango.me/board"+param);
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
                                        JSONArray array = responseJson.getJSONArray("posts");
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

                    Log.d("down","down");
                }
            }
        });



        ExecutorService executor = Executors.newSingleThreadExecutor();
        String first_request = "?board=0&page="+Integer.toString(count);
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.me/board"+first_request);
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
                            JSONArray array = responseJson.getJSONArray("posts");
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



        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                ExecutorService refresh_view = Executors.newSingleThreadExecutor();
                refresh_view.execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.me/board"+first_request);
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
                                    JSONArray array = responseJson.getJSONArray("posts");
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

                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        FloatingActionButton floating_btn = activity.findViewById(R.id.floating_btn);
//        floating_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.middle_top_navigation, menu);
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



    public ArrayList<DataComu> make_comunity_item(JSONArray json_array, ArrayList<DataComu> tmp) throws IOException, JSONException {
//        ArrayList<DataComu> tmp = new ArrayList<DataComu>();
        for(int i = 0 ; i< json_array.length(); i++){
            JSONObject tt = json_array.getJSONObject(i);
            DataComu dataComu = new DataComu();

            dataComu.setId(Integer.toString(tt.getInt("id")));
            dataComu.setTitle(tt.getString("title"));
            dataComu.setContent(tt.getString("content"));
            dataComu.setGood_num(Integer.toString(tt.getInt("like")));
            dataComu.setDatetime(tt.getString("createdAt"));
            dataComu.setReply_num(Integer.toString(tt.getInt("reply_count")));
            tmp.add(dataComu);
        }

        for(int i = 0 ; i< json_array.length(); i++){
            JSONObject tt = json_array.getJSONObject(i);
            DataComu dataComu = new DataComu();

            dataComu.setId(Integer.toString(tt.getInt("id")));
            dataComu.setTitle(tt.getString("title"));
            dataComu.setContent(tt.getString("content"));
            dataComu.setGood_num(Integer.toString(tt.getInt("like")));
            dataComu.setDatetime(tt.getString("createdAt"));
            dataComu.setReply_num(Integer.toString(tt.getInt("reply_count")));
            tmp.add(dataComu);
        }


        return tmp;
    }
}