package com.fangye.user;

import android.os.Bundle;

import com.fangye.annotation.TestRouter;
import com.fangye.user.databinding.ActivityUserBinding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Description: TODO 。
 *
 * @ProjectName: JavaPoetDemo1
 * @Package: com.fangye.user
 * @Author: shijiaxiong@moyi365.com
 * @UpdateDate: 2020/5/25 15:25
 */
@TestRouter(path="user/user_main")
public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.setClick(new ClickProxy());
    }

    public class ClickProxy{
        public void onClick(){

        }
    }
}
