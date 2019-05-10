package com.example.lib;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.lotus.annotation.LotusProxy;
import com.example.framework.TestImpl;

/**
 * Created by ljq on 2019/5/8
 */
@LotusProxy(TestImpl.TAG)
@Keep
public class TestProxy {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        Log.e("TAG", "setData:" + data);
    }

    public void startTestActivity(Activity activity) {
        activity.startActivity(new Intent(activity, TestActivity.class));
    }
}
