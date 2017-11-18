package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.ApplyDetailBean;
import com.yhkj.yymall.bean.ApplyListBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ApplyListActivity extends BaseToolBarActivity {

    @Bind(R.id.aa_recycleview)
    RecyclerView mRecycleView;

//    @Bind(R.id.vrr_rl_nodata)
//    RelativeLayout mRlNOData;
//
//    @Bind(R.id.vrr_img_nodata)
//    ImageView mImgNoData;
//
//    @Bind(R.id.vrr_tv_tip)
//    TextView mTvTip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applylist);
    }

    @Override
    protected void initView() {
        super.initView();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void getData(){
        YYMallApi.getApplyWithdrawList(this, new YYMallApi.ApiResult<ApplyListBean.DataBean>(this) {
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
            public void onNext(final ApplyListBean.DataBean dataBean) {
                if (dataBean == null || dataBean.getWithdrawList() == null || dataBean.getWithdrawList().size()==0){
                   setNoDataView(R.mipmap.ic_nor_orderbg,"暂无提现");
                    return;
                }
                setNetWorkErrShow(View.GONE);
                mRecycleView.setAdapter(new CommonAdapter<ApplyListBean.DataBean.WithdrawListBean>(ApplyListActivity.this,R.layout.item_log,dataBean.getWithdrawList()) {
                    @Override
                    protected void convert(ViewHolder holder,final ApplyListBean.DataBean.WithdrawListBean bean, int position) {
                        holder.setText(R.id.il_tv_title,"提现");
                        holder.setText(R.id.il_tv_time,bean.getTime());

                        //提现状态-1失败,0未处理,1处理中,2成功
                        switch (bean.getStatus()){
                            case -1:
                                holder.setText(R.id.il_tv_balance,"失败");
                                holder.setTextColor(R.id.il_tv_balance,getResources().getColor(R.color.redfont));
                                break;
                            case 0:
                                holder.setText(R.id.il_tv_balance,"待处理");
                                holder.setTextColor(R.id.il_tv_balance,getResources().getColor(R.color.redfont));
                                break;
                            case 1:
                                holder.setText(R.id.il_tv_balance,"处理中");
                                holder.setTextColor(R.id.il_tv_balance,getResources().getColor(R.color.redfont));
                                break;
                            case 2:
                                holder.setText(R.id.il_tv_balance,"已完成");
                                holder.setTextColor(R.id.il_tv_balance,getResources().getColor(R.color.theme_bule));
                                break;
                        }
                        holder.setText(R.id.il_tv_dyn,bean.getAmount() + "元");
                        holder.setVisible(R.id.il_tv_dyn,true);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ApplyListActivity.this, ApplyDetailActivity.class);
                                intent.putExtra("id",bean.getId());
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void initData() {
        setTvTitleText("提现明细");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
