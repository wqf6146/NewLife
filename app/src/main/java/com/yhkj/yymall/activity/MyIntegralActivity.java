package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.AssetBean;
import com.yhkj.yymall.bean.MyInteYaYaBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/12.
 */

public class MyIntegralActivity extends BaseToolBarActivity {

    @Bind(R.id.am_tv_myinte)
    TextView mTvInte;

    @Bind(R.id.am_tv_shopeva)
    TextView mTvShopEva;

    @Bind(R.id.am_tv_msg)
    TextView mTvMsg;

    @Bind(R.id.am_ll_sign)
    LinearLayout mLlSign;

    @Bind(R.id.am_ll_msg)
    LinearLayout mLlMsg;

    @Bind(R.id.am_ll_buyshop)
    LinearLayout mLlBusShop;

    @Bind(R.id.am_ll_shopeva)
    LinearLayout mLlShopEva;

    @Bind(R.id.am_ll_draw)
    LinearLayout mLlDraw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myintegral);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IntegralLogActivity.class);
            }
        });

        mLlBusShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYApp.getInstance().setIndexShow(0);
                startActivity(MainActivity.class);
            }
        });
        mLlSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYApp.getInstance().setIndexShow(0);
                startActivity(MainActivity.class);
            }
        });
        mLlMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UserDataActivity.class);
            }
        });
        mLlShopEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE);
                startActivity(MineOrderActivity.class, bundle);
            }
        });
        mLlDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LotteryActivity.class);
            }
        });
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private void getData() {
        YYMallApi.getUserAsset(this, false,new YYMallApi.ApiResult<AssetBean.DataBean>(this) {
            @Override
            public void onStart() {
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(AssetBean.DataBean bean) {
                setNetWorkErrShow(View.GONE);
                mTvInte.setText(String.valueOf(bean.getPoint()));
            }
        });
        YYMallApi.getMyIntYaYa(this, new ApiCallback<MyInteYaYaBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(MyInteYaYaBean.DataBean dataBean) {
                mTvMsg.setText("+" + dataBean.getInfo().getPoints().getFill_my_info());
                mTvShopEva.setText("+" + dataBean.getInfo().getPoints().getComment_point());
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("我的积分");
        setTvRightText("明细");
    }
}
