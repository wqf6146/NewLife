package com.yhkj.yymall.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.vise.xsnow.event.BusFactory;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DailyDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.RecommenActivity;
import com.yhkj.yymall.activity.ShopClassifyActivity;
import com.yhkj.yymall.activity.ShopListActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.YiYaShopBean;
import com.yhkj.yymall.bean.YiyaListBean;
import com.yhkj.yymall.event.MainTabSelectEvent;

import java.util.List;

import static android.view.View.GONE;


/**
 * Created by Administrator on 2017/7/7.
 */

public class YiYamallAdapter extends DelegateAdapter {
    Context mContext;
    YiYamallTopAdapeter yiYamallTopAdapeter;
    YiYamallclassifyAdapeter yiYamallclassifyAdapeter;
    YiYamallGridAdapeter yiYamallGridAdapeter;

    private YiYaShopBean.DataBean dataBean;

    public YiYamallAdapter(Activity context, final VirtualLayoutManager layoutManager, YiYaShopBean.DataBean dataBeans) {
        super(layoutManager);
        mContext = context;
        this.dataBean = dataBeans;
        initChildAdapter();
    }

    private void initChildAdapter() {
        yiYamallTopAdapeter = new YiYamallAdapter.YiYamallTopAdapeter(new LinearLayoutHelper(), R.layout.item_yiyamall_top, 1) {
            @Override
            public void onBindViewHolder(YiYamallAdapter.YiYamallTopViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

        };
        addAdapter(yiYamallTopAdapeter);

        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setMarginBottom(10);


        yiYamallclassifyAdapeter = new YiYamallAdapter.YiYamallclassifyAdapeter(linearLayoutHelper, R.layout.item_yiyamall_line, dataBean.getCategroys().size()) {
            @Override
            public void onBindViewHolder(YiYamallAdapter.YiYamallClassifyViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                YiYamallClassifyViewHolder yiYamallClassifyViewHolder = holder;
                yiYamallClassifyViewHolder.bindView(dataBean.getCategroys().get(position), position);
            }
        };
        addAdapter(yiYamallclassifyAdapeter);

        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
        gridLayoutHelper.setHGap(10);
        gridLayoutHelper.setVGap(10);
        gridLayoutHelper.setAutoExpand(false);
        yiYamallGridAdapeter = new YiYamallAdapter.YiYamallGridAdapeter(gridLayoutHelper, R.layout.item_shop) {
            @Override
            public void onBindViewHolder(YiYamallAdapter.YiYamallGridViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                YiYamallGridViewHolder yiYamallGridViewHolder = holder;
                yiYamallGridViewHolder.bindView(dataBean.getGoods().get(position), position);
            }
        };
        addAdapter(yiYamallGridAdapeter);
    }

    public void addDatas(List<YiyaListBean.DataBean.GoodsBean>  list){
        int start = dataBean.getGoods().size();
        dataBean.getGoods().addAll(list);
//        yiYamallGridAdapeter.notifyDataSetChanged();
        yiYamallGridAdapeter.notifyItemRangeChanged(start,dataBean.getGoods().size());
    }

    class YiYamallTopAdapeter extends DelegateAdapter.Adapter<YiYamallTopViewHolder> {
        private LayoutHelper mLayoutHelper;
        private int mCount = 0;
        private int mLayoutId;

        public YiYamallTopAdapeter(LayoutHelper layoutHelper, int layoutId, int count) {
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            mLayoutId = layoutId;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public YiYamallTopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new YiYamallAdapter.YiYamallTopViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(YiYamallTopViewHolder holder, int position) {
            holder.mImg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusFactory.getBus().post(new MainTabSelectEvent(1));
                }
            });
            holder.mImg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, RecommenActivity.class));
                }
            });
            holder.mImg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopClassifyActivity.class);
                    intent.putExtra("select",dataBean.getFields().get(0));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE,Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });
            holder.mImg4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopClassifyActivity.class);
                    intent.putExtra("select",dataBean.getFields().get(1));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE,Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });
            holder.mImg5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopClassifyActivity.class);
                    intent.putExtra("select",dataBean.getFields().get(2));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE,Constant.TOOLBAR_TYPE.SEARCH_TV);
                    mContext.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class YiYamallclassifyAdapeter extends DelegateAdapter.Adapter<YiYamallClassifyViewHolder> {
        private LayoutHelper mLayoutHelper;
        private int mCount = 0;
        private int mLayoutId;

        public YiYamallclassifyAdapeter(LayoutHelper layoutHelper, int layoutId, int count) {
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            mLayoutId = layoutId;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public YiYamallClassifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new YiYamallAdapter.YiYamallClassifyViewHolder(mContext,LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(YiYamallClassifyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    class YiYamallGridAdapeter extends DelegateAdapter.Adapter<YiYamallGridViewHolder> {
        private GridLayoutHelper gridLayoutHelper;
        private int mLayoutId;

        public YiYamallGridAdapeter(GridLayoutHelper gridLayoutHelper, int layoutId) {
            this.gridLayoutHelper = gridLayoutHelper;
            mLayoutId = layoutId;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return gridLayoutHelper;
        }

        @Override
        public YiYamallGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new YiYamallAdapter.YiYamallGridViewHolder(mContext,LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(YiYamallGridViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return dataBean.getGoods().size();
        }
    }

    static class YiYamallTopViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView mImg1;
        ImageView mImg2;
        ImageView mImg3;
        ImageView mImg4;
        ImageView mImg5;

        public YiYamallTopViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mImg1 = (ImageView) view.findViewById(R.id.img_yiyamall1);
            mImg2 = (ImageView) view.findViewById(R.id.img_yiyamall2);
            mImg3 = (ImageView) view.findViewById(R.id.img_yiyamall3);
            mImg4 = (ImageView) view.findViewById(R.id.img_yiyamall4);
            mImg5 = (ImageView) view.findViewById(R.id.img_yiyamall5);

        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    static class YiYamallClassifyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tv_yiyamall_line_name, tv_yiyamall_line_ad1, tv_yiyamall_line_ad2;
        ImageView img_yiyamall_ad;
        LinearLayout iyl_rl_container;
        Context mContext;

        public YiYamallClassifyViewHolder(Context context,View itemView) {
            super(itemView);
            view = itemView;
            mContext = context;
            tv_yiyamall_line_name = (TextView) view.findViewById(R.id.tv_yiyamall_line_name);
            tv_yiyamall_line_ad1 = (TextView) view.findViewById(R.id.tv_yiyamall_line_ad1);
            tv_yiyamall_line_ad2 = (TextView) view.findViewById(R.id.tv_yiyamall_line_ad2);
            img_yiyamall_ad = (ImageView) view.findViewById(R.id.img_yiyamall_ad);
            iyl_rl_container = (LinearLayout)view.findViewById(R.id.iyl_ll_container);
        }

        public void bindView(final YiYaShopBean.DataBean.CategroysBean categroysBeans, final int pos) {
            tv_yiyamall_line_name.setText(categroysBeans.getName());
            tv_yiyamall_line_ad1.setText(categroysBeans.getSlogan1());
            tv_yiyamall_line_ad2.setText(categroysBeans.getSlogan2());
            Glide.with(mContext).load(categroysBeans.getPic()).into(img_yiyamall_ad);
            iyl_rl_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ShopListActivity.class);
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                    intent.putExtra("name", categroysBeans.getName());
                    intent.putExtra("id", String.valueOf(categroysBeans.getId()));
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    static class YiYamallGridViewHolder extends RecyclerView.ViewHolder {

        View view;
        private LinearLayout is_ll_vert, is_ll_hor;
        private TextView is_vert_shop_name, is_vert_shop_price, is_vert_shop_groupnumber;
        private ImageView is_vert_img_shop,is_vert_img_tagshop;
        private Intent intent;
        Context mContext;

        public YiYamallGridViewHolder(Context context,View itemView) {
            super(itemView);
            view = itemView;
            mContext = context;
            is_ll_vert = (LinearLayout) view.findViewById(R.id.is_ll_vert);
            is_ll_hor = (LinearLayout) view.findViewById(R.id.fn_ll_hor);
            is_vert_shop_name = (TextView) view.findViewById(R.id.is_vert_shop_name);
            is_vert_shop_price = (TextView) view.findViewById(R.id.is_vert_shop_price);
            is_vert_shop_groupnumber = (TextView) view.findViewById(R.id.is_vert_shop_groupnumber);
            is_vert_img_shop = (ImageView) view.findViewById(R.id.is_vert_img_shop);
            is_vert_img_tagshop = (ImageView) view.findViewById(R.id.is_vert_img_tagshop);

            is_ll_vert.setVisibility(View.VISIBLE);
            is_ll_hor.setVisibility(GONE);
        }

        public void bindView(final YiYaShopBean.DataBean.GoodsBean goodsBean, final int pos) {
            is_vert_shop_name.setText(goodsBean.getName());
            is_vert_shop_groupnumber.setText("已售" + goodsBean.getSale() + "件");

            Glide.with(mContext).load(goodsBean.getImg()).into(is_vert_img_shop);
            is_vert_img_tagshop.setVisibility(GONE);
            if ((goodsBean.getType() + "").equals("2")) {
                is_vert_shop_price.setText("¥" + goodsBean.getPrice());
                is_vert_img_tagshop.setVisibility(View.VISIBLE);
                is_vert_img_tagshop.setImageResource(R.mipmap.ic_nor_tagfree);
            }else if((goodsBean.getType() + "").equals("1")){
                is_vert_shop_price.setText("¥" + goodsBean.getPrice());
                is_vert_img_tagshop.setVisibility(View.VISIBLE);
                is_vert_img_tagshop.setImageResource(R.mipmap.ic_nor_taggroup);
            }else if((goodsBean.getType() + "").equals("3")){
                //折扣
                is_vert_shop_price.setText("¥" + goodsBean.getPrice());
                is_vert_img_tagshop.setVisibility(View.VISIBLE);
                is_vert_img_tagshop.setImageResource(R.mipmap.ic_nor_tagdiscount);
            }else if((goodsBean.getType() + "").equals("4")){
                //积分
                is_vert_shop_price.setText(goodsBean.getPrice() + "积分");
                is_vert_img_tagshop.setVisibility(View.VISIBLE);
                is_vert_img_tagshop.setImageResource(R.mipmap.ic_nor_tagintegral);
            }else if (goodsBean.getType() == 0 && goodsBean.getPanicBuyItemId() != 0){
                //限时抢购
                is_vert_shop_price.setText("¥" + goodsBean.getPrice());
                is_vert_img_tagshop.setImageResource(R.mipmap.ic_nor_tagtimekill);
                is_vert_img_tagshop.setVisibility(View.VISIBLE);
            }else{
                is_vert_shop_price.setText("¥" + goodsBean.getPrice());
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((goodsBean.getType() == 0)) {
                        if (goodsBean.getPanicBuyItemId()!= 0){
                            Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                            intent.putExtra("id",goodsBean.getPanicBuyItemId() + "");
                            mContext.startActivity(intent);
                        }else{
                            intent = new Intent(mContext, CommodityDetailsActivity.class);
                            intent.putExtra("goodsId", goodsBean.getId() + "");
                            mContext.startActivity(intent);
                        }
                    } else if ((goodsBean.getType() + "").equals("2")) {
                        intent = new Intent(mContext, LeaseDetailActivity.class);
                        intent.putExtra("id", goodsBean.getId() + "");
                        mContext.startActivity(intent);
                    }else if((goodsBean.getType() + "").equals("1")){
                        intent = new Intent(mContext, GrouponDetailsActivity.class);
                        intent.putExtra("goodsId", goodsBean.getId() + "");
                        mContext.startActivity(intent);
                    }else if((goodsBean.getType() + "").equals("3")){
                        //折扣
                        Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                        intent.putExtra("goodsId", goodsBean.getId() + "");
                        mContext.startActivity(intent);
                    }else if((goodsBean.getType() + "").equals("4")){
                        //积分
                        Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                        intent.putExtra("id", goodsBean.getId() + "");
                        mContext.startActivity(intent);
                    }else if((goodsBean.getType() + "").equals("6")){
                        //日常活动
                        Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                        intent.putExtra("goodsId", goodsBean.getId() + "");
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }


}
