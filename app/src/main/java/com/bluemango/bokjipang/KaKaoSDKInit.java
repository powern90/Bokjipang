//package com.bluemango.bokjipang;
//
//import android.app.Application;
//import com.kakao.auth.KakaoSDK;
//
//public class KaKaoSDKInit extends Application {
//    private static volatile KaKaoSDKInit instance = null;
//
//    public static KaKaoSDKInit getGlobalApplicationContext() {
//        if(instance == null) {
//            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
//        }
//        return instance;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        instance = this;
//        KakaoSDK.init(new KakaoSDKAdapter());
//
//    }
//
//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        instance = null;
//    }
//}
