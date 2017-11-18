package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.AssetBean;
import com.yhkj.yymall.bean.BalanceBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/12.
 */

public class MyPriceActivity extends BaseToolBarActivity {

    @Bind(R.id.am_tv_myprice)
    TextView mTvMyPrice;

    @Bind(R.id.am_ll_apply)
    LinearLayout mLlApply;

    @Bind(R.id.am_ll_recharge)
    LinearLayout mLlRecharge;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprice);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BalanceLogActivity.class);
            }
        });

        mLlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MyPriceActivity.this,ApplyWithdrawActivity.class),0);
            }
        });
        mLlRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MyPriceActivity.this,RechargeActivity.class),0);
            }
        });
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("我的余额");
        setTvRightText("明细");
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private String mPrice;
    private void getData(){
        YYMallApi.getUserAsset(this, false,new YYMallApi.ApiResult<AssetBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(View.VISIBLE);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(AssetBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                mPrice = dataBean.getBalance();
                mTvMyPrice.setText(mPrice);
            }
        });
    }
}
