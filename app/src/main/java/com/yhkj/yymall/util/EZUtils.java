package com.yhkj.yymall.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.bean.EZCameraInfo;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.videogo.util.LogUtil;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;



public class EZUtils {


    public static void saveCapturePictrue(String filePath, Bitmap bitmap) throws InnerException {
        if (TextUtils.isEmpty(filePath)) {
            LogUtil.d("EZUtils", "saveCapturePictrue file is null");
            return;
        }
        File filepath = new File(filePath);
        File parent = filepath.getParentFile();
        if (parent == null || !parent.exists() || parent.isFile()) {
            parent.mkdirs();
        }
        FileOutputStream out = null;
        try {
            // 保存原图

            if (!TextUtils.isEmpty(filePath)) {
                out = new FileOutputStream(filepath);
                bitmap.compress(CompressFormat.JPEG, 100, out);
                //out.write(tempBuf, 0, size);
                out.flush();
                out.close();
                out = null;
            }
        } catch (FileNotFoundException e) {
//            throw new InnerException(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
//            throw new InnerException(e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将视频插入图库
     * @param url 视频路径地址
     */
    public static void updateVideo(Context context,String url){
        File file=new File(url);
        //获取ContentResolve对象，来操作插入视频
        ContentResolver localContentResolver = context.getContentResolver();
        //ContentValues：用于储存一些基本类型的键值对
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        //insert语句负责插入一条新的纪录，如果插入成功则会返回这条记录的id，如果插入失败会返回-1。
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,localContentValues);
    }

    //再往数据库中插入数据的时候将，将要插入的值都放到一个ContentValues的实例当中
    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong){
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/3gp");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    public static Object getPrivateMethodInvoke(Object instance, /*Class destClass,*/ String methodName,
                                                Class<?>[] parameterClass, Object... args) throws Exception {
        Class<?>[] parameterTypes = null;
        if (args != null) {
            parameterTypes = parameterClass;
        }
        Method method = instance.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(instance, args);
    }

    /**
     * 通过ezdevice 得到其中通道信息
     *
     * @param deviceInfo
     * @return
     */
    public static EZCameraInfo getCameraInfoFromDevice(EZDeviceInfo deviceInfo, int camera_index) {
        if (deviceInfo == null) {
            return null;
        }
        if (deviceInfo.getCameraNum() > 0 && deviceInfo.getCameraInfoList() != null && deviceInfo.getCameraInfoList().size() > camera_index) {
            return deviceInfo.getCameraInfoList().get(camera_index);
        }
        return null;
    }

    /**
     * EZDeviceInfo 得到取流通道和探测器意外的信息对象
     *
     * @param deviceInfo
     * @return
     */
    public static EZDeviceInfo CopyEzDeviceInfoNoCameraAndDetector(EZDeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return null;
        }
        EZDeviceInfo ezDeviceInfo = new EZDeviceInfo();
        ezDeviceInfo.setDeviceName(deviceInfo.getDeviceName());
        ezDeviceInfo.setIsEncrypt(deviceInfo.getIsEncrypt());
        ezDeviceInfo.setCameraNum(deviceInfo.getCameraNum());
        ezDeviceInfo.setDefence(deviceInfo.getDefence());
        return deviceInfo;
    }

    /**
     * 此判断方式只能判断获取报警信息列表获取到的报警图片url
     *
     * @param url
     * @return 图片url解析得到图片是否加密
     */
    private static boolean isEncrypt(String url) {
        int ret = 0;
        try {
            Uri uri = Uri.parse(url);
            ret = Integer.parseInt(uri.getQueryParameter("isEncrypted"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret == 1;
    }

    /**
     * 注意：
     * 图片加载，加密图片需要先得到byte数据，然后调用EZOpenSDK.getInstance().decryptData进行解码，得到能够正常显示的图片信息
     * 密码需要开发者自己存储，图片信息获取以及缓存策略需要开发者自己设计缓存规则
     */
    public static void loadImage(final Context context, final ImageView imageView, final String url, final String deviceSerial, final VerifyCodeInput.VerifyCodeErrorListener verifyCodeErrorListener) {
        final String verifyCode = DataManager.getInstance().getDeviceSerialVerifyCode(deviceSerial);
        if (!isEncrypt(url)) {
            Glide.with(context).load(url)
                    .placeholder(R.mipmap.ic_nor_srcpic)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                            if (e != null) {
                                e.printStackTrace();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                            return false;
                        }
                    })
                    .error(R.mipmap.ic_nor_srcpic)
                    .into(imageView);
        } else {
            if (TextUtils.isEmpty(verifyCode)) {
                imageView.setImageResource(R.drawable.alarm_encrypt_image_mid);
                if (verifyCodeErrorListener != null) {
                    verifyCodeErrorListener.verifyCodeError();
                }
                return;
            }
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    /**************图片加载监听，打印错误信息*************************/
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            if (e != null) {
                                e.printStackTrace();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .placeholder(R.mipmap.ic_nor_srcpic)
                    /**************加密图片本地文件缓存,开发者自己决定缓存机制*******************/
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .imageDecoder(new ResourceDecoder<InputStream, Bitmap>() {
                        @Override
                        public Resource<Bitmap> decode(InputStream source, int width, int height) throws IOException {
                            ByteArrayOutputStream output = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            Bitmap desBitmap = null;
                            int n = 0;
                            while (-1 != (n = source.read(buffer))) {
                                output.write(buffer, 0, n);
                            }
                            output.flush();
                            output.close();
                            byte[] src = output.toByteArray();
                            if (src == null || src.length <= 0) {
                                LogUtil.d("EZUTils", "图片加载错误！");
                                return null;
                            }
                            if (!isEncrypt(url)) {
                                desBitmap = BitmapFactory.decodeByteArray(src, 0, src.length);
                            } else {
                                /*************** 开发者需要调用此接口解密 ****************/
                                byte[] data1 = EZOpenSDK.getInstance().decryptData(output.toByteArray(), verifyCode);
                                if (data1 == null || data1.length <= 0) {
                                    LogUtil.d("EZUTils", "verifyCodeError！");
                                    /*************** 验证码错误 ,此处回调是在子线程中，处理UI需调回到主线程****************/
                                    if (verifyCodeErrorListener != null) {
                                        verifyCodeErrorListener.verifyCodeError();
                                    }
                                } else {
                                    desBitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                                }
                            }
                            if (desBitmap != null) {
                                return new BitmapResource(desBitmap, DataManager.getInstance().getBitmapPool(context));
                            }
                            return null;
                        }

                        @Override
                        public String getId() {
                            return url;
                        }
                    })
                    .into(imageView);
        }
    }
}
