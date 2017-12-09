package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.RegisterBean;
import com.yhkj.yymall.bean.UserConfig;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CheckPhone;
import com.yhkj.yymall.view.pageindicatorview.animation.type.FillAnimation;

import java.util.HashMap;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/30.
 */

public class RegisterActivity extends BaseToolBarActivity implements View.OnClickListener {

    @Bind(R.id.ed_register_phone)
    EditText ed_register_phone;

    @Bind(R.id.ed_register_psw)
    EditText ed_register_psw;

    @Bind(R.id.ed_register_psw_2)
    EditText mEditPwdComfim;

    @Bind(R.id.ed_register_yzm)
    EditText ed_register_yzm;

    @Bind(R.id.bt_register_next)
    Button bt_register_next;

    @Bind(R.id.bt_register_yzm)
    Button bt_register_yzm;

    @Bind(R.id.rl_register_gou1)
    LinearLayout rl_register_gou1;

    @Bind(R.id.img_register_gou1)
    ImageView img_register_gou1;

    @Bind(R.id.img_register_eye)
    ImageView img_register_eye;

    @Bind(R.id.img_register_eye_2)
    ImageView mImgEye2;

    @Bind(R.id.tv_register_fwxy)
    TextView mTvRegister;

    private Boolean boolxs = true;
    private Boolean ofYhxy = false;
    private Handler handler;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                bt_register_yzm.setText("剩余" + msg.arg1 + "秒");
                if (msg.arg1 == 0) {
                    bt_register_yzm.setText("重新获取");
                    bt_register_yzm.setClickable(true);
                }
            }
        };
    }

    @Override
    protected void initView() {
        super.initView();
//        setStatusVisiable(GONE);
        setNetWorkErrShow(GONE);
    }
    private void createAccountThenLoginChatServer(final String phone) {
        final String account = phone;
        final String userPwd = "123456";
        ChatClient.getInstance().register(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                loginHx(phone);
            }

            @Override
            public void onError(final int errorCode, final String error) {
                if (errorCode == Error.USER_ALREADY_EXIST)
                    loginHx(phone);
                else{
                    AppManager.getInstance().finishActivity(LoginActivity.class);
                    AppManager.getInstance().finishActivity(RegisterActivity.class);
                }
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,WebActivity.class);
                intent.putExtra("title","服务协议");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.FUWUXIEYI);
                startActivity(intent);
            }
        });
        mImgEye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImgEye2.getTag() == null || (Boolean) mImgEye2.getTag() == false){
                    mImgEye2.setTag(true);
                    mImgEye2.setImageResource(R.mipmap.login_eye1);
                    mEditPwdComfim.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditPwdComfim.setSelection(mEditPwdComfim.length());
                }else {
                    mImgEye2.setTag(false);
                    mImgEye2.setImageResource(R.mipmap.login_eye);
                    mEditPwdComfim.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEditPwdComfim.setSelection(mEditPwdComfim.length());
                }

            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("用户注册");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.INVISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);

        bt_register_next.setOnClickListener(this);
        bt_register_yzm.setOnClickListener(this);
        rl_register_gou1.setOnClickListener(this);
        img_register_eye.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register_next:
                //完成注册
                String pwd = ed_register_psw.getText().toString();
                String pwd2 = mEditPwdComfim.getText().toString();
                final String phone = ed_register_phone.getText().toString();
                if (!ofYhxy) {
                    showToast("请勾选同意【服务协议】！");
                } else if (TextUtils.isEmpty(phone)) {
                    showToast("请输入11位手机号");
                } else if (phone.length() < 11) {
                    showToast("手机号码格式错误");
                } else if (pwd.length() < 6 || TextUtils.isEmpty(pwd)) {
                    showToast("请输入6~12位密码");
                } else if (!pwd.equals(pwd2)){
                    showToast("两次输入密码不一致");
                } else if (ed_register_yzm.getText().equals("") || ed_register_yzm == null || ed_register_yzm.getText().length() < 6) {
                    showToast("请输入6位短信验证码");
                } else {
                    hashMap = new HashMap<>();
                    hashMap.put("phone", phone);
                    hashMap.put("password", pwd);
                    hashMap.put("msgcode", ed_register_yzm.getText().toString());
                    YYMallApi.getRegister(RegisterActivity.this, hashMap, new ApiCallback<RegisterBean.DataBean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(ApiException e) {
                            ViseLog.e(e);
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(RegisterBean.DataBean dataBean) {
                            DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                            UserConfig userConfig = new UserConfig();
                            userConfig.setPhone(phone);
                            userConfig.setToken(dataBean.getToken());
                            userConfig.setHeadIco(dataBean.getHeadIco());
                            userConfig.setState(true);
                            DbHelper.getInstance().userConfigLongDBManager().insert(userConfig);
                            YYApp.getInstance().setToken(dataBean.getToken());
                            createAccountThenLoginChatServer(phone);

                        }
                    });

                }

                break;

            case R.id.bt_register_yzm:
                //获取验证码
                if (ed_register_phone.getText().toString().equals("") || ed_register_phone == null || ed_register_phone.length() == 0) {
                    showToast("请输入11位手机号");
                } else if (ed_register_phone.getText().length() < 11 || !CheckPhone.isPhoneNum(ed_register_phone.getText().toString())) {
                    showToast("手机号格式错误");
                } else {
                    YYMallApi.getNote(RegisterActivity.this, ed_register_phone.getText().toString(), new ApiCallback<CommonBean>() {
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
                            bt_register_yzm.setClickable(false);
                            new Thread(sendable).start();
                        }
                    });
                }

                break;
            case R.id.rl_register_gou1:
                //勾选服务协议
                if (ofYhxy) {
                    img_register_gou1.setBackgroundResource(R.mipmap.login_gou1);
                    bt_register_next.setBackgroundResource(R.drawable.bt_yuan_hui);
                    bt_register_next.setTextColor(Color.parseColor("#FFFFFF"));
                    ofYhxy = false;
                } else {
                    img_register_gou1.setBackgroundResource(R.mipmap.login_gou);
                    bt_register_next.setBackgroundResource(R.drawable.bt_yuan_white);
                    bt_register_next.setTextColor(getResources().getColor(R.color.theme_bule));
                    ofYhxy = true;
                }

                break;
            case R.id.img_register_eye:
                //显示密码
                if (boolxs) {
                    img_register_eye.setImageResource(R.mipmap.login_eye1);
                    ed_register_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ed_register_psw.setSelection(ed_register_psw.length());
                    boolxs = false;
                } else {
                    img_register_eye.setImageResource(R.mipmap.login_eye);
                    ed_register_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ed_register_psw.setSelection(ed_register_psw.length());
                    boolxs = true;
                }
                break;

            default:
                break;

        }
    }


    private void loginHx(final String phone) {
        ChatClient.getInstance().login(phone, "123456", new Callback() {
            @Override
            public void onSuccess() {
                Log.d(RegisterActivity.class.toString(), "Hx login success!");
                AppManager.getInstance().finishActivity(LoginActivity.class);
                AppManager.getInstance().finishActivity(RegisterActivity.class);
            }

            @Override
            public void onError(int code, String error) {
                Log.e(RegisterActivity.class.toString(), "Hx login fail,code:" + code + ",error:" + error);
                AppManager.getInstance().finishActivity(LoginActivity.class);
                AppManager.getInstance().finishActivity(RegisterActivity.class);
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
