package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.ChatClient;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/24.
 */

public class MessageMinuteActivity extends BaseToolBarActivity {

    @Bind(R.id.tv_messageminute_time)
    TextView tv_messageminute_time;

    @Bind(R.id.tv_messageminute_con)
    TextView tv_messageminute_con;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messageminute);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        setImgRightResource(R.mipmap.ic_nor_delete);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MessageMinuteActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定要删除此条消息吗？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YYMallApi.deleteMessage(MessageMinuteActivity.this, intent.getStringExtra("id"),
                                new YYMallApi.ApiResult<CommonBean>(MessageMinuteActivity.this) {
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
                                        showToast("删除成功");
                                        AppManager.getInstance().finishActivity(MessageMinuteActivity.this);
                                    }
                                });
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("消息内容");
        setImgBackVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        intent =getIntent();
        tv_messageminute_time.setText(intent.getStringExtra("time"));
        tv_messageminute_con.setText(intent.getStringExtra("title") + "/n" + intent.getStringExtra("content"));
    }
}
