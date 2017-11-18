package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;

/**
 */
public class DialogPopup extends BasePopupWindow {

    private TextView ok;
    private TextView cancel, title, content;
    private Context mContext;

    public DialogPopup(Activity context, String titles, String contents, String oks, String cancels) {
        super(context);
        mContext = context;
        ok = (TextView) findViewById(R.id.ok);
        cancel = (TextView) findViewById(R.id.cancel);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        title.setText(titles);
        content.setText(contents);
        ok.setText(oks);
        cancel.setText(cancels);
    }

    @Override
    protected Animation initShowAnimation() {
//        AnimationSet set = new AnimationSet(false);
//        Animation shakeAnima = new RotateAnimation(0, 15, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        shakeAnima.setInterpolator(new CycleInterpolator(5));
//        shakeAnima.setDuration(400);
//        set.addAnimation(getDefaultAlphaAnimation());
//        set.addAnimation(shakeAnima);

        Animation animation = getTranslateAnimation(250 * 2, 0, 300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;

    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_popup_dialog);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    public void setOkOnClickListener(View.OnClickListener okOnClickListener) {
        ok.setOnClickListener(okOnClickListener);
    }

    public void setCancelOnClickListener(View.OnClickListener okOnClickListener) {
        cancel.setOnClickListener(okOnClickListener);
    }


}
