package com.andlp.mylib.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andlib.lp.activity.Activity_Base;
import com.andlp.mylib.R;
import com.andlp.mylib.view.MyView;

public class Activity_Main extends Activity_Base {
    RelativeLayout content;
    MyView myview;


    TextView tv;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView(){
        tv=$(R.id.tv);
        myview=$(R.id.myview);
//        content=(ConstraintLayout) findViewById(R.id.content);//MyView  my=$(R.id.myview);
//        myview= new MyView(this);
//        content.addView(myview);//第一种方法


        initData();
    }

    private void initData(){
        tv.setText("加载正常$$$");
    }




}
