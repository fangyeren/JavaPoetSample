package com.fangye.javapoetdemo1;

import android.os.Bundle;

import com.fangye.annotation.TestRouter;

import androidx.appcompat.app.AppCompatActivity;

@TestRouter(path = "app/main_other")
public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
