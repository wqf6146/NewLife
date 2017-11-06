package com.yhkj.yymall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.PriviledgeBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/11.
 */

public class PrivilegeLevelFragment extends BaseFragment {

    @Bind(R.id.fp_recycleview)
    RecyclerView mRecycleView;

    public static PrivilegeLevelFragment getInstance() {
        PrivilegeLevelFragment fragment = new PrivilegeLevelFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_privilegelevel;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        init();
    }

    private void init(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        getData();
    }

    private void getData() {
        YYMallApi.getPrivilege(_mActivity,new YYMallApi.ApiResult<PriviledgeBean.DataBean>(_mActivity){
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(PriviledgeBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setData(dataBean);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void setData(PriviledgeBean.DataBean dataBean) {
        mRecycleView.setAdapter(new CommonAdapter<PriviledgeBean.DataBean.ListsBean>(_mActivity,R.layout.item_priviledge,dataBean.getLists()) {
            @Override
            protected void convert(ViewHolder holder, PriviledgeBean.DataBean.ListsBean bean, int position) {
                holder.setText(R.id.ip_tv_title,bean.getTitle());
                holder.setText(R.id.ip_tv_content,bean.getDes());
                holder.setText(R.id.ip_tv_tag,bean.getTag());
            }
        });
    }
}
