package com.yhkj.yymall.config;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by jingbin on 2016/11/17.
 * js通信接口
 */
public class JSInterface {
    private JsCallBack mJsCallBack;
    public JSInterface(JsCallBack jsCallBack) {
        this.mJsCallBack = jsCallBack;
    }

    @JavascriptInterface
    public void imageClick(String imgUrl, String hasLink) {
//        Toast.makeText(context, "----点击了图片", Toast.LENGTH_SHORT).show();
        // 查看大图
//        Intent intent = new Intent(context, ViewBigImageActivity.class);
//        context.startActivity(intent);
//        Log.e("----点击了图片 url: ", "" + imgUrl);
    }

    @JavascriptInterface
    public void textClick(String type, String item_pk) {
//        if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(item_pk)) {
//            Toast.makeText(context, "----点击了文字", Toast.LENGTH_SHORT).show();
//        }
    }

    @JavascriptInterface
    public void videoFullScreenClick(String url){
//        Toast.makeText(context, "----全屏", Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void invokeAosShare(String title,String content){
        mJsCallBack.callBack(title,content);
    }

    public interface JsCallBack {
        void callBack(String ... args);
    }
}
