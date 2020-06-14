package com.fangye.intelligent;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fangye.annotation.EAutowired;
import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;
import com.fangye.erouterapi.launcher.AutowiredManager;

/**
 * 智能备考首页
 */
@TestRouter(path="/intelligent/intelligent_main")
@ERouter(path="/intelligent/intelligent_main")
public class IntelligentMainActivity extends BaseActivity {

    @EAutowired
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent);
//        ActivityIntelligentBinding viewDataBinding  = DataBindingUtil.setContentView(this, R.layout.activity_intelligent);
//        viewDataBinding.setLifecycleOwner(this);
//        viewDataBinding.setClick(new ClickProxy());
        AutowiredManager.getInstance().inject(this);
        Log.e(TAG,"name ====:"+name);
    }

    public class ClickProxy{
        public void onClick(){

        }
    }
}
