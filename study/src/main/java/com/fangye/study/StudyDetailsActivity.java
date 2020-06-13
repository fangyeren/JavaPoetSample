package com.fangye.study;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fangye.annotation.EAutowired;
import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;


/**
 * 学习中心首页
 */
@TestRouter(path="/study/study_details")
@ERouter(path="/study/study_details")
public class StudyDetailsActivity extends AppCompatActivity {

    @EAutowired
    String name;

    @EAutowired
    String age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        ActivityStudyBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_study);
//        viewDataBinding.setLifecycleOwner(this);
//        viewDataBinding.setClick(new ClickProxy());
    }
    public class ClickProxy{
        public void onClick(){

        }
    }
}
