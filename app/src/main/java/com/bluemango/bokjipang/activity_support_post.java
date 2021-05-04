package com.bluemango.bokjipang;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class activity_support_post extends AppCompatActivity {
    TextView createtime;
    TextView title;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_post);

        createtime = (TextView) findViewById(R.id.post_time);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);

        Intent intent = getIntent();
        DataSup dataSup = (DataSup) intent.getSerializableExtra("data");
        String a = dataSup.getContent();
        content.setText(fromHtml(dataSup.getContent()));
        title.setText(dataSup.getTitle());
        createtime.setText(dataSup.getDate());
    }

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);

    }
}
