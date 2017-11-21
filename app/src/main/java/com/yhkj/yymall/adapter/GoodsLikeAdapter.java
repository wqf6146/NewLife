package com.yhkj.yymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.yhkj.yymall.activity.TimeKillDetailActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GoodsLikeBean;
import com.yhkj.yymall.event.MainTabSelectEvent;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/3.
 */
public abstract class GoodsLikeAdapter extends DelegateAdapter {

    private Context mContext;

    public GoodsLikeAdapter(Context context, final VirtualLayoutManager layoutManager, GoodsLikeBean.DataBean bean) {
        super(layoutManager,false);
        mContext = context;
        initChildAdapter(bean);
    }

    public GoodsLikeAdapter(Context context, final VirtualLayoutManager layoutManager,int headLayoutId, GoodsLikeBean.DataBean bean) {
        super(layoutManager,false);
        mContext = context;
        mHeadLayoutId = headLayoutId;
        initChildAdapter(bean);
    }


    private int mHeadLayoutId;
    private GridLayoutHelper mShopGridLayoutHelp;
    private CommonAdapter mTopTipAdapter;
    private ShopItemAdapter mItemAdpater;

    /**
     * 初始化头部视图
     */
    public abstract void initHeadView(CommonHolder holder, int position);

    private void initChildAdapter(GoodsLikeBean.DataBean bean) {
        if (mHeadLayoutId != 0){
            mTopTipAdapter = new CommonAdapter(new LinearLayoutHelper(),mHeadLayoutId,1){
                @Override
                public void onBindViewHolder(CommonHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    initHeadView(holder,position);
                }
            };
            addAdapter(mTopTipAdapter);
        }


        mShopGridLayoutHelp = new GridLayoutHelper(2);
        mShopGridLayoutHelp.setHGap(10);
        mShopGridLayoutHelp.setVGap(10);
        mShopGridLayoutHelp.setAutoExpand(false);
        mItemAdpater = new ShopItemAdapter<GoodsLikeBean.DataBean.ListBean>(mShopGridLayoutHelp,R.layout.item_shop,null){
            @Override
            public void onBindViewHolder(ShopItemViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (getData() == null || getData().size() == 0 || position >= getData().size()) return;
                final GoodsLikeBean.DataBean.ListBean bean = getData().get(position);
                holder.mImgTagShop.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(GONE);
                // VERT
                Glide.with(mContext).load(bean.getImg()).into(holder.mImgVertShop);
                holder.mTvVertShopGroupNumber.setText("已售" + String.valueOf(bean.getSale())+"件");
                holder.mTvVertShopName.setText(bean.getName());
                holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
                if (bean.getType() == 2) {
                    //租赁商品
                    holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
                    holder.mImgTagShop.setImageResource(R.mipmap.ic_nor_tagfree);
                    holder.mImgTagShop.setVisibility(View.VISIBLE);
                }else if (bean.getType() == 1){
                    //拼团商品
                    holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
                    holder.mImgTagShop.setImageResource(R.mipmap.ic_nor_taggroup);
                    holder.mImgTagShop.setVisibility(View.VISIBLE);
                }else if (bean.getType() == 3){
                    //折扣
                    holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
                    holder.mImgTagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);
                    holder.mImgTagShop.setVisibility(View.VISIBLE);
                }else if (bean.getType() == 4){
                    //积分
                    holder.mTvVertShopPrice.setText(bean.getPrice() + "积分");
                    holder.mImgTagShop.setImageResource(R.mipmap.ic_nor_tagintegral);
                    holder.mImgTagShop.setVisibility(View.VISIBLE);
                }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                    //限时抢购
                    holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
                    holder.mImgTagShop.setImageResource(R.mipmap.ic_nor_tagtimekill);
                    holder.mImgTagShop.setVisibility(View.VISIBLE);
                }else{
                    holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
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
        mItemAdpater.setData(bean.getList());
        addAdapter(mItemAdpater);
    }
    class CommonAdapter extends DelegateAdapter.Adapter<CommonHolder> {
        private LayoutHelper mLayoutHelper;
        private int mCount = 0;
        private int mLayoutId;

        public CommonAdapter( LayoutHelper layoutHelper,int layoutId, int count){
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            mLayoutId = layoutId;
        }
        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public CommonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CommonHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(CommonHolder holder, int position) {}

        @Override
        protected void onBindViewHolderWithOffset(CommonHolder holder, int position, int offsetTotal) {}
        @Override
        public int getItemCount() {
            return mCount;
        }
    }
    /***
     * 普通holder
     */
    public class CommonHolder extends RecyclerView.ViewHolder {
        public CommonHolder(View itemView) {
            super(itemView);
        }
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    class ShopItemAdapter<T> extends DelegateAdapter.Adapter<ShopItemViewHolder> {
        private LayoutHelper mLayoutHelper;
        private int mLayoutId;
        private List<T> mDatas;

        public List<T> getData(){
            return mDatas;
        }

        public void setData(List<T> data){
            mDatas = data;
        }

        public void addData(List<T> data){
            mDatas.addAll(data);
        }

        public ShopItemAdapter( LayoutHelper layoutHelper,int layoutId, List<T> datas) {
            this.mLayoutHelper = layoutHelper;
            mDatas = datas;
            mLayoutId = layoutId;
        }
        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public int getItemViewType(int position) {
            return Constant.SHOP_TYPE.COMMON;
        }

        @Override
        public ShopItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShopItemViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(ShopItemViewHolder holder, int position) {}

        @Override
        protected void onBindViewHolderWithOffset(ShopItemViewHolder holder, int position, int offsetTotal) {}
        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }
    }
    /**
     * 商品item holder
     */
    public class ShopItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.is_img_shop)
        ImageView mImgShop;
        @Bind(R.id.is_vert_img_shop)
        ImageView mImgVertShop;

        @Bind(R.id.is_hor_shop_name)
        TextView mTvShopName;

        @Bind(R.id.is_vert_shop_name)
        TextView mTvVertShopName;

        @Bind(R.id.is_hor_shop_price)
        TextView mTvShopPrice;

        @Bind(R.id.is_vert_shop_price)
        TextView mTvVertShopPrice;

        @Bind(R.id.is_hor_groupnumber)
        TextView mTvShopGroupNumber;

        @Bind(R.id.is_vert_shop_groupnumber)
        TextView mTvVertShopGroupNumber;

        @Bind(R.id.is_vert_img_tagshop)
        ImageView mImgTagShop;

        public ShopItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    public void setItemList(GoodsLikeBean.DataBean bean){
        mItemAdpater.setData(bean.getList());
        mItemAdpater.notifyDataSetChanged();
    }
    public void addItemList(GoodsLikeBean.DataBean bean){
        mItemAdpater.addData(bean.getList());
        mItemAdpater.notifyDataSetChanged();
    }

}
