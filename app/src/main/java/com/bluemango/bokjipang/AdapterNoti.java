package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class AdapterNoti extends RecyclerView.Adapter<AdapterNoti.ViewHolder>{
    private ArrayList<DataNoti> mData = null ;
    private Context context = null;
    private SharedPreferences Shared_noti_list;

    public ArrayList<DataNoti> return_mdata(){return this.mData;}

    public AdapterNoti(Context context, ArrayList<DataNoti> list, SharedPreferences noti_list) {
        this.context = context;
        mData = list ;
        Shared_noti_list = noti_list;
    }


    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView upload_time;
        TextView type;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.title) ;
            upload_time = itemView.findViewById(R.id.upload_time);
            type = itemView.findViewById(R.id.type);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        mOnPopupClick(v, pos);
                        mData.get(pos).setRead(false);

                        String noti_tmp = Shared_noti_list.getString("noti_list", null);

                        ArrayList<String> list = null;
                        if(noti_tmp != null){
                            Gson gson2 = new Gson();
                            Type type = new TypeToken<ArrayList<String>>() {}.getType();
                            list = gson2.fromJson(noti_tmp, type);

                        }
                        else{
                            Log.d("없을 일이다.","");
                        }

                        ArrayList<String> tmp = new ArrayList<>();
                        tmp.add(mData.get(pos).getIdx());
                        list.removeAll(tmp);

                        Gson gson;
                        gson = new GsonBuilder().create();
                        if(list.size() == 0 ){
                            Shared_noti_list.edit().putString("noti_list", null).apply();
                        }
                        else{
                            String noti_info = gson.toJson(list);
                            Shared_noti_list.edit().putString("noti_list", noti_info).apply();
                        }

                    }
                }
            });
        }
    }

    public void mOnPopupClick(View v, int pos) {
        ArrayList<DataNoti> list = this.mData;
        Intent intent = new Intent(context, activity_support_post.class);

        DataNoti dataNoti = list.get(pos);
        intent.putExtra("data", dataNoti.getIdx());
        context.startActivity(intent);
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    AdapterNoti(ArrayList<DataNoti> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public AdapterNoti.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

//        final GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.read);
//        final
        View view = inflater.inflate(R.layout.form_notification, parent, false) ;

        /** 백그라운드 값 세팅해주는 부분*/
        final LinearLayout backGround = view.findViewById(R.id.notification_background);
        backGround.setBackgroundColor(Color.parseColor("#FFDCFF"));
        /** 여기까지 백그라운드 조건에 따라서 넣어주면*/

        AdapterNoti.ViewHolder vh = new AdapterNoti.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(AdapterNoti.ViewHolder holder, int position) {
        DataNoti text = mData.get(position) ;
        holder.title.setText(text.getTitle());
        holder.type.setText(text.getType());

        if(!mData.get(position).getread()){
            final LinearLayout backGround = holder.itemView.findViewById(R.id.notification_background);
            backGround.setBackgroundColor(Color.parseColor("#ffffff")); //클릭시 배경 바꿈 이거 디비값 같은거 0으로 바꿔서 다시 로딩되도록 해야될듯
        }


        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat receive_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String date = format.format(new Date());
        receive_format.setTimeZone(TimeZone.getTimeZone("KST"));
        Date dt = null;
        Date dt2 = null;
        try {
            dt = format.parse(date);
            dt2 = receive_format.parse(text.getDatetime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String receive = format.format(dt2);

        long cal = dt.getTime() - dt2.getTime();
        long upload_sub = cal/(60*1000);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat new_format = new SimpleDateFormat("MM/dd HH:mm");

        holder.upload_time.setText(Integer.toString((int)upload_sub)+"분전");


    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}
