package com.fangye.study;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fangye.annotation.EAutowired;
import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;
import com.fangye.erouterapi.launcher.AutowiredManager;


/**
 * 学习中心首页
 */
@TestRouter(path="/study/study_details")
@ERouter(path="/study/study_details")
public class StudyDetailsActivity extends BaseActivity {

    @EAutowired
    String name;

    @EAutowired
    int age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        ActivityStudyBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_study);
//        viewDataBinding.setLifecycleOwner(this);
//        viewDataBinding.setClick(new ClickProxy());
        AutowiredManager.getInstance().inject(this);
        Log.e(TAG,"name ====:"+name);
        Log.e(TAG,"age ====:"+age);
    }
    public class ClickProxy{
        public void onClick(){

        }
    }
}
