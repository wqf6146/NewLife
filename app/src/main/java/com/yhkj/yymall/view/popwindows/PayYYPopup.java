package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;

/**
 */
public class PayYYPopup extends BasePopupWindow implements View.OnClickListener {

    private EditText ed_checkout;
    private TextView tv_checkout_diss, tv_checkout_ok, tv_checkout_sub, tv_checkout_add,vpp_tv_yayamax,vpp_tv_yayamin;
    private int mCurSelectNumb = 0;
    private int maxNumb;
    private MyTextWathcer myTextWathcer;
    private YYcall yYcall;

    public PayYYPopup(Activity context, YYcall yYcall, int maxNumb,int myYaya) {
        super(context);
        ed_checkout = (EditText) findViewById(R.id.ed_checkout);
        tv_checkout_diss = (TextView) findViewById(R.id.tv_checkout_diss);
        tv_checkout_ok = (TextView) findViewById(R.id.tv_checkout_ok);
        tv_checkout_sub = (TextView) findViewById(R.id.tv_checkout_sub);
        tv_checkout_add = (TextView) findViewById(R.id.tv_checkout_add);
        vpp_tv_yayamin = (TextView)findViewById(R.id.vpp_tv_yayamin);
        vpp_tv_yayamax = (TextView)findViewById(R.id.vpp_tv_yayamax);
        this.yYcall = yYcall;
        this.maxNumb = maxNumb;
        mCurSelectNumb = maxNumb;
        ed_checkout.setText(String.valueOf(mCurSelectNumb));
        tv_checkout_diss.setOnClickListener(this);
        tv_checkout_ok.setOnClickListener(this);
        tv_checkout_sub.setOnClickListener(this);
        tv_checkout_add.setOnClickListener(this);
        myTextWathcer = new MyTextWathcer();
        ed_checkout.addTextChangedListener(myTextWathcer);

        vpp_tv_yayamin.setText("使用丫丫付款（剩余丫丫" + myYaya +"）");
        vpp_tv_yayamax.setText("1丫丫=1元，最多可用" + maxNumb  + "丫丫");
    }

    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(250 * 2, 0, 300);
        return animation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_popup_payyy);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_animas);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_checkout_diss:
                dismiss();
                break;
            case R.id.tv_checkout_ok:
                if (yYcall != null) {
                    yYcall.send(mCurSelectNumb);
                }
                dismiss();
                break;
            case R.id.tv_checkout_sub:
                if (mCurSelectNumb > 1) {
                    mCurSelectNumb--;
                    ed_checkout.setText(mCurSelectNumb + "");
                } else {
                    ed_checkout.setText("");
                }
                break;
            case R.id.tv_checkout_add:
                if (mCurSelectNumb < maxNumb) {
                    mCurSelectNumb++;
                    ed_checkout.setText(mCurSelectNumb + "");
                }
                break;
            default:
                break;
        }
    }

    private class MyTextWathcer implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                mCurSelectNumb = Integer.valueOf(s.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mCurSelectNumb > maxNumb) {
                ed_checkout.removeTextChangedListener(this);
                setMyText();
            }
        }
    }

    private void setMyText() {
        mCurSelectNumb = maxNumb;
        ed_checkout.setText(mCurSelectNumb + "");
        ed_checkout.addTextChangedListener(myTextWathcer);
    }

    public interface YYcall {
        void send(int numb);
    }

}
