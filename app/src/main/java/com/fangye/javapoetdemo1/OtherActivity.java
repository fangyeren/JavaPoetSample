package com.fangye.javapoetdemo1;

import android.os.Bundle;

import com.fangye.annotation.TestRouter;
import com.fangye.javapoetdemo1.databinding.ActivityOtherBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

@TestRouter(path = "app/main_other")
public class OtherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOtherBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_other);
    }
}
