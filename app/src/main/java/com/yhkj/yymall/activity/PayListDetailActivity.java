package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.PayDetailBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/25.
 */

public class PayListDetailActivity extends BaseToolBarActivity {

    @Bind(R.id.ar_tv_price)
    TextView mTvPrice;

    @Bind(R.id.ar_tv_paytype)
    TextView mTvPayType;

    @Bind(R.id.ar_tv_time)
    TextView mTvTime;

    @Bind(R.id.ar_tv_type)
    TextView mTvType;

    @Bind(R.id.ar_tv_balance)
    TextView mTvBalance;

    @Bind(R.id.ap_ll_paytype)
    LinearLayout mLlPayType;

    @Bind(R.id.ap_ll_no)
    LinearLayout mLlNo;

    @Bind(R.id.ar_tv_no)
    TextView mTvNo;

    @Bind(R.id.ap_tv_paytag)
    TextView mTvPayTag;

    @Bind(R.id.ap_tv_ordertag)
    TextView mTvOrderTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylistdetail);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void getData() {
        YYMallApi.getPayDeatail(this, getIntent().getIntExtra("id", 0), new YYMallApi.ApiResult<PayDetailBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PayDetailBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setUidata(dataBean);
            }

        });
    }
    private String getPayRoad(int type){
        String str = "";
        switch (type){
            case 1:
                str = "充值";
                mTvPayTag.setText("充值方式");
                break;
            case 2:
                str = "提现";
                mTvPayTag.setText("提现方式");
                break;
            case 3:
                str = "支付";
                mTvOrderTag.setText("订单编号");
                break;
            case 4:
                str = "退款";
                mTvOrderTag.setText("订单编号");
                break;
        }
        return str;
    }
    private void setUidata(PayDetailBean.DataBean dataBean) {
        //类型1充值到余额2从余额提现3从余额支付4退款到余额
        int type = dataBean.getInfo().getEvent();
        if (type == 2){
            mLlNo.setVisibility(GONE);
        }else{
            if (!TextUtils.isEmpty(dataBean.getInfo().getOrder_no())){
                mLlNo.setVisibility(VISIBLE);
                mTvNo.setText(dataBean.getInfo().getOrder_no());
            }
        }
        if (type == 3 || type == 4){
            mLlPayType.setVisibility(GONE);
        }else{
            mLlPayType.setVisibility(VISIBLE);
            mTvPayType.setText(dataBean.getInfo().getPayment());
        }
        mTvPrice.setText(dataBean.getInfo().getAmount() + "元");
        mTvBalance.setText(dataBean.getInfo().getAmount_log() + "元");
        mTvTime.setText(dataBean.getInfo().getTime());
        mTvType.setText(getPayRoad(dataBean.getInfo().getEvent()));
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("详情");

    }
}
