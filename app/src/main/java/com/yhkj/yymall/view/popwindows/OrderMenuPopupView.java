package com.yhkj.yymall.view.popwindows;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;

public class OrderMenuPopupView extends BasePopupWindow implements View.OnClickListener {

    public OrderMenuPopupView(final Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.vpm_ll_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLisiten!=null){
                    mOnItemClickLisiten.OnItemClickLisiten(1);
                }
                dismiss();
            }
        });
        findViewById(R.id.vpm_ll_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLisiten!=null){
                    mOnItemClickLisiten.OnItemClickLisiten(2);
                }
                dismiss();
            }
        });
        findViewById(R.id.vpm_ll_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLisiten!=null){
                    mOnItemClickLisiten.OnItemClickLisiten(3);
                }
                dismiss();
            }
        });
    }

    public OrderMenuPopupView setOnItemClickLisiten(OnItemClickListen onItemClickLisiten) {
        this.mOnItemClickLisiten = onItemClickLisiten;
        return this;
    }

    private OnItemClickListen mOnItemClickLisiten;
    public interface OnItemClickListen {
        void OnItemClickLisiten(int type);
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
        //return null;
    }

    @Override
    public Animator initShowAnimator() {
       /* AnimatorSet set=new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mAnimaView,"scaleX",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"scaleY",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"alpha",0.0f,1.0f).setDuration(300*3/2));*/
        return null;
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetX(-(getWidth() - v.getWidth() / 3*2));
        setOffsetY(-v.getHeight() / 2);
        super.showPopupWindow(v);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_popup_menu);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_contianer);
    }

    @Override
    public void onClick(View v) {
    }
}
