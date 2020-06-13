package com.fangye.intelligent;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;

/**
 * 智能备考首页
 */
@TestRouter(path="/intelligent/intelligent_main")
@ERouter(path="/intelligent/intelligent_main")
public class IntelligentMainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent);
//        ActivityIntelligentBinding viewDataBinding  = DataBindingUtil.setContentView(this, R.layout.activity_intelligent);
//        viewDataBinding.setLifecycleOwner(this);
//        viewDataBinding.setClick(new ClickProxy());
    }

    public class ClickProxy{
        public void onClick(){

        }
    }
}
