package com.yhkj.yymall.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.ScanActivity;
import com.yhkj.yymall.activity.SearchActivity;
import com.yhkj.yymall.activity.ShopListActivity;
import com.yhkj.yymall.activity.WebActivity;
import com.yhkj.yymall.adapter.LeaseAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.LeaseClassifyBean;
import com.yhkj.yymall.bean.LeaseHotBean;
import com.yhkj.yymall.bean.ShopSelectBean;
import com.yhkj.yymall.bean.UnReadBean;
import com.yhkj.yymall.event.LeaseSortEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.BottomBarView.BottomBar;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.behavior.LeaseContentBehavior;
import com.yhkj.yymall.view.behavior.LeaseHeaderPagerBehavior;
import com.yhkj.yymall.view.popwindows.ShopFiddlePopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NewLeaseFragment extends BaseFragment implements LeaseHeaderPagerBehavior.OnPagerStateListener {

    @Bind(R.id.fy_toolbar)
    LinearLayout mLlToolbar;

    @Bind(R.id.vt_btn_left)
    ImageView mImgLeft;

    @Bind(R.id.vt_tv_search)
    TextView mTvSearch;

    @Bind(R.id.fl_view_status)
    View mViewStatus;

    @Bind(R.id.vt_btn_right)
    ImageView mImgRight;

    @Bind(R.id.fn_img_back)
    ImageView mImgBackTop;

    @Bind(R.id.id_lease_header)
    LinearLayout mFlHeader;

    @Bind(R.id.fn_recyclerView_hot)
    RecyclerView mRvHotLease;

    @Bind(R.id.fn_recycleview_content)
    RecyclerView mRvLeaseContent;

//    @Bind(R.id.ilc_ll_1)
//    LinearLayout mLlIntro1;
//
//    @Bind(R.id.ilc_ll_2)
//    LinearLayout mLlIntro2;
//
//    @Bind(R.id.ilc_ll_3)
//    LinearLayout mLlIntro3;
//
//    @Bind(R.id.ilc_ll_4)
//    LinearLayout mLlIntro4;

    @Bind(R.id.as_tv_synthesize)
    TextView mTvSynthesize;

    @Bind(R.id.as_tv_sales)
    TextView mTvSales;

    @Bind(R.id.as_ll_price)
    LinearLayout mLlPrice;

    @Bind(R.id.as_tv_price)
    TextView mTvPrice;

    @Bind(R.id.as_arrow_up)
    ImageView mImgArrowUp;

    @Bind(R.id.as_arrow_down)
    ImageView mImgArrowDown;

    @Bind(R.id.fn_flowlistview)
    NestFullListView mAttrListView;

    @Bind(R.id.fn_refreshview)
    SmartRefreshLayout mRefreshLayout;

//    @Bind(R.id.fn_ll_toolbar)
//    LinearLayout mLlToolBar;

    @Bind(R.id.fn_ll_content)
    LinearLayout mLlContent;

    private String mOrder = null,mBy = null,mPage = "1",mLimit = "10",mBrand = null,mAttr = null;
    private Drawable[] mDrawable;
    private int mToolBarHeight = 0;
    LinearLayout.LayoutParams mToolBarParam;

    public static NewLeaseFragment getInstance() {
        NewLeaseFragment fragment = new NewLeaseFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_newlease;
    }

    private LeaseHeaderPagerBehavior mHeaderPagerBehavior;
    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        mDrawable = mTvSearch.getCompoundDrawables();
        initRefreshLayout();
        mLlToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mToolBarHeight == 0)
                    mToolBarHeight = mLlToolbar.getHeight();
                mToolBarParam = (LinearLayout.LayoutParams)mLlToolbar.getLayoutParams();
            }
        });

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                mFlHeader.getLayoutParams();
        mHeaderPagerBehavior = (LeaseHeaderPagerBehavior) layoutParams.getBehavior();
        mHeaderPagerBehavior.setPagerStateListener(this);
        mFlHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                        mFlHeader.getLayoutParams();
                mHeaderPagerBehavior = (LeaseHeaderPagerBehavior) layoutParams.getBehavior();
                mHeaderPagerBehavior.setHeadViewHeight(mFlHeader.getMeasuredHeight());
                mHeaderPagerBehavior.setPagerStateListener(NewLeaseFragment.this);
                CoordinatorLayout.LayoutParams contentViewLayoutParams = (CoordinatorLayout.LayoutParams)
                        mLlContent.getLayoutParams();
                LeaseContentBehavior leaseContentBehavior = (LeaseContentBehavior) contentViewLayoutParams.getBehavior();
                leaseContentBehavior.setHeadViewHeight(mFlHeader.getMeasuredHeight());
            }
        });
    }

    private void initRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableOverScrollBounce(false);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = "1";
                mRefreshLayout.setLoadmoreFinished(false);
                getHotLeaseInfo(refreshlayout);
                getLeaseAllData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {

                final String page = String.valueOf(Integer.valueOf(mPage) + 1);
                YYMallApi.getLeaseSelectList(_mActivity, mOrder, mBy, page, mLimit, mBrand, mAttr,false, new YYMallApi.ApiResult<ShopSelectBean.DataBean>(_mActivity) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        refreshlayout.finishLoadmore();
                        ViseLog.e(e);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(ShopSelectBean.DataBean dataBean) {
                        if (dataBean.getList() !=null && dataBean.getList().size() > 0){
                            refreshlayout.finishLoadmore();
                            mPage = page;
                            addLeaseSelList(dataBean);
                        }else{
                            refreshlayout.finishLoadmore();
                            refreshlayout.setLoadmoreFinished(true);
                        }
                    }
                });
            }
        });
    }

    private void addLeaseSelList(ShopSelectBean.DataBean dataBean) {
        mRvContentAdapter.addDatas(dataBean.getList());
        mRvContentAdapter.notifyDataSetChanged();
    }

    //设置tab选择ui
    final int mPinkColor = Color.rgb(0,124,209);
    final int mGrayColor = Color.rgb(114,112,112);
    private int mPriceSelectStatus = 2;
    private void initTabColor(int index){
        mTvSynthesize.setTextColor(mGrayColor);
        mTvSales.setTextColor(mGrayColor);
        mTvPrice.setTextColor(mGrayColor);
        mImgArrowDown.setImageResource(R.mipmap.arraw_bottom);
        mImgArrowUp.setImageResource(R.mipmap.arraw_top);
        switch (index){
            case 1:
                mTvSynthesize.setTextColor(mPinkColor);
                mTvSales.setTextColor(mGrayColor);
                mTvPrice.setTextColor(mGrayColor);
                break;
            case 2:
                mTvSynthesize.setTextColor(mGrayColor);
                mTvSales.setTextColor(mPinkColor);
                mTvPrice.setTextColor(mGrayColor);
                break;
            case 3:
                mTvSynthesize.setTextColor(mGrayColor);
                mTvSales.setTextColor(mGrayColor);
                mTvPrice.setTextColor(mPinkColor);
                if (mPriceSelectStatus == 2){
                    mTvPrice.setTag(true);
                    //top
                    mPriceSelectStatus = 1;
                    mImgArrowUp.setImageResource(R.mipmap.ic_nor_bluearrowtop);
                    mImgArrowDown.setImageResource(R.mipmap.arraw_bottom);
                }else{
                    mTvPrice.setTag(false);
                    //bottom
                    mPriceSelectStatus = 2;
                    mImgArrowUp.setImageResource(R.mipmap.arraw_top);
                    mImgArrowDown.setImageResource(R.mipmap.ic_nor_bluearrowbot);
                }
                break;
        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeaderPagerBehavior.openPager();
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchActivity.class, Constant.TOOLBAR_TYPE.SEARCH_EDIT);
            }
        });
        mTvSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDrawable.length < 2 || mDrawable[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > mTvSearch.getWidth() - mDrawable[2].getBounds().width()) {

                    startActivity(ScanActivity.class);
                    return true;
                }
                return false;
            }
        });
        mTvSynthesize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTabColor(1);
                refreshLeaseData(Constant.TYPE_SELECT.OTHER,"all","asc","1","6",null,null,null);
            }
        });
        mTvSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTabColor(2);
                refreshLeaseData(Constant.TYPE_SELECT.OTHER,"sale","desc","1","6",null,null,null);
            }
        });
        mLlPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTabColor(3);
                refreshLeaseData(Constant.TYPE_SELECT.OTHER,"price",mPriceSelectStatus == 2 ? "desc" : "asc","1","6",null,null,null);
            }
        });
        mImgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRvContentAdapter==null)return;
                Object object = mRvLeaseContent.getTag();
                if ( object != null && (boolean)object == true){
                    // true - ver
                    mShopPutStatus = false;
                    mRvLeaseContent.setTag(false);
                    GridLayoutManager gridLayoutManager = (GridLayoutManager)mRvLeaseContent.getLayoutManager();
                    gridLayoutManager.setSpanCount(2);
                    GridLayoutManager rvHotLeaseLayoutManager = (GridLayoutManager)mRvHotLease.getLayoutManager();
                    rvHotLeaseLayoutManager.setSpanCount(2);
                    mImgRight.setImageResource(R.mipmap.ic_nor_whitelistclose);
                }else{
                    mShopPutStatus = true;
                    mRvLeaseContent.setTag(true);
                    GridLayoutManager gridLayoutManager = (GridLayoutManager)mRvLeaseContent.getLayoutManager();
                    gridLayoutManager.setSpanCount(1);
                    GridLayoutManager rvHotLeaseLayoutManager = (GridLayoutManager)mRvHotLease.getLayoutManager();
                    rvHotLeaseLayoutManager.setSpanCount(1);
                    mImgRight.setImageResource(R.mipmap.ic_nor_whitelistopen);
                }

                mRvHotLease.getAdapter().notifyDataSetChanged();
                mRvLeaseContent.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mViewStatus.setVisibility(GONE);
        }
        mLlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_bule));
        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        mImgRight.setImageResource(R.mipmap.ic_nor_whitelistclose);
        initListData();
        getHotLeaseInfo(null);
        getLeaseAllData();
        mImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                    Intent intent = new Intent(mContext, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                }
            }
        });
    }

    private CommonAdapter mHotAdapter;
    private HeaderAndFooterWrapper mWrapperHotAdapter;
    private void getLeaseAllData(){
        getLeaseClassify();
        getLeaseSelectListData(false);
    }

    private Boolean mShopPutStatus = false; //默认并列状态
    private View mItemHead;
    //热门领用数据设置
    private void initHotAdapter(List<LeaseHotBean.DataBean.HotlistBean> hotlist) {
        if (hotlist == null || hotlist.size() == 0)
            mTvHotTag.setVisibility(GONE);
        else{
            mTvHotTag.setVisibility(VISIBLE);
        }
        if (mHotAdapter == null){
            mRvHotLease.setLayoutManager(new GridLayoutManager(_mActivity,2));
            mRvHotLease.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(_mActivity,1)));
            mHotAdapter = new CommonAdapter<LeaseHotBean.DataBean.HotlistBean>(_mActivity,R.layout.item_shop,hotlist) {
                @Override
                protected void convert(ViewHolder holder,final LeaseHotBean.DataBean.HotlistBean bean, int position) {
                    holder.setOnClickListener(R.id.is_fl_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                            intent.putExtra("id",String.valueOf(bean.getId()));
                            mContext.startActivity(intent);
                        }
                    });
                    if (!mShopPutStatus){
                        //此时横条状态
                        holder.setVisible(R.id.is_ll_vert,true);
                        holder.setVisible(R.id.fn_ll_hor,false);
                        // VERT
                        Glide.with(mContext).load(bean.getImg()).into((ImageView)holder.getView(R.id.is_vert_img_shop));
                        ((ImageView)holder.getView(R.id.is_vert_img_tagshop)).setImageResource(R.mipmap.ic_nor_tagfree);
                        holder.setText(R.id.is_vert_shop_groupnumber,String.format(mContext.getString(R.string.getpeoplecount),bean.getSale()));
                        holder.setText(R.id.is_vert_shop_name,bean.getName());
                        holder.setText(R.id.is_vert_shop_price,String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                    }else{
                        //并列状态
                        holder.setVisible(R.id.is_ll_vert,false);
                        holder.setVisible(R.id.fn_ll_hor,true);
                        holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(View.VISIBLE);
                        // HOR
                        holder.setText(R.id.is_hor_groupnumber,String.format(mContext.getString(R.string.getpeoplecount),bean.getSale()));
                        holder.setText(R.id.is_hor_shop_name,bean.getName());
                        holder.setText(R.id.is_hor_shop_price,String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                        Glide.with(mContext).load(bean.getImg()).into((ImageView)holder.getView(R.id.is_img_shop));
                        ((ImageView)holder.getView(R.id.is_img_tagshop)).setImageResource(R.mipmap.ic_nor_tagfree);
                    }
                }
            };
            mWrapperHotAdapter = new HeaderAndFooterWrapper(mHotAdapter);
            mWrapperHotAdapter.addHeaderView(mItemHead);
            mRvHotLease.setAdapter(mWrapperHotAdapter);
        }else{
            mHotAdapter.setDatas(hotlist);
            mWrapperHotAdapter.notifyDataSetChanged();
        }
    }

    private void initLeaseHead(View itemHead) {
        View ilc_ll_1 = itemHead.findViewById(R.id.ilc_ll_1);
        View ilc_ll_2 = itemHead.findViewById(R.id.ilc_ll_2);
        View ilc_ll_3 = itemHead.findViewById(R.id.ilc_ll_3);
        View ilc_ll_4 = itemHead.findViewById(R.id.ilc_ll_4);
        ilc_ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_mActivity,WebActivity.class);
                intent.putExtra("title","租赁须知");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.LEASE_NEED);
                startActivity(intent);
            }
        });
        ilc_ll_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_mActivity,WebActivity.class);
                intent.putExtra("title","租赁协议");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.LEASE_XIYI);
                startActivity(intent);
            }
        });
        ilc_ll_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_mActivity,WebActivity.class);
                intent.putExtra("title","服务保障");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.SERVERBZ);
                startActivity(intent);
            }
        });
        ilc_ll_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_mActivity,WebActivity.class);
                intent.putExtra("title","养护须知");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.YANGHUXUZI);
                startActivity(intent);
            }
        });
    }

    private void getLeaseClassify() {
        if (mAttrSrcName!=null) return;
        YYMallApi.getLeaseClassify(_mActivity, false, new YYMallApi.ApiResult<LeaseClassifyBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(LeaseClassifyBean.DataBean dataBean) {
                setLeaseTabInfo(dataBean);
            }
        });
    }

    private void getHotLeaseInfo(final RefreshLayout xRefreshView){
        YYMallApi.getHotLease(_mActivity, new YYMallApi.ApiResult<LeaseHotBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                mSkipFirst = true;
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(LeaseHotBean.DataBean dataBean) {
                if (xRefreshView!=null) xRefreshView.finishRefresh();
                mSkipFirst = true;
                initHotAdapter(dataBean.getHotlist());
            }
        });
    }

    private boolean mSkipFirst = false;
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        String hotString = YYApp.getInstance().getmHotSearch();
        if (!TextUtils.isEmpty(hotString)){
            YYApp.getInstance().setmHotSearch(hotString);
            mTvSearch.setHint(hotString);
        }
        if (mSkipFirst){
            getHotLeaseInfo(null);
        }
        if (!TextUtils.isEmpty(YYApp.getInstance().getToken())){
            YYMallApi.getUnReadMesTag(_mActivity, new YYMallApi.ApiResult<UnReadBean.DataBean>(_mActivity) {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    showToast(e.getMessage());
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(UnReadBean.DataBean dataBean) {
                    if (dataBean.getStatus()==1){
                        mImgLeft.setImageResource(R.mipmap.ic_nor_unmessage);
                    }else{
                        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
                    }
                }
            });
        }else{
            mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        }
    }

    private String[] mAttrSrcName;
    private LeaseClassifyBean.DataBean.AttrsBean buildBrandBean(List<LeaseClassifyBean.DataBean.BrandBean> brandBeanList){
        LeaseClassifyBean.DataBean.AttrsBean bean = new LeaseClassifyBean.DataBean.AttrsBean();
        bean.setId("-1");
        bean.setName("品牌");
        List<String> stringList = new ArrayList<>();
        for (int i=0;i<brandBeanList.size();i++){
            LeaseClassifyBean.DataBean.BrandBean brandBean = brandBeanList.get(i);
            stringList.add(brandBean.getName() + "," + brandBean.getId());
        }
        bean.setValue(stringList);
        return bean;
    }
    private void setLeaseTabInfo(LeaseClassifyBean.DataBean dataBean) {
        final List<LeaseClassifyBean.DataBean.AttrsBean> leaseAttrsBean = dataBean.getAttrs();
        List<LeaseClassifyBean.DataBean.BrandBean> leaseBrandBean = dataBean.getBrand();
        if (dataBean.getAttrs().size() == 0 && dataBean.getBrand().size() == 0) {
            mAttrListView.setVisibility(GONE);
            return;
        }
        if (leaseAttrsBean!=null && leaseAttrsBean.size() > 0 && !leaseAttrsBean.get(0).getId().equals("-1")){
            for (int i=leaseAttrsBean.size()-1; i>=3; i--){
                leaseAttrsBean.remove(i);
            }

        }

        if (leaseBrandBean!=null && leaseBrandBean.size()>0)
            leaseAttrsBean.add(0,buildBrandBean(leaseBrandBean));
        mAttrListView.setTag(leaseAttrsBean.size());
        //补齐 4个
        if (leaseAttrsBean.size() < 4){
            int bs = 4 - leaseAttrsBean.size();
            for (int i=0 ;i<bs; i++)
                leaseAttrsBean.add(null);
        }
//                holder.mFlowList.setTag();
        if ( mAttrSrcName == null ) mAttrSrcName = new String[leaseAttrsBean.size()];
        mAttrListView.setAdapter(new NestFullListViewAdapter<LeaseClassifyBean.DataBean.AttrsBean>(R.layout.item_flow_item,leaseAttrsBean) {
            @Override
            public void onBind(final int pos, final LeaseClassifyBean.DataBean.AttrsBean bean, NestFullViewHolder nestFullViewHolder) {
                if (mAttrListView.getTag() !=null && pos < (Integer)mAttrListView.getTag() && bean!=null) {
                    if (TextUtils.isEmpty(mAttrSrcName[pos])) mAttrSrcName[pos] = bean.getName();
                    if (bean.getName().equals("全部")) {
                        String name = mAttrSrcName[pos];
                        nestFullViewHolder.setText(R.id.ifi_tv_tag,  CommonUtil.subTextString(name,7));
                    }else {
                        nestFullViewHolder.setText(R.id.ifi_tv_tag, CommonUtil.subTextString(bean.getName(),7));
                    }
                } else{
                    nestFullViewHolder.setVisible(R.id.ifi_ll_bg,false);
                }
            }
        });
        mAttrListView.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
            @Override
            public void onItemClick(NestFullListView parent, final View v, final int position) {
                showTabSelectPopView(v, (TextView) v.findViewById(R.id.ifi_tv_tag),
                        (ImageView) v.findViewById(R.id.ifi_img_arrow),
                        (LeaseClassifyBean.DataBean.AttrsBean) mAttrListView.getAdapter().getDatas().get(position), new ShopFiddlePopView.OnCallBack() {
                            @Override
                            public void onSelectResString(String select) {
                                leaseAttrsBean.get(position).setName(select);
                                mAttrListView.updateUI();
//                                ((TextView)v.findViewById(R.id.ifi_tv_tag)).setText(select);
                            }

                            @Override
                            public void onStartSelect(int type, String order, String by, String page, String limit, String brand, String key, String value) {
//                                BusFactory.getBus().post(new LeaseSortEvent(type,order,by,page,limit,brand,key,value));
                                refreshLeaseData(type,order,by,page,limit,brand,key,value);
                            }
                        });
            }
        });
    }
    HashMap mAttrHashMap = new LinkedHashMap();
    HashMap mAttrValueHashMap = new LinkedHashMap();
    private void refreshLeaseData(int type, String order, String by, String page, String limit, String brand, String key, String value) {
        mRefreshLayout.setLoadmoreFinished(false);
        mPage = "1";
        if (type == Constant.TYPE_SELECT.BRAND){
            //品牌
            if (brand == null || brand.equals("全部"))
                mBrand = null;
            else
                mBrand = brand;
        }else if (type == Constant.TYPE_SELECT.ATTR){
            Gson gson = new Gson();
            if (value.equals("全部")) {
                Iterator iterator = mAttrValueHashMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String tempKey = (String) iterator.next();
                    if (key.equals(tempKey)) {
                        iterator.remove();
                        mAttrValueHashMap.remove(tempKey);
                    }
                }
            }else{
                mAttrValueHashMap.put(key, value);
            }
            mAttrHashMap.put("attrs",mAttrValueHashMap);
            mAttr = gson.toJson(mAttrHashMap);
        }else{
            mOrder = order;
            mBy = by;
            mLimit = String.valueOf(limit);
        }
        YYMallApi.getLeaseSelectList(_mActivity, mOrder, mBy, mPage, mLimit, mBrand, mAttr,true, new YYMallApi.ApiResult<ShopSelectBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
//                if (mToolBarStatus == 1)
//                    getShopToolbarAnimIn();
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSelectBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setLeaseSelList(dataBean);
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getLeaseAllData();
    }

    private void showTabSelectPopView(final View view, final TextView tvTag, final ImageView arrow, final LeaseClassifyBean.DataBean.AttrsBean bean
            , ShopFiddlePopView.OnCallBack onCallBack){
        RotateAnimation openAnim = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        openAnim.setDuration(450);
        openAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        openAnim.setFillAfter(true);
        openAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setBackgroundResource(R.drawable.shop_tag_checked_bg);
                tvTag.setTextColor(mContext.getResources().getColor(R.color.theme_bule));
                arrow.setColorFilter(mContext.getResources().getColor(R.color.theme_bule));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Animation closeAnim = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        closeAnim.setDuration(450);
        closeAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        closeAnim.setFillAfter(true);
        closeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setBackgroundResource(R.drawable.shop_tag_normal_bg);
                tvTag.setTextColor(mContext.getResources().getColor(R.color.grayfont));
                arrow.setColorFilter(mContext.getResources().getColor(R.color.transparency));
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ShopFiddlePopView shopFiddlePopView = new ShopFiddlePopView<LeaseClassifyBean.DataBean.AttrsBean>(_mActivity,bean){
            @Override
            protected void initPopView(RecyclerView recyclerView,final LeaseClassifyBean.DataBean.AttrsBean databean) {
                recyclerView.setAdapter(new com.vise.xsnow.ui.adapter.recycleview.CommonAdapter<String>(mContext,R.layout.item_fillder_tv,databean.getValue()) {
                    @Override
                    protected void convert(final ViewHolder holder, String bean, int position) {
                        if (databean.getId().equals("-1")){
                            if (bean.equals("全部")){
                                holder.setText(R.id.ift_tv,bean);
//                            holder.setTag(R.id.ift_tv,"-1");
                            }else {
                                String[] strings = bean.split(",");
                                if (strings.length >=2){
                                    holder.setText(R.id.ift_tv,strings[0]);
                                    holder.setTag(R.id.ift_tv,strings[1]);
                                }
                            }
                        }else {
                            holder.setText(R.id.ift_tv,bean);
//                        holder.setTag(R.id.ift_tv,mDataBean.getId());
                        }
                        holder.setOnClickListener(R.id.ift_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tag = (TextView)v;
                                String value = (String)tag.getTag();
                                if (databean.getId().equals("-1")){
                                    //品牌
//                                BusFactory.getBus().post(new LeaseSortEvent(Constant.TYPE_SELECT.BRAND,null,null,"1","6",value
//                                        ,null,null));
                                    mOnCallBack.onStartSelect(Constant.TYPE_SELECT.BRAND,null,null,"1","10",value
                                            ,null,null);
                                }else{
                                    //属性
//                                BusFactory.getBus().post(new LeaseSortEvent(Constant.TYPE_SELECT.ATTR,null,null,"1","6",null
//                                        ,mDataBean.getId(),tag.getText().toString()));
                                    mOnCallBack.onStartSelect(Constant.TYPE_SELECT.ATTR,null,null,"1","10",null
                                            ,databean.getId(),tag.getText().toString());
                                }
                                if (mOnCallBack!=null) {
                                    String select = tag.getText().toString();
                                    mOnCallBack.onSelectResString(select);
                                }
                                dismissWithOutAnima();
                            }
                        });
                    }
                });
            }
        };
        shopFiddlePopView.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
            @Override
            public boolean onBeforeDismiss() {
                arrow.startAnimation(closeAnim);
                return super.onBeforeDismiss();
            }
        });
        shopFiddlePopView.setOnCallBack(onCallBack);
        arrow.startAnimation(openAnim);
        shopFiddlePopView.showPopupWindow(view);
    }

    private void getLeaseSelectListData(boolean bLoadView){
        YYMallApi.getLeaseSelectList(_mActivity, mOrder, mBy, mPage, mLimit, mBrand, mAttr, bLoadView,new YYMallApi.ApiResult<ShopSelectBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
//                if (mToolBarStatus == 1)
//                    getShopToolbarAnimIn();
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSelectBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setLeaseSelList(dataBean);
            }
        });
    }
    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
    private CommonAdapter mRvContentAdapter;
    private void setLeaseSelList(ShopSelectBean.DataBean dataBean) {
        if (mRvContentAdapter == null){
            mRvLeaseContent.setLayoutManager(new GridLayoutManager(_mActivity,2));
            mRvLeaseContent.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(_mActivity,1)));
            mRvContentAdapter = new CommonAdapter<ShopSelectBean.DataBean.ListBean>(_mActivity,R.layout.item_shop,dataBean.getList()) {
                @Override
                protected void convert(ViewHolder holder, final ShopSelectBean.DataBean.ListBean bean, int position) {
                    holder.setOnClickListener(R.id.is_fl_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                            intent.putExtra("id",String.valueOf(bean.getId()));
                            mContext.startActivity(intent);
                        }
                    });
                    if (!mShopPutStatus){
                        //此时横条状态
                        holder.setVisible(R.id.is_ll_vert,true);
                        holder.setVisible(R.id.fn_ll_hor,false);
                        // VERT
                        Glide.with(mContext).load(bean.getPic()).into((ImageView)holder.getView(R.id.is_vert_img_shop));
                        ((ImageView)holder.getView(R.id.is_vert_img_tagshop)).setImageResource(R.mipmap.ic_nor_tagfree);
                        holder.setText(R.id.is_vert_shop_groupnumber,String.format(mContext.getString(R.string.getpeoplecount),String.valueOf(bean.getSale())));
                        holder.setText(R.id.is_vert_shop_name,bean.getName());
                        holder.setText(R.id.is_vert_shop_price,String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                    }else{
                        //并列状态
                        holder.setVisible(R.id.is_ll_vert,false);
                        holder.setVisible(R.id.fn_ll_hor,true);
                        holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(View.VISIBLE);
                        // HOR
                        holder.setText(R.id.is_hor_groupnumber,String.format(mContext.getString(R.string.getpeoplecount),String.valueOf(bean.getSale())));
                        holder.setText(R.id.is_hor_shop_name,bean.getName());
                        holder.setText(R.id.is_hor_shop_price,String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                        Glide.with(mContext).load(bean.getPic()).into((ImageView)holder.getView(R.id.is_img_shop));
                        ((ImageView)holder.getView(R.id.is_img_tagshop)).setImageResource(R.mipmap.ic_nor_tagfree);
                    }
                }
            };
            mRvLeaseContent.setAdapter(mRvContentAdapter);
        }else{
            mRvContentAdapter.setDatas(dataBean.getList());
            mRvContentAdapter.notifyDataSetChanged();
        }
    }
    private int mToolBarStatus = 0; //0-展开 1-隐藏
    private ValueAnimator mAnimOut,mAnimIn;
    private TextView mTvHotTag;
    private void initListData() {
        mItemHead = LayoutInflater.from(_mActivity).inflate(R.layout.item_lease_classify,null);
        mTvHotTag = (TextView) mItemHead.findViewById(R.id.ilc_tv_hottag);
        initLeaseHead(mItemHead);
        mRvLeaseContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (mToolBarStatus == 0 && dy > 0)
//                    getShopToolbarAnimOut();
//                else if (mToolBarStatus == 1 && dy < 0)
//                    getShopToolbarAnimIn();
            }
        });
    }
    private void getShopToolbarAnimIn(){
        if (mAnimIn == null){
            mAnimIn = ValueAnimator.ofInt(0,mToolBarHeight);
            mAnimIn.setDuration(300);
            mAnimIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mToolBarParam.height = (Integer) animation.getAnimatedValue();
                    mLlToolbar.setLayoutParams(mToolBarParam);
                }
            });
            mAnimIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolBarStatus = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimIn.start();
        }else if (!mAnimIn.isRunning()){
            mAnimIn.start();
        }

    }
    private void getShopToolbarAnimOut(){
        if (mAnimOut == null){
            mAnimOut = ValueAnimator.ofInt(0,mToolBarHeight);
            mAnimOut.setDuration(300);
            mAnimOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mToolBarParam.height  = mToolBarHeight - (Integer) animation.getAnimatedValue();
                    mLlToolbar.setLayoutParams(mToolBarParam);
                }
            });
            mAnimOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolBarStatus = 1;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimOut.start();
        }else if (!mAnimOut.isRunning()){
            mAnimOut.start();
        }
    }

    public void tooglePager(boolean isOpen) {
        if (isOpen) {
            mRefreshLayout.setEnableRefresh(false);
        } else {
            mRefreshLayout.setEnableRefresh(true);
        }
    }
    public void scrollToFirst(boolean isSmooth) {
        if (mRvHotLease == null) {
            return;
        }
        if (isSmooth) {
            mRvHotLease.smoothScrollToPosition(0);
        } else {
            mRvHotLease.scrollToPosition(0);
        }
    }
    @Override
    public void onPagerClosed() {
        mImgBackTop.setVisibility(View.VISIBLE);
        mViewStatus.setBackgroundColor(getResources().getColor(R.color.white));
        tooglePager(false);
    }

    @Override
    public void onPagerOpened() {
        mImgBackTop.setVisibility(View.INVISIBLE);
        tooglePager(true);
    }

    @Override
    public void startPagerOpened() {
        mViewStatus.setBackgroundColor(getResources().getColor(R.color.theme_bule));
        scrollToFirst(false);
    }

    @Override
    public boolean onBackPressedSupport() {
        if(mHeaderPagerBehavior.isClosed()){
            mHeaderPagerBehavior.openPager();
            return true;
        }
        return super.onBackPressedSupport();
    }
}
