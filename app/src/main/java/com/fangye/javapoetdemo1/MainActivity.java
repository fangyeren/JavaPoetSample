package com.fangye.javapoetdemo1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.fangye.annotation.TestRouter;

@TestRouter(path = "app/main_home")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
