package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/31.
 */

public class RentTurnSucActivity extends BaseToolBarActivity {

    @Bind(R.id.ar_tv_gohome)
    TextView mTvGoHome;

    @Bind(R.id.ar_tv_goyy)
    TextView mTvGoYY;

    @Bind(R.id.ar_tv_remainyy)
    TextView mTvRemainYY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentturnsuc);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("置换成功");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        mTvRemainYY.setText("剩余丫丫" + getIntent().getStringExtra("yy") + "个");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYApp.getInstance().setIndexShow(0);
                startActivity(MainActivity.class);
            }
        });
        mTvGoYY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentTurnSucActivity.this, MyYaYaActivity.class);
                intent.putExtra("value", getIntent().getStringExtra("yy"));
                startActivity(intent);
            }
        });
    }
}

