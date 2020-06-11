package com.fangye.intelligent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;
import com.fangye.intelligent.databinding.ActivityIntelligentBinding;

/**
 * Description: TODO 。
 *
 * @ProjectName: JavaPoetDemo1
 * @Package: com.fangye.user
 * @Author: shijiaxiong@moyi365.com
 * @UpdateDate: 2020/5/25 15:25
 */
@TestRouter(path="user/user_main")
public class IntelligentMainActivity extends BaseActivity {
    ActivityIntelligentBinding viewDataBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding  = DataBindingUtil.setContentView(this, R.layout.activity_intelligent);
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.setClick(new ClickProxy());
    }

    public class ClickProxy{
        public void onClick(){

        }
    }
}
