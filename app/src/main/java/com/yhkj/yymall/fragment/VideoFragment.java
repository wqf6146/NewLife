package com.yhkj.yymall.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.yhkj.yymall.R;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_video,container,false);
        mRlContainer = (RelativeLayout)view;
        mEzUiPlayer = (EZUIPlayer) view.findViewById(R.id.vv_ezuiplayer);
        return view;
    }

    private String mToken,mUrl;
    private VideoListBean.DataBean.ListBean mDataBean;
    private MyOrientationDetector mOrientationDetector;
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mToken = getArguments().getString("token");
        mDataBean = getArguments().getParcelable("data");
        //设置授权token
        EZUIKit.setAccessToken(mToken);
        mOrientationDetector = new MyOrientationDetector(_mActivity);
        initUi();
    }

    private void initUi() {
        if (mDataBean.getStatus() != 1){
            TextView errTip = new TextView(_mActivity);
            errTip.setText("设备不在线");
            errTip.setTextColor(getResources().getColor(R.color.white));
            errTip.setTextSize(12);
            mRlContainer.addView(errTip);
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
//                mEzUiPlayer.startPlay();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        onOrientationChanged();
        super.onConfigurationChanged(newConfig);
    }

    private void onOrientationChanged() {
        setSurfaceSize();
    }

    private void setSurfaceSize(){
        DisplayMetrics dm = new DisplayMetrics();
        _mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        boolean isWideScrren = mOrientationDetector.isWideScrren();
        //竖屏
        if (!isWideScrren) {
            mFlVideoPlay.setLayoutParams(mVCLayouyParams);
            //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
            mEzUiPlayer.setSurfaceSize(dm.widthPixels, 0);
        } else {
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            mFlVideoPlay.setLayoutParams(realPlayPlayRlLp);

            //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
            mEzUiPlayer.setSurfaceSize(dm.widthPixels,dm.heightPixels);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        startPlay();
    }

    private void startPlay() {
        mEzUiPlayer.startPlay();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
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
        mOrientationDetector.disable();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void releasePlay(){
        mEzUiPlayer.stopPlay();
        mEzUiPlayer.releasePlayer();
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
                int current = _mActivity.getRequestedOrientation();
                if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        || current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    _mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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
}
