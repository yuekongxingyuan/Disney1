package com.ly.disney;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * A login screen that offers login via email/password.
 */
public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String APPKEY = "16144a735a6c0";
    private final static String APPSECRETE = "821ce0c91817da025c7e98544c01bc9c";
    //手机号输入框
    private AutoCompleteTextView inputPhoneEt;
    //验证码输入框
    private  EditText inputCodeEt;
    //获取验证码按钮
    private Button requestCodeBtn;
    //注册按钮
    private Button commitBtn;
    int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        init();
    }
    android.os.Handler handler = new android.os.Handler(){
        public void handleMessage(Message msg){
            if(msg.what == -9){
                requestCodeBtn.setText("重新发送("+i+")");
            }else if(msg.what == -8){
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
                i=30;
            }else{
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event","event="+event);
                if(result == cn.smssdk.SMSSDK.RESULT_COMPLETE){
                    //短信注册成功后返回MainActivity
                    if(event == cn.smssdk.SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){//提交验证码成功
                        Toast.makeText(getApplicationContext(),"验证码校验成功",Toast.LENGTH_SHORT).show();
                    }else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        Toast.makeText(getApplicationContext(),"正在获取验证码",Toast.LENGTH_SHORT).show();
                    }else {
                        ((Throwable)data).printStackTrace();
                    }
                }
            }
        }
    };
    /*
    初始化控件
     */
    private void init() {
        inputPhoneEt = (AutoCompleteTextView)findViewById(R.id.regist_input_phone_et);
        inputCodeEt = (EditText)findViewById(R.id.regist_input_code_et);
        requestCodeBtn = (Button)findViewById(R.id.regist_request_code_btn);
        commitBtn = (Button)findViewById(R.id.regist_commit_btn);
        requestCodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        //启动短信验证sdk
        cn.smssdk.SMSSDK.initSDK(this,APPKEY,APPSECRETE);
        final EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调接口
        cn.smssdk.SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();
        switch (v.getId()){
            case R.id.regist_request_code_btn:
                //1.通过规则判断手机号
                if(!judgePhoneNums(phoneNums)){
                    return;
                }//2.通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);
                //3.把按钮变成不可点击
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(;i > 0;i--){
                            handler.sendEmptyMessage(-9);
                            if(i <= 0){
                                break;
                            }
                            try{
                                Thread.sleep(1000);
                            } catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case R.id.regist_commit_btn:
                //将收到的验证码和手机号提交再次审核
                SMSSDK.submitVerificationCode("86",phoneNums,inputCodeEt.getText().toString());
                //createProgressBar();
                Intent intent = new Intent(RegistActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*
    判断手机号是否合理
     */
    private boolean judgePhoneNums(String phoneNums){
        if(isMatchLength(phoneNums,11)&&isMobileNo(phoneNums)){
            return true;
        }
        Toast.makeText(this,"手机号输入有误",Toast.LENGTH_SHORT).show();
        return false;
    }

    /*
    判断一个字符串的位数是否规定位
     */
    public  static  boolean isMatchLength(String str,int length){
        if(str.isEmpty()){
            return false;
        }else {
            return str.length() == length?true:false;
        }
    }
    /*
    验证手机格式
     */
    public static boolean isMobileNo(String mobileNums){
    /*
        * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
        * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if(TextUtils.isEmpty((mobileNums)))
            return false;
        else return mobileNums.matches(telRegex);
    }

    /*
    progressbar
     */
    private void createProgressBar(){
        FrameLayout layout = (FrameLayout)findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }


    @Override
    protected void onDestroy() {
        cn.smssdk.SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}

