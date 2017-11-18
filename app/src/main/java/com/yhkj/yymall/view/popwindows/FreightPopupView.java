package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.CheckOutBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public abstract class FreightPopupView<T> extends BasePopupWindow {

    private List<T> mDataBeans;

    @Bind(R.id.vps_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.vps_btn_copy)
    TextView mTvCopy;

    @Bind(R.id.vps_tv_title)
    TextView mTvTitle;

    public FreightPopupView(Activity context, List<T> databean) {
        super(context);
        mDataBeans = databean;
        init();
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(500 * 2, 0, 300);
        return animation;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation animation = getTranslateAnimation(0, 500 * 2, 300);
        return animation;
    }



    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_shopargss);
        ButterKnife.bind(this,view);
        return view;
    }

    private void init() {
        mTvTitle.setText("配送方式");
//        setDismissWhenTouchOuside(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(new CommonAdapter<T>(getContext(),R.layout.item_freight,mDataBeans) {
            @Override
            protected void convert(ViewHolder holder, T bean, int position) {
                bind(holder,bean,position);
            }
        });
        mTvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        onSelectRes(mFreightBean);
        super.dismiss();
    }

    public RecyclerView.Adapter getAdapter(){
        return mRecycleView.getAdapter();
    }

    public void setTvTitle(String title){
        mTvTitle.setText(title);
    }


    protected CheckOutBean.DataBean.SellersBean.FreightBean mFreightBean;
    protected abstract void bind(ViewHolder holder, T t, int position);
    protected abstract void onSelectRes(CheckOutBean.DataBean.SellersBean.FreightBean freightBean);
    @Override
    public View initAnimaView() {
        return findViewById(R.id.vps_rl_popview);
    }
}
