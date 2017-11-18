package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.ShopSelectBean;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShopFiltratePopView extends BasePopupWindow {

    @Bind(R.id.vps_listview)
    NestFullListView mListView;

    @Bind(R.id.vps_tv_reset)
    TextView mTvReset;

    @Bind(R.id.vps_tv_commit)
    TextView mTvCommit;

    @Bind(R.id.vps_et_hightprice)
    EditText mEditHightPrice;

    @Bind(R.id.vps_et_lowprice)
    EditText mEditLowPrice;


    private List<ShopSelectBean.DataBean.AttrsBean> mAttrsBean;
    private HashMap mPriceEntry = null;
    private LinkedHashMap mSelectAttrMap = new LinkedHashMap();
    public ShopFiltratePopView(Activity context, List<ShopSelectBean.DataBean.AttrsBean> attrsBeanList, LinkedHashMap linkedHashMap,HashMap priceMap) {
        super(context);
        setBackPressEnable(false);
        setDismissWhenTouchOuside(true);
        mAttrsBean = attrsBeanList;
        mSelectAttrMap = linkedHashMap == null ? new LinkedHashMap() : linkedHashMap;
        mPriceEntry = priceMap == null ? new HashMap() : priceMap;
        initData();
    }

    private void initData() {

        mEditHightPrice.setText(mPriceEntry.get("max") == null ? "" : String.valueOf(mPriceEntry.get("max")));
        mEditLowPrice.setText(mPriceEntry.get("min") == null ? "" : String.valueOf(mPriceEntry.get("min")));

        mTvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditHightPrice.setText("");
                mEditLowPrice.setText("");
                mPriceEntry = new HashMap();
                mSelectAttrMap = new LinkedHashMap();
                mListView.updateUI();
            }
        });

        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCallBack!=null){
                    String lowPrice = mEditLowPrice.getText().toString();
                    String hightPrice = mEditHightPrice.getText().toString();

                    if (!TextUtils.isEmpty(lowPrice) && !TextUtils.isEmpty(hightPrice)){
                        int iprice = Integer.parseInt(lowPrice);
                        int hprice = Integer.parseInt(hightPrice);
                        int max = iprice > hprice ? iprice : hprice;
                        int min = iprice < hprice ? iprice : hprice;
                        mPriceEntry.put("min",min);
                        mPriceEntry.put("max",max);
                    }else{
                        if (!TextUtils.isEmpty(lowPrice)){
                            mPriceEntry.put("min",lowPrice);
                        }else{
//                            mPriceEntry.put("min","");
                            mPriceEntry.remove("min");
                        }
                        if (!TextUtils.isEmpty(hightPrice)){
                            mPriceEntry.put("max",hightPrice);
                        }else{
//                            mPriceEntry.put("max","");
                            mPriceEntry.remove("max");
                        }
                    }
                    mOnCallBack.onStartSelect(mSelectAttrMap,mPriceEntry);
                }
                dismissWithOutAnima();
            }
        });

        mListView.setAdapter(new NestFullListViewAdapter<ShopSelectBean.DataBean.AttrsBean>(R.layout.item_filter_flowitem,mAttrsBean) {
            @Override
            public void onBind(final int pos, ShopSelectBean.DataBean.AttrsBean bean, NestFullViewHolder holder) {
                holder.setText(R.id.iff_title,bean.getName());
                TagFlowLayout tagFlowLayout = holder.getView(R.id.iff_flowlayout);
                TagAdapter tagAdapter = new TagAdapter<String>(bean.getValue()) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_tv,
                                parent, false);
                        tv.setText(s);
                        tv.setTag(s);
                        tv.setTag(R.id.item_attr_id,mAttrsBean.get(pos).getId());
                        tv.setTag(R.id.item_attr_pos,String.valueOf(pos));
                        return tv;
                    }
                };
                //重置
                HashMap<String,String> attrEntry = (HashMap) mSelectAttrMap.get(String.valueOf(pos));
                if (attrEntry == null || attrEntry.size() == 0) {
                    tagAdapter.setSelectedList(0);
                }else{
                    String destVal = "";
                    int tag;
                    for (Map.Entry<String, String> item : attrEntry.entrySet()){
                        destVal=item.getValue();
                    }
                    List<String> strings = mAttrsBean.get(pos).getValue();
                    for (tag=0; tag<strings.size();tag++){
                        if (strings.get(tag).equals(destVal))
                            break;
                    }
                    tagAdapter.setSelectedList(tag);
                }
                tagFlowLayout.setAdapter(tagAdapter);
                tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        String pos = (String)view.getTag(R.id.item_attr_pos);
                        String id = (String)view.getTag(R.id.item_attr_id);
                        String value = (String)view.getTag();

                        HashMap attrEntiy = (HashMap) mSelectAttrMap.get(pos);
                        if (attrEntiy != null){
                            //已经选择过
                            Object oldValue = attrEntiy.get(id);
                            if (value.equals("全部")){
                                //全部取消此属性
                                mSelectAttrMap.remove(pos);
                            }else{
                                if ( oldValue!=null && value.equals(oldValue) ){
                                    //取消选择
                                    Collection<String> col = attrEntiy.values();
                                    col.remove(value);
                                }else{
                                    //选择
                                    attrEntiy.put(id,value);
                                }
                            }
                        }else{
                            //添加新的
                            attrEntiy = new HashMap();
                            attrEntiy.put(id,value);
                            mSelectAttrMap.put(pos,attrEntiy);
                        }
                        return false;
                    }
                });
                tagFlowLayout.setMaxSelectCount(1);
            }
        });
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
    }

    public interface OnCallBack{
        void onStartSelect(LinkedHashMap attrHashMap, HashMap priceHashMap);
    }

    private OnCallBack mOnCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.mOnCallBack = onCallBack;
    }


    @Override
    protected Animation initShowAnimation() {
        return AnimationUtils.loadAnimation(getContext(),R.anim.pop_action_right_enter);
    }

    @Override
    protected Animation initExitAnimation() {
        /**
         * 发现在4.1.1 小米会蹦
         */
        return AnimationUtils.loadAnimation(getContext(),R.anim.pop_action_right_exit);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_shopfiltrate);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.vp_pop_view);
    }

}
