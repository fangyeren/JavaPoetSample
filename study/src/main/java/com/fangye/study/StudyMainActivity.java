package com.fangye.study;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fangye.annotation.EAutowired;
import com.fangye.annotation.ERouter;
import com.fangye.annotation.TestRouter;
import com.fangye.business.base.BaseActivity;
import com.fangye.erouterapi.launcher.AutowiredManager;
import com.fangye.erouterapi.launcher.ERouterManager;


/**
 * 学习中心首页
 */
@TestRouter(path="/study/study_main")
@ERouter(path="/study/study_main")
public class StudyMainActivity extends BaseActivity {

    @EAutowired
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        AutowiredManager.getInstance().inject(this);
        Log.e(TAG,"name==:"+name);


//        ActivityStudyBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_study);
//        viewDataBinding.setLifecycleOwner(this);
//        viewDataBinding.setClick(new ClickProxy());


        findViewById(R.id.btn_open_intelligent_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIntelligentMain();
            }
        });
    }

    private void openIntelligentMain() {
        ERouterManager.getInstance()
                .build("/intelligent/intelligent_main")
                .withString("name","我是从学习中心首页过来的")
                .navigation(this);
    }

    public class ClickProxy{
        public void onClick(){

        }
    }
}
