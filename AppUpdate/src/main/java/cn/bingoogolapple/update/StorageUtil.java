package cn.bingoogolapple.update;

import android.content.Context;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static cn.bingoogolapple.update.AppUtil.sApp;

/**
 */
class StorageUtil {
    private static final String DIR_NAME_APK = "yiyiyaya_apk";

    private StorageUtil() {
    }

    /**
     * 获取 apk 文件夹
     *
     * @return
     */
    static File getApkFileDir() {
        return sApp.getExternalFilesDir(DIR_NAME_APK);
    }

    /**
     * 获取 apk 文件
     *
     * @param version
     * @return
     */
    static File getApkFile(String version) {
        String appName;
        try {
            appName = sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), 0).applicationInfo.loadLabel(sApp.getPackageManager()).toString();
        } catch (Exception e) {
            // 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
            appName = "";
        }
        return new File(getApkFileDir(), appName.toLowerCase() + "_" + version + ".apk");
    }

    /**
     * 获取安装路径版本号对应app名字
     */
    static String getApkVersionName(String version){
        String appName;
        try {
            appName = sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), 0).applicationInfo.loadLabel(sApp.getPackageManager()).toString();
        } catch (Exception e) {
            // 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
            appName = "";
        }
        return getApkFileDir() + "/" +  appName.toLowerCase() + "_" + version + ".apk";
    }

    /**
     * 获取文件(patch)
     */
    static File getDiffFile(String oldverion,String newersion) {
        String appName;
        try {
            appName = sApp.getPackageManager().getPackageInfo(sApp.getPackageName(), 0).applicationInfo.loadLabel(sApp.getPackageManager()).toString();
        } catch (Exception e) {
            // 利用系统api getPackageName()得到的包名，这个异常根本不可能发生
            appName = "";
        }
        return new File(getApkFileDir(), appName.toLowerCase() + "_v" + oldverion + "_" + newersion + ".patch");
    }

    /**
     * 保存 apk 文件
     *
     * @param is
     * @param version
     * @return
     */
    static File saveApk(InputStream is, String version) {
        File file = getApkFile(version);

        if (writeFile(file, is)) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * 保存 Patch 文件
     *
     * @param is
     * @return
     */
    static File savePatch(InputStream is, String oldversion,String newversion) {
        File file = getDiffFile(oldversion,newversion);

        if (writeFile(file, is)) {
            return file;
        } else {
            return null;
        }
    }
    /**
     * 根据输入流，保存文件
     *
     * @param file
     * @param is
     * @return
     */
    static boolean writeFile(File file, InputStream is) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = is.read(data)) != -1) {
                os.write(data, 0, length);
            }
            os.flush();
            return true;
        } catch (Exception e) {
            if (file != null && file.exists()) {
                file.deleteOnExit();
            }
            e.printStackTrace();
        } finally {
            closeStream(os);
            closeStream(is);
        }
        return false;
    }
    /**
     * 删除当前版本外的文件
     *
     * @param file
     */
    static void deleteExcludeFile(File file,String version) {
        try {
            if (file == null || !file.exists()) {
                return;
            }

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        if (f.exists()) {
                            if (f.isDirectory()) {
                                deleteExcludeFile(f, version);
                            } else {
                                String fileName = f.getName();
                                if (!fileName.endsWith(version)) {
                                    f.delete();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    static void deleteFile(File file,Context context) {
        try {
            if (file == null || !file.exists()) {
                return;
            }

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        if (f.exists()) {
                            if (f.isDirectory()) {
                                deleteFile(f,context);
                            } else {
                                if (context==null)
                                    f.delete();
                                else {
                                    String fileName=f.getName();
                                    String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
                                    if (!prefix.equals("apk") || f.length() == 0 || !BGAUpgradeUtil.isSameSignatureInfo(context,f)){
                                        f.delete();
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭流
     *
     * @param closeable
     */
    static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
