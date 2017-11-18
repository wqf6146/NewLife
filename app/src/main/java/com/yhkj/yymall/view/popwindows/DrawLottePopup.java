package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.LotteryResBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

/**
 * 支付dialog
 */
public class DrawLottePopup extends BasePopupWindow {

    @Bind(R.id.vpd_img_shopimg)
    ImageView mImgShopImg;

    @Bind(R.id.vpd_img_shopimgbg)
    CircleImageView mImgShopImgBg;

    @Bind(R.id.vpd_img_close)
    ImageView mImgClose;

    @Bind(R.id.vpd_tv_content)
    TextView mTvContent;

    @Bind(R.id.vpd_tv_redraw)
    TextView mTvReDraw;

    @Bind(R.id.vpd_tv_title)
    TextView mTvTitle;

    private Context mContext;

    public DrawLottePopup(Activity context, LotteryResBean.DataBean.ResultBean dataBean,String errmsg) {
        super(context);
        mContext = context;
        init(dataBean,errmsg);
    }

    private void init(LotteryResBean.DataBean.ResultBean dataBean,String errmsg) {
        //type  类型 goods yy point empty
        if (dataBean == null){
            //没有积分
            mImgShopImg.setVisibility(GONE);
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(errmsg);
            mTvContent.setText("告诉你一个去领积分的地方");
            mTvReDraw.setText("        去领积分        ");
            mTvReDraw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDrawPopViewCallback!=null) {
                        dismissWithOutAnima();
                        mDrawPopViewCallback.doGetInteg();
                    }
                }
            });
            mImgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissWithOutAnima();
                }
            });
        }else {
//            Glide.with(mContext).load(dataBean.getInfo().getImg()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcpic)
//                    .into(mImgShopImg);
            Glide.with(mContext).load(dataBean.getInfo().getImg())
                    .asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcpic)
                    .into(new BitmapImageViewTarget(mImgShopImg) {
                @Override
                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    mImgShopImg.setImageDrawable(circularBitmapDrawable);
                    mImgShopImg.setImageBitmap(resource);
                }
            });
            mTvContent.setText(dataBean.getInfo().getName());
            mTvTitle.setVisibility(View.GONE);
            if (dataBean.getType().equals("goods")){
                mTvReDraw.setText("        去领奖        ");
                mTvReDraw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //领取商品
                        if (mDrawPopViewCallback!=null) {
                            dismissWithOutAnima();
                            mDrawPopViewCallback.doGetShop();
                        }
                    }
                });
                mImgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("取消确认");
                        builder.setMessage("确定要放弃领取该商品吗？");
                        builder.setPositiveButton("取消", null);
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismissWithOutAnima();
                            }
                        });
                        builder.show();
                    }
                });
            }else{
                mTvReDraw.setText("        再抽一次        ");
                mTvReDraw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //领取商品
                        if (mDrawPopViewCallback!=null) {
                            dismissWithOutAnima();
                            mDrawPopViewCallback.onecAgainst();
                        }
                    }
                });
                mImgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissWithOutAnima();
                    }
                });
            }

        }
    }

    @Override
    protected Animation initShowAnimation() {
//        AnimationSet set = new AnimationSet(false);
//        Animation shakeAnima = new RotateAnimation(0, 15, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        shakeAnima.setInterpolator(new CycleInterpolator(5));
//        shakeAnima.setDuration(400);
//        set.addAnimation(getDefaultAlphaAnimation());
//        set.addAnimation(shakeAnima);

        Animation animation = getTranslateAnimation(250 * 2, 0, 300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;

    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_popup_drawres);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    private DrawPopViewCallback mDrawPopViewCallback;

    public DrawLottePopup setDrawPopViewCallback(DrawPopViewCallback drawPopViewCallback) {
        this.mDrawPopViewCallback = drawPopViewCallback;
        return this;
    }

    public interface DrawPopViewCallback {
        //领取商品
        void doGetShop();
        //再来一次
        void onecAgainst();
        //领积分
        void doGetInteg();
    }

}
