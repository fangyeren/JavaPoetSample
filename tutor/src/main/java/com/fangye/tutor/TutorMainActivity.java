package com.fangye.tutor;

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
 * 口语家教首页
 */
@TestRouter(path = "/tutor/tutor_main")
@ERouter(path = "/tutor/tutor_main")
public class TutorMainActivity extends BaseActivity {

    @EAutowired
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        //        ActivityTutorBinding viewDataBinding  = DataBindingUtil.setContentView(this, R.layout.activity_tutor);
        //        viewDataBinding.setLifecycleOwner(this);
        //        viewDataBinding.setClick(new ClickProxy());
        AutowiredManager.getInstance().inject(this);
        Log.e(TAG,"name ====:"+name);
    }

    public class ClickProxy {

        public void onClick() {

        }
    }
}
