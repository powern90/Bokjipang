package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
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

public class fragment_support extends Fragment {
    JSONObject responseJson = null;
    private AdapterSup adapterSup;
    String user_token;
    ArrayList<DataSup> list;
    RecyclerView recyclerView;
    Fragment fragment;

    int board;      //board는 커뮤니티 종류 잡아주는거 나중에 api에서 이 board 사용해서 요청.
    int renew=-1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        MainActivity activity = (MainActivity)  getActivity();
        user_token = activity.Shared_user_info.getString("token",null);
        list = new ArrayList<>();



        fragment = this;

        /** renew는 처음오는건지 아닌건지 확인 후에 스피너에서 변경시 board 바꾸는 것*/
        if(renew!=-1)
            board = renew;
        else
            board = 0;
        Log.d("board", String.valueOf(board));


        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                recyclerView = view.findViewById(R.id.recycler2) ;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())) ;
                adapterSup = new AdapterSup(getActivity(), list);
                recyclerView.setAdapter(adapterSup);
            }
        };



        ExecutorService executor = Executors.newSingleThreadExecutor();
        String category = "?support="+ Integer.toString(board);
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support"+category);
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
                            list = make_support_item(array);
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
        inflater.inflate(R.menu.middle_top_navigation, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.support_spinner,
                R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(board != position) {
                    renew = position;
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(fragment).attach(fragment).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mypage_btn :
                Intent intent = new Intent(getActivity(), activity_mypage.class);
                startActivityForResult(intent,1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<DataSup> make_support_item(JSONArray json_array) throws IOException, JSONException {
        ArrayList<DataSup> tmp = new ArrayList<DataSup>();
        for(int i = 0 ; i< json_array.length(); i++){
            JSONObject tt = json_array.getJSONObject(i);
            DataSup dataSup = new DataSup();

            dataSup.setTitle(tt.getString("title"));
            dataSup.setContent(tt.getString("content").replace("\n","<br>").replace("\t",""));
            dataSup.setDate(tt.getString("createdAt"));
            tmp.add(dataSup);
        }
        return tmp;
    }

}