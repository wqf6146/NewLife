package com.yhkj.yymall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.bumptech.glide.Glide;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.WebActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.LeaseClassifyBean;
import com.yhkj.yymall.bean.LeaseHotBean;
import com.yhkj.yymall.bean.ShopSelectBean;
import com.yhkj.yymall.event.LeaseSortEvent;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.popwindows.ShopFiddlePopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/26.
 */
public class LeaseAdapter extends DelegateAdapter {
    private int mStatusSpans = 2; //1-ver 2-horiz
    private Activity mContext;


    public void changeShowStatus(int statusspans){
        mStatusSpans = statusspans;
        mShopGridLayoutHelp1.setSpanCount(statusspans);
        mShopGridLayoutHelp2.setSpanCount(statusspans);
        mShopItemAdapter1.notifyDataSetChanged();
        mShopItemAdapter2.notifyDataSetChanged();
    }

    public int getStatusSpanc(){
        return mStatusSpans;
    }


    public LeaseAdapter(Activity context, final VirtualLayoutManager layoutManager){
        super(layoutManager,false);
        mContext = context;
        initChildAdapter();
    }

    //分类tab
    private CommonAdapter mLeaseClassify;

    private ShopTabViewAdapter mShopTabAdapter;
    private ShopItemAdapter mShopItemAdapter1;
    private ShopItemAdapter mShopItemAdapter2;
    private GridLayoutHelper mShopGridLayoutHelp1;
    private GridLayoutHelper mShopGridLayoutHelp2;
    private StickyLayoutHelper mStickyShopTabLayoutHelp;

    public StickyLayoutHelper getStickyShopTabLayoutHelp() {
        return mStickyShopTabLayoutHelp;
    }

    private void initChildAdapter() {
        mLeaseClassify = new CommonAdapter(new LinearLayoutHelper(),R.layout.item_lease_classify,1){
            @Override
            public void onBindViewHolder(CommonHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.findViewById(R.id.ilc_ll_1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,WebActivity.class);
                        intent.putExtra("title","租赁须知");
                        intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.LEASE_NEED);
                        mContext.startActivity(intent);
                    }
                });
                holder.itemView.findViewById(R.id.ilc_ll_2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,WebActivity.class);
                        intent.putExtra("title","租赁协议");
                        intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.LEASE_XIYI);
                        mContext.startActivity(intent);
                    }
                });
                holder.itemView.findViewById(R.id.ilc_ll_3).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,WebActivity.class);
                        intent.putExtra("title","服务保障");
                        intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.SERVERBZ);
                        mContext.startActivity(intent);
                    }
                });
                holder.itemView.findViewById(R.id.ilc_ll_4).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,WebActivity.class);
                        intent.putExtra("title","养护须知");
                        intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.YANGHUXUZI);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onBindViewHolder(CommonHolder holder, int position, List<Object> payloads) {
                super.onBindViewHolder(holder, position, payloads);
            }
        };
        addAdapter(mLeaseClassify);

        LinearLayoutHelper linearLayoutHelper =new LinearLayoutHelper();
        linearLayoutHelper.setMarginTop(CommonUtil.dip2px(mContext,10));
        addAdapter(new CommonAdapter(linearLayoutHelper,R.layout.item_common_title,1){
            @Override
            public void onBindViewHolder(CommonHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public void onBindViewHolder(CommonHolder holder, int position, List<Object> payloads) {
                super.onBindViewHolder(holder, position, payloads);
            }
        });

        mShopGridLayoutHelp1 = new GridLayoutHelper(mStatusSpans);
        mShopGridLayoutHelp1.setHGap(10);
        mShopGridLayoutHelp1.setVGap(10);
//        mShopGridLayoutHelp1.setBgColor(mContext.getResources().getColor(R.color.white));
        mShopGridLayoutHelp1.setAutoExpand(false);
        mShopGridLayoutHelp1.setMarginBottom(CommonUtil.dip2px(mContext,10));
        mShopItemAdapter1 = new ShopItemAdapter<LeaseHotBean.DataBean.HotlistBean>(mShopGridLayoutHelp1,R.layout.item_shop,null){
            @Override
            public void onBindViewHolder(ShopItemViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (getData() == null || getData().size() == 0  || position >= getData().size())  return;
                final LeaseHotBean.DataBean.HotlistBean bean = getData().get(position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                        intent.putExtra("id",String.valueOf(bean.getId()));
                        mContext.startActivity(intent);
                    }
                });

                if(mStatusSpans == Constant.SHOPITEM_ORI.VER){
                    holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(View.VISIBLE);
                    holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(GONE);
                    // VERT
                    Glide.with(mContext).load(bean.getImg()).into(holder.mImgVertShop);
                    holder.mTvVertShopGroupNumber.setText(String.format(mContext.getString(R.string.getpeoplecount),bean.getSale()));
                    holder.mTvVertShopName.setText(bean.getName());
                    holder.mTvVertShopPrice.setText(String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                }else{
                    holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(GONE);
                    holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(View.VISIBLE);
                    // HOR
                    Glide.with(mContext).load(bean.getImg()).into(holder.mImgShop);
                    holder.mTvShopGroupNumber.setText(String.format(mContext.getString(R.string.getpeoplecount),bean.getSale()));
                    holder.mTvShopName.setText(bean.getName());
                    holder.mTvShopPrice.setText(String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                }
            }

            @Override
            public void onBindViewHolder(ShopItemViewHolder holder, int position, List<Object> payloads) {
                super.onBindViewHolder(holder, position, payloads);
            }
        };
        addAdapter(mShopItemAdapter1);


        mStickyShopTabLayoutHelp = new StickyLayoutHelper(true);
        mShopTabAdapter = new ShopTabViewAdapter(mStickyShopTabLayoutHelp,R.layout.item_shop_tab,1){
            @Override
            public void onBindViewHolder(final ShopTabViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (mLeaseAttrsBean == null || mLeaseAttrsBean.size() == 0) return;
                holder.mTvSynthesize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initTabColor(holder,0);
                        BusFactory.getBus().post(new LeaseSortEvent(Constant.TYPE_SELECT.OTHER,"all","asc","1","6",null,null,null));
                    }
                });
                holder.mTvSales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initTabColor(holder,1);
                        BusFactory.getBus().post(new LeaseSortEvent(Constant.TYPE_SELECT.OTHER,"sale","desc","1","6",null,null,null));
                    }
                });
                holder.mLlPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initTabColor(holder,2);
                        // 2-bottom 1-top
                        BusFactory.getBus().post(new LeaseSortEvent(Constant.TYPE_SELECT.OTHER,"price",holder.mPriceSelectStatus == 2 ? "desc" : "asc","1","6",null,null,null));
                    }
                });
                holder.mLlFiltrate.setVisibility(GONE);

                if (!mLeaseAttrsBean.get(0).getId().equals("-1")){
                    for (int i=mLeaseAttrsBean.size()-1; i>=3; i--){
                        mLeaseAttrsBean.remove(i);
                    }
                    mLeaseAttrsBean.add(0,buildBrandBean(mLeaseBrandBean));
                    holder.mFlowList.setTag(mLeaseAttrsBean.size());
                }
                //补齐 4个
                if (mLeaseAttrsBean.size() < 4){
                    int bs = 4 - mLeaseAttrsBean.size();
                    for (int i=0 ;i<bs; i++)
                        mLeaseAttrsBean.add(null);
                }
//                holder.mFlowList.setTag();
                if ( mSrcName == null ) mSrcName = new String[mLeaseAttrsBean.size()];
                holder.mFlowList.setAdapter(new NestFullListViewAdapter<LeaseClassifyBean.DataBean.AttrsBean>(R.layout.item_flow_item,mLeaseAttrsBean) {
                    @Override
                    public void onBind(int pos, final LeaseClassifyBean.DataBean.AttrsBean bean, NestFullViewHolder nestFullViewHolder) {
                        if (pos < (Integer)holder.mFlowList.getTag()) {
                            if (TextUtils.isEmpty(mSrcName[pos])) mSrcName[pos] = bean.getName();
                            if (bean.getName().equals("全部")) {
                                String name = mSrcName[pos];
                                int count = CommonUtil.getChineseCount(name);
                                nestFullViewHolder.setText(R.id.ifi_tv_tag,  count > 4 ? name.substring(0,4) + ".." : name);
                            }else {
                                int count = CommonUtil.getChineseCount(bean.getName());
                                nestFullViewHolder.setText(R.id.ifi_tv_tag, count > 4 ? bean.getName().substring(0,4) + ".." : bean.getName());
                            }
                        } else{
                            nestFullViewHolder.setVisible(R.id.ifi_ll_bg,false);
                        }
                    }
                });
                holder.mFlowList.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(NestFullListView parent, final View view, final int position) {
                        showTabSelectPopView(view, (TextView) view.findViewById(R.id.ifi_tv_tag),
                                (ImageView) view.findViewById(R.id.ifi_img_arrow),
                                (LeaseClassifyBean.DataBean.AttrsBean) holder.mFlowList.getAdapter().getDatas().get(position), new ShopFiddlePopView.OnCallBack() {
                                    @Override
                                    public void onSelectResString(String select) {
                                        mLeaseAttrsBean.get(position).setName(select);
//                                        ((TextView)view.findViewById(R.id.ifi_tv_tag)).setText(select);
                                    }

                                    @Override
                                    public void onStartSelect(int type, String order, String by, String page, String limit, String brand, String key, String value) {
                                        BusFactory.getBus().post(new LeaseSortEvent(type,order,by,page,limit,brand,key,value));
                                    }
                                });
                    }
                });
            }
        };
        addAdapter(mShopTabAdapter);

        mShopGridLayoutHelp2 = new GridLayoutHelper(mStatusSpans);
        mShopGridLayoutHelp2.setHGap(10);
        mShopGridLayoutHelp2.setVGap(10);
        mShopGridLayoutHelp2.setAutoExpand(false);
        mShopItemAdapter2 = new ShopItemAdapter<ShopSelectBean.DataBean.ListBean>(mShopGridLayoutHelp2,R.layout.item_shop,null){
            @Override
            public void onBindViewHolder(ShopItemViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (getData() == null || getData().size() == 0 || position >= getData().size()) return;
                final ShopSelectBean.DataBean.ListBean bean = getData().get(position);
                if(mStatusSpans == Constant.SHOPITEM_ORI.VER){
                    holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(View.VISIBLE);
                    holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(GONE);
                    // VERT

                    Glide.with(mContext).load(bean.getPic()).into(holder.mImgVertShop);
                    holder.mTvVertShopGroupNumber.setText(String.format(mContext.getString(R.string.getpeoplecount),String.valueOf(bean.getSale())));
                    holder.mTvVertShopName.setText(bean.getName());
                    holder.mTvVertShopPrice.setText(String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                }else{
                    holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(GONE);
                    holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(View.VISIBLE);
                    // HOR
                    Glide.with(mContext).load(bean.getPic()).into(holder.mImgShop);
                    holder.mTvShopGroupNumber.setText(String.format(mContext.getString(R.string.getpeoplecount),String.valueOf(bean.getSale())));
                    holder.mTvShopName.setText(bean.getName());
                    holder.mTvShopPrice.setText(String.format(mContext.getString(R.string.leaseprice),bean.getPrice()));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                        intent.putExtra("id",String.valueOf(bean.getId()));
                        mContext.startActivity(intent);
                    }
                });
            }
        };
        addAdapter(mShopItemAdapter2);
    }


    private String[] mSrcName;

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

    class ShopTabViewAdapter extends DelegateAdapter.Adapter<ShopTabViewHolder> {
        private LayoutHelper mLayoutHelper;
        private int mCount = 0;
        private int mLayoutId;

        public ShopTabViewAdapter( LayoutHelper layoutHelper,int layoutId, int count) {
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            mLayoutId = layoutId;
        }
        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public ShopTabViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ShopTabViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(ShopTabViewHolder holder, int position) {}

        @Override
        protected void onBindViewHolderWithOffset(ShopTabViewHolder holder, int position, int offsetTotal) {}
        @Override
        public int getItemCount() {
            return mCount;
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


        public ShopItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }

    /**
     * tab holder
     */
    public class ShopTabViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgUp;
        ImageView mImgDown;
        TextView mTvPrice;
        TextView mTvSales;
        TextView mTvSynthesize;
        LinearLayout mLlFiltrate;
        LinearLayout mLlPrice;
        NestFullListView mFlowList;

        final int mPinkColor = Color.rgb(251,30,145);
        final int mGrayColor = Color.rgb(114,112,112);
        int mPriceSelectStatus = 1;

        TextView[] mTabViews = new TextView[4];

        public ShopTabViewHolder(View itemView) {
            super(itemView);
            mImgUp = (ImageView) itemView.findViewById(R.id.as_arrow_up);
            mImgDown = (ImageView) itemView.findViewById(R.id.as_arrow_down);
            mTvPrice = (TextView) itemView.findViewById(R.id.as_tv_price);
            mTvSales = (TextView)itemView.findViewById(R.id.as_tv_sales);
            mTvSynthesize = (TextView)itemView.findViewById(R.id.as_tv_synthesize);
            mLlFiltrate = (LinearLayout)itemView.findViewById(R.id.as_ll_filtrate);
            mLlPrice = (LinearLayout)itemView.findViewById(R.id.as_ll_price);
            mFlowList = (NestFullListView)itemView.findViewById(R.id.ist_flowlistview);
            mTabViews[0] = mTvSynthesize;
            mTabViews[1] = mTvSales;
            mTabViews[2] = mTvPrice;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            ViseLog.e("ShopTabFinalize");
        }
    }
    //设置tab选择ui
    private void initTabColor(ShopTabViewHolder holder, int index){
        holder.mTvPrice.setTextColor(holder.mGrayColor);
        holder.mTvSales.setTextColor(holder.mGrayColor);
        holder.mTvSynthesize.setTextColor(holder.mGrayColor);
        holder.mImgDown.setImageResource(R.mipmap.arraw_bottom);
        holder.mImgUp.setImageResource(R.mipmap.arraw_top);
        if (index >=0 && index<=3){
            holder.mTabViews[index].setTextColor(holder.mPinkColor);
            if (index == 2){
                if (holder.mPriceSelectStatus == 1){
                    holder.mPriceSelectStatus = 2;
                    holder.mImgDown.setImageResource(R.mipmap.pink_arraw_bottom);
                    holder.mImgUp.setImageResource(R.mipmap.arraw_top);
                }else{
                    holder.mPriceSelectStatus = 1;
                    holder.mImgDown.setImageResource(R.mipmap.arraw_bottom);
                    holder.mImgUp.setImageResource(R.mipmap.pink_arrow_top);
                }
            }
        }
    }

    /***
     * 普通holder
     */
    class CommonHolder extends RecyclerView.ViewHolder {
        public CommonHolder(View itemView) {
            super(itemView);
        }
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }


    /**
     *  更新热门领用数据
     */
    public void setLeaseHotInfo(List<LeaseHotBean.DataBean.HotlistBean> hotLeaseBean){
        mShopItemAdapter1.setData(hotLeaseBean);
        mShopItemAdapter1.notifyDataSetChanged();
    }
    /**
     *
     */
    private List<LeaseClassifyBean.DataBean.AttrsBean> mLeaseAttrsBean;
    private List<LeaseClassifyBean.DataBean.BrandBean> mLeaseBrandBean;
    public void setLeaseTabInfoBean(LeaseClassifyBean.DataBean leaseSelListBean){
        mLeaseAttrsBean = leaseSelListBean.getAttrs();
        mLeaseBrandBean = leaseSelListBean.getBrand();
//        mShopTabAdapter.notifyDataSetChanged();

    }
    public void setLeaseSelList(ShopSelectBean.DataBean leaseSelListBean){
        mShopItemAdapter2.setData(leaseSelListBean.getList());
        mShopItemAdapter2.notifyDataSetChanged();
    }
    public void addLeaseSelList(ShopSelectBean.DataBean leaseSelListBean){
        mShopItemAdapter2.addData(leaseSelListBean.getList());
        mShopItemAdapter2.notifyDataSetChanged();
    }


    //-----------------------
    private void showTabSelectPopView(final View view,final TextView tvTag,final ImageView arrow,final LeaseClassifyBean.DataBean.AttrsBean bean
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

        ShopFiddlePopView shopFiddlePopView = new ShopFiddlePopView<LeaseClassifyBean.DataBean.AttrsBean>(mContext,bean){
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
}
