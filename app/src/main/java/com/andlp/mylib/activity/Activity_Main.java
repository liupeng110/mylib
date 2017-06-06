package com.andlp.mylib.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.andlib.lp.activity.Activity_Base;
import com.andlp.mylib.R;

public class Activity_Main extends Activity_Base {

    TextView tv;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView(){
        tv=$(R.id.tv);
        initData();
    }

    private void initData(){
        tv.setText("加载正常$$$");
    }




}
