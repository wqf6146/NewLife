package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.CountDownButton;
import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/22.
 */

public class UpdatePhoneActivity extends BaseToolBarActivity {

    @Bind(R.id.av_tv_getcode)
    CountDownButton mBtnGetCode;

    @Bind(R.id.av_edit_code)
    EditText mEditCode;

    @Bind(R.id.av_edit_phone)
    EditText mEditPhone;

    @Bind(R.id.av_tv_commit)
    TextView mTvCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatephone);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
//
//        mType = getIntent().getIntExtra(Constant.PWDTYPE.TYPE,Constant.PWDTYPE.LOGIN);
//        if (mType != Constant.PWDTYPE.LOGIN){
//            mLlNewPwd.setVisibility(GONE);
//        }
//        mPhone = YYApp.getInstance().getPhone();
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
                mPhone = mEditPhone.getText().toString();
                if (!TextUtils.isEmpty(mPhone)) {
                    YYMallApi.getNote(UpdatePhoneActivity.this, mPhone, new ApiCallback<CommonBean>() {
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
                }else{
                    showToast("请输入11位手机号码");
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

                YYMallApi.updatePhone(UpdatePhoneActivity.this, mPhone, code, new YYMallApi.ApiResult<CommonBean>(UpdatePhoneActivity.this) {
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
                        showToast("修改成功,请重新登录");
                        YYApp.getInstance().setToken(null);
                        DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                        AppManager.getInstance().finishExceptActivity(MainActivity.class);
                        startActivity(LoginActivity.class);
                    }
                });

            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("修改手机号码");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

//        if (!TextUtils.isEmpty(mPhone))
//            mTvWord.setText(String.format(getString(R.string.codemsg),mPhone));
    }
}
