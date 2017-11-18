package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vise.xsnow.manager.AppManager;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/5.
 */

public class PayResultActivity extends BaseToolBarActivity {
    @Bind(R.id.tv_payresult_backhome)
    TextView tv_payresult_backhome;

    @Bind(R.id.tv_payresult_money)
    TextView tv_payresult_money;

    @Bind(R.id.tv_payresult_look)
    TextView tv_payresult_look;

    @Bind(R.id.ap_tv_resstring)
    TextView mTvResString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payresult);


    }

    @Override
    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
        AppManager.getInstance().finishExceptActivity(MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.ALL);
        startActivity(MineOrderActivity.class, bundle);
    }


    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("支付结果");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
        setTvRightVisiable(View.VISIBLE);
        String total = getIntent().getStringExtra("total");
        if (!TextUtils.isEmpty(total) && total.equals("-1")){
            //中奖结果
            tv_payresult_money.setVisibility(GONE);
            mTvResString.setText("领取成功");
        }else if (!TextUtils.isEmpty(total) && total.equals("-2")){
            //积分
            mTvResString.setText("下单成功");
            int integral = getIntent().getIntExtra("integralId",0);
            tv_payresult_money.setText(integral + "积分");
        }else if (!TextUtils.isEmpty(total) && total.equals("-3")){
            //货到付款
            mTvResString.setText("下单成功");
            tv_payresult_money.setVisibility(GONE);
        }else{
            mTvResString.setText("支付成功");
            tv_payresult_money.setText(total + "元");
        }

        tv_payresult_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishExceptActivity(MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.ALL);
                startActivity(MineOrderActivity.class, bundle);
                finish();
            }
        });

        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishExceptActivity(MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.ALL);
                startActivity(MineOrderActivity.class, bundle);
            }
        });

        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishExceptActivity(MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.ALL);
                startActivity(MineOrderActivity.class, bundle);
            }
        });
        tv_payresult_backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYApp.getInstance().setIndexShow(0);
                startActivity(MainActivity.class);
                AppManager.getInstance().finishExceptActivity(MainActivity.class);

            }
        });
    }
}
