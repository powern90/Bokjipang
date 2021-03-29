package com.bluemango.bokjipang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Webview_address extends Fragment {
    private WebView webView;
    private Handler handler;
    JSONObject js = new JSONObject();

    View view;
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        view = layoutInflater.inflate(R.layout.webview_address, container, false);

        /**회원가입에서 작성해놓은 데이터들 json으로 저장*/
        Bundle bundle2= getArguments();
        if(bundle2!=null){
            try {
                js.put("input_phone",bundle2.getString("input_phone"));
                js.put("first_password",bundle2.getString("first_password"));
                js.put("second_password",bundle2.getString("second_password"));
                js.put("name",bundle2.getString("name"));
                js.put("age",bundle2.getString("age"));
                js.put("gender",bundle2.getString("gender"));
                js.put("auth_checked",bundle2.getString("auth_checked"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        init_webView();
        handler = new Handler();
        return view;
    }
    public void init_webView() {
        webView = (WebView) view.findViewById(R.id.webview_address);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://223.194.43.116:80/address.html");
    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    fragment_signup fragment_signup = new fragment_signup();
                    Bundle bundle = new Bundle();
                    /** fragment_singup에서 받아온 string들 다시 넣어주기*/
                    bundle.putString("arg1",arg1);
                    bundle.putString("arg2",arg2);
                    bundle.putString("arg3",arg3);
                    Iterator<String> iterator = js.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        try {
                            Object value = js.get(key);
                            bundle.putString(key, (String) value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    fragment_signup.setArguments(bundle);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment_signup);
                    transaction.commit();
                }
            });
        }
    }
}
