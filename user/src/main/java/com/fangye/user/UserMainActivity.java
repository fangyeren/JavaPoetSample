package com.fangye.user;

import android.os.Bundle;

import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 个人中心首页
 */
@TestRouter(path = "/user/user_main")
@ERouter(path = "/user/user_main")
public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //        ActivityUserBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        //        viewDataBinding.setLifecycleOwner(this);
        //        viewDataBinding.setClick(new ClickProxy());
    }

    public class ClickProxy {

        public void onClick() {

        }
    }
}
