package com.yhkj.yymall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.unionpay.UPPayAssistEx;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.bean.AssetBean;
import com.yhkj.yymall.bean.BalanceBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.RechargeBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.HashMap;

import butterknife.Bind;
import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCBillOrder;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/4.
 */

public class RechargeActivity extends BaseToolBarActivity {

    @Bind(R.id.ar_tv_price)
    TextView mTvPrice;

    @Bind(R.id.ar_spinner)
    Spinner mSpiner;

//    @Bind(R.id.ar_et_acount)
//    EditText mEditAcount;

    @Bind(R.id.ar_et_price)
    EditText mEditPrice;

    @Bind(R.id.ar_tv_apply)
    TextView mTvRecharge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_recharge);
        String initInfo = BCPay.initWechatPay(RechargeActivity.this, "wx1b4e339277365985");
        if (initInfo != null) {
            showToast("微信初始化失败：" + initInfo);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mLoadingDialog = new ProgressDialog(RechargeActivity.this);
        mLoadingDialog.setMessage("启动第三方支付，请稍候...");
        mLoadingDialog.setIndeterminate(true);
    }

    private int mRechargeType = 1; //1银联 2支付宝 3为微信

    private ProgressDialog mLoadingDialog;
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(final BCResult bcResult) {
            mLoadingDialog.dismiss();
            final BCPayResult bcPayResult = (BCPayResult) bcResult;
            //此处关闭loading界面

            //根据你自己的需求处理支付结果
            //需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
            RechargeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String result = bcPayResult.getResult();

                    /*
                      注意！
                      所有支付渠道建议以服务端的状态金额为准，此处返回的RESULT_SUCCESS仅仅代表手机端支付成功
                    */
                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {
//                        YYApp.getInstance().setUiUpdateTag(R.layout.fragment_mine,true);
//                        YYMallApi.rechargeCallback(RechargeActivity.this, mOrderNo, new ApiCallback<CommonBean>() {
//                            @Override
//                            public void onStart() {
//
//                            }
//
//                            @Override
//                            public void onError(ApiException e) {
//                                showToast(e.getMessage());
//                            }
//
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onNext(CommonBean commonBean) {
//                                Intent intent = new Intent(RechargeActivity.this,RechargeResultActivity.class);
//                                intent.putExtra("price",mPrice);
//                                startActivity(intent);
//                                AppManager.getInstance().finishActivity(RechargeActivity.this);
//                            }
//                        });
                        Intent intent = new Intent(RechargeActivity.this,RechargeResultActivity.class);
                        intent.putExtra("price",mPrice);
                        startActivity(intent);
                        AppManager.getInstance().finishActivity(RechargeActivity.this);
                    } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
                        showToast("用户取消支付");
                    } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        showToast("支付失败");
                        /**
                         * 你发布的项目中不应该出现如下错误，此处由于支付宝政策原因，
                         * 不再提供支付宝支付的测试功能，所以给出提示说明
                         */
                        showToast(bcPayResult.getErrMsg());
                        if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") &&
                                bcPayResult.getDetailInfo().startsWith("支付宝参数")) {
                        }

                        /**
                         * 以下是正常流程，请按需处理失败信息
                         */
                        if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED)) {
                            //银联需要重新安装控件
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                    } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
                        //可能出现在支付宝8000返回状态
                    } else {
                    }
                    if (bcPayResult.getId() != null) {
                        //你可以把这个id存到你的订单中，下次直接通过这个id查询订单
                        //根据ID查询，此处只是演示如何通过id查询订单，并非支付必要部分
                        getBillInfoByID(bcPayResult.getId());
                    }
                }
            });
        }
    };
    void getBillInfoByID(String id) {

        BCQuery.getInstance().queryBillByIDAsync(id,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        BCQueryBillResult billResult = (BCQueryBillResult) result;
                        Log.d("", "------ response info ------");
                        Log.d("", "------getResultCode------" + billResult.getResultCode());
                        Log.d("", "------getResultMsg------" + billResult.getResultMsg());
                        Log.d("", "------getErrDetail------" + billResult.getErrDetail());

                        if (billResult.getResultCode() != 0)
                            return;

                        Log.d("", "------- bill info ------");
                        BCBillOrder billOrder = billResult.getBill();
                        Log.d("", "订单唯一标识符：" + billOrder.getId());
                        Log.d("", "订单号:" + billOrder.getBillNum());
                        Log.d("", "订单金额, 单位为分:" + billOrder.getTotalFee());
                        Log.d("", "渠道类型:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getChannel()));
                        Log.d("", "子渠道类型:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getSubChannel()));
                        Log.d("", "订单是否成功:" + billOrder.getPayResult());

                        if (billOrder.getPayResult())
                            Log.d("", "渠道返回的交易号，未支付成功时，是不含该参数的:" + billOrder.getTradeNum());
                        else
                            Log.d("", "订单是否被撤销，该参数仅在线下产品（例如二维码和扫码支付）有效:"
                                    + billOrder.getRevertResult());
                        Log.d("", "扩展参数:" + billOrder.getOptional());
                        Log.w("", "订单是否已经退款成功(用于后期查询): " + billOrder.getRefundResult());
                        Log.w("", "渠道返回的详细信息，按需处理: " + billOrder.getMessageDetail());
                    }
                });
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {
                //如果用户手机没有安装银联支付控件,则会提示用户安装
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RechargeActivity.this);
                builder.setTitle("提示");
                builder.setMessage("完成支付需要安装或者升级银联支付控件，是否安装？");
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UPPayAssistEx.installUPPayPlugin(RechargeActivity.this);
                                dialog.dismiss();
                                finish();
                            }
                        });
                builder.setPositiveButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
            return true;
        }
    });

    private String mPrice;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RechargeLogActivity.class);
            }
        });
        mSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mRechargeType = pos + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mTvRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String acount = mEditAcount.getText().toString();
//                if (TextUtils.isEmpty(acount)){
//                    showToast("请输入充值账户");
//                    return;
//                }
                if (TextUtils.isEmpty(mEditPrice.getText().toString())){
                    showToast("请输入充值金额");
                    return;
                }
                mPrice = mEditPrice.getText().toString();
                YYMallApi.doRecharge(RechargeActivity.this, mRechargeType, Float.parseFloat(mPrice),
                        new YYMallApi.ApiResult<RechargeBean.DataBean>(RechargeActivity.this) {
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
                            public void onNext(RechargeBean.DataBean bean) {
                                mOrderNo = bean.getOrderNo();
                                beecloudRecharge(mPrice,bean.getOrderNo());
                            }
                        });
            }
        });
    }

    private String mOrderNo;
    private void beecloudRecharge(String price,String orderNo){
        switch (mRechargeType){
//            case 1:
//                //银联
//                mLoadingDialog.show();
//                BCPay.PayParams payParams = new BCPay.PayParams();
//                payParams.channelType = BCReqParams.BCChannelTypes.UN_APP;
//                //商品描述, 32个字节内, 汉字以2个字节计
//                payParams.billTitle = "YiYiYaYa余额充值";
//                payParams.optional = new HashMap<>();
//                payParams.optional.put("type","recharge");
//                //支付金额，以分为单位，必须是正整数
//                int moneys1 = (int) (Double.parseDouble(price) * 100);
//                payParams.billTotalFee = moneys1;
//                payParams.billTimeout = 360;
//                //商户自定义订单号
//                payParams.billNum = orderNo;
//                BCPay.getInstance(RechargeActivity.this).reqPaymentAsync(payParams, bcCallback);
//                break;
            case 1:
                //支付宝
                mLoadingDialog.show();
                BCPay.PayParams payParam = new BCPay.PayParams();
                payParam.channelType = BCReqParams.BCChannelTypes.ALI_APP;
                payParam.optional = new HashMap<>();
                payParam.optional.put("type","recharge");
                payParam.billTitle = "YiYiYaYa余额充值";
                int moneys = (int) (Double.parseDouble(price) * 100);
                payParam.billTotalFee = moneys;
                payParam.billNum = orderNo;
                // 第二个参数实现BCCallback接口，在done方法中查看支付结果
                BCPay.getInstance(RechargeActivity.this).reqPaymentAsync(payParam, bcCallback);
                break;
            case 2:
                //微信
                mLoadingDialog.show();
                if (BCPay.isWXAppInstalledAndSupported() &&
                        BCPay.isWXPaySupported()) {
                    BCPay.PayParams payParamss = new BCPay.PayParams();
                    payParamss.channelType = BCReqParams.BCChannelTypes.WX_APP;
                    //商品描述
                    payParamss.billTitle = "YiYiYaYa余额充值";
                    //支付金额，以分为单位，必须是正整数
                    int money = (int) (Double.parseDouble(price) * 100);
                    payParamss.billTotalFee = money;
                    payParamss.optional = new HashMap<>();
                    payParamss.optional.put("type","recharge");
                    //商户自定义订单号
                    payParamss.billNum = orderNo;
                    // 第二个参数实现BCCallback接口，在done方法中查看支付结果
                    BCPay.getInstance(RechargeActivity.this).reqPaymentAsync(payParamss, bcCallback);
                } else {
                    showToast("您尚未安装微信或者安装的微信版本不支持");
                    mLoadingDialog.dismiss();
                }

                break;
        }
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(false);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(false);
    }

    private void getData(boolean bShow) {

        YYMallApi.getUserAsset(this,bShow, new YYMallApi.ApiResult<AssetBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(AssetBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mBalance = Float.parseFloat(dataBean.getBalance());
                mTvPrice.setText(dataBean.getBalance() + "元");
            }
        });
    }


    private Float mBalance;
    @Override
    protected void initData() {
        setTvTitleText("余额充值");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setImgRightResource(R.mipmap.ic_nor_applydetail);
    }
}
