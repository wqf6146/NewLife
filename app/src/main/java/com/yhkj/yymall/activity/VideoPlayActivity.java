package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.videogo.openapi.EZConstants;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.RotateViewUtil;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.NormalFragmentAdapter;
import com.yhkj.yymall.bean.GoodsLikeBean;
import com.yhkj.yymall.bean.VideoIoBean;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.fragment.SpitVideoFragment;
import com.yhkj.yymall.fragment.VideoFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import com.yhkj.yymall.view.popwindows.VideoControlPopupView;

import android.support.v4.view.ViewPager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by Administrator on 2017/12/16.
 */

public class VideoPlayActivity extends BaseActivity implements SpitVideoFragment.OnSpitVideoSelect {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.av_viewpager_mult)
    ViewPager mViewPagerMult;

    @Bind(R.id.av_viewpager_single)
    ViewPager mViewPagerSingle;

    @Bind(R.id.av_rl_videoplay)
    RelativeLayout mRlVideoPlay;

//    @Bind(R.id.av_tv_places)
//    TextView mTvPlaces;

//    @Bind(R.id.av_img_fullscreen)
//    ImageView mImgFullScreen;

//    @Bind(R.id.av_rl_place)
//    RelativeLayout mRlPlace;

    @Bind(R.id.av_img_back)
    ImageView mImgBack;

    @Bind(R.id.av_tv_title)
    TextView mTvTitle;

    @Bind(R.id.av_rl_toolbar)
    RelativeLayout mRlToolbar;

    @Bind(R.id.av_rl_loading)
    RelativeLayout mRlLoading;

    @Bind(R.id.av_rl_fullcontrolbg)
    RelativeLayout mRlFullControlBg;

    @Bind(R.id.av_ll_full_normalcontrol)
    LinearLayout mLlFullNormalControl;

    @Bind(R.id.av_ll_full_control)
    LinearLayout mLlFullControl;

    @Bind(R.id.av_img_fullstart)
    ImageView mImgFullStart;

    @Bind(R.id.av_img_fullvoice)
    ImageView mImgFullVoice;

    @Bind(R.id.av_img_full_split)
    ImageView mImgFullSplit;

    @Bind(R.id.av_img_fullcontrol)
    ImageView mImgFullControl;

    @Bind(R.id.av_img_fullvideo)
    ImageView mImgFullVideo;

    @Bind(R.id.av_img_fulltakephoto)
    ImageView mImgFullTakePhoto;

    @Bind(R.id.av_tv_full_qa)
    TextView mTvFullQa;

    @Bind(R.id.av_img_full_fullscreen)
    ImageView mImgFullFullScreen;

    @Bind(R.id.av_img_fullcontrolplay)
    ImageView mImgFullControlPlay;

    @Bind(R.id.av_img_fullcontrolvoice)
    ImageView mImgFullControlVoice;

    @Bind(R.id.av_ll_fulltopclick)
    LinearLayout mLlFullTopClick;

    @Bind(R.id.av_ll_fullleftclick)
    LinearLayout mLlFullLeftClick;

    @Bind(R.id.av_ll_fullrightclick)
    LinearLayout mLlFullRightClick;

    @Bind(R.id.av_ll_fullbottomclick)
    LinearLayout mLlFullBottomClick;

    @Bind(R.id.av_img_fulltopclick)
    ImageView mImgFullTopclick;

    @Bind(R.id.av_img_fullleftclick)
    ImageView mImgFullLeftclick;

    @Bind(R.id.av_img_fullrightclick)
    ImageView mImgFullRightclick;

    @Bind(R.id.av_img_fullbottomclick)
    ImageView mImgFullBottomclick;

    @Bind(R.id.av_img_fullcontrolclose)
    ImageView mImgFullControlClose;

    @Bind(R.id.av_tv_waittime)
    TextView mTvWaittime;

    private ViewGroup.LayoutParams mVCLayouyParams;

    private MyOrientationDetector mOrientationDetector;

    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;

    private boolean m4BoxMode = false; //四分屏

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mOrientation = newConfig.orientation;
        if (mVideoFragments == null) return;
        if (mControlPopupView!=null && mControlPopupView.isShowing())
            mControlPopupView.dismiss();
        updateOperatorUI();
        setSurfaceSize();
    }
    private void updateOrientation() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private void setSurfaceSize() {
        if (m4BoxMode == false){
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            for (int i=0; i<mVideoFragments.length;i++)
                mVideoFragments[i].setSurfaceSize(dm.widthPixels);
        }else{
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            for (int i=0; i<mSpitFragments.length;i++)
                mSpitFragments[i].setSurfaceSize(dm.widthPixels/2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrientationDetector!=null)
            mOrientationDetector.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOrientationDetector!=null)
            mOrientationDetector.disable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mHandler!=null){
            mHandler.removeMessages(1);
            mHandler.removeMessages(0);
        }
        if (mFullScreenHandler != null) {
            mFullScreenHandler.removeMessages(1);
        }
    }

    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            // 显示状态栏
            fullScreen(false);
            setStatusViewVisiable(true);
            mRlFullControlBg.setVisibility(VISIBLE);
            mRlToolbar.setVisibility(VISIBLE);
            mLlFullNormalControl.setVisibility(GONE);
            mLlFullControl.setVisibility(GONE);
//            if (!m4BoxMode)
//                mRlPlace.setVisibility(VISIBLE);
            mRlVideoPlay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRlVideoPlay.setLayoutParams(mVCLayouyParams);
                }
            },200);
        } else {
            // 隐藏状态栏
            fullScreen(true);
            setStatusViewVisiable(false);
            mRlFullControlBg.setVisibility(VISIBLE);
            mRlToolbar.setVisibility(VISIBLE);
            mLlFullNormalControl.setVisibility(VISIBLE);
            mLlFullControl.setVisibility(GONE);

//            mRlPlace.setVisibility(GONE);
//            if (!m4BoxMode)
//
            mRlVideoPlay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    realPlayPlayRlLp.gravity = Gravity.CENTER;
                    mRlVideoPlay.setLayoutParams(realPlayPlayRlLp);
                }
            },200);
        }
    }
    private void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);
    }

    @Override
    protected void initView() {
//        setToolBarColor(getResources().getColor(R.color.theme_bule));
//        setTvTitleText(getIntent().getStringExtra("title"));
        setStatusColor(getResources().getColor(R.color.halfblackbar));
        mRecycleView.setLayoutManager(new GridLayoutManager(this,2));
        mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(this,1)));
        mRefreshView.setEnableRefresh(false);
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPage++;
                getGoodsData(true);
            }
        });
    }
    private int m16to9Height = 0;
    @Override
    protected void bindEvent() {
        mRlVideoPlay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (m16to9Height == 0) {
                    m16to9Height = (int) Math.round(mRlVideoPlay.getWidth() / 1.77);
                    mRlVideoPlay.getLayoutParams().height = m16to9Height;
                }
            }
        });
//        mImgFullScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updateOrientation();
//            }
//        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrientation != Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    return;
                }
                AppManager.getInstance().finishActivity(VideoPlayActivity.this);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    private List<VideoListBean.DataBean.ListBean> mListData;
    private String mToken,mTitle;

    private int mCurVideoId;

    @Override
    protected void initData() {
        mListData = getIntent().getParcelableArrayListExtra("list");
        mTitle = mListData.get(getIntent().getIntExtra("pos",0)).getTitle();
        mTvTitle.setText(mTitle);
        mToken = getIntent().getStringExtra("token");
        mOrientationDetector = new MyOrientationDetector(this);
        mRecordRotateViewUtil = new RotateViewUtil();
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        mVCLayouyParams = mRlVideoPlay.getLayoutParams();
        getGoodsData(null);
        mControlView = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.view_videoandcontrol,mRecycleView,false);
        bindControlView(mControlView);
        buildSingleViewPager();
    }
    private VideoIoBean.DataBean mVideoIoBean;
    private boolean mCanControl = false;
    private void getIoPageData(final int id){
        YYMallApi.getVideoIoInfo(this, id, false,new ApiCallback<VideoIoBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(VideoIoBean.DataBean dataBean) {
                if (mCurVideoId == id){
                    mVideoIoBean = dataBean;
                    mCanControl = dataBean.getHandle() == 0 ? false : true;
                    setControlBtnState();
                    if (dataBean.getImg_valid() == 0){
                        VideoFragment fragment = getInTopVideo();
                        if (fragment!=null)
                            fragment.uploadCurFrame();
                    }
                }
            }

            @Override
            public void onError(ApiException e){
                super.onError(e);
                showToast(e.getMessage());
            }
        });
    }

    private void setControlBtnState() {
        mRlControl.setEnabled(true);
        if (mVideoIoBean.getRestHandleSecond() > 0 ) {
//            mTvControl.setTextColor(getResources().getColor(R.color.redfont));
            mImgControl.setImageResource(R.mipmap.ic_nor_controling);
            mImgFullControl.setImageResource(R.mipmap.ic_nor_fullcontrol);
            mTvControl.setVisibility(View.INVISIBLE);
            mTvControl.setTag(true);//表示控制中
            mHandler.sendMessage(buildMessage(false,mVideoIoBean.getRestHandleSecond())); //没打开控制窗口前的定时
        }else if (mVideoIoBean.getNextHandleSecond() > 0){
            mHandler.removeMessages(1);
            mHandler.removeMessages(0);
            mRankSec = mVideoIoBean.getNextHandleSecond();
            mHandler.sendMessage(buildMessage(true,0));
            mTvControl.setTag(false);//表示排队中
        }
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getGoodsData(null);
    }

    private VideoFragment[] mVideoFragments;
    private int mCurPage = 1;
    private CommonAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapperAdapter;
    private View mControlView;
    private void getGoodsData(final Boolean bLoadmore) {
        YYMallApi.getGoodsLike(this, bLoadmore == null ? 1 : mCurPage, false, new ApiCallback<GoodsLikeBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                if (bLoadmore != null) {
                    mRefreshView.finishLoadmore();
                    mCurPage--;
                }
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GoodsLikeBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (bLoadmore != null && mAdapter !=null) {
                    mRefreshView.finishLoadmore();
                    if (dataBean!=null && dataBean.getList() != null && dataBean.getList().size() > 0){
                        int start = mAdapter.getItemCount();
                        mAdapter.addDatas(dataBean.getList());
                        mWrapperAdapter.notifyItemRangeInserted(start + 1,mAdapter.getItemCount() + 1);
                    }else{
                        mRefreshView.setLoadmoreFinished(true);
                    }
                    return;
                }
                if (mAdapter == null){
                    mAdapter = new CommonAdapter<GoodsLikeBean.DataBean.ListBean>(VideoPlayActivity.this,R.layout.item_shop,dataBean.getList()) {
                        @Override
                        protected void convert(ViewHolder holder, final GoodsLikeBean.DataBean.ListBean bean, int position) {
//                            holder.mImgTagShop.setVisibility(View.GONE);
                            initGoodsUi(holder,bean,position);
                        }
                    };

                    mWrapperAdapter = new HeaderAndFooterWrapper(mAdapter);
                    mWrapperAdapter.addHeaderView(mControlView);
                    mRecycleView.setAdapter(mWrapperAdapter);
                }
                mRlLoading.setVisibility(GONE);
            }
        });
    }


    private NormalFragmentAdapter mSpitAdapter;
    private SpitVideoFragment[] mSpitFragments;

    private void removeSpitPlayer(){
        if (mSpitFragments==null) return;
        for (int i=0; i<mSpitFragments.length; i++){
            mSpitFragments[i].releasePlay();
        }
        mSpitFragments = null;
    }

    private void buildMultViewPager(){
        int tag = 4;
        int pagerSize = (int)Math.ceil(mListData.size() / (tag+0.0f));
        mSpitFragments = new SpitVideoFragment[pagerSize];
        for (int i=0; i < pagerSize; i++){
            mSpitFragments[i] = SpitVideoFragment.getInstance();
            int type;
            boolean autoPlay = false;
            if (i == 0){
                autoPlay = true;
            }
            if (tag == 4)
                type = 0;
            else
                type = 1;
            // 4*i+1     4*(i+1)
            int left = tag * (i);
            int right = tag * (i+1) - 1;
            int max = right > mListData.size()-1 ? mListData.size()-1 : right;
            ArrayList<VideoListBean.DataBean.ListBean> deviceList = new ArrayList<>();
            for ( int j=left; j<= max ;j++){
                deviceList.add(mListData.get(j));
            }
            mSpitFragments[i].setData(type,autoPlay,deviceList);
            mSpitFragments[i].setOnSpitVideoSelect(this);
        }
        mViewPagerMult.setOffscreenPageLimit(mSpitFragments.length);
        mSpitAdapter = new NormalFragmentAdapter(getSupportFragmentManager(),mSpitFragments);
        mViewPagerMult.setAdapter(mSpitAdapter);
        mSpitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onVideoSelect(VideoListBean.DataBean.ListBean deviceInfo, int pos) {
        m4BoxMode = false;
        removeSpitPlayer();
        mRlVideoPlay.removeView(mViewPagerMult);
        mRlVideoPlay.addView(mViewPagerSingle,0);
        mImgMultScreen.setImageResource(R.mipmap.ic_nor_multscreen);
        mImgFullSplit.setImageResource(R.mipmap.ic_nor_full_split2);
        int i=0;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        for (;i<mVideoFragments.length;i++){
            if (mVideoFragments[i].getDataBean().getDeviceSerial().equals(deviceInfo.getDeviceSerial())){
                mViewPagerSingle.setCurrentItem(i);
                mVideoFragments[i].onSupportVisible();
                mVideoFragments[i].setSurfaceSize(m4BoxMode == false ? dm.widthPixels : dm.widthPixels/2);
            }else {
                mVideoFragments[i].setSurfaceSize(m4BoxMode == false ? dm.widthPixels : dm.widthPixels/2);
            }
        }
        mRlFullControlBg.setVisibility(VISIBLE);
    }

    private int mBlueColor = Color.argb(255,00,124,209);
    private Animation mToolBarInAnimation,mToolBarOutAnimation;
    private void toolBarAnimation(boolean show){
        if (!show){
            if (mToolBarOutAnimation==null){
                mToolBarOutAnimation = AnimationUtils.loadAnimation(VideoPlayActivity.this, R.anim.fade_out);
                mToolBarOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRlFullControlBg.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            mRlFullControlBg.startAnimation(mToolBarOutAnimation);
        }else{
            if (mToolBarInAnimation==null){
                mToolBarInAnimation = AnimationUtils.loadAnimation(VideoPlayActivity.this, R.anim.fade_in);
                mToolBarInAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mRlFullControlBg.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            mRlFullControlBg.startAnimation(mToolBarInAnimation);
        }
    }

    private Boolean mNeedUpdate = null;

    private void buildSingleViewPager() {
        mVideoFragments = new VideoFragment[mListData.size()];
        for (int i=0; i<mListData.size();i++){
            mVideoFragments[i] = VideoFragment.getInstance(mToken,i,
                    i == getIntent().getIntExtra("pos",-1),
                    mListData.get(i)).setVideoParent(new OnVideoSelect() {
                @Override
                public void onVideoClick() {
                    toolBarAnimation(mRlFullControlBg.getVisibility() != VISIBLE);
                }

                @Override
                public void onVideoSelect(VideoListBean.DataBean.ListBean bean) {
//                    mTvPlaces.setText(bean.getTitle());
                    if (mOrientation != Configuration.ORIENTATION_PORTRAIT){
                        if(mLlFullControl.getVisibility() == VISIBLE){
                            mLlFullControl.setVisibility(GONE);
                            mLlFullNormalControl.setVisibility(VISIBLE);
                        }
                    }
                    mTvTitle.setText(bean.getTitle());
                    setControlBtnEnable(false);
                    mRlControl.setEnabled(false);
                    mImgControl.setImageResource(R.mipmap.ic_nor_control);
                    mImgFullControl.setImageResource(R.mipmap.ic_nor_full_control);
                    mImgControl.setVisibility(VISIBLE);
                    mTvControl.setVisibility(View.INVISIBLE);
                    mTvControl.setText("控制");
                    mTvControl.setTag(null);
                    mHandler.removeMessages(0);
                    mHandler.removeMessages(1);
                    mFullScreenHandler.removeMessages(1);
                    mCurVideoId = bean.getId();
                }

                @Override
                public void onVideoPlayState(int state) {
                    if (state == EZUIPlayer.STATUS_PLAY){
                        mImgStartVideo.setImageResource(R.mipmap.ic_nor_stopvideo);
                        mImgFullStart.setImageResource(R.mipmap.ic_nor_full_stop);
                        mImgFullControlPlay.setImageResource(R.mipmap.ic_nor_full_stop);
                        getIoPageData(mCurVideoId);
                        setControlBtnEnable(true);
                    }else{
                        mImgStartVideo.setImageResource(R.mipmap.ic_nor_startvideo);
                        mImgFullStart.setImageResource(R.mipmap.ic_nor_full_start);
                        mImgFullControlPlay.setImageResource(R.mipmap.ic_nor_full_start);
                    }
                }

                @Override
                public void onVideoVoiceControl(boolean bOpen) {
                    if (bOpen){
                        mImgVoiceControl.setImageResource(R.mipmap.ic_nor_voiceopen);
                        mImgFullVoice.setImageResource(R.mipmap.ic_nor_full_voice);
                        mImgFullControlVoice.setImageResource(R.mipmap.ic_nor_full_voice);
                    }else{
                        mImgVoiceControl.setImageResource(R.mipmap.ic_nor_voiceclose);
                        mImgFullVoice.setImageResource(R.mipmap.ic_nor_full_voiceclose);
                        mImgFullControlVoice.setImageResource(R.mipmap.ic_nor_full_voiceclose);
                    }
                }

                @Override
                public void onVideoRecordState(boolean bStart) {
                    if (bStart){
                        mRecordRotateViewUtil.applyRotation(mFlRecordContainer, mImgRecordStart,
                                mImgRecordStop, 0, 90);
                        mImgFullVideo.setImageResource(R.mipmap.ic_nor_full_stopplay);
                    }else{
                        mRecordRotateViewUtil.applyRotation(mFlRecordContainer, mImgRecordStop,
                                mImgRecordStart, 0, 90);
                        mImgFullVideo.setImageResource(R.mipmap.ic_nor_full_record);
                    }
                }
            });
        }
        NormalFragmentAdapter fragmentAdapter = new NormalFragmentAdapter(getSupportFragmentManager(),mVideoFragments);
        mViewPagerSingle.setAdapter(fragmentAdapter);
        mViewPagerSingle.setOffscreenPageLimit(mVideoFragments.length);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPagerSingle.setCurrentItem(getIntent().getIntExtra("pos",0),false);
            }
        },100);
    }

    private void setControlBtnEnable(boolean enable){
        mRlVideoPlay.setEnabled(enable);
        mRLVoiceControl.setEnabled(enable);
        mRlVideoQa.setEnabled(enable);
        mRlTakePhoto.setEnabled(enable);
        mRlRecord.setEnabled(enable);
        mImgFullControl.setEnabled(enable);
    }

    @Override
    public void onBackPressedSupport() {

        if (mOrientation != Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
//        if (mLocalInfo!=null)
//            mLocalInfo.setSoundOpen(false);

//        if (mIsRecording) {
//            stopRealPlayRecord();
//        }

        super.onBackPressedSupport();
    }

    private View mRlFullScreen,mRlStartVideo,mRlMultScreen,mRlVideoQa,mRLVoiceControl,mRlTakePhoto,mRlRecord,mRlControl,mFlRecordContainer;
    private ImageView mImgMultScreen,mImgStartVideo,mImgVoiceControl,mImgRecordStart,mImgRecordStop,mImgControl;
    private TextView mTvVideoQa,mTvControl;
    private RotateViewUtil mRecordRotateViewUtil;
    private void bindControlView(final View view) {

        mRlRecord = view.findViewById(R.id.vv_rl_record);
        mFlRecordContainer = view.findViewById(R.id.vv_fl_videocontainer);
        mImgRecordStart = (ImageView)mRlRecord.findViewById(R.id.vv_img_videostart);
        mImgRecordStop = (ImageView)mRlRecord.findViewById(R.id.vv_img_videostop);
        mRlRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m4BoxMode){
                    showToast("请选择具体设备");
                    return;
                }

                VideoFragment fragment = getInTopVideo();
                if (fragment!=null){
                    fragment.onRecordBtnClick();
                }
            }
        });

        mRlControl = view.findViewById(R.id.vv_rl_control);
        mTvControl = (TextView) mRlControl.findViewById(R.id.vv_tv_control);
        mImgControl = (ImageView)mRlControl.findViewById(R.id.vv_img_control);
        mRlControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImgControl.getVisibility() == VISIBLE)
                    tryControl(false);
            }
        });

        mRlTakePhoto = view.findViewById(R.id.vv_rl_takephoto);
        mRlTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m4BoxMode){
                    showToast("请选择具体设备");
                    return;
                }

                VideoFragment fragment = getInTopVideo();
                if (fragment!=null){
                    fragment.onCapturePicBtnClick();
                }
            }
        });

        mRLVoiceControl = view.findViewById(R.id.vv_rl_voicecontrol);
        mImgVoiceControl = (ImageView)mRLVoiceControl.findViewById(R.id.vv_img_voicecontrol);
        mRLVoiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doVoiceControl();
            }
        });

        mRlMultScreen = view.findViewById(R.id.vv_rl_multscreen);
        mImgMultScreen = (ImageView)mRlMultScreen.findViewById(R.id.vv_img_multscreen);
        mRlMultScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMultScreen();
            }
        });

        mRlVideoQa = view.findViewById(R.id.vv_rl_videoqa);
        mTvVideoQa = (TextView) mRlVideoQa.findViewById(R.id.vv_tv_videoqa);
        mRlVideoQa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m4BoxMode){
                    showToast("请选择具体设备");
                    return;
                }
                final VideoFragment fragment = getInTopVideo();
                if (!fragment.isVideoNormal())
                    return;
                if (fragment.getIsRecord()){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("选择码流会终止录像，是否选择码流？");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.stopRealPlayRecord();
                            dialog.dismiss();
                            openQaWindows();
                        }
                    });
                    builder.show();
                    return;
                }
                openQaWindows();

            }
        });

        mRlFullScreen = view.findViewById(R.id.vv_rl_fullscreen);
        mRlFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrientation();
            }
        });
        mRlStartVideo = view.findViewById(R.id.vv_rl_startvideo);
        mImgStartVideo = (ImageView)mRlStartVideo.findViewById(R.id.vv_img_startvideo);
        mRlStartVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartPlar();
            }
        });

        mImgFullFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrientation();
            }
        });

        mImgFullTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m4BoxMode){
                    showToast("请选择具体设备");
                    return;
                }

                VideoFragment fragment = getInTopVideo();
                if (fragment!=null){
                    fragment.onCapturePicBtnClick();
                }
            }
        });

        mImgFullVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m4BoxMode){
                    showToast("请选择具体设备");
                    return;
                }

                VideoFragment fragment = getInTopVideo();
                if (fragment!=null){
                    fragment.onRecordBtnClick();
                }
            }
        });

        mImgFullSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMultScreen();
            }
        });

//        mImgFullControlSplit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doMultScreen();
//            }
//        });

        mImgFullVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doVoiceControl();
            }
        });

        mImgFullControlVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doVoiceControl();
            }
        });

        mImgFullStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartPlar();
            }
        });
        mImgFullControlPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartPlar();
            }
        });
        mTvFullQa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m4BoxMode){
                    showToast("请选择具体设备");
                    return;
                }
                final VideoFragment fragment = getInTopVideo();
                if (!fragment.isVideoNormal())
                    return;
                if (fragment.getIsRecord()){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("选择码流会终止录像，是否选择码流？");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.stopRealPlayRecord();
                            dialog.dismiss();
                            openQaWindows();
                        }
                    });
                    builder.show();
                    return;
                }
                openQaWindows();
            }
        });

        mImgFullControlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlFullNormalControl.setVisibility(VISIBLE);
                mLlFullControl.setVisibility(GONE);
            }
        });

        mImgFullControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mLlFullControl.setVisibility(VISIBLE);
//                mLlFullNormalControl.setVisibility(GONE);
                tryControl(true);
            }
        });


        mLlFullTopClick.setOnTouchListener(mOnTouchListener);
        mLlFullLeftClick.setOnTouchListener(mOnTouchListener);
        mLlFullRightClick.setOnTouchListener(mOnTouchListener);
        mLlFullBottomClick.setOnTouchListener(mOnTouchListener);
    }

    Handler mFullScreenHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mLlFullControl.getVisibility() == INVISIBLE) return false;
            if (msg.arg1 > 0){
                mTvWaittime.setText("剩余控制 " + CommonUtil.secToMills(msg.arg1 - 1));
                Message message = new Message();
                message.what = 1;
                message.arg1 = msg.arg1 - 1;
                mFullScreenHandler.sendMessageDelayed(message,1000l);
            }else{
                //超过控制时间
                showToast("控制时间已结束，请重新申请。");
                mTvControl.setTag(null);
                mImgControl.setImageResource(R.mipmap.ic_nor_control);
                mImgFullControl.setImageResource(R.mipmap.ic_nor_full_control);
                if (mLlFullControl.getVisibility() == VISIBLE){
                    mLlFullControl.setVisibility(GONE);
                    mLlFullNormalControl.setVisibility(VISIBLE);
                }
            }
            return false;
        }
    });

    private void tryControl(final boolean bFullScreen) {
        if (!mCanControl){
            showToast("此设备暂不可控制");
            return;
        }

        if (m4BoxMode){
            showToast("请选择具体设备");
            return;
        }
//                if (canControlVideo()){
//                    openVideoControlWindow(mVideoIoBean.getRestHandleSecond());
//                    return;
//                }
        Boolean res = mTvControl.getTag() == null ? null : (Boolean) mTvControl.getTag();
        if (res!=null){
            //控制中
            YYMallApi.getVideoIoInfo(VideoPlayActivity.this, mCurVideoId, true,new ApiCallback<VideoIoBean.DataBean>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(VideoIoBean.DataBean dataBean) {
                    handlerControlStatue(dataBean,bFullScreen);
                }

                @Override
                public void onError(ApiException e) {
                    showToast(e.getMessage());
                }
            });
            return;
        }

        YYMallApi.applyVideoControl(VideoPlayActivity.this, mCurVideoId, new ApiCallback<VideoIoBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(VideoIoBean.DataBean dataBean) {
                handlerControlStatue(dataBean,bFullScreen);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }
        });
    }

    private boolean bCanControl = true;
    private void handlerControlStatue(VideoIoBean.DataBean dataBean,boolean bFullScreen){
        mVideoIoBean = dataBean;
        if (dataBean.getRestHandleSecond() > 0){
            //立即控制
            mImgControl.setImageResource(R.mipmap.ic_nor_controling);
            mImgFullControl.setImageResource(R.mipmap.ic_nor_fullcontrol);
            mTvControl.setTag(true);

            if (bFullScreen){
                mLlFullControl.setVisibility(VISIBLE);
                mLlFullNormalControl.setVisibility(GONE);
                bCanControl = true;
//                mLlFullTopClick.setEnabled(true);
//                mLlFullLeftClick.setEnabled(true);
//                mLlFullRightClick.setEnabled(true);
//                mLlFullBottomClick.setEnabled(true);
                int time = dataBean.getRestHandleSecond()-1;
                mTvWaittime.setText("剩余控制 " + CommonUtil.secToMills(time));
                mFullScreenHandler.removeMessages(1);
                Message message = new Message();
                message.what=1;
                message.arg1 = time-1;
                mFullScreenHandler.sendMessage(message);
            }else {
                mHandler.removeMessages(0);
                mHandler.removeMessages(1);
                openVideoControlWindow(dataBean.getRestHandleSecond());
            }
        }else{
            //排队  true排队 false开始
            mHandler.removeMessages(1);
            mHandler.removeMessages(0);
            mTvControl.setTag(false);
            bCanControl = false;
//            mLlFullTopClick.setEnabled(false);
//            mLlFullLeftClick.setEnabled(false);
//            mLlFullRightClick.setEnabled(false);
//            mLlFullBottomClick.setEnabled(false);
            mRankSec = dataBean.getNextHandleSecond();
            if (bFullScreen){
                mLlFullControl.setVisibility(VISIBLE);
                mLlFullNormalControl.setVisibility(GONE);
            }
            String[] secArr = CommonUtil.secToMillsArr(mRankSec);
            if (secArr !=null && secArr.length  >1) {
                int people = mVideoIoBean.getNum() > 0 ? mVideoIoBean.getNum() : 1;
                if (secArr[0].equals("00"))
                    showToast("您需等待"+ people + "人，预计 " + secArr[1] + "秒。");
                else
                    showToast("您需等待 " + people +"人，预计 "+ secArr[0] + "分钟。");
            }else{
                showToast("处于队列中，请等待");
            }

            mHandler.sendMessage(buildMessage(true,0));
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            VideoFragment fragment = getInTopVideo();
            if (fragment == null)
                return false;
            if (!bCanControl)
                return true;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
                        case R.id.av_ll_fulltopclick:
                            mImgFullTopclick.setImageResource(R.mipmap.ic_nor_full_clicktop);
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_UP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.av_ll_fullleftclick:
                            mImgFullLeftclick.setImageResource(R.mipmap.ic_nor_full_clickleft);
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.av_ll_fullrightclick:
                            mImgFullRightclick.setImageResource(R.mipmap.ic_nor_full_clickright);
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.av_ll_fullbottomclick:
                            mImgFullBottomclick.setImageResource(R.mipmap.ic_nor_full_clickbottom);
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        default:
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.av_ll_fulltopclick:
                            mImgFullTopclick.setImageResource(R.mipmap.ic_nor_full_top);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.av_ll_fullleftclick:
                            mImgFullLeftclick.setImageResource(R.mipmap.ic_nor_full_left);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.av_ll_fullrightclick:
                            mImgFullRightclick.setImageResource(R.mipmap.ic_nor_full_right);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.av_ll_fullbottomclick:
                            mImgFullBottomclick.setImageResource(R.mipmap.ic_nor_full_bottom);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private void doStartPlar() {
        if (m4BoxMode){
            showToast("请选择具体设备");
            return;
        }

        //播放与暂停
        VideoFragment fragment = getInTopVideo();
        if (fragment!=null){
            Boolean res = fragment.startPlay();
            if (res!=null){
                if (res){
                    mImgStartVideo.setImageResource(R.mipmap.ic_nor_stopvideo);
                    mImgFullStart.setImageResource(R.mipmap.ic_nor_full_stop);
                    mImgFullControlPlay.setImageResource(R.mipmap.ic_nor_full_stop);
                }else{
                    mImgStartVideo.setImageResource(R.mipmap.ic_nor_startvideo);
                    mImgFullStart.setImageResource(R.mipmap.ic_nor_full_start);
                    mImgFullControlPlay.setImageResource(R.mipmap.ic_nor_full_start);
                }
            }
        }
    }

    private void doVoiceControl() {
        if (m4BoxMode){
            showToast("请选择具体设备");
            return;
        }

        //声音控制
        VideoFragment fragment = getInTopVideo();
        if (fragment!=null){
            Boolean res = fragment.onSoundBtnClick();
            if (res!=null){
                if (res){
                    mImgVoiceControl.setImageResource(R.mipmap.ic_nor_voiceopen);
                    mImgFullVoice.setImageResource(R.mipmap.ic_nor_full_voice);
                    mImgFullControlVoice.setImageResource(R.mipmap.ic_nor_full_voice);
                }else{
                    mImgVoiceControl.setImageResource(R.mipmap.ic_nor_voiceclose);
                    mImgFullVoice.setImageResource(R.mipmap.ic_nor_full_voiceclose);
                    mImgFullControlVoice.setImageResource(R.mipmap.ic_nor_full_voiceclose);
                }
            }
        }
    }

    private void doMultScreen() {
        //分屏
        if (m4BoxMode == false){

            final VideoFragment fragment = getInTopVideo();
            if (fragment!=null){
                if (fragment.getIsRecord()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("进入分屏界面会终止录像，是否进入分屏？");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mRlToolbar.getVisibility() == VISIBLE){
                                toolBarAnimation(false);
                            }
                            fragment.stopRealPlayRecord();
                            stopCurPlayer();
                            removeSpitPlayer();
                            mRlVideoPlay.removeView(mViewPagerSingle);
                            if (mRlVideoPlay.findViewById(R.id.av_viewpager_mult) == null)
                                mRlVideoPlay.addView(mViewPagerMult,0);
                            showSpitWindow();
                            m4BoxMode = true;
                            mImgVoiceControl.setImageResource(R.mipmap.ic_nor_voiceopen);
                            mImgStartVideo.setImageResource(R.mipmap.ic_nor_startvideo);
                            mImgMultScreen.setImageResource(R.mipmap.ic_nor_singlescreen);
                            mImgFullSplit.setImageResource(R.mipmap.ic_nor_full_split);
                            mImgFullStart.setImageResource(R.mipmap.ic_nor_full_start);
                            mImgFullVoice.setImageResource(R.mipmap.ic_nor_full_voice);
                            mImgFullControlPlay.setImageResource(R.mipmap.ic_nor_full_start);
                        }
                    });
                    builder.show();
                    return;
                }
            }


            if (mRlToolbar.getVisibility() == VISIBLE){
                toolBarAnimation(false);
            }
            stopCurPlayer();
            removeSpitPlayer();
            mRlVideoPlay.removeView(mViewPagerSingle);
            if (mRlVideoPlay.findViewById(R.id.av_viewpager_mult) == null)
                mRlVideoPlay.addView(mViewPagerMult,0);
            showSpitWindow();
            m4BoxMode = true;
            mImgVoiceControl.setImageResource(R.mipmap.ic_nor_voiceopen);
            mImgStartVideo.setImageResource(R.mipmap.ic_nor_startvideo);
            mImgMultScreen.setImageResource(R.mipmap.ic_nor_singlescreen);

            mImgFullSplit.setImageResource(R.mipmap.ic_nor_full_split);
            mImgFullStart.setImageResource(R.mipmap.ic_nor_full_start);
            mImgFullVoice.setImageResource(R.mipmap.ic_nor_full_voice);
            mImgFullControlPlay.setImageResource(R.mipmap.ic_nor_full_start);
        }else{
            if (mRlToolbar.getVisibility() != VISIBLE){
                toolBarAnimation(true);
            }
            removeSpitPlayer();
            mRlVideoPlay.removeView(mViewPagerMult);
            mRlVideoPlay.addView(mViewPagerSingle,0);
            int curpage = mViewPagerSingle.getCurrentItem();
            if (mVideoFragments.length > curpage)
                mVideoFragments[curpage].onSupportVisible();
            m4BoxMode = false;
            mImgMultScreen.setImageResource(R.mipmap.ic_nor_multscreen);

            mImgFullSplit.setImageResource(R.mipmap.ic_nor_full_split2);
        }
    }

    private void openQaWindows(){
        final VideoFragment fragment = getInTopVideo();
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        builder.setTitle("选择码流");
        builder.setItems( new String[]{"高清","标清"}, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (fragment != null) {
                    switch (position) {
                        case 0:
                            //高清
                            fragment.setVideoQa(VideoFragment.QA_HD);
                            mTvVideoQa.setText("高清");
                            mTvFullQa.setText("高清");
                            dialog.dismiss();
                            break;
                        case 1:
                            //标清
                            fragment.setVideoQa(VideoFragment.QA_BA);
                            mTvVideoQa.setText("标清");
                            mTvFullQa.setText("标清");
                            dialog.dismiss();
                            break;
                    }
                }
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    private Message buildMessage(boolean bOpen,int sec){
        Message message = new Message();
        message.obj = bOpen;
        message.arg1 = sec;
        message.what = bOpen ? 0 : 1;
        return message;
    }

//    private boolean bCanControl = false;
//    private boolean canControlVideo(){
//        if (mVideoIoBean!=null){
//            return bCanControl || mVideoIoBean.getNextHandleSecond() == 0 || mVideoIoBean.getRestHandleSecond() > 0 ;
//
//        }
//        return false;
//    }
//    private boolean mRanking = false;
    private int mRankSec;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (VideoPlayActivity.this.isFinishing()){
                return false;
            }
            Boolean res = (Boolean) msg.obj;
            if (res){
                //排队
                mTvControl.setText("等待控制：" + CommonUtil.secToMills(mRankSec));
                mTvWaittime.setText("等待控制：" + CommonUtil.secToMills(mRankSec));
                mTvControl.setVisibility(VISIBLE);
                mImgControl.setVisibility(View.INVISIBLE);
                if (mRankSec > 0){
                    mRankSec--;
                    mHandler.sendMessageDelayed(buildMessage(true,0),1000l);
                }else{
                    //去控制
                    mImgControl.setImageResource(R.mipmap.ic_nor_controling);
                    mImgFullControl.setImageResource(R.mipmap.ic_nor_fullcontrol);
                    mTvControl.setVisibility(INVISIBLE);
                    mImgControl.setVisibility(View.VISIBLE);
                    bCanControl = true;
//                    mLlFullTopClick.setEnabled(true);
//                    mLlFullLeftClick.setEnabled(true);
//                    mLlFullRightClick.setEnabled(true);
//                    mLlFullBottomClick.setEnabled(true);
                    if (mOrientation == Configuration.ORIENTATION_PORTRAIT)
                        allowControlVideo(mVideoIoBean.getHandleTime());
                    else{
                        if (mLlFullControl.getVisibility() != View.VISIBLE){
                            mLlFullControl.setVisibility(VISIBLE);
                            mLlFullNormalControl.setVisibility(GONE);
                        }
                        int time = mVideoIoBean.getHandleTime()-1;
                        mTvWaittime.setText("剩余控制 " + CommonUtil.secToMills(time));
                        mFullScreenHandler.removeMessages(1);
                        Message message = new Message();
                        message.what=1;
                        message.arg1 = time-1;
                        mFullScreenHandler.sendMessage(message);
                    }
                    showToast("现在可以控制摄像头啦");
                }
            }else{
                //控制中
                int sec = msg.arg1;
                if (sec > 0){
                    mHandler.sendMessageDelayed(buildMessage(false,sec - 1),1000);
                }else{
                    mTvControl.setTag(null);
                    mImgControl.setImageResource(R.mipmap.ic_nor_control);
                    mImgFullControl.setImageResource(R.mipmap.ic_nor_full_control);
                }
            }

            return false;
        }
    });

    private void allowControlVideo(int time) {
        mTvControl.setVisibility(View.INVISIBLE);
        mTvControl.setText("控制");
        mTvControl.setTag(true);
        mImgControl.setImageResource(R.mipmap.ic_nor_controling);
        mImgFullControl.setImageResource(R.mipmap.ic_nor_fullcontrol);
        mImgControl.setVisibility(VISIBLE);
//        mTvControl.setTextColor(getResources().getColor(R.color.redfont));
        openVideoControlWindow(mVideoIoBean.getHandleTime());
    }

    private VideoControlPopupView mControlPopupView;
    private void openVideoControlWindow(int resTime) {
        if (mControlPopupView == null){
            mControlPopupView = new VideoControlPopupView(VideoPlayActivity.this);
            mControlPopupView.setLastTime(resTime);
            mControlPopupView.setOnVideoControl(new VideoControlPopupView.OnVideoControl() {

                @Override
                public void onCloseWindow(int sec) {
                    if (sec > 2){
                        mHandler.sendMessage(buildMessage(false,sec));
                    }else{
                        mImgControl.setImageResource(R.mipmap.ic_nor_control);
                        mImgFullControl.setImageResource(R.mipmap.ic_nor_full_control);
                        mImgControl.setVisibility(VISIBLE);
                        mTvControl.setVisibility(View.INVISIBLE);
                        mTvControl.setTag(null);
                    }
                }

                @Override
                public void onControlEnd() {
                    mImgControl.setImageResource(R.mipmap.ic_nor_control);
                    mImgFullControl.setImageResource(R.mipmap.ic_nor_full_control);
                    mImgControl.setVisibility(VISIBLE);
                    mTvControl.setVisibility(View.INVISIBLE);
                    mTvControl.setTag(null);
//                    mTvControl.setTextColor(mBlueColor);
                }

                @Override
                public void onActionTop(boolean bDownOrUp) {
                    VideoFragment fragment = getInTopVideo();
                    if (fragment!=null){
                        if (bDownOrUp){
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_UP);
//                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);
                        }else{
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                        }
                    }
                }

                @Override
                public void onActionLeft(boolean bDownOrUp) {
                    VideoFragment fragment = getInTopVideo();
                    if (fragment!=null){
                        if (bDownOrUp){
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
//                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                        }else{
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                        }
                    }
                }

                @Override
                public void onActionRight(boolean bDownOrUp) {
                    VideoFragment fragment = getInTopVideo();
                    if (fragment!=null){
                        if (bDownOrUp){
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
//                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                        }else{
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                        }
                    }
                }

                @Override
                public void onActionBottom(boolean bDownOrUp) {
                    VideoFragment fragment = getInTopVideo();
                    if (fragment!=null){
                        if (bDownOrUp){
                            fragment.setPtzDirectionIv(RealPlayStatus.PTZ_DOWN);
//                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                        }else{
                            fragment.ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                        }
                    }
                }
            });
        }
        mControlPopupView.setLastTime(resTime);
        mControlPopupView.showPopupWindow();
    }

    private void showSpitWindow() {
        buildMultViewPager();
        mViewPagerMult.setVisibility(View.VISIBLE);
    }

    private void stopCurPlayer() {
        VideoFragment fragment = getInTopVideo();
        if (fragment!=null){
            fragment.onSupportInvisible();
        }
    }

    private VideoFragment getInTopVideo(){
        for (int i=0; i<mVideoFragments.length;i++){
            VideoFragment fragment = mVideoFragments[i];
            if (fragment.isInTop())
                return fragment;
        }
        return null;
    }

    private void initGoodsUi(ViewHolder holder,final GoodsLikeBean.DataBean.ListBean bean, int position) {
        holder.setVisible(R.id.is_vert_img_tagshop,false);
        holder.itemView.findViewById(R.id.is_ll_vert).setVisibility(View.VISIBLE);
        holder.itemView.findViewById(R.id.fn_ll_hor).setVisibility(GONE);
        // VERT
        Glide.with(mContext).load(bean.getImg()).into((ImageView)holder.getView(R.id.is_vert_img_shop));
        holder.setText(R.id.is_vert_shop_groupnumber,"已售" + String.valueOf(bean.getSale())+"件");
        holder.setText(R.id.is_vert_shop_name,bean.getName());
        holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
        if (bean.getType() == 2) {
            //租赁商品
            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.is_vert_img_tagshop).getLayoutParams();
            layoutParams.removeRule(ALIGN_PARENT_LEFT);
            layoutParams.addRule(ALIGN_PARENT_RIGHT);
            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagfree);
            holder.setVisible(R.id.is_vert_img_tagshop,true);
        }else if (bean.getType() == 1){
            //拼团商品
            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.is_vert_img_tagshop).getLayoutParams();
            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
            layoutParams.addRule(ALIGN_PARENT_LEFT);
            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_taggroup);
            holder.setVisible(R.id.is_vert_img_tagshop,true);
        }else if (bean.getType() == 3){
            //折扣
            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.is_vert_img_tagshop).getLayoutParams();
            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
            layoutParams.addRule(ALIGN_PARENT_LEFT);
            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagdiscount);
            holder.setVisible(R.id.is_vert_img_tagshop,true);
        }else if (bean.getType() == 4){
            //积分
            holder.setText(R.id.is_vert_shop_price,bean.getPrice() + "积分");
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.is_vert_img_tagshop).getLayoutParams();
            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
            layoutParams.addRule(ALIGN_PARENT_LEFT);

            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagintegral);
            holder.setVisible(R.id.is_vert_img_tagshop,true);
        }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
            //限时抢购
            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getView(R.id.is_vert_img_tagshop).getLayoutParams();
            layoutParams.removeRule(ALIGN_PARENT_RIGHT);
            layoutParams.addRule(ALIGN_PARENT_LEFT);

            holder.setImageResource(R.id.is_vert_img_tagshop,R.mipmap.ic_nor_tagtimekill);
            holder.setVisible(R.id.is_vert_img_tagshop,true);
        }else{
            holder.setText(R.id.is_vert_shop_price,"¥" + bean.getPrice());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商品类型 0 普通商品 1 团购商品 2 租赁商品
                if (bean.getType() == 0) {
                    if (bean.getPanicBuyItemId() != 0){
                        Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                        intent.putExtra("id",bean.getPanicBuyItemId() + "");
                        mContext.startActivity(intent);
                    }else{
                        Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                        intent.putExtra("goodsId",bean.getId() + "");
                        mContext.startActivity(intent);
                    }
                } else if (bean.getType() == 2) {
                    Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                    intent.putExtra("id", bean.getId() + "");
                    mContext.startActivity(intent);
                }else if (bean.getType() ==  1) {
                    //拼团
                    Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                    intent.putExtra("goodsId", bean.getId() + "");
                    mContext.startActivity(intent);
                }else if (bean.getType() == 3){
                    //折扣
                    Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                    intent.putExtra("goodsId", bean.getId() + "");
                    mContext.startActivity(intent);
                }else if (bean.getType() == 4){
                    //积分
                    Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                    intent.putExtra("id", bean.getId() + "");
                    mContext.startActivity(intent);
                }
                else if (bean.getType() == 6){
                    //积分
                    Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                    intent.putExtra("goodsId", bean.getId() + "");
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public class MyOrientationDetector extends OrientationEventListener {

        private WindowManager mWindowManager;
        private int mLastOrientation = 0;

        public MyOrientationDetector(Context context) {
            super(context);
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        public boolean isWideScrren() {
            Display display = mWindowManager.getDefaultDisplay();
            Point pt = new Point();
            display.getSize(pt);
            return pt.x > pt.y;
        }
        @Override
        public void onOrientationChanged(int orientation) {
            int value = getCurentOrientationEx(orientation);
            if (value != mLastOrientation) {
                mLastOrientation = value;
                int current = getRequestedOrientation();
                if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }
        }

        private int getCurentOrientationEx(int orientation) {
            int value = 0;
            if (orientation >= 315 || orientation < 45) {
                // 0度
                value = 0;
                return value;
            }
            if (orientation >= 45 && orientation < 135) {
                // 90度
                value = 90;
                return value;
            }
            if (orientation >= 135 && orientation < 225) {
                // 180度
                value = 180;
                return value;
            }
            if (orientation >= 225 && orientation < 315) {
                // 270度
                value = 270;
                return value;
            }
            return value;
        }
    }

    public interface OnVideoSelect {
        void onVideoClick();
        void onVideoSelect(VideoListBean.DataBean.ListBean bean);
        void onVideoPlayState(int state);
        void onVideoVoiceControl(boolean bOpen);
        void onVideoRecordState(boolean bStart);
    }
}
