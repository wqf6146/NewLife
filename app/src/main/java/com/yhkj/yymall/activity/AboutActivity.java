package com.yhkj.yymall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GetServerBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/3.
 */

public class AboutActivity extends BaseToolBarActivity {

    @Bind(R.id.aa_rl_jies)
    RelativeLayout mRlJies;

    @Bind(R.id.aa_rl_xieyi)
    RelativeLayout mRlXieYi;

    @Bind(R.id.aa_rl_server)
    RelativeLayout mRlServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    private GetServerBean.DataBean mDataBean;
    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        YYMallApi.getService(this, new YYMallApi.ApiResult<GetServerBean.DataBean>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onNext(GetServerBean.DataBean dataBean) {
                mDataBean = dataBean;
            }

            @Override
            public void onStart() {

            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("关于YiYiYaYa");
        setTvRightVisiable(GONE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();


        mRlServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (CommonUtil.isQQClientAvailable(AboutActivity.this) && mDataBean!=null )
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + mDataBean.getInfo().getQq() + "")));
//                else
//                    showToast("请先安装QQ");
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }
                startActivity(MyServiceActivity.class);
            }
        });

        mRlJies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("title","公司介绍");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.GONGSIJIESHAO);
                startActivity(intent);
            }
        });
        mRlXieYi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,WebActivity.class);
                intent.putExtra("title","服务协议");
                intent.putExtra(Constant.WEB_TAG.TAG,ApiService.YYWEB + Constant.WEB_TAG.FUWUXIEYI);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }
}
