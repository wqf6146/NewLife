package com.yhkj.yymall.view.popwindows;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;


/**
 * Created by Administrator on 2017/7/11.
 */

public class DetailsMenuPopupView extends BasePopupWindow implements View.OnClickListener {

    public DetailsMenuPopupView(final Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.vpm_ll_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMenuClickLisiten!=null)
                    mOnMenuClickLisiten.onMenuClick(1);
            }
        });
        findViewById(R.id.vpm_ll_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMenuClickLisiten!=null)
                    mOnMenuClickLisiten.onMenuClick(2);
            }
        });
        findViewById(R.id.vpm_ll_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMenuClickLisiten!=null)
                    mOnMenuClickLisiten.onMenuClick(3);
            }
        });
    }

    private OnMenuClickLisiten mOnMenuClickLisiten;

    public DetailsMenuPopupView setOnMenuClickLisiten(OnMenuClickLisiten onMenuClickLisiten) {
        this.mOnMenuClickLisiten = onMenuClickLisiten;
        return this;
    }

    public interface OnMenuClickLisiten {
        void onMenuClick(int pos);
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
//        setOffsetY(-v.getHeight() / 2);
        super.showPopupWindow(v);
    }

    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_popup_details);
    }

    @Override
    public View initAnimaView() {
        return getPopupWindowView().findViewById(R.id.popup_contianer);
    }

    @Override
    public void onClick(View v) {
    }
}