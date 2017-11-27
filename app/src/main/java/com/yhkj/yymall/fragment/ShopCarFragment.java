package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.yhkj.yymall.BaseToolBarFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CheckOutActivity;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DailyDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;
import com.yhkj.yymall.adapter.ShopCarsAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GoodsLikeBean;
import com.yhkj.yymall.bean.ShopCarBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import com.yhkj.yymall.view.YiYaHeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/6/19.
 */

public class ShopCarFragment extends BaseToolBarFragment {
    private List<HashMap<String, Object>> allList;
    private List<HashMap<String, Object>> shopList;
    private HashMap<String, Object> hashMap;
    @Bind(R.id.fsc_refreshlayout)
    SmartRefreshLayout fsc_refreshlayout;

    @Bind(R.id.rv_shopcar_gid)
    RecyclerView rv_shopcar_gid;

    @Bind(R.id.ll_shopcar)
    LinearLayout ll_shopcar;

    @Bind(R.id.tv_shopcar_dosing)
    TextView tv_shopcar_dosing;

    @Bind(R.id.img_chopcar_all)
    ImageView img_chopcar_all;

    @Bind(R.id.tv_shopcar_money)
    TextView tv_shopcar_money;

    @Bind(R.id.tv_shopcar_jsmoney)
    TextView tv_shopcar_jsmoney;

    @Bind(R.id.rl_shopcar)
    RelativeLayout rl_shopcar;

    @Bind(R.id.tv_shopcar_yrscj)
    TextView tv_shopcar_yrscj;

    @Bind(R.id.rl_shopcar_pay)
    RelativeLayout rl_shopcar_pay;

    private Intent intent;
    private ShopCarsAdapter mAdapter;
    private boolean aBoolean = true;
    private boolean compileBl = false;
    private List<String> listId;
    private List<String> delshopList;
    private String[] collectid;
    private int type = 0;
    private int mCurPage = 1;

    private CommonAdapter<GoodsLikeBean.DataBean.ListBean> shopNullEntiryAdapter;
    private HeaderAndFooterWrapper mShopNullAdapter;
    private ShopCarsAdapter.Call call = new ShopCarsAdapter.Call() {
        @Override
        public void send(String checked, String[] shopId) {
            type = 0;
            YYMallApi.setShopCarSelect(_mActivity, shopId, checked, apiCallback);
        }

        @Override
        public void del(String[] productIds) {
            type = 1;
            YYMallApi.removeCar(mContext, productIds, apiCallback);
        }

        @Override
        public void collect(String[] id) {
            collectid = id;
            type = 3;
            YYMallApi.collectAddCollect(mContext, collectid, apiCallback);
        }
    };

    private ApiCallback<ShopCarBean.DataBean> apiCallback = new YYMallApi.ApiResult<ShopCarBean.DataBean>(_mActivity) {
        @Override
        public void onStart() {

        }

        @Override
        public void onError(ApiException e) {
            super.onError(e);
            fsc_refreshlayout.finishRefresh();
            showToast(e.getMessage());
            setNetWorkErrShow(VISIBLE);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(final ShopCarBean.DataBean dataBean) {
            setNetWorkErrShow(GONE);
            fsc_refreshlayout.finishRefresh();
            if (type == 1) {
                showToast("删除成功");
            } else if (type == 2) {
                showToast("收藏成功");
                String id[] = new String[delshopList.size()];
                for (int i = 0; i < delshopList.size(); i++) {
                    id[i] = delshopList.get(i);
                }
                type = 0;
                YYMallApi.removeCar(mContext, id, apiCallback);
                return;
            } else if (type == 3) {
                showToast("收藏成功");
                type = 0;
                YYMallApi.removeCar(mContext, collectid, apiCallback);
                return;
            }

            if (dataBean.getSellers().size() == 0 && dataBean.getInvalid().size() == 0) {
                setTitleTvRightText("");
//                tv_shopcar_dosing.setText("结算 (" + dataBean.getCheckedCount() + ")");
//                tv_shopcar_yrscj.setVisibility(GONE);
//                shopCarsNullAdapter = null;
                fsc_refreshlayout.setLoadmoreFinished(false);
                fsc_refreshlayout.setEnableLoadmore(true);
                if (shopNullEntiryAdapter == null)
                    YYMallApi.getGoodsLike(getActivity(), mCurPage = 1, false, nulApiCallback);
                aBoolean = true;
                rl_shopcar_pay.setVisibility(GONE);
                ll_shopcar.setVisibility(GONE);
                compileBl = false;
            } else {
                shopNullEntiryAdapter = null;
                fsc_refreshlayout.setEnableLoadmore(false);
                rl_shopcar_pay.setVisibility(View.VISIBLE);
                if (!compileBl) {
                    setTitleTvRightText("编辑");
                    tv_shopcar_dosing.setText("结算 (" + dataBean.getCheckedCount() + ")");
                    tv_shopcar_yrscj.setVisibility(GONE);
                    ll_shopcar.setVisibility(View.VISIBLE);
                }
                allList = new ArrayList<>();
                shopList = new ArrayList<>();
                for (int i = 0; i < dataBean.getSellers().size(); i++) {
                    hashMap = new HashMap<>();
                    hashMap.put("id", dataBean.getSellers().get(i).getId() + "");
                    hashMap.put("name", dataBean.getSellers().get(i).getName());
                    hashMap.put("checked", dataBean.getSellers().get(i).getChecked() + "");
                    hashMap.put("type", 10);
                    allList.add(hashMap);
                    for (int a = 0; a < dataBean.getSellers().get(i).getGoodses().size(); a++) {
                        hashMap = new HashMap<>();
                        ShopCarBean.DataBean.SellersBean.GoodsesBean goodsesBean = dataBean.getSellers().get(i).getGoodses().get(a);
                        hashMap.put("id", goodsesBean.getId() + "");
                        hashMap.put("name", goodsesBean.getName());
                        hashMap.put("spec", goodsesBean.getSpec());
                        hashMap.put("price", goodsesBean.getPrice());
                        hashMap.put("sell_price", goodsesBean.getSell_price());
                        hashMap.put("marketPrice", goodsesBean.getMarketPrice());
                        hashMap.put("nums", goodsesBean.getNums());
                        hashMap.put("img", goodsesBean.getImg());
                        hashMap.put("checked", goodsesBean.getChecked() + "");
                        hashMap.put("goodsId", goodsesBean.getGoodsId() + "");
                        hashMap.put("gift",goodsesBean.getGift());
                        hashMap.put("type", 11);
                        allList.add(hashMap);
                        shopList.add(hashMap);
                    }
                }
                if (dataBean.getInvalid() != null && dataBean.getInvalid().size() > 0) {
                    for (int i = 0; i < dataBean.getInvalid().size(); i++) {
                        hashMap = new HashMap<>();
                        hashMap.put("id", dataBean.getInvalid().get(i).getId());
                        hashMap.put("name", dataBean.getInvalid().get(i).getName());
                        hashMap.put("spec", dataBean.getInvalid().get(i).getSpec());
                        hashMap.put("goodsId", dataBean.getInvalid().get(i).getGoodsId());
                        hashMap.put("img", dataBean.getInvalid().get(i).getImg());
                        hashMap.put("categoryId", dataBean.getInvalid().get(i).getCategoryId());
                        hashMap.put("categoryName", dataBean.getInvalid().get(i).getCategoryName());
                        hashMap.put("type", 12);
                        allList.add(hashMap);
                    }
                    HashMap hashMaps = new HashMap();
                    hashMaps.put("type", 13);
                    allList.add(hashMaps);
                }
                rv_shopcar_gid.setLayoutManager(new LinearLayoutManager(getActivity()));
                if (aBoolean) {
                    mAdapter = new ShopCarsAdapter(allList, getActivity(), call, dataBean);
                    rv_shopcar_gid.setAdapter(mAdapter);
                    aBoolean = false;
                } else {
                    mAdapter.setDate(allList, dataBean);
                    mAdapter.notifyDataSetChanged();
                }
                if (dataBean.getAllChecked() == 0) {
                    img_chopcar_all.setImageResource(R.mipmap.shopcar_null);
                    img_chopcar_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> idArr = new ArrayList<>();
                            for (int i = 0; i < allList.size(); i++) {
                                if ((Integer)allList.get(i).get("type") == 10 || (Integer)allList.get(i).get("type") == 11){
                                    idArr.add(allList.get(i).get("id") + "");
                                }
                            }
                            if (idArr.size() == 0){
                                showToast("无可选商品");
                                return;
                            }
                            type = 0;
                            YYMallApi.setShopCarSelect(_mActivity, idArr.toArray(new String[idArr.size()]), "1", apiCallback);
                        }
                    });

                } else if (dataBean.getAllChecked() == 1) {
                    img_chopcar_all.setImageResource(R.mipmap.shopcar_gou);
                    img_chopcar_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> idArr = new ArrayList<>();
                            for (int i = 0; i < allList.size(); i++) {
                                if ((Integer)allList.get(i).get("type") == 10 || (Integer)allList.get(i).get("type") == 11){
                                    idArr.add(allList.get(i).get("id") + "");
                                }
                            }
                            if (idArr.size() == 0){
                                showToast("无可选商品");
                                return;
                            }
                            type = 0;
                            YYMallApi.setShopCarSelect(_mActivity, idArr.toArray(new String[idArr.size()]), "0", apiCallback);
                        }
                    });
                }
                java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
                tv_shopcar_money.setText("合计：¥" + df.format(dataBean.getTotal()));
//                tv_shopcar_jsmoney.setText("为您节省" + dataBean.getDiffTotal() + "元");

                if (compileBl) {
                    if (dataBean.getCheckedCount() != 0)
                        tv_shopcar_dosing.setText("删除 ("+ dataBean.getCheckedCount() +")");
                    else
                        tv_shopcar_dosing.setText("删除");
                    final List<String> listId = new ArrayList<String>();
                    for (int i = 0; i < allList.size(); i++) {
                        if ((allList.get(i).get("checked") + "").equals("1")) {
                            listId.add(allList.get(i).get("id") + "");
                        }
                    }
                    if (listId.size() != 0) {
                        tv_shopcar_dosing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id[] = new String[listId.size()];
                                for (int i = 0; i < listId.size(); i++) {
                                    id[i] = listId.get(i);
                                }
                                type = 1;
                                YYMallApi.removeCar(mContext, id, apiCallback);
                            }
                        });

                    }

                } else {
                    if (dataBean.getCheckedCount() == 0) {
                        tv_shopcar_dosing.setText("结算");
                        tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#D4D4D4"));
                        tv_shopcar_dosing.setClickable(false);
                    } else {
                        tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#007cd1"));
                        tv_shopcar_dosing.setText("结算 (" + dataBean.getCheckedCount() + ")");
                        tv_shopcar_dosing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent = new Intent(getActivity(), CheckOutActivity.class);
                                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.SHOPCAR);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
            listId = new ArrayList<String>();
            delshopList = new ArrayList<>();
            if (shopList != null && shopList.size() > 0) {
                for (int i = 0; i < shopList.size(); i++) {
                    if ((shopList.get(i).get("checked") + "").equals("1")) {
                        listId.add(shopList.get(i).get("goodsId") + "");
                        delshopList.add(shopList.get(i).get("id") + "");
                    }
                }
            }


            if (compileBl) {
//                setTitleTvRightText("完成");
                tv_shopcar_yrscj.setVisibility(View.VISIBLE);
                tv_shopcar_yrscj.setTextColor(Color.parseColor("#FF0000"));
                if (listId.size() != 0) {
                    tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#FF0000"));
                    tv_shopcar_dosing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id[] = new String[listId.size()];
                            for (int i = 0; i < listId.size(); i++) {
                                id[i] = delshopList.get(i);
                            }
                            type = 1;
                            YYMallApi.removeCar(mContext, id, apiCallback);
                        }
                    });
                    tv_shopcar_yrscj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id[] = new String[listId.size()];
                            for (int i = 0; i < listId.size(); i++) {
                                id[i] = listId.get(i);
                            }
                            type = 2;
                            YYMallApi.collectAddCollect(mContext, id, apiCallback);
                        }
                    });

                } else {
                    tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#D4D4D4"));
                    tv_shopcar_yrscj.setTextColor(Color.parseColor("#D4D4D4"));
                    tv_shopcar_dosing.setClickable(false);
                    tv_shopcar_yrscj.setClickable(false);
                }

            } else {
                tv_shopcar_yrscj.setVisibility(GONE);
            }


            setTitleTvRightLisiten(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (compileBl) {
                        type = 0;
                        YYMallApi.getShopCar(_mActivity, YYApp.getInstance().getToken(),true, apiCallback);

                        tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#007cd1"));
                        tv_shopcar_dosing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                intent = new Intent(getActivity(), CheckOutActivity.class);
                                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.SHOPCAR);
                                startActivity(intent);
                            }
                        });
                        setTitleTvRightText("编辑");
                        rl_shopcar.setVisibility(View.VISIBLE);
                        compileBl = false;
                        mAdapter.setCompile(compileBl);
                        tv_shopcar_dosing.setText("结算 (" + dataBean.getCheckedCount() + ")");
                        tv_shopcar_yrscj.setVisibility(GONE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        setTitleTvRightText("完成");
                        rl_shopcar.setVisibility(GONE);
                        compileBl = true;
                        mAdapter.setCompile(compileBl);
                        tv_shopcar_yrscj.setVisibility(View.VISIBLE);
                        tv_shopcar_dosing.setText("删除 ("+ dataBean.getCheckedCount() +")");
                        if (listId.size() != 0) {
                            tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#FF0000"));
                            tv_shopcar_yrscj.setTextColor(Color.parseColor("#FF0000"));
                            tv_shopcar_dosing.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String id[] = new String[listId.size()];
                                    for (int i = 0; i < listId.size(); i++) {
                                        id[i] = delshopList.get(i);
                                    }
                                    type = 1;
                                    YYMallApi.removeCar(mContext, id, apiCallback);
                                }
                            });
                            tv_shopcar_yrscj.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String id[] = new String[listId.size()];
                                    for (int i = 0; i < listId.size(); i++) {
                                        id[i] = listId.get(i);
                                    }
                                    type = 2;
                                    YYMallApi.collectAddCollect(mContext, id, apiCallback);
                                }
                            });
                        } else {
                            tv_shopcar_dosing.setBackgroundColor(Color.parseColor("#D4D4D4"));
                            tv_shopcar_yrscj.setTextColor(Color.parseColor("#D4D4D4"));
                            tv_shopcar_dosing.setClickable(false);
                            tv_shopcar_yrscj.setClickable(false);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });


        }
    };

    private ApiCallback nulApiCallback = new ApiCallback<GoodsLikeBean.DataBean>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onError(ApiException e) {
            showToast(e.getMessage());
            setNetWorkErrShow(VISIBLE);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(GoodsLikeBean.DataBean dataBean) {
            setNetWorkErrShow(GONE);
            setTitleTvRightText("");
            if (shopNullEntiryAdapter == null) {
                rv_shopcar_gid.setLayoutManager(new GridLayoutManager(_mActivity,2));
                rv_shopcar_gid.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(_mActivity,1)));
                View view = LayoutInflater.from(_mActivity).inflate(R.layout.item_shopcarnull_top,rv_shopcar_gid,false);
                view.findViewById(R.id.ist_tv_specmall).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        BusFactory.getBus().post(new MainTabSelectEvent(0));
                    }
                });
                shopNullEntiryAdapter = new CommonAdapter<GoodsLikeBean.DataBean.ListBean>(getActivity(), R.layout.item_shop, dataBean.getList()){
                    @Override
                    protected void convert(ViewHolder holder, final GoodsLikeBean.DataBean.ListBean bean, int position) {
//                        holder.mImgTagShop.setVisibility(View.GONE);
//                        holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(View.VISIBLE);
//                        holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(GONE);
                        // VERT

//                        holder.mTvVertShopGroupNumber.setText("已售" + String.valueOf(bean.getSale())+"件");
//                        holder.mTvVertShopName.setText(bean.getName());
//                        holder.mTvVertShopPrice.setText("¥" + bean.getPrice());
                        holder.setVisible(R.id.is_vert_img_tagshop,false);
                        holder.setVisible(R.id.is_ll_vert,true);
                        holder.setVisible(R.id.fn_ll_hor,false);
                        Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView)holder.getView(R.id.is_vert_img_shop));
                        holder.setText(R.id.is_vert_shop_groupnumber,"已售" + String.valueOf(bean.getSale())+"件");
                        holder.setText(R.id.is_vert_shop_name,bean.getName());
                        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                        if (bean.getType() == 2) {
                            //租赁商品
                            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagfree);
                            holder.setVisible(R.id.is_vert_img_tagshop,true);
                        }else if (bean.getType() == 1){
                            //拼团商品
                            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_taggroup);
                            holder.setVisible(R.id.is_vert_img_tagshop,true);
                        }else if (bean.getType() == 3){
                            //折扣
                            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagdiscount);
                            holder.setVisible(R.id.is_vert_img_tagshop,true);
                        }else if (bean.getType() == 4){
                            //积分
                            holder.setText(R.id.is_vert_shop_price,bean.getPrice() + "积分");
                            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagintegral);
                            holder.setVisible(R.id.is_vert_img_tagshop,true);

                        }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                            //限时抢购
                            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
                            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagtimekill);
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
                mShopNullAdapter = new HeaderAndFooterWrapper(shopNullEntiryAdapter);
                mShopNullAdapter.addHeaderView(view);
                rv_shopcar_gid.setAdapter(mShopNullAdapter);
                mShopNullAdapter.notifyDataSetChanged();
            } else {
                int start = mShopNullAdapter.getItemCount();
                shopNullEntiryAdapter.addDatas(dataBean.getList());
//                mShopNullAdapter.notifyDataSetChanged();
                mShopNullAdapter.notifyItemRangeInserted(start,shopNullEntiryAdapter.getItemCount());
            }
        }
    };

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getDataEntry();
    }

    private void getData(boolean bShow) {
        if (!TextUtils.isEmpty(YYApp.getInstance().getToken())) {
            type = 0;
            YYMallApi.getShopCar(_mActivity, YYApp.getInstance().getToken(),bShow, apiCallback);
        } else {
            setTitleTvRightText("");
            rl_shopcar_pay.setVisibility(GONE);
            YYMallApi.getGoodsLike(getActivity(), 1, true, nulApiCallback);
        }
    }

    public static ShopCarFragment getInstance() {
        ShopCarFragment fragment = new ShopCarFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_shopcar;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setTvTitleText("购物车");
        setImgBackVisiable(View.INVISIBLE);
        setImgRightVisiable(View.INVISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        initRefreshLayout();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }


    private void initRefreshLayout() {
        fsc_refreshlayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        fsc_refreshlayout.setEnableOverScrollBounce(false);
        fsc_refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                type = 0;
                mCurPage = 1;
                refreshlayout.setLoadmoreFinished(false);
                shopNullEntiryAdapter = null;
                YYMallApi.getShopCar(_mActivity, YYApp.getInstance().getToken(),false, apiCallback);
            }
        });
        fsc_refreshlayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                YYMallApi.getGoodsLike(getActivity(), mCurPage + 1, false, new ApiCallback<GoodsLikeBean.DataBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        refreshlayout.finishLoadmore();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(GoodsLikeBean.DataBean dataBean) {
                        mCurPage++;
                        if (dataBean.getList() != null && dataBean.getList().size() > 0) {
                            refreshlayout.finishLoadmore();
                            int start = mShopNullAdapter.getItemCount();
                            shopNullEntiryAdapter.addDatas(dataBean.getList());
//                            mShopNullAdapter.notifyDataSetChanged();
                            mShopNullAdapter.notifyItemRangeInserted(start,shopNullEntiryAdapter.getItemCount());
                        } else {
                            refreshlayout.finishLoadmore();
                            refreshlayout.setLoadmoreFinished(true);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
            fsc_refreshlayout.setEnableRefresh(false);
            fsc_refreshlayout.setEnableLoadmore(true);
            fsc_refreshlayout.setLoadmoreFinished(false);
        }else{
            fsc_refreshlayout.setEnableRefresh(true);
            fsc_refreshlayout.setEnableLoadmore(false);
            fsc_refreshlayout.setLoadmoreFinished(true);
        }
        rl_shopcar.setVisibility(View.VISIBLE);
        compileBl = false;
        if (mAdapter != null) {
            mAdapter.setCompile(compileBl);
            mAdapter.notifyDataSetChanged();
        }
        tv_shopcar_yrscj.setVisibility(GONE);
    }

    /**
     * 焦点在当前页面时进行处理
     */

    private boolean mFirstLoad = true;
    private void getDataEntry(){
        if (mFirstLoad){
            mFirstLoad = false;
            getData(false);
        }else{
            getData(false);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getDataEntry();
    }
}
