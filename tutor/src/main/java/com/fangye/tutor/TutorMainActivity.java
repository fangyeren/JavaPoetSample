package com.fangye.tutor;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.fangye.annotation.TestRouter;
import com.fangye.tutor.databinding.ActivityTutorBinding;

/**
 * Description: TODO 。
 *
 * @ProjectName: JavaPoetDemo1
 * @Package: com.fangye.user
 * @Author: shijiaxiong@moyi365.com
 * @UpdateDate: 2020/5/25 15:25
 */
@TestRouter(path="tutor/tutor_main")
public class TutorMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTutorBinding viewDataBinding  = DataBindingUtil.setContentView(this, R.layout.activity_tutor);
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.setClick(new ClickProxy());
    }

    public class ClickProxy{
        public void onClick(){

        }
    }
}
