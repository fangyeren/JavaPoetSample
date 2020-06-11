package com.fangye.javapoetdemo;

import android.app.Application;

import com.fangye.business.GlobalPathManager;
import com.fangye.intelligent.IntelligentMainActivity;
import com.fangye.study.StudyMainActivity;
import com.fangye.tutor.TutorMainActivity;
import com.fangye.user.UserMainActivity;

/**
 * Description:
 *
 * @package: com.fangye.javapoetdemo
 * @className: App
 * @author: shijiaxiong@moyi365.com
 * @date: 2020/6/11 14:55
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalPathManager.INSTANCE.putPathData("user", "/user/user_main", UserMainActivity.class);
        GlobalPathManager.INSTANCE.putPathData("tutor", "/tutor/tutor_main", TutorMainActivity.class);
        GlobalPathManager.INSTANCE.putPathData("study", "/study/study_main", StudyMainActivity.class);
        GlobalPathManager.INSTANCE.putPathData("intelligent", "/intelligent/intelligent_main", IntelligentMainActivity.class);
    }
}
