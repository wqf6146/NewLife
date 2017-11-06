package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.vise.xsnow.util.InputMethodUtils;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.ForgetPSWActivity;
import com.yhkj.yymall.activity.PayResultActivity;
import com.yhkj.yymall.activity.UpdatepwdActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.PasswordView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Administrator on 2017/7/15.
 */

public class PaymentPopupView extends BasePopupWindow {

    private PasswordView aspp_passwordview_payment;
    private ImageView img_paymentpop_cloes;
    private TextView tv_payment_forget;

    private int mPwdSize = 6;
    private CharSequence mPassword;
    private Activity context;
    private String orderNo, total;

    public PaymentPopupView(final Activity contexts, String orderNos, String totals) {
        super(contexts);
        aspp_passwordview_payment = (PasswordView) findViewById(R.id.aspp_passwordview_payment);
        img_paymentpop_cloes = (ImageView) findViewById(R.id.img_paymentpop_cloes);
        tv_payment_forget = (TextView) findViewById(R.id.tv_payment_forget);
        this.context = contexts;
        orderNo = orderNos;
        total = totals;
        tv_payment_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexts,UpdatepwdActivity.class);
                intent.putExtra(Constant.PWDTYPE.TYPE,Constant.PWDTYPE.PAY);
                contexts.startActivity(intent);
            }
        });

        img_paymentpop_cloes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithOutAnima();
            }
        });
        aspp_passwordview_payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, final int count) {
                if (s.length() < mPwdSize) {
                    mPassword = s;
                    aspp_passwordview_payment.setPassword(mPassword);
                } else if (s.length() == mPwdSize) {
                    mPassword = s;
                    aspp_passwordview_payment.setPassword(mPassword);
                    YYMallApi.getPayBalance(context, mPassword.toString(), orderNo, total, new YYMallApi.ApiResult<CommonBean>(context) {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(ApiException e) {
                            super.onError(e);
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            InputMethodUtils.showInputMethod(context);
                            dismiss();
                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(CommonBean commonBean) {
                            if (commonBean.getCode() == 0) {
                                Intent intent = new Intent(getContext(), PayResultActivity.class);
                                intent.putExtra("total", total);
                                context.startActivity(intent);
                                context.finish();
                            } else {
                                Toast.makeText(contexts, commonBean.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                            InputMethodUtils.showInputMethod(context);
                            dismiss();

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public EditText getInputView() {
        return aspp_passwordview_payment;
    }

    /**
     * PopupWindow展示出来后，需要执行动画的View.一般为蒙层之上的View
     */
    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(250 * 2, 0, 300);
        return animation;
    }

    /**
     * 设置一个点击后触发dismiss PopupWindow的View，一般为蒙层
     */
    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_popup_payment);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.rl_paymentpop_animas);
    }

}
