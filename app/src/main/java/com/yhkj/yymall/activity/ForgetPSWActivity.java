package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class ForgetPSWActivity extends BaseToolBarActivity implements View.OnClickListener {

    @Bind(R.id.ed_forget_phone)
    EditText ed_forget_phone;

    @Bind(R.id.ed_forget_yzm)
    EditText ed_forget_yzm;

    @Bind(R.id.ed_forget_psw)
    EditText ed_forget_psw;

    @Bind(R.id.bt_forget_yzm)
    Button bt_forget_yzm;

    @Bind(R.id.bt_register_next)
    Button bt_register_next;

    @Bind(R.id.img_forget_eye)
    ImageView img_forget_eye;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            bt_forget_yzm.setText("剩余" + msg.arg1 + "秒");
            if (msg.arg1 == 0) {
                bt_forget_yzm.setText("重新获取");
                bt_forget_yzm.setClickable(true);
            }
        }
    };
    private Boolean boolxs = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpsw);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("找回密码");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.INVISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
        bt_forget_yzm.setOnClickListener(this);
        bt_register_next.setOnClickListener(this);
        img_forget_eye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_forget_eye:
                if (boolxs) {
                    img_forget_eye.setImageResource(R.mipmap.login_eye);
                    ed_forget_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    boolxs = false;
                } else {
                    img_forget_eye.setImageResource(R.mipmap.login_eye1);
                    ed_forget_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    boolxs = true;
                }
                break;

            case R.id.bt_forget_yzm:
                if (ed_forget_phone.getText().toString().equals("") || ed_forget_phone == null) {
                    showToast("请输入11位手机号");
                } else if (ed_forget_phone.getText().length() < 11) {
                    showToast("手机号格式错误");
                } else {
                    YYMallApi.getNote(ForgetPSWActivity.this, ed_forget_phone.getText().toString(), new ApiCallback<CommonBean>() {
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
                            bt_forget_yzm.setClickable(false);
                            new Thread(sendable).start();
                        }
                    });

                }

                break;
            case R.id.bt_register_next:
                if (ed_forget_phone.getText().equals("") || ed_forget_phone.getText() == null || ed_forget_phone.getText().toString().equals("")) {
                    showToast("请输入11位手机号");
                } else if (ed_forget_phone.getText().toString().length() < 11) {
                    showToast("手机号码格式错误");
                } else if (ed_forget_psw.getText().toString() == null || ed_forget_psw.getText().toString().equals("")) {
                    showToast("请输入登录密码");
                } else if (ed_forget_psw.getText().length() < 6 ||
                        ed_forget_psw.getText().equals("")) {
                    showToast("登录密码至少6位长度！");
                } else if (ed_forget_yzm.getText().equals("") || ed_forget_yzm == null || ed_forget_yzm.getText().length() < 6) {
                    showToast("请输入6位短信验证码");
                } else {
                    YYMallApi.getForget(ForgetPSWActivity.this, ed_forget_phone.getText().toString(), ed_forget_psw.getText().toString(), ed_forget_yzm.getText().toString(), new ApiCallback<CommonBean>() {
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
                        public void onNext(CommonBean commonBean) {
                            HashMap hashMap = new HashMap<>();
                            hashMap.put("phone", ed_forget_phone.getText().toString());
                            hashMap.put("password", ed_forget_psw.getText().toString());
                            YYMallApi.getLogin(ForgetPSWActivity.this, hashMap, new ApiCallback<RegisterBean.DataBean>() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onNext(RegisterBean.DataBean dataBean) {
                                    showToast("修改成功");
                                    createAccountThenLoginChatServer(ed_forget_phone.getText().toString());
                                    DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                                    UserConfig userConfig = new UserConfig();
                                    userConfig.setToken(dataBean.getToken());
                                    userConfig.setState(false);
                                    userConfig.setPhone(ed_forget_phone.getText().toString());
                                    DbHelper.getInstance().userConfigLongDBManager().insert(userConfig);
                                    YYApp.getInstance().setToken(dataBean.getToken());
                                    startActivity(MainActivity.class);
                                    AppManager.getInstance().finishActivity(ForgetPSWActivity.this);
                                }

                                @Override
                                public void onError(ApiException e) {
                                    super.onError(e);
                                    showToast(e.getMessage());
                                }
                            });
                        }
                    });

                }
                break;
            default:
                break;

        }
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
                if (errorCode == Error.USER_ALREADY_EXIST)
                    loginHx(account);
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
                Log.d(LoginActivity.class.toString(), "Hx login success!");
            }

            @Override
            public void onError(int code, String error) {
                Log.e(LoginActivity.class.toString(), "Hx login fail,code:" + code + ",error:" + error);
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
