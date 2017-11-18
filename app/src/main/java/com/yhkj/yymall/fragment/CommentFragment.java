package com.yhkj.yymall.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.CommonEvaAdapter;
import com.yhkj.yymall.bean.CommonEntiyBean;
import com.yhkj.yymall.bean.ShopEvaTagBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.ninegrid.NineGridView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/20.
 */

public class CommentFragment extends BaseFragment implements CommonEvaAdapter.OnTagUpdateLisiten {

    public final static String TAG = CommentFragment.class.getName();

    @Bind(R.id.fc_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.fc_smartlayout)
    SmartRefreshLayout mRefreshLayout;

    public interface OnFinishListen {
        void onFinish();
    }

    public static CommentFragment getInstance(int goodsid,int select,Integer impId) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id",goodsid);
        bundle.putInt("select",select);
        if (impId != null)
            bundle.putInt("imp",impId);
        fragment.setArguments(bundle);
        return fragment;
    }
    private OnFinishListen onFinishListen;

    public CommentFragment setOnFinishListen(OnFinishListen onFinishListen) {
        this.onFinishListen = onFinishListen;
        return this;
    }

    @Override
    public boolean onBackPressedSupport() {
        if (onFinishListen!=null)
            onFinishListen.onFinish();
        pop();
        return true;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_comment;
    }

    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_color)//
                    .error(R.drawable.ic_default_color)//
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    @Override
    public void onUpdate(String imp,int select) {
        if (Integer.parseInt(imp) < 0){
            switch (Integer.parseInt(imp)){
                case -1:
                    mPoint = null;
                    mIsImg =null;
                    break;
                case -2:
                    mPoint = "5";
                    mIsImg =null;
                    break;
                case -3:
                    mPoint = "3";
                    mIsImg =null;
                    break;
                case -4:
                    mPoint = "1";
                    mIsImg =null;
                    break;
                case -5:
                    mPoint = null;
                    mIsImg ="1";
                    break;
            }
            mImpressionId = null;
        }else{
            mImpressionId = Integer.parseInt(imp);
        }

        mCurPage = 1;
        mSelect = String.valueOf(select);
        mRefreshLayout.setLoadmoreFinished(false);
        refreshCurStateList(null,null);
    }

    /**
     * 全部以及有图等等状态 false;
     * 印象标签状态 true;
     */
    private boolean mCurType;

    private void comfimStatus(int tag){
        if (tag < 0) {
            mCurType = false;
        }else {
            mCurType = true;
        }
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mGoodsId = String.valueOf(getArguments().get("id"));
        mSelect = String.valueOf(getArguments().get("select"));
        mImpressionId = getArguments().get("imp") == null ? null : (Integer)getArguments().get("imp");
        NineGridView.setImageLoader(new GlideImageLoader());
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setLoadmoreFinished(true);
        mRefreshLayout.setEnableOverScrollBounce(false);
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshData(refreshlayout);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadmoreData(refreshlayout);
            }
        });
    }

    private void refreshData(RefreshLayout refreshlayout) {
        mCurPage = 1;
        mRefreshLayout.setLoadmoreFinished(false);
        refreshCurStateList(true,refreshlayout);
    }

    private void loadmoreData(RefreshLayout refreshlayout) {
        mCurPage++;
        refreshCurStateList(false,refreshlayout);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    private int mCurPage = 1;
    private String mGoodsId;
    private String mSelect;
    private Integer mImpressionId;
    private String mPoint;//5好评 3中评 1差评
    private String mIsImg;//1-有图

    //获取全部及其他下的评论列表
    private void doAllCommonList(final Boolean refreshOrloadmore,final RefreshLayout refreshlayout){
        YYMallApi.getShopEvaList(_mActivity, mGoodsId, String.valueOf(mCurPage), mPoint,mIsImg, new YYMallApi.ApiResult<CommonEntiyBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
                if (refreshlayout == null)return;
                if (refreshOrloadmore)
                    refreshlayout.finishRefresh();
                else {
                    refreshlayout.finishLoadmore();
                    mCurPage--;
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CommonEntiyBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                CommonEvaAdapter adapter = (CommonEvaAdapter)mRecycleView.getAdapter();

                if (refreshlayout!=null){
                    if (refreshOrloadmore)
                        refreshlayout.finishRefresh();
                    else{
                        refreshlayout.finishLoadmore();
                        if (dataBean ==null || dataBean.getComments() == null || dataBean.getComments().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                            mCurPage--;
                        }
                        if (adapter != null)
                            adapter.addEvaListData(dataBean.getComments());
                        return;
                    }
                }

                if (adapter != null) {
                    adapter.setEvaListData(dataBean.getComments(),mSelect, false);
                }else{
                    mRecycleView.setAdapter(new CommonEvaAdapter(_mActivity,dataBean.getComments()).setOnTagUpdateLisiten(CommentFragment.this));
                }
            }
        });
    }

    //获取印象标签下的评论列表
    private void doEvaList(final Boolean refreshOrloadmore,final RefreshLayout refreshlayout){
        YYMallApi.getShopEvaClassify(_mActivity, mGoodsId, mCurPage, mImpressionId, new YYMallApi.ApiResult<CommonEntiyBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
                if (refreshlayout == null)return;
                if (refreshOrloadmore)
                    refreshlayout.finishRefresh();
                else {
                    refreshlayout.finishLoadmore();
                    mCurPage--;
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CommonEntiyBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                CommonEvaAdapter adapter = (CommonEvaAdapter)mRecycleView.getAdapter();

                if (refreshlayout!=null){
                    if (refreshOrloadmore)
                        refreshlayout.finishRefresh();
                    else{
                        refreshlayout.finishLoadmore();
                        if (dataBean ==null || dataBean.getComments() == null || dataBean.getComments().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                            mCurPage--;
                        }
                        if (adapter != null)
                            adapter.addEvaListData(dataBean.getComments());
                        return;
                    }
                }

                if (adapter != null) {
                    adapter.setEvaListData(dataBean.getComments(),mSelect,false);
                }else{
                    mRecycleView.setAdapter(new CommonEvaAdapter(_mActivity,dataBean.getComments()).setOnTagUpdateLisiten(CommentFragment.this));
                }
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getCommonTagData();
    }

    /**
     * 刷新当前状态列表
     */
    private void refreshCurStateList(final Boolean refreshOrloadmore,final RefreshLayout refreshlayout){
        if (mImpressionId!=null && mImpressionId > 0) {
            //印象标签刷新
            doEvaList(refreshOrloadmore, refreshlayout);
        }else {
            doAllCommonList(refreshOrloadmore, refreshlayout);
        }
    }

    //获取商品印象标签
    private void getCommonTagData(){
        YYMallApi.getShopEvaTag(_mActivity,mGoodsId,new YYMallApi.ApiResult<ShopEvaTagBean.DataBean>(_mActivity){
            @Override
            public void onNext(ShopEvaTagBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mRecycleView.setAdapter(new CommonEvaAdapter(_mActivity,dataBean.getImpressions(),mSelect).setOnTagUpdateLisiten(CommentFragment.this));
//                ((CommonEvaAdapter)mRecycleView.getAdapter()).setEvaTagData(dataBean.getImpressions());
//                if (TextUtils.isEmpty(mImpressionId))
//                    getPureAllData(false,null);
//                else
//                    updateShopEvaList(false,null);
//                refreshCurStateList(null,null);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onStart() {

            }
        });
    }


//    private void getPureAllData(final Boolean refreshOrloadmore,final RefreshLayout refreshlayout){
//        YYMallApi.getShopEvaList(_mActivity,mGoodsId,String.valueOf(mCurPage),null,null,new ApiCallback<CommonEntiyBean.DataBean>(){
//            @Override
//            public void onNext(CommonEntiyBean.DataBean dataBean) {
//                setNetWorkErrShow(GONE);
//                if (refreshlayout!=null) {
//                    if (refreshOrloadmore)
//                        refreshlayout.finishRefresh();
//                    else {
//                        refreshlayout.finishLoadmore();
//                        if (dataBean == null || dataBean.getComments()==null || dataBean.getComments().size() == 0) {
//                            refreshlayout.setLoadmoreFinished(true);
//                            mCurPage--;
//                        }else{
//                            CommonEvaAdapter adapter = (CommonEvaAdapter)mRecycleView.getAdapter();
//                            adapter.addEvaListData(dataBean.getComments());
//                        }
//                        return;
//                    }
//                }
//                ((CommonEvaAdapter)mRecycleView.getAdapter()).setEvaListData(dataBean.getComments());
////                mRecycleView.setAdapter(new CommonEvaAdapter(_mActivity,dataBean.getComments()).setOnTagUpdateLisiten(CommentFragment.this));
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(ApiException e) {
//                setNetWorkErrShow(VISIBLE);
//                ViseLog.e(e);
//                showToast(e.getMessage());
//                if (refreshlayout!=null) {
//                    if (refreshOrloadmore)
//                        refreshlayout.finishRefresh();
//                    else {
//                        refreshlayout.finishLoadmore();
//                        mCurPage--;
//                    }
//                }
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//        });
//    }

    @Override
    protected void initData() {
        getCommonTagData();
    }
}
