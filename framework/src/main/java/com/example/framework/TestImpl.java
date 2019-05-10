package com.example.framework;


import android.app.Activity;
import android.support.annotation.Keep;

import com.lotus.annotation.LotusImpl;

/**
 * Created by ljq on 2019/5/8
 */
@LotusImpl(TestImpl.TAG)
@Keep
public interface TestImpl {

    String TAG = "TestImpl";

    String getData();

    void setData(String data);

    void startTestActivity(Activity activity);
}
