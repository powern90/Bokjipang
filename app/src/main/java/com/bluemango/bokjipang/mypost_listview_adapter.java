package com.bluemango.bokjipang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class mypost_listview_adapter extends RecyclerView.Adapter<mypost_listview_adapter.ViewHolder>{
    private ArrayList<mypost_listview_item> mData = null ;
    private Context context = null;
    JSONObject responseJson;
    String user_token;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView content;
        TextView comment;
        TextView like;
        ImageView delete;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.post_title) ;
            content = itemView.findViewById(R.id.post_content);
            like = itemView.findViewById(R.id.post_like);
            comment = itemView.findViewById(R.id.post_comment);
            delete = itemView.findViewById(R.id.post_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mOnPopupClick(v, pos);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder success_alert = new AlertDialog.Builder(context);
                    success_alert.setTitle(" delete ?");
                    success_alert.create().show();
                    ArrayList<mypost_listview_item> list = mData;
                    mypost_listview_item dataitem = list.get(getAdapterPosition());
                    my_post_delete(dataitem.getIdx(), user_token);
                }
            });

        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public mypost_listview_adapter(Context context, ArrayList<mypost_listview_item> list, String user_token) {
        this.context = context;
        mData = list ;
        this.user_token = user_token;
    }

    public void mOnPopupClick(View v, int pos) {
        ArrayList<mypost_listview_item> list = this.mData;
        Intent intent = new Intent(context, activity_community_post.class);

        mypost_listview_item dataitem = list.get(pos);
        intent.putExtra("data", dataitem.getIdx());
        context.startActivity(intent);
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public mypost_listview_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.sub_mypost, parent, false) ;
        mypost_listview_adapter.ViewHolder vh = new mypost_listview_adapter.ViewHolder(view) ;

        return vh ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(mypost_listview_adapter.ViewHolder holder, int position) {
        mypost_listview_item text = mData.get(position) ;
        holder.title.setText(text.get_title());
        holder.content.setText(text.get_content());
        holder.like.setText(text.get_like());
        holder.comment.setText(text.get_comment());
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if(mData == null) {
            return 0;
        }
        else return mData.size() ;
    }



    public void my_post_delete(int idx, String user_token){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    URL url = new URL("https://api.bluemango.site/board/post/delete/");
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("POST");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    String str = "{\"id\":"+idx+"}"; //여기에 post인 경우 body json형식으로 채우기
                    byte[] outputInBytes = str.getBytes(StandardCharsets.UTF_8);    //post 인 경우 body 채우는 곳
                    OutputStream os = myconnection.getOutputStream();
                    os.write( outputInBytes );
                    os.close();
                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        Log.d("커뮤니티 글쓰기 성공?",responseJson.getString("success"));
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