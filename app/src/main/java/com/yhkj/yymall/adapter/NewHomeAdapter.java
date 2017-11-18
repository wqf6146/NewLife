package com.yhkj.yymall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DailyActActivity;
import com.yhkj.yymall.activity.DailyDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.GrouponListActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.IntegralShopListActivity;
import com.yhkj.yymall.activity.OffPriceShopListActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.LotteryActivity;
import com.yhkj.yymall.activity.SeckillingActivity;
import com.yhkj.yymall.activity.ShopListActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerBean;
import com.yhkj.yymall.bean.DaySignBeab;
import com.yhkj.yymall.bean.HomeActBean;
import com.yhkj.yymall.bean.HomeRecommBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.CountView;
import com.yhkj.yymall.view.viewpager.UltraViewPager;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class NewHomeAdapter extends RecyclerView.Adapter<NewHomeAdapter.HomeViewHolder> {
    private Activity mContext;
    private DecimalFormat mTwoPointFormat = new java.text.DecimalFormat("#0.00");
    public NewHomeAdapter(Activity context) {
        mContext = context;
    }
    @Override
    public void onBindViewHolder(NewHomeAdapter.HomeViewHolder holder, int position) {
        if (position == 0) {
            bindBanner(holder);
        } else if (position == 1){
            bindIntegral(holder);
        } else if (position == 2) {
            bindClassify(holder);
        } else if (position == 3) {
            bindActModule(holder);
        } else if (position == 4) {
            bindGroup(holder);
        } else if (position >= 5) {
            bindListItem(holder, position);
        }
    }
    private void bindIntegral(HomeViewHolder holder) {
        ImageView img = (ImageView)holder.itemView.findViewById(R.id.ii_img);
        img.setImageResource(R.mipmap.ic_nor_blueintengral);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, IntegralShopListActivity.class));
            }
        });
    }
    private void bindListItem(HomeViewHolder homeViewHolder, int position) {
        if (mRecomBean != null) {
            final HomeRecommBean.DataBean.CategorysBean bean = mRecomBean.getCategorys().get(position - 5);
            HomeListItemHolder holder = (HomeListItemHolder) homeViewHolder;
            holder.mTvName.setText(bean.getName());
            holder.mTvGroupSize.setVisibility(View.GONE);
            holder.mLlAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,ShopListActivity.class);
                    intent.putExtra("name",bean.getName());
                    intent.putExtra("id",String.valueOf(bean.getId()));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });
            if (bean.getGoods() != null && bean.getGoods().size() > 0) {
                List<HomeRecommBean.DataBean.CategorysBean.GoodsBean> goodsBeen = bean.getGoods();
                if (goodsBeen.size() > 0) {
                    final HomeRecommBean.DataBean.CategorysBean.GoodsBean goodsBean = goodsBeen.get(0);
                    holder.mImgTagShop1.setVisibility(View.VISIBLE);
                    holder.mImgShop1.setWillNotDraw(false);
                    Glide.with(mContext).load(goodsBean.getImg()).into(holder.mImgShop1);
                    holder.mImgTagShop1.setVisibility(View.GONE);
                    holder.mTvShopName1.setText(goodsBean.getName());
                    holder.mTvSaleTotal1.setText(String.format(mContext.getString(R.string.sale), String.valueOf(goodsBean.getSale())));
                    if (goodsBean.getType() == 1){
                        holder.mTvPrice1.setText("¥"+ mTwoPointFormat.format(goodsBean.getPrice()));
                        holder.mImgTagShop1.setImageResource(R.mipmap.ic_nor_taggroup);
                        holder.mImgTagShop1.setVisibility(View.VISIBLE);
                    }else if (goodsBean.getType() == 2){
                        holder.mTvPrice1.setText("¥"+ mTwoPointFormat.format(goodsBean.getPrice()));
                        holder.mImgTagShop1.setImageResource(R.mipmap.ic_nor_tagfree);
                        holder.mImgTagShop1.setVisibility(View.VISIBLE);
                    }else if (goodsBean.getType() == 3){
                        //折扣
                        holder.mTvPrice1.setText("¥"+mTwoPointFormat.format(goodsBean.getPrice()));
                        holder.mImgTagShop1.setImageResource(R.mipmap.ic_nor_tagdiscount);
                        holder.mImgTagShop1.setVisibility(View.VISIBLE);
                    }else if (goodsBean.getType() == 4){
                        //积分
                        holder.mTvPrice1.setText(Math.round(goodsBean.getPrice()) + "积分");
                        holder.mImgTagShop1.setImageResource(R.mipmap.ic_nor_tagintegral);
                        holder.mImgTagShop1.setVisibility(View.VISIBLE);
                    }else{
                        holder.mTvPrice1.setText("¥"+mTwoPointFormat.format(goodsBean.getPrice()));
                    }
                    holder.mLlShop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //商品类型：0，普通商品 1，拼团商品 2，租赁商品 ,3打折促销，4积分兑换
                            if (goodsBean.getType() == 1){
                                Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                intent.putExtra("goodsId", goodsBean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (goodsBean.getType() == 0){
                                Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                intent.putExtra("goodsId", goodsBean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (goodsBean.getType() == 2){
                                Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                intent.putExtra("id", goodsBean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (goodsBean.getType() == 3){
                                //折扣
                                Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                intent.putExtra("goodsId", goodsBean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (goodsBean.getType() == 4){
                                //积分
                                Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                intent.putExtra("id", goodsBean.getId() + "");
                                mContext.startActivity(intent);
                            }else if (goodsBean.getType() == 6){
                                //日常活动
                                Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                intent.putExtra("goodsId", goodsBean.getId() + "");
                                mContext.startActivity(intent);
                            }
                        }
                    });

                }else{
                    holder.mImgTagShop1.setVisibility(View.GONE);
                    holder.mImgShop1.setWillNotDraw(true);
                    holder.mTvShopName1.setText("");
                    holder.mTvPrice1.setText("");
                    holder.mTvSaleTotal1.setText("");
                    holder.mLlShop1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
                if (goodsBeen.size() > 1) {
                    final HomeRecommBean.DataBean.CategorysBean.GoodsBean bean1 = goodsBeen.get(1);
                    holder.mImgTagShop2.setVisibility(View.VISIBLE);
                    holder.mImgShop2.setWillNotDraw(false);
                    Glide.with(mContext).load(bean1.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into(holder.mImgShop2);
                    holder.mTvShopName2.setText(bean1.getName());
                    holder.mTvSaleTotal2.setText(String.format(mContext.getString(R.string.sale), String.valueOf(bean1.getSale())));
                    if (bean1.getType() == 1){
                        holder.mTvPrice2.setText("¥" + mTwoPointFormat.format(bean1.getPrice()));
                        holder.mImgTagShop2.setImageResource(R.mipmap.ic_nor_taggroup);
                        holder.mImgTagShop2.setVisibility(View.VISIBLE);
                    }else if (bean1.getType() == 2){
                        holder.mTvPrice2.setText("¥" + mTwoPointFormat.format(bean1.getPrice()));
                        holder.mImgTagShop2.setImageResource(R.mipmap.ic_nor_tagfree);
                        holder.mImgTagShop2.setVisibility(View.VISIBLE);
                    }else if (bean1.getType() == 3){
                        //折扣
                        holder.mTvPrice2.setText("¥" + mTwoPointFormat.format(bean1.getPrice()));
                        holder.mImgTagShop2.setImageResource(R.mipmap.ic_nor_tagdiscount);
                        holder.mImgTagShop2.setVisibility(View.VISIBLE);
                    }else if (bean1.getType() == 4){
                        //积分
                        holder.mTvPrice2.setText(Math.round(bean1.getPrice()) + "积分");
                        holder.mImgTagShop2.setImageResource(R.mipmap.ic_nor_tagintegral);
                        holder.mImgTagShop2.setVisibility(View.VISIBLE);
                    }else{
                        holder.mTvPrice2.setText("¥" + mTwoPointFormat.format(bean1.getPrice()));
                    }
                    holder.mLlShop2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //商品类型：0，普通商品 1，拼团商品 2，租赁商品 ,3打折促销，4积分兑换
                            if (bean1.getType() == 1){
                                Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                intent.putExtra("goodsId", bean1.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean1.getType() == 0){
                                Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                intent.putExtra("goodsId", bean1.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean1.getType() == 2){
                                Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                intent.putExtra("id", bean1.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean1.getType() == 3){
                                //折扣
                                Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                intent.putExtra("goodsId", bean1.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean1.getType() == 4){
                                //积分
                                Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                intent.putExtra("id", bean1.getId() + "");
                                mContext.startActivity(intent);
                            }else if (bean1.getType() == 6){
                                //日常活动
                                Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                intent.putExtra("goodsId", bean1.getId() + "");
                                mContext.startActivity(intent);
                            }
                        }
                    });
                }else{
                    //reinit
                    holder.mImgTagShop2.setVisibility(View.GONE);
                    holder.mImgShop2.setWillNotDraw(true);
                    holder.mTvShopName2.setText("");
                    holder.mTvPrice2.setText("");
                    holder.mTvSaleTotal2.setText("");
                    holder.mLlShop2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            }
        }
    }

    private void bindGroup(HomeViewHolder homeViewHolder) {
        if (mActData != null) {
            HomeGroupPayHolder holder = (HomeGroupPayHolder) homeViewHolder;
            if (mActData.getGroupPurchase() != null && mActData.getGroupPurchase().size() > 0) {
                final HomeActBean.DataBean.GroupPurchaseBean bean = mActData.getGroupPurchase().get(0);
                holder.mImgShop1.setWillNotDraw(false);
                holder.mImgShopTag1.setVisibility(View.VISIBLE);
                holder.mTvGoPay1.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into(holder.mImgShop1);
                holder.mTvShopName1.setText(bean.getName());
                holder.mTvPrice1.setText("¥" + bean.getPrice());
                holder.mTvMarkPrice1.setText(bean.getMarketPrice());
                holder.mTvMarkPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.mTvSaleTotal1.setText(String.format(mContext.getString(R.string.sale), String.valueOf(bean.getSale())));
                holder.ll_home_groupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GrouponListActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                holder.ll_home_groupon1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                        intent.putExtra("goodsId", bean.getId() + "");
                        mContext.startActivityForResult(intent,1);
                    }
                });
                if (mActData.getGroupPurchase().size() > 1) {
                    final HomeActBean.DataBean.GroupPurchaseBean bean1 = mActData.getGroupPurchase().get(1);
                    holder.mImgShop2.setWillNotDraw(false);
                    Glide.with(mContext).load(bean1.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into(holder.mImgShop2);
                    holder.mImgShopTag2.setVisibility(View.VISIBLE);
                    holder.mTvGoPay2.setVisibility(View.VISIBLE);
                    holder.mTvShopName2.setText(bean1.getName());
                    holder.mTvPrice2.setText("¥" + bean1.getPrice());
                    holder.mTvMarkPrice2.setText(bean1.getMarketPrice());
                    holder.mTvMarkPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.mTvSaleTotal2.setText(String.format(mContext.getString(R.string.sale), String.valueOf(bean1.getSale())));
                    holder.ll_home_groupon2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                            intent.putExtra("goodsId", bean1.getId()+"");
                            mContext.startActivity(intent);
                        }
                    });
                }else{
                    //reinit
                    holder.mImgShopTag2.setVisibility(View.GONE);
                    holder.mTvGoPay2.setVisibility(View.GONE);
                    holder.mImgShop2.setWillNotDraw(true);
                    holder.mTvShopName2.setText("");
                    holder.mTvPrice2.setText("");
                    holder.mTvMarkPrice2.setText("");
                    holder.mTvSaleTotal2.setText("");
                    holder.ll_home_groupon2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }
            }else{
                //reinit
                holder.mImgShopTag1.setVisibility(View.GONE);
                holder.mTvGoPay1.setVisibility(View.GONE);
                holder.mImgShop1.setWillNotDraw(true);
                holder.mTvShopName1.setText("");
                holder.mTvPrice1.setText("");
                holder.mTvMarkPrice1.setText("");
                holder.mTvSaleTotal1.setText("");
                holder.ll_home_groupon1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                holder.mImgShopTag2.setVisibility(View.GONE);
                holder.mTvGoPay2.setVisibility(View.GONE);
                holder.mImgShop2.setWillNotDraw(true);
                holder.mTvShopName2.setText("");
                holder.mTvPrice2.setText("");
                holder.mTvMarkPrice2.setText("");
                holder.mTvSaleTotal2.setText("");
                holder.ll_home_groupon2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        }
    }

    private void bindActModule(HomeViewHolder homeViewHolder) {
        if (mActData != null) {
            HomeActModuleHolder holder = (HomeActModuleHolder) homeViewHolder;
//            holder.mLlJfcj.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
//                        Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
//                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
//                        return;
//                    }
//                    if (mActData.getActivity2().getCleanSale() == -1){
//                        Toast.makeText(mContext,"当前暂无活动",Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    mContext.startActivity(new Intent(mContext, LotteryActivity.class));
//                }
//            });

            holder.mLltimekill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mActData.getActivity2().getFlashSale() == -1){
                        Toast.makeText(mContext,"当前暂无活动",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(mContext, SeckillingActivity.class);
                    mContext.startActivity(intent);
                }
            });
            holder.mLlIntegral.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, OffPriceShopListActivity.class));
                }
            });
            holder.mLlChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusFactory.getBus().post(new MainTabSelectEvent(1));
                }
            });
            holder.mTvGetChartPeople.setText(String.format(mContext.getString(R.string.chart_getpeople), String.valueOf(mActData.getActivity2().getFree())));
            holder.mTvCleaLinePeople.setText(String.format(mContext.getString(R.string.cleanlinepeople), String.valueOf(mActData.getActivity2().getCleanSale() == -1 ? 0 : mActData.getActivity2().getCleanSale())));
            holder.mCountView.start((long) mActData.getActivity2().getFlashSale() == -1 ? 0 : (long) mActData.getActivity2().getFlashSale());
        }
    }

    private void bindClassify(HomeViewHolder holder) {
        if (mActData == null) return;
        View view = holder.itemView;

        Glide.with(mContext).load(mActData.getActivity().getPic()).into((ImageView)view.findViewById(R.id.ihc_img_1));
        ((TextView)view.findViewById(R.id.ihc_tv_1)).setText(mActData.getActivity().getName());
        view.findViewById(R.id.ll_home_act).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActData.getActivity().getStatus() == 1){
                    Intent intent = new Intent(mContext, DailyActActivity.class);
                    mContext.startActivity(intent);
                }else{
                    Toast.makeText(mContext,"暂无活动",Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.ihc_ll_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusFactory.getBus().post(new MainTabSelectEvent(1));
            }
        });

        view.findViewById(R.id.ihc_ll_lottery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    return;
                }
                if (mActData.getRoll() != 1){
                    Toast.makeText(mContext,"当前暂无活动",Toast.LENGTH_SHORT).show();
                    return;
                }
                mContext.startActivity(new Intent(mContext, LotteryActivity.class));
            }
        });

        view.findViewById(R.id.ll_home_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    Toast.makeText(mContext,"请先登录",Toast.LENGTH_SHORT).show();
                    mContext.startActivity(new Intent(mContext,LoginActivity.class));
                    return;
                }
                YYMallApi.daySign(mContext, new YYMallApi.ApiResult<DaySignBeab.DataBean>(mContext) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(DaySignBeab.DataBean dataBean) {
                        Toast.makeText(mContext, dataBean.getData(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void bindBanner(NewHomeAdapter.HomeViewHolder holder) {
        if (mBannerData != null && mBannerData.getSlides() != null && mBannerData.getSlides().size() > 0) {
            UltraBannerPagerAdapter adapters = new UltraBannerPagerAdapter(mContext);
            adapters.setOnItemCLickListener(new com.yhkj.yymall.adapter.UltraBannerPagerAdapter.OnItemClickListener() {
                @Override
                public void onItemClickListener(ImageView imageView, int pos) {

                }
            });
            adapters.setDataBean(mBannerData);
            UltraViewPager ultraViewPager = (UltraViewPager) holder.itemView;
            ultraViewPager.setAdapter(adapters);
            ultraViewPager.setAutoScroll(2000);
            ultraViewPager.initIndicator();
            ultraViewPager.getIndicator().setMargin(0,0,0,20);
            ultraViewPager.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL);
            ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            ultraViewPager.getIndicator().setFocusResId(0).setNormalResId(0);
            ultraViewPager.getIndicator().setFocusColor(mContext.getResources().getColor(R.color.theme_bule)).setNormalColor(mContext.getResources().getColor(R.color.halfgraybg))
                    .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics()));
            ultraViewPager.getIndicator().build();
        }
    }

    @Override
    public void onBindViewHolder(NewHomeAdapter.HomeViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public NewHomeAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Constant.TYPE_HOME.BANNER)
            return new HomeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_banner_pager, parent, false));
        else if (viewType == Constant.TYPE_HOME.ACTIVITY_CLASSIFY)
            return new HomeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_classify, parent, false));
        else if (viewType == Constant.TYPE_HOME.ACTIVITY_MODULE)
            return new HomeActModuleHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_actmodule, parent, false));
        else if (viewType == Constant.TYPE_HOME.ACTIVITY_GROUP)
            return new HomeGroupPayHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_grouppay, parent, false));
        else if (viewType == Constant.TYPE_HOME.ACTIVITY_INTEGRALBAR)
            return new HomeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_img, parent, false));
        else
            return new HomeListItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_grid_blue, parent, false));
    }

    @Override
    public int getItemCount() {
        if (mRecomBean != null && mRecomBean.getCategorys() != null && mRecomBean.getCategorys().size() > 0) {
            return 5 + mRecomBean.getCategorys().size();
        }
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 5)
            return 5;
        return position;
    }

    /**
     * 更新轮播图
     *
     * @param bannerData
     */
    private BannerBean.DataBean mBannerData;

    public void setBannerData(BannerBean.DataBean bannerData) {
        mBannerData = bannerData;
        notifyDataSetChanged();
//        mBannerAdapter.notifyDataSetChanged();
    }

    /**
     * 更新首页活动相关
     *
     * @param
     */
    private HomeActBean.DataBean mActData;

    public void setHomeActData(HomeActBean.DataBean homeActData) {
        mActData = homeActData;
        notifyDataSetChanged();
    }

    private HomeRecommBean.DataBean mRecomBean;

    public void setHomeRecommendData(HomeRecommBean.DataBean dataBean) {
        if (dataBean == null || dataBean.getCategorys() == null || dataBean.getCategorys().size() == 0) {
            notifyDataSetChanged();
            return;
        }
        mRecomBean = dataBean;
        notifyDataSetChanged();
    }

    /***
     * 通用holder
     */
    static class HomeViewHolder extends RecyclerView.ViewHolder {
        public HomeViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    class UltraBannerPagerAdapter extends com.yhkj.yymall.view.viewpager.PagerAdapter {

        BannerBean.DataBean mBannerData;

        public void setDataBean(BannerBean.DataBean dataBean) {
            mBannerData = dataBean;
        }

        private Context mContext;
        private com.yhkj.yymall.adapter.UltraBannerPagerAdapter.OnItemClickListener mOnItemClickListener;

        public UltraBannerPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mBannerData == null ? 0 : mBannerData.getSlides().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.view_banner_item, null);
            final ImageView imageView = (ImageView) frameLayout.findViewById(R.id.vb_img);
            Glide.with(mContext).load(mBannerData.getSlides().get(position).getImg()).into(imageView);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClickListener(imageView, position);
                }
            });
            container.addView(frameLayout);
            return frameLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FrameLayout view = (FrameLayout) object;
            container.removeView(view);
        }

        public void setOnItemCLickListener(com.yhkj.yymall.adapter.UltraBannerPagerAdapter.OnItemClickListener onItemCLickListener) {
            mOnItemClickListener = onItemCLickListener;
        }
    }

    static class HomeActModuleHolder extends HomeViewHolder {

        TextView mTvGetChartPeople;
        TextView mTvCleaLinePeople;
        CountView mCountView;
        LinearLayout mLltimekill;
        LinearLayout mLlIntegral;
//        LinearLayout mLlJfcj;
        RelativeLayout mLlChart;

        public HomeActModuleHolder(View itemView) {
            super(itemView);
            mTvGetChartPeople = (TextView) itemView.findViewById(R.id.iha_tv_chartgetpeople);
            mTvCleaLinePeople = (TextView) itemView.findViewById(R.id.iha_cleanlinepeople);
            mLlChart = (RelativeLayout)itemView.findViewById(R.id.iha_ll_chart);
            mLltimekill = (LinearLayout) itemView.findViewById(R.id.iha_timekill);
            mLlIntegral = (LinearLayout) itemView.findViewById(R.id.iha_ll_integral);
            mCountView = (CountView) itemView.findViewById(R.id.iha_countview);
//            mLlJfcj = (LinearLayout)itemView.findViewById(R.id.iha_ll_jfcj);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }


    static class HomeGroupPayHolder extends HomeViewHolder {
        ImageView mImgShop1;
        TextView mTvShopName1;
        TextView mTvPrice1;
        TextView mTvMarkPrice1;
        TextView mTvSaleTotal1;
        TextView mTvGoPay1;

        ImageView mImgShop2;
        ImageView mImgShopTag2,mImgShopTag1;
        TextView mTvShopName2;
        TextView mTvPrice2;
        TextView mTvMarkPrice2;
        TextView mTvSaleTotal2;
        TextView mTvGoPay2;
        LinearLayout ll_home_groupon, ll_home_groupon1, ll_home_groupon2;

        public HomeGroupPayHolder(View itemView) {
            super(itemView);
            mImgShop1 = (ImageView) itemView.findViewById(R.id.ihg_img_shop_1);
            mTvShopName1 = (TextView) itemView.findViewById(R.id.ihg_tv_shopname_1);
            mTvPrice1 = (TextView) itemView.findViewById(R.id.ihg_tv_price_1);
            mTvMarkPrice1 = (TextView) itemView.findViewById(R.id.ihg_tv_marketPrice_1);
            mTvSaleTotal1 = (TextView) itemView.findViewById(R.id.ihg_tv_sale_1);
            mTvGoPay1 = (TextView) itemView.findViewById(R.id.ihg_tv_gopay_1);
            ll_home_groupon = (LinearLayout) itemView.findViewById(R.id.ll_home_groupon);

            mImgShop2 = (ImageView) itemView.findViewById(R.id.ihg_img_shop_2);
            mImgShopTag2 = (ImageView)itemView.findViewById(R.id.ihg_img_shop_tag2);
            mImgShopTag1 = (ImageView)itemView.findViewById(R.id.ihg_img_shop_tag1);
            mTvShopName2 = (TextView) itemView.findViewById(R.id.ihg_tv_shopname_2);
            mTvPrice2 = (TextView) itemView.findViewById(R.id.ihg_tv_price_2);
            mTvMarkPrice2 = (TextView) itemView.findViewById(R.id.ihg_tv_marketPrice_2);
            mTvSaleTotal2 = (TextView) itemView.findViewById(R.id.ihg_tv_sale_2);
            mTvGoPay2 = (TextView) itemView.findViewById(R.id.ihg_tv_gopay_2);
            ll_home_groupon1 = (LinearLayout) itemView.findViewById(R.id.ll_home_groupon1);
            ll_home_groupon2 = (LinearLayout) itemView.findViewById(R.id.ll_home_groupon2);

        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }


    static class HomeListItemHolder extends HomeViewHolder {
        LinearLayout mLlShop1;
        LinearLayout mLlShop2;
        LinearLayout mLlAll;
        TextView mTvName;
        TextView mTvGroupSize;
        ImageView mImgShop1;
        ImageView mImgTagShop1;
        TextView mTvShopName1;
        TextView mTvPrice1;
        //        TextView mTvMarkPrice1;
        TextView mTvSaleTotal1;
        TextView mTvGoPay1;

        ImageView mImgShop2;
        ImageView mImgTagShop2;
        TextView mTvShopName2;
        TextView mTvPrice2;
        //        TextView mTvMarkPrice2;
        TextView mTvSaleTotal2;
        TextView mTvGoPay2;

        public HomeListItemHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.ihgb_tv_name);
            mLlAll = (LinearLayout)itemView.findViewById(R.id.ihgb_ll_all);
            mTvGroupSize = (TextView) itemView.findViewById(R.id.ihgb_tv_groupsize);
            mImgShop1 = (ImageView) itemView.findViewById(R.id.ihg_img_shop_1);
            mImgTagShop1 = (ImageView) itemView.findViewById(R.id.ihg_img_tagshop);
            mTvShopName1 = (TextView) itemView.findViewById(R.id.ihg_tv_shopname_1);
            mTvPrice1 = (TextView) itemView.findViewById(R.id.ihg_tv_price_1);
//            mTvMarkPrice1 = (TextView)itemView.findViewById(R.id.ihg_tv_marketPrice_1);
            mTvSaleTotal1 = (TextView) itemView.findViewById(R.id.ihg_tv_sale_1);
            mTvGoPay1 = (TextView) itemView.findViewById(R.id.ihg_tv_gopay_1);

            mImgShop2 = (ImageView) itemView.findViewById(R.id.ihg_img_shop_2);
            mImgTagShop2 = (ImageView) itemView.findViewById(R.id.ihg_img_tagshop_2);
            mTvShopName2 = (TextView) itemView.findViewById(R.id.ihg_tv_shopname_2);
            mTvPrice2 = (TextView) itemView.findViewById(R.id.ihg_tv_price_2);
//            mTvMarkPrice2 = (TextView)itemView.findViewById(R.id.ihg_tv_marketPrice_2);
            mTvSaleTotal2 = (TextView) itemView.findViewById(R.id.ihg_tv_sale_2);
            mTvGoPay2 = (TextView) itemView.findViewById(R.id.ihg_tv_gopay_2);

            mLlShop1 = (LinearLayout)itemView.findViewById(R.id.ihgb_ll_item1);
            mLlShop2 = (LinearLayout)itemView.findViewById(R.id.ihgb_ll_item2);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
}
