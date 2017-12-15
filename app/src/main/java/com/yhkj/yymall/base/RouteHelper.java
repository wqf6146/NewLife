package com.yhkj.yymall.base;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DailyActActivity;
import com.yhkj.yymall.activity.DailyDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.GrouponListActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.IntegralShopListActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.MainActivity;
import com.yhkj.yymall.activity.OffPriceShopListActivity;
import com.yhkj.yymall.activity.OrderDetailActivity;
import com.yhkj.yymall.activity.SeckillingActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;
import com.yhkj.yymall.activity.WebActivity;

/**
 * Created by Administrator on 2017/12/15.
 */

public class RouteHelper {

    // type ==3 的时候 id - 拼团id
    // type ==4 的时候 id - 文章id
    public static void skip(Context context,String type,String listName,Integer goodsType,String goodsId,String paniclBuyItemId,String id,
                            String title,String href){
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        try{
            switch (Integer.parseInt(type)){
                case 1:
                    // 活动列表
                    if (listName.equals("dailyList")){
                        Intent dailyIntent = new Intent(context, DailyActActivity.class);
                        Intent[] intents = {i, dailyIntent};
                        context.startActivities(intents);
                    }else if (listName.equals("panicList")){
                        Intent seckIintent = new Intent(context, SeckillingActivity.class);
//							seckIintent.putExtra("panicBuyId",json.getString("panicBuyId"));
                        Intent[] intents = {i, seckIintent};
                        context.startActivities(intents);
                    }else if (listName.equals("groupList")){
                        Intent groupListIntent = new Intent(context, GrouponListActivity.class);
                        Intent[] intents = {i, groupListIntent};
                        context.startActivities(intents);
                    }else if (listName.equals("rentList")){
                        YYApp.getInstance().setIndexShow(1);
                        context.startActivity(i);
                    }else if (listName.equals("discountList")){
                        Intent offListIntent = new Intent(context, OffPriceShopListActivity.class);
                        Intent[] intents = {i, offListIntent};
                        context.startActivities(intents);
                    }else if (listName.equals("integralList")){
                        Intent integralListIntent = new Intent(context, IntegralShopListActivity.class);
                        Intent[] intents = {i, integralListIntent};
                        context.startActivities(intents);
                    }else if (listName.equals("offlineList")){
                        Intent offLineIntent = new Intent(context, WebActivity.class);
                        offLineIntent.putExtra(Constant.WEB_TAG.TAG, href);
                        offLineIntent.putExtra("title", TextUtils.isEmpty(title) ? "YiYa精选" : title);
                        Intent[] intents = {i, offLineIntent};
                        context.startActivities(intents);
                    }else{
                        context.startActivity(i);
                    }
                    break;
                case 2:
                    //商品详情
                    Intent shopIntent = null;
                    if (goodsType == 2) {
                        //租赁商品
                        shopIntent = new Intent(context, LeaseDetailActivity.class);
                        shopIntent.putExtra("id", goodsId);
                    }else if (goodsType == 0){
                        if (!TextUtils.isEmpty(paniclBuyItemId)){
                            //限时抢购
                            shopIntent = new Intent(context, TimeKillDetailActivity.class);
                            shopIntent.putExtra("id", paniclBuyItemId);
                        }else{
                            //普通商品
                            shopIntent = new Intent(context, CommodityDetailsActivity.class);
                            shopIntent.putExtra("goodsId", goodsId);
                        }
                    }else if (goodsType == 1){
                        //拼团商品
                        shopIntent = new Intent(context, GrouponDetailsActivity.class);
                        shopIntent.putExtra("goodsId", goodsId);
                    }else if (goodsType == 3){
                        //折扣
                        shopIntent = new Intent(context, DiscountDetailsActivity.class);
                        shopIntent.putExtra("goodsId", goodsId);
                    }else if (goodsType == 4){
                        //积分
                        shopIntent = new Intent(context, IntegralDetailActivity.class);
                        shopIntent.putExtra("id", goodsId);
                    }else if (goodsType == 6){
                        //日常活动
                        shopIntent = new Intent(context, DailyDetailsActivity.class);
                        shopIntent.putExtra("goodsId", goodsId);
                    }
                    Intent[] intents = {i, shopIntent};
                    context.startActivities(intents);
                    break;
                case 3:
                    //拼团成功
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("id", Integer.parseInt(id));
                    Intent[] groupintents = {i, intent};
                    context.startActivities(groupintents);
                    break;
                case 4:
                    //文章
                    if (TextUtils.isEmpty(id)){
                        //外链
                        Intent offLineIntent = new Intent(context, WebActivity.class);
                        offLineIntent.putExtra(Constant.WEB_TAG.TAG, href);
                        offLineIntent.putExtra("title", "YiYa精选");
                        Intent[] webIntents = {i, offLineIntent};
                        context.startActivities(webIntents);
                    }else{
                        //文章
                        //ApiService.SERVER_URL + childArray.get(groupPosition).get(childPosition).get("articleId")
                        Intent newMesIntent = new Intent(context, WebActivity.class);
                        newMesIntent.putExtra("id", id);
                        Intent[] artIntents = {i, newMesIntent};
                        context.startActivities(artIntents);
                    }
                    break;
                default:
                    context.startActivity(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
