package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.bean.CollectClassifyBean;
import com.yhkj.yymall.bean.CollectListBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.ListPopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by Administrator on 2017/7/12.
 */

public class CollectActivity extends BaseToolBarActivity implements ListPopView.classifyItemClickEvent {

    @Bind(R.id.ac_tv_cancelcollect)
    TextView mTvCancelCollect;

    @Bind(R.id.ac_img_allselect)
    ImageView mImgAllSelect;

    @Bind(R.id.ac_img_classify)
    ImageView mImgClassify;

    @Bind(R.id.ac_tv_allshop)
    TextView mTvAllShop;

    @Bind(R.id.ac_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.ac_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.ac_ll_bottombar)
    LinearLayout mLlBottomBar;

    @Bind(R.id.ac_rl_nodata)
    RelativeLayout mRlNodata;

    @Bind(R.id.ac_tv_totaymall)
    TextView mTvTotayMall;

    @Bind(R.id.ac_ll_topbar)
    LinearLayout mLlTopBar;

    private RotateAnimation mOpenAinm,mExitAnim;

    private CollectClassifyBean.DataBean mClassifyBean;
    private CommonAdapter mShopListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    private int mStatus = 0; // 0-正常 1-编辑
    private int mSelectClassifyPos = 0;
    private void initAnim(){
        if (mOpenAinm == null){
            mOpenAinm = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mOpenAinm.setDuration(450);
            mOpenAinm.setInterpolator(new AccelerateDecelerateInterpolator());
            mOpenAinm.setFillAfter(true);
            mOpenAinm.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mImgClassify.setColorFilter(mContext.getResources().getColor(R.color.theme_bule));
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        if (mExitAnim == null){
            mExitAnim = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            mExitAnim.setDuration(450);
            mExitAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            mExitAnim.setFillAfter(true);
            mExitAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mImgClassify.setColorFilter(mContext.getResources().getColor(R.color.transparency));
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private CollectClassifyBean.DataBean.CateListBean mCateBean;
    @Override
    public void onClassifyItemClick(int pos,CollectClassifyBean.DataBean.CateListBean bean) {
        mSelectClassifyPos = pos;
        mCateBean = bean;
        if (pos == 0) mCateBean = null;
        mRefreshLayout.setLoadmoreFinished(false);
        mShopListAdapter.setSelectStatus(false);
        mImgAllSelect.setImageResource(R.mipmap.ic_nor_graycicle);
        mImgAllSelect.setTag(false);
        getData(mCurPage = 1,null);
    }


    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvTotayMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext,MainActivity.class);
//                intent.putExtra(Constant.MAINBOTTOMBAR.INDEX,0);
//                startActivity(intent);
                YYApp.getInstance().setIndexShow(0);
                startActivity(MainActivity.class);
                AppManager.getInstance().finishExceptActivity(MainActivity.class);
            }
        });
        mImgAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean bSelect = (Boolean)v.getTag();
                if (bSelect != null && bSelect){
                    mImgAllSelect.setImageResource(R.mipmap.ic_nor_graycicle);
                    v.setTag(false);
                    mSelectPos.clear();
                    mShopListAdapter.setSelectStatus(false);
                    mShopListAdapter.notifyDataSetChanged();
                }else{
                    //全选
                    mImgAllSelect.setImageResource(R.mipmap.ic_nor_bluenike);
                    v.setTag(true);
                    mSelectPos.clear();
                    for (int i=0; i<mShopListAdapter.getDatas().size(); i++){
                        CollectListBean.DataBean.ListBean bean = (CollectListBean.DataBean.ListBean)mShopListAdapter.getDatas().get(i);
                        mSelectPos.put(i,String.valueOf(bean.getProduct_id()));
                    }
                    mShopListAdapter.setSelectStatus(true);
                    mShopListAdapter.notifyDataSetChanged();
                }
            }
        });
        mTvCancelCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消收藏
                if ((mImgAllSelect.getTag() ==null || mImgAllSelect.getTag().equals(false) ) &&
                        mSelectPos.size() > 0 && mSelectPos.size() != mShopListAdapter.getDatas().size()){
                    delNumbShop();
                }else{
                    delAllShop();
                }
            }
        });
        mTvAllShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClassifyPopView();
            }
        });
        mImgClassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClassifyPopView();
            }
        });
        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus();
            }
        });
    }

    private void delNumbShop(){
        if (mSelectPos.size() == 0){
            showToast("请选择取消收藏的商品");
            return;
        }
        String[] arr = new String[mSelectPos.size()];
        for (int i=0;i<mSelectPos.size();i++){
            arr[i] = mSelectPos.valueAt(i);
        }
        YYMallApi.deleteCollectShpp(CollectActivity.this, arr, new YYMallApi.ApiResult<CommonBean>(CollectActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CommonBean commonBean) {
                if(commonBean.getCode()==0){
                    mSelectPos.clear();
                    showToast("删除成功");
                    mRefreshLayout.setLoadmoreFinished(false);
                    mCurPage = 1;
                    getData(1,null);
                }else{
                    showToast(commonBean.getMsg());
                }
            }
        });
    }
    private void delAllShop(){
        YYMallApi.delAllCollect(this, mCateBean == null ? null : mCateBean.getId(), new ApiCallback<CommonBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CommonBean commonBean) {
                if(commonBean.getCode()==0){
                    mSelectClassifyPos = -1;
                    mSelectPos.clear();
                    showToast("删除成功");
                    mRefreshLayout.setLoadmoreFinished(false);
                    mCurPage = 1;
                    getData(1,null);
                }else{
                    showToast(commonBean.getMsg());
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }
        });
    }

    private void changeStatus(){
        if (mShopListAdapter !=null && mStatus == 0){
            mStatus = 1;
            setTvTitleText("编辑");
            setImgBackVisiable(View.INVISIBLE);
            setTvRightText("完成");
            setImgRightVisiable(View.INVISIBLE);
            mImgAllSelect.setVisibility(View.VISIBLE);
            mLlBottomBar.setVisibility(View.VISIBLE);
            mShopListAdapter.setEditMode(true);
        }else if (mShopListAdapter !=null){
            mStatus = 0;
            setTvTitleText("我的收藏");
            setImgBackVisiable(View.VISIBLE);
            setTvRightText("编辑");
            setImgRightVisiable(View.VISIBLE);
            mImgAllSelect.setVisibility(View.GONE);
            mLlBottomBar.setVisibility(View.GONE);
            mShopListAdapter.setEditMode(false);
        }
    }

    private void initRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(mCurPage = 1,refreshlayout);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getData(++mCurPage,refreshlayout);
            }
        });
    }

    private void openClassifyPopView(){
//        if (mClassifyBean == null){
            getClassifyList();
//        }else{
//            initAnim();
//            ListPopView listPopView = new ListPopView(CollectActivity.this,mClassifyBean,mSelectClassifyPos);
//            listPopView.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                }
//                @Override
//                public boolean onBeforeDismiss() {
//                    mImgClassify.startAnimation(mExitAnim);
//                    return super.onBeforeDismiss();
//                }
//            });
//            mImgClassify.startAnimation(mOpenAinm);
//            listPopView.setOnClassifyItemClick(CollectActivity.this);
//            listPopView.showPopupWindow(mImgClassify);
//        }
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("我的收藏");
        initRefreshLayout();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new CollectAdapter(this);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null,null);
        getTotalCount();
    }

    private void getTotalCount() {
        YYMallApi.getClassifyCollectList(this, new YYMallApi.ApiResult<CollectClassifyBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CollectClassifyBean.DataBean dataBean) {
                if (dataBean == null || dataBean.getCate_list() == null || dataBean.getCate_list().size() == 0)
                    return;
                for (int i=0; i<dataBean.getCate_list().size(); i++){
                    mTotalCount += dataBean.getCate_list().get(i).getCate_nub();
                }
                //String.format(getString(R.string.allshop),String.valueOf(mTotalCount))
                mTvAllShop.setText("全部商品");
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
        getTotalCount();
    }

    private int mCurPage = 1;
    private CollectListBean.DataBean mListBean;
    private void getData(final Integer page, final RefreshLayout refreshLayout) {
        YYMallApi.getCollectList(this,page,mCateBean == null ? null : mCateBean.getCate_name(), new YYMallApi.ApiResult<CollectListBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
                if (refreshLayout!=null) {
                    if (page == 1) {
                        refreshLayout.finishRefresh();
                    } else {
                        refreshLayout.finishLoadmore();
                        mCurPage--;
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CollectListBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                mListBean = dataBean;
                if (refreshLayout!=null) {
                    if (page == 1) {
                        // 下拉刷新
                        refreshLayout.finishRefresh();
                        refreshLayout.setLoadmoreFinished(false);
                    } else {
                        // 上拉加载
                        refreshLayout.finishLoadmore();
                        if (dataBean == null || dataBean.getList() == null || dataBean.getList().size() == 0) {
                            refreshLayout.setLoadmoreFinished(true);
                            mCurPage--;
                            return;
                        }
                    }
                }else{
                    //删除刷新 、首次刷新、分类刷新
                    if (dataBean == null || dataBean.getList() == null || dataBean.getList().size() == 0){
                        changeStatus();
                        mRlNodata.setVisibility(View.VISIBLE);
                        setTvRightVisiable(View.INVISIBLE);
                        if (page == null && refreshLayout == null){
                            mLlTopBar.setVisibility(View.GONE);
                        }
                        return;
                    }else{
                        setTvRightText("编辑");
                        mRlNodata.setVisibility(View.GONE);
                    }
                }
                if (mShopListAdapter == null){
                    mShopListAdapter = new CommonAdapter<CollectListBean.DataBean.ListBean>(CollectActivity.this,R.layout.item_order_entity,dataBean.getList()) {
                        @Override
                        protected void convert(final ViewHolder holder, final CollectListBean.DataBean.ListBean bean, final int position) {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (bean.getType() == 2) {
                                        //租赁商品
                                        Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                        intent.putExtra("id", String.valueOf(bean.getProduct_id()));
                                        startActivity(intent);
                                    }else if (bean.getType() == 0){
                                        //普通商品
                                        if (bean.getPanicBuyItemId() !=0 ){
                                            //限时抢购
                                            Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                            intent.putExtra("id", String.valueOf(bean.getPanicBuyItemId()));
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                            intent.putExtra("goodsId", String.valueOf(bean.getProduct_id()));
                                            startActivity(intent);
                                        }
                                    }else if (bean.getType() == 1){
                                        //拼团商品
                                        Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                        intent.putExtra("goodsId", String.valueOf(bean.getProduct_id()));
                                        startActivity(intent);
                                    }else if (bean.getType() == 3){
                                        //折扣
                                        Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                        intent.putExtra("goodsId", bean.getProduct_id() + "");
                                        mContext.startActivity(intent);
                                    }else if (bean.getType() == 4){
                                        //积分
                                        Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                        intent.putExtra("id", bean.getProduct_id() + "");
                                        mContext.startActivity(intent);
                                    }else if (bean.getType() == 6){
                                        //日常活动
                                        Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                        intent.putExtra("goodsId", bean.getProduct_id() + "");
                                        mContext.startActivity(intent);
                                    }
                                }
                            });
                            if (mEditMode)
                                holder.setVisible(R.id.ioe_img_select,true);
                            else
                                holder.setVisible(R.id.ioe_img_select,false);
                            if (mSelectPos.get(position) != null){
                                holder.setImageResource(R.id.ioe_img_select,R.mipmap.ic_nor_bluenike);
                                holder.setTag(R.id.ioe_img_select,R.id.item_status,1);
                            }else{
                                holder.setImageResource(R.id.ioe_img_select,R.mipmap.ic_nor_graycicle);
                                holder.setTag(R.id.ioe_img_select,R.id.item_status,0);
                            }
                            holder.setTag(R.id.ioe_img_select,String.valueOf(bean.getProduct_id()));

                            Glide.with(CollectActivity.this).load(bean.getImg()).into((ImageView)holder.getView(R.id.ioe_img_shopimg));
                            holder.setText(R.id.ioc_tv_shopname,bean.getName());
                            holder.getView(R.id.ioc_tv_shopdesc).setVisibility(View.INVISIBLE);
                            holder.setVisible(R.id.ioe_tv_count,false);
                            holder.setVisible(R.id.ioe_tv_addcar,false);
                            holder.setVisible(R.id.is_vert_img_tagshop,false);
                            if (bean.getType() == 2) {
                                //租赁商品
                                holder.setText(R.id.ioc_tv_shopprice, "¥" + String.valueOf(bean.getSell_price()));

                                ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                layoutParams.removeRule(ALIGN_PARENT_LEFT);
                                layoutParams.addRule(ALIGN_PARENT_RIGHT);
                                tagShop.setImageResource(R.mipmap.ic_nor_tagfree);

                                holder.setVisible(R.id.is_vert_img_tagshop,true);
                            }else if (bean.getType() == 1){
                                //拼团商品
                                holder.setText(R.id.ioc_tv_shopprice, "¥" + String.valueOf(bean.getSell_price()));
                                ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                layoutParams.addRule(ALIGN_PARENT_LEFT);
                                tagShop.setImageResource(R.mipmap.ic_nor_taggroup);

                                holder.setVisible(R.id.is_vert_img_tagshop,true);
                            }else if (bean.getType() == 3){
                                //折扣
                                holder.setText(R.id.ioc_tv_shopprice, "¥" + String.valueOf(bean.getSell_price()));
                                ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                layoutParams.addRule(ALIGN_PARENT_LEFT);
                                tagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);
                                holder.setVisible(R.id.is_vert_img_tagshop,true);
                            }else if (bean.getType() == 4){
                                //积分
                                holder.setText(R.id.ioc_tv_shopprice, String.valueOf(bean.getSell_price()) + "积分");
                                ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                layoutParams.addRule(ALIGN_PARENT_LEFT);
                                tagShop.setImageResource(R.mipmap.ic_nor_tagintegral);


                                holder.setVisible(R.id.is_vert_img_tagshop,true);
                            }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                                //限时抢购
                                ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                layoutParams.addRule(ALIGN_PARENT_LEFT);
                                tagShop.setImageResource(R.mipmap.ic_nor_tagtimekill);

                                holder.setVisible(R.id.is_vert_img_tagshop,true);
                                holder.setText(R.id.ioc_tv_shopprice, "¥" + String.valueOf(bean.getSell_price()));
                            }else{
                                holder.setText(R.id.ioc_tv_shopprice, "¥" + String.valueOf(bean.getSell_price()));
                            }
                            holder.setOnClickListener(R.id.ioe_img_select, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 1-选中 0-没选中
                                    String productId = (String) v.getTag();
                                    Object status = v.getTag(R.id.item_status);
                                    if (status != null && (int)status == 0){
                                        v.setTag(R.id.item_status,1);
                                        holder.setImageResource(R.id.ioe_img_select,R.mipmap.ic_nor_bluenike);
                                        mSelectPos.put(position,productId);
                                        if (mCateBean != null && mSelectPos.size() == mCateBean.getCate_nub()){
                                            mImgAllSelect.setImageResource(R.mipmap.ic_nor_bluenike);
                                        }else{
                                            if (mTotalCount == mSelectPos.size()) {
                                                mImgAllSelect.setImageResource(R.mipmap.ic_nor_bluenike);
                                            }
                                        }
                                    }else{
                                        v.setTag(R.id.item_status,0);
//                                        for(int i=0;i<mSelectPos.size();i++){
//                                            if(mSelectPos.get(i).equals(productId)){
//                                                mSelectPos.remove(i);
//                                            }
//                                        }
                                        mSelectPos.remove(position);
                                        holder.setImageResource(R.id.ioe_img_select,R.mipmap.ic_nor_graycicle);
                                        mImgAllSelect.setImageResource(R.mipmap.ic_nor_graycicle);
                                    }
                                }

                            });
                        }
                    };
                    mRecycleView.setAdapter(mShopListAdapter);
                }else{
                    if (page > 1)
                        mShopListAdapter.addDatas(dataBean.getList());
                    else
                        mShopListAdapter.setDatas(dataBean.getList());
                    mShopListAdapter.notifyDataSetChanged();
                }
                if (mCateBean == null) {
                    mTvAllShop.setText("全部商品");
                }else {
                    mTvAllShop.setText(mCateBean.getCate_name());
                }
            }
        });
    }

    private SparseArray<String> mSelectPos = new SparseArray<>();
    private int mTotalCount = 0;
    private void getClassifyList(){
        YYMallApi.getClassifyCollectList(this,  new YYMallApi.ApiResult<CollectClassifyBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CollectClassifyBean.DataBean dataBean) {
                if (dataBean == null || dataBean.getCate_list() == null || dataBean.getCate_list().size() == 0)
                    return;
                mClassifyBean = dataBean;

                mTotalCount = 0;
                for (int i=0; i<dataBean.getCate_list().size(); i++){
                    mTotalCount += dataBean.getCate_list().get(i).getCate_nub();
                }

                CollectClassifyBean.DataBean.CateListBean cateListBean = new CollectClassifyBean.DataBean.CateListBean();
                int count = 0;
                for (int i = 0; i< mClassifyBean.getCate_list().size(); i++){
                    count += mClassifyBean.getCate_list().get(i).getCate_nub();
                }
                cateListBean.setCate_name("全部商品");
                cateListBean.setCate_nub(count);
                mClassifyBean.getCate_list().add(0,cateListBean);

                initAnim();
                ListPopView listPopView = new ListPopView(CollectActivity.this,dataBean,mSelectClassifyPos);
                listPopView.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }
                    @Override
                    public boolean onBeforeDismiss() {
                        mImgClassify.startAnimation(mExitAnim);
                        return super.onBeforeDismiss();
                    }
                });
                mImgClassify.startAnimation(mOpenAinm);
                listPopView.setOnClassifyItemClick(CollectActivity.this);
                listPopView.showPopupWindow(mImgClassify);
            }
        });
    }
}
