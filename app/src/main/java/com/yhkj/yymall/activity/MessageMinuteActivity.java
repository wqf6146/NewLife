package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/24.
 */

public class MessageMinuteActivity extends BaseToolBarActivity {

    @Bind(R.id.tv_messageminute_time)
    TextView tv_messageminute_time;

    @Bind(R.id.tv_messageminute_con)
    TextView tv_messageminute_con;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messageminute);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("消息内容");
        setImgBackVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        intent =getIntent();
        tv_messageminute_time.setText(intent.getStringExtra("time"));
        tv_messageminute_con.setText(intent.getStringExtra("content"));

    }
}
