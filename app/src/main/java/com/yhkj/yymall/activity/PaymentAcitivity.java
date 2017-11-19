package com.yhkj.yymall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unionpay.UPPayAssistEx;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.popwindows.DialogPopup;
import com.yhkj.yymall.view.popwindows.PaymentPopupView;

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

/**
 * Created by Administrator on 2017/7/5.
 */

public class PaymentAcitivity extends BaseToolBarActivity implements View.OnClickListener {

    private Intent intent;
    private String orderNo, total;
    private static final int WALLET = 0;
    private static final int ZHIFUBAO = 1;
    private static final int WEIXIN = 2;
    private static final int YINLIAN = 3;
    private int payType = 0;
    @Bind(R.id.rl_payment_wallet)
    RelativeLayout rl_payment_wallet;

    @Bind(R.id.rl_payment_zfb)
    RelativeLayout rl_payment_zfb;

    @Bind(R.id.rl_payment_wx)
    RelativeLayout rl_payment_wx;

//    @Bind(R.id.rl_payment_yl)
//    RelativeLayout rl_payment_yl;

    @Bind(R.id.img_paymentg_wallet)
    ImageView img_paymentg_wallet;

    @Bind(R.id.img_paymentg_zfb)
    ImageView img_paymentg_zfb;

    @Bind(R.id.img_paymentg_wx)
    ImageView img_paymentg_wx;

//    @Bind(R.id.img_paymentg_yl)
//    ImageView img_paymentg_yl;

    @Bind(R.id.tv_chopcar_all)
    TextView tv_chopcar_all;

    @Bind(R.id.tv_payment_pay)
    TextView tv_payment_pay;
    private ProgressDialog loadingDialog;
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(final BCResult bcResult) {
            loadingDialog.dismiss();
            final BCPayResult bcPayResult = (BCPayResult) bcResult;
            //此处关闭loading界面

            //根据你自己的需求处理支付结果
            //需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
            PaymentAcitivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String result = bcPayResult.getResult();

                    /*
                      注意！
                      所有支付渠道建议以服务端的状态金额为准，此处返回的RESULT_SUCCESS仅仅代表手机端支付成功
                    */
                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {
                        Intent intent = new Intent(getApplicationContext(), PayResultActivity.class);
                        intent.putExtra("total", total);
                        startActivity(intent);
                        finish();

                    } else if (result.equals(BCPayResult.RESULT_CANCEL)) {
                        showToast("用户取消支付");
                    } else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        showToast("支付失败(" + bcPayResult.getDetailInfo() + "|" + bcPayResult.getErrMsg()+")");
                        /**
                         * 你发布的项目中不应该出现如下错误，此处由于支付宝政策原因，
                         * 不再提供支付宝支付的测试功能，所以给出提示说明
                         */
//                        showToast(bcPayResult.getErrMsg());
                        if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") &&
                                bcPayResult.getDetailInfo().startsWith("支付宝参数")) {
                        }

                        /**
                         * 以下是正常流程，请按需处理失败信息
                         */
//                        if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED)) {
//                            //银联需要重新安装控件
//                            Message msg = mHandler.obtainMessage();
//                            msg.what = 1;
//                            mHandler.sendMessage(msg);
//                        }
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
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == 1) {
                //如果用户手机没有安装银联支付控件,则会提示用户安装
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PaymentAcitivity.this);
                builder.setTitle("提示");
                builder.setMessage("完成支付需要安装或者升级银联支付控件，是否安装？");
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UPPayAssistEx.installUPPayPlugin(PaymentAcitivity.this);
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

    @Override
    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
        AppManager.getInstance().finishExceptActivity(MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNPAY);
        startActivity(MineOrderActivity.class, bundle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        BCPay.initWechatPay(PaymentAcitivity.this, getResources().getString(R.string.weixin_key));
//        if (initInfo != null) {
//            showToast("微信初始化失败：" + initInfo);
//        }
        loadingDialog = new ProgressDialog(PaymentAcitivity.this);
        loadingDialog.setMessage("启动第三方支付，请稍候...");
        loadingDialog.setIndeterminate(true);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishExceptActivity(MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNPAY);
                startActivity(MineOrderActivity.class, bundle);
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void initData() {
        setTvTitleText("付款");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.INVISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
        img_paymentg_wallet.setImageResource(R.mipmap.shopcar_gou);
        intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        total = intent.getStringExtra("total");

//        tv_chopcar_all.setText("支付：¥" + total);
        java.text.DecimalFormat df =new java.text.DecimalFormat("#0.00");
        tv_chopcar_all.setText("支付：¥" + df.format(Double.parseDouble(total)));
//        rl_payment_yl.setOnClickListener(this);
        rl_payment_zfb.setOnClickListener(this);
        rl_payment_wx.setOnClickListener(this);
        rl_payment_wallet.setOnClickListener(this);
        tv_payment_pay.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_payment_wallet:
                payType = WALLET;
                img_paymentg_wallet.setImageResource(R.mipmap.shopcar_gou);
                img_paymentg_zfb.setImageResource(R.mipmap.shopcar_null);
                img_paymentg_wx.setImageResource(R.mipmap.shopcar_null);
//                img_paymentg_yl.setImageResource(R.mipmap.shopcar_null);
                break;
            case R.id.rl_payment_zfb:
                payType = ZHIFUBAO;
                img_paymentg_wallet.setImageResource(R.mipmap.shopcar_null);
                img_paymentg_zfb.setImageResource(R.mipmap.shopcar_gou);
                img_paymentg_wx.setImageResource(R.mipmap.shopcar_null);
//                img_paymentg_yl.setImageResource(R.mipmap.shopcar_null);
                break;
            case R.id.rl_payment_wx:
                payType = WEIXIN;
                img_paymentg_wallet.setImageResource(R.mipmap.shopcar_null);
                img_paymentg_zfb.setImageResource(R.mipmap.shopcar_null);
                img_paymentg_wx.setImageResource(R.mipmap.shopcar_gou);
//                img_paymentg_yl.setImageResource(R.mipmap.shopcar_null);
                break;
            case R.id.rl_payment_yl:
                payType = YINLIAN;
                img_paymentg_wallet.setImageResource(R.mipmap.shopcar_null);
                img_paymentg_zfb.setImageResource(R.mipmap.shopcar_null);
                img_paymentg_wx.setImageResource(R.mipmap.shopcar_null);
//                img_paymentg_yl.setImageResource(R.mipmap.shopcar_gou);
                break;
            case R.id.tv_payment_pay:
                switch (payType) {
                    case WALLET:
                        YYMallApi.getPayToken(PaymentAcitivity.this, new YYMallApi.ApiResult<CommonBean>(PaymentAcitivity.this) {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onNext(CommonBean commonBean) {
                                if (commonBean.getCode() == 0) {
                                    PaymentPopupView payYYPopup = new PaymentPopupView(PaymentAcitivity.this, orderNo, total);
                                    payYYPopup.setAutoShowInputMethod(true);
                                    payYYPopup.setAdjustInputMethod(false);
                                    payYYPopup.showPopupWindow();
                                } else if (commonBean.getCode() == 1007) {
                                    //设置支付密码
                                    final DialogPopup popup = new DialogPopup(PaymentAcitivity.this, "温馨提示", "您尚未设置支付密码，不能使用余额支付。", "马上设置", "残忍拒绝");
                                    popup.setCancelOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            popup.dismiss();
                                        }
                                    });

                                    popup.setOkOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            popup.dismiss();
                                            Intent intent = new Intent(mContext,UpdatepwdActivity.class);
                                            intent.putExtra(Constant.PWDTYPE.TYPE,Constant.PWDTYPE.PAY);
                                            startActivity(intent);
                                        }
                                    });
                                    popup.showPopupWindow();
                                }
                            }
                        });

                        break;
                    case ZHIFUBAO:
                        loadingDialog.show();
                        BCPay.PayParams payParam = new BCPay.PayParams();
                        payParam.channelType = BCReqParams.BCChannelTypes.ALI_APP;
                        payParam.billTitle = "YiYa商品";
                        int moneys = (int) (Double.parseDouble(total) * 100);
                        payParam.billTotalFee = moneys;
                        payParam.billNum = orderNo;
                        payParam.billTimeout = 360;
                        // 第二个参数实现BCCallback接口，在done方法中查看支付结果
                        BCPay.getInstance(PaymentAcitivity.this).reqPaymentAsync(payParam, bcCallback);
                        break;
                    case WEIXIN:
                        loadingDialog.show();
                        if (BCPay.isWXAppInstalledAndSupported() &&
                                BCPay.isWXPaySupported()) {
                            BCPay.PayParams payParamss = new BCPay.PayParams();
                            payParamss.channelType = BCReqParams.BCChannelTypes.WX_APP;
                            //商品描述
                            payParamss.billTitle = "YiYa商品";
                            //支付金额，以分为单位，必须是正整数
                            int money = (int) (Double.parseDouble(total) * 100);
                            payParamss.billTotalFee = money;
                            //商户自定义订单号
                            payParamss.billNum = orderNo;
                            // 第二个参数实现BCCallback接口，在done方法中查看支付结果
                            BCPay.getInstance(PaymentAcitivity.this).reqPaymentAsync(payParamss, bcCallback);
                        } else {
                            showToast("您尚未安装微信或者安装的微信版本不支持");
                            loadingDialog.dismiss();
                        }

                        break;
                    case YINLIAN:
                        loadingDialog.show();
                        BCPay.PayParams payParams = new BCPay.PayParams();
                        payParams.channelType = BCReqParams.BCChannelTypes.UN_APP;
                        //商品描述, 32个字节内, 汉字以2个字节计
                        payParams.billTitle = "YiYa商品";
                        //支付金额，以分为单位，必须是正整数
                        int moneys1 = (int) (Double.parseDouble(total) * 100);
                        payParams.billTotalFee = moneys1;
                        payParams.billTimeout = 360;
                        //商户自定义订单号
                        payParams.billNum = orderNo;
                        BCPay.getInstance(PaymentAcitivity.this).reqPaymentAsync(payParams, bcCallback);
                        break;
                    default:
                        break;

                }

                break;


            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BCPay.detachWechat();
        BCPay.clear();
    }

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
}
