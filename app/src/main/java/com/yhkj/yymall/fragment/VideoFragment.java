package com.yhkj.yymall.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.VideoPlayActivity;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer;
import java.util.Calendar;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2017/12/16.
 */

public class VideoFragment extends SupportFragment {

    public static VideoFragment getInstance(String token,int pos,VideoListBean.DataBean.ListBean bean) {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        bundle.putInt("pos",pos);
        bundle.putParcelable("data",bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EZUIPlayer mEzUiPlayer;
    private RelativeLayout mRlContainer;
    private TextView mTvTip;
    private VideoPlayActivity.OnVideoSelect mVideoParent;

    private boolean mInTop = false;

    public VideoFragment setVideoParent(VideoPlayActivity.OnVideoSelect videoParent) {
        this.mVideoParent = videoParent;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_video,container,false);
        mRlContainer = (RelativeLayout)view;
        mEzUiPlayer = (EZUIPlayer) view.findViewById(R.id.vv_ezuiplayer);
        mTvTip = (TextView)view.findViewById(R.id.vv_tv_tip);
        return view;
    }

    private String mToken,mUrl,mErrStr;
    private VideoListBean.DataBean.ListBean mDataBean;

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mToken = getArguments().getString("token");
        mDataBean = getArguments().getParcelable("data");
        //设置授权token
        EZUIKit.setAccessToken(mToken);
        initUi();
    }

    private void initUi() {
        if (mDataBean.getStatus() != 1){
            mRlContainer.removeView(mEzUiPlayer);
            mErrStr = "设备不在线";
            mTvTip.setText(mErrStr);
            mTvTip.setVisibility(View.VISIBLE);
            return;
        }
        final View laodingView = LayoutInflater.from(_mActivity).inflate(R.layout.view_video_loading,mRlContainer,false);
        mRlContainer.addView(laodingView);
        DisplayMetrics dm = new DisplayMetrics();
        _mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mEzUiPlayer.setSurfaceSize(dm.widthPixels, 0);
        mEzUiPlayer.setOpenSound(false);
        mEzUiPlayer.setAutoPlay(getArguments().getInt("pos",0) == 0 ? true : false);
        mEzUiPlayer.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {

            @Override
            public void onShowLoading() {
                laodingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPlaySuccess() {
                Log.e("ezuiplayer","onPlaySuccess");
//                            ezUIPlayer.setZOrderOnTop(true);
                laodingView.setVisibility(View.INVISIBLE);
                if (mInTop && mVideoParent!=null)
                    mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_PLAY);
                mErrStr = null;
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                if (mInTop && mVideoParent!=null)
                    mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT);
                mErrStr = ezuiError.getErrorString();
                Toast.makeText(_mActivity, mErrStr, Toast.LENGTH_SHORT).show();
                mTvTip.setText(mErrStr);
                mTvTip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoSizeChange(int width, int height) {
            }

            @Override
            public void onPrepared() {

            }

            @Override
            public void onPlayTime(Calendar calendar) {

            }

            @Override
            public void onPlayFinish() {
                Log.e("ezuiplayer","onPlayFinish");
            }
        });
        mUrl = "ezopen://open.ys7.com/" + mDataBean.getDeviceSerial() + "/1.live";
        mEzUiPlayer.setUrl(mUrl);
    }

    public void setSurfaceSize(int width){
        if (mEzUiPlayer!=null)
            mEzUiPlayer.setSurfaceSize(width, 0);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mInTop = true;
        if (mVideoParent!=null)
            mVideoParent.onVideoSelect(mDataBean.getTitle());
        if (mDataBean.getStatus() == 1)
            startRealPlay();
        else
            mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT);
    }

    private void startRealPlay() {
        mEzUiPlayer.startPlay();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mInTop = false;
        if (mDataBean.getStatus() == 1)
            stopPlay();
    }

    private void stopPlay() {
        mEzUiPlayer.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlay();
    }

    //待用
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void releasePlay(){
        mEzUiPlayer.stopPlay();
        mEzUiPlayer.releasePlayer();
    }

    public boolean isInTop(){
        return mInTop;
    }

    private boolean isVideoNormal(){
        if (!mInTop) return false;

        if (!TextUtils.isEmpty(mErrStr)){
            Toast.makeText(_mActivity, mErrStr, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public Boolean startPlay(){
        if (!isVideoNormal()){
            return null;
        }

        if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
            //播放状态，点击停止播放
            mEzUiPlayer.pausePlay();
            return false;
        } else if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PAUSE) {
            //停止状态，点击播放
            mEzUiPlayer.resumePlay();
            return true;
        } else if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_STOP
                || mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_INIT) {
            mEzUiPlayer.startPlay();
            return true;
        }

        return null;
    }
}
