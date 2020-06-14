package com.fangye.user;

import android.os.Bundle;
import android.util.Log;

import com.fangye.annotation.EAutowired;
import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;
import com.fangye.erouterapi.launcher.AutowiredManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 个人中心首页
 */
@TestRouter(path = "/user/user_main")
@ERouter(path = "/user/user_main")
public class UserMainActivity extends BaseActivity {

    @EAutowired(name = "name")
    String name;
    @EAutowired
    String age;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //        ActivityUserBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        //        viewDataBinding.setLifecycleOwner(this);
        //        viewDataBinding.setClick(new ClickProxy());

        AutowiredManager.getInstance().inject(this);

        Log.e(TAG,"name ====:"+name);
        Log.e(TAG,"age ====:"+age);
    }

    public class ClickProxy {

        public void onClick() {

        }
    }
}
