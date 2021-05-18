package com.bluemango.bokjipang;

import com.google.firebase.messaging.FirebaseMessagingService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class firebase_message extends FirebaseMessagingService {

    String user_token;
    SharedPreferences Shared_user_info;
    JSONObject responseJson = null;
    Gson gson;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("fcm token : ", s);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/user/fcm");
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("POST");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                    String str = "{\"token\":"+"\""+s+"\""+"}"; //여기에 post인 경우 body json형식으로 채우기
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
                        if(!responseJson.getBoolean("success")) {
                            android.os.Process.killProcess(android.os.Process.myPid());
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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String message = "";
        String title = "";

        // Notifition 항목이 있을때.
        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            title = remoteMessage.getNotification().getTitle();
        }

        // Data 항목이 있을때.
        Map<String, String> data = remoteMessage.getData();
        String messageData = data.get("message");
        String titleData = data.get("title");
        String nameData = data.get("body");
        String datetime = data.get("createdAt");
        String idx = remoteMessage.getData().get("id");
        Log.d("create : ", datetime);

        //저는 포그라운드 백그라운드 동일하게 컨트롤하기 위해 Data항목에 푸쉬 Title, Body 모두 넣어서 구현하였습니다.
        sendNotification(titleData, messageData, nameData, idx);


//        super.onMessageReceived(remoteMessage);
//
        SharedPreferences Shared_noti_list = getApplicationContext().getSharedPreferences("noti_list",MODE_PRIVATE);

        String noti_tmp = Shared_noti_list.getString("noti_list", null);
        ArrayList<String> list;
        if(noti_tmp != null){
            Gson gson2 = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            list = gson2.fromJson(noti_tmp, type);

        }
        else{
            list  = new ArrayList<String>();
        }
        list.add(remoteMessage.getData().get("id"));

//        ArrayList<String> list = new ArrayList<String>();
        gson = new GsonBuilder().create();
        String noti_info = gson.toJson(list);
        Shared_noti_list.edit().putString("noti_list", noti_info).apply();
        Log.d("noti_list : ", noti_info);
//
//
//        String title = remoteMessage.getData().get("title");//firebase에서 보낸 메세지의 title
//        String message = remoteMessage.getData().get("message");//firebase에서 보낸 메세지의 내용
//        String test = remoteMessage.getData().get("test");
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("title", title);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            String channel = "채널";
//            String channel_nm = "채널명";
//
//            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
//                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
//            channelMessage.setDescription("채널에 대한 설명.");
//            channelMessage.enableLights(true);
//            channelMessage.enableVibration(true);
//            channelMessage.setShowBadge(false);
//            channelMessage.setVibrationPattern(new long[]{1000, 1000});
//            notichannel.createNotificationChannel(channelMessage);
//
//            //푸시알림을 Builder를 이용하여 만듭니다.
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, channel)
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)//푸시알림의 제목
//                            .setContentText(message)//푸시알림의 내용
//                            .setChannelId(channel)
//                            .setAutoCancel(true)//선택시 자동으로 삭제되도록 설정.
//                            .setContentIntent(pendingIntent)//알림을 눌렀을때 실행할 인텐트 설정.
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        } else {
//            NotificationCompat.Builder notificationBuilder =
//                    new NotificationCompat.Builder(this, "")
//                            .setSmallIcon(R.drawable.ic_launcher_background)
//                            .setContentTitle(title)
//                            .setContentText(message)
//                            .setAutoCancel(true)
//                            .setContentIntent(pendingIntent)
//                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
//
//            NotificationManager notificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.notify(9999, notificationBuilder.build());
//
//        }
    }

    private void sendNotification(String title, String message, String name, String idx) {

        Intent intent;
        PendingIntent pendingIntent;

        intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name);  //push 정보중 name 값을 mainActivity로 넘김

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //SDK26부터 푸쉬에 채널항목에 대한 세팅이 필요하다.
        if (Build.VERSION.SDK_INT >= 26) {

            String channelId = "test push";
            String channelName = "test Push Message";
            String channelDescription = "New test Information";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            //각종 채널에 대한 설정
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300});
            notificationManager.createNotificationChannel(channel);
            //channel이 등록된 builder
            notificationBuilder = new NotificationCompat.Builder(this, channelId);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }

        notificationBuilder.setSmallIcon(R.drawable.donut)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setContentText(message);

        notificationManager.notify(Integer.parseInt(idx) /* ID of notification */, notificationBuilder.build());
    }

//    private void sendNotification(String title, String message, String name, String idx) {
//
//        Intent intent;
//        PendingIntent pendingIntent;
//
//        intent = new Intent(this, MainActivity.class);
//        intent.putExtra("name", name);  //push 정보중 name 값을 mainActivity로 넘김
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"")
//                .setSmallIcon(R.drawable.donut)
//                .setContentTitle(title)
//                .setContentText(name)
//                .setContentIntent(pendingIntent)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
////        //SDK26부터 푸쉬에 채널항목에 대한 세팅이 필요하다.
////        if (Build.VERSION.SDK_INT >= 26) {
////
////            String channelId = "test push";
////            String channelName = "test Push Message";
////            String channelDescription = "New test Information";
////            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
////            channel.setDescription(channelDescription);
////            //각종 채널에 대한 설정
////            channel.enableLights(true);
////            channel.setLightColor(Color.RED);
////            channel.enableVibration(true);
////            channel.setVibrationPattern(new long[]{100, 200, 300});
////            notificationManager.createNotificationChannel(channel);
////            //channel이 등록된 builder
////            notificationBuilder = new NotificationCompat.Builder(this, channelId);
////        } else {
////            notificationBuilder = new NotificationCompat.Builder(this);
////        }
//
////        notificationBuilder.setSmallIcon(R.drawable.donut)
////                .setContentTitle(title)
////                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
////                .setAutoCancel(true)
////                .setSound(defaultSoundUri)
////                .setContentIntent(pendingIntent)
////                .setContentText(message);
//
//        notificationManager.notify(Integer.parseInt(idx) /* ID of notification */, notificationBuilder.build());
//
//    }

}
