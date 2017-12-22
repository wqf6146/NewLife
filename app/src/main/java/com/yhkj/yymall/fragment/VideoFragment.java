package com.yhkj.yymall.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.hyphenate.chat.ChatClient;
import com.videogo.exception.BaseException;
import com.videogo.exception.ErrorCode;
import com.videogo.exception.InnerException;
import com.videogo.openapi.EZConstants;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.realplay.RealPlayStatus;
import com.videogo.util.LocalInfo;
import com.videogo.util.LogUtil;
import com.videogo.util.MediaScanner;
import com.videogo.util.SDCardUtil;
import com.videogo.util.Utils;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.VideoPlayActivity;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.AudioPlayUtil;
import com.yhkj.yymall.util.EZUtils;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/12/16.
 */

public class VideoFragment extends SupportFragment {

    public static VideoFragment getInstance(String token,int pos,boolean bShow,VideoListBean.DataBean.ListBean bean) {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        bundle.putInt("pos",pos);
        bundle.putBoolean("bshow",bShow);
        bundle.putParcelable("data",bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EZUIPlayer mEzUiPlayer;
    private RelativeLayout mRlContainer;
    private TextView mTvTip;
    private ImageView mImgPic,mImgDirection;
    private LinearLayout mLLRecordView;
    private ImageView mImgRecordTag;
    private TextView mTvRecordSec;
    private VideoPlayActivity.OnVideoSelect mVideoParent;

    public int getVideoStatus(){
        if (mEzUiPlayer==null)
            return -1;
        return mEzUiPlayer.getStatus();
    }

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
        init(view);
        return view;
    }

    private void init(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoParent!=null)
                    mVideoParent.onVideoClick();
            }
        });


        mEzUiPlayer = (EZUIPlayer) view.findViewById(R.id.vv_ezuiplayer);
        mTvTip = (TextView)view.findViewById(R.id.vv_tv_tip);
        mImgPic = (ImageView)view.findViewById(R.id.vv_img_pic);
        mLLRecordView = (LinearLayout)view.findViewById(R.id.vv_ll_record);
        mImgRecordTag = (ImageView)view.findViewById(R.id.vv_img_realplay);
        mTvRecordSec = (TextView)view.findViewById(R.id.vv_tv_realplay);
        mImgDirection = (ImageView)view.findViewById(R.id.vv_img_direction);
        mToken = getArguments().getString("token");
        mDataBean = getArguments().getParcelable("data");
    }

    private String mToken,mPreUrl,mErrStr;
    private VideoListBean.DataBean.ListBean mDataBean;

    public VideoListBean.DataBean.ListBean getDataBean(){
        return mDataBean;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        //设置授权token
        EZUIKit.setAccessToken(mToken);
        initUi();
    }
    private LocalInfo mLocalInfo;
    private void initUi(){
        if (mDataBean.getStatus() != 1){
            mRlContainer.removeView(mEzUiPlayer);
            mErrStr = "设备不在线";
            mTvTip.setText(mErrStr);
            mTvTip.setVisibility(View.VISIBLE);
            return;
        }
        mLocalInfo = LocalInfo.getInstance();
        mAudioPlayUtil = AudioPlayUtil.getInstance(YYApp.getInstance());
        final View laodingView = LayoutInflater.from(_mActivity).inflate(R.layout.view_video_loading,mRlContainer,false);
        mRlContainer.addView(laodingView);
        DisplayMetrics dm = new DisplayMetrics();
        _mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mEzUiPlayer.setSurfaceSize(dm.widthPixels, 0);
        mEzUiPlayer.setOpenSound(false);
        //getArguments().getInt("pos",0) == 0 ? true : false
        mEzUiPlayer.setAutoPlay(getArguments().getBoolean("bshow"));
        mEzUiPlayer.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {

            @Override
            public void onRetryLoad() {
                mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
            }

            @Override
            public void onShowLoading() {
                mImgPic.setVisibility(GONE);
                laodingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPlaySuccess() {
                Log.e("ezuiplayer","onPlaySuccess");
//                            ezUIPlayer.setZOrderOnTop(true);
                mImgPic.setVisibility(GONE);
                laodingView.setVisibility(View.INVISIBLE);
                if (mInTop && mVideoParent!=null) {
                    mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_PLAY);
                    mVideoParent.onVideoVoiceControl(true);
                }
                mErrStr = null;
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                if (mIsRecording){
                    stopRealPlayRecord();
                }
                if (mInTop && mVideoParent!=null) {
                    mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT);
                    mVideoParent.onVideoVoiceControl(false);
                }
                mErrStr = ezuiError.getErrorString();
                Toast.makeText(_mActivity, mErrStr, Toast.LENGTH_SHORT).show();
//                mTvTip.setText(mErrStr);
//                mTvTip.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoSizeChange(int width, int height) {
            }

            @Override
            public void onPrepared() {
                Log.e("onPrepared","onPrepared");
                mEzUiPlayer.startPlay();
            }

            @Override
            public void onPlayTime(Calendar calendar) {

            }

            @Override
            public void onPlayFinish() {
                Log.e("ezuiplayer","onPlayFinish");
            }
        });
        mPreUrl = "ezopen://open.ys7.com/" + mDataBean.getDeviceSerial();
        mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
    }

    public final static String QA_HD = "/1.hd.live";
    public final static String QA_BA = "/1.live";
    private String mVideoQa = QA_BA;

    public void setSurfaceSize(int width){
        if (mEzUiPlayer!=null)
            mEzUiPlayer.setSurfaceSize(width, 0);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mInTop = true;
        if (mVideoParent!=null)
            mVideoParent.onVideoSelect(mDataBean);

        if (mDataBean.getStatus() == 1 && mEzUiPlayer.getStatus() != EZUIPlayer.STATUS_PLAY){
            mImgPic.setVisibility(GONE);
            mEzUiPlayer.startPlay();
        }else {
            if (mVideoParent!=null){
                mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT);
                mVideoParent.onVideoVoiceControl(false);
            }
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mInTop = false;
        if (mIsRecording){
            stopRealPlayRecord();
        }
        if (mDataBean.getStatus() == 1)
            stopPlay();
    }

    public void stopPlay() {
        if (mEzUiPlayer!=null)
            mEzUiPlayer.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        mHandler = null;
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

    public boolean isVideoNormal(){
        if (!mInTop) return false;

        if (!TextUtils.isEmpty(mErrStr)){
            Toast.makeText(_mActivity, mErrStr, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 开始录像
     *
     * @see
     * @since V1.0
     */
    private boolean mIsRecording = false;
    private String mRecordTime = null;
    private String mCurRecordName;
    public void onRecordBtnClick() {
        if (!isVideoNormal()){
            return ;
        }

        if (mEzUiPlayer.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_STOP
                || mEzUiPlayer.getStatus() == com.ezvizuikit.open.EZUIPlayer.STATUS_INIT) {
//            if (mVideoParent!=null){
//                mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_PLAY);
//            }
//            mEzUiPlayer.startPlay();
            Utils.showToast(_mActivity,"请先获取直播，再录像");
            return;
        }

        if (mIsRecording) {
            stopRealPlayRecord();
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(_mActivity,"存储卡不可用");
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(_mActivity, "录像中断,存储空间已满");
            return;
        }

        if (mEzUiPlayer.getEZUIPlayer() != null) {
            mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
            java.util.Date date = new java.util.Date();
            String strRecordFile = Environment.getExternalStorageDirectory().getPath() + "/YiYiYaYa/Records/" + String.format("%tY", date)
                    + String.format("%tm", date) + String.format("%td", date) + "/"
                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) + ".mp4";

            if (mEzUiPlayer.getEZUIPlayer().startLocalRecordWithFile(strRecordFile))
            {
                handleRecordSuccess(strRecordFile);
            } else {
                handleRecordFail();
            }
        }
    }

    public boolean getIsRecord(){
        return mIsRecording;
    }

    private void handleRecordFail() {
        Utils.showToast(_mActivity,"录像失败");
        if (mIsRecording) {
            stopRealPlayRecord();
        }
    }
    /**
     * 开始录像成功
     *
     * @param recordFilePath
     * @see
     * @since V2.0
     */
    /**
     * 录像时间
     */
    private int mRecordSecond = 0;
    private void handleRecordSuccess(String recordFilePath) {
        EZUtils.updateVideo(_mActivity,recordFilePath);
        mCurRecordName = "已将录像保存至目录：" + recordFilePath;
        if (mVideoParent!=null){
            mVideoParent.onVideoRecordState(true);
        }

        mIsRecording = true;
        // 计时按钮可见
        mLLRecordView.setVisibility(View.VISIBLE);
        mTvRecordSec.setText("00:00");
        mRecordSecond = 0;
        startUpdateTimer();
    }
    /**
     * 启动定时器
     *
     * @see
     * @since V1.0
     */
    /**
     * 定时器
     */
    private Timer mUpdateTimer = null;
    /**
     * 定时器执行的任务
     */
    private TimerTask mUpdateTimerTask = null;
    private void startUpdateTimer() {
        stopUpdateTimer();
        // 开始录像计时
        mUpdateTimer = new Timer();
        mUpdateTimerTask = new TimerTask() {
            @Override
            public void run() {
                // 更新录像时间
                if (mEzUiPlayer != null && mIsRecording) {
                    // 更新录像时间
                    Calendar OSDTime = mEzUiPlayer.getOSDTime();
                    if (OSDTime != null) {
                        String playtime = Utils.OSD2Time(OSDTime);
                        if (!TextUtils.equals(playtime, mRecordTime)) {
                            mRecordSecond++;
                            mRecordTime = playtime;
                        }
                    }
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(MSG_PLAY_UI_UPDATE);
                }
            }
        };
        // 延时1000ms后执行，1000ms执行一次
        mUpdateTimer.schedule(mUpdateTimerTask, 0, 1000);
    }
    // UI消息
    public static final int MSG_PLAY_UI_UPDATE = 200;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_PLAY_UI_UPDATE:
                    updateRealPlayUI();
                    break;
                case MSG_HIDE_PTZ_DIRECTION:
                    handleHidePtzDirection(msg);
                    break;
            }
            return false;
        }
    });

    private void updateRealPlayUI() {
        if (mIsRecording){
            updateRecordTime();
        }
    }
    /**
     * 更新录像时间
     *
     * @see
     * @since V1.0
     */
    private void updateRecordTime() {
        if (mImgRecordTag.getVisibility() == View.VISIBLE) {
            mImgRecordTag.setVisibility(View.INVISIBLE);
        } else {
            mImgRecordTag.setVisibility(View.VISIBLE);
        }
        // 计算分秒
        int leftSecond = mRecordSecond % 3600;
        int minitue = leftSecond / 60;
        int second = leftSecond % 60;

        // 显示录像时间
        String recordTime = String.format("%02d:%02d", minitue, second);
        mTvRecordSec.setText(recordTime);
    }
    /**
     * 停止定时器
     *
     * @see
     * @since V1.0
     */
    private void stopUpdateTimer() {
        if (mHandler!=null)
            mHandler.removeMessages(MSG_PLAY_UI_UPDATE);
        // 停止录像计时
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }

        if (mUpdateTimerTask != null) {
            mUpdateTimerTask.cancel();
            mUpdateTimerTask = null;
        }
    }
    /**
     * 停止录像
     *
     * @see
     * @since V1.0
     */
    public void stopRealPlayRecord() {
        if (mEzUiPlayer.getEZUIPlayer() == null || !mIsRecording) {
            return;
        }
        Toast.makeText(_mActivity, mCurRecordName, Toast.LENGTH_SHORT).show();
        mCurRecordName = "";
        if (mVideoParent!=null){
            mVideoParent.onVideoRecordState(false);
        }
        mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
        mEzUiPlayer.getEZUIPlayer().stopLocalRecord();
        // 计时按钮不可见
        mLLRecordView.setVisibility(GONE);
        mIsRecording = false;
    }

    public Boolean onSoundBtnClick(){
        if (!isVideoNormal()){
            return null;
        }
        if (mEzUiPlayer.getStatus() != EZUIPlayer.STATUS_PLAY) {
            return null;
        }
        if (mLocalInfo.isSoundOpen()) {
            mLocalInfo.setSoundOpen(false);
            mEzUiPlayer.setOpenSound(false);
            return false;
        } else {
            mLocalInfo.setSoundOpen(true);
            mEzUiPlayer.setOpenSound(true);
            return true;
        }
    }

    public Boolean setVideoQa(final String qa){
        if (!isVideoNormal()){
            return null;
        }

        if (mIsRecording){
            AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
            builder.setTitle("提示");
            builder.setMessage("选择码流会终止录像，是否选择码流？");
            builder.setPositiveButton("取消", null);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopRealPlayRecord();
                    mVideoQa = qa;
                    mEzUiPlayer.stopPlay();
                    mEzUiPlayer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
                        }
                    },500);
                }
            });
            builder.show();
            return null;
        }

        mVideoQa = qa;
        mEzUiPlayer.stopPlay();
        mEzUiPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEzUiPlayer.setUrl(mPreUrl + mVideoQa);
            }
        },500);

        return null;
    }
    private AudioPlayUtil mAudioPlayUtil = null;
    public void onCapturePicBtnClick() {

        if (!isVideoNormal()){
            return;
        }

        if (!SDCardUtil.isSDCardUseable()) {
            // 提示SD卡不可用
            Utils.showToast(_mActivity, "存储卡不可用");
            return;
        }

        if (SDCardUtil.getSDCardRemainSize() < SDCardUtil.PIC_MIN_MEM_SPACE) {
            // 提示内存不足
            Utils.showToast(_mActivity, "抓图失败,存储空间已满");
            return;
        }

        if (mEzUiPlayer.getEZUIPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
            Thread thr = new Thread() {
                @Override
                public void run() {
                    Bitmap bmp = mEzUiPlayer.getEZUIPlayer().capturePicture();
                    if (bmp != null) {
                        try {
                            mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);
                            // 可以采用deviceSerial+时间作为文件命名，demo中简化，只用时间命名
                            java.util.Date date = new java.util.Date();
                            final String path = Environment.getExternalStorageDirectory().getPath() + "/YiYiYaYa/CapturePicture/" + String.format("%tY", date)
                                    + String.format("%tm", date) + String.format("%td", date) + "/"
                                    + String.format("%tH", date) + String.format("%tM", date) + String.format("%tS", date) + String.format("%tL", date) +".jpg";

                            if (TextUtils.isEmpty(path)) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                            EZUtils.saveCapturePictrue(path, bmp);


                            MediaScanner mMediaScanner = new MediaScanner(_mActivity);
                            mMediaScanner.scanFile(path, "jpg");
                            _mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(_mActivity, "已保存至相册"+path, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (InnerException e) {
                            e.printStackTrace();
                        } finally {
                            if (bmp != null) {
                                bmp.recycle();
                                bmp = null;
                                return;
                            }
                        }
                    }
                    super.run();
                }
            };
            thr.start();
        }
    }

    public void uploadCurFrame(){
        if (mEzUiPlayer.getEZUIPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Bitmap bmp = mEzUiPlayer.getEZUIPlayer().capturePicture();
                    if (bmp==null)return;
                    File file = new File(Environment.getExternalStorageDirectory(), "a.jpg");
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(
                                new FileOutputStream(file));
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bos.flush();
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    YYMallApi.updateCurFragme(_mActivity, file, String.valueOf(mDataBean.getId()), new ApiCallback<CommonBean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(CommonBean commonBean) {
                            Log.e("VideoFragment","updaload img suc!");
                        }

                        @Override
                        public void onError(ApiException e) {
                            super.onError(e);
                            Log.e("VideoFragment",e.getMessage());
                        }
                    });

                }
            }.start();
        }
    }


    public Boolean startPlay(){
        if (!isVideoNormal()){
            return null;
        }

        if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
            //播放状态，点击停止播放
            if (mIsRecording){
                AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
                builder.setTitle("提示");
                builder.setMessage("暂停观看直播会终止录像，是否暂停观看？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopRealPlayRecord();
                        if (mVideoParent!=null){
                            mVideoParent.onVideoPlayState(EZUIPlayer.STATUS_INIT);
                        }
                        if (mEzUiPlayer.getEZUIPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final WeakReference<Bitmap> bmp = new WeakReference(mEzUiPlayer.getEZUIPlayer().capturePicture());
                                    if (bmp==null || bmp.get()==null)
                                        return;
                                    try {
                                        _mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mImgPic.setImageBitmap(bmp.get());
                                                mImgPic.setVisibility(View.VISIBLE);
                                                mEzUiPlayer.stopPlay();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                    }
                                }
                            }).start();
                        }
                    }
                });
                builder.show();
                return null;
            }

            if (mEzUiPlayer.getEZUIPlayer() != null && mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final WeakReference<Bitmap> bmp = new WeakReference(mEzUiPlayer.getEZUIPlayer().capturePicture());
                        if (bmp==null || bmp.get()==null)
                            return;
                        try {
                            _mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mImgPic.setImageBitmap(bmp.get());
                                    mImgPic.setVisibility(View.VISIBLE);
                                    mEzUiPlayer.stopPlay();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
//                            if (bmp != null && bmp.get()!=null) {
//                                bmp.get().recycle();
//                            }
                        }
                    }
                }).start();
                return false;
            }
        }  else if (mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_STOP
                || mEzUiPlayer.getStatus() == EZUIPlayer.STATUS_INIT) {
            mImgPic.setVisibility(View.GONE);
            mEzUiPlayer.startPlay();
            return true;
        }
        return null;
    }


    /**
     * 云台操作
     *
     * @param command ptz控制命令
     * @param action  控制启动/停止
     */
    public void ptzOption(final EZConstants.EZPTZCommand command, final EZConstants.EZPTZAction action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean ptz_result = false;
                try {
                    ptz_result = EZOpenSDK.getInstance().controlPTZ(mDataBean.getDeviceSerial(), mEzUiPlayer.getCameraId(), command,
                            action, EZConstants.PTZ_SPEED_DEFAULT);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
                LogUtil.i("VideoFragment", "controlPTZ ptzCtrl result: " + ptz_result);
            }
        }).start();
    }

    public static final int MSG_HIDE_PTZ_DIRECTION = 204;
    private void handleHidePtzDirection(Message msg) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        if (msg.arg1 > 2) {
            mImgDirection.setVisibility(GONE);
        } else {
            mImgDirection.setVisibility(msg.arg1 == 1 ? GONE : View.VISIBLE);
            Message message = new Message();
            message.what = MSG_HIDE_PTZ_DIRECTION;
            message.arg1 = msg.arg1 + 1;
            mHandler.sendMessageDelayed(message, 500);
        }
    }


    public void setPtzDirectionIv(int command) {
        if (command != -1 ) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            switch (command) {
                case RealPlayStatus.PTZ_LEFT:
                    mImgDirection.setBackgroundResource(R.drawable.left_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    mImgDirection.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_RIGHT:
                    mImgDirection.setBackgroundResource(R.drawable.right_twinkle);
                    params.addRule(RelativeLayout.CENTER_VERTICAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    mImgDirection.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_UP:
                    mImgDirection.setBackgroundResource(R.drawable.up_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    mImgDirection.setLayoutParams(params);
                    break;
                case RealPlayStatus.PTZ_DOWN:
                    mImgDirection.setBackgroundResource(R.drawable.down_twinkle);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.vv_ezuiplayer);
                    mImgDirection.setLayoutParams(params);
                    break;
                default:
                    break;
            }
            mImgDirection.setVisibility(View.VISIBLE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
            Message msg = new Message();
            msg.what = MSG_HIDE_PTZ_DIRECTION;
            msg.arg1 = 1;
            mHandler.sendMessageDelayed(msg, 500);
        } else {
            mImgDirection.setVisibility(GONE);
            mHandler.removeMessages(MSG_HIDE_PTZ_DIRECTION);
        }
    }
}
