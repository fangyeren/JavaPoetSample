package com.fangye.javapoetdemo;

import android.os.Bundle;

import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.javapoetdemo.R;

import androidx.appcompat.app.AppCompatActivity;

@TestRouter(path = "/app/main_other")
@ERouter(path = "/app/main_other")
public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
    }
}
