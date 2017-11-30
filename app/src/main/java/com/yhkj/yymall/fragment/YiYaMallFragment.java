package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DailyDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.MessageActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.RecommenActivity;
import com.yhkj.yymall.activity.ScanActivity;
import com.yhkj.yymall.activity.SearchActivity;
import com.yhkj.yymall.activity.ShopClassifyActivity;
import com.yhkj.yymall.activity.ShopListActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;
//import com.yhkj.yymall.adapter.YiYamallAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.UnReadBean;
import com.yhkj.yymall.bean.YiYaShopBean;
import com.yhkj.yymall.bean.YiyaListBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.view.View.GONE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by Administrator on 2017/6/19.
 */

public class YiYaMallFragment extends BaseFragment {

    @Bind(R.id.fy_toolbar)
    LinearLayout mLlToolbar;

    @Bind(R.id.fy_view_status)
    View mViewStatus;

    @Bind(R.id.rv_yiyamall)
    RecyclerView rv_yiyamall;

    @Bind(R.id.fsc_refreshlayout)
    SmartRefreshLayout fsc_refreshlayout;

    @Bind(R.id.vt_btn_right)
    ImageView mImgRight;

    @Bind(R.id.vt_btn_left)
    ImageView mImgLeft;

    @Bind(R.id.vt_tv_search)
    TextView mTvSearch;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        String hotString = YYApp.getInstance().getmHotSearch();
        if (!TextUtils.isEmpty(hotString)){
            YYApp.getInstance().setmHotSearch(hotString);
            mTvSearch.setHint(hotString);
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
        }else {
            mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        }
    }

    ApiCallback apiCallback = new ApiCallback<YiYaShopBean.DataBean>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onError(ApiException e) {
            fsc_refreshlayout.finishRefresh();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(final YiYaShopBean.DataBean dataBean) {
            fsc_refreshlayout.finishRefresh();
//            final VirtualLayoutManager layoutManager = new VirtualLayoutManager(_mActivity);
//            rv_yiyamall.setLayoutManager(layoutManager);
//            final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//            rv_yiyamall.setRecycledViewPool(viewPool);
//            viewPool.setMaxRecycledViews(0, 20);
//            adapter = new YiYamallAdapter(_mActivity, layoutManager, dataBean);
//            rv_yiyamall.setAdapter(adapter);

//            if (mEntiryAdapter == null || mWrapperAdapter == null){

//            }else{

//            }
            if (mWrapperAdapter!=null && mEntiryAdapter!=null){
                mEntiryAdapter.setDatas(dataBean.getGoods());
                mWrapperAdapter.notifyDataSetChanged();
                return;
            }

            View view = LayoutInflater.from(_mActivity).inflate( R.layout.item_yiyamall_top,rv_yiyamall,false);
            view.findViewById(R.id.img_yiyamall1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusFactory.getBus().post(new MainTabSelectEvent(1));
                }
            });
            view.findViewById(R.id.img_yiyamall2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, RecommenActivity.class));
                }
            });
            view.findViewById(R.id.img_yiyamall3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopClassifyActivity.class);
                    intent.putExtra("select",dataBean.getFields().get(0));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE,Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });
            view.findViewById(R.id.img_yiyamall4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopClassifyActivity.class);
                    intent.putExtra("select",dataBean.getFields().get(1));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE,Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });
            view.findViewById(R.id.img_yiyamall5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopClassifyActivity.class);
                    intent.putExtra("select",dataBean.getFields().get(2));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE,Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });
            NestFullListView listView = (NestFullListView)view.findViewById(R.id.iyt_listview);
            listView.setAdapter(new NestFullListViewAdapter<YiYaShopBean.DataBean.CategroysBean>(R.layout.item_yiyamall_line,dataBean.getCategroys()) {
                @Override
                public void onBind(int pos, final YiYaShopBean.DataBean.CategroysBean categroysBean, NestFullViewHolder holder) {
                    holder.setText(R.id.tv_yiyamall_line_name,categroysBean.getName());
                    holder.setText(R.id.tv_yiyamall_line_ad1,categroysBean.getSlogan1());
                    holder.setText(R.id.tv_yiyamall_line_ad2,categroysBean.getSlogan2());
                    Glide.with(mContext).load(categroysBean.getPic()).into((ImageView)holder.getView(R.id.img_yiyamall_ad));
                    holder.getView(R.id.iyl_ll_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShopListActivity.class);
                            intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                            intent.putExtra("name", categroysBean.getName());
                            intent.putExtra("id", String.valueOf(categroysBean.getId()));
                            mContext.startActivity(intent);
                        }
                    });
                }
            });
            listView.setOnItemClickListener(null);
            mEntiryAdapter = new CommonAdapter<YiYaShopBean.DataBean.GoodsBean>(_mActivity,R.layout.item_shop,dataBean.getGoods()) {
                @Override
                protected void convert(ViewHolder holder, final YiYaShopBean.DataBean.GoodsBean bean, int position) {
                    holder.setVisible(R.id.is_vert_img_tagshop,false);
                    holder.setVisible(R.id.is_ll_vert,true);
                    holder.setVisible(R.id.fn_ll_hor,false);
                    Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic)
                            .into((ImageView)holder.getView(R.id.is_vert_img_shop));
                    holder.setText(R.id.is_vert_shop_groupnumber,"已售" + String.valueOf(bean.getSale())+"件");
                    holder.setText(R.id.is_vert_shop_name,bean.getName());
                    holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                    if (bean.getType() == 2) {
                        //租赁商品
                        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                        ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                        layoutParams.removeRule(ALIGN_PARENT_LEFT);
                        layoutParams.addRule(ALIGN_PARENT_RIGHT);
                        tagShop.setImageResource(R.mipmap.ic_nor_tagfree);

                        holder.setVisible(R.id.is_vert_img_tagshop,true);
                    }else if (bean.getType() == 1){
                        //拼团商品
                        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                        ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                        layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(ALIGN_PARENT_LEFT);
                        tagShop.setImageResource(R.mipmap.ic_nor_taggroup);


                        holder.setVisible(R.id.is_vert_img_tagshop,true);
                    }else if (bean.getType() == 3){
                        //折扣
                        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                        ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                        layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(ALIGN_PARENT_LEFT);
                        tagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);


                        holder.setVisible(R.id.is_vert_img_tagshop,true);
                    }else if (bean.getType() == 4){
                        //积分
                        holder.setText(R.id.is_vert_shop_price,bean.getPrice() + "积分");
                        ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                        layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(ALIGN_PARENT_LEFT);
                        tagShop.setImageResource(R.mipmap.ic_nor_tagintegral);


                        holder.setVisible(R.id.is_vert_img_tagshop,true);

                    }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                        //限时抢购
                        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                        ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                        layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(ALIGN_PARENT_LEFT);
                        tagShop.setImageResource(R.mipmap.ic_nor_tagtimekill);


                        holder.setVisible(R.id.is_vert_img_tagshop,true);

                    }else{
                        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                    }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //商品类型 0 普通商品 1 团购商品 2 租赁商品
                            if (bean.getType() == 0) {
                                if (bean.getPanicBuyItemId() != 0){
                                    Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                    intent.putExtra("id",bean.getPanicBuyItemId() + "");
                                    mContext.startActivity(intent);
                                }else{
                                    Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                    intent.putExtra("goodsId",bean.getId() + "");
                                    mContext.startActivity(intent);
                                }
                            } else if (bean.getType() == 2) {
                                Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                intent.putExtra("id", bean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean.getType() ==  1) {
                                //拼团
                                Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                intent.putExtra("goodsId", bean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean.getType() == 3){
                                //折扣
                                Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                intent.putExtra("goodsId", bean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean.getType() == 4){
                                //积分
                                Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                intent.putExtra("id", bean.getId() + "");
                                mContext.startActivity(intent);
                            }
                            else if (bean.getType() == 6){
                                //积分
                                Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                intent.putExtra("goodsId", bean.getId() + "");
                                mContext.startActivity(intent);
                            }
                        }
                    });

                }
            };
            mWrapperAdapter = new HeaderAndFooterWrapper(mEntiryAdapter);
            mWrapperAdapter.addHeaderView(view);
            rv_yiyamall.setAdapter(mWrapperAdapter);
        }
    };

    CommonAdapter mEntiryAdapter;
    HeaderAndFooterWrapper mWrapperAdapter;

    public static YiYaMallFragment getInstance() {
        YiYaMallFragment fragment = new YiYaMallFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_yiyamall;
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
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
    }

    private int mCurPager = 1;
    private void initRefreshLayout() {
        rv_yiyamall.setLayoutManager(new GridLayoutManager(_mActivity,2));
        rv_yiyamall.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(_mActivity,1)));
        fsc_refreshlayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        fsc_refreshlayout.setEnableLoadmore(true);
        fsc_refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPager = 1;
                fsc_refreshlayout.setLoadmoreFinished(false);
                YYMallApi.getYiYaShop(_mActivity,apiCallback);
            }
        });
        fsc_refreshlayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                int nextpage = mCurPager + 1;
                getPageData(nextpage);
            }
        });
    }

    private void getPageData(final int nextpage) {
        YYMallApi.getYiYaShopList(YYApp.getInstance(), nextpage, new ApiCallback<YiyaListBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                fsc_refreshlayout.finishLoadmore();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(YiyaListBean.DataBean dataBean) {
                mCurPager = nextpage;
                if (dataBean.getGoods() == null || dataBean.getGoods().size() == 0){
                    fsc_refreshlayout.finishLoadmore();
                    fsc_refreshlayout.setLoadmoreFinished(true);
                }else {
                    fsc_refreshlayout.finishLoadmore();
                }
                int start = mWrapperAdapter.getItemCount();
                mEntiryAdapter.addDatas(dataBean.getGoods());
//                mWrapperAdapter.notifyDataSetChanged();
                mWrapperAdapter.notifyItemRangeInserted(start,mEntiryAdapter.getItemCount());
            }
        });
    }

    private Drawable[] mDrawable;
    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        mDrawable = mTvSearch.getCompoundDrawables();
        if (Build.VERSION.SDK_INT < KITKAT)
            mViewStatus.setVisibility(GONE);
    }

    @Override
    protected void initData() {
        mLlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_bule));
        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        mImgRight.setImageResource(R.mipmap.ic_nor_classily);
        mImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YYApp.getInstance().getToken() != null) {
                    Intent intent = new Intent(mContext, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                }
            }
        });
        mImgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShopClassifyActivity.class, Constant.TOOLBAR_TYPE.SEARCH_TV);
            }
        });
        initRefreshLayout();
        YYMallApi.getYiYaShop(_mActivity, apiCallback);
    }

}
