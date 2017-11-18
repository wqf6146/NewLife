package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.RentRecordBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/1.
 */

public class BackPladgeActivity extends BaseToolBarActivity {

//    @Bind(R.id.ar_img_allselect)
//    ImageView mImgAllSelect;

    @Bind(R.id.ar_listview)
    NestFullListView mNestListView;

    @Bind(R.id.ar_tv_commit)
    TextView mTvCommit;

    @Bind(R.id.ar_tv_cancel)
    TextView mTvCancel;

    @Bind(R.id.vrr_tv_tip)
    TextView mTvTip;

    @Bind(R.id.vrr_tv_btn)
    TextView mTvTurnBtn;

    @Bind(R.id.ab_webview)
    WebView mWebView;

    @Bind(R.id.vrr_rl_nodata)
    RelativeLayout mRlNoData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backpladge);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(BackPladgeActivity.this);
            }
        });
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTurnYY();
            }
        });
        mTvTurnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYApp.getInstance().setIndexShow(1);
                startActivity(MainActivity.class);
                AppManager.getInstance().finishExceptActivity(MainActivity.class);

            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("返还押金");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        mWebView.loadUrl(ApiService.YYWEB + Constant.WEB_TAG.YAJINFANHUAN);
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
        YYMallApi.getRentRecord(this,"0", "1", new YYMallApi.ApiResult<RentRecordBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(RentRecordBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (dataBean.getRentals() == null  || dataBean.getRentals().size() == 0){
                    mRlNoData.setVisibility(View.VISIBLE);
                    mTvTip.setText("您暂无押金");
                    mTvTurnBtn.setText("逛逛热门租赁");
                    return;
                }
                mRlNoData.setVisibility(GONE);
                getItemData(dataBean.getRentals());
                mNestListView.setAdapter(new NestFullListViewAdapter<RentRecordBean.DataBean.RentalsBean.GoodsesBean>(R.layout.item_rentshop,mDatas) {
                    @Override
                    public void onBind(int pos, final RentRecordBean.DataBean.RentalsBean.GoodsesBean bean, final NestFullViewHolder holder) {

//                        if (bean.getGoodsId())

                        holder.setVisible(R.id.ir_img_select,true);
                        if (mSelectGoods == 0 || mSelectGoods != bean.getId()){
                            holder.setImageResource(R.id.ir_img_select,R.mipmap.ic_nor_graycicle);
                        }else{
                            holder.setImageResource(R.id.ir_img_select,R.mipmap.ic_nor_bluenike);
                        }
                        holder.getView(R.id.irs_img_arrow).setVisibility(GONE);

                        Glide.with(BackPladgeActivity.this).load(bean.getGoodsImg()).into((ImageView)holder.getView(R.id.is_img_shop));

                        holder.setOnClickListener(R.id.ir_img_select, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //
                                mSelectGoods = bean.getId();
                                mNestListView.updateUI();
                            }
                        });
                        holder.setText(R.id.fn_tv_shopname_hor_2,bean.getGoodsName());
                        holder.setVisible(R.id.ir_tv_yy,false);
                        holder.setText(R.id.fn_tv_shopprice_hor_2,String.valueOf(bean.getRealPrice())  + "元 x" +bean.getGoodsNums());
                        holder.setText(R.id.is_shop_rentNumb,"(共" + bean.getGoodsNums() + "件)");
                    }
                });
            }
        });
    }

    private int mSelectGoods=0;
    private int mRes = 0;
    public void doTurnYY(){
        if (mSelectGoods==0){
            showToast("请选择要返还的商品");
            return;
        }
        Intent intent = new Intent(this,ApplyAfterMallActivity.class);
        intent.putExtra("orderGoodsId",mSelectGoods);
        startActivity(intent);
//        YYMallApi.doRentCash(BackPladgeActivity.this, mSelectGoods, new ApiCallback<RentTurnBean>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onError(ApiException e) {
//                ViseLog.e(e);
//                showToast(e.getMessage());
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onNext(RentTurnBean rentTurnBean) {
//                for (int i=0;i<mSelectGoods.size();i++){
//                    if (mSelectGoods.get(i).equals(goodsid)){
//                        mSelectGoods.remove(i);
//                    }
//                }
//                getData();
//            }
//        });
    }

    private List<RentRecordBean.DataBean.RentalsBean.GoodsesBean> mDatas;
    public void getItemData(List<RentRecordBean.DataBean.RentalsBean> dataBean) {
        if (dataBean.size() == 0)
            return ;
        mDatas = new ArrayList<>();
        for (int i=0; i <dataBean.size(); i++){
            for (int j = 0;j <dataBean.get(i).getGoodses().size(); j++){
                mDatas.add(dataBean.get(i).getGoodses().get(j));
            }
        }
    }
}
