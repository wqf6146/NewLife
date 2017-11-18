package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.GrouponListActivity;
import com.yhkj.yymall.activity.MineGroupDetailActivity;
import com.yhkj.yymall.activity.OrderDetailActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GroupListBean;
import com.yhkj.yymall.bean.GrouponListBeans;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;

import com.yhkj.yymall.view.YiYaHeaderView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/***
 *  拼团Fragment
 */
/**
 * Created by Administrator on 2017/7/3.
 */

public class GroupFragment extends BaseFragment {

    private int mType;

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;


//    @Bind(R.id.vrr_rl_nodata)
//    RelativeLayout mRlNOData;
//
//    @Bind(R.id.vrr_img_nodata)
//    ImageView mImgNoData;
//
//    @Bind(R.id.vrr_tv_tip)
//    TextView mTvTip;
//
//    @Bind(R.id.vrr_tv_btn)
//    TextView mTvTurnBtn;


    public static GroupFragment getInstance(int type) {
        GroupFragment fragment = new GroupFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_FRAGMENT_GROUP.TYPE,type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ViseLog.e(mType);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.view_refresh_recycleview;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mType = getArguments().getInt(Constant.TYPE_FRAGMENT_GROUP.TYPE);
        buildUrl();
        initRefreshLayout();
    }

    private String mUrl;
    private String mStatusStr;
    private void buildUrl(){
        switch (mType) {
            case 0:
                mUrl = ApiService.ALLGROUPLIST;
                break;
            case 1:
                mUrl = ApiService.WAITGROUPLIST;
                mStatusStr = "等待成团";
                break;
            case 2:
                mUrl = ApiService.SUCGROUPLIST;
                mStatusStr = "拼团成功";
                break;
            case 3:
                mUrl = ApiService.FAILDGROUPLIST;
                mStatusStr = "拼团失败";
                break;
        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData(){
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getData(null,null);
    }

    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    private int mCurPage = 1;
    private CommonAdapter mAdapter;

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    private void getData(final Boolean pullorloadmore, final RefreshLayout refreshlayout) {
        YYMallApi.getGroupList(_mActivity,mUrl, mCurPage, null,false,new YYMallApi.ApiResult<GroupListBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());
                if (refreshlayout!=null){
                    if (pullorloadmore){
                        refreshlayout.finishRefresh();
                    }else{
                        refreshlayout.finishLoadmore();
                        mCurPage--;
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GroupListBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout!=null){
                    if (pullorloadmore){
                        refreshlayout.finishRefresh();
                        mAdapter.setDatas(dataBean.getList());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getList() == null || dataBean.getList().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                        }
                        mAdapter.addDatas(dataBean.getList());
                        mAdapter.notifyDataSetChanged();
                    }
                    return;
                }

                if (dataBean.getList()==null || dataBean.getList().size()==0){
//                    mRlNOData.setVisibility(View.VISIBLE);
//                    mTvTip.setText("暂无订单");
//                    mTvTurnBtn.setVisibility(GONE);
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无订单");
                    return;
                }

                mAdapter = new CommonAdapter<GroupListBean.DataBean.ListBean>(_mActivity,R.layout.item_group_entity,dataBean.getList()) {
                    @Override
                    protected void convert(final ViewHolder holder, final GroupListBean.DataBean.ListBean bean, int position) {
                        //0全部 1待拼团 2拼团成功 3 拼团失败
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(_mActivity, MineGroupDetailActivity.class);
                                intent.putExtra("groupid",bean.getId());
                                intent.putExtra("id", bean.getOrderId());
                                intent.putExtra("status",((TextView)holder.getView(R.id.ioe_tv_status)).getText());
                                intent.putExtra("img",bean.getImg());
                                intent.putExtra("goodsid",bean.getGoodsId());
                                startActivity(intent);
                            }
                        });
                        Glide.with(_mActivity).load(bean.getImg()).into((ImageView)holder.getView(R.id.ioe_img_shopimg));
                        holder.setText(R.id.ioc_tv_shopname,bean.getName());
                        holder.setText(R.id.ioc_tv_shopprice,"¥" + bean.getPrice());
                        holder.setText(R.id.ioc_tv_groupnumb,bean.getPersonNum() + "人团");
                        if (bean.getType() == null) {
                            //其他
                            holder.setText(R.id.ioe_tv_status, mStatusStr);
                            if (mType == 1){
                                //等待成团显示时间
                                holder.setText(R.id.ioc_tv_tab1,"等待" + getFormatHMStext(bean.getTime()));
                                holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }else{
                                holder.setVisible(R.id.ioc_tv_tab1,false);
                            }
                        }else {
                            //全部
                            if (mType == 0){
                                holder.setText(R.id.ioe_tv_status, bean.getType() == 0 ? "等待成团" : bean.getType() == 1 ? "拼团成功" : "拼团失败");
                            }else{
                                holder.setText(R.id.ioe_tv_status, mStatusStr);
                            }

                            if (bean.getType() == 0){
                                holder.setText(R.id.ioc_tv_tab1,"等待" + getFormatHMStext(bean.getTime()));
                                holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                            }else{
                                holder.setVisible(R.id.ioc_tv_tab1,false);
                            }
                        }
                        holder.setText(R.id.ioc_tv_tab2,"拼团详情");
                        holder.setText(R.id.ioc_tv_tab3,"查看订单");
                        holder.setOnClickListener(R.id.ioc_tv_tab3, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(_mActivity, OrderDetailActivity.class);
                                intent.putExtra("id", bean.getOrderId());
//                                intent.putExtra("status",((TextView)holder.getView(R.id.ioe_tv_status)).getText());
                                startActivity(intent);
                            }
                        });
                    }
                };
                mRecycleView.setAdapter(mAdapter);
            }
        });
    }

    private String getFormatHMStext(@NonNull int mills){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timearr[] = formatter.format(mills * 1000).split(":");

        if (timearr.length >=2 ){
            return timearr[0]+"小时"+timearr[1]+"分钟";
        }
        return "";
    }
}
