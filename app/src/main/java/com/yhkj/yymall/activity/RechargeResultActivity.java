package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.vise.xsnow.manager.AppManager;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RechargeResultActivity extends BaseToolBarActivity {

    @Bind(R.id.ar_tv_price)
    TextView mTvPrice;

    @Bind(R.id.ar_tv_commit)
    TextView mTvCommit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rechargeresult);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("充值结果");
        mTvPrice.setText(getIntent().getStringExtra("price") + "元");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(RechargeResultActivity.this);
            }
        });
    }
}
