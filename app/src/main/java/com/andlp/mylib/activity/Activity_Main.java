package com.andlp.mylib.activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andlib.lp.activity.Activity_Base;
import com.andlp.mylib.R;
import com.andlp.mylib.receiver.Receiver_Sms;
import com.andlp.mylib.view.MyView;
import com.mob.MobSDK;
import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.CustomAPI;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.UserCenter;
import com.mob.tools.utils.Data;
import com.mob.tools.utils.ResHelper;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

@ContentView(R.layout.activity_main)
public class Activity_Main extends Activity_Base  {
    @ViewInject(R.id.content)RelativeLayout content;//中间布局
    @ViewInject(R.id.myview) MyView myview;
    @ViewInject(R.id.tv)TextView tv;
    @ViewInject(R.id.register)Button register;
    @ViewInject(R.id.login)Button login;
    @ViewInject(R.id.changepass)Button changepass;
    @ViewInject(R.id.put)Button put;
    @ViewInject(R.id.get)Button get;
    @ViewInject(R.id.getAll)Button getAll;

    @ViewInject(R.id.user)EditText user;
    @ViewInject(R.id.pass)EditText pass;
    @ViewInject(R.id.email)EditText email;
    NotificationManager mNotificationManager;

    //--sms
    EventHandler eh;
    private BroadcastReceiver smsReceiver;
    private UserCenter api;//用户系统
    String token,uid;
    //--sms
    int onClickNum=0;
    String str_user,str_pass,str_email;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        initData();
    } //01
    private void initData(){
        tv.setText("加载正常$$$");
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tv.setText("onclick"+(onClickNum++));
                getSms();
            }
        });

        initSMS();
    }            //02

   //--------短信
    private void initSMS(){
      api = ResHelper.forceCast(MobAPI.getAPI(UserCenter.NAME));//初始化用户体系
       eh=new EventHandler(){
        @Override  public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    txt("验证码 回调完成");
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {    //提交验证码成功
                        end("校验成功 手动 end:"+result+",data:"+data.toString());
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){  //获取验证码成功
                        //data =true -----  /false   send
                        if (data.toString().equals("true")){
                            end("校验成功 自动 end:"+result+",data:"+data.toString());
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
                reciveSms(verifyCode);
            }
        });
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }  //初始化监听等
    private void getSms(){
        SMSSDK.getVerificationCode("86", "17665255699",  new OnSendMessageHandler() {
            @Override public boolean onSendMessage(String s, String s1) {
                 txt("验证码 onSend---s:"+s+",s1:"+s1);
                return false;
            }
        });
    }     //send
    private void reciveSms(String verifyCode){
        txt("收到短信验证码："+verifyCode);//自动填充
        subSmsCode("86","17665255699",verifyCode);
    }//收到短信验证码
    private void subSmsCode(String county,String phone,String code){
        SMSSDK.submitVerificationCode(county, phone, code);
    }
    private void end(String str){
        txt(str);
   }
   //--------短信


    @Event(R.id.register) private void register(View view){
       txt(upe());
      register(str_user,str_pass,str_email);
    }

    @Event(R.id.login) private void login(View view){
        txt(upe());
        login(str_user,str_pass );
    }

    @Event(R.id.changepass) private void changepass(View view){
        txt(upe());
        changepass(str_user,str_pass,str_email);
    }

    @Event(R.id.put) private void put(View view){
        String item = null,value = null;
        try {
            item = encodeData(str_user);//进行BASE64编码,URL_SAFE/NO_WRAP/NO_PADDING
            value = encodeData(str_pass);
        } catch (Exception e) { e.printStackTrace(); }
        putData(item,value);
    }

    @Event(R.id.get) private void get(View view){
       getData(str_user);
        String item = null ;
        try {
            item = encodeData(str_user);//进行BASE64编码,URL_SAFE/NO_WRAP/NO_PADDING
        } catch (Exception e) { e.printStackTrace(); }
        getData(item);

    }
    @Event(R.id.getAll) private void getAll(View view){
        getAll();
    }

   private String upe(){
       str_user=user.getText().toString().trim();
       str_pass=pass.getText().toString().trim();
       str_email=email.getText().toString().trim();
    return  str_user+","+str_pass+","+str_email;
   }
   //-----用户体系
    private void register(String userName, String passWoed, String email){

          api.register(userName,   passWoed,  email,new APICallback(){

              @Override public void onSuccess(API api, int i, Map<String, Object> map) {
                           txt(map.toString());
              }
              @Override public void onError(API api, int i, Throwable throwable) {
                  txt(throwable.toString());
              }
          });
    }
    private void login(String userName,String passWord){
       api.login(userName, passWord, new APICallback() {
           @Override public void onSuccess(API api, int i, Map<String, Object> map) {
               txt(map.toString());
               HashMap<String, String> res = ResHelper.forceCast(map.get("result"));
               if (res != null) {
                   token = res.get("token");
                   uid = res.get("uid");
               }
           }
           @Override public void onError(API api, int i, Throwable throwable) {
               txt(throwable.toString());
           }
       });
    }
    private void changepass(String userName,String oldPass,String newPass){
            api.changePassword(userName, oldPass, newPass, new APICallback() {
                @Override public void onSuccess(API api, int i, Map<String, Object> map) {
                    txt(map.toString());
                }
                @Override public void onError(API api, int i, Throwable throwable) {
                    txt(throwable.toString());
                }
            });
    }
    private void resetPass(String userName,String newPass_temp,String newPass){
        api.changePassword(userName, newPass_temp, newPass, 2, new APICallback() {
            @Override public void onSuccess(API api, int i, Map<String, Object> map) {
                txt(map.toString());
            }
            @Override public void onError(API api, int i, Throwable throwable) {
                txt(throwable.toString());
            }
        });

    }
    private void forgotPass(String userName){
        api.forgotPassword(userName, new APICallback() {
            @Override public void onSuccess(API api, int i, Map<String, Object> map) {
                txt(map.toString());
            }
            @Override public void onError(API api, int i, Throwable throwable) {
                txt(throwable.toString());
            }
        });
    }
    private void putData(String key,String value){
       api.putData(token, uid, key, value, new APICallback() {
           @Override public void onSuccess(API api, int i, Map<String, Object> map) {
               txt(map.toString());
           }
           @Override public void onError(API api, int i, Throwable throwable) {
               txt(throwable.toString());
           }
       });
   }//插入/更新用户资料
    private void getData(String key){
     api.queryData(token,uid, key, new APICallback() {
        @Override public void onSuccess(API api, int i, Map<String, Object> map) {
            txt(map.toString());
          }
         @Override public void onError(API api, int i, Throwable throwable) {
             txt(throwable.toString());
          }
      });
    }            //查询用户资料
    private void delData(String key){
        api.delData(token,uid, key, new APICallback() {
            @Override public void onSuccess(API api, int i, Map<String, Object> map) {
                txt(map.toString());
            }
            @Override public void onError(API api, int i, Throwable throwable) {
                txt(throwable.toString());
            }
        });
    }            //查询用户资料
    private void getAll(){
        api.queryData(token, uid, "", new APICallback() {
            @Override public void onSuccess(API api, int i, Map<String, Object> map) {
                txt(map.toString());
            }
            @Override public void onError(API api, int i, Throwable throwable) {
                txt(throwable.toString());
            }
        });
    }




    //用户密码加密(此部分用户可以自己实现)
    private String encodePassword(String password) {
        return Data.MD5(password +"PUBLIC_LICENCE");
    }

    //用户资料项和用户数据进行base64编码(此部分必须是base64编码,如有需要,在编码前可自行加密)
    private String encodeData(String data) throws UnsupportedEncodingException {
        //进行BASE64编码,URL_SAFE/NO_WRAP/NO_PADDING
        return new String(Base64.encode(data.getBytes("utf-8"), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING), "utf-8");
    }

    //解析用户资料项和用户数据
    private String decodeData(String data) throws UnsupportedEncodingException {
        //进行BASE64解码,URL_SAFE/NO_WRAP/NO_PADDING
        return new String(Base64.decode(data.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING), "utf-8");
    }


    //---用户体系

    private void txt(final  String str){
        x.task().autoPost(new Runnable() {
            @Override public void run() {
                tv.setText(str);
            }
        });
    }
    @Override protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);//取消短信监听
    }




}
