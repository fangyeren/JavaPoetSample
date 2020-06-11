package com.fangye.tutor;

import android.os.Bundle;

import com.fangye.annotation.TestRouter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 口语家教首页
 */
@TestRouter(path = "/tutor/tutor_main")
public class TutorMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        //        ActivityTutorBinding viewDataBinding  = DataBindingUtil.setContentView(this, R.layout.activity_tutor);
        //        viewDataBinding.setLifecycleOwner(this);
        //        viewDataBinding.setClick(new ClickProxy());
    }

    public class ClickProxy {

        public void onClick() {

        }
    }
}
