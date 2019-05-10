package com.example.apt;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.framework.TestImpl;
import com.example.framework.TestImpl2;
import com.example.lib.TestActivity;
import com.example.lib.TestProxy;
import com.lotus.manager.Lotus;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TestImpl testImpl = Lotus.getInstance().invoke(TestImpl.class);
        testImpl.setData("test1");
        Log.e("Lotus", "data:" + testImpl.getData());

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                testImpl.startTestActivity(MainActivity.this);
            }
        });
    }
}
