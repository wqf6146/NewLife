package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/5.
 */

public class PayDefeatedActivity extends BaseToolBarActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paydefeated);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("支付结果");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
        setTvRightVisiable(View.VISIBLE);
        setTvRightText("完成");
    }
}
