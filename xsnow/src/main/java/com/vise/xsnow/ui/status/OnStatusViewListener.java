package com.vise.xsnow.ui.status;

import android.view.View;

/**
 * @Description: 状态视图显示监听
 */
public interface OnStatusViewListener {
    void onShowView(View view, int id);

    void onHideView(View view, int id);
}
