package com.andlib.lp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.xutils.x;

/** 717219917@qq.com  2017/6/6 14:39 */
public class Activity_Base extends FragmentActivity {


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this); //注解view
    }

    public <T> T $(int viewID) { return (T) findViewById(viewID); } //封装findview



}
