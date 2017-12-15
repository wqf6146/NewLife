package com.yhkj.yymall.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;


import com.google.gson.Gson;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.api.ViseApi;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.UserConfig;
import com.yhkj.yymall.http.api.ApiService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_ERROR;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_EXPIRED;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_FAILD;

/**
 * Created by Administrator on 2017/7/3.
 */

public class YYMallApi {
    public static class ApiResult<T> extends ApiCallback<T>{

        private Context mContext;

        public ApiResult(Context context){
            mContext = context;
        }

        @Override
        public void onError(ApiException e) {
            switch (e.getCode()){
                //授权失败 需重新登录
                case ACCESS_TOKEN_ERROR :
                case ACCESS_TOKEN_FAILD :
                case ACCESS_TOKEN_EXPIRED :
                    List<UserConfig> list = DbHelper.getInstance().userConfigLongDBManager().loadAll();
                    if (list!=null && list.size() > 0) {
                        DbHelper.getInstance().userConfigLongDBManager().deleteAll();
                        mContext.startActivity(new Intent(mContext,LoginActivity.class));
                    }
            }
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onStart() {

        }
    }

    /**
     * 轮播图接口
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getBanner(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.BANNER, new HashMap<String, String>(), false, callback);
    }

    /**
     * 拼团详情
     */
    public static <T> void getGroupDetail(Context context, int GroupUserId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("GroupUserId", GroupUserId);
        api.apiPost(ApiService.GROUPDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 宝宝信息丫丫
     */
    public static <T> void getBaByInfoYaya(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETBABYYAYA, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * 首页活动相关接口
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getHomeAct(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.HOMEACT, new HashMap<String, String>(), false, callback);
    }

    /**
     * 线下活动接口回调
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void OfflineActCallback(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.post(ApiService.OFFLINECALLBACK, YYApp.getInstance().getToken(),new HashMap(), false, callback);
    }



    /**
     * 可选规格
     */
    public static <T> void getEnableSpec(Context context,String goodsId, String spec,Boolean bShow, ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("goodsId",goodsId);
        if (TextUtils.isEmpty(spec)){
            api.apiPost(ApiService.ENABLESPEC,YYApp.getInstance().getToken(),hashMap,bShow, callback);
        }else{
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), spec);
            api.apiPost(ApiService.ENABLESPEC,YYApp.getInstance().getToken(),body,hashMap,bShow, callback);
        }
    }

    /**
     * 添加宝宝信息
     */
    public static <T> void addBaByInfo(Context context,int sex, String brithday,int type,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("sex",sex);
        hashMap.put("brithday",brithday);
        hashMap.put("type",type);
        api.apiPost(ApiService.ADDBABY,YYApp.getInstance().getToken(),hashMap, true, callback);
    }
    /** 添加宝宝信息
     */
    public static <T> void updateBaByInfo(Context context,int babyid,int sex, String brithday,int type,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("id",babyid);
        hashMap.put("sex",sex);
        hashMap.put("brithday",brithday);
        hashMap.put("type",type);
        api.apiPost(ApiService.UPDATEBABY,YYApp.getInstance().getToken(),hashMap, true, callback);
    }

    /**
     * 获取折扣商品列表
     */
    public static <T> void getDiscoutnShopList(Context context,int page,Integer categoryId,boolean bShow,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("page",page);
        if (categoryId!=null)
            hashMap.put("categoryId",categoryId);
        api.apiPost(ApiService.DISCOUNTLIST,hashMap, bShow, callback);
    }

    /**
     * 获取消息
     */
    public static <T> void getMesById(Context context,String id,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("id",id);
        api.apiPost(ApiService.GETMES,YYApp.getInstance().getToken(),hashMap, false, callback);
    }

    public static <T> void getVideoList(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETVIDEOLIST,YYApp.getInstance().getToken(),new HashMap(), false, callback);
    }

    /**
     * 获取积分商品列表
     */
    public static <T> void getIntegralShopList(Context context,int page,Integer categoryId,boolean bShow,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("page",page);
        if (categoryId!=null)
            hashMap.put("categoryId",categoryId);
        api.apiPost(ApiService.INTEGRALLIST,hashMap, bShow, callback);
    }

    /**
     *更新宝宝信息
     */
    public static <T> void deleteBaByInfo(Context context,int babyid,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap =  new HashMap();
        hashMap.put("id",babyid);
        api.apiPost(ApiService.DELBABY,YYApp.getInstance().getToken(),hashMap, false, callback);
    }

    /**
      *更新宝宝信息
     */
    public static <T> void getBaByInfo(Context context,int babyid,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap =  new HashMap();
        hashMap.put("id",babyid);
        api.apiPost(ApiService.GETBABYINFO,YYApp.getInstance().getToken(),hashMap, false, callback);
    }

    /**
     * 日常活动头部
     */
    public static <T> void getDailyHead(Context context ,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.DAILYACTHEAD, new HashMap(), false, callback);
    }

    /**
     * 日常活动商品列表
     */
    public static <T> void getDailyList(Context context, int dailyActiveId,Integer categoryId,int page,boolean bshow,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("dailyActiveId",dailyActiveId);
        if (categoryId != null)
            hashMap.put("categoryId",categoryId);
        hashMap.put("page",page);
        api.apiPost(ApiService.DAILYLIST, hashMap, bshow, callback);
    }


    /**
     * 首页推荐相关接口
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getHomeRecomm(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.HOMEREMMOND, new HashMap<String, String>(), false, callback);
    }

    /**
     * 获取地区
     */
    public static <T> void getPlaces(Context context, int id, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("areaId", String.valueOf(id));
        api.post(ApiService.GETPLACES, hashMap, false, callback);
    }

    /**
     * 我的评价列表
     */
    public static <T> void getMineEvaList(Context context,int page,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("page", page);
        api.apiPost(ApiService.MINEEVALIST, YYApp.getInstance().getToken(),hashMap, false, callback);
    }

    /**
     * 发送短信接口
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getNote(Context context, String phone, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("phone", phone);
        api.apiPost(ApiService.SENDMSG, hashMap, true, callback);
    }

    public static <T> void getVideoDesc(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.VIDEODESC, new HashMap(), false, callback);
    }

    /**
     * 注册接口
     *
     * @param context
     * @param hashMap
     * @param callback
     * @param <T>
     */
    public static <T> void getRegister(Context context, HashMap hashMap, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.REGISTER, hashMap, true, callback);
    }

    /**
     * 商品分类
     */
    public static <T> void getShopClassify(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.SHOPCLASSIFY, new HashMap<String, String>(), false, callback);
    }

    /**
     * 登录接口
     *
     * @param context
     * @param hashMap
     * @param callback
     * @param <T>
     */
    public static <T> void getLogin(Context context, HashMap hashMap, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.LOGIN, hashMap, true, callback);
    }

    /**
     * 找回密码
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getForget(Context context, String phone, String pwd, String code, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("phone", phone);
        hashMap.put("password", pwd);
        hashMap.put("msgcode", code);
        api.apiPost(ApiService.RESETPASSWORD, hashMap, false, callback);
    }

    /**
     * 读取购物车数据
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getShopCar(Context context, String token,boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.CARLIST, token, new HashMap(), bShow, callback);
    }



    /**
     * 热门关键字
     */
    public static <T> void getHotKey(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.HOTKEYVALUE, new HashMap(), false, callback);
    }

    /**
     * 关键字搜索商品
     */
    public static <T> void getShopList(Context context,String url, String search,String categoryId, String order, String by, String page, String limit, String brand, String attr,boolean bLoadShow, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).connectTimeout(60).readTimeout(60).writeTimeout(60).build();
        HashMap hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(search)){
            hashMap.put("search", search);
        }
        if (!TextUtils.isEmpty(categoryId)){
            hashMap.put("categoryId",categoryId);
        }
        if (!TextUtils.isEmpty(order)) {
            hashMap.put("order", order);
        }
        if (!TextUtils.isEmpty(by)) {
            hashMap.put("by", by);
        }
        hashMap.put("page", page);
        hashMap.put("limit", limit);
        if (!TextUtils.isEmpty(brand)) {
            hashMap.put("brand", brand);
        }
        if (!TextUtils.isEmpty(attr)) {
            api.apiPost(url, YYApp.getInstance().getToken(), RequestBody.create(MediaType.parse("application/json"), attr), hashMap, bLoadShow, callback);
        } else {
            api.apiPost(url, YYApp.getInstance().getToken(), hashMap, bLoadShow, callback);
        }
    }

    /***
     * String TIMEKILL_PREORDER = HOST + "/order/previewPanicBuy";//限时抢购预览订单
     * String TIMEKILL_SUBMITORDER = HOST + "/order/submitPanicBuy";//提交订单
     */
    public static <T> void getPrewTimekillOrder(Context context, String productId, String nums, String panicBuyId,Integer addressid, Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<String, String>();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("panicBuyId", panicBuyId);
        if(addressid !=null)
            hashMap.put("addressId", addressid);
        api.apiPost(ApiService.TIMEKILL_PREORDER, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    public static <T> void submitTimekillOrder(Context context, String productId, String nums, String addressId, String panicBuyId,HashMap delivery,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("addressId", addressId);
        hashMap.put("panicBuyId", panicBuyId);

        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.TIMEKILL_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.TIMEKILL_SUBMITORDER, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);

//        api.apiPost(ApiService.TIMEKILL_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /***
     * 添加收货地址
     */
    public static <T> void addAddress(Context context, String name, String zip, String province,
                                      String city, String area, String streeet, String address, String mobile, String idefault, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("accept_name", name);
        hashMap.put("zip", zip);
        hashMap.put("province", province);
        hashMap.put("city", city);
        hashMap.put("area", area);
        if (!TextUtils.isEmpty(streeet))
            hashMap.put("street", streeet);
        hashMap.put("address", address);
        hashMap.put("mobile", mobile);
        hashMap.put("is_default", idefault);
        api.apiPost(ApiService.ADDADDRESS, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /***
     * 修改收货地址
     */
    public static <T> void updateAddress(Context context, String name, String addressid, String zip, String province,
                                         String city, String area, String streeet, String address, String mobile, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("accept_name", name);
        hashMap.put("addressId", addressid);
        hashMap.put("zip", zip);
        hashMap.put("province", province);
        hashMap.put("city", city);
        hashMap.put("area", area);
        hashMap.put("street", streeet);
        hashMap.put("address", address);
        hashMap.put("mobile", mobile);
        api.apiPost(ApiService.UPDATEADDRESS, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 设置默认地址
     */
    public static <T> void setDefAddress(Context context, String addressid, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("addressId", addressid);
        api.apiPost(ApiService.SETDEFADDRESS, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 删除地址
     */
    public static <T> void delAddress(Context context, String addressid, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("addressId", addressid);
        api.apiPost(ApiService.DELADDRESS, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 我的积分丫丫
     */
    public static <T> void getMyIntYaYa(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.MYINTEYAYA, new HashMap(), false, callback);
    }

    /***
     * 获取收货地址
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getAddressList(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETADDRESSLIST, YYApp.getInstance().getToken(), new HashMap<String, Object>(), false, callback);
    }


    /***
     * 获取抽奖信息
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getLotteryInfo(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETLOTTROYINFO, YYApp.getInstance().getToken(), new HashMap<String, Object>(), false, callback);
    }


    /***
     * 抽奖
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void startLottery(Context context, String rollid, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("rollid", rollid);
        api.apiPost(ApiService.DOLOTTERY, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 预览中奖订单
     */
    public static <T> void prewLotteryOrder(Context context, String prizeId,Integer addressid, Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("prizeId", prizeId);
        if (addressid!=null)
            hashMap.put("addressId",addressid);
        api.apiPost(ApiService.PREWDRAWLOTTE, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }
    /**
     * 提交中奖订单
     */
    public static <T> void submitLotteryOrder(Context context, String prizeId,String addressId,HashMap delivery, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("prizeId", prizeId);
        hashMap.put("addressId", addressId);
//        api.apiPost(ApiService.SUBMITDRAW, YYApp.getInstance().getToken(), hashMap, true, callback);


        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.SUBMITDRAW, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.SUBMITDRAW, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }

    /***
     * 热门领用
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getHotLease(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        api.apiPost(ApiService.LEASE_HOT, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 设置选中状态
     *
     * @param context
     * @param strings
     * @param callback
     * @param <T>
     */
    public static <T> void setShopCarSelect(Context context, String[] strings, String checked, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("checked", checked);
        api.apiPost(ApiService.SHOPCAR_SELECTSTATE, YYApp.getInstance().getToken(), strings, hashMap, true, callback);
    }

    /**
     * 获取logo
     */
    public static <T> void getLaunchLogo(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETLOGO, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * PRIVILEDGE
     */
    public static <T> void getPrivilege(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.PRIVILEDGE, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * 订单预览
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getLookIndent(Context context,Integer addressId, Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        if (addressId != null)
            hashMap.put("addressId",addressId);
        api.apiPost(ApiService.ORDER_PREVIEW, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    /***
     * 组拧筛选列表
     */
    public static <T> void getLeaseSelectList(Context context, String order, String by, String page, String limit, String brand, String attr,boolean bLoadView, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        if (!TextUtils.isEmpty(order)) {
            hashMap.put("order", order);
        }
        if (!TextUtils.isEmpty(by)) {
            hashMap.put("by", by);
        }
        hashMap.put("page", page);
        hashMap.put("limit", limit);
        if (!TextUtils.isEmpty(brand)) {
            hashMap.put("brand", brand);
        }
        if (!TextUtils.isEmpty(attr))
            api.apiPost(ApiService.LEASELISTS, YYApp.getInstance().getToken(), RequestBody.create(MediaType.parse("application/json"), attr), hashMap, bLoadView, callback);
        else
            api.apiPost(ApiService.LEASELISTS, YYApp.getInstance().getToken(), hashMap, bLoadView, callback);
    }
    /**
     * 获取分类筛选
     */
    public static <T> void getLeaseClassify(Context context, boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETLEASECLASSIFY, YYApp.getInstance().getToken(), new HashMap(), bShow, callback);
    }
    /**
     * 创建订单
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getOrderSubmit(Context context, String addressId, String useYaya,HashMap delivery, ApiCallback<T> callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("addressId", addressId);
        hashMap.put("useYaya", useYaya);
        ViseApi api = new ViseApi.Builder(context).build();
        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.ORDER_SUBMIT, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.ORDER_SUBMIT, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }

    /**
     * 获取商品评论列表
     */
    public static <T> void getShopEvaList(Context context, String goodsId, String page, String point, String isImg, ApiCallback<T> callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId", goodsId);
        hashMap.put("page", page);
        if (!TextUtils.isEmpty(point))
            hashMap.put("point", point);
        else if (!TextUtils.isEmpty(isImg))
            hashMap.put("isImg", isImg);
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETEVAALLLIST, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 等级梯形图
     */
    public static <T> void getRankChartData(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.RANKCHART,YYApp.getInstance().getToken(),new HashMap(),false,callback);
    }

    /**
     * 确认收货
     */
    public static <T> void verifyTakeGoods(Context context, String orderId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderId", orderId);
        api.apiPost(ApiService.VERIFYTAKE, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 获取商品印象标签
     */
    public static <T> void getShopEvaTag(Context context, String goodsId, ApiCallback<T> callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId", goodsId);
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETSHOPEVA, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 消息分类列表
     */
    public static <T> void getMesClassifyList(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETMESCATGORYLIST, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * 获取商品印象标签分类
     */
    public static <T> void getShopEvaClassify(Context context, String goodsId, int page, Integer impressionId, ApiCallback<T> callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId", goodsId);
        hashMap.put("page", String.valueOf(page));
        if (impressionId !=null )
            hashMap.put("impressionId", impressionId);
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETEVACLASSIFY, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 获取余额 GETBALANCE
     */
    public static <T> void getBalance(Context context,int type,boolean bShow, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("type",type);
        api.apiPost(ApiService.GETBALANCE, YYApp.getInstance().getToken(),hashMap, bShow, callback);
    }

    /***
     * String APPLYWITHDRAW = HOST + "/withdraw/applyWithdraw";//申请提现
     * <p>
     * String APPLYLIST = HOST + "/withdraw/withdrawRecord";//提现记录
     * <p>
     * String APPLYDETAIL = HOST + "/withdraw/withdrawDetails";//提现记录详情
     */
    //申请提现
    public static <T> void doApplyWithdraw(Context context, int type, String name,String account, float amount, ApiCallback<T> callback) {
        HashMap hashMap = new HashMap();
        hashMap.put("type", type);
        hashMap.put("account", account);
        hashMap.put("name", name);
        hashMap.put("amount", amount);
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.APPLYWITHDRAW, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    //提现列表
    public static <T> void getApplyWithdrawList(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.APPLYLIST, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    //提现记录详情
    public static <T> void getApplyDetail(Context context, int id, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        api.apiPost(ApiService.APPLYDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获取售后信息
     */
    public static <T> void getAfterMallInfo(Context context, String orderId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderId", orderId);
        api.apiPost(ApiService.GETAFTERMALLINFO, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获取售后信息
     */
    public static <T> void getAfterMallInfo(Context context, int orderId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderGoodsId", orderId);
        api.apiPost(ApiService.NEWAFTERMALlPAGE, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 提交商品评论
     */
    public static <T> void submitShopEva(Context context, String[] imp, Map<String, Object> maps, MultipartBody files, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        if (files == null || files.size() == 0) {
            api.apiPostImp(ApiService.SUBMITEVA, YYApp.getInstance().getToken(), imp, maps, true, callback);
            return;
        }
        api.uploadEva(ApiService.SUBMITEVA, YYApp.getInstance().getToken(), imp, maps, files, true, callback);
    }

    /**
     * 获得售后件数价格
     */
    public static <T> void getAfterMallPrice(Context context,int orderGoodsId,int count,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderGoodsId", orderGoodsId);
        hashMap.put("count", count);
        api.apiPost(ApiService.GETAFTERMALLPRICE, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 提交商品售后
     */
//    public static <T> void submitShopAfter(Context context, Map<String, Object> maps, MultipartBody files, ApiCallback<T> callback) {
//        ViseApi api = new ViseApi.Builder(context).build();
//        if (files == null || files.size() == 0) {
//            api.apiPostImp(ApiService.SUBMITAFTERMALL, YYApp.getInstance().getToken(), new String[2], maps, true, callback);
//            return;
//        }
//        api.uploadEva(ApiService.SUBMITAFTERMALL, YYApp.getInstance().getToken(), new String[1], maps, files, true, callback);
//    }

    /**
     * 提交商品售后
     */
    public static <T> void submitShopAfter(Context context, Map<String, Object> maps, MultipartBody files, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        if (files == null || files.size() == 0) {
            api.apiPostImp(ApiService.NEWSUBMITAFET, YYApp.getInstance().getToken(), new String[2], maps, true, callback);
            return;
        }
        api.uploadEva(ApiService.NEWSUBMITAFET, YYApp.getInstance().getToken(), new String[1], maps, files, true, callback);
    }

    //提交售后
    public static <T> void submitAdvance(Context context, Map<String, Object> maps, MultipartBody files, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        if (files == null || files.size() == 0) {
            api.apiPost(ApiService.ADVANCE, YYApp.getInstance().getToken(), maps, true, callback);
            return;
        }
        api.uploadImg(ApiService.ADVANCE, YYApp.getInstance().getToken(), maps, files, true, callback);
    }

    //限时限购分类
    public static <T> void timeKillClassify(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.TIMEKILLCLASSIFY, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * 充值
     */
    public static <T> void doRecharge(Context context,int paymentId,float account, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("paymentId",paymentId);
        hashMap.put("account",account);
        api.apiPost(ApiService.RECHARGE, YYApp.getInstance().getToken(), hashMap, true, callback);
    }
    /**
     * 充值回调
     */
    public static <T> void rechargeCallback(Context context,String orderNo, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderNo",orderNo);
        api.apiPost(ApiService.RECHARGECALLBACK, YYApp.getInstance().getToken(), hashMap, false, callback);
    }
    /**
     * 充值回调
     */
    public static <T> void getRechargeList(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.RECHARGELIST, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /***
     * 限时抢购时间
     */
    public static <T> void getTimeKillDate(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.KILLTIME, YYApp.getInstance().getToken(), new HashMap<String, Object>(), false, callback);
    }
    /**
     * 退款详情
     */
    public static <T> void getRefundDetail(Context context,String id,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        api.apiPost(ApiService.REFUNDDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 积分日志
     */
    public static <T> void getInterLog(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.INTELOG, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 余额日志
     */
    public static <T> void getBalanceLog(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.BALANCELOG, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 是否存在未读消息
     */
    public static <T> void getUnReadMesTag(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.UNREADMESEXIT, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * yaya日志
     */
    public static <T> void getYayaLog(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.YYLOG, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 全部拼团列表
     */
    public static <T> void getGroupList(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.GROUP_LISTS, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 成功拼团列表
     */
    public static <T> void getSucGroupList(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.SUCGROUPLIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 申请售后
     */
    public static <T> void getAfterMallList(Context context,int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page",page);
        api.apiPost(ApiService.APPLYAFTERMALLLIST, YYApp.getInstance().getToken(),hashMap , false, callback);
    }

    /**
     * 版本更新
     */
    public static <T> void getVersionInfo(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.UPDATE, new HashMap(), false, callback);
    }

    /**
     * 失败拼团列表
     */
    public static <T> void getFaildGroupList(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.FAILDGROUPLIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 等待拼团列表
     */
    public static <T> void getWaitGroupList(Context context, int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.WAITGROUPLIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }


    /***
     * 限时抢购列表
     */
    public static <T> void getTimeKillShopList(Context context, String categoryId, String paniclBuyId, String page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(categoryId)) {
            hashMap.put("categoryId", categoryId);
        }
        if (!TextUtils.isEmpty(paniclBuyId)) {
            hashMap.put("paniclBuyId", paniclBuyId);
        }
        hashMap.put("page", page);
        api.apiPost(ApiService.KILLSHOPLIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /***
     * 限时抢购分类
     */
    public static <T> void getTimeKillClassify(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.KILLCLASSIFY, YYApp.getInstance().getToken(), new HashMap<String, Object>(), false, callback);
    }


    /**
     * 商品详情
     *
     * @param context
     * @param callback
     * @param <T>
     */
    public static <T> void getShopInfo(Context context, String goodsId,int goodsType, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId", goodsId);
        hashMap.put("goodsType",goodsType);
        api.apiPost(ApiService.SHOP_INFO,YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 修改登录手机号
     */
    public static <T> void updatePhone(Context context, String phone,String msgcode,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap<>();
        hashMap.put("phone", phone);
        hashMap.put("msgcode", msgcode);
        api.apiPost(ApiService.UPDATEPHONE,YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 我的页面
     * MINEINFO
     */
    public static <T> void getMyInfo(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.MINEINFO, YYApp.getInstance().getToken(), new HashMap<String, Object>(), false, callback);
    }

    /**
     * 个人资料
     */
    public static <T> void getPersonalInfo(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.PERSONINFO, YYApp.getInstance().getToken(), new HashMap<String, Object>(), false, callback);
    }

    /**
     * 全部订单
     */
    public static <T> void getAllOrUnPayOrderList(Context context, int ptype, int status, int page, boolean bshow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("type", String.valueOf(ptype));
        hashMap.put("status", String.valueOf(status));
        hashMap.put("page", String.valueOf(page));
        api.apiPost(ApiService.ORDERALL, YYApp.getInstance().getToken(), hashMap, bshow, callback);
    }

    //    String CANCELORDER = HOST + "/order/cancelOrder";//取消订单
    public static <T> void cancelOrder(Context context, int orderId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderGroupId", String.valueOf(orderId));
        api.apiPost(ApiService.CANCELORDER, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    //    String UPDATEADDRESS = HOST + "/order/getOrderAdr";//修改订单地址
    public static <T> void updateOrderAddress(Context context, int orderGroupId,
                                              String acceptName, String province, String city, String area, String address, String mobile,
                                              ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderGroupId", String.valueOf(orderGroupId));
        hashMap.put("accept_name", acceptName);
        hashMap.put("province", province);
        hashMap.put("city", city);
        hashMap.put("area", area);
        hashMap.put("address", address);
        hashMap.put("mobile", mobile);
        api.apiPost(ApiService.MODIFADDRESS, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 获取订单收货地址
     */
    public static <T> void getOrderAddress(Context context, int orderId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderGroupId", String.valueOf(orderId));
        api.apiPost(ApiService.GETORDERADDRESS, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * String GETPEYPWDTIKER = HOST + "/pay/getTicket";//获取支付密码修改凭证
     * <p>
     * String UPDATEPAYPWD = HOST + "/pay/resetPayToken" ;//修改支付密码
     */
    public static <T> void getPayPwdTicket(Context context, String code, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("msgCode", code);
        api.apiPost(ApiService.GETPEYPWDTIKER, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    //修改支付密码
    public static <T> void updatePayPwd(Context context, String ticket, String payToken, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("ticket", ticket);
        hashMap.put("payToken", payToken);
        api.apiPost(ApiService.UPDATEPAYPWD, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /***
     * 我的订单（待收货，待评价）
     */
    public static <T> void getOrderThree(Context context, int status, int type, int page, int isComment, int isGroup,boolean bshow, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("status", String.valueOf(status));
        hashMap.put("page", String.valueOf(page));
        hashMap.put("isComment", String.valueOf(isComment));
        hashMap.put("type", String.valueOf(type));
        hashMap.put("isGroup", String.valueOf(isGroup));
        api.apiPost(ApiService.ORDER_THREE, YYApp.getInstance().getToken(), hashMap, bshow, callback);
    }

    /**
     * 商品印象标签
     *
     * @param context
     * @param goodsid
     * @param callback
     * @param <T>
     */
    public static <T> void getGoodsImp(Context context, String goodsid, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId", String.valueOf(goodsid));
        api.apiPost(ApiService.GOODSIMG, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /***
     * 收藏商品列表
     * COLLECTLIST
     * String COLLECTLIST = HOST + "/collect/collectList"; //"收藏商品列表"
     * <p>
     * String DELETE_COLLECTSHOP = HOST + "/collect/delCollect"; //删除收藏商品
     * <p>
     * String CLASSIFY_COLLECTLIST = HOST + "/collect/cateList"; //获得收藏分类
     */
    public static <T> void getCollectList(Context context, Integer page, String name, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        if (page != null)
            hashMap.put("page", page);
        if (!TextUtils.isEmpty(name))
            hashMap.put("cateName", name);
        api.apiPost(ApiService.COLLECTLIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * String CLASSIFY_COLLECTLIST = HOST + "/collect/cateList"; //获得收藏分类
     */
    public static <T> void getClassifyCollectList(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.CLASSIFY_COLLECTLIST, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * 订单评论页面
     */
    public static <T> void getOrderEva(Context context, String orderid, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hash = new HashMap();
        hash.put("id", orderid);
        api.apiPost(ApiService.ORDEREVA, YYApp.getInstance().getToken(), hash, false, callback);
    }

    /**
     * 添加收藏  String ADD_COLLECTSHOP = HOST + "/collect/addCollect"; //添加收藏商品
     *
     * @param context
     * @param arrShop
     * @param callback
     * @param <T>
     */
    public static <T> void addCollectShpp(Context context, String[] arrShop, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.ADD_COLLECTSHOP, YYApp.getInstance().getToken(), arrShop, new HashMap(), true, callback);
    }

    /**
     * String DELETE_COLLECTSHOP = HOST + "/collect/delCollect"; //删除收藏商品
     *
     * @param context
     * @param arrShop
     * @param callback
     * @param <T>
     */
    public static <T> void deleteCollectShpp(Context context, String[] arrShop, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.DELETE_COLLECTSHOP, YYApp.getInstance().getToken(), arrShop, new HashMap(), true, callback);
    }


    /***
     * 读取货品规格
     */
    public static <T> void getShopSpec(Context context, String goodid, String jsonSpec,Integer panicId,boolean loadView, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId", goodid);
        if (panicId != null){
            hashMap.put("panicId", panicId);
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonSpec);
        api.apiPost(ApiService.LOADGOODSPEC, YYApp.getInstance().getToken(), body, hashMap, loadView, callback);
    }

    /***
     * 抢购详情
     */
    public static <T> void getTimeKillDetail(Context context, String paniclBuyItemId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("paniclBuyItemId", paniclBuyItemId);
        api.apiPost(ApiService.KILLDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }


    /**
     * UPDATEUSERINFO
     */
    public static <T> void updateUserInfo(Context context, File avatar, String name, int sex, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        if (avatar == null) {
            HashMap hashMap = new HashMap();
            hashMap.put("name", name);
            hashMap.put("sex", sex);
            api.apiPost(ApiService.UPDATEUSERINFO, YYApp.getInstance().getToken(), hashMap, true, callback);
            return;
        }
        RequestBody requestName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
        RequestBody requestSex = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(sex));
        api.uploadImage(ApiService.UPDATEUSERINFO, YYApp.getInstance().getToken(), avatar, requestName, requestSex, true, callback);
    }

    /**
     * 线下活动
     */
    public static <T> void getOfflineAct(Context context, ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.OFFLINEACT, YYApp.getInstance().getToken(), new HashMap(),false, callback);
    }

    /**
     * 线下活动
     */
    public static <T> void getOfflineActIsValid(Context context,int id,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        api.apiPost(ApiService.OFFLINEACTVALID, YYApp.getInstance().getToken(),hashMap,false, callback);
    }




    /**
     * 加入购物车
     */
    public static <T> void getAddCar(Context context, int productId, int nums, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        api.apiPost(ApiService.ADD_CAR, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 预览订单(单品)
     */
    public static <T> void getOneLookOrder(Context context, String productId, String nums, Integer addressid,Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        if (addressid!=null)
            hashMap.put("addressId",addressid);
        api.apiPost(ApiService.ORDER_PREVIEWSINGLE, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    /**
     * YiYa商品列表
     */
    public static <T> void getYiYaShopList(Context context, Integer page,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", page);
        api.apiPost(ApiService.YIYASHOPLIST, hashMap,false, callback);
    }


    /**
     * 获取用户租赁情况
     * 押金状态（1:失效；0:租赁中）
     */
    public static <T> void getRentRecord(Context context, String status, String page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("status", status);
        hashMap.put("page", page);
        api.apiPost(ApiService.USERRECORD, YYApp.getInstance().getToken(), hashMap, false, callback);
    }


    /**
     * 我的等级
     */
    public static <T> void getMyExperience(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.MYEXPERIENCE, YYApp.getInstance().getToken(), new HashMap<String, Object>(), true, callback);
    }

    /**
     * 创建订单(单件)
     */
    public static <T> void getOneCreateOrder(Context context, String addressId, String useYaya, String productId, String nums,HashMap delivery, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("addressId", addressId);
        hashMap.put("useYaya", useYaya);
//        api.apiPost(ApiService.ORDER_SUBMITSINGLE, YYApp.getInstance().getToken(), hashMap, true, callback);


        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.ORDER_SUBMITSINGLE, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.ORDER_SUBMITSINGLE, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }

    /**
     * 置换详情
     */
    public static <T> void getRentDetailInfo(Context context, String id, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        api.apiPost(ApiService.RENTDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 置换押金
     */
    public static <T> void doRentCash(Context context, String id,int num, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        hashMap.put("num",num);
        api.post(ApiService.RENTCASH, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 创建订单(单件)
     */
    public static <T> void getLeaseOrder(Context context, String addressId, String useYaya, String productId, String nums,HashMap delivery, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("addressId", addressId);
        hashMap.put("useYaya", useYaya);
//        api.apiPost(ApiService.LEASE_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);

        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.LEASE_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.LEASE_SUBMITORDER, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }
    /**
     * 创建订单(折扣)
     */
    public static <T> void getDiscountOrder(Context context, String discountId, String addressId, String productId, String nums,HashMap delivery,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("addressId", addressId);
        hashMap.put("discountId", discountId);
//        api.apiPost(ApiService.DISCOUNT_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);

        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.DISCOUNT_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.DISCOUNT_SUBMITORDER, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }

    /**
     * 创建订单(日常)
     */
    public static <T> void getDailyOrder(Context context, String dailyId, String addressId, String productId, String nums,HashMap delivery,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("addressId", addressId);
        hashMap.put("dailyId", dailyId);
//        api.apiPost(ApiService.DISCOUNT_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);

        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.DAILY_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.DAILY_SUBMITORDER, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }

    /***
     * 赠品
     */
    public static <T> void getShopGift(Context context,int goodsId, ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("goodsId",goodsId);
        api.apiPost(ApiService.SHOPGIFT,hashMap,false,callback);
    }

    /**
     * 创建订单(积分)
     */
    public static <T> void getIntegralOrder(Context context, String integerId, String addressId, String productId, String nums, HashMap delivery, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        hashMap.put("addressId", addressId);
        hashMap.put("integerId", integerId);

        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.INTEGRAL_SUBMITORDER, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.INTEGRAL_SUBMITORDER, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }


    /***
     * 租宁预览订单
     */
    public static <T> void getLeasePreviewOrder(Context context, String productId, String nums, Integer addressid,Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        if (addressid != null)
            hashMap.put("addressId",addressid);
        api.apiPost(ApiService.LEASE_PREVIEWORDER, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    /***
     * 折扣预览订单
     */
    public static <T> void getDiscountPreviewOrder(Context context, String discountId, String productId, String nums, Integer addressid,Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("discountId", discountId);
        hashMap.put("nums", nums);
        if (addressid != null)
            hashMap.put("addressId",addressid);
        api.apiPost(ApiService.DISCOUNT_PREVIEWORDER, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    /***
     * 年终大促预览订单
     */
    public static <T> void getDailyPreviewOrder(Context context, String dailyId, String productId, String nums, Integer addressid,Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("dailyId", dailyId);
        hashMap.put("nums", nums);
        if (addressid != null)
            hashMap.put("addressId",addressid);
        api.apiPost(ApiService.DAILY_PREVIEWORDER, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }


    /***
     * 积分预览订单
     */
    public static <T> void getIntegralPreviewOrder(Context context, String integralId, String productId, String nums,Integer addressid, Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("integralId", integralId);
        hashMap.put("nums", nums);
        if (addressid != null)
            hashMap.put("addressId",addressid);
        api.apiPost(ApiService.INTEGRAL_PREVIEWORDER, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }
    /**
     * 订单详情
     */
    public static <T> void getOrderDetail(Context context, String id, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        api.post(ApiService.ORDERDETAIL, YYApp.getInstance().getToken(), hashMap, true, callback);
    }


    /**
     * 订单详情
     */
    public static <T> void getNewOrderDetail(Context context, String id, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        api.post(ApiService.NEWORDERDETAIL, YYApp.getInstance().getToken(), hashMap, true, callback);
    }


    /**
     * 物流详情
     */
    public static <T> void getLogisticsDetail(Context context, String id, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderId", id);
        api.post(ApiService.LOGISTICSDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * YiYa商城
     */
    public static <T> void getYiYaShop(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.YIYA_SHOP, new HashMap<String, String>(), false, callback);
    }

    /**
     * 验证设置支付密码
     */
    public static <T> void getPayToken(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.post(ApiService.PAY_CHECKPAYTOKEN, YYApp.getInstance().getToken(), new HashMap<String, String>(), true, callback);
    }

    /**
     * 充值详情
     */
    public static <T> void getRechargeDeatail(Context context,int id,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        api.apiPost(ApiService.RECHARGEDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    public static <T> void getPayDeatail(Context context,int id,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id",id);
        api.apiPost(ApiService.PAYDETAIL, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 余额支付
     */
    public static <T> void getPayBalance(Context context, String payToken, String orderNo, String total, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("orderNo", orderNo);
        hashMap.put("total", total);
        hashMap.put("payToken", payToken);
        api.post(ApiService.PAY_BALANCEPAY, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 修改购买数量
     */
    public static <T> void setCarNumb(Context context, String productId, int nums, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("productId", productId);
        hashMap.put("nums", nums + "");
        api.apiPost(ApiService.CAR_SET_NUMS, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 移出购物车
     */
    public static <T> void removeCar(Context context, String[] strings, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        api.apiPost(ApiService.CAR_REMOVE, YYApp.getInstance().getToken(), strings, hashMap, true, callback);
    }

    /**
     * 用户签到
     */
    public static <T> void daySign(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();

        api.apiPost(ApiService.DAYSIGN, YYApp.getInstance().getToken(), new HashMap<String, Object>(), true, callback);
    }

    /**
     * 获得列表
     */
    public static <T> void getMessageList(Context context,int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page",page);
        api.apiPost(ApiService.MESSAGELIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获得物流列表
     */
    public static <T> void getMessageLogtisList(Context context,int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page",page);
        api.apiPost(ApiService.GETLOSGTIEMES, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获得资产列表
     */
    public static <T> void getMessageMyPriceList(Context context,int page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page",page);
        api.apiPost(ApiService.GETMINEASSETS, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获取客服联系方式
     */
    public static <T> void getService(Context context,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETSERVICE, YYApp.getInstance().getToken(), new HashMap(), false, callback);
    }

    /**
     * 获取用户资产
     */
    public static <T> void getUserAsset(Context context,boolean bshow,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.GETUSERASSET, YYApp.getInstance().getToken(), new HashMap(), bshow, callback);
    }

    /**
     * 标为已读
     */
    public static <T> void readMessage(Context context, String messageId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("messageId", messageId);
        api.apiPost(ApiService.READMESSAGE, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * String SHARECODE = HOST + "/share/shareInvite";//分享邀请码
     * String CHECKCODE = HOST + "/share/checkShareCode";//接受邀请码
     */
    public static <T> void shareInvite(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.SHARECODE, YYApp.getInstance().getToken(), new HashMap(), true, callback);
    }

    public static <T> void checkInvite(Context context, String share_code, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("share_code", share_code);
        api.apiPost(ApiService.CHECKCODE, YYApp.getInstance().getToken(), hashMap, true, callback);
    }


    /**
     * 消息删除
     */
    public static <T> void deleteMessage(Context context, String messageId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("messageId", messageId);
        api.apiPost(ApiService.DELETEMESSAGE, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 全部标位已读
     */
    public static <T> void readAllMessage(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.READALL_MESSAGE, YYApp.getInstance().getToken(), new HashMap<String, Object>(), true, callback);
    }

    /**
     * 全部消息删除
     */
    public static <T> void deleteAllMessage(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.DELEALL_MESSAGE, YYApp.getInstance().getToken(), new HashMap<String, Object>(), true, callback);
    }

    /**
     * 获取猜你喜欢
     */
    public static <T> void getGoodsLike(Context context, int page, boolean showload, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", page + "");
        api.apiPost(ApiService.GOODS_LIKE, YYApp.getInstance().getToken(), hashMap, showload, callback);
    }

    /**
     * 确认建团
     */
    public static <T> void getGroupCreate(Context context, int id, int productId, int nums, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("id", id);
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        api.apiPost(ApiService.GROUP_CREATE, YYApp.getInstance().getToken(), hashMap, true, callback);
    }

    /**
     * 预览订单(拼团)
     */
    public static <T> void getOrderPreviewGroupBuy(Context context, String groupUserId, String nums, String productId,Integer addressid, Boolean bShow,ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("groupUserId", groupUserId);
        hashMap.put("nums", nums);
        hashMap.put("productId", productId);
        if (addressid!=null)
            hashMap.put("addressId", addressid);
        api.apiPost(ApiService.ORDER_PREVIEWGROUPBUY, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    /**
     * 创建订单(拼团)
     */
    public static <T> void getOrderSubmitGroupBuy(Context context, String groupUserId, String addressId, String nums, String productId,HashMap delivery, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("groupUserId", groupUserId);
        hashMap.put("addressId", addressId);
        hashMap.put("nums", nums);
        hashMap.put("productId", productId);
//        api.apiPost(ApiService.ORDER_SUBMITGROUPBUY, YYApp.getInstance().getToken(), hashMap, true, callback);

        if (delivery == null || delivery.size() == 0)
            api.apiPost(ApiService.ORDER_SUBMITGROUPBUY, YYApp.getInstance().getToken(), hashMap, true, callback);
        else
            api.apiPost(ApiService.ORDER_SUBMITGROUPBUY, YYApp.getInstance().getToken(),
                    RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(delivery)), hashMap, true, callback);
    }

    /**
     * 参团详情页
     */
    public static <T> void getGroupJoin(Context context, String groupId, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("groupId", groupId);
        api.apiPost(ApiService.GROUP_JOIN, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 确认参团
     */
    public static <T> void getGroupComfirm(Context context, int productId, int groupId, int nums, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("groupId", groupId);
        hashMap.put("productId", productId);
        hashMap.put("nums", nums);
        api.apiPost(ApiService.GROUP_COMFIRM, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获取待成团列表
     */
    public static <T> void getGroupWaitGroup(Context context, String page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", page);
        api.apiPost(ApiService.GROUP_WAITGROUP, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 获取当前活动下所有拼团组
     */
    public static <T> void getGroupGroupList(Context context, String groupId, String page, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", page);
        hashMap.put("groupId", groupId);
        api.apiPost(ApiService.GROUP_GROUPLIST, YYApp.getInstance().getToken(), hashMap, false, callback);
    }

    /**
     * 拼团好货
     */
    public static <T> void getGroupList(Context context,String url, int page,Integer categoryId,boolean bShow, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("page", page);
        if (categoryId!=null){
            hashMap.put("categoryId",categoryId);
        }
        //
        api.apiPost(url, YYApp.getInstance().getToken(), hashMap, bShow, callback);
    }

    /**
     * 问题列表
     */
    public static <T> void getArticleGetarticlelist(Context context, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        api.apiPost(ApiService.ARTICLE_GETARTICLELIST, new HashMap<String, String>(), false, callback);
    }

    /**
     * 第三方登录
     */
    public static <T> void getAuthThdsignIn(Context context, String thdId, String type, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("thdId", thdId);
        hashMap.put("type", type);
        api.apiPost(ApiService.AUTH_THDSIGNIN, hashMap, true, callback);
    }

    /**
     * 第三方绑定
     */
    public static <T> void getAuthThdBind(Context context, String phone, String thdId, String nickname, String headImg, String type, String msgcode, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        hashMap.put("phone", phone);
        hashMap.put("nickname", nickname);
        hashMap.put("thdId", thdId);
        hashMap.put("headImg", headImg);
        hashMap.put("type", type);
        hashMap.put("msgcode", msgcode);
        api.apiPost(ApiService.AUTH_THDBIND, hashMap, true, callback);
    }

    /**
     * 删除全部收藏
     */
    public static <T> void delAllCollect(Context context,Integer categoryId,ApiCallback<T> callback){
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        if (categoryId != null)
            hashMap.put("categoryId", categoryId);
        api.apiPost(ApiService.COLLECT_DELALL, YYApp.getInstance().getToken(),hashMap, true, callback);
    }

    /**
     * 添加收藏商品
     */
    public static <T> void collectAddCollect(Context context, String[] strings, ApiCallback<T> callback) {
        ViseApi api = new ViseApi.Builder(context).build();
        HashMap hashMap = new HashMap();
        api.apiPost(ApiService.COLLECT_ADDCOLLECT, YYApp.getInstance().getToken(), strings, hashMap, true, callback);
    }

}

