package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yhkj.yymall.bean.RentTurnBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.NumberPickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/1.
 */

public class RentPladgeActivity extends BaseToolBarActivity {

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

    @Bind(R.id.vrr_rl_nodata)
    RelativeLayout mRlNoData;

    @Bind(R.id.ar_webview)
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentpladge);
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
                AppManager.getInstance().finishActivity(RentPladgeActivity.this);
            }
        });
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mSelectGoods)){
                    showToast("请选择要置换的商品");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(RentPladgeActivity.this);
                builder.setTitle("置换确认");
                builder.setMessage("确定要置换该商品吗？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTurnYY();
                    }
                });
                builder.show();
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

//    int mSelect = 0;   // 1-全选 -1-全部选

    @Override
    protected void initData() {
        setTvTitleText("置换押金");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        getData();
        mWebView.loadUrl(ApiService.YYWEB + Constant.WEB_TAG.YAJINZHIHUANYAYA);

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
            public void onNext(final RentRecordBean.DataBean dataBean) {
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
                        holder.setVisible(R.id.ir_img_select,true);
                        if (mSelectGoods ==null || !mSelectGoods.equals(String.valueOf(bean.getId()))){
                            holder.setImageResource(R.id.ir_img_select,R.mipmap.ic_nor_graycicle);
                            holder.getView(R.id.irs_ll_select).setVisibility(GONE);
                        }else{
                            holder.setImageResource(R.id.ir_img_select,R.mipmap.ic_nor_bluenike);
                            holder.getView(R.id.irs_ll_select).setVisibility(VISIBLE);
                            ((NumberPickerView)holder.getView(R.id.iva_addview)).setMaxValue(bean.getGoodsNums())
                                    .setCurrentInventory(bean.getGoodsNums()) //租赁限制购买数量
                                    .setMinDefaultNum(1)
                                    .setCurrentNum(1)
                                    .setmOnClickInputListener(new NumberPickerView.OnClickInputListener() {
                                        @Override
                                        public String onIsCanClick() {
                                            return "";
                                        }
                                        @Override
                                        public void onSelectDone(int value) {
                                            mSelectGoodsNumb = value;
                                        }

                                        @Override
                                        public void onWarningForInventory(int inventory) {
                                            Toast.makeText(RentPladgeActivity.this,"超过最大数量",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onWarningMinInput(int minValue) {
//                                            Toast.makeText(RentPladgeActivity.this,"超过最大数量",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onWarningMaxInput(int maxValue) {
                                            Toast.makeText(RentPladgeActivity.this,"超过最大数量",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        holder.getView(R.id.irs_img_arrow).setVisibility(GONE);

                        Glide.with(RentPladgeActivity.this).load(bean.getGoodsImg()).into((ImageView)holder.getView(R.id.is_img_shop));

                        holder.setOnClickListener(R.id.ir_img_select, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //
                                mSelectGoods = String.valueOf(bean.getId());
                                mNestListView.updateUI();
                            }
                        });
                        holder.setText(R.id.fn_tv_shopname_hor_2,bean.getGoodsName());
                        holder.setText(R.id.fn_tv_shopprice_hor_2,"¥" + String.valueOf(bean.getRealPrice())+ "x" +bean.getGoodsNums());
                        Double totalprice = bean.getRealPrice() * bean.getGoodsNums();
                        holder.setText(R.id.ir_tv_yy,totalprice + "元押金可置换" + Math.ceil(totalprice) + "丫丫");
//                        holder.setText(R.id.fn_tv_shopprice_hor_2,String.valueOf(bean.getRealPrice()) + "元");
//                        holder.setVisible(R.id.is_shop_rentNumb,true);
//                        holder.setText(R.id.is_shop_rentNumb,"×" + bean.getGoodsNums());


                    }
                });
            }
        });
    }

    String mSelectGoods;
    int mSelectGoodsNumb = 1;
    public void doTurnYY(){
        YYMallApi.doRentCash(RentPladgeActivity.this, mSelectGoods,mSelectGoodsNumb, new YYMallApi.ApiResult<RentTurnBean>(RentPladgeActivity.this) {
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
                if (rentTurnBean.getCode() == 0) {
                    showToast("置换成功");
                    Intent intent = new Intent(RentPladgeActivity.this, RentTurnSucActivity.class);
                    intent.putExtra("yy", String.valueOf(rentTurnBean.getData()));
                    startActivity(intent);
                    AppManager.getInstance().finishActivity(RentPladgeActivity.this);
                }else{
                    showToast(rentTurnBean.getMsg());
                }
            }
        });
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
