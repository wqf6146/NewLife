package com.yhkj.yymall.activity;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.archivepatcher.applier.ApplierListen;
import com.umeng.socialize.UMShareAPI;
import com.vise.xsnow.event.EventSubscribe;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.bean.UpdateBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.fragment.HomeFragment;
import com.yhkj.yymall.fragment.NewLeaseFragment;
import com.yhkj.yymall.fragment.YiYaMallFragment;
import com.yhkj.yymall.fragment.MineFragment;
import com.yhkj.yymall.fragment.ShopCarFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.util.NetStateReceiver;
import com.yhkj.yymall.view.BottomBarView.BottomBar;
import com.yhkj.yymall.view.BottomBarView.BottomBarTab;
import com.yhkj.yymall.view.popwindows.HProgressDialogUtils;
import java.io.File;
import butterknife.Bind;
import cn.bingoogolapple.update.BGADownloadProgressEvent;
import cn.bingoogolapple.update.BGAUpgradeUtil;
import me.yokeyword.fragmentation.SupportFragment;
import rx.Subscriber;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    private static String Tag = MainActivity.class.toString();

    @Bind(R.id.am_bottombar)
    BottomBar mBottomBar;

    SupportFragment[] mFragments = new SupportFragment[5];

    private int mTabIndex = 0;

    //网络观察者
    protected NetStateReceiver.NetChangeObserver mNetChangeObserver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setOnResumeRegisterBus(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    Boolean mPrepareExit = false;
    private final int BACK = 1;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == BACK){
                mPrepareExit = false;
            }
            return false;
        }
    });
    @Override
    public void onBackPressedSupport() {
//        moveTaskToBack(false);
        if (mPrepareExit){
//            onReceiverDeadSigned();
            AppManager.getInstance().appExit(this);
        }else{
            mPrepareExit = true;
            showToast("再按一次退出YiYiYaYa");
            mHandler.sendEmptyMessageDelayed(BACK,2000l);
        }
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        checkUpdate();
    }

    /**
     * 检查更新
     */
    private UpdateBean.DataBean mUpdateBean = null;
    private void checkUpdate() {
        YYMallApi.getVersionInfo(this, new ApiCallback<UpdateBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(UpdateBean.DataBean dataBean) {
                mUpdateBean = dataBean;
                tryUpdateVersion();
            }
        });
    }

    private int mNeedUpdate = 0;
    private void tryUpdateVersion() {
        mNeedUpdate = compareVersion(mUpdateBean);
        if (mNeedUpdate == -1) {
            //强制更新
            new AlertDialog.Builder(this)
                    .setTitle(String.format("请升级到%s版本", mUpdateBean.getInfo().getVersion()))
                    .setCancelable(false)
                    .setMessage(mUpdateBean.getInfo().getVersionDescription())
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startUpdateVersion();
                        }
                    })
                    .create()
                    .show();
        }else if (mNeedUpdate == 1){
            //可以更新
            new AlertDialog.Builder(this)
                    .setTitle(String.format("是否升级到%s版本？", mUpdateBean.getInfo().getVersion()))
                    .setMessage(mUpdateBean.getInfo().getVersionDescription())
                    .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startUpdateVersion();
                        }
                    })
                    .setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * 执行更新
     */
    private void handleUpdate(){
        String oldVersion = CommonUtil.getVersionName(this);
        final String newVersion = mUpdateBean.getInfo().getVersion();
        if (BGAUpgradeUtil.tryInstallApk(this,oldVersion,newVersion,mUpdateBean.getInfo().getMd5(),new ApplierListen(){
            @Override
            public void onApplierSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tryInstallApk(BGAUpgradeUtil.getApkFile(newVersion));
                    }
                });
            }

            @Override
            public void onApplierFail(final String s) {
                Log.e(Tag,s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BGAUpgradeUtil.deleteApkExcpetFile(MainActivity.this);
                        showToast(s);
                    }
                });
            }
        })) {
            return;
        }
        //开始下载文件
        // 检查是否有本地安装包 如有则下载对应差分包
        //                      如无则下载新版apk文件
        File oldApkFile = BGAUpgradeUtil.getApkFile(oldVersion);
        if (oldApkFile.exists()){
            //下载差分包
            downloadUpdateFile("/patch/yiyiyaya_v" + oldVersion + "_" + newVersion + ".patch",oldVersion,newVersion);
        }else{
            downloadUpdateFile("/newversion/yiyiyaya_" + newVersion + ".apk");
        }
    }

    /**
     * 下载Apk文件
     */
    private void downloadUpdateFile(String url){
        BGAUpgradeUtil.downloadApkFile(mUpdateBean.getInfo().getVersionUrl() + url, mUpdateBean.getInfo().getVersion())
                .retry(3)
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onStart() {
                        HProgressDialogUtils.showHorizontalProgressDialog(MainActivity.this, "下载进度", true);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HProgressDialogUtils.cancel();
                        if (mNeedUpdate == -1) {
                            //强制更新
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("更新失败")
                                    .setCancelable(false)
                                    .setMessage("更新发生了错误，请重试")
                                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startUpdateVersion();
                                        }
                                    })
                                    .create()
                                    .show();
                        }else if (mNeedUpdate == 1){
                            //可以更新
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("更新失败")
                                    .setMessage("更新发生了错误，请重试")
                                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startUpdateVersion();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }

                    @Override
                    public void onNext(File updateFile) {
                        if (updateFile != null) {
                            HProgressDialogUtils.cancel();
                            tryInstallApk(updateFile);
                        }
                    }
                });
    }

    /**
     * 下载patch文件
     */
    private void downloadUpdateFile(String url,String oldversion, String newversion){
        BGAUpgradeUtil.downloadPatchFile(mUpdateBean.getInfo().getVersionUrl() + url, oldversion,newversion)
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onStart() {
                        HProgressDialogUtils.showHorizontalProgressDialog(MainActivity.this, "下载进度", true);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HProgressDialogUtils.cancel();
                        if (mNeedUpdate == -1) {
                            //强制更新
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("更新失败")
                                    .setCancelable(false)
                                    .setMessage("更新发生了错误，请重试")
                                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startUpdateVersion();
                                        }
                                    })
                                    .create()
                                    .show();
                        }else if (mNeedUpdate == 1) {
                            //可以更新
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("更新失败")
                                    .setMessage("更新发生了错误，请重试")
                                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startUpdateVersion();
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }

                    @Override
                    public void onNext(final File updateFile) {
                        //差分包
                        final String newVersion =  mUpdateBean.getInfo().getVersion();
                        BGAUpgradeUtil.buildPatchApk(updateFile, CommonUtil.getVersionName(MainActivity.this),newVersion,
                                new ApplierListen() {
                                    @Override
                                    public void onApplierSuccess() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                HProgressDialogUtils.cancel();
                                                tryInstallApk(BGAUpgradeUtil.getApkFile(newVersion));
                                            }
                                        });
                                    }

                                    @Override
                                    public void onApplierFail(final String s) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                HProgressDialogUtils.cancel();
                                                BGAUpgradeUtil.deleteApkExcpetFile(MainActivity.this);
                                                if (mNeedUpdate == -1){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                    builder.setCancelable(false);
                                                    builder.setTitle("更新失败");
                                                    builder.setMessage("更新文件发生了错误");
                                                    builder.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            startUpdateVersion();
                                                        }
                                                    });
                                                    builder.show();
                                                }else{
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                    builder.setTitle("更新失败");
                                                    builder.setMessage("更新文件发生了错误");
                                                    builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            startUpdateVersion();
                                                        }
                                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    builder.show();
                                                }
                                            }
                                        });
                                    }
                                });
                    }
                });
    }

    private void tryInstallApk(File apkFile){
        if (!apkFile.exists()){
            Log.e("UpdateVersion","apkFile not find!");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("更新失败");
            builder.setMessage("更新文件未找到");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            BGAUpgradeUtil.deleteApkExcpetFile(this);
            return;
        }
//        验证是否签名相同
        if (BGAUpgradeUtil.isSameSignatureInfo(MainActivity.this,apkFile)) {
            BGAUpgradeUtil.installApk(apkFile);
        }else{
            Log.e("UpdateVersion","md5 vertify faild! remove files");
            BGAUpgradeUtil.deleteApkExcpetFile(this);
            if (mNeedUpdate == -1){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("更新失败");
                builder.setMessage("更新文件发生了错误");
                builder.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startUpdateVersion();
                    }
                });
                builder.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("更新失败");
                builder.setMessage("更新文件发生了错误");
                builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startUpdateVersion();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        }
    }

    /**
     * 开始更新
     */
    public void startUpdateVersion() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (AndPermission.hasPermission(this, perms)) {
            // 如果新版 apk 文件已经下载过了，直接 return，此时不需要开发者调用安装 apk 文件的方法，在 tryInstallApk 里已经调用了安装」
            handleUpdate();
        } else {
//            AndPermission.requestPermissions(this, "更新版本需要授权读写外部存储权限!", RC_PERMISSION_DOWNLOAD, perms);
            AndPermission.with(MainActivity.this)
                    .requestCode(100)
                    .permission(perms)
                    .send();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            handleUpdate();
        }
    }

    /**
     * 比对版本号
     * @param dataBean
     * return 0-无需更新 1-可以更新 -1-必须更新
     */
    private int compareVersion(UpdateBean.DataBean dataBean) {
        String curVersion = CommonUtil.getVersionName(this);
        String lowVersion = dataBean.getInfo().getVersionLowest();
        String serverVersion = dataBean.getInfo().getVersion();
        int needUpdate = 0;
        if (!TextUtils.isEmpty(lowVersion)){
            Integer curv = Integer.parseInt(curVersion.replace(".",""));
            Integer lowv = Integer.parseInt(lowVersion.replace(".",""));
            needUpdate = lowv >= curv ? -1 : 0;
        }
        if (needUpdate != 0)
            return needUpdate;
        if (!TextUtils.isEmpty(serverVersion) && !TextUtils.isEmpty(curVersion) && !curVersion.equals(serverVersion)){
            Integer curv = Integer.parseInt(curVersion.replace(".",""));
            Integer serv = Integer.parseInt(serverVersion.replace(".",""));
            needUpdate = serv > curv ? 1 : 0;
        }
        return needUpdate;
    }

    @Override
    protected void initView() {
        setStatusViewVisiable(false);
        mBottomBar.addItem(new BottomBarTab(this,R.mipmap.ic_nor_todaymall,R.mipmap.ic_nor_bluetodaymall,"今日特卖").setSelectTextColor(getResources().getColor(R.color.theme_bule)))
                .addItem(new BottomBarTab(this,R.mipmap.ic_nor_bluepay,R.mipmap.ic_nor_pay,"租赁").setSelectTextColor(getResources().getColor(R.color.theme_bule)))
                .addItem(new BottomBarTab(this,R.mipmap.ic_nor_home,R.mipmap.ic_nor_grayhome,"YiYa商城").setSelectTextColor(getResources().getColor(R.color.theme_bule)))
                .addItem(new BottomBarTab(this,R.mipmap.ic_nor_blueshopcar,R.mipmap.ic_nor_gray_shopcar,"购物车").setSelectTextColor(getResources().getColor(R.color.theme_bule)))
                .addItem(new BottomBarTab(this,R.mipmap.ic_nor_graymine,R.mipmap.ic_nor_mine,"我的").setSelectTextColor(getResources().getColor(R.color.theme_bule)));

        mNetChangeObserver = new NetStateReceiver.NetChangeObserver() {
            @Override
            public void onNetConnected(int type) {

            }

            @Override
            public void onNetDisConnect() {
                if (HProgressDialogUtils.isShowing()){
                    HProgressDialogUtils.cancel();
                    if (mNeedUpdate == -1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle("更新失败");
                        builder.setMessage("更新发生了错误");
                        builder.setNegativeButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startUpdateVersion();
                            }
                        });
                        builder.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("更新失败");
                        builder.setMessage("更新发生了错误");
                        builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startUpdateVersion();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            }
        };
        NetStateReceiver.registerObserver(mNetChangeObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int index = YYApp.getInstance().getIndexShow();
        if (index != -1)
            mBottomBar.setCurrentItem(index);
    }

    @Override
    protected void bindEvent() {
        // 监听下载进度
        BGAUpgradeUtil.getDownloadProgressEventObservable()
                .compose(this.<BGADownloadProgressEvent>bindToLifecycle())
                .subscribe(new Action1<BGADownloadProgressEvent>() {
                    @Override
                    public void call(BGADownloadProgressEvent downloadProgressEvent) {
                        if (HProgressDialogUtils.isShowing() && downloadProgressEvent.isNotDownloadFinished()) {
                            HProgressDialogUtils.setMax(downloadProgressEvent.getTotal());
                            HProgressDialogUtils.setProgress(downloadProgressEvent.getProgress(),downloadProgressEvent.getTotal());
                        }
                    }
                });
    }

    @EventSubscribe
    public void tabSelect(MainTabSelectEvent tabSelectEvent){
        mBottomBar.setCurrentItem(tabSelectEvent.tab);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            ((HomeFragment)mFragments[0]).aotuRefresh();
        }else {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void initData() {

        mFragments[0] = HomeFragment.getInstance();
        mFragments[1] = NewLeaseFragment.getInstance();
        mFragments[2] = YiYaMallFragment.getInstance();
        mFragments[3] = ShopCarFragment.getInstance();
        mFragments[4] = MineFragment.getInstance();


        loadMultipleRootFragment(R.id.am_contanier,0,mFragments);
        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, int prePosition) {
                if (position == 3 && TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return false;
                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
                return true;
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }
}
