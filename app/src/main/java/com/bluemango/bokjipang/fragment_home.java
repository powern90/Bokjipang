package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class fragment_home extends Fragment {
    ViewFlipper viewFlipper;
    float xAtDown, xAtUp;
    ImageView sarang, bokjiro, jeongbu;
    BottomNavigationView bottomNavigationView;
    ListView listview;
    home_listview_adapter adapter;
    boolean gojung=true;
    List<DataZzim> zzimList;
    List<DataBoard> boardList;
    List<DataHigh> highList;
    ArrayList<home_listview_item> itemList = new ArrayList<home_listview_item>();
    JSONObject user_info, user_interest;
    String user_token;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = (MainActivity)  getActivity();

        user_token = activity.Shared_user_info.getString("token",null);
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
            Log.d("시발", String.valueOf(user_interest.getBoolean("고령자")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int a =1;

        zzimList = new ArrayList<DataZzim>();
        boardList = new ArrayList<DataBoard>();
        highList = new ArrayList<DataHigh>();

        /**찜한 지원사업*/
        TextView zzim1 = (TextView) view.findViewById(R.id.zzim1);
        TextView zzim2 = (TextView) view.findViewById(R.id.zzim2);
        TextView zzim3 = (TextView) view.findViewById(R.id.zzim3);


        TextView popular_title1 = (TextView) view.findViewById(R.id.popular_title1);
        TextView popular_content1 = (TextView) view.findViewById(R.id.popular_content1);
        TextView popular_like1 = (TextView) view.findViewById(R.id.popular_like1_count);
        TextView popular_title2 = (TextView) view.findViewById(R.id.popular_title2);
        TextView popular_content2 = (TextView) view.findViewById(R.id.popular_content2);
        TextView popular_like2 = (TextView) view.findViewById(R.id.popular_like2_count);
        TextView popular_title3 = (TextView) view.findViewById(R.id.popular_title3);
        TextView popular_content3 = (TextView) view.findViewById(R.id.popular_content3);
        TextView popular_like3 = (TextView) view.findViewById(R.id.popular_like3_count);

        /** 게시판*/
        adapter = new home_listview_adapter(itemList);
        listview = (ListView)view.findViewById(R.id.home_listview);
        listview.setAdapter(adapter);

        /**백그라운드에서 ui 변경하고 싶어서 handler 호출 하기 위해....*/
        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                listview.setAdapter(adapter);
            }
        };

        /** 사진 클릭 하이퍼링크*/
        imageclick(view);
        /**홈에 들어온 경우 main 에서 찜이랑 게시판 받아오는 부분 start*/
        AsyncTask.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.me/main/ ");
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    Log.d("home", String.valueOf(myconnection.getResponseCode()));
                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        InputStream responseBody = myconnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject();
                        while(jsonReader.hasNext()){
                            String key = null;
                            try {
                                key = jsonReader.nextName();
                            }catch(IllegalStateException e){
                                break;
                            }
                                /**찜한 리스트 띄워주는 세팅*/
                            if(key.equals("zzim")){
                                zzimList = read_zzim(jsonReader);
                                try{
                                    zzim1.setText(zzimList.get(0).getTitle());
                                    zzim2.setText(zzimList.get(1).getTitle());
                                    zzim3.setText(zzimList.get(2).getTitle());
                                }catch(IndexOutOfBoundsException e){
                                    if(zzimList.size()==0)
                                        zzim1.setText("아직 찜한 목록이 없습니다.");
                                }
                            }
                            else if(key.equals("high")){
                                highList = read_high(jsonReader);
                                try{
                                    popular_title1.setText(highList.get(0).getTitle());
                                    popular_content1.setText(highList.get(0).getContent());
//                                    popular_like1.setText(String.valueOf(highList.get(0).getLike()));
                                    popular_title2.setText(highList.get(1).getTitle());
                                    popular_content2.setText(highList.get(1).getContent());
//                                    popular_like2.setText(String.valueOf(highList.get(1).getLike()));
                                    popular_title3.setText(highList.get(2).getTitle());
                                    popular_content3.setText(highList.get(2).getContent());
//                                    popular_like3.setText(String.valueOf(highList.get(2).getLike()));
                                }catch(IndexOutOfBoundsException e){
                                    if(highList.size()==0)
                                        popular_title1.setText("아직 인기 게시물이 없습니다.");
                                }
                            }
                            else if(key.equals("board")){
                                boardList = read_board(jsonReader);
                                if(gojung) {
                                    /** 즐겨찾기 인경우 먼저 숫자를 부여 -> 코드 수정 필요*/
                                    try{
                                        adapter.addItem(0, R.drawable.star_white, "장애인 게시판", boardList.get(0).getTitle());
                                        adapter.addItem(1, R.drawable.star_white, "저소득 게시판", boardList.get(1).getTitle());
                                        adapter.addItem(2, R.drawable.star_white, "다문화 게시판", boardList.get(2).getTitle());
                                        adapter.addItem(3, R.drawable.star_white, "고령자 게시판", boardList.get(3).getTitle());
                                        adapter.addItem(4, R.drawable.star_white, "한부모 게시판", boardList.get(4).getTitle());
                                        adapter.addItem(5, R.drawable.star_white, "자유게시판", boardList.get(5).getTitle());
                                    }catch(IndexOutOfBoundsException e){
                                        Log.d("error","board error");
                                    }
                                    gojung=false;
                                    /**핸들러 호출*/
                                    Message msg = handler.obtainMessage();
                                    handler.sendMessage(msg);
                                }
                            }
                            else{
                                jsonReader.skipValue();
                            }
                        }
                        //jsonReader.endObject();
                        jsonReader.close();
                        myconnection.disconnect();

                    }else{
                        Log.d("api 연결","error 200아님");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("api 연결","tru catch 에러뜸");
                }
            }
        });
        /**홈에 들어온 경우 main 에서 찜이랑 게시판 받아오는 부분 end*/



        /**자동 이미지 배너*/
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper1);
        viewFlipper.setAutoStart(true);
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != viewFlipper) return false;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    xAtDown = event.getX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    xAtUp = event.getX();
                    if (xAtUp < xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out));
                        viewFlipper.showNext();
                    } else if (xAtUp > xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.push_right_out));
                        viewFlipper.showPrevious();
                    }
                }
                return true;
            }
        });
        viewFlipper.setFlipInterval(3000);      //3초마다 자동 이미지 전환
        viewFlipper.startFlipping();


        return view;
    }

    /**api에서 받은 high 처리부분*/
    public List<DataHigh> read_high(JsonReader reader) throws IOException {
        List<DataHigh> temp = new ArrayList<DataHigh>();
        reader.beginArray();
        while(reader.hasNext()){
            DataHigh dh = new DataHigh();
            reader.beginObject();
            while(reader.hasNext()){
                String name = reader.nextName();
                if (name.equals("id")) {
                    dh.setId(reader.nextInt());
                } else if (name.equals("title")) {
                    dh.setTitle(reader.nextString());
                } else if (name.equals("content")) {
                    dh.setContent(reader.nextString());
                } else if (name.equals("category")) {
                    dh.setCategory(reader.nextString());
                } else if (name.equals("like")) {
                    dh.setLike(reader.nextInt());
                } else {
                    reader.skipValue();
                }
            }

            temp.add(dh);
            reader.endObject();
        }
        reader.endArray();
        return temp;
    }


    /**api에서 받은 zzim 처리부분*/
    public List<DataZzim> read_zzim(JsonReader reader) throws IOException{
        List<DataZzim> temp = new ArrayList<DataZzim>();
        reader.beginArray();
        while(reader.hasNext()){
            DataZzim dz = new DataZzim();
            reader.beginObject();
            while(reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    dz.setId(reader.nextInt());
                } else if (name.equals("title")) {
                    dz.setTitle(reader.nextString());
                } else if (name.equals("content")) {
                    dz.setContent(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            temp.add(dz);
            reader.endObject();
        }
        reader.endArray();
        return temp;
    }
    public DataBoard setdb(JsonReader reader, DataBoard db) throws IOException {
        reader.beginObject();
        while(reader.hasNext()){
            String nm = reader.nextName();
            if(nm.equals("id")){
                db.setId(reader.nextInt());
            }
            else if(nm.equals("title")){
                db.setTitle(reader.nextString());
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return db;
    }
    /**api에서 받은 board 처리부분*/
    public List<DataBoard> read_board(JsonReader reader) throws IOException{
        List<DataBoard> temp = new ArrayList<DataBoard>();
        reader.beginObject();
        while(reader.hasNext()){
            DataBoard db = new DataBoard();
            String name = reader.nextName();
            if(name.equals("0")){
                db.setBoard(name);          //board 어떤 건지 지정
                db = setdb(reader,db);
                temp.add(db);
            }else if(name.equals("1")){
                db.setBoard(name);          //board 어떤 건지 지정
                db = setdb(reader,db);
                temp.add(db);
            }else if(name.equals("2")){
                db.setBoard(name);          //board 어떤 건지 지정
                db = setdb(reader,db);
                temp.add(db);
            }else if(name.equals("3")){
                db.setBoard(name);          //board 어떤 건지 지정
                db = setdb(reader,db);
                temp.add(db);
            }else if(name.equals("4")){
                db.setBoard(name);          //board 어떤 건지 지정
                db = setdb(reader,db);
                temp.add(db);
            }else if(name.equals("5")){
                db.setBoard(name);          //board 어떤 건지 지정
                db = setdb(reader,db);
                temp.add(db);
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return temp;
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
    /** 사진 클릭 하이퍼링크*/
    public void imageclick(View view){
        bokjiro = (ImageView)view.findViewById(R.id.bokjiro);
        bokjiro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://bokjiro.go.kr"));
                startActivity(intent);
            }
        });
        jeongbu = (ImageView)view.findViewById(R.id.jeongbu);
        jeongbu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://gov.kr"));
                startActivity(intent);
            }
        });
        sarang = (ImageView)view.findViewById(R.id.sarang);
        sarang.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://chest.or.kr"));
                startActivity(intent);
            }
        });

    }
}
