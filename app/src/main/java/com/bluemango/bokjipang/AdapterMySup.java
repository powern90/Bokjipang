package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdapterMySup extends RecyclerView.Adapter<AdapterMySup.ViewHolder>{
    private ArrayList<mypost_listview_item> mData = null ;
    private Context context = null;
    JSONObject responseJson;
    private String user_token;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        ImageView delete_zzim_sup;


        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.sup_title) ;
            delete_zzim_sup = itemView.findViewById(R.id.my_sup_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mOnPopupClick(v, pos);
                    }
                }
            });

            @SuppressLint("HandlerLeak") final Handler handler = new Handler()
            {
                public void handleMessage(Message msg){
                    // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                    AlertDialog.Builder success_alert = new AlertDialog.Builder(context);
                    try {
                        success_alert.setTitle("delete : "+responseJson.getString("success"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    success_alert.create().show();
                }
            };

            delete_zzim_sup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder success_alert = new AlertDialog.Builder(context);
                    success_alert.setTitle(" delete ?");
                    success_alert.create().show();
                    ArrayList<mypost_listview_item> list = mData;
                    mypost_listview_item dataitem = list.get(getAdapterPosition());
                    sup_sub_zzim(handler, dataitem.getIdx(), user_token);
                }
            });
        }



    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public AdapterMySup(Context context, ArrayList<mypost_listview_item> list, String user_token) {
        this.context = context;
        mData = list ;
        this.user_token = user_token;
    }

    public void mOnPopupClick(View v, int pos) {
        ArrayList<mypost_listview_item> list = this.mData;
        Intent intent = new Intent(context, activity_support_post.class);

        mypost_listview_item dataitem = list.get(pos);
        intent.putExtra("data", dataitem.getIdx());
        context.startActivity(intent);
    }


    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AdapterMySup.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.form_my_zzim_sup, parent, false) ;
        AdapterMySup.ViewHolder vh = new AdapterMySup.ViewHolder(view) ;

        return vh ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(AdapterMySup.ViewHolder holder, int position) {
        mypost_listview_item text = mData.get(position) ;
        holder.title.setText(text.get_title());
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if(mData == null) {
            return 0;
        }
        else return mData.size() ;
    }

    public void update_mysup_list(ArrayList<mypost_listview_item> list){
        mData= list;
        this.notifyDataSetChanged();
    }


    public void sup_sub_zzim(Handler handler, String sup_idx, String user_token){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String idx = "?id="+ sup_idx;
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support/zzim/delete"+idx);
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
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
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