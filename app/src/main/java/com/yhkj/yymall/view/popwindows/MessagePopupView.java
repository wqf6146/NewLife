package com.yhkj.yymall.view.popwindows;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;

/**
 * Created by Administrator on 2017/7/24.
 */

public class MessagePopupView extends BasePopupWindow {

    private MessageCall call;

    public MessagePopupView(final Activity context, MessageCall calls) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        call = calls;
        findViewById(R.id.vpm_ll_yidu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYMallApi.readAllMessage(context, new YYMallApi.ApiResult<CommonBean>(context) {
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
                    public void onNext(CommonBean commonBean) {
                        if (call != null) {
                            call.send(true);
                            dismiss();
                        }
                    }
                });

            }
        });
        findViewById(R.id.vpm_ll_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYMallApi.deleteAllMessage(context, new YYMallApi.ApiResult<CommonBean>(context) {
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
                    public void onNext(CommonBean commonBean) {
                        if (call != null) {
                            call.send(false);
                            dismiss();
                        }
                    }
                });
            }
        });
    }


    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
    }

    @Override
    public Animator initShowAnimator() {
        return null;
    }

    @Override
    public void showPopupWindow(View v) {
//        setOffsetX(-(getWidth() - v.getWidth() / 2));
        setOffsetY(-v.getHeight() / 2);
        super.showPopupWindow(v);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_popup_message);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_contianer);
    }

    public interface MessageCall {
        void send(boolean readAllOrdelAll);
    }

}
