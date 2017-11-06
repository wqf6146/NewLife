package com.yhkj.yymall.fragment;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.vise.xsnow.manager.AppManager;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.ShopBannerActivity;
import com.yhkj.yymall.base.Constant;

import java.io.File;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShopBannerDetailFragment extends BaseFragment {

    private int mType;
    private String mVideoUrl;
    private File mFile;
    private String mImgUrl;

    @Bind(R.id.fsi_rl_bg)
    RelativeLayout mRlBg;

    private JCVideoPlayerStandard mVideoView;
    private ImageView mImg;
    public static ShopBannerDetailFragment getInstance() {
        ShopBannerDetailFragment fragment = new ShopBannerDetailFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mType = bundle.getInt(Constant.BANNER.BANNER_TYPE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (mType == Constant.BANNER.VIDEO && mVideoView !=null){
            startOrStopVideo();
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mType == Constant.BANNER.VIDEO && mVideoView !=null){
            startOrStopVideo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVideoView!=null){
            mVideoView.destroyDrawingCache();
            mVideoView.releaseAllVideos();
        }
    }

    @Override
    protected int getLayoutID() {
        if (mType == Constant.BANNER.IMG || mType == Constant.BANNER.FILE)
            return R.layout.fragment_showbanner_img;
        return R.layout.fragment_showbanner_video;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        if (mType == Constant.BANNER.IMG || mType == Constant.BANNER.FILE){
            mImg = (ImageView)mRootView.findViewById(R.id.fsi_img);
            mImg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    WindowManager wm = (WindowManager) _mActivity
                            .getSystemService(Context.WINDOW_SERVICE);
                    Point outSize = new Point();
                    wm.getDefaultDisplay().getSize(outSize);
                    mImg.getLayoutParams().height = outSize.x;
                }
            });
        }else if(mType == Constant.BANNER.VIDEO){
            mVideoView = (JCVideoPlayerStandard)mRootView.findViewById(R.id.fsi_videoplayer);
        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivityWithNoAnim(ShopBannerActivity.class);
            }
        });
        if (mType == Constant.BANNER.IMG){
            mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppManager.getInstance().finishActivityWithNoAnim(ShopBannerActivity.class);
                }
            });
        }
    }

    @Override
    protected void initData() {

        if (mType == Constant.BANNER.IMG) {
            mImgUrl = getArguments().getString(Constant.BANNER.URL_IMG);

            Glide.with(_mActivity).load(mImgUrl).placeholder(R.mipmap.ic_nor_srcpic).into(mImg);
        }else if (mType == Constant.BANNER.FILE){
            mFile = (File) getArguments().getSerializable(Constant.BANNER.URL_FILE);
            Glide.with(_mActivity).load(mFile).into(mImg);
        } else if(mType == Constant.BANNER.VIDEO){
//            mVideoView.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
//                    , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
            mVideoUrl = getArguments().getString(Constant.BANNER.URL_VIDEO);
            mVideoView.setUp(mVideoUrl, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,"");
            Glide.with(_mActivity).load(mImgUrl).into(mVideoView.thumbImageView);
            mVideoView.startVideo();
        }
    }

    private void startOrStopVideo(){
        mVideoView.startButton.performClick();
    }
}


