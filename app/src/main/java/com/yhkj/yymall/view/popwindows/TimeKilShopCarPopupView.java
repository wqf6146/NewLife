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
import com.vise.log.ViseLog;
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
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.NormsBean;
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.bean.ShopSpecBean;
import com.yhkj.yymall.bean.TimeKillDetailBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.NumberPickerView;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 */
public class TimeKilShopCarPopupView extends BasePopupWindow {

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
    FrameLayout mFlLoading;

    private View mPayNumbView;
    java.text.DecimalFormat mTwoPointDf =new java.text.DecimalFormat("#0.00");
    TimeKillDetailBean.DataBean mDataBean;
    private String mSelectNumb;
    private OnShopCarResLisiten mOnShopCarResLisiten;
    private Integer mType; // null 0-左边 1-右边
    public TimeKilShopCarPopupView(Activity context, OnShopCarResLisiten onShopCarResLisiten, TimeKillDetailBean.DataBean dataBean,Integer type) {
        this(context,onShopCarResLisiten,dataBean,null,null,null,type);
    }
    public TimeKilShopCarPopupView(Activity activity, OnShopCarResLisiten onShopCarResLisiten,
                                   TimeKillDetailBean.DataBean dataBean, HashMap specHashMap, HashMap selectPosHashMap, String numb,Integer type){
        super(activity);
        mType = type;
        mSpecHashMap = specHashMap;
        mOnShopCarResLisiten = onShopCarResLisiten;
        mSelectPosHashMap = selectPosHashMap;
        mDataBean = dataBean;
        mSelectNumb = numb;
        setBottomShow();
        init();
    }

    private LinkedHashMap mSelectSpecs = new LinkedHashMap();
    private HashMap mSpecHashMap;
    private HashMap mSelectPosHashMap;
    NumberPickerView mNumbPickerView;

    private void setBottomShow(){
        if (mType == null){
            mTvCopyLeft.setText("加入购物车");
            mTvCopyRight.setText("立即购买");
            return;
        }else{
            switch (mType){
                case 0:
                    mTvCopyLeft.setText("确定");
                    mTvCopyRight.setVisibility(GONE);
                    break;
                case 1:
                    mTvCopyRight.setText("确定");
                    mTvCopyLeft.setVisibility(GONE);
                    break;
            }
        }

    }

    private void init() {
        initShopSpec();
        updateShopSpec(false);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommonAdapter commonAdapter = new CommonAdapter<TimeKillDetailBean.DataBean.SpecBean>(getContext(),
                R.layout.item_shopcar_standard,mDataBean.getSpec()) {
            @Override
            protected void convert(ViewHolder holder, TimeKillDetailBean.DataBean.SpecBean bean, int position) {
                holder.setText(R.id.iss_tv_key,bean.getName());
                TagFlowLayout tagFlowLayout = holder.getView(R.id.iss_flowlayout);
                tagFlowLayout.setTag(bean.getId());
//                tagFlowLayout.setAdapter(new TagAdapter<TimeKillDetailBean.DataBean.SpecBean.ValueBean>(bean.getValue()) {
//                    @Override
//                    public View getView(FlowLayout parent, int position, TimeKillDetailBean.DataBean.SpecBean.ValueBean bean) {
//                        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_tv,
//                                parent, false);
//                        tv.setText(bean.getName());
//                        tv.setTag(bean.getName());
//                        return tv;
//                    }
//                });
//                tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
//                    @Override
//                    public boolean onTagClick(final View view, final int position, final FlowLayout parent) {
//                        mSelectSpecs.put(parent.getTag(),view.getTag());
//                        if (mSelectPosHashMap == null) mSelectPosHashMap = new HashMap();
//                        mSelectPosHashMap.put(parent.getTag(),position);
//                        updateShopSpec();
//                        return false;
//                    }
//                });
//                Object object = mSelectPosHashMap == null ? null : mSelectPosHashMap.get(bean.getId());
//                if (object != null)
//                    tagFlowLayout.getAdapter().setSelectedList((Integer) object);
//                else
//                    tagFlowLayout.getAdapter().setSelectedList(0);
//                tagFlowLayout.setMaxSelectCount(1);
                Object object = mSelectPosHashMap == null ? null : mSelectPosHashMap.get(bean.getId());
                if (object != null) {
                    tagFlowLayout.setTag(R.id.item_attr_select,object);
                }else {
                    tagFlowLayout.setTag(R.id.item_attr_select,0);
                }
                tagFlowLayout.setMaxSelectCount(1);
                tagFlowLayout.setAdapter(new TagAdapter<TimeKillDetailBean.DataBean.SpecBean.ValueBean>(bean.getValue()) {
                    @Override
                    public View getView(final FlowLayout parent,final int position, final TimeKillDetailBean.DataBean.SpecBean.ValueBean bean) {
                        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_tv, parent, false);
                        tv.setText(bean.getName());
                        if (parent.getTag(R.id.item_attr_select) != null && (Integer)parent.getTag(R.id.item_attr_select) == position) {
                            tv.setTextColor(Color.parseColor("#ffffff"));
                            tv.setBackgroundResource(R.drawable.tag_checked_bg);
                        }else {
                            tv.setTextColor(Color.parseColor("#727070"));
                            tv.setBackgroundResource(R.drawable.tag_normal_bg);
                        }
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (parent.getTag(R.id.item_attr_select)!=null && (Integer)parent.getTag(R.id.item_attr_select) == position)
                                    return;
                                parent.setTag(R.id.item_attr_select,position);
                                notifyDataChanged();
                                mSelectSpecs.put(parent.getTag(),bean.getName());
                                if (mSelectPosHashMap == null) mSelectPosHashMap = new HashMap();
                                mSelectPosHashMap.put(parent.getTag(),position);
                                mSelectNumb = String.valueOf(mNumbPickerView.getNumText());
                                updateShopSpec(true);
                            }
                        });
                        return tv;
                    }
                });
            }
        };
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        mPayNumbView = LayoutInflater.from(getContext()).inflate(R.layout.item_vps_add,mRecycleView,false);
        mNumbPickerView = (NumberPickerView) mPayNumbView.findViewById(R.id.iva_addview);
        TextView iva_tv_buynumb = (TextView)mPayNumbView.findViewById(R.id.iva_tv_buynumb);
        if (mDataBean.getMaxCount() > 0){
            iva_tv_buynumb.setVisibility(View.VISIBLE);
            iva_tv_buynumb.setText("（每人限购" + mDataBean.getMaxCount() + "件）");
        }else{
            iva_tv_buynumb.setVisibility(GONE);
        }
        mHeaderAndFooterWrapper.addFootView(mPayNumbView);
        mRecycleView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();

        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTvCopyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //我要买

                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSpecBean==null){
                    Toast.makeText(getContext(),"请选择要购买的商品规格",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSpecBean.getStoreNum() == 0){
                    Toast.makeText(getContext(),"该商品暂无库存",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!canBuy()){
                    Toast.makeText(getContext(),"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getContext(), CheckOutActivity.class);
                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.TIMEKILL);
                intent.putExtra("productId", String.valueOf(mSpecBean.getId()));
                intent.putExtra("nums", String.valueOf(mNumbPickerView.getNumText()));
                intent.putExtra("panicBuyId", String.valueOf(mDataBean.getPanicId()));
                getContext().startActivity(intent);
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

                if (mSpecBean==null){
                    Toast.makeText(getContext(),"请选择要购买的商品规格",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSpecBean.getStoreNum() == 0){
                    Toast.makeText(getContext(),"该商品暂无库存",Toast.LENGTH_SHORT).show();
                    return;
                }

                YYMallApi.getAddCar(getContext(), String.valueOf(mSpecBean.getId()), mNumbPickerView.getNumText(), new YYMallApi.ApiResult<CommonBean>(getContext()) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ViseLog.e(e);
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        Toast.makeText(getContext(),"加入购物车成功",Toast.LENGTH_SHORT).show();
                        dismissWithOutAnima();
                    }
                });
            }
        });

    }

    public void setOnShopCarResLisiten(OnShopCarResLisiten onShopCarResLisiten){
        mOnShopCarResLisiten = onShopCarResLisiten;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnShopCarResLisiten!=null){
            mOnShopCarResLisiten.onShopCarResLisiten(mSpecHashMap);
            mOnShopCarResLisiten.onShopCarResPos(mSelectPosHashMap);
            if (mSpecBean != null) {
                mOnShopCarResLisiten.onShopSpecRes(mSpecBean, String.valueOf(mNumbPickerView.getNumText()));
                mOnShopCarResLisiten.onCanSelectRes(getCanSelectStr());
            }
        }
    }

    private void initShopSpec(){
        if (mSpecHashMap != null){
            mSelectSpecs = (LinkedHashMap) mSpecHashMap.get("spec");
            String curSelecst = getSelectString();
//            mTvCurSelect.setText(curSelecst);
            if (mOnShopCarResLisiten != null){
                mOnShopCarResLisiten.onShopCarResLisiten(mSpecHashMap);
                mOnShopCarResLisiten.onShopCarSelectString(curSelecst);
            }
        }else{
            mSpecHashMap = new HashMap();
            mSelectPosHashMap = new HashMap();
            String curSelecst = "已选择 ";
            for (int i=0;i<mDataBean.getSpec().size();i++){
                final TimeKillDetailBean.DataBean.SpecBean specBean = mDataBean.getSpec().get(i);
                for (int j=0;i<specBean.getValue().size();j++){
                    TimeKillDetailBean.DataBean.SpecBean.ValueBean valueBean = specBean.getValue().get(j);
                    mSelectSpecs.put(specBean.getId(),valueBean.getName());
                    curSelecst += valueBean.getName() + " ";
                    break;
                }
            }
            mSpecHashMap.put("spec",mSelectSpecs);
//            mTvCurSelect.setText(curSelecst);
            if (mOnShopCarResLisiten != null){
                mOnShopCarResLisiten.onShopCarResLisiten(mSpecHashMap);
                mOnShopCarResLisiten.onShopCarSelectString(curSelecst);
            }
        }
    }

    private String getSelectString(){
        String selectString = "已选择 ";
        Iterator iter = mSelectSpecs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            selectString += entry.getValue() + " ";
        }
        return selectString;
    }

    private int mCommonCanMaxBuy; //普通最大购买量
    //计算购买上限
    private void calculateCanBuyTag(){
        int limit = mDataBean.getMaxCount();
        int storeNumb = mSpecBean.getStoreNum();
        int allowPayNumb = mDataBean.getAllowMaxCount();
        if (limit == 0){
            //不限购
            mCommonCanMaxBuy = storeNumb;
        }else{
            //限购
            mCommonCanMaxBuy = allowPayNumb > storeNumb ? storeNumb : allowPayNumb;
        }
    }
    /**
     *
     */
    private boolean canBuy(){
        return mNumbPickerView.getNumText() <= mCommonCanMaxBuy;
    }
    private String getCanSelectStr() {
        if (!canBuy()) {
            if (mSpecBean.getStoreNum() == 0) {
                return "暂无库存";
            } else {
                return "超过最大可购买数量";
            }
        }
        return null;
    }

    private void updateShopSpec(boolean loadView){
        String json = new Gson().toJson(mSpecHashMap);
        YYMallApi.getShopSpec(getContext(), String.valueOf(mDataBean.getGoodsId()), json,loadView,  new YYMallApi.ApiResult<ShopSpecBean.DataBean>(getContext()) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                mFlLoading.setVisibility(GONE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSpecBean.DataBean dataBean) {
                mFlLoading.setVisibility(GONE);
                initData(dataBean);
                String selects = getSelectString();
//                mTvCurSelect.setText(selects);
                if (mOnShopCarResLisiten!=null){
                    mOnShopCarResLisiten.onShopCarResLisiten(mSpecHashMap);
                    mOnShopCarResLisiten.onShopCarSelectString(selects);
                    mOnShopCarResLisiten.onShopCarResPos(mSelectPosHashMap);
                    mOnShopCarResLisiten.onShopSpecRes(dataBean,String.valueOf(mNumbPickerView.getNumText()));
                }
            }
        });
    }

    private ShopSpecBean.DataBean mSpecBean;
    private void initData(ShopSpecBean.DataBean dataBean) {
        mSpecBean = dataBean;
        calculateCanBuyTag();
        Glide.with(getContext()).load(dataBean.getImg()).into(mImgShop);
        mTvShopPrice.setText("¥"+mTwoPointDf.format(mSpecBean.getPrice()));
        mNumbPickerView.setMaxValue(mCommonCanMaxBuy == 0 ? 1 : mCommonCanMaxBuy)
                .setCurrentInventory(mCommonCanMaxBuy == 0 ? 1 : mCommonCanMaxBuy)
                .setMinDefaultNum(1)
                .setCurrentNum(TextUtils.isEmpty(mSelectNumb) ? 1 : Integer.parseInt(mSelectNumb))
                .setmOnClickInputListener(new NumberPickerView.OnClickInputListener() {
                    @Override
                    public void onSelectDone(int value) {

                    }
                    @Override
                    public void onWarningForInventory(int inventory) {
                        Toast.makeText(getContext(),"超过最大库存",Toast.LENGTH_SHORT).show();
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
        if (mOnShopCarResLisiten!=null)
            mOnShopCarResLisiten.onShopSpecRes(dataBean,String.valueOf(mNumbPickerView.getNumText()));
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
        void onShopCarResLisiten(HashMap hashMap);
        void onShopCarResPos(HashMap hashMap);
        void onShopCarSelectString(String string);
        void onShopSpecRes(ShopSpecBean.DataBean specBean, String numb);
        void onCanSelectRes(String commonCanBuy);
    }
}
