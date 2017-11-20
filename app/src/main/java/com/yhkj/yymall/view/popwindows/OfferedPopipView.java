package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CheckOutActivity;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.EnableSpecBean;
import com.yhkj.yymall.bean.GrouponSubBean;
import com.yhkj.yymall.bean.OfferedDetailsBean;
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.bean.ShopSpecBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.NumberPickerView;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/11/18.
 */

public class OfferedPopipView extends BasePopupWindow {

    @Bind(R.id.vps_img_close)
    ImageView mImgClose;

    @Bind(R.id.vps_img_shop)
    ImageView mImgShop;

    @Bind(R.id.vps_tv_shopprice)
    TextView mTvShopPrice;

    @Bind(R.id.vps_tv_curselect)
    TextView mTvCurSelect;

    @Bind(R.id.vps_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.vps_tv_pay)
    TextView mTvCopyLeft;

    @Bind(R.id.vps_tv_lease)
    TextView mTvCopyRight;

    @Bind(R.id.vps_tv_inventory)
    TextView mTvInventory;

    @Bind(R.id.vps_fl_loading)
    FrameLayout mFlLoding;

    private View mPayNumbView;
    private String mGroupId;
    OfferedDetailsBean.DataBean mDataBean;
    private String mSelectNumb;
    private OfferedPopipView.OnShopCarResLisiten mOnShopCarResLisiten;
    private Integer mType; // null 0-左边 1-右边
    private java.text.DecimalFormat mTwoPointDf =new java.text.DecimalFormat("#0.00");
    private final int COLOR_SELECT = Color.parseColor("#ffffff");
    private final int COLOR_ENABLE = Color.parseColor("#525050");
    private final int COLOR_UNENABLE = Color.parseColor("#a1a1a1");
    public OfferedPopipView(Activity context, OfferedPopipView.OnShopCarResLisiten onShopCarResLisiten, OfferedDetailsBean.DataBean dataBean, Integer type,String groupId) {
        this(context,onShopCarResLisiten,dataBean,null,null,null,type,groupId);
    }
    public OfferedPopipView(Activity activity, OfferedPopipView.OnShopCarResLisiten onShopCarResLisiten,
                            OfferedDetailsBean.DataBean dataBean, LinkedHashMap specHashMap, HashMap selectPosHashMap, String numb, Integer type,String groupId){
        super(activity);
        mSelectSpecs = specHashMap;
        mOnShopCarResLisiten = onShopCarResLisiten;
        mSelectPosHashMap = selectPosHashMap;
        mDataBean = dataBean;
        mSelectNumb = numb;
        mType = type;
        mGroupId = groupId;
        setBottomShow();
        if (mSelectPosHashMap == null || mSelectPosHashMap.size() == 0)
            getSpecList();
        else
            init();
    }

    private void setBottomShow(){
        if (mType == null){
            mTvCopyLeft.setText("单独购买");
            mTvCopyRight.setText(mDataBean.getAllowNum() + "人团");
            return;
        }else{
            switch (mType){
                case 0:
                    mTvCopyLeft.setText("单独购买");
                    mTvCopyRight.setVisibility(GONE);
                    break;
                case 1:
                    mTvCopyRight.setText(mDataBean.getAllowNum() + "人团");
                    mTvCopyLeft.setVisibility(GONE);
                    break;
            }
        }
    }

    private void getSpecList(){
        YYMallApi.getEnableSpec(getContext(),String.valueOf(mDataBean.getGoods().getId()),null,false,new YYMallApi.ApiResult<EnableSpecBean.DataBean>(getContext()){
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(EnableSpecBean.DataBean dataBean) {
                mFlLoding.setVisibility(GONE);
                mEnableSpecList = dataBean.getList();
                init();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }
    private LinkedHashMap mSelectSpecs;
    private HashMap mSelectPosHashMap;
    NumberPickerView mNumbPickerView;
    private TextView mTvLimiteNumb;
    private CommonAdapter mCommonAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<OfferedDetailsBean.DataBean.GoodsBean.SpecBean> mAllSpecBean;
    private String mCurPriceRange;
    private void init() {
        initShopSpec();
        mAllSpecBean = mDataBean.getGoods().getSpec();
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCommonAdapter = new CommonAdapter<OfferedDetailsBean.DataBean.GoodsBean.SpecBean>(getContext(),
                R.layout.item_shopcar_standard,mAllSpecBean) {
            @Override
            protected void convert(ViewHolder holder, OfferedDetailsBean.DataBean.GoodsBean.SpecBean bean, final int position) {
                holder.setText(R.id.iss_tv_key,bean.getName());
                TagFlowLayout tagFlowLayout = holder.getView(R.id.iss_flowlayout);
                tagFlowLayout.setTag(bean.getId());
                Object object = mSelectPosHashMap == null ? null : mSelectPosHashMap.get(bean.getId());
                if (object != null) {
                    tagFlowLayout.setTag(R.id.item_attr_select,object);
                }
                tagFlowLayout.setMaxSelectCount(1);

                if (mEnableSpecList!=null){
                    for (int i=0;i<mEnableSpecList.size();i++){
                        EnableSpecBean.DataBean.ListBean enableBean = mEnableSpecList.get(i);
                        if (bean.getId().equals(enableBean.getId())){
//                            bean.setSelectdone(1);
                            for (int j=0;j<bean.getValue().size();j++){
                                OfferedDetailsBean.DataBean.GoodsBean.SpecBean.ValueBean tagBean = bean.getValue().get(j);
                                //先置状态灰色、确认有库存再点亮
                                tagBean.setEnable(-1);
                                for (int p=0;p<enableBean.getValue().size();p++){
                                    EnableSpecBean.DataBean.ListBean.ValueBean valueBean = enableBean.getValue().get(p);
                                    if (tagBean.getName().equals(valueBean.getName())){
                                        tagBean.setEnable(1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                tagFlowLayout.setAdapter(new TagAdapter<OfferedDetailsBean.DataBean.GoodsBean.SpecBean.ValueBean>(bean.getValue()) {
                    @Override
                    public View getView(final FlowLayout parent, final int pos, final OfferedDetailsBean.DataBean.GoodsBean.SpecBean.ValueBean bean) {
                        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_tv, parent, false);
                        tv.setText(bean.getName());

                        //找到是否在可选择列表里 找到则置可选状态
                        if (bean.getEnable() != -1){
                            tv.setTag(true);
                            if (parent.getTag(R.id.item_attr_select) != null && (Integer)parent.getTag(R.id.item_attr_select) == pos) {
                                tv.setTextColor(COLOR_SELECT);
                                tv.setBackgroundResource(R.drawable.tag_checked_bg);
                            }else {
                                tv.setTextColor(COLOR_ENABLE);
                                tv.setBackgroundResource(R.drawable.tag_normal_bg);
                            }
                        }else{
                            tv.setTextColor(COLOR_UNENABLE);
                            tv.setTag(false);
                            tv.setBackgroundResource(R.drawable.tag_normal_bg);
                        }


                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getTag()!=null && (Boolean)v.getTag() == false){
                                    showToast("暂无库存");
                                    return;
                                }
                                Integer curPos = (Integer) parent.getTag(R.id.item_attr_select);
                                if (mSelectPosHashMap == null) mSelectPosHashMap = new HashMap();
                                if (mSelectSpecs == null) mSelectSpecs = new LinkedHashMap();
                                if (mSelectSpecs.get(parent.getTag()) != null){
                                    mSelectSpecs.remove(parent.getTag());
                                    parent.setTag(R.id.item_attr_select,null);
                                    mSelectPosHashMap.remove(parent.getTag());
                                }
                                if (curPos == null || pos != curPos) {
                                    mSelectSpecs.put(parent.getTag(),bean.getName());
                                    parent.setTag(R.id.item_attr_select,pos);
                                    mSelectPosHashMap.put(parent.getTag(),pos);
                                }else{
                                    mTvInventory.setText("");
                                    mTvShopPrice.setText(mCurPriceRange);
                                }
                                boolean bShow = true;
                                if (isSelectDone()){
                                    updateShopSpec(true);
                                    bShow = false;
                                }
                                selectOneSpec(bShow);
                            }
                        });
                        return tv;
                    }
                });
            }
        };
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mCommonAdapter);
        mPayNumbView = LayoutInflater.from(getContext()).inflate(R.layout.item_vps_add,mRecycleView,false);
        mTvLimiteNumb = (TextView)mPayNumbView.findViewById(R.id.iva_tv_buynumb);
        mNumbPickerView = (NumberPickerView) mPayNumbView.findViewById(R.id.iva_addview);
        mHeaderAndFooterWrapper.addFootView(mPayNumbView);
        mRecycleView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();

        if (isSelectDone()) {
            updateShopSpec(false);
            getPriceRange();
        }else{
            mFlLoding.setVisibility(GONE);
            Glide.with(getContext()).load(mDataBean.getGoods().getDefaultSpec().getImg()).placeholder(R.mipmap.ic_nor_srcpic).into(mImgShop);
            getPriceRange();
            mTvShopPrice.setText(mCurPriceRange);
            mNumbPickerView.setMaxValue(1) //界面上最小显示1
                    .setCurrentInventory(1) //租赁限制购买数量
                    .setMinDefaultNum(1)
                    .setCurrentNum(1)
                    .setmOnClickInputListener(new NumberPickerView.OnClickInputListener() {
                        @Override
                        public String onIsCanClick() {
                            return isSelectDone() ? "" : "请选选择规格";
                        }
                        @Override
                        public void onSelectDone(int value) {
                            mSelectNumb = String.valueOf(value);
                        }
                        @Override
                        public void onWarningForInventory(int inventory) {
                            Toast.makeText(getContext(),"请选选择规格",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onWarningMinInput(int minValue) {
                        }

                        @Override
                        public void onWarningMaxInput(int maxValue) {
                            Toast.makeText(getContext(),"请选选择规格",Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvCopyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSpecBean == null || !isSelectDone()){
                    Toast.makeText(getContext(),"请选择要购买的商品规格",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!canBuy(1)){
                    if (mSpecBean.getStoreNum() == 0){
                        Toast.makeText(getContext(),"暂无库存",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"超过最大可租赁数量",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                createGroup();
//                Intent intent = new Intent(getContext(), CheckOutActivity.class);
//                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.LEASESHOP);
//                intent.putExtra("productId", String.valueOf(mSpecBean.getId()));
//                intent.putExtra("nums", String.valueOf(mNumbPickerView.getNumText()));
//                getContext().startActivity(intent);
            }
        });
        mTvCopyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dismiss();
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSpecBean == null || !isSelectDone()){
                    Toast.makeText(getContext(),"请选择要购买的商品规格",Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!canBuy(0)){
                    if (mSpecBean.getStoreNum() == 0){
                        Toast.makeText(getContext(),"暂无库存",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }


                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.COMMONSHOP);
                intent.putExtra("productId", String.valueOf(mSpecBean.getId()));
                intent.putExtra("nums", String.valueOf(mNumbPickerView.getNumText()));
                getContext().startActivity(intent);
            }
        });

    }

    private void getPriceRange(){
        if (mType != null && mType == 0){
            if (mDataBean.getGoods().getDefaultSpec().getMaxPrice().equals(mDataBean.getGoods().getDefaultSpec().getMinPrice())){
                mCurPriceRange = "¥"+mDataBean.getGoods().getDefaultSpec().getnMinPrice();
            }else{
                mCurPriceRange = "¥"+mDataBean.getGoods().getDefaultSpec().getnMinPrice() + "～" + mDataBean.getGoods().getDefaultSpec().getnMaxPrice();
            }
        }else {
            if (mDataBean.getGoods().getDefaultSpec().getMaxPrice().equals(mDataBean.getGoods().getDefaultSpec().getMinPrice())){
                mCurPriceRange = "¥"+mDataBean.getGoods().getDefaultSpec().getMinPrice();
            }else{
                mCurPriceRange = "¥"+mDataBean.getGoods().getDefaultSpec().getMinPrice() + "～" + mDataBean.getGoods().getDefaultSpec().getMaxPrice();
            }
        }
    }

    private void createGroup(){
//        YYMallApi.getGroupCreate(getContext(), mDataBean.getGroup().getId(), mSpecBean.getId(), mNumbPickerView.getNumText() ,new ApiCallback<GrouponSubBean.DataBean>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onError(ApiException e) {
//                showToast(e.getMessage());
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onNext(GrouponSubBean.DataBean dataBean) {
//                if (dataBean.getResult() != 0){
//                    //0 操作成功 1 操作失败 2 库存不足 3 拼团活动已结束 4 购买数量已达上限 5 本商品不可重复拼团
//                    switch (dataBean.getResult()){
//                        case 1:
//                            showToast("操作失败");
//                            break;
//                        case 2:
//                            showToast("库存不足");
//                            break;
//                        case 3:
//                            showToast("拼团活动已结束");
//                            break;
//                        case 4:
//                            showToast("购买数量已达上限");
//                            break;
//                        case 5:
//                            showToast("本商品不可重复拼团");
//                            break;
//                    }
//                    return;
//                }
//                Intent intent = new Intent(getContext(), CheckOutActivity.class);
//                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.GROUPON);
//                intent.putExtra("groupUserId", dataBean.getGroupUserId() + "");
//                intent.putExtra("nums", String.valueOf(mNumbPickerView.getNumText()));
//                intent.putExtra("productId", String.valueOf(mSpecBean.getId()));
//                getContext().startActivity(intent);
//            }
//        });
        YYMallApi.getGroupComfirm(getContext(), mSpecBean.getId(), Integer.parseInt(mGroupId), mNumbPickerView.getNumText(),
                new ApiCallback<GrouponSubBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GrouponSubBean.DataBean dataBean) {
                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.GROUPON);
                intent.putExtra("groupUserId", dataBean.getGroupUserId() + "");
                intent.putExtra("nums", mNumbPickerView.getNumText() + "");
                intent.putExtra("productId", String.valueOf(mSpecBean.getId()));
                getContext().startActivity(intent);
            }
        });
    }
    private boolean isSelectDone(){
        return mSelectSpecs!=null && mSelectSpecs.size() == mDataBean.getGoods().getSpec().size();
    }

    public void setOnShopCarResLisiten(OfferedPopipView.OnShopCarResLisiten onShopCarResLisiten){
        mOnShopCarResLisiten = onShopCarResLisiten;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnShopCarResLisiten!=null){
            mOnShopCarResLisiten.onShopCarEntityRes(mSelectSpecs);
            mOnShopCarResLisiten.onShopCarResPos(mSelectPosHashMap);
            mOnShopCarResLisiten.onShopCarSelectString(getSelectString());
            if (isSelectDone()) {
                mOnShopCarResLisiten.onShopSpecRes(mSpecBean, mNumbPickerView.getNumText());
                mOnShopCarResLisiten.onCanSelectRes(getCanSelectStr(0),getCanSelectStr(1));
            }else{
                mOnShopCarResLisiten.onShopSpecRes(null,-1);
            }
        }
    }

    private String getCanSelectStr(int type) {
        if ( mSpecBean == null)
            return null;
        if (type == 0) {
            if (!canBuy(0)) {
                if (mSpecBean.getStoreNum() == 0) {
                    return "暂无库存";
                } else {
                    return "超过最大可购买数量";
                }
            }
            return null;
        } else {
            if (!canBuy(1)) {
                if (mSpecBean.getStoreNum() == 0) {
                    return "暂无库存";
                } else {
                    return "超过最大可租赁数量";
                }
            }
            return null;
        }
    }



    private void initShopSpec(){
        if (mSelectSpecs != null && mSelectSpecs.size() > 0){
            String curSelecst = getSelectString();
            mTvCurSelect.setText(curSelecst);
            if (mOnShopCarResLisiten != null ){
                mOnShopCarResLisiten.onShopCarEntityRes(mSelectSpecs);
                mOnShopCarResLisiten.onShopCarSelectString(curSelecst);
            }
        }
    }

    private String getSelectString(){
        if (mSelectSpecs == null || mSelectSpecs.size() == 0)
            return null;
        String selectString = "已选择 ";
        Iterator iter = mSelectSpecs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            selectString += entry.getValue() + " ";
        }
        return selectString;
    }

    private void selectOneSpec(Boolean bShow){
//        mLastSelectSpecId = id;
        String jsonStr = null;
        if (mSelectSpecs != null && mSelectSpecs.size() != 0){
            HashMap args = new HashMap();
            args.put("spec",mSelectSpecs);
            jsonStr = new Gson().toJson(args);
        }
        YYMallApi.getEnableSpec(getContext(),String.valueOf(mDataBean.getGoods().getId()),jsonStr,bShow,new YYMallApi.ApiResult<EnableSpecBean.DataBean>(getContext()){
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(EnableSpecBean.DataBean dataBean) {
                mEnableSpecList = dataBean.getList();
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    private List<EnableSpecBean.DataBean.ListBean> mEnableSpecList;
    private void updateShopSpec(boolean loadView){
        HashMap hashMap = new HashMap();
        hashMap.put("spec",mSelectSpecs);
        String json = new Gson().toJson(hashMap);
        YYMallApi.getShopSpec(getContext(), String.valueOf(mDataBean.getGoods().getId()), json,null,loadView, new YYMallApi.ApiResult<ShopSpecBean.DataBean>(getContext()) {
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
            public void onNext(ShopSpecBean.DataBean dataBean) {
                mFlLoding.setVisibility(GONE);
                initData(dataBean);
            }
        });
    }

    private ShopSpecBean.DataBean mSpecBean;
    private void initData(ShopSpecBean.DataBean dataBean) {
        mSpecBean = dataBean;
        calculateCanBuyTag();
        Glide.with(getContext()).load(dataBean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into(mImgShop);
        if (mType==null){
            //null
            mTvShopPrice.setText("¥"+mTwoPointDf.format(dataBean.getPrice()));
            if (dataBean.getLimitnum() > 0){
                mTvLimiteNumb.setVisibility(View.VISIBLE);
                mTvLimiteNumb.setText("（团购每人限购" + dataBean.getLimitnum() + "件）");
            }else{
                mTvLimiteNumb.setVisibility(GONE);
            }
        }else if (mType == 0){
            //买
            mTvShopPrice.setText("¥"+mTwoPointDf.format(mSpecBean.getNormalPrice()));
        }else if (mType == 1){
            //租
            mTvShopPrice.setText("¥"+mTwoPointDf.format(dataBean.getPrice()));
            if (dataBean.getLimitnum() > 0){
                mTvLimiteNumb.setVisibility(View.VISIBLE);
                mTvLimiteNumb.setText("（每人限购" + dataBean.getLimitnum() + "件）");
            }else{
                mTvLimiteNumb.setVisibility(GONE);
            }
        }
        mNumbPickerView.setMaxValue(getCurMaxBuy() == 0 ? 1 : getCurMaxBuy()) //界面上最小显示1
                .setCurrentInventory(getCurMaxBuy() == 0 ? 1 : getCurMaxBuy()) //租赁限制购买数量
                .setMinDefaultNum(1)
                .setCurrentNum(TextUtils.isEmpty(mSelectNumb) ? 1 : Integer.parseInt(mSelectNumb))
                .setmOnClickInputListener(new NumberPickerView.OnClickInputListener() {
                    @Override
                    public String onIsCanClick() {
                        return isSelectDone() ? "" : "请选选择规格";
                    }
                    @Override
                    public void onSelectDone(int value) {
                        mSelectNumb = String.valueOf(value);
                    }
                    @Override
                    public void onWarningForInventory(int inventory) {
                        Toast.makeText(getContext(),"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onWarningMinInput(int minValue) {
                    }

                    @Override
                    public void onWarningMaxInput(int maxValue) {
                        Toast.makeText(getContext(),"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                    }
                });
        mTvInventory.setText("库存"+dataBean.getStoreNum()+"件");
//            mOnShopCarResLisiten.onShopSpecRes(dataBean, mNumbPickerView.getNumText());
//        }
    }

    private int mCommonCanMaxBuy;
    private int mGroupCanMaxBuy;
    //计算购买上限
    private void calculateCanBuyTag(){
        int limit = mSpecBean.getLimitnum();
        int storeNumb = mSpecBean.getStoreNum();
        int allowPayNumb = mSpecBean.getAllowMaxNum();
        mCommonCanMaxBuy = storeNumb;
        if (limit == 0){
            //不限购
            mGroupCanMaxBuy = storeNumb;
        }else{
            //限购
            mGroupCanMaxBuy = allowPayNumb > storeNumb ? storeNumb : allowPayNumb;
        }
    }

    private int getCurMaxBuy(){
        if (mType == null || mType == 0){
            //两者和普通
            return mCommonCanMaxBuy;
        }else {
            return mGroupCanMaxBuy;
        }
    }

    /**
     *
     * @param type
     */
    private boolean canBuy(int type){
        //type  0-buy  1-lease
        if (type == 0){
            return mNumbPickerView.getNumText() <= mCommonCanMaxBuy;
        }else{
            return mNumbPickerView.getNumText() <= mGroupCanMaxBuy;
        }
    }

    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(500 * 2, 0, 300);
        return animation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_shopcar);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.vps_rl_popview);
    }

    public interface OnShopCarResLisiten{
        void onShopCarEntityRes(LinkedHashMap hashMap);
        void onShopCarResPos(HashMap hashMap);
        void onShopCarSelectString(String string);
        void onShopSpecRes(ShopSpecBean.DataBean specBean, int numb);
        void onCanSelectRes(String commonCanBuy, String leaseCanBuy);
    }
}
