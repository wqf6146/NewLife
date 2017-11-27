package com.yhkj.yymall.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BalanceBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.config.IWebPageView;
import com.yhkj.yymall.config.JSInterface;
import com.yhkj.yymall.config.MyWebChromeClient;
import com.yhkj.yymall.config.MyWebViewClient;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ApplyWithdrawActivity extends BaseToolBarActivity implements IWebPageView {

    @Bind(R.id.aa_tv_price)
    TextView mTvPrice;

    @Bind(R.id.aa_spinner)
    Spinner mSpiner;

    @Bind(R.id.aa_et_acount)
    EditText mEditAcount;

    @Bind(R.id.aa_et_name)
    EditText nEditName;

    @Bind(R.id.aa_et_price)
    EditText mEditPrice;

    @Bind(R.id.aa_tv_apply)
    TextView mTvApply;

    @Bind(R.id.aa_tv_tip)
    TextView mTvTip;

    @Bind(R.id.aa_webview)
    WebView mWebView;

    private MyWebChromeClient mWebChromeClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_applywithdraw);
    }

    private String[] mApplyArrString;
    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        mApplyArrString = getResources().getStringArray(R.array.apply);
        initWebView();
    }



    @Override
    public void hindProgressBar() {
        if (!CommonUtil.isNetworkConnected(this)){
            setNetWorkErrShow(VISIBLE);
        }else{
            setNetWorkErrShow(GONE);
        }
    }

    @Override
    public void startProgress() {
//        startProgress90();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public FrameLayout getVideoFullView() {
        return null;
    }

    @Override
    public void showWebView() {
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        mFlViewdeoContainer = new FullscreenHolder(LeaseDetailActivity.this);
//        mFlViewdeoContainer.addView(view);
//        decor.addView(mFlViewdeoContainer);
    }
    @Override
    public void showVideoFullView() {
//        mFlViewdeoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
//        mFlViewdeoContainer.setVisibility(View.GONE);
    }

    @Override
    public void progressChanged(int newProgress) {

    }
    @Override
    public void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        mWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        mWebView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }


    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);
        if (mWebView == null) return;
        WebSettings ws = mWebView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 缩放比例 1
        mWebView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        mWebView.setWebChromeClient(mWebChromeClient);
        // 与js交互
//        mWebView.addJavascriptInterface(new JSInterface(this), "injectedObject");
        mWebView.setWebViewClient(new MyWebViewClient(this));
    }


    private int mApplyType = 1; //1为支付宝2为微信

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ApplyListActivity.class);
            }
        });
        mSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                getData(pos + 1,true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mEditPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString()) || mBalance == null) return;
                if (Float.parseFloat(s.toString()) > mBalance){
                    mTvTip.setVisibility(View.VISIBLE);
                }else{
                    mTvTip.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提现
                String acount = mEditAcount.getText().toString();
                String name = nEditName.getText().toString();
                if (TextUtils.isEmpty(name)){
                    showToast("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(acount)){
                    showToast("请输入提现账户");
                    return;
                }
                if (TextUtils.isEmpty(mEditPrice.getText().toString())){
                    showToast("请输入提现金额");
                    return;
                }
                if (mTvTip.getVisibility() != GONE){
                    showToast("提现金额超限");
                    return;
                }
                YYMallApi.doApplyWithdraw(ApplyWithdrawActivity.this, mApplyType,name, acount, Float.parseFloat(mEditPrice.getText().toString()),
                        new YYMallApi.ApiResult<CommonBean>(ApplyWithdrawActivity.this) {
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
                            public void onNext(CommonBean commonBean) {
                                showToast("提现申请成功");
                                AppManager.getInstance().finishActivity(ApplyWithdrawActivity.this);
                            }
                        });

            }
        });
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(mApplyType,false);
        mWebView.loadUrl(ApiService.YYWEB + Constant.WEB_TAG.TIXIANTIESHI);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(mApplyType,false);
        mWebView.loadUrl(ApiService.YYWEB + Constant.WEB_TAG.TIXIANTIESHI);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mWebView.onDestroy();
        mWebView.destroy();
    }


    private void getData(final int type, boolean bShow) {

        YYMallApi.getBalance(this,type,bShow, new ApiCallback<BalanceBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(BalanceBean.DataBean balance) {
                setNetWorkErrShow(GONE);
                mApplyType = type;
                mBalance = Float.parseFloat(balance.getBalance());
                mTvPrice.setText(balance.getBalance() + "元");
                nEditName.setText(balance.getName());
                mEditAcount.setText(balance.getAccount());
            }
        });
    }


    private Float mBalance;
    @Override
    protected void initData() {
        setTvTitleText("申请提现");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setImgRightResource(R.mipmap.ic_nor_applydetail);
    }
}
