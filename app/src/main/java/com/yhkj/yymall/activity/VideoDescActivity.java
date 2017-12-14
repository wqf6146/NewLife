package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.VideoDescBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.viewpager.UltraViewPager;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/12/14.
 */

public class VideoDescActivity extends BaseToolBarActivity {

    @Bind(R.id.av_ultraviewpager)
    UltraViewPager mUltraViewPager;

    @Bind(R.id.av_tv_desc)
    TextView mTvDesc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodesc);
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("视频监控说明");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private void getData() {
        YYMallApi.getVideoDesc(this, new ApiCallback<VideoDescBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(VideoDescBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                mTvDesc.setText(dataBean.getExplain());
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(View.VISIBLE);
            }
        });
    }
}
