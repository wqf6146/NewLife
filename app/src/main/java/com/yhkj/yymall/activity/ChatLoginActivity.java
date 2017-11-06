package com.yhkj.yymall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;

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
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Intent intent = getIntent();
        selectedIndex = intent.getIntExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY,
                Constant.INTENT_CODE_IMG_SELECTED_DEFAULT);
        messageToIndex = intent.getIntExtra(Constant.MESSAGE_TO_INTENT_EXTRA, Constant.MESSAGE_TO_DEFAULT);

        //ChatClient.getInstance().isLoggedInBefore() 可以检测是否已经登录过环信，如果登录过则环信SDK会自动登录，不需要再次调用登录操作
        toChatActivity();
//        if (ChatClient.getInstance().isLoggedInBefore()) {
//            progressDialog = getProgressDialog();
//            progressDialog.setMessage("正在加载，请稍后...");
//            progressDialog.show();
//            toChatActivity();
//        } else {
//            //随机创建一个用户并登录环信服务器
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
        UIProvider.getInstance().popActivity(this);
    }

    private void createAccountThenLoginChatServer() {
        final String account = YYApp.getInstance().getPhone();
        final String userPwd = "123456";
        progressDialog = getProgressDialog();
        progressDialog.setMessage("正在加载，请稍后...");
        progressDialog.show();
        // createAccount to huanxin server
        // if you have a account, this step will ignore
        ChatClient.getInstance().register(account, userPwd, new Callback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "demo register success");
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
                        if(progressDialog != null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
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
                Log.d(TAG, "demo login success!");
                if (!progressShow) {
                    return;
                }
                toChatActivity();
            }

            @Override
            public void onError(int code, String error) {
                Log.e(TAG, "login fail,code:" + code + ",error:" + error);
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
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

    private void toChatActivity() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!ChatLoginActivity.this.isFinishing())
                    if (progressDialog!=null && progressDialog.isShowing())
                        progressDialog.dismiss();

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
                String titleName = null;
                if (conversation.officialAccount() != null){
                    titleName = conversation.officialAccount().getName();
                }
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
                startActivity(intent);
                finish();

            }
        });
    }
}
