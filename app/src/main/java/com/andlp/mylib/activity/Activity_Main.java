package com.andlp.mylib.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andlib.lp.activity.Activity_Base;
import com.andlp.mylib.R;
import com.andlp.mylib.receiver.Receiver_Sms;
import com.andlp.mylib.view.MyView;
import com.mob.tools.utils.DeviceHelper;

import org.xutils.x;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class Activity_Main extends Activity_Base implements SMSSDK.VerifyCodeReadListener {
    RelativeLayout content;
    MyView myview;
    TextView tv;
    NotificationManager mNotificationManager;

    //--sms
    EventHandler eh;
    private BroadcastReceiver smsReceiver;


     //----


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


      int a=0;
    private void initData(){
        tv.setText("加载正常$$$");

        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tv.setText("onclick"+(a++));
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:120"));

                getSms();
            }
        });

        initSMS();
    }


    private void initSMS(){
       eh=new EventHandler(){
        @Override  public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {//回调完成
                    txt("验证码 回调完成");
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){  //获取验证码成功
                        //data =true -----  /false   send
                        if (data.toString().equals("true")){
                            txt("验证码 自动验证成功"+result+",data:"+data.toString());
                        }else {
                            txt("验证码 自动验证失败,正常下发"+result+",data:"+data.toString());
                        }

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){  //返回支持发送验证码的国家列表

                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调


        smsReceiver = new Receiver_Sms(new SMSSDK.VerifyCodeReadListener() {
            public void onReadVerifyCode(final String verifyCode) {
                txt("收到短信验证码："+verifyCode);
            }
        });
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

//        try {
//            if (DeviceHelper.getInstance(this).checkPermission("android.permission.RECEIVE_SMS")) {
//
//            }
//        } catch (Throwable t) {
//            t.printStackTrace();
//            smsReceiver = null;
//        }

    }


    private void getSms(){
        SMSSDK.getVerificationCode("86", "17665255699",  new OnSendMessageHandler() {
            @Override public boolean onSendMessage(String s, String s1) {
                 txt("验证码 onSend---s:"+s+",s1:"+s1);
                return false;
            }
        });



    }



    @Override public void onReadVerifyCode(String s) {
        txt("验证码"+s);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    private void txt(final  String str){
        x.task().autoPost(new Runnable() {
            @Override public void run() {
                tv.setText(str);
            }
        });
    }


}
