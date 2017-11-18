package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.TimeKillClassifBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 */

public class ShopClassifyPopView extends BasePopupWindow {

    @Bind(R.id.ir_recycleview)
    RecyclerView mRecyleView;

    @Bind(R.id.vt_progress)
    ProgressBar mProgress;

    public ShopClassifyPopView(Activity context) {
        super(context);
        setBackPressEnable(false);
        setDismissWhenTouchOuside(true);
        init();
    }


    private void init(){
        mRecyleView.setLayoutManager(new GridLayoutManager(getContext(),4));
        YYMallApi.getTimeKillClassify(getContext(), new YYMallApi.ApiResult<TimeKillClassifBean.DataBean>(getContext()) {
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
            public void onNext(TimeKillClassifBean.DataBean dataBean) {
                mProgress.setVisibility(View.GONE);
//                TimeKillClassifBean.DataBean.CategorysBean bean2 = new TimeKillClassifBean.DataBean.CategorysBean();
//                bean2.setId(-2);
//                bean2.setName("品牌");
//                dataBean.getCategorys().add(0,bean2);
//                TimeKillClassifBean.DataBean.CategorysBean bean1 = new TimeKillClassifBean.DataBean.CategorysBean();
//                bean1.setId(-1);
//                bean1.setName("新品");
//                dataBean.getCategorys().add(0,bean1);
                TimeKillClassifBean.DataBean.CategorysBean bean = new TimeKillClassifBean.DataBean.CategorysBean();
                bean.setId(0);
                bean.setName("全部");
                dataBean.getCategorys().add(0,bean);
                mRecyleView.setAdapter(new CommonAdapter<TimeKillClassifBean.DataBean.CategorysBean>(getContext(),R.layout.item_tv_pure,dataBean.getCategorys()) {
                    @Override
                    protected void convert(ViewHolder holder, final TimeKillClassifBean.DataBean.CategorysBean bean, int position) {
                        holder.setText(R.id.itp_tv,bean.getName());
                        int pad = CommonUtil.dip2px(mContext,15);
                        holder.getView(R.id.itp_rl_container).setPadding(0,pad,0,pad);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mOnClassifyUpdate!=null) {
                                    if (bean.getId() == 0)
                                        mOnClassifyUpdate.onClassifyUpdate(null);
                                    else if (bean.getId() == -1)
                                        mOnClassifyUpdate.onClassifyUpdate(null);
                                    else if (bean.getId() == -2)
                                        mOnClassifyUpdate.onClassifyUpdate(null);
                                    else
                                        mOnClassifyUpdate.onClassifyUpdate(bean.getId());
                                    dismissWithOutAnima();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    private OnClassifyUpdate mOnClassifyUpdate;
    public interface OnClassifyUpdate {
        void onClassifyUpdate(Integer id);
    }

    public ShopClassifyPopView setOnClassifyUpdate(OnClassifyUpdate onClassifyUpdate){
        mOnClassifyUpdate = onClassifyUpdate;
        return this;
    }

    @Override
    protected Animation initShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -CommonUtil.dipToPx(getContext(), 180f), 0);
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(1));
        return translateAnimation;
    }

    @Override
    protected Animation initExitAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -CommonUtil.dipToPx(getContext(), 180f));
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
        View view = createPopupById(R.layout.view_timekillclassify);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.ir_ll_popview);
    }
}
