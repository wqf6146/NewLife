package com.yhkj.yymall.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.TimeKillClassifBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/8/7.
 */

public class TimeKillClassifyFragment extends BaseFragment {

    @Bind(R.id.ft_recycleview)
    RecyclerView mRecyleView;

    private int mCurPage;

    public static TimeKillClassifyFragment getInstance(int page) {
        TimeKillClassifyFragment fragment = new TimeKillClassifyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page",page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_timekillclassify;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mRecyleView.setLayoutManager(new GridLayoutManager(_mActivity,4));
        mCurPage = getArguments().getInt("page");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

//    private List<TimeKillClassifBean.DataBean.CategorysBean> getListBean(List<TimeKillClassifBean.DataBean.CategorysBean> dataBean){
//        int left,right;
//
//        List<TimeKillClassifBean.DataBean.CategorysBean> beanList = dataBean.subList(0,7);
//    }

    @Override
    protected void initData() {

    }
}
