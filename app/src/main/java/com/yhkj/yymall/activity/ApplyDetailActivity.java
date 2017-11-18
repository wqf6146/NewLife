package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.ApplyDetailBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ApplyDetailActivity extends BaseToolBarActivity {

    @Bind(R.id.aa_tv_statusstring)
    TextView mTvStatusString;

    @Bind(R.id.aa_tv_price)
    TextView mTvPrice;

    @Bind(R.id.aa_tv_applystatus)
    TextView mTvApplyStatus;

    @Bind(R.id.il_view_line_bottom_1)
    View mViewLine_1;

    @Bind(R.id.il_view_line_bottom_2)
    View mViewLine_2;


    @Bind(R.id.aa_tv_applyprogress_1)
    TextView mTvApplyPregress_1;

    @Bind(R.id.aa_tv_applyprogress_2)
    TextView mTvApplyPregress_2;

    @Bind(R.id.aa_tv_applyprogress_3)
    TextView mTvApplyPregress_3;

    @Bind(R.id.il_tv_time_1)
    TextView mTvTime_1;

    @Bind(R.id.il_tv_time_2)
    TextView mTvTime_2;

    @Bind(R.id.il_tv_time_3)
    TextView mTvTime_3;

    @Bind(R.id.aa_tv_acount)
    TextView mTvAcount;

    @Bind(R.id.aa_tv_createtime)
    TextView mTvCreateTime;

    @Bind(R.id.aa_tv_applyno)
    TextView mTvApplyNo;

//    @Bind(R.id.aa_ll_progress_2)
//    LinearLayout mLlProgress_2;

    @Bind(R.id.aa_ll_progress_3)
    LinearLayout mLlProgress_3;

    @Bind(R.id.aa_rl_progress)
    RelativeLayout mRlProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applydetail);
    }

    private int mId;

    @Override
    protected void initView() {
        super.initView();
        mId = getIntent().getIntExtra("id",0);
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
        YYMallApi.getApplyDetail(this, mId, new YYMallApi.ApiResult<ApplyDetailBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ApplyDetailBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                initUi(dataBean);
            }
        });
    }

    private void initUi(ApplyDetailBean.DataBean dataBean) {
        //提现方式1为支付宝2为微信
        mRlProgress.setVisibility(View.VISIBLE);
        mTvStatusString.setText( dataBean.getType() == 1 ? "支付宝" : "微信");
        mTvPrice.setText(dataBean.getAmount() + "元");
        mTvAcount.setText((dataBean.getType() == 1 ? "支付宝:" : "微信:" )+ dataBean.getAccount());
        mTvCreateTime.setText(dataBean.getTime());
        switch (dataBean.getStatus()){
            case -1:
                mTvApplyStatus.setText("失败");
                break;
            case 0:
                mTvApplyStatus.setText("未处理");

                mLlProgress_3.setVisibility(GONE);
                mViewLine_2.setVisibility(GONE);
                mTvTime_1.setText(dataBean.getTime());
                mTvTime_2.setText(dataBean.getTime());
                break;
            case 1:
                mTvApplyStatus.setText("处理中");

                mLlProgress_3.setVisibility(GONE);
                mViewLine_2.setVisibility(GONE);
                mTvTime_1.setText(dataBean.getTime());
                mTvTime_2.setText(dataBean.getTime());
                break;
            case 2:
                mTvApplyStatus.setText("提现成功");
                mTvTime_1.setText(dataBean.getTime());
                mTvTime_2.setText(dataBean.getTime());
                mTvTime_3.setText(dataBean.getHandleTime());
                break;
        }
    }

    @Override
    protected void initData() {
        setTvTitleText("详情");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
