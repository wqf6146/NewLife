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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public abstract class ShopArgsPopupView<T> extends BasePopupWindow {

    private List<T> mDataBeans;

    @Bind(R.id.vps_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.vps_btn_copy)
    TextView mTvCopy;

    @Bind(R.id.vps_tv_title)
    TextView mTvTitle;

    public ShopArgsPopupView(Activity context,List<T> databean) {
        super(context);
        mDataBeans = databean;
        init();
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
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_shopargss);
        ButterKnife.bind(this,view);
        return view;
    }

    private void init() {
        mTvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(new CommonAdapter<T>(getContext(),R.layout.item_shopargs,mDataBeans) {
            @Override
            protected void convert(ViewHolder holder, T bean, int position) {
                bind(holder,bean,position);
            }
        });
    }

    public void setTvTitle(String title){
        mTvTitle.setText(title);
    }

    protected abstract void bind(ViewHolder holder, T t, int position);
    @Override
    public View initAnimaView() {
        return findViewById(R.id.vps_rl_popview);
    }
}
