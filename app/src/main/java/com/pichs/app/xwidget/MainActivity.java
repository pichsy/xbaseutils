package com.pichs.app.xwidget;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pichs.common.utils.utils.ImageLoader;
import com.pichs.common.utils.utils.OsUtils;
import com.pichs.common.widget.cardview.XCardButton;
import com.pichs.common.widget.utils.XTypefaceHelper;
import com.pichs.common.widget.view.XButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv1);
        ImageView iv = findViewById(R.id.iv_image);
        XCardButton btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XTypefaceHelper.setGlobalTypefaceFromAssets(getApplicationContext(), "leihong.ttf");
                XTypefaceHelper.setGlobalTypefaceStyle(getApplicationContext(), XTypefaceHelper.BOLD);
            }
        });

        ImageLoader.with()
                .load("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2485381680,689400916&fm=26&gp=0.jpg")
                .into(iv);

    }
}