package com.yhkj.yymall.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.vise.xsnow.manager.AppManager;
import com.yanzhenjie.permission.AndPermission;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.base.HxHelper;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ChatLoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private boolean progressShow;
    private ProgressDialog progressDialog;
    private int selectedIndex = Constant.INTENT_CODE_IMG_SELECTED_DEFAULT;
    private int messageToIndex = Constant.MESSAGE_TO_DEFAULT;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AppManager.getInstance().addActivity(this);
        Intent intent = getIntent();
        selectedIndex = intent.getIntExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY,
                Constant.INTENT_CODE_IMG_SELECTED_DEFAULT);
        messageToIndex = intent.getIntExtra(Constant.MESSAGE_TO_INTENT_EXTRA, Constant.MESSAGE_TO_DEFAULT);
//        HxHelper.instance.initSdk();
        if (ChatClient.getInstance().isLoggedInBefore()) {
            progressDialog = getProgressDialog();
            progressDialog.setMessage("正在加载，请稍后...");
            progressDialog.show();
            toChatActivity();
        } else {
            createAccountThenLoginChatServer();
        }
//        if(AndPermission.hasPermission(this, Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.WRITE_SETTINGS)) {
//             有权限，直接do anything.
//
//        }else{
//             申请权限。
//            AndPermission.with(this)
//                    .requestCode(100)
//                    .permission(Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.WRITE_SETTINGS)
//                    .send();
//        }
    }
//    @TargetApi(23)
//    private void CheckPermission() {
//        if (!Settings.System.canWrite(this)) {
//            Toast.makeText(this,"先设置")
//            Uri selfPackageUri = Uri.parse("package:"
//                    + this.getPackageName());
//            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                    selfPackageUri);
//            startActivity(intent);
//        }
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        for (int i=0; i<permissions.length; i++){
//            if (grantResults[i] == -1){
//                Toast.makeText(this,"需要在设置里打开YiYiYaYa的配置权限",Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
//        HxHelper.instance.initSdk();
//        if (ChatClient.getInstance().isLoggedInBefore()) {
//            progressDialog = getProgressDialog();
//            progressDialog.setMessage("正在加载，请稍后...");
//            progressDialog.show();
//            toChatActivity();
//        } else {
//            createAccountThenLoginChatServer();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIProvider.getInstance().pushActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();
        UIProvider.getInstance().popActivity(this);
    }

    private void createAccountThenLoginChatServer() {
        final String account = YYApp.getInstance().getPhone();
        final String userPwd = "123456";
        progressDialog = getProgressDialog();
        progressDialog.setMessage("正在加载，请稍后...");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        ChatClient.getInstance().register(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //登录环信服务器
                        login(account, userPwd);
                    }
                });
            }

            @Override
            public void onError(final int errorCode, final String error) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        dimissDialog();
                        if (errorCode == Error.NETWORK_ERROR){
                            Toast.makeText(getApplicationContext(), "网络连接不可用，请检查网络", Toast.LENGTH_SHORT).show();
                        }else if (errorCode == Error.USER_ALREADY_EXIST){
                            login(account, userPwd);
                        }else if(errorCode == Error.USER_AUTHENTICATION_FAILED){
                            Toast.makeText(getApplicationContext(), "无开放注册权限", Toast.LENGTH_SHORT).show();
                        } else if (errorCode == Error.USER_ILLEGAL_ARGUMENT){
                            Toast.makeText(getApplicationContext(), "用户名不合法", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    private ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ChatLoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressShow = false;
                }
            });
        }
        return progressDialog;
    }

    private void login(final String uname, final String upwd) {
        progressShow = true;
        progressDialog = getProgressDialog();
        progressDialog.setMessage("正在联系客服，请稍等…");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        // login huanxin server
        ChatClient.getInstance().login(uname, upwd, new Callback() {
            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                toChatActivity();
            }

            @Override
            public void onError(int code, String error) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        dimissDialog();
                        Toast.makeText(ChatLoginActivity.this,
                                "联系客服失败",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    private void dimissDialog(){
        if (progressDialog != null && progressDialog.isShowing() && !this.isFinishing()){
            progressDialog.dismiss();
        }
    }


    private void toChatActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dimissDialog();
                //此处演示设置技能组,如果后台设置的技能组名称为[shouqian|shouhou],这样指定即分配到技能组中.
                //为null则不按照技能组分配,同理可以设置直接指定客服scheduleAgent
                String queueName = null;
                switch (messageToIndex){
                    case Constant.MESSAGE_TO_AFTER_SALES:
                        queueName = "shouhou";
                        break;
                    case Constant.MESSAGE_TO_PRE_SALES:
                        queueName = "shouqian";
                        break;
                    default:
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, selectedIndex);
                //设置点击通知栏跳转事件
                Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Constant.LyImServiceNum);
                String titleName = "YiYiYaYa客服";
//                if (conversation.officialAccount() != null){
//                    titleName = conversation.officialAccount().getName();
//                }
                // 进入主页面
                Intent intent = new IntentBuilder(ChatLoginActivity.this)
                        .setTargetClass(ChatActivity.class)
//                        .setVisitorInfo(DemoMessageHelper.createVisitorInfo())
                        .setServiceIMNumber(Constant.LyImServiceNum)
//                        .setScheduleQueue(DemoMessageHelper.createQueueIdentity(queueName))
                        .setTitleName(titleName)
//						.setScheduleAgent(DemoMessageHelper.createAgentIdentity("ceshiok1@qq.com"))
                        .setShowUserNick(true)
                        .setBundle(bundle)
                        .build();
                if (selectedIndex == Constant.INTENT_CODE_IMG_ORDER){
                    intent.putExtra("orderid", getIntent().getStringExtra("orderid"));
                    intent.putExtra("ordertitle",getIntent().getStringExtra("ordertitle"));
                    intent.putExtra("orderprice",getIntent().getStringExtra("orderprice"));
                    intent.putExtra("orderdesc",getIntent().getStringExtra("orderdesc"));
                    intent.putExtra("orderimg",getIntent().getStringExtra("orderimg"));
                }else if (selectedIndex == Constant.INTENT_CODE_IMG_SHOP){
                    intent.putExtra("shopid", getIntent().getStringExtra("shopid"));
                    intent.putExtra("panicBuyItemId", getIntent().getStringExtra("panicBuyItemId"));
                    intent.putExtra("shoptype", getIntent().getStringExtra("shoptype"));
                    intent.putExtra("shopname",getIntent().getStringExtra("shopname"));
                    intent.putExtra("shopprice",getIntent().getStringExtra("shopprice"));
                    intent.putExtra("shopdesc",getIntent().getStringExtra("shopdesc"));
                    intent.putExtra("shopimg",getIntent().getStringExtra("shopimg"));
                }
                startActivity(intent);
                finish();
            }
        });
    }
}
