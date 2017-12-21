package com.yhkj.yymall.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/22.
 */

public class CommonUtil {

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 判断网络是否连通
     */
    public static boolean isNetworkConnected(Context context) {
        try {
            if (context != null) {
                @SuppressWarnings("static-access")
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            } else {
                /**如果context为空，就返回false，表示网络未连接*/
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getVersionName(Context context)
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        String version = "";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            version = packInfo.versionName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获得中文个数
     */
    public static int getChineseCount(String str){
        int count=0;
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while(m.find())count++;
        return count;
    }

    //截取字符串长度(中文2个字节，半个中文显示一个)
    public static String subTextString(String str,int len){
        if(str.length()<len/2)return str;
        int count = 0;
        StringBuffer sb = new StringBuffer();
        String[] ss = str.split("");
        for(int i=1;i<ss.length;i++){
            count+=ss[i].getBytes().length>1?2:1;
            sb.append(ss[i]);
            if(count>=len)break;
        }
        //不需要显示...的可以直接return sb.toString();
        return (sb.toString().length()<str.length())?sb.append("..").toString():str;
    }

    /**
     * 电话打码
     */
    public static String getMarkPhone(String phone){
        if (TextUtils.isEmpty(phone)) return "";
        return phone.substring(0,3) + "****" + phone.substring(7,11);
    }

    /**
     * 计算图片尺寸
     * @param srcImg
     * @return
     */
    public static int[] computeImgFileSize(File srcImg) {
        int[] size = new int[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(srcImg.getAbsolutePath(), options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;
        return size;
    }

    /**
     * dip转px
     */
    public static int dipToPx(Context context, float dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean notEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 保存图片
     * @param context
     * @param bmp
     */
    public static File saveImage(Context context,String filename, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "yiyiyaya");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = filename;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 最后通知图库更新
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return file;
    }
    /**
     * 保存图片到相册
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "yiyiyaya");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
    public static boolean isWeiboClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String name = pinfo.get(i).packageName.toLowerCase(Locale.ENGLISH);
                if ("com.sina.weibo".equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String[] secToTime(long time) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0) {
            timeStr = "00:00:00";
            return timeStr.split(":");
        }else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    timeStr = "99:59:59";
                    return timeStr.split(":");
                }
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr.split(":");
    }
    public static String secToMills(long time) {
        String timeStr = null;
        long minute = 0;
        long second = 0;
        if (time <= 0) {
            timeStr = "00:00";
            return timeStr;
        }else {
            minute = time / 60;
            second = time % 60;
            timeStr = unitFormat(minute) + ":" + unitFormat(second);
        }
        return timeStr;
    }
    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
