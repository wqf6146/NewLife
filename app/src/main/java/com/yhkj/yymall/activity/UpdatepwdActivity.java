package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.PayGetTiketBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.CountDownButton;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/22.
 */

public class UpdatepwdActivity extends BaseToolBarActivity {

    @Bind(R.id.av_tv_getcode)
    CountDownButton mBtnGetCode;

    @Bind(R.id.av_edit_code)
    EditText mEditCode;

    @Bind(R.id.av_edit_pwd)
    EditText mEditPwd;

    @Bind(R.id.av_tv_commit)
    TextView mTvCommit;

    @Bind(R.id.au_tv_word)
    TextView mTvWord;

    @Bind(R.id.au_ll_newpwd)
    LinearLayout mLlNewPwd;

    private String mCode;

    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepwd);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        mType = getIntent().getIntExtra(Constant.PWDTYPE.TYPE,Constant.PWDTYPE.LOGIN);
        if (mType != Constant.PWDTYPE.LOGIN){
            mLlNewPwd.setVisibility(GONE);
        }
        mPhone = YYApp.getInstance().getPhone();
    }

    private String mPhone;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnGetCode.isCountDownNow())
                    return;
                if (!TextUtils.isEmpty(mPhone)) {
                    YYMallApi.getNote(UpdatepwdActivity.this, mPhone, new ApiCallback<CommonBean>() {
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
                        public void onNext(CommonBean commonBean) {
                            showToast("发送成功");
                            if (!mBtnGetCode.isCountDownNow())
                                mBtnGetCode.startCountDown();
                        }
                    });
                }
            }
        });

        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEditCode.getText().toString();
                if (TextUtils.isEmpty(code) || code.length() != 6){
                    showToast("请填写6位验证码");
                    return;
                }

                if (mType == Constant.PWDTYPE.PAY){
                    YYMallApi.getPayPwdTicket(UpdatepwdActivity.this, code, new YYMallApi.ApiResult<PayGetTiketBean.DataBean>(UpdatepwdActivity.this) {
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
                        public void onNext(PayGetTiketBean.DataBean dataBean) {
                            Intent intent =  new Intent(UpdatepwdActivity.this,SetPayPasswordActivity.class);
                            intent.putExtra("ticket",dataBean.getTicket());
                            startActivity(intent);
                        }
                    });
                    return;
                }

                String pwd = mEditPwd.getText().toString();
                if (TextUtils.isEmpty(pwd) ){
                    showToast("请填写新密码");
                    return;
                }

                YYMallApi.getForget(UpdatepwdActivity.this, mPhone, pwd, code, new ApiCallback<CommonBean>() {
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
                    public void onNext(CommonBean commonBean) {
                        showToast("修改成功");
                        AppManager.getInstance().finishActivity(UpdatepwdActivity.this);
                    }
                });

            }
        });
    }

    @Override
    protected void initData() {
        if (mType == Constant.PWDTYPE.PAY) {
            setTvTitleText("设置支付密码");
            mTvCommit.setText("下一步");
        }else {
            setTvTitleText("修改密码");
        }
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        if (!TextUtils.isEmpty(mPhone))
            mTvWord.setText(String.format(getString(R.string.codemsg), CommonUtil.getMarkPhone(mPhone)));
    }
}
