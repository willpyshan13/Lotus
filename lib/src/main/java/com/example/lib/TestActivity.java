package com.example.lib;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.framework.TestImpl2;
import com.lotus.manager.Lotus;

/**
 * Created by ljq on 2019/5/10
 */
public class TestActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final TestImpl2 testImpl2 = Lotus.getInstance().invoke(TestImpl2.class);
        ((TextView) findViewById(R.id.tv)).setText("来自lib2的数据:" + testImpl2.getLib2Data());
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testImpl2.startToLib2Activity(TestActivity.this);
            }
        });
    }
}
