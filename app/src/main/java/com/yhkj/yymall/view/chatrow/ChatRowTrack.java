package com.yhkj.yymall.view.chatrow;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.easeui.widget.chatrow.ChatRow;
import com.hyphenate.helpdesk.model.MessageHelper;
import com.hyphenate.helpdesk.model.VisitorTrack;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DailyDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatRowTrack extends ChatRow {

    ImageView mImageView;
    TextView mTextViewDes;
    TextView mTextViewprice;
    TextView mChatTextView;
    TextView mTrackTitle;

    public ChatRowTrack(Context context, Message message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == Message.Direct.RECEIVE ? R.layout.hd_row_received_message : R.layout.em_row_sent_track, this);
    }

    @Override
    protected void onFindViewById() {
        if (message.direct() == Message.Direct.SEND) {
            mTextViewDes = (TextView) findViewById(R.id.tv_description);
            mTextViewprice = (TextView) findViewById(R.id.tv_price);
            mImageView = (ImageView) findViewById(R.id.iv_picture);
            mChatTextView = (TextView) findViewById(R.id.tv_chatcontent);
            mTrackTitle = (TextView) findViewById(R.id.tv_title);
        }else{
            findViewById(R.id.hrrm_ll_goods).setVisibility(VISIBLE);
            mTextViewDes = (TextView) findViewById(R.id.tv_description);
            mTextViewprice = (TextView) findViewById(R.id.tv_price);
            mImageView = (ImageView) findViewById(R.id.iv_picture);
        }
    }

    @Override
    protected void onUpdateView() {
    }

    int mGoodsType;
    int mGoodsId;
    int mPanicBuyItemId;
    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.body();
        if (message.direct() == Message.Direct.RECEIVE) {
            //设置内容
            try{
                JSONObject articlesObject = (JSONObject)message.getJSONObjectAttribute("msgtype").get("news");
                JSONArray arrayDate = (JSONArray)articlesObject.get("articles");
                JSONObject entitry = (JSONObject)arrayDate.get(0);
                mGoodsType = Integer.parseInt(String.valueOf(entitry.get("goodsType")));
                String img = String.valueOf(entitry.get("image"));
                mGoodsId = Integer.parseInt(String.valueOf(entitry.get("id")));
                mPanicBuyItemId = Integer.parseInt(String.valueOf(entitry.get("panicBuyItemId")));
                String name = String.valueOf(entitry.get("name"));
                String price = String.valueOf(entitry.get("price"));
//                mTrackTitle.setText(name);
                mTextViewDes.setText(name);
                mTextViewprice.setText(price);
                if (!TextUtils.isEmpty(img)) {
                    Glide.with(context).load(img).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(com.hyphenate.helpdesk.R.drawable.hd_default_image).into(mImageView);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return;
        }
        VisitorTrack visitorTrack = MessageHelper.getVisitorTrack(message);
        if (visitorTrack == null) {
            return;
        }
        mTrackTitle.setText(visitorTrack.getTitle());
        mTextViewDes.setText(visitorTrack.getDesc());
        mTextViewprice.setText(visitorTrack.getPrice());
        String imageUrl = visitorTrack.getImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(com.hyphenate.helpdesk.R.drawable.hd_default_image).into(mImageView);
        }
    }

    @Override
    protected void onBubbleClick() {
        String id = "";
        String type = "";
        String panicBuyItemId = "";
        int t = 0,pid = 0;
        if (message.direct() == Message.Direct.SEND) {
            try {
                id = message.getStringAttribute("shopid");
                type = message.getStringAttribute("type");
                panicBuyItemId = message.getStringAttribute("panicBuyItemId");
                t = Integer.parseInt(type);
                pid = TextUtils.isEmpty(panicBuyItemId) ? 0 : Integer.parseInt(panicBuyItemId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            t = mGoodsType;
            id = String.valueOf(mGoodsId);
            pid = mPanicBuyItemId;
        }

        if (t == 2) {
            //租赁商品
            Intent intent = new Intent(getContext(), LeaseDetailActivity.class);
            intent.putExtra("id", id);
            getContext().startActivity(intent);
        }else if (t == 0){
            if (pid != 0){
                //限时抢购
                Intent intent = new Intent(getContext(), TimeKillDetailActivity.class);
                intent.putExtra("id", String.valueOf(pid));
                getContext().startActivity(intent);
            }else{
                //普通商品
                Intent intent = new Intent(getContext(), CommodityDetailsActivity.class);
                intent.putExtra("goodsId", id);
                getContext().startActivity(intent);
            }
        }else if (t == 1){
            //拼团商品
            Intent intent = new Intent(getContext(), GrouponDetailsActivity.class);
            intent.putExtra("goodsId", id);
            getContext().startActivity(intent);
        }else if (t == 3){
            //折扣
            Intent intent = new Intent(getContext(), DiscountDetailsActivity.class);
            intent.putExtra("goodsId", id);
            getContext().startActivity(intent);
        }else if (t == 4){
            //积分
            Intent intent = new Intent(getContext(), IntegralDetailActivity.class);
            intent.putExtra("id", id);
            getContext().startActivity(intent);
        }else if (t == 6){
            //日常活动
            Intent intent = new Intent(getContext(), DailyDetailsActivity.class);
            intent.putExtra("goodsId", id);
            getContext().startActivity(intent);
        }
    }
}
