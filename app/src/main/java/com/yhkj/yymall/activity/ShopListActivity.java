package com.yhkj.yymall.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
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
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.ShopSelectBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.ShopFiddlePopView;
import com.yhkj.yymall.view.popwindows.ShopFiltratePopView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ShopListActivity extends BaseToolBarActivity {

    @Bind(R.id.as_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.as_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.as_img_backtop)
    ImageView mImgBackTop;

    @Bind(R.id.as_tv_synthesize)
    TextView mTvSynthesize;

    @Bind(R.id.as_ll_price)
    LinearLayout mLlPrice;

    @Bind(R.id.as_tv_price)
    TextView mTvPrice;

    @Bind(R.id.as_arrow_up)
    ImageView mImgArrowUp;

    @Bind(R.id.as_arrow_down)
    ImageView mImgArrowDown;

    @Bind(R.id.ist_flowlistview)
    NestFullListView mNestListView;

    @Bind(R.id.as_tv_sales)
    TextView mTvSales;

    @Bind(R.id.as_ll_filtrate)
    LinearLayout mLlFiltrate;

    @Bind(R.id.as_fl_nodata)
    FrameLayout mFlNodata;
    private int mToolBarHeight = 0;

    private String mOrder = "sort",mBy = "asc",mPage = "1",mLimit = "10",mBrand = null,mAttr = null;
    private HashMap mPriceMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.StatusBarLightMode(this);
        initRefreshLayout();
        mCateGoryId = getIntent().getStringExtra("id");
        mShopName = getIntent().getStringExtra("name");
        mKeyValue = getIntent().getStringExtra("value");
        if (!TextUtils.isEmpty(mCateGoryId)){
            mUrl = ApiService.GOODSLIST;
        }else if (!TextUtils.isEmpty(mKeyValue)){
            mUrl = ApiService.KEYSEARCH;
        }
    }

    LinearLayout.LayoutParams mToolBarParam;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightVisiable(View.VISIBLE);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchActivity.class, Constant.TOOLBAR_TYPE.SEARCH_EDIT);
                AppManager.getInstance().finishActivity(ShopListActivity.class);
            }
        });
        mTvSynthesize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTabColor(0);
                mOrder = "sort";
                mPage = "1";
                mBy = "asc";
                mRefreshView.setLoadmoreFinished(false);
                getPureListData(true,false,null);
            }
        });
        mTvSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTabColor(1);
                mOrder = "sale";
                mPage = "1";
                mBy = "desc";
                mRefreshView.setLoadmoreFinished(false);
                getPureListData(true,false,null);
            }
        });
        mLlPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTabColor(2);
                mOrder = "price";
                mPage = "1";
                mRefreshView.setLoadmoreFinished(false);
                mBy = mPriceSelectStatus == 2 ? "desc" : "asc";
                getPureListData(true,false,null);
            }
        });
        mLlFiltrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopShopFiltrate();
            }
        });

        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShopListAdapter==null)return;
                Object object = mRecycleView.getTag();
                if ( object != null && (boolean)object == true){
                    // true - ver
                    mRecycleView.setTag(false);
                    GridLayoutManager gridLayoutManager = (GridLayoutManager)mRecycleView.getLayoutManager();
                    gridLayoutManager.setSpanCount(1);
                    setImgRightResource(R.mipmap.ic_nor_listopen);
                }else{
                    mRecycleView.setTag(true);
                    GridLayoutManager gridLayoutManager = (GridLayoutManager)mRecycleView.getLayoutManager();
                    gridLayoutManager.setSpanCount(2);
                    setImgRightResource(R.mipmap.ic_nor_listclose);
                }

                mRecycleView.getAdapter().notifyDataSetChanged();
            }
        });
        mRlToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mToolBarHeight == 0)
                    mToolBarHeight = mRlToolbar.getHeight();
                mToolBarParam = (LinearLayout.LayoutParams)mRlToolbar.getLayoutParams();
            }
        });

        mImgBackTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToolBarStatus == 1){
                    getShopToolbarAnimIn();
                }
                mScrollDy =  0;
                mRecycleView.scrollToPosition(0);
            }
        });
    }
    private int mScrollDy = 0;
    //记录选中属性
    private LinkedHashMap<String,HashMap> mAttrSelectMap = new LinkedHashMap<>();
    //显示商品筛选窗口
    public void showPopShopFiltrate() {

        ShopFiltratePopView shopFiltratePopView = new ShopFiltratePopView(this,mAttrBean,mAttrSelectMap,mPriceMap);
        shopFiltratePopView.setOnCallBack(new ShopFiltratePopView.OnCallBack() {
            @Override
            public void onStartSelect(LinkedHashMap attrHashMap,HashMap priceMap) {
                if (mToolBarStatus == 1)
                    getShopToolbarAnimIn();
                mPriceMap = priceMap;
                mAttrSelectMap = attrHashMap;
                mAttrValueHashMap = new HashMap();
                mPage = "1";
                mRefreshView.setLoadmoreFinished(false);
                //重置
                for (int i=1;i<mShowAttrBean.size(); i++){
                    if (mShowAttrBean.get(i) != null){
                        ShopSelectBean.DataBean.AttrsBean attrsBean = mShowAttrBean.get(i);
                        attrsBean.setName(mSrcName[i]);
                    }
                }

                for (Map.Entry<String, HashMap> item : mAttrSelectMap.entrySet()){
                    HashMap<String,String> attrEntry = item.getValue();

                    for (Map.Entry<String,String> entry : attrEntry.entrySet()){
                        String id = entry.getKey();
                        String name = entry.getValue();
                        mAttrValueHashMap.put(id,name);
                    }
                }
                for (Map.Entry<String, String> item : mAttrValueHashMap.entrySet()){
                    String id = item.getKey();
                    String name = item.getValue();
                    for (int i=1;i<mShowAttrBean.size(); i++){
                        ShopSelectBean.DataBean.AttrsBean attrsBean = mShowAttrBean.get(i);
                        if (attrsBean!=null){
                            if (attrsBean.getId().equals(id)){
                                attrsBean.setName(name);
                            }
                        }
                    }
                }

                Gson gson = new Gson();
                mAttrHashMap.put("attrs",mAttrValueHashMap);
                mAttrHashMap.put("price",mPriceMap);
                mAttr = gson.toJson(mAttrHashMap);
                YYMallApi.getShopList(ShopListActivity.this,mUrl,mKeyValue,mCateGoryId, mOrder, mBy, mPage, mLimit, mBrand, mAttr,true,
                        new YYMallApi.ApiResult<ShopSelectBean.DataBean>(ShopListActivity.this) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        if (mToolBarStatus == 1)
                            getShopToolbarAnimIn();
                        ViseLog.e(e);
                        setNetWorkErrShow(VISIBLE);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(ShopSelectBean.DataBean dataBean) {
                        if (dataBean==null || dataBean.getList() == null || dataBean.getList().size() == 0) {
//                            setNoDataView(R.mipmap.ic_nor_orderbg,"暂无搜索结果");
                            mFlNodata.setVisibility(VISIBLE);
                        }else{
                            mFlNodata.setVisibility(GONE);
                        }
                        setData(dataBean.getList());
                    }
                });

            }
        });
        shopFiltratePopView.showPopupWindow();
    }

    TextView[] mTabViews = new TextView[4];
    @Override
    protected void initData() {
        setTvTitleText("商品列表");
        setTvTitleColor(getResources().getColor(R.color.grayfont));
        setImgBackResource(R.mipmap.ic_nor_gray_arrowleft);
        setImgRightResource(R.mipmap.ic_nor_listopen);
        setToolBarColor(getResources().getColor(R.color.white));
        mTvSearch.setCompoundDrawables(null,null,null,null);
        mTvSearch.setText(mShopName);
        initShopListView();
        mTabViews[0] = mTvSynthesize;
        mTabViews[1] = mTvSales;
        mTabViews[2] = mTvPrice;
    }

    private String mKeyValue,mShopName,mCateGoryId,mUrl;

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private List<ShopSelectBean.DataBean.AttrsBean> mAttrBean;
    private List<ShopSelectBean.DataBean.AttrsBean> mShowAttrBean = new ArrayList<>();
    private List<ShopSelectBean.DataBean.BrandBean> mBrandBean;
    private void getData() {
        YYMallApi.getShopList(this,mUrl, mKeyValue,mCateGoryId, mOrder, mBy, mPage, mLimit, mBrand, mAttr, false,
                new YYMallApi.ApiResult<ShopSelectBean.DataBean>(ShopListActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                setNetWorkErrShow(VISIBLE);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSelectBean.DataBean dataBean) {
//                mShopAdapter = new ShopListAdapter(ShopListActivity.this,mLayoutManager);
//                mRecycleView.setAdapter(mShopAdapter);
                setNetWorkErrShow(GONE);
                setImgRightVisiable(VISIBLE);
                if (dataBean ==null ||dataBean.getList() == null || dataBean.getList().size() == 0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无搜索结果");
                    setImgRightVisiable(GONE);
                }else{
                    mFlNodata.setVisibility(GONE);
                    mAttrBean = dataBean.getAttrs();
                    mBrandBean = dataBean.getBrand();
                    setData(dataBean.getList());
                }
            }
        });
    }

    private CommonAdapter mShopListAdapter;
    private void setData(List<ShopSelectBean.DataBean.ListBean> dataBean) {
        if (mShopListAdapter == null){
            mShopListAdapter = new CommonAdapter<ShopSelectBean.DataBean.ListBean>(ShopListActivity.this, R.layout.item_shop, dataBean) {
                @Override
                protected void convert(ViewHolder holder, final ShopSelectBean.DataBean.ListBean bean, int position) {
                    Object object = mRecycleView.getTag();
                    if (object != null && (boolean) object == true) {
                        holder.setVisible(R.id.is_ll_vert, true);
                        holder.setVisible(R.id.fn_ll_hor, false);
                        holder.setVisible(R.id.is_vert_img_tagshop,false);
                        Glide.with(mContext).load(bean.getPic()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView) holder.getView(R.id.is_vert_img_shop));
                        holder.setText(R.id.is_vert_shop_name, bean.getName());
                        if (bean.getType()== 2){
                            holder.setText(R.id.is_vert_shop_groupnumber, String.format(mContext.getString(R.string.getpeoplecount), String.valueOf(bean.getSale())));
                        }else{
                            holder.setText(R.id.is_vert_shop_groupnumber, String.format(mContext.getString(R.string.salepeoplecount), String.valueOf(bean.getSale())));
                        }
                        if (bean.getType() == 2) {
                            //租赁商品
                            holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                            ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_LEFT);
                            layoutParams.addRule(ALIGN_PARENT_RIGHT);
                            tagShop.setImageResource(R.mipmap.ic_nor_tagfree);
                            holder.setVisible(R.id.is_vert_img_tagshop,true);
                        }else if (bean.getType() == 1){
                            //拼团商品
                            holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));

                            ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(ALIGN_PARENT_LEFT);
                            tagShop.setImageResource(R.mipmap.ic_nor_taggroup);

                            holder.setVisible(R.id.is_vert_img_tagshop,true);
                        }else if (bean.getType() == 3){
                            //折扣
                            holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));

                            ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(ALIGN_PARENT_LEFT);
                            tagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);

                            holder.setVisible(R.id.is_vert_img_tagshop,true);
                        }else if (bean.getType() == 4){
                            //积分
                            holder.setText(R.id.is_vert_shop_price, String.valueOf(bean.getPrice()) + "积分");

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

                            holder.setVisible(R.id.is_img_tagshop,true);
                            holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                        }else{
                            holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                        }
                    } else {
                        holder.setVisible(R.id.is_ll_vert, false);
                        holder.setVisible(R.id.fn_ll_hor, true);
                        holder.setVisible(R.id.is_img_tagshop,false);
                        Glide.with(mContext).load(bean.getPic()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView) holder.getView(R.id.is_img_shop));
                        holder.setText(R.id.is_hor_shop_name, bean.getName());


                        if (bean.getType()== 2){
                            holder.setText(R.id.is_hor_groupnumber, String.format(mContext.getString(R.string.getpeoplecount), String.valueOf(bean.getSale())));
                        }else{
                            holder.setText(R.id.is_hor_groupnumber, String.format(mContext.getString(R.string.salepeoplecount), String.valueOf(bean.getSale())));
                        }
                        if (bean.getType() == 2) {
                            //租赁商品
                            holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));

                            ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_LEFT);
                            layoutParams.addRule(ALIGN_PARENT_RIGHT);
                            tagShop.setImageResource(R.mipmap.ic_nor_tagfree);

                            holder.setVisible(R.id.is_img_tagshop,true);
                        }else if (bean.getType() == 1){
                            //拼团商品
                            holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));

                            ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(ALIGN_PARENT_LEFT);
                            tagShop.setImageResource(R.mipmap.ic_nor_taggroup);

                            holder.setVisible(R.id.is_img_tagshop,true);
                        }else if (bean.getType() == 3){
                            //折扣
                            holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));

                            ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(ALIGN_PARENT_LEFT);
                            tagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);

                            holder.setVisible(R.id.is_img_tagshop,true);
                        }else if (bean.getType() == 4){
                            //积分
                            holder.setText(R.id.is_hor_shop_price, String.valueOf(bean.getPrice()) + "积分");

                            ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(ALIGN_PARENT_LEFT);
                            tagShop.setImageResource(R.mipmap.ic_nor_tagintegral);

                            holder.setVisible(R.id.is_img_tagshop,true);
                        }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                            //限时抢购
                            holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));

                            ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                            layoutParams.addRule(ALIGN_PARENT_LEFT);
                            tagShop.setImageResource(R.mipmap.ic_nor_tagtimekill);

                            holder.setVisible(R.id.is_img_tagshop,true);
                        }else{
                            holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));
                        }
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bean.getType() == 2) {
                                //租赁商品
                                Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                intent.putExtra("id", String.valueOf(bean.getId()));
                                startActivity(intent);
                            }else if (bean.getType() == 0){
                                if (bean.getPanicBuyItemId() != 0){
                                    //限时抢购
                                    Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                    intent.putExtra("id", String.valueOf(bean.getPanicBuyItemId()));
                                    startActivity(intent);
                                }else{
                                    //普通商品
                                    Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                    intent.putExtra("goodsId", String.valueOf(bean.getId()));
                                    startActivity(intent);
                                }
                            }else if (bean.getType() == 1){
                                //拼团商品
                                Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                intent.putExtra("goodsId", String.valueOf(bean.getId()));
                                startActivity(intent);
                            }else if (bean.getType() == 3){
                                //折扣
                                Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                intent.putExtra("goodsId", String.valueOf(bean.getId()));
                                startActivity(intent);
                            }else if (bean.getType() == 4){
                                //积分
                                Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                intent.putExtra("id", String.valueOf(bean.getId()));
                                startActivity(intent);
                            }else if (bean.getType() == 6){
                                //日常活动
                                Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                intent.putExtra("goodsId", String.valueOf(bean.getId()));
                                startActivity(intent);
                            }
                        }
                    });

                }
            };
            mRecycleView.setAdapter(mShopListAdapter);
            final List<ShopSelectBean.DataBean.AttrsBean> attrsBean = mShowAttrBean;
            if (attrsBean.size() == 0){
                for (int i =0; i < 3; i++) {
                    if (mAttrBean.size()-1 >= i ) {
                        ShopSelectBean.DataBean.AttrsBean bean = null;
                        try {
                            bean = (ShopSelectBean.DataBean.AttrsBean) mAttrBean.get(i).clone();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        attrsBean.add(bean);
                    }
                }
                attrsBean.add(0, buildBrandBean(mBrandBean));
            }
//         (!attrsBean.get(0).getId().equals("-1"))

            mNestListView.setTag(attrsBean.size());
            //补齐 4个
            if (attrsBean.size() < 4) {
                int bs = 4 - attrsBean.size();
                for (int i = 0; i < bs; i++)
                    attrsBean.add(null);
            }
//                holder.mFlowList.setTag();
            if (mSrcName == null) mSrcName = new String[attrsBean.size()];
            mNestListView.setAdapter(new NestFullListViewAdapter<ShopSelectBean.DataBean.AttrsBean>(R.layout.item_flow_item, attrsBean) {
                @Override
                public void onBind(int pos, final ShopSelectBean.DataBean.AttrsBean bean, NestFullViewHolder nestFullViewHolder) {
                    if (pos < (Integer) mNestListView.getTag()) {
                        if (TextUtils.isEmpty(mSrcName[pos])) mSrcName[pos] = bean.getName();
                        if (bean.getName().equals("全部")) {
                            String name = mSrcName[pos];
                            nestFullViewHolder.setText(R.id.ifi_tv_tag, CommonUtil.subTextString(name,7));
                        }else {
                            nestFullViewHolder.setText(R.id.ifi_tv_tag,  CommonUtil.subTextString(bean.getName(),7));
                        }
                    } else {
                        nestFullViewHolder.setVisible(R.id.ifi_ll_bg, false);
                    }
                }
            });
            mNestListView.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                @Override
                public void onItemClick(NestFullListView parent, final View view, final int position) {
                    showTabSelectPopView(view, (TextView) view.findViewById(R.id.ifi_tv_tag),
                            (ImageView) view.findViewById(R.id.ifi_img_arrow),
                            (ShopSelectBean.DataBean.AttrsBean) mNestListView.getAdapter().getDatas().get(position), new ShopFiddlePopView.OnCallBack() {
                                @Override
                                public void onSelectResString(String select) {
                                    attrsBean.get(position).setName(select);
                                }

                                @Override
                                public void onStartSelect(int type, String order, String by, String page, String limit, String brand, String key, String value) {
                                    mAttrValueHashMap = new HashMap<>();
                                    if (value!=null && value.equals("全部")) {
                                        Iterator iterator = mAttrValueHashMap.keySet().iterator();
                                        while (iterator.hasNext()) {
                                            String attrkey = (String) iterator.next();
                                            if (key.equals(attrkey)) {
                                                iterator.remove();
                                                mAttrValueHashMap.remove(attrkey);
                                            }
                                        }
                                    }else{
                                        mAttrValueHashMap.put(key, value);
                                    }
                                    mAttrSelectMap.put(String.valueOf(position-1),mAttrValueHashMap);
                                    mPage = "1";
                                    mRefreshView.setLoadmoreFinished(false);
                                    if (mToolBarStatus == 1)
                                        getShopToolbarAnimIn();
                                    refreshLeaseData(type,order,by,page,limit,brand);
                                }
                            });
                }
            });
        }else {
            mShopListAdapter.setDatas(dataBean);
            mShopListAdapter.notifyDataSetChanged();
            mNestListView.updateUI();
        }

    }

    HashMap mAttrHashMap = new LinkedHashMap();
    HashMap<String,String> mAttrValueHashMap = new LinkedHashMap();
    public void refreshLeaseData(int type, String order, String by, String page, String limit, String brand){
        if (type == Constant.TYPE_SELECT.BRAND){
            //品牌
            if (brand == null || brand.equals("0"))
                mBrand = null;
            else
                mBrand = brand;
        }else if (type == Constant.TYPE_SELECT.ATTR){
            Gson gson = new Gson();

            Iterator<Map.Entry<String,HashMap>> iterator = mAttrSelectMap.entrySet().iterator();
            HashMap hashMap = new HashMap();
            while (iterator.hasNext()){
                Map.Entry<String,HashMap> mapEntry = iterator.next();
                HashMap<String,String> mapEntryValue = mapEntry.getValue();
                Iterator<Map.Entry<String,String>> mapiterator = mapEntryValue.entrySet().iterator();
                while (mapiterator.hasNext()){
                    Map.Entry<String,String> bean = mapiterator.next();
                    hashMap.put(bean.getKey(),bean.getValue());
                }
            }

            mAttrHashMap.put("attrs",hashMap);
            mAttrHashMap.put("price",mPriceMap);
            mAttr = gson.toJson(mAttrHashMap);
        }else{
            mOrder = order;
            mBy = by;
            mPage = String.valueOf(page);
            mLimit = String.valueOf(limit);
        }

        getPureListData(true,false,null);
    }

    private void getPureListData(Boolean bShow,final boolean loadmore,final RefreshLayout refreshlayout){
        final boolean bLoadmore = loadmore;
        final boolean bRefresh = !loadmore && refreshlayout!=null;

        if (bLoadmore)
            mPage = String.valueOf(Integer.parseInt(mPage) + 1);
        else if (bRefresh)
            mPage = "1";

        YYMallApi.getShopList(this,mUrl,mKeyValue,mCateGoryId, mOrder, mBy, mPage, mLimit, mBrand,mAttr,bShow, new ApiCallback<ShopSelectBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                if (mToolBarStatus == 1)
                    getShopToolbarAnimIn();
                ViseLog.e(e);
                showToast(e.getMessage());
                if (bLoadmore){
                    mPage = String.valueOf(Integer.parseInt(mPage) - 1);
                    refreshlayout.finishLoadmore();
                }else if (bRefresh){
                    refreshlayout.finishRefresh();
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSelectBean.DataBean dataBean) {
                if (bLoadmore){
                    //加载更多
                    refreshlayout.finishLoadmore();
                    if (dataBean==null || dataBean.getList()==null || dataBean.getList().size() == 0)
                        refreshlayout.setLoadmoreFinished(true);
                    else{
                        mShopListAdapter.addDatas(dataBean.getList());
                        mShopListAdapter.notifyDataSetChanged();
                    }
                }else if (bRefresh){
                    //下拉刷新
                    refreshlayout.finishRefresh();
                    setData(dataBean.getList());
                    refreshlayout.finishRefresh();
                    refreshlayout.setLoadmoreFinished(false);
                }else{
                    if (dataBean==null || dataBean.getList()==null || dataBean.getList().size() == 0){
                        mFlNodata.setVisibility(VISIBLE);
                    }else{
                        mFlNodata.setVisibility(GONE);
                    }
                    setData(dataBean.getList());
                }
            }
        });
    }

    private ShopSelectBean.DataBean.AttrsBean buildBrandBean(List<ShopSelectBean.DataBean.BrandBean> brandBeanList){
        ShopSelectBean.DataBean.AttrsBean bean = new ShopSelectBean.DataBean.AttrsBean();
        bean.setId("-1");
        bean.setName("品牌");
        List<String> stringList = new ArrayList<>();
        for (int i=0;i<brandBeanList.size();i++){
            ShopSelectBean.DataBean.BrandBean brandBean = brandBeanList.get(i);
            stringList.add(brandBean.getName() + "," + brandBean.getId());
        }
        bean.setValue(stringList);
        return bean;
    }

    private String[] mSrcName;
    //-----------------------
    private void showTabSelectPopView(final View view,final TextView tvTag,final ImageView arrow,final ShopSelectBean.DataBean.AttrsBean bean
            ,ShopFiddlePopView.OnCallBack onCallBack){
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

        ShopFiddlePopView shopFiddlePopView = new ShopFiddlePopView<ShopSelectBean.DataBean.AttrsBean>(this,bean){
            @Override
            protected void initPopView(RecyclerView recyclerView, final ShopSelectBean.DataBean.AttrsBean databean) {
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

    private int mToolBarStatus = 0; //0-展开 1-隐藏
    private ValueAnimator mAnimOut,mAnimIn;
    private void getShopToolbarAnimOut(){
        if (mAnimOut == null){
            mAnimOut = ValueAnimator.ofInt(0,mToolBarHeight);
            mAnimOut.setDuration(300);
            mAnimOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mToolBarParam.height  = mToolBarHeight - (Integer) animation.getAnimatedValue();
                    mRlToolbar.setLayoutParams(mToolBarParam);
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


    private void getShopToolbarAnimIn(){
        if (mAnimIn == null){
            mAnimIn = ValueAnimator.ofInt(0,mToolBarHeight);
            mAnimIn.setDuration(300);
            mAnimIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mToolBarParam.height = (Integer) animation.getAnimatedValue();
                    mRlToolbar.setLayoutParams(mToolBarParam);
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

    private GridLayoutManager mGridLayoutHelper;
    private void initShopListView() {

        mGridLayoutHelper = new GridLayoutManager(this,1);
        mRecycleView.setLayoutManager(mGridLayoutHelper);
        mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(this,1)));
//        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//        mRecycleView.setRecycledViewPool(viewPool);
//        viewPool.setMaxRecycledViews(0, 10);


        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollDy >= 2000 && mToolBarStatus == 0 && dy > 0)
                    getShopToolbarAnimOut();
                else if (mToolBarStatus == 1 && dy < 0)
                    getShopToolbarAnimIn();
//                if (mGridLayoutHelper.getOffsetToStart() > 2000 && mImgBackTop.getVisibility() != VISIBLE){
//                    Log.e("BACKTOP", "VISIABLE");
//                    mImgBackTop.setVisibility(View.VISIBLE);
//                }else if (layoutManager.getOffsetToStart() < 2000 && mImgBackTop.getVisibility() != INVISIBLE){
//                    Log.e("BACKTOP", "INVISIABLE");
//                    mImgBackTop.setVisibility(View.INVISIBLE);
//                }
                mScrollDy += dy;
                if (mScrollDy >= 2000 && mImgBackTop.getVisibility() != VISIBLE) {
                    mImgBackTop.setVisibility(VISIBLE);
                }else if (mScrollDy <= 2000 && mImgBackTop.getVisibility() != INVISIBLE){
                    mImgBackTop.setVisibility(INVISIBLE);
                }

            }
        });
    }

    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mScrollDy = 0;
                getPureListData(false,false,refreshlayout);
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getPureListData(false,true,refreshlayout);
            }
        });
    }
    final int mPinkColor = Color.rgb(0,124,209);
    final int mGrayColor = Color.rgb(114,112,112);
    int mPriceSelectStatus = 2;
    //设置tab选择ui
    private void initTabColor(int index){
        mTvPrice.setTextColor(mGrayColor);
        mTvSales.setTextColor(mGrayColor);
        mTvSynthesize.setTextColor(mGrayColor);
        mImgArrowDown.setImageResource(R.mipmap.arraw_bottom);
        mImgArrowUp.setImageResource(R.mipmap.arraw_top);
        if (index >=0 && index<=3){
            mTabViews[index].setTextColor(mPinkColor);
            if (index == 2){
                if (mPriceSelectStatus == 1){
                    mPriceSelectStatus = 2;
                    mImgArrowDown.setImageResource(R.mipmap.ic_nor_bluearrowbot);
                    mImgArrowUp.setImageResource(R.mipmap.arraw_top);
                }else{
                    mPriceSelectStatus = 1;
                    mImgArrowDown.setImageResource(R.mipmap.arraw_bottom);
                    mImgArrowUp.setImageResource(R.mipmap.ic_nor_bluearrowtop);
                }
            }
        }
    }
}



