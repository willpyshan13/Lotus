package com.example.framework;


import android.app.Activity;
import android.support.annotation.Keep;

import com.lotus.annotation.LotusImpl;

/**
 * Created by ljq on 2019/5/8
 */
@LotusImpl(TestImpl2.TAG)
@Keep
public interface TestImpl2 {

    String TAG = "TestImpl2";

    void startToLib2Activity(Activity activity);

    String getLib2Data();
}
