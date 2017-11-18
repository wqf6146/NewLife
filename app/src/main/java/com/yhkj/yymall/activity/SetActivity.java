package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.ChatClient;
import com.vise.xsnow.manager.AppManager;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.util.GlideCatchUtil;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/3.
 */

public class SetActivity extends BaseToolBarActivity {

    @Bind(R.id.as_rl_about)
    RelativeLayout mRlAbout;

    @Bind(R.id.as_rl_userdata)
    RelativeLayout mRlUserData;

    @Bind(R.id.as_rl_acountsafety)
    RelativeLayout mRlAcountSafe;

    @Bind(R.id.as_rl_clean)
    RelativeLayout mRlClean;

    @Bind(R.id.as_tv_logout)
    TextView mTvLogout;

    @Bind(R.id.as_tv_cache)
    TextView mTvCache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
    }

    @Override
    protected void initData() {
        setTvTitleText("设置");
        setTvRightVisiable(View.GONE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        mTvCache.setText(GlideCatchUtil.getInstance().getCacheSize());
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRlClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GlideCatchUtil.getInstance().cleanCatchDisk();
//                        GlideCatchUtil.getInstance().clearCacheDiskSelf();
                        GlideCatchUtil.getInstance().clearCacheMemory();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTvCache.setText(GlideCatchUtil.getInstance().getCacheSize());
                                showToast("清理成功");
                            }
                        });
                    }
                }).start();
            }
        });
        mRlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AboutActivity.class);
            }
        });
        mRlUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                }else{
                    startActivity(UserDataActivity.class);
                }
            }
        });

        mRlAcountSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                }else {
                    startActivity(AcountSafeActivity.class);
                }
            }
        });

        mTvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    startActivity(LoginActivity.class);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定要退出登录吗？");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            YYApp.getInstance().setToken(null);
                            DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                            try{
                                if(ChatClient.getInstance().isLoggedInBefore())
                                    ChatClient.getInstance().logout(true, null);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            AppManager.getInstance().finishActivity(SetActivity.this);
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
            mTvLogout.setText("登录");
        }else{
            mTvLogout.setText("退出登录");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }
}
