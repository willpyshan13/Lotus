package com.example.lib2;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Keep;

import com.lotus.annotation.LotusProxy;
import com.example.framework.TestImpl2;

/**
 * Created by ljq on 2019/5/8
 */
@LotusProxy(TestImpl2.TAG)
@Keep
public class TestProxy2 {

    public void startToLib2Activity(Activity activity) {
        activity.startActivity(new Intent(activity, Lib2Activity.class));
    }

    public String getLib2Data() {
        return "invoke success";
    }
}
