package com.yhkj.yymall.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yanzhenjie.permission.AndPermission;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.LaunchInfoBean;
import com.yhkj.yymall.bean.UpdateBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
//import cn.bingoogolapple.update.BGADownloadProgressEvent;
//import cn.bingoogolapple.update.BGAUpgradeUtil;
//import rx.functions.Action1;

/**
 * Created by Administrator on 2017/7/13.
 */

public class LaunchActivity extends AppCompatActivity {
    
    @Bind(R.id.al_tv_skip)
    TextView mTvSkip;

    @Bind(R.id.al_rl_container)
    RelativeLayout mRlContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        getLaunchPic();
        init();
    }

    Bitmap mLaunchBitmap;
    private LaunchInfoBean.DataBean mDataBean;
    private void getLaunchPic() {
        YYMallApi.getLaunchLogo(this, new ApiCallback<LaunchInfoBean.DataBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ApiException e) {
//                Toast.makeText(LaunchActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(LaunchInfoBean.DataBean dataBean) {
                mDataBean = dataBean;
                picThread.start();
            }

            @Override
            public void onStart() {

            }
        });
    }

    private boolean mDestroy = false;
    Thread picThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                Bitmap bitmap = Glide.with(LaunchActivity.this)
                        .load(mDataBean.getStartmap())
                        .asBitmap()
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
                if (!mDestroy)
                    mLaunchBitmap = bitmap;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    private Handler mHanlder = new Handler();
    private void init(){
        mTvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                mDestroy = true;
                finish();
            }
        });
        mHanlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLaunchBitmap == null){
                    startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                    mDestroy = true;
                    finish();
                }else{
                    mTvSkip.setVisibility(View.VISIBLE);
                    mRlContainer.setBackground(new BitmapDrawable(getResources(),mLaunchBitmap));
                    mHanlder.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mDestroy) return;
                            startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                            finish();
                        }
                    },2000);
                }
            }
        },1000);

    }
}
