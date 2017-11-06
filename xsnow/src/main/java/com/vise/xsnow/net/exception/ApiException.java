package com.vise.xsnow.net.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.vise.xsnow.net.mode.ApiCode;
import com.vise.xsnow.net.mode.ApiResult;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * @Description: API异常统一管理
 */
public class ApiException extends Exception {

    private final int code;
    private String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ApiException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDisplayMessage() {
        return message + "(code:" + code + ")";
    }

    public static boolean isSuccess(ApiResult apiResult) {
        if (apiResult == null) {
            return false;
        }
        if (apiResult.getCode() == ApiCode.Response.HTTP_SUCCESS || ignoreSomeIssue(apiResult.getCode())) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean ignoreSomeIssue(int code) {
        switch (code) {
//            case ApiCode.Response.ACCESS_TOKEN_FAILD://验证失败
//            case ApiCode.Response.ACCESS_TOKEN_EXPIRED://AccessToken错误或已过期
//            case ApiCode.Response.ERROR_PHONE_PWD:
//            case ApiCode.Response.REFRESH_TOKEN_EXPIRED://RefreshToken错误或已过期
//            case ApiCode.Response.OTHER_PHONE_LOGINED: //帐号在其它手机已登录
//            case ApiCode.Response.SIGN_ERROR://签名错误
//                return true;
            default:
                return false;
        }
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ApiCode.Request.HTTP_ERROR);
            switch (httpException.code()) {
                case ApiCode.Http.UNAUTHORIZED:
                case ApiCode.Http.FORBIDDEN:
                case ApiCode.Http.NOT_FOUND:
                case ApiCode.Http.REQUEST_TIMEOUT:
                case ApiCode.Http.GATEWAY_TIMEOUT:
                case ApiCode.Http.INTERNAL_SERVER_ERROR:
                case ApiCode.Http.BAD_GATEWAY:
                case ApiCode.Http.SERVICE_UNAVAILABLE:
                default:
                    ex.message = "NETWORK_ERROR";
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ex = new ApiException(e, ApiCode.Request.PARSE_ERROR);
            ex.message = "PARSE_ERROR";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ApiCode.Request.NETWORK_ERROR);
            ex.message = "网络错误，请检查网络是否通畅";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ApiCode.Request.SSL_ERROR);
            ex.message = "SSL_ERROR";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ApiCode.Request.TIMEOUT_ERROR);
            ex.message = "网络阻塞，请重试";
            return ex;
        } else {
            if (e instanceof ApiException){
                ApiException apiException = (ApiException)e;
                ex = new ApiException(e, apiException.getCode());
                switch (ex.getCode()){
                    case 3001: ex.message = "头像上传失败,请重试";break;
                    case 3002: ex.message = "修改个人信息失败,请重试";break;
                    case 3003: ex.message = "默认地址无法删除";break;
                    case 3004: ex.message = "删除地址不存在";break;
                    case 2001: ex.message = "授权验证失败，请重新登录";break;
                    case 1002: ex.message = "手机号或密码错误"; break;
                    case 1003: ex.message = "验证码不正确，请重新输入";  break;
                    case 1004: ex.message = "用户已注册，请直接登录";  break;
                    case 1005: ex.message = "第三方未找到用户";  break;
                    case 1008: ex.message = "新密码与原密码不能相同";  break;
                    case 1009: ex.message = "图片太大了，请使用小一点的图片~";  break;
                    case 3005: ex.message = "请先切换默认设备，再解绑当前设备";  break;
                    case 3006: ex.message = "当前设备为默认设备，不能关闭";  break;
                    case 4001: ex.message = "设备未绑定";  break;

                }
            }else{
                ex = new ApiException(e, ApiCode.Request.UNKNOWN);
                ex.message = "网络错误，请检查网络是否通畅";
            }
//            ex.message = "UNKNOWN";
            return ex;
        }
    }

}
