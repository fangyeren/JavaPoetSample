package com.fangye.javapoetdemo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.business.GlobalPathManager;
import com.fangye.business.base.BaseActivity;
import com.fangye.user.UserMainActivity;

import androidx.databinding.DataBindingUtil;

@TestRouter(path = "/app/main_home")
@ERouter(path = "/app/main_home")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        mainBinding.setLifecycleOwner(this);
//        mainBinding.setClick(new ClickProxy());

        findViewById(R.id.btn_normal).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onNormal();
            }
        });
        findViewById(R.id.btn_classload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClassLoader();
            }
        });
        findViewById(R.id.btn_map).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onGlabolMap();
            }
        });
        findViewById(R.id.btn_router).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onRouter();
            }
        });


    }

//    public class ClickProxy{

        /**
         * 普通跳转方式
         */
        public void onNormal(){
            Intent intent = new Intent(MainActivity.this, UserMainActivity.class);
            intent.putExtra("name","normal");
            startActivity(intent);
        }

        /**
         * 类型加载方式
         */
        public void onClassLoader(){
            try {
                Class<?> aClass = Class.forName("com.fangye.tutor.TutorMainActivity");
                Intent intent = new Intent(this,aClass);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * 全局map
         */
        public void onGlabolMap(){
            Class<?> study = GlobalPathManager.INSTANCE.getTargetClass("study", "/study/study_main");
            Intent intent = new Intent(this,study);
            startActivity(intent);
        }

        /**
         * 路由方式跳转
         */
        public void onRouter(){

        }
//    }
}
