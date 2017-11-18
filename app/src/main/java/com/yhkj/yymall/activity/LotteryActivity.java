package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.LotteryResBean;
import com.yhkj.yymall.bean.LottoryInfoBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.view.LotteryLayout;
import com.yhkj.yymall.view.popwindows.DrawLottePopup;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/1.
 */

public class LotteryActivity extends BaseToolBarActivity implements LotteryLayout.OnLotteryLisiten,DrawLottePopup.DrawPopViewCallback {

    @Bind(R.id.al_lotterylayout)
    LotteryLayout mLotteryLayout;

    @Bind(R.id.al_tv_jifen)
    TextView mTvIntegral;

    @Bind(R.id.al_tv_checkout)
    TextView mTvCheckOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LotteryActivity.this,WebActivity.class);
                intent.putExtra("title","抽奖规则");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.DRAW);
                startActivity(intent);
            }
        });
    }


    private boolean mStartGame = false;
    private int mCurPoint;
    private int mOnceTimePoint;
    @Override
    public void onStartLottery() {
        if (!mStartGame && mDataBean!=null){
            int point = mCurPoint;

            if (point == 0){
                new DrawLottePopup(LotteryActivity.this,null,"没有积分啦").setDrawPopViewCallback(LotteryActivity.this).showPopupWindow();
                return;
            }else if (point < mOnceTimePoint){
                new DrawLottePopup(LotteryActivity.this,null,"积分不足啦").setDrawPopViewCallback(LotteryActivity.this).showPopupWindow();
                return;
            }

            point -= mOnceTimePoint;
            mTvIntegral.setText(String.format(getString(R.string.myintegral),String.valueOf(point)));
            YYMallApi.startLottery(this, mDataBean.getRollinfo().getRollid(), new YYMallApi.ApiResult<LotteryResBean.DataBean>(this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    mStartGame = false;
                    ViseLog.e(e);
                    mTvIntegral.setText(String.format(getString(R.string.myintegral),String.valueOf(mCurPoint)));
                    if (e.getCode() == 6002){
                        //积分不足
                        new DrawLottePopup(LotteryActivity.this,null,null).setDrawPopViewCallback(LotteryActivity.this).showPopupWindow();
                    }else{
                        showToast(e.getMessage());
                    }
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(LotteryResBean.DataBean dataBean) {
                    mResBean = dataBean;
                    mCurPoint = mResBean.getResult().getPoint();
                    String resId = String.valueOf(dataBean.getResult().getPrizeid());
                    int tag = -1;
                    for (int i=0; i< mDataBean.getPrizeinfo().size(); i++){
                        LottoryInfoBean.DataBean.PrizeinfoBean bean = mDataBean.getPrizeinfo().get(i);
                        if (resId.equals(bean.getId())){
                            tag = i;
                            Log.e("tag",tag+"|" + bean.getTitle());
                            break;
                        }
                    }

                    if (tag != -1) {
                        mLotteryLayout.startRunLottery(tag);
                        mStartGame = true;
                    }
                }
            });
        }
    }

    @Override
    public void doGetShop() {
        //领取商品
        Intent intent = new Intent(this,CheckOutActivity.class);
        intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.LOTTER);
        intent.putExtra("productId",String.valueOf(mResBean.getResult().getPrizeid()));
        startActivity(intent);
    }

    @Override
    public void onecAgainst() {
        //再来一次
        onStartLottery();
    }

    @Override
    public void doGetInteg() {
        //领积分
        startActivity(MyIntegralActivity.class);
    }

    @Override
    public void onEndLottery() {
        mStartGame = false;
        mTvIntegral.setText(String.format(getString(R.string.myintegral),String.valueOf(mCurPoint)));
        new DrawLottePopup(this,mResBean.getResult(),null).setDrawPopViewCallback(this).showPopupWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLotteryLayout.onDestroy();
    }

    private LottoryInfoBean.DataBean mDataBean;
    private LotteryResBean.DataBean mResBean;

    @Override
    protected void initData() {
        setTvTitleText("积分抽奖");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        view_btx.setVisibility(View.INVISIBLE);

        getData();
        mLotteryLayout.setLotteryLisiten(this);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void getData() {
        YYMallApi.getLotteryInfo(this, new YYMallApi.ApiResult<LottoryInfoBean.DataBean>(LotteryActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                if (e.getCode() == 6001){
                    setNoDataView(R.mipmap.ic_nor_orderbg,e.getMessage());
                }else{
                    setNetWorkErrShow(View.VISIBLE);
                    showToast(e.getMessage());
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(LottoryInfoBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                mCurPoint = Integer.parseInt(dataBean.getMypoint());
                mOnceTimePoint = dataBean.getRollinfo().getPoint();
                mTvIntegral.setText(String.format(getString(R.string.myintegral),dataBean.getMypoint()));
                mDataBean =  dataBean;
                mLotteryLayout.setInfo(dataBean);
            }
        });
    }
}
