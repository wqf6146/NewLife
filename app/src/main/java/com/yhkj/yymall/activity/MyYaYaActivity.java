package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.AssetBean;
import com.yhkj.yymall.bean.MyInteYaYaBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/12.
 */

public class MyYaYaActivity extends BaseToolBarActivity {

    @Bind(R.id.am_tv_myprice)
    TextView mTvMyPrice;

    @Bind(R.id.am_tv_userhelp)
    TextView mTvUserHelp;

    @Bind(R.id.am_ll_drawegetyy)
    LinearLayout mLlDrawGetYy;

    @Bind(R.id.am_ll_pladgegetyy)
    LinearLayout mLlPladgeGetYy;

    @Bind(R.id.am_ll_invati)
    LinearLayout mLlInvate;

//    @Bind(R.id.am_ll_byinvati)
//    LinearLayout mLlByInvate;

    @Bind(R.id.am_ll_usercreate)
    LinearLayout mLlUserCraete;

    @Bind(R.id.am_tv_usercreate)
    TextView mTvUserCreate;

    @Bind(R.id.am_ll_baby)
    LinearLayout mLlBaby;

    @Bind(R.id.am_tv_baby)
    TextView mTvBaby;

    @Bind(R.id.am_tv_inviteyaya)
    TextView mTvInteYaya;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myyy);
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
                startActivity(YaYaLogActivity.class);
            }
        });
        mTvUserHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyYaYaActivity.this,WebActivity.class);
                intent.putExtra("title","丫丫使用帮助");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.YAYABANGZHU);
                startActivity(intent);
            }
        });
        mLlDrawGetYy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LotteryActivity.class);
            }
        });
        mLlPladgeGetYy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RentPladgeActivity.class);
            }
        });
        mLlInvate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShareCodeActivity.class);
            }
        });
//        mLlByInvate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(ShareCodeActivity.class);
//            }
//        });
        mLlUserCraete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(YaYaLogActivity.class);
            }
        });
        mLlBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(UserDataActivity.class);
            }
        });
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private void getData() {
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
                mTvInteYaya.setText("+" + dataBean.getInfo().getYy().getInvite_reward());
                mTvUserCreate.setText("+" + dataBean.getInfo().getYy().getUser_register());
                mTvBaby.setText("+" + dataBean.getInfo().getYy().getFill_baby_info());
            }
        });
        YYMallApi.getUserAsset(this,false,new YYMallApi.ApiResult<AssetBean.DataBean>(this) {
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
            public void onNext(AssetBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                mTvMyPrice.setText(String.valueOf(dataBean.getYaya()));
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
        setTvTitleText("我的丫丫");
        setTvRightText("明细");
    }
}
