package com.yhkj.yymall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.GetServerBean;
import com.yhkj.yymall.bean.MessageCatorBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/24.
 */

public class NewMessageActivity extends BaseToolBarActivity {

//    @Bind(R.id.an_ll_syslogist)
//    LinearLayout mLlSysLogist;
//
//    @Bind(R.id.an_ll_sysmes)
//    LinearLayout mLlSysMes;
//
//    @Bind(R.id.an_ll_sysprice)
//    LinearLayout mLlSysPrice;
//
//    @Bind(R.id.an_ll_yiyiyaya)
//    LinearLayout mLLYiYiyaya;
//
//    @Bind(R.id.an_tv_logitcount)
//    TextView mTvLogitCount;
//
//    @Bind(R.id.an_tv_mescount)
//    TextView mTvMesCount;
//
//    @Bind(R.id.an_tv_pricecount)
//    TextView mTvPriceCount;

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;


     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_refresh_recycleview);
    }

    @Override
    protected void initView() {
        super.initView();
        initRefreshLayout();
    }

    @Override
    protected void initData() {
        setTvTitleText("消息中心");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }

    private GetServerBean.DataBean mDataBean;
    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null);
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
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData(null);
    }

    private void initRefreshLayout() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableOverScrollBounce(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshLayout.setLoadmoreFinished(false);
                getData(refreshlayout);
            }
        });
    }

    private void getData(final RefreshLayout refreshlayout) {
        YYMallApi.getMesClassifyList(this, new YYMallApi.ApiResult<MessageCatorBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
                if (refreshlayout!=null) refreshlayout.finishRefresh();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(MessageCatorBean.DataBean dataBean) {
                if (refreshlayout!=null) refreshlayout.finishRefresh();
                setNetWorkErrShow(GONE);
                setUiBean(dataBean);
            }

        });
    }

    private void setUiBean(MessageCatorBean.DataBean dataBean) {
        CommonAdapter commonAdapter = new CommonAdapter<MessageCatorBean.DataBean.ListBean>(this,R.layout.item_mes_child,dataBean.getList()) {
            @Override
            protected void convert(ViewHolder holder, MessageCatorBean.DataBean.ListBean bean, int position) {
                //类型 1=>系统通知2=>我的资产3=>物流通知
                if (bean.getCount() == 0){
                    holder.setVisible(R.id.imc_tv_count,false);
                }else{
                    holder.setText(R.id.imc_tv_count,bean.getCount() > 99 ? "99+" : String.valueOf(bean.getCount()));
                }
                switch (bean.getType()){
                    case 1:
                        holder.setImageResource(R.id.imc_img_icon,R.mipmap.ic_nor_messystem);
                        holder.setText(R.id.imc_tv_title,"系统通知");
//                        holder.setText(R.id.imc_tv_count,String.valueOf(bean.getCount()));
                        holder.setOnClickListener(R.id.imc_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(NewMessageActivity.this,MessageActivity.class),0);
                            }
                        });
                        break;
                    case 2:
                        holder.setImageResource(R.id.imc_img_icon,R.mipmap.ic_nor_mesprice);
                        holder.setText(R.id.imc_tv_title,"我的资产");
//                        holder.setText(R.id.imc_tv_count,String.valueOf(bean.getCount()));
                        holder.setOnClickListener(R.id.imc_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(NewMessageActivity.this,MesMyPriceAcitvity.class),0);
                            }
                        });
                        break;
                    case 3:
                        holder.setImageResource(R.id.imc_img_icon,R.mipmap.ic_nor_meslogits);
                        holder.setText(R.id.imc_tv_title,"物流通知");

                        holder.setOnClickListener(R.id.imc_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivityForResult(new Intent(NewMessageActivity.this,MesMyLogitAcitvity.class),0);
                            }
                        });
                        break;
                }
            }
        };
        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        View view = LayoutInflater.from(this).inflate(R.layout.item_mes_official,mRecycleView,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //联系官方客服
//                if (CommonUtil.isQQClientAvailable(NewMessageActivity.this) && mDataBean!=null && mDataBean.getInfo()!=null)
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + mDataBean.getInfo().getQq() + "")));
//                else
//                    showToast("请先安装QQ");
                Intent intent = new Intent();
//                intent.putExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY, index);
//                intent.putExtra(Constant.MESSAGE_TO_INTENT_EXTRA, Constant.MESSAGE_TO_AFTER_SALES);
                intent.setClass(NewMessageActivity.this, ChatLoginActivity.class);
                startActivity(intent);
            }
        });
        headerAndFooterWrapper.addFootView(view);
        mRecycleView.setAdapter(headerAndFooterWrapper);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
