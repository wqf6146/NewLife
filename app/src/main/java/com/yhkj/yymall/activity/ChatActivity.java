package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.recorder.MediaManager;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.easeui.util.Config;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.hyphenate.helpdesk.model.OrderInfo;
import com.hyphenate.helpdesk.model.VisitorTrack;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.fragment.CustomChatFragment;

public class ChatActivity extends AppCompatActivity {

    public static ChatActivity instance = null;

    private ChatFragment chatFragment;

    String toChatUsername;

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
        UIProvider.getInstance().getNotifier().reset();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.hd_activity_chat);
        StatusBarUtil.setStatusBarColor(this,R.color.theme_bule);
        instance = this;
        AppManager.getInstance().addActivity(this);
//        mDeadStatusView = findViewById(R.id.ftr_dead_statusbg);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
//            mDeadStatusView.setVisibility(GONE);
//        }else{
//            mDeadStatusView.getLayoutParams().height = CommonUtil.getStatusBarHeight(this);
//            mDeadStatusView.setBackgroundColor(getResources().getColor(R.color.theme_bule));
//        }
        //IM服务号
        toChatUsername = getIntent().getExtras().getString(Config.EXTRA_SERVICE_IM_NUMBER);
        //可以直接new ChatFragment使用
        String chatFragmentTAG = "chatFragment";
        chatFragment = (ChatFragment) getSupportFragmentManager().findFragmentByTag(chatFragmentTAG);
        if (chatFragment == null){
            chatFragment = new CustomChatFragment();
            //传入参数
            chatFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment, chatFragmentTAG).commit();
            sendOrderOrTrack();
        }
    }


    /**
     * 发送订单或轨迹消息
     */
    private void sendOrderOrTrack() {
        Bundle bundle = getIntent().getBundleExtra(Config.EXTRA_BUNDLE);
        if (bundle != null) {
            //检查是否是从某个商品详情进来
            int selectedIndex = bundle.getInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, Constant.INTENT_CODE_IMG_SELECTED_DEFAULT);
            switch (selectedIndex) {
                case Constant.INTENT_CODE_IMG_ORDER:
                case Constant.INTENT_CODE_IMG_SELECTED_2:

                    sendOrderMessage(selectedIndex);
                    break;
                case Constant.INTENT_CODE_IMG_SHOP:
                case Constant.INTENT_CODE_IMG_SELECTED_4:
                    sendTrackMessage(selectedIndex);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 发送订单消息
     *
     * 不发送则是saveMessage
     * @param selectedIndex
     */
    private void sendOrderMessage(int selectedIndex){
        Message message = Message.createTxtSendMessage(getIntent().getStringExtra("ordertitle"), toChatUsername);
        OrderInfo info = ContentFactory.createOrderInfo(null);
        info.title("订单号：" + String.format(getIntent().getStringExtra("ordertitle"))).orderTitle("")
                .price(getIntent().getStringExtra("orderprice"))
                .desc(getIntent().getStringExtra("orderdesc"))
                .imageUrl(getIntent().getStringExtra("orderimg"))
                .itemUrl("https://admin.yiyiyaya.cc/index.php?controller=order&action=order_show&id=" + getIntent().getStringExtra("orderid"));
        message.addContent(info);
        message.setAttribute("id",getIntent().getStringExtra("orderid"));
//        message.setMsgId(getIntent().getStringExtra("orderid"));
        ChatClient.getInstance().chatManager().saveMessage(message);
    }

    /**
     * 发送轨迹消息
     * @param selectedIndex
     */
    private void sendTrackMessage(int selectedIndex) {
        Message message = Message.createTxtSendMessage(getIntent().getStringExtra("shopname"), toChatUsername);
        VisitorTrack info = ContentFactory.createVisitorTrack(null);
        info.title("我正在看")
                .price(getIntent().getStringExtra("shopprice"))
                .desc(String.format(getIntent().getStringExtra("shopname")))
                .imageUrl(getIntent().getStringExtra("shopimg"))
                .itemUrl("https://admin.yiyiyaya.cc/index.php?controller=goods&action=goods_edit&id=" + getIntent().getStringExtra("shopid"));
        message.addContent(info);
        message.setAttribute("shopid",getIntent().getStringExtra("shopid"));
        message.setAttribute("type",getIntent().getStringExtra("shoptype"));
        String pid = getIntent().getStringExtra("panicBuyItemId");
        if (!TextUtils.isEmpty(pid))
            message.setAttribute("panicBuyItemId",pid);
        ChatClient.getInstance().chatManager().sendMessage(message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
        instance = null;
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(Config.EXTRA_SERVICE_IM_NUMBER);
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
//            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        if (chatFragment != null) {
            chatFragment.onBackPressed();
        }
        if (CommonUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}