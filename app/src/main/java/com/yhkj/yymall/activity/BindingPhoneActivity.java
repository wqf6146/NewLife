package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.RegisterBean;
import com.yhkj.yymall.bean.UserConfig;
import com.yhkj.yymall.http.YYMallApi;

import java.util.HashMap;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/30.
 */

public class BindingPhoneActivity extends BaseToolBarActivity implements View.OnClickListener {

    @Bind(R.id.ed_binding_phone)
    EditText ed_binding_phone;

    @Bind(R.id.ed_binding_yzm)
    EditText ed_binding_yzm;

    @Bind(R.id.bt_binding_yzm)
    Button bt_binding_yzm;

    @Bind(R.id.bt_binding_next)
    Button bt_binding_next;

    @Bind(R.id.rl_binding_gou1)
    LinearLayout rl_binding_gou1;

    @Bind(R.id.img_binding_gou1)
    ImageView img_binding_gou1;

    private Intent intent;
    private String uid, iconurl, name, type;
    private Boolean ofYhxy = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bt_binding_yzm.setText("剩余" + msg.arg1 + "秒");
            if (msg.arg1 == 0) {
                bt_binding_yzm.setText("重新获取");
                bt_binding_yzm.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("绑定手机");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.INVISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
        bt_binding_yzm.setOnClickListener(this);
        bt_binding_next.setOnClickListener(this);
        rl_binding_gou1.setOnClickListener(this);
        intent = getIntent();
        uid = intent.getStringExtra("uid");
        iconurl = intent.getStringExtra("iconurl");
        name = intent.getStringExtra("name");
        type = intent.getStringExtra("type");
    }
    private void createAccountThenLoginChatServer(String phone) {
        final String account = "yiyiyaya_"+phone;
        final String userPwd = "123456";
        // createAccount to huanxin server
        // if you have a account, this step will ignore
        ChatClient.getInstance().register(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onError(final int errorCode, final String error) {
                Log.e(LoginActivity.class.toString(),error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_binding_yzm:
                if (ed_binding_phone.getText().toString().equals("") || ed_binding_phone == null || ed_binding_phone.length() == 0) {
                    showToast("请输入11位手机号");
                } else if (ed_binding_phone.getText().length() < 11) {
                    showToast("手机号格式错误");
                } else {
                    YYMallApi.getNote(BindingPhoneActivity.this, ed_binding_phone.getText().toString(), new ApiCallback<CommonBean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(ApiException e) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(CommonBean commonBean) {
                            new Thread(sendable).start();
                            bt_binding_yzm.setClickable(false);
                        }
                    });

                }
                break;
            case R.id.bt_binding_next:
                final String phone = ed_binding_phone.getText().toString();
                if (!ofYhxy) {
                    showToast("请勾选同意【服务协议】！");
                } else if (TextUtils.isEmpty(phone)) {
                    showToast("请输入11位手机号");
                } else if (phone.length() < 11) {
                    showToast("手机号码格式错误");
                } else if (TextUtils.isEmpty(ed_binding_yzm.getText().toString()) || ed_binding_yzm.getText().length() < 6) {
                    showToast("请输入6位短信验证码");
                } else {

                    YYMallApi.getAuthThdBind(BindingPhoneActivity.this, phone, uid, name, iconurl, type, ed_binding_yzm.getText().toString(), new ApiCallback<RegisterBean.DataBean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(ApiException e) {
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(RegisterBean.DataBean dataBean) {
                            createAccountThenLoginChatServer(phone);
                            DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                            UserConfig userConfig = new UserConfig();
                            userConfig.setToken(dataBean.getToken());
                            userConfig.setPhone(phone);
                            userConfig.setState(true);
                            DbHelper.getInstance().userConfigLongDBManager().insert(userConfig);
                            YYApp.getInstance().setToken(dataBean.getToken());
                            AppManager.getInstance().finishActivity(LoginActivity.class);
                            AppManager.getInstance().finishActivity(BindingPhoneActivity.this);
                        }
                    });
                }
                break;
            case R.id.rl_binding_gou1:
                if (ofYhxy) {
                    img_binding_gou1.setBackgroundResource(R.mipmap.login_gou1);
                    bt_binding_next.setClickable(false);
                    bt_binding_next.setBackgroundResource(R.drawable.bt_yuan_hui);
                    bt_binding_next.setTextColor(Color.parseColor("#FFFFFF"));
                    ofYhxy = false;
                } else {
                    img_binding_gou1.setBackgroundResource(R.mipmap.login_gou);
                    bt_binding_next.setClickable(true);
                    bt_binding_next.setBackgroundResource(R.drawable.bt_yuan_white);
                    bt_binding_next.setTextColor(getResources().getColor(R.color.theme_bule));
                    ofYhxy = true;
                }
                break;

            default:
                break;
        }
    }
    private void registerLx(final String phone) {
        ChatClient.getInstance().register(phone, "123456", new Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器
                        loginHx(phone);
                    }
                });
            }

            @Override
            public void onError(final int errorCode, final String error) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (errorCode == Error.NETWORK_ERROR){
                            Log.e("registerHx","网络连接不可用，请检查网络");
//                            Toast.makeText(getApplicationContext(), "网络连接不可用，请检查网络", Toast.LENGTH_SHORT).show();
                        }else if (errorCode == Error.USER_ALREADY_EXIST){
                            Log.e("registerHx","账户已存在");
                        }else if(errorCode == Error.USER_AUTHENTICATION_FAILED){
                            Log.e("registerHx","无开放注册权限");
//                            Toast.makeText(getApplicationContext(), "无开放注册权限", Toast.LENGTH_SHORT).show();
                        } else if (errorCode == Error.USER_ILLEGAL_ARGUMENT){
                            Log.e("registerHx","用户名不合法");
//                            Toast.makeText(getApplicationContext(), "用户名不合法", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e("registerHx","error");
//                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
    private void loginHx(final String phone) {
        ChatClient.getInstance().login(phone, "123456", new Callback() {
            @Override
            public void onSuccess() {
                Log.d(BindingPhoneActivity.class.toString(), "Hx login success!");
//                                if (!progressShow) {
//                                    return;
//                                }
//                                toChatActivity();
            }

            @Override
            public void onError(int code, String error) {
                Log.e(BindingPhoneActivity.class.toString(), "Hx login fail,code:" + code + ",error:" + error);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
////                                        progressDialog.dismiss();
////                                        Toast.makeText(ChatLoginActivity.this,
////                                                "联系客服失败",
////                                                Toast.LENGTH_SHORT).show();
////                                        finish();
//                                    }
//                                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            int a = 60;
            while (-1 < a) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.arg1 = a;
                    handler.sendMessage(message);
                    a--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };
}