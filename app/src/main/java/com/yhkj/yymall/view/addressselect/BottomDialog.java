package com.yhkj.yymall.view.addressselect;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.yhkj.yymall.R;
import com.yhkj.yymall.util.CommonUtil;


public class BottomDialog extends Dialog {
    private AddressSelector selector;

    public BottomDialog(Context context) {
        super(context, R.style.bottom_dialog);
        init(context);
    }

    public BottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public BottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        selector = new AddressSelector(context);
        setContentView(selector.getView());

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = CommonUtil.dip2px(context, 256);
        window.setAttributes(params);

        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void hide() {
        super.hide();
    }

    public void setOnAddressSelectedListener(OnAddressSelectedListener listener) {
        this.selector.setOnAddressSelectedListener(listener);
    }

    public static BottomDialog show(Context context) {
        return show(context, null);
    }

    public static BottomDialog show(Context context, OnAddressSelectedListener listener) {
        BottomDialog dialog = new BottomDialog(context, R.style.bottom_dialog);
        dialog.selector.setOnAddressSelectedListener(listener);
        dialog.show();

        return dialog;
    }
}
