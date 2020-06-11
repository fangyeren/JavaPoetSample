package com.fangye.javapoetdemo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;
import com.fangye.javapoetdemo1.databinding.ActivityMainBinding;
import com.fangye.user.UserMainActivity;

@TestRouter(path = "app/main_home")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainBinding.setLifecycleOwner(this);
        mainBinding.setClick(new ClickProxy());
    }

    public class ClickProxy{

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

        }

        /**
         * 全局map
         */
        public void onGlabolMap(){

        }

        /**
         * 路由方式跳转
         */
        public void onRouter(){

        }
    }
}
