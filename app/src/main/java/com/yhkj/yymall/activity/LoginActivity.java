package com.yhkj.yymall.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.InputMethodUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.base.HxHelper;
import com.yhkj.yymall.bean.RegisterBean;
import com.yhkj.yymall.bean.UserConfig;
import com.yhkj.yymall.http.YYMallApi;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/6/29.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_login_back)
    LinearLayout ll_login_back;

    @Bind(R.id.ll_login_zddl)
    LinearLayout ll_login_zddl;

    @Bind(R.id.ll_login_forget)
    LinearLayout ll_login_forget;

    @Bind(R.id.img_login_eye)
    ImageView img_login_eye;

    @Bind(R.id.img_login_weibo)
    ImageView img_login_weibo;

    @Bind(R.id.img_login_weixin)
    ImageView img_login_weixin;

    @Bind(R.id.img_login_qq)
    ImageView img_login_qq;

    @Bind(R.id.img_login_gou)
    ImageView img_login_gou;

    @Bind(R.id.ed_login_psw)
    EditText ed_login_psw;

    @Bind(R.id.ed_login_phone)
    EditText ed_login_phone;

    @Bind(R.id.bt_login_next)
    Button bt_login_next;

    @Bind(R.id.bt_login_zc)
    Button bt_login_zc;

    private Boolean boolxs = true;
    private Boolean boolzddl = true;
    private Boolean zddl = true;
    private Intent intent;
    private String uid, iconurl, name;
    private HashMap<String, String> hashMap;
    private String type;



    private void tryCreateHxAccount(String phone){
//        if(AndPermission.hasPermission(this, Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.WRITE_SETTINGS)) {
            // 有权限，直接do anything.
            createAccountThenLoginChatServer(phone);
//        }
    }

    private void createAccountThenLoginChatServer(String phone) {
        final String account = phone;
        final String userPwd = "123456";
        ChatClient.getInstance().register(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginHx(account);
                    }
                });
            }

            @Override
            public void onError(final int errorCode, final String error) {
                Log.e(LoginActivity.class.toString(),error);
                if (errorCode == Error.USER_ALREADY_EXIST)
                    loginHx(account);
                else{
                    AppManager.getInstance().finishActivity(LoginActivity.this);
                }
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
                AppManager.getInstance().finishActivity(LoginActivity.this);
            }

            @Override
            public void onError(int code, String error) {
                Log.e(LoginActivity.class.toString(), "Hx login fail,code:" + code + ",error:" + error);
                AppManager.getInstance().finishActivity(LoginActivity.this);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    protected void bindEvent() {

    }

    @Override
    protected void initView() {

    }

    private UMAuthListener umAuthListener = new UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            uid = map.get("uid");
            iconurl = map.get("iconurl");
            name = map.get("name");
            YYMallApi.getAuthThdsignIn(LoginActivity.this, uid, type, new ApiCallback<RegisterBean.DataBean>() {
                @Override
                public void onStart() {

                }
                @Override
                public void onError(ApiException e) {
                    showToast(e.getMessage());
                    if (e.getCode() == 1005) {
                        Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("iconurl", iconurl);
                        intent.putExtra("name", name);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCompleted() {

                }
                @Override
                public void onNext(RegisterBean.DataBean dataBean) {
                    DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                    UserConfig userConfig = new UserConfig();
                    userConfig.setToken(dataBean.getToken());
                    userConfig.setState(zddl);
                    DbHelper.getInstance().userConfigLongDBManager().insert(userConfig);
                    YYApp.getInstance().setToken(dataBean.getToken());
                    AppManager.getInstance().finishActivity(LoginActivity.this);
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(LoginActivity.this).setShareConfig(config);
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    protected void initData() {
        setStatusViewVisiable(false);
        ll_login_back.setOnClickListener(this);
        img_login_eye.setOnClickListener(this);
        ll_login_zddl.setOnClickListener(this);
        bt_login_zc.setOnClickListener(this);
        bt_login_next.setOnClickListener(this);
        ll_login_forget.setOnClickListener(this);
        img_login_weibo.setOnClickListener(this);
        img_login_weixin.setOnClickListener(this);
        img_login_qq.setOnClickListener(this);
//        setStatusColor(getResources().getColor(R.color.theme_bule));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_login_back:
                finish();
                break;
            case R.id.img_login_eye:
                if (boolxs) {
                    img_login_eye.setBackgroundResource(R.mipmap.login_eye1);
                    ed_login_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ed_login_psw.setSelection(ed_login_psw.length());
                    boolxs = false;
                } else {
                    img_login_eye.setBackgroundResource(R.mipmap.login_eye);
                    ed_login_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ed_login_psw.setSelection(ed_login_psw.length());
                    boolxs = true;
                }
                break;
            case R.id.ll_login_zddl:
                if (boolzddl) {
                    img_login_gou.setBackgroundResource(R.mipmap.login_gou1);
                    boolzddl = false;
                    zddl = false;
                } else {
                    img_login_gou.setBackgroundResource(R.mipmap.login_gou);
                    boolzddl = true;
                    zddl = true;
                }
                break;
            case R.id.bt_login_next:
                //登录
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                final String phone = ed_login_phone.getText().toString();
                String psw = ed_login_psw.getText().toString();
                if (phone.length() == 0 || phone.toString() == null) {
                    showToast("请输入11位手机号码");
                } else if (phone.length() < 11) {
                    showToast("手机号码格式错误");
                } else if (psw.length() < 6 || psw.length() > 12) {
                    showToast("请输入6~12位密码");
                } else {
                    hashMap = new HashMap<>();
                    hashMap.put("phone", phone);
                    hashMap.put("password", psw);
                    YYMallApi.getLogin(LoginActivity.this, hashMap, new ApiCallback<RegisterBean.DataBean>() {
                        @Override
                        public void onStart() {

                        }
                        @Override
                        public void onError(ApiException e) {
                            showToast(e.getMessage());
                            if (e.getCode() == 1005) {
                                Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("iconurl", iconurl);
                                intent.putExtra("name", name);
                                intent.putExtra("uid", uid);
                                startActivity(intent);
                            }
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
                            userConfig.setState(zddl);
                            DbHelper.getInstance().userConfigLongDBManager().insert(userConfig);
                            YYApp.getInstance().setToken(dataBean.getToken());
                            tryCreateHxAccount(phone);
                        }
                    });
                }
                break;
            case R.id.bt_login_zc:
                //注册
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_login_forget:
                //忘记密码
                intent = new Intent(getApplicationContext(), ForgetPSWActivity.class);
                startActivity(intent);
                break;
            case R.id.img_login_qq:
                //QQ登录
                type = "1";
                UMShareAPI mShareAPIa = UMShareAPI.get(LoginActivity.this);
                mShareAPIa.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.img_login_weixin:
                //微信登录
                type = "2";
                UMShareAPI mShareAPIs1 = UMShareAPI.get(LoginActivity.this);
                mShareAPIs1.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.img_login_weibo:
                //微博登录
                type = "3";
                UMShareAPI mShareAPIs = UMShareAPI.get(LoginActivity.this);
                mShareAPIs.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
