package com.yhkj.yymall.view.pageindicatorview.animation;

import android.support.annotation.NonNull;

import com.yhkj.yymall.view.pageindicatorview.animation.controller.AnimationController;
import com.yhkj.yymall.view.pageindicatorview.animation.controller.ValueController;
import com.yhkj.yymall.view.pageindicatorview.draw.data.Indicator;


public class AnimationManager {

    private AnimationController animationController;

    public AnimationManager(@NonNull Indicator indicator, @NonNull ValueController.UpdateListener listener) {
        this.animationController = new AnimationController(indicator, listener);
    }

    public void basic() {
        if (animationController != null) {
            animationController.end();
            animationController.basic();
        }
    }

    public void interactive(float progress) {
        if (animationController != null) {
            animationController.interactive(progress);
        }
    }

    public void end() {
        if (animationController != null) {
            animationController.end();
        }
    }
}
