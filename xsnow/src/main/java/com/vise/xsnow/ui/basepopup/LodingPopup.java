package com.vise.xsnow.ui.basepopup;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import com.vise.xsnow.R;

public class LodingPopup extends BasePopupWindow {

    public LodingPopup(Activity context) {
        super(context);
        /**全屏popup*/
//        setPopupWindowFullScreen(true);
    }

    @Override protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public Animator initShowAnimator() {
        return ObjectAnimator.ofFloat(mAnimaView, "alpha", 0.4f, 1).setDuration(250 * 3 / 2);
    }

    @Override
    public Animator initExitAnimator() {
        return  ObjectAnimator.ofFloat(mAnimaView, "alpha", 1f, 0).setDuration(200);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override public View onCreatePopupView() {
        return createPopupById(R.layout.popup_loding);
    }

    @Override public View initAnimaView() {
        return findViewById(R.id.pl_ll_popview);
    }
}
