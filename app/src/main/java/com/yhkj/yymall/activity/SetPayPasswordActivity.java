package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.PasswordView;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/13.
 */

public class SetPayPasswordActivity extends BaseToolBarActivity {

    @Bind(R.id.aspp_passwordview)
    PasswordView mPasswordView;

    @Bind(R.id.aspp_tv_phone)
    TextView mTvPhone;

    private String mTicket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpaypassword);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        mTicket = getIntent().getStringExtra("ticket");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    private CharSequence mPassword;
    private int mPwdSize = 6;
    @Override
    protected void initData() {
        setTvTitleText("设置支付密码");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        mTvPhone.setText("请为账户" + YYApp.getInstance().getPhone());

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ViseLog.e(s);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= mPwdSize){
                    mPassword = s;
                    mPasswordView.setPassword(mPassword);
                    if (s.length() == 6){
                        if (TextUtils.isEmpty(mTicket)) return;
                        YYMallApi.updatePayPwd(SetPayPasswordActivity.this, mTicket, mPasswordView.getPassword().toString(), new YYMallApi.ApiResult<CommonBean>(SetPayPasswordActivity.this) {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                ViseLog.e(e);
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onNext(CommonBean commonBean) {
                                showToast("设置成功");
                                AppManager.getInstance().finishActivity(SetPayPasswordActivity.this);
                                AppManager.getInstance().finishActivity(UpdatepwdActivity.class);
                            }
                        });
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                ViseLog.e(s);
            }
        });
        mPasswordView.setPasswordCount(mPwdSize);
    }
}
