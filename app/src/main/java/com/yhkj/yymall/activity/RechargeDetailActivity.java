package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.RechargeDetailBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/30.
 */

public class RechargeDetailActivity extends BaseToolBarActivity {

    @Bind(R.id.ar_tv_paytype)
    TextView mTvPayType;

    @Bind(R.id.ar_tv_price)
    TextView mTvPrice;

    @Bind(R.id.ar_tv_status)
    TextView mTvStatus;

    @Bind(R.id.ar_tv_acount)
    TextView mTvAcount;

//    @Bind(R.id.ar_tv_acountname)
//    TextView mTvAcountName;

    @Bind(R.id.ar_tv_date)
    TextView mTvDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechargedetail);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    private void getData(){
        YYMallApi.getRechargeDeatail(this, getIntent().getIntExtra("id", 0), new YYMallApi.ApiResult<RechargeDetailBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(RechargeDetailBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setUi(dataBean);
            }
        });
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void setUi(RechargeDetailBean.DataBean dataBean) {
        mTvPayType.setText(dataBean.getInfo().getPay_name());
        mTvPrice.setText(dataBean.getInfo().getAccount());
        mTvAcount.setText(dataBean.getInfo().getTelphone());
//        mTvAcountName.setText(getIntent().getStringExtra("name"));
        mTvDate.setText(dataBean.getInfo().getTime());
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        setTvTitleText("详情");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
