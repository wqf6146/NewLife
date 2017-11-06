package cn.bingoogolapple.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.google.archivepatcher.applier.ApplierListen;
import com.google.archivepatcher.applier.FileByFileV1DeltaApplier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static cn.bingoogolapple.update.AppUtil.sApp;
import static cn.bingoogolapple.update.Md5Util.getPackageArchiveInfo;
import static cn.bingoogolapple.update.Md5Util.getPublicKey;
import static cn.bingoogolapple.update.Md5Util.getSign;
import static cn.bingoogolapple.update.StorageUtil.deleteFile;
import static cn.bingoogolapple.update.StorageUtil.getApkVersionName;
import static cn.bingoogolapple.update.StorageUtil.getDiffFile;

/**
 */
public class BGAUpgradeUtil {
    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";


    private BGAUpgradeUtil() {
    }

    /**
     * 监听下载进度
     *
     * @return
     */
    public static Observable<BGADownloadProgressEvent> getDownloadProgressEventObservable() {
        return RxUtil.getDownloadEventObservable();
    }

    /**
     * apk 文件是否已经下载过，如果已经下载过就直接安装
     *
     //1.检查是否有最新版本的apk存在、如有立即安装 如无进入 2
     //2.检查是否已存在当前版本与最新版本的差分包和老的安装包 如有即合成-安装 如无安装包-进入4 、若有安装包 进入 3
     //3.下载差分包 合成 安装
     //4.下载最新安装包
     *
     * @return
     */
    public static boolean tryInstallApk(Context context,String oldversion, String newversion, String md5, ApplierListen applierListen) {
        File apkFile = StorageUtil.getApkFile(newversion);
        if (apkFile.exists()) {
            if (isSameSignatureInfo(context,apkFile)){
                installApk(apkFile);
                return true;
            }else{
                //删除有问题的安装文件
                StorageUtil.deleteFile(apkFile,null);
                return false;
            }
        }else{
            File patchFile = StorageUtil.getDiffFile(oldversion,newversion);
            File oldApkFile = StorageUtil.getApkFile(oldversion);
            if (patchFile.exists() && oldApkFile.exists()) {
                //合成
                buildPatchFile(oldApkFile, patchFile, newversion,applierListen);
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * 验证是否有相同的签名信息
     */
    public static boolean isSameSignatureInfo(Context context,File apkFile){
        byte[] signature = getSign(context);
        String installPublickey = getPublicKey(signature);//已安装的应用APK签名信息获取
        //apkFile ：下载的未安装APK的安装包路径，如：
        byte[] unstallSignature = getPackageArchiveInfo(apkFile, PackageManager.GET_ACTIVITIES | PackageManager.GET_SIGNATURES);
        String unInstallPublickey = getPublicKey(unstallSignature );//未安装的APK包的签名信息获取

        if (TextUtils.isEmpty(unInstallPublickey) || TextUtils.isEmpty(installPublickey))
            return false;

        return installPublickey.equals(unInstallPublickey);
    }

    /**
     * 获取安装包文件
     */
    public static File getApkFile(String version){
        return StorageUtil.getApkFile(version);
    }

    /**
     * 生成差分包后缀
     * example : v1.0.0_1.0.1
     */
    public static String getPatchSuffix(String oldversion,String newversion){
       return "v_" + oldversion + "_" + newversion;
    }

    /**
     * 获取差分包文件
     */
    public static File getDiffFile(String oldversion,String newversion){
        return StorageUtil.getDiffFile(oldversion,newversion);
    }

    /**
     * 差分包 文件是否已经下载过，如果已经下载过就直接合成
     *
     * @return
     */
    public static boolean isPatchFileDownloaded(String oldversion,String newversion) {
        File patchFile = StorageUtil.getDiffFile(oldversion,newversion);
        if (patchFile.exists()) {
            return true;
        }
        return false;
    }


    /**
     * 下载新版 apk 文件
     *
     * @param url     apk 文件路径
     * @param version 新 apk 文件版本号
     * @return
     */
    public static Observable<File> downloadApkFile(final String url, final String version) {
        return Observable.defer(new Func0<Observable<InputStream>>() {
            @Override
            public Observable<InputStream> call() {
                try {
                    return Observable.just(Engine.getInstance().getDownloadApi().downloadFile(url).execute().body().byteStream());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).map(new Func1<InputStream, File>() {
            @Override
            public File call(InputStream inputStream) {
                return StorageUtil.saveApk(inputStream, version);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 下载新版 差分包 文件
     *
     * @return
     */
    public static Observable<File> downloadPatchFile(final String url, final String oldversion,final String newversion) {
        return Observable.defer(new Func0<Observable<InputStream>>() {
            @Override
            public Observable<InputStream> call() {
                try {
                    return Observable.just(Engine.getInstance().getDownloadApi().downloadFile(url).execute().body().byteStream());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).map(new Func1<InputStream, File>() {
            @Override
            public File call(InputStream inputStream) {
                return StorageUtil.savePatch(inputStream, oldversion,newversion);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 合成差分包文件
     */
    public static void buildPatchFile(final File oldFile,final File patchFile,final String newversion,final ApplierListen applierListen){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Inflater uncompressor = new Inflater(true);
                try {
                    FileInputStream compressedPatchIn = new FileInputStream(patchFile);
                    final InflaterInputStream patchIn =
                            new InflaterInputStream(compressedPatchIn, uncompressor, 32768);
                    final FileOutputStream newFileOut = new FileOutputStream(getApkVersionName(newversion));
                    new FileByFileV1DeltaApplier().applyDelta(oldFile, patchIn, newFileOut,applierListen);
                } catch (Exception e) {
                    e.printStackTrace();
                    applierListen.onApplierFail(e.getMessage());
                }finally {
                    uncompressor.end();
                }
            }
        }).start();
    }

    /**
     * 合成patch文件
     */
    public static void buildPatchApk(File patchFile,String oldVersion,String newVersion,final ApplierListen applierListen) {
        buildPatchFile(StorageUtil.getApkFile(oldVersion), patchFile, newVersion,applierListen);
    }

    /**
     * 安装 apk 文件
     *
     * @param apkFile
     */
    public static void installApk(File apkFile) {
        Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            installApkIntent.setDataAndType(FileProvider.getUriForFile(sApp, PermissionUtil.getFileProviderAuthority(), apkFile), MIME_TYPE_APK);
            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            installApkIntent.setDataAndType(Uri.fromFile(apkFile), MIME_TYPE_APK);
        }

        if (sApp.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            sApp.startActivity(installApkIntent);
        }
    }

    /**
     * 删除之前升级时下载的老的 apk 文件
     */
    public static void deleteFile(File file,Context context) {
        StorageUtil.deleteFile(file,context);
    }

    /**
     * 删除安装目录下的文件
     */
    public static void deleteApkExcpetFile(Context context) {
        File apkDir = StorageUtil.getApkFileDir();
        if (apkDir == null || apkDir.listFiles() == null || apkDir.listFiles().length == 0) {
            return;
        }

        // 删除文件
        StorageUtil.deleteFile(apkDir,context);
    }
}
