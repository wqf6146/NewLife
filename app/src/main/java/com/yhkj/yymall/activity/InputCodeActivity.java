package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.InputMethodUtils;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.InputCodeBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/8/9.
 */

public class InputCodeActivity extends BaseActivity {

    @Bind(R.id.ai_img_leftbtn)
    ImageView mImgLeftBtn;

    @Bind(R.id.ai_et_code)
    EditText mEtCode;

    @Bind(R.id.ai_tv_share)
    TextView mTvShare;

    @Bind(R.id.ai_tv_rule)
    TextView mTvRule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputcode);
    }

    @Override
    protected void initView() {
        setStatusViewVisiable(false);
    }

    @Override
    protected void bindEvent() {
        mTvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputCodeActivity.this,WebActivity.class);
                intent.putExtra("title","邀请好友规则");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.YAOQINGGUIZE);
                startActivity(intent);
            }
        });
        mImgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                AppManager.getInstance().finishActivity(InputCodeActivity.this);
            }
        });
        mTvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEtCode.getText().toString();
                if (TextUtils.isEmpty(code) ){
                    showToast("请输入邀请码");
                    return;
                }
                if (code.length() < 8){
                    showToast("邀请码必须是8位数字");
                    return;
                }
                YYMallApi.checkInvite(InputCodeActivity.this, code, new YYMallApi.ApiResult<InputCodeBean.DataBean>(InputCodeActivity.this) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(InputCodeBean.DataBean bean) {
                        if (bean.getYaya() != 0)
                            showToast(bean.getDesc());
                        AppManager.getInstance().finishActivity(InputCodeActivity.this);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        closeKeyboard();
        super.onBackPressedSupport();
    }

    @Override
    protected void initData() {
        String code = getIntent().getStringExtra("code");
        if (!TextUtils.isEmpty(code))
            mEtCode.setText(code);
    }
}
