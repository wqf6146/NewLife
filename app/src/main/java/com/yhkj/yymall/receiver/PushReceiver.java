package com.yhkj.yymall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.yhkj.yymall.activity.MessageActivity;
import com.yhkj.yymall.activity.MessageMinuteActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.OffPriceShopListActivity;
import com.yhkj.yymall.activity.OrderDetailActivity;
import com.yhkj.yymall.activity.SeckillingActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;
import com.yhkj.yymall.activity.WebActivity;
import com.yhkj.yymall.base.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
//			Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

			if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
				YYApp.getInstance().setRegistrationId(bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
			} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//				processCustomMessage(context, bundle);

			} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
				int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//				Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
				parsePush(context,bundle);
//				Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");

				//打开自定义的Activity
//				Intent i = new Intent(context, TestActivity.class);
//				i.putExtras(bundle);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//				context.startActivity(i);

			} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//				Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

			} else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//				Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
			} else {
//				Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		} catch (Exception e){

		}

	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//					Logger.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
//					Logger.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	//解析推送
	private void parsePush(Context context,Bundle bundle){
		String jsonData = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if (TextUtils.isEmpty(jsonData) || jsonData.equals("{}")){
			return;
		}else {
			try{
				JSONObject json = new JSONObject(jsonData);
				String type = json.getString("type");
				Intent i = new Intent(context, MainActivity.class);
				i.putExtras(bundle);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				switch (Integer.parseInt(type)){
					case 1:
						// 活动列表
						String listName = json.getString("name");

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
							offLineIntent.putExtra(Constant.WEB_TAG.TAG, json.getString("href"));
							offLineIntent.putExtra("title", json.getString("title"));
							Intent[] intents = {i, offLineIntent};
							context.startActivities(intents);
						}
						break;
					case 2:
						//商品详情
						String goodsId = json.getString("goodsId");
						Intent shopIntent = null;
						int goodsType = Integer.parseInt(json.getString("goodsType"));
						if (goodsType == 2) {
							//租赁商品
							shopIntent = new Intent(context, LeaseDetailActivity.class);
							shopIntent.putExtra("id", goodsId);
						}else if (goodsType == 0){
							String paniclBuyItemId = null;
							try{
								paniclBuyItemId = json.getString("paniclBuyItemId");
							}catch (Exception e){

							}
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
						String id = json.getString("id");
						Intent intent = new Intent(context, OrderDetailActivity.class);
						intent.putExtra("id", Integer.parseInt(id));
						Intent[] groupintents = {i, intent};
						context.startActivities(groupintents);
						break;
					case 4:
						//文章
						String mesId = null;
						try{
							mesId = json.getString("id");
						}catch (Exception e){

						}
						if (TextUtils.isEmpty(mesId)){
							//外链
							Intent offLineIntent = new Intent(context, WebActivity.class);
							offLineIntent.putExtra(Constant.WEB_TAG.TAG, json.getString("href"));
							offLineIntent.putExtra("title", json.getString("title"));
							Intent[] webIntents = {i, offLineIntent};
							context.startActivities(webIntents);
						}else{
							//站内信
//							Intent newMesIntent = new Intent(context, NewMessageActivity.class);
//							Intent mesIntent = new Intent(context, MessageActivity.class);
//							Intent mesIntent = new Intent(context, MessageMinuteActivity.class);
//							offLineIntent.putExtra(Constant.WEB_TAG.TAG, json.getString("href"));
//							offLineIntent.putExtra("title", json.getString("title"));
//							Intent[] intents = {i, offLineIntent};
//							context.startActivities(intents);
						}


						break;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (YYApp.getInstance().isForeground()) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
		}
	}
}
