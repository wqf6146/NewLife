package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.RentDetailBean;
import com.yhkj.yymall.bean.RentTurnBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/31.
 */

public class PladgeDetailActivity extends BaseToolBarActivity {

    @Bind(R.id.ap_tv_shopname)
    TextView mTvShopName;

    @Bind(R.id.ap_tv_ordernumb)
    TextView mTvShopNumb;

    @Bind(R.id.ap_tv_pladgestatus)
    TextView mTvShopPladgeStatus;

    @Bind(R.id.ap_tv_losetime)
    TextView mTvLoseTime;

    @Bind(R.id.ap_tv_endtime)
    TextView mTvEndTime;

    @Bind(R.id.ap_tv_turnpladge)
    TextView mTvTurnPladge;

    @Bind(R.id.ap_tv_backpladge)
    TextView mTvBackPladge;

    @Bind(R.id.ap_ll_shopselect)
    LinearLayout mLlShopSelect;

    @Bind(R.id.ap_tv_shopselect)
    TextView mTvShopSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pladgedetail);
    }

    @Override
    protected void initView() {
        super.initView();
        mGoodgsId = getIntent().getStringExtra("id");
        String sLose = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(sLose) && sLose.equals("lose")){
            mTvTurnPladge.setVisibility(GONE);
            mTvBackPladge.setVisibility(GONE);
            mLlShopSelect.setVisibility(GONE);
        }

    }

    String mGoodgsId;
    private int mSelectGoodsNumb = 0;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mLlShopSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean == null) return;
                final String[] numbArr = new String[mDataBean.getNums()];
                for (int i=0;i<numbArr.length;i++){
                    numbArr[i] = String.valueOf(i+1);
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(PladgeDetailActivity.this);
                builder.setTitle("选择置换商品数量");
                builder.setItems(numbArr,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvShopSelect.setText(numbArr[which]);
                        mSelectGoodsNumb = which + 1;
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        });
        mTvTurnPladge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //置换
                if (mSelectGoodsNumb == 0){
                    showToast("请选择置换商品");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PladgeDetailActivity.this);
                builder.setTitle("置换确认");
                builder.setMessage("确定要置换该商品吗？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YYMallApi.doRentCash(PladgeDetailActivity.this, mGoodgsId,mSelectGoodsNumb, new YYMallApi.ApiResult<RentTurnBean>(PladgeDetailActivity.this) {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                ViseLog.e(e);
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onNext(RentTurnBean rentTurnBean) {
                                if (rentTurnBean.getCode() != 0){
                                    showToast(rentTurnBean.getMsg());
                                    return;
                                }
                                showToast("置换成功");
                                Intent intent = new Intent(PladgeDetailActivity.this,RentTurnSucActivity.class);
                                intent.putExtra("yy",String.valueOf(rentTurnBean.getData()));
                                startActivity(intent);
                                AppManager.getInstance().finishActivity(PladgeDetailActivity.this);
                            }
                        });
                    }
                });
                builder.show();
            }
        });

        mTvBackPladge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返还
                if (mDataBean == null )return;
                Intent intent = new Intent(PladgeDetailActivity.this,ApplyAfterMallActivity.class);
                intent.putExtra("orderGoodsId",mDataBean.getId());
                intent.putExtra("price",String.valueOf(mDataBean.getRealPrice()));
                intent.putExtra("numb",String.valueOf(getIntent().getStringExtra("numb")));
                startActivity(intent);
            }
        });
    }

    private RentDetailBean.DataBean mDataBean;
    @Override
    protected void initData() {
        setTvTitleText("押金详情");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
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
        YYMallApi.getRentDetailInfo(this, mGoodgsId, new YYMallApi.ApiResult<RentDetailBean.DataBean>(this) {
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
            public void onNext(RentDetailBean.DataBean dataBean) {
                mDataBean = dataBean;
                setData(dataBean);
                setNetWorkErrShow(GONE);
            }
        });
    }

//    private String getStatusString(int status){
//        String res = ""
//        switch (status){
//            case 1:
//                res = "待付款";
//                break;
//            case 2:
//                res = "已支付";
//                break;
//            case 5:
//                res = "已支付";
//                break;
//            case 6:
//                break;
//        }
//    }

    private void setData(RentDetailBean.DataBean dataBean) {
        mTvEndTime.setText(dataBean.getEndTime());
        mTvLoseTime.setText(dataBean.getPayTime());
        mTvShopNumb.setText(dataBean.getOrderNo());
        mTvShopPladgeStatus.setText("已扣");
        mTvShopName.setText(dataBean.getGoodsName());

    }
}
