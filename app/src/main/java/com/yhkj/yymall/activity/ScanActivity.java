package com.yhkj.yymall.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yanzhenjie.permission.AndPermission;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.http.api.ApiService;

import butterknife.Bind;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Created by Administrator on 2017/6/24.
 */

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    @Bind(R.id.as_btn_light)
    ImageView mBtnLight;

    @Bind(R.id.zbarview)
    ZXingView mZbarView;

    @Bind(R.id.as_img_back)
    ImageView mImgBack;

    private boolean mOpenLight = false;

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        if (!TextUtils.isEmpty(result) && result.startsWith(ApiService.SHARE_CODE_URL)){
            Intent intent = new Intent(this,InputCodeActivity.class);
            intent.putExtra("code",result.split("#")[1]);
            startActivity(intent);
            finish();
        }
//        showToast(result);
    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    @Override
    public void onStart() {
        super.onStart();
        mZbarView.startCamera();
        mZbarView.showScanRect();
        mZbarView.changeToScanQRCodeStyle();
        mZbarView.startSpot();
    }

    @Override
    public void onStop() {
        mZbarView.stopCamera();
        super.onStop();
    }
    @Override
    public void onDestroy() {
        mZbarView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void initData() {
//        setToolBarColor(getResources().getColor(R.color.theme_bule));
//        setTvTitleText("扫一扫");
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    @Override
    protected void initView() {
        setStatusViewVisiable(false);
        mZbarView.setDelegate(this);
        if(AndPermission.hasPermission(this,
                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // 有权限，直接do anything.

        } else {
            // 申请权限。
            AndPermission.with(this)
                    .requestCode(100)
                    .permission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.CAMERA,
                            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .send();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            mZbarView.startCamera();
            mZbarView.showScanRect();
            mZbarView.changeToScanQRCodeStyle();
            mZbarView.startSpot();
        }
    }

    @Override
    protected void bindEvent() {
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenLight){
                    mOpenLight = false;
                    mZbarView.closeFlashlight();
                    mBtnLight.setBackgroundResource(R.drawable.circle_uncheck_scan);
                    mBtnLight.setImageResource(R.drawable.ic_nor_lightning);
                }else{
                    mOpenLight = true;
                    mZbarView.openFlashlight();
                    mBtnLight.setBackgroundResource(R.drawable.circle_check_scan);
                    mBtnLight.setImageResource(R.drawable.ic_nor_lighting_gray);
                }

            }
        });
    }

}
