package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.zxing.qrcode.encoder.QRCode;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.vise.xsnow.util.luban.Luban;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.ApplyAfterMallActivity;
import com.yhkj.yymall.bean.InviteCodeBean;
import com.yhkj.yymall.bean.LotteryResBean;
import com.yhkj.yymall.bean.MineBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

/**
 * 支付dialog
 */
public class QrcodePopup extends BasePopupWindow {

    @Bind(R.id.vpd_img_userimg)
    ImageView mImgUserImg;

    @Bind(R.id.vpq_img_hide)
    ImageView mImgClose;

    @Bind(R.id.imh_tv_username)
    TextView mTvUserName;

    @Bind(R.id.imh_img_mylevel)
    ImageView mImgMyLevels;

    @Bind(R.id.imh_tv_mylevel)
    TextView mTvLevels;

    @Bind(R.id.vpq_img_qrcode)
    ImageView mImgQrCode;

    @Bind(R.id.vpq_tv_share)
    TextView mTvShare;

    @Bind(R.id.vpq_tv_savepic)
    TextView mTvSavePic;

    @Bind(R.id.vpq_tv_codetip)
    TextView mTvCodeTip;

    @Bind(R.id.view_progress)
    ProgressBar mProgressView;

    @Bind(R.id.vpq_ll_container)
    LinearLayout mLlContainer;

    private Context mContext;

    public QrcodePopup(Activity context, MineBean.DataBean.InfoBean infoBean) {
        super(context);
        mContext = context;
        init(infoBean);
    }

    private void init(final MineBean.DataBean.InfoBean infoBean) {
        mTvUserName.setText(infoBean.getName());
        switch (infoBean.getLevel()) {
            case 0:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv0);
                break;
            case 1:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv1);
                break;
            case 2:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv2);
                break;
            case 3:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv3);
                break;
            case 4:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv4);
                break;
            case 5:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv5);
                break;
            case 6:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv6);
                break;
            case 7:
                mImgMyLevels.setImageResource(R.mipmap.ic_nor_mv7);
                break;
        }
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithOutAnima();
            }
        });
        mTvLevels.setText(String.format(mContext.getString(R.string.mylevels), String.valueOf(infoBean.getLevel())));
        if (infoBean.getHead_ico().equals("未设置")){
            mImgUserImg.setImageResource(R.mipmap.ic_nor_srcheadimg);
        }else{
            Glide.with(mContext).load(infoBean.getHead_ico()).asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(mImgUserImg) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgUserImg.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        YYMallApi.shareInvite(mContext, new YYMallApi.ApiResult<InviteCodeBean.DataBean>(mContext) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(InviteCodeBean.DataBean dataBean) {
                mCode = dataBean.getShare_code();
                mTvCodeTip.setText("邀请好友扫码下载YiYiYaYa客户端得" + dataBean.getNub() + "丫丫");
                buildQrCode(dataBean.getShare_code());
            }

        });
        mTvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCodePopCallback!=null && !TextUtils.isEmpty(mCode))
                    mCodePopCallback.doShare(mCode);
            }
        });
        mTvSavePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLlContainer.setDrawingCacheEnabled(true);
                mLlContainer.buildDrawingCache();  //启用DrawingCache并创建位图
                Bitmap bitmap = Bitmap.createBitmap(mLlContainer.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
                mLlContainer.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能

                CommonUtil.saveImageToGallery(mContext,bitmap);
                Toast.makeText(mContext,"保存图片成功",Toast.LENGTH_SHORT).show();
//                mImgQrCode.setImageBitmap(bitmap);
            }
        });
    }
    private String mCode;
    private void buildQrCode(String code) {
        Observable.just(code)
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String code){
                        return QRCodeEncoder.syncEncodeQRCode(ApiService.SHARE_CODE_URL + "#" + code, BGAQRCodeUtil.dp2px(mContext, 150));
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap codeImg) {
                        mProgressView.setVisibility(GONE);
                        mImgQrCode.setImageBitmap(codeImg);
                    }
                });
    }

    @Override
    protected Animation initShowAnimation() {
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
        View view = createPopupById(R.layout.view_popup_qrcode);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    private CodePopCallBack mCodePopCallback;

    public QrcodePopup setCodePopCallback(CodePopCallBack codePopCallback) {
        this.mCodePopCallback = codePopCallback;
        return this;
    }

    public CodePopCallBack getCodePopCallback() {
        return mCodePopCallback;
    }

    public interface CodePopCallBack {
        void doShare(String code);
    }
}
