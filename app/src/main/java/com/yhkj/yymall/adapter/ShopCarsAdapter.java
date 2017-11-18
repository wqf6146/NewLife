package com.yhkj.yymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.ShopListActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.ShopCarBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/3.
 */

public class ShopCarsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private List<HashMap<String, Object>> allList;
    private String checked;
    private Call call;
    private ShopCarBean.DataBean dataBean;
    private List<String> shopidList;
    private boolean compileBl = false;
    private DecimalFormat mTwoPointFormat = new java.text.DecimalFormat("#0.00");

    public ShopCarsAdapter(List<HashMap<String, Object>> allList, Context context, Call call, ShopCarBean.DataBean dataBean) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.allList = allList;
        this.dataBean = dataBean;
        this.call = call;
    }

    public void setCompile(Boolean compileBls) {
        compileBl = compileBls;
    }

    public void setDate(List<HashMap<String, Object>> alllists, ShopCarBean.DataBean dataBean) {
        allList = alllists;
        this.dataBean = dataBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 10:
                view = mInflater.inflate(R.layout.expendlist_group, parent, false);
                return new ParentViewHolder(view);
            case 11:
                view = mInflater.inflate(R.layout.expendlist_item, parent, false);
                return new ChildViewHolder(view);
            case 12:
                view = mInflater.inflate(R.layout.item_lose, parent, false);
                return new LoseViewHolder(view);
            case 13:
                view = mInflater.inflate(R.layout.item_clear, parent, false);
                return new ClearViewHolder(view);
            default:
                view = mInflater.inflate(R.layout.expendlist_group, parent, false);
                return new ParentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 10:
                if (holder instanceof ParentViewHolder) {
                    ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                    parentViewHolder.bindView(allList.get(position), position);
                }
                break;
            case 11:
                if (holder instanceof ChildViewHolder) {
                    ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                    childViewHolder.bindView(allList.get(position), position);
                }
                break;

            case 12:
                if (holder instanceof LoseViewHolder) {
                    LoseViewHolder loseViewHolder = (LoseViewHolder) holder;
                    loseViewHolder.bindView(allList.get(position), position);
                }
                break;

            case 13:
                if (holder instanceof ClearViewHolder) {
                    ClearViewHolder clearViewHolder = (ClearViewHolder) holder;
                    clearViewHolder.bindView(position);
                }
                break;

        }
    }

    @Override
    public int getItemCount() {
        return allList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (int) allList.get(position).get("type");
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView txt;
        private ImageView imgs;

        public ParentViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            txt = (TextView) view.findViewById(R.id.txt);
            imgs = (ImageView) view.findViewById(R.id.imgs);
        }

        public void bindView(final HashMap<String, Object> hashMap, final int pos) {

            if (hashMap.get("checked").equals("0")) {
                imgs.setImageResource(R.mipmap.shopcar_null);
            } else if (hashMap.get("checked").equals("1")) {
                imgs.setImageResource(R.mipmap.shopcar_gou);
            }
            txt.setText(hashMap.get("name") + "");
            imgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopidList = new ArrayList<String>();
                    if (allList.get(pos).get("checked").equals("0")) {
                        checked = "1";
                    } else if (allList.get(pos).get("checked").equals("1")) {
                        checked = "0";
                    }
                    String id = allList.get(pos).get("id") + "";
                    for (int i = 0; i < dataBean.getSellers().size(); i++) {
                        String ids = dataBean.getSellers().get(i).getId() + "";
                        if (ids.equals(id)) {
                            for (int s = 0; s < dataBean.getSellers().get(i).getGoodses().size(); s++) {
                                String shopId = dataBean.getSellers().get(i).getGoodses().get(s).getId() + "";
                                shopidList.add(shopId);
                            }
                        }
                    }
                    String[] a = new String[shopidList.size()];
                    for (int d = 0; d < shopidList.size(); d++) {
                        a[d] = shopidList.get(d) + "";
                    }
                    if (call != null) {
                        call.send(checked, a);
                    }
                }
            });

        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView txt, tv_chopcar_content, tv_item_del,
                tv_chopcar_money, tv_chopcar_fakemoney, tv_shopcar_numb,
                tv_expenditem_sub, tv_expenditem_numb, tv_expenditem_add;
        private ImageView img_item, img_shopcar_item;
        private LinearLayout ll_expendlistitem, mLlDelete,ei_ll_gift;
        private RelativeLayout rl_expendlistitem;
        private NestFullListView ei_nestlistview;

        private int numbs;

        public ChildViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            txt = (TextView) view.findViewById(R.id.txt);
            tv_chopcar_content = (TextView) view.findViewById(R.id.tv_chopcar_content);
            tv_chopcar_money = (TextView) view.findViewById(R.id.tv_chopcar_money);
            tv_chopcar_fakemoney = (TextView) view.findViewById(R.id.tv_chopcar_fakemoney);
            tv_shopcar_numb = (TextView) view.findViewById(R.id.tv_shopcar_numb);
            ei_nestlistview = (NestFullListView)view.findViewById(R.id.ei_nestlistview);
            ei_ll_gift = (LinearLayout)view.findViewById(R.id.ei_ll_gift);
            img_item = (ImageView) view.findViewById(R.id.img_item);
            tv_expenditem_sub = (TextView) view.findViewById(R.id.tv_expenditem_sub);
            tv_expenditem_numb = (TextView) view.findViewById(R.id.tv_expenditem_numb);
            tv_expenditem_add = (TextView) view.findViewById(R.id.tv_expenditem_add);
            img_shopcar_item = (ImageView) view.findViewById(R.id.img_shopcar_item);
            ll_expendlistitem = (LinearLayout) view.findViewById(R.id.ll_expendlistitem);
            rl_expendlistitem = (RelativeLayout) view.findViewById(R.id.rl_expendlistitem);
            tv_item_del = (TextView) view.findViewById(R.id.tv_item_del);
            mLlDelete = (LinearLayout) view.findViewById(R.id.ll_dele_item);

        }

        public void bindView(final HashMap<String, Object> hashMap, final int pos) {
            numbs = (int) hashMap.get("nums");
            if (hashMap.get("checked").equals("0")) {
                img_item.setImageResource(R.mipmap.shopcar_null);
            } else if (hashMap.get("checked").equals("1")) {
                img_item.setImageResource(R.mipmap.shopcar_gou);
            }

            Glide.with(context).load(hashMap.get("img")).into(img_shopcar_item);
            if (compileBl) {
                ei_ll_gift.setVisibility(GONE);
                ll_expendlistitem.setVisibility(View.GONE);
                rl_expendlistitem.setVisibility(View.VISIBLE);
                tv_expenditem_numb.setText(numbs + "");
                tv_expenditem_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //减
                        if (numbs != 1) {
                            numbs--;
                            tv_expenditem_numb.setText(numbs + "");
                            amendNumb(hashMap.get("id") + "", numbs);
                        }
                    }
                });
                tv_expenditem_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //加
                        numbs++;
                        tv_expenditem_numb.setText(numbs + "");
                        amendNumb(hashMap.get("id") + "", numbs);
                    }
                });

                tv_item_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (call != null) {
                            String[] productIds = {hashMap.get("id") + ""};
                            call.del(productIds);
                        }
                    }
                });
            } else {
                ll_expendlistitem.setVisibility(View.VISIBLE);
                rl_expendlistitem.setVisibility(View.GONE);
                ll_expendlistitem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, CommodityDetailsActivity.class);
                        intent.putExtra("goodsId", hashMap.get("goodsId") + "");
                        context.startActivity(intent);
                    }
                });
                mLlDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (call != null) {
                            String[] productIds = {hashMap.get("id") + ""};
                            call.del(productIds);
                        }
                    }
                });

                final List<ShopCarBean.DataBean.SellersBean.GoodsesBean.GiftBean> giftBeen = (List<ShopCarBean.DataBean.SellersBean.GoodsesBean.GiftBean>)hashMap.get("gift");
                if (giftBeen !=null && giftBeen.size() > 0){
                    ei_ll_gift.setVisibility(View.VISIBLE);
                    ei_nestlistview.setAdapter(new NestFullListViewAdapter<ShopCarBean.DataBean.SellersBean.GoodsesBean.GiftBean>(R.layout.item_tv,giftBeen) {
                        @Override
                        public void onBind(int pos, final ShopCarBean.DataBean.SellersBean.GoodsesBean.GiftBean bean, NestFullViewHolder holder) {
                            holder.setText(R.id.it_tv_left,bean.getName());
                            holder.setText(R.id.it_tv_right,"x" + bean.getNums());

                        }
                    });
                    ei_nestlistview.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(NestFullListView parent, View view, int position) {
                            ShopCarBean.DataBean.SellersBean.GoodsesBean.GiftBean bean = giftBeen.get(position);
                            if (bean.getType() == 0) {
                                Intent intent = new Intent(context, CommodityDetailsActivity.class);
                                intent.putExtra("goodsId", bean.getGoodsId() + "");
                                context.startActivity(intent);
                            } else if (bean.getType() == 2) {
                                Intent intent = new Intent(context, LeaseDetailActivity.class);
                                intent.putExtra("id", bean.getGoodsId() + "");
                                context.startActivity(intent);
                            }else if(bean.getType() == 1){
                                Intent intent = new Intent(context, GrouponDetailsActivity.class);
                                intent.putExtra("goodsId", bean.getGoodsId() + "");
                                context.startActivity(intent);
                            }else if(bean.getType() == 3){
                                //折扣
                                Intent intent = new Intent(context, DiscountDetailsActivity.class);
                                intent.putExtra("goodsId", bean.getGoodsId() + "");
                                context.startActivity(intent);
                            }else if(bean.getType() == 4){
                                //积分
                                Intent intent = new Intent(context, IntegralDetailActivity.class);
                                intent.putExtra("id", bean.getGoodsId() + "");
                                context.startActivity(intent);
                            }
                        }
                    });
                }else{
                    ei_ll_gift.setVisibility(View.GONE);
                }
            }

            txt.setText(hashMap.get("name") + "");
            tv_chopcar_content.setText(hashMap.get("spec") + "");
            tv_chopcar_money.setText("¥ " + mTwoPointFormat.format(hashMap.get("sell_price")));
            tv_chopcar_fakemoney.setText("¥" + mTwoPointFormat.format(hashMap.get("marketPrice")));
            tv_chopcar_fakemoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_shopcar_numb.setText("×" + numbs);
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] a = {allList.get(pos).get("id") + ""};
                    if (allList.get(pos).get("checked").equals("0")) {
                        checked = "1";
                    } else if (allList.get(pos).get("checked").equals("1")) {
                        checked = "0";
                    }
                    if (call != null) {
                        call.send(checked, a);
                    }
                }
            });
        }

    }

    public class ClearViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ClearViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void bindView(final int pos) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> lists = new ArrayList<String>();
                    for (int i = 0; i < allList.size(); i++) {
                        if ((int) allList.get(i).get("type") == 12) {
                            lists.add(allList.get(i).get("id") + "");
                        }
                    }
                    String[] del = new String[lists.size()];
                    for (int i = 0; i < lists.size(); i++) {
                        del[i] = lists.get(i);
                    }
                    if (call != null) {
                        call.del(del);
                    }
                }
            });
        }
    }

    public class LoseViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private ImageView img_shopcarclear_item;
        private TextView tv_shopcarclear_txt, tv_chopcarclear_content, tv_chopcarclear_yrscj, tv_chopcarclear_ztk;

        public LoseViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            img_shopcarclear_item = (ImageView) view.findViewById(R.id.img_shopcarclear_item);
            tv_shopcarclear_txt = (TextView) view.findViewById(R.id.tv_shopcarclear_txt);
            tv_chopcarclear_content = (TextView) view.findViewById(R.id.tv_chopcarclear_content);
            tv_chopcarclear_yrscj = (TextView) view.findViewById(R.id.tv_chopcarclear_yrscj);
            tv_chopcarclear_ztk = (TextView) view.findViewById(R.id.tv_chopcarclear_ztk);
        }

        public void bindView(final HashMap<String, Object> hashMap, final int pos) {
            tv_shopcarclear_txt.setText(hashMap.get("name") + "");
            tv_chopcarclear_content.setText(hashMap.get("spec") + "");
            Glide.with(context).load(hashMap.get("img") + "").into(img_shopcarclear_item);
            tv_chopcarclear_yrscj.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (call != null) {
                        String[] id = {hashMap.get("goodsId") + ""};
                        call.collect(id);
                    }
                }
            });
            tv_chopcarclear_ztk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ShopListActivity.class);
                    intent.putExtra("name",String.valueOf(hashMap.get("categoryName")));
                    intent.putExtra("id",String.valueOf(hashMap.get("categoryId")));
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                    context.startActivity(intent);
                }
            });
        }
    }


    public interface Call {
        void send(String checked, String[] shopId);

        void del(String[] productIds);

        void collect(String[] id);
    }

    public void amendNumb(String productId, int numb) {
        YYMallApi.setCarNumb(context, productId, numb, new YYMallApi.ApiResult<CommonBean>(context) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CommonBean commonBean) {

            }
        });


    }

}
