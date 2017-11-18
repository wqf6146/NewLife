package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/9/6.
 */

public class GoodsGoneActivity extends BaseToolBarActivity {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_refresh_recycleview);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {

    }
}
