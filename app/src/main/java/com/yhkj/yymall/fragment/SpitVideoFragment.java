package com.yhkj.yymall.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezvizuikit.open.EZUIError;
import com.videogo.openapi.bean.EZDeviceInfo;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by Administrator on 2017/9/22.
 */

public class SpitVideoFragment extends SupportFragment {

    private List<VideoListBean.DataBean.ListBean> mEZDeviceInfoList;

    public static SpitVideoFragment getInstance() {
        SpitVideoFragment fragment = new SpitVideoFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Bind(R.id.fs_videocontainer)
    LinearLayout mLlVideoContainer;

    @Bind(R.id.fs_ll_rootview)
    LinearLayout mLLRootView;

    private int mType = -1; //0-4box 1-9box
    private static final int BOX_4 = 0;
    private static final int BOX_9 = 1;


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        startPlay();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlay();
        mSpitPlayerArr.clear();
    }

    public void setData(int type,boolean autoPlay,List<VideoListBean.DataBean.ListBean> deviceInfoList){
        mType = type;
        mAutoPlay = autoPlay;
        mEZDeviceInfoList = deviceInfoList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (mType == BOX_4)
            view = inflater.inflate(R.layout.fragment_spitvideo_4box,container,false);
        else
            view = inflater.inflate(R.layout.fragment_spitvideo_9box,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView((view));
    }

    private boolean mAutoPlay = false;
    protected void initView(final View contentView) {
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initVideo(contentView);
            }
        });
    }

    private boolean mInit = false;

    private void initVideo(final View contentView){
        if (mInit) return;
        mInit =true;
        if (mType == BOX_4){
            for (int i=0;i < mEZDeviceInfoList.size(); i++){
                switch (i){
                    case 0:
                        buildChildPlayer(0,R.id.fs_rl_ezuiplayer_1,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 1:
                        buildChildPlayer(1,R.id.fs_rl_ezuiplayer_2,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 2:
                        buildChildPlayer(2,R.id.fs_rl_ezuiplayer_3,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 3:
                        buildChildPlayer(3,R.id.fs_rl_ezuiplayer_4,contentView,mEZDeviceInfoList.get(i));
                        break;
                }
            }
        }else{
            for (int i=0;i < mEZDeviceInfoList.size(); i++){
                switch (i){
                    case 0:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_1,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 1:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_2,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 2:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_3,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 3:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_4,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 4:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_5,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 5:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_6,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 6:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_7,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 7:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_8,contentView,mEZDeviceInfoList.get(i));
                        break;
                    case 8:
                        buildChildPlayer(i,R.id.fs_rl_ezuiplayer_9,contentView,mEZDeviceInfoList.get(i));
                        break;
                }
            }
        }
    }

    public void startPlay(){
        if (!mIsPlay){
            mIsPlay = true;
            for (int i=0; i<mSpitPlayerArr.size(); i++){
                int key = mSpitPlayerArr.keyAt(i);
                VideoBrother videoBrother = mSpitPlayerArr.get(key);
                if (videoBrother!=null && videoBrother.mEzUiPlayer!=null)
                    videoBrother.mEzUiPlayer.startPlay();
            }
        }
    }

    public void setSurfaceSize(int width){
        for (int i=0; i<mSpitPlayerArr.size(); i++){
            int key = mSpitPlayerArr.keyAt(i);
            VideoBrother videoBrother = mSpitPlayerArr.get(key);
            if (videoBrother!=null && videoBrother.mEzUiPlayer!=null)
                videoBrother.mEzUiPlayer.setSurfaceSize(width,0);
        }
    }

    private boolean mIsPlay = false;
    public void stopPlay(){
        if (mIsPlay){
            mIsPlay = false;
            for (int i=0; i<mSpitPlayerArr.size(); i++){
                int key = mSpitPlayerArr.keyAt(i);
                VideoBrother videoBrother = mSpitPlayerArr.get(key);
                if (videoBrother!=null && videoBrother.mEzUiPlayer!=null) {
                    if (videoBrother.mLoadingView!=null)
                        videoBrother.mLoadingView.setVisibility(View.VISIBLE);
                    videoBrother.mEzUiPlayer.stopPlay();
                }
            }
        }
    }

    public void releasePlay(){
        for (int i=0; i<mSpitPlayerArr.size(); i++){
            int key = mSpitPlayerArr.keyAt(i);
            VideoBrother videoBrother = mSpitPlayerArr.get(key);
            if (videoBrother!=null && videoBrother.mEzUiPlayer!=null) {
                videoBrother.mEzUiPlayer.stopPlay();
                videoBrother.mEzUiPlayer.releasePlayer();
                mSpitPlayerArr.put(i,null);
            }
        }
    }

    private OnSpitVideoSelect mOnSpitVideoSelect;

    public void setOnSpitVideoSelect(OnSpitVideoSelect OnSpitVideoSelect) {
        this.mOnSpitVideoSelect = OnSpitVideoSelect;
    }

    public interface OnSpitVideoSelect {
        void onVideoSelect(VideoListBean.DataBean.ListBean deviceInfo, int pos);
    }

    private SparseArray<VideoBrother> mSpitPlayerArr = new SparseArray<>();
    private void buildChildPlayer(final int key, int parentId, View contentView, final VideoListBean.DataBean.ListBean deviceInfo){
        RelativeLayout rlVideoContainer = (RelativeLayout)contentView.findViewById(parentId);

        if (deviceInfo.getStatus() != 1){
            //设备不在线
            TextView errTip = new TextView(_mActivity);
            errTip.setText("设备不在线");
            errTip.setTextColor(getResources().getColor(R.color.white));
            errTip.setTextSize(12);
            rlVideoContainer.addView(errTip);
            return;
        }

        rlVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSpitVideoSelect!=null){
                    mOnSpitVideoSelect.onVideoSelect(deviceInfo,key + 1);
                }
            }
        });

        final View laodingView = LayoutInflater.from(_mActivity).inflate(R.layout.view_video_loading,rlVideoContainer,false);
        rlVideoContainer.addView(laodingView);

        final EZUIPlayer ezuiPlayer = new EZUIPlayer(_mActivity);
        ezuiPlayer.setSurfaceSize(rlVideoContainer.getWidth(),0);
//        ezuiPlayer.setOpenSound(false);
        ezuiPlayer.setAutoPlay(mAutoPlay);
        ezuiPlayer.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {

            @Override
            public void onShowLoading() {
                laodingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPlaySuccess() {
                Log.e("ezuiplayer","onPlaySuccess");
//                            ezUIPlayer.setZOrderOnTop(true);
                laodingView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                if (ezuiError.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)){

                }else if(ezuiError.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_NOT_FOUND_RECORD_FILES)){
                    // TODO: 2017/5/12
                    //未发现录像文件
                }
            }

            @Override
            public void onVideoSizeChange(int width, int height) {
            }

            @Override
            public void onPrepared() {
//                ezuiPlayer.startPlay();
            }

            @Override
            public void onPlayTime(Calendar calendar) {

            }

            @Override
            public void onRetryLoad() {

            }

            @Override
            public void onPlayFinish() {
                Log.e("ezuiplayer","onPlayFinish");
            }
        });
        String ezopenUrl = "ezopen://open.ys7.com/" + deviceInfo.getDeviceSerial() + "/1.live?mute=true";
        rlVideoContainer.addView(ezuiPlayer,0);

        ezuiPlayer.setUrl(ezopenUrl);
        mSpitPlayerArr.put(key,new VideoBrother(ezuiPlayer,laodingView));
    }

    class VideoBrother {
        public EZUIPlayer mEzUiPlayer;
        public View mLoadingView;
        public VideoBrother(EZUIPlayer ezuiPlayer,View loadingView){
            mEzUiPlayer = ezuiPlayer;
            mLoadingView = loadingView;
        }
    }
}

