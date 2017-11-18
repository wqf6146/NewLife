package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.ShopfiltrationAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.LeaseClassifyBean;
import com.yhkj.yymall.bean.ShopSelectBean;
import com.yhkj.yymall.util.CommonUtil;

public abstract class ShopFiddlePopView<T> extends BasePopupWindow {

    T mDataBean;
//    private String mParentName;
    public ShopFiddlePopView(Activity context, final T attrsBean) {
        super(context);
        mDataBean = attrsBean;

//        if (mDataBean !=null && mDataBean.getValue() != null && mDataBean.getValue().size() > 0){
//            if (!mDataBean.getValue().get(0).equals("全部"))
//                mDataBean.getValue().add(0,"全部");
//        }

        setBackPressEnable(false);
        setDismissWhenTouchOuside(true);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.ir_recycleview);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        if (mDataBean == null){
            recyclerView.setAdapter(new ShopfiltrationAdapter(getContext()));
        }else{
            initPopView(recyclerView,mDataBean);
        }
    }

    protected abstract void initPopView(RecyclerView recyclerView,T databean);

    public interface OnCallBack{
        void onSelectResString(String select);
        void onStartSelect(int type, String order, String by, String page, String limit, String brand, String key, String value);
    }

    protected OnCallBack mOnCallBack;

    public void setOnCallBack(OnCallBack onCallBack) {
        this.mOnCallBack = onCallBack;
    }

    @Override
    protected Animation initShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -CommonUtil.dip2px(getContext(), 350f), 0);
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(1));
        return translateAnimation;
    }

    @Override
    protected Animation initExitAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -CommonUtil.dip2px(getContext(), 350f));
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(-4));
        return translateAnimation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_pop_shopfiddle);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.ir_ll_popview);
    }

}
