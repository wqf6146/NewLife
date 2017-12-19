package com.yhkj.yymall.view.EZUIkit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.ParamException;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.openapi.EZPlayer;
import com.videogo.openapi.EzvizAPI;
import com.videogo.openapi.bean.EZCloudRecordFile;
import com.videogo.openapi.bean.EZRecordFile;
import com.videogo.util.LogUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

public class EZUIPlayer extends RelativeLayout implements EZUIPlayerInterface {
    private static final String TAG = "EZUIPlayer";
    private Context mContext;
    private SurfaceView mSurfaceView;
    private RelativeLayout mSurfaceFrame;
    private SurfaceHolder mHolder;
    private EZPlayer mEZPlayer;
    private EZUIPlayer.EZUIPlayerCallBack mEzUIPlayerCallBack;
    private View mLoadView;
    private TextView mErrorTextView;
    private int mWidth = 0;
    private int mHeight = 0;
    private int mDefaultWidth = 0;
    private int mDefaultHeight = 0;
    private int mVideoWidth = 0;
    private int mVideoHeight = 0;
    private int mStatus = 0;
    private AtomicBoolean isSurfaceInit = new AtomicBoolean(false);
    private boolean isOpenSound = true;
    public static final int STATUS_INIT = 0;
    public static final int STATUS_START = 1;
    public static final int STATUS_STOP = 2;
    public static final int STATUS_PLAY = 3;
    public static final int STATUS_PAUSE = 4;
    private EZPlayURLParams ezPlayURLParams = null;
    private ArrayList<EZRecordFile> mRecordFiles;
    private ArrayList<EZRecordFile> mPlayRecordList;
    private int mCurrentIndex = 0;
    private boolean isPlayBack;
    private static final int MSG_UPDATE_OSD = 8888;
    private Calendar mSeekCalendar;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String e;
            switch(msg.what) {
            case 102:
            case 205:
                LogUtil.d("EZUIPlayer", "MSG_REALPLAY_PLAY_SUCCESS");
                EZUIPlayer.this.dismissomLoading();
                EZUIPlayer.this.optionSound();
                if(EZUIPlayer.this.mStatus != 2) {
                    EZUIPlayer.this.setStatus(3);
                    EZUIPlayer.this.mEzUIPlayerCallBack.onPlaySuccess();
                }

                this.sendEmptyMessage(8888);
                break;
            case 103:
            case 206:
                LogUtil.d("EZUIPlayer", "MSG_REALPLAY_PLAY_FAIL");
                this.removeMessages(8888);
                EZUIPlayer.this.dismissomLoading();
                if(EZUIPlayer.this.mStatus != 2) {
                    e = "UE105";
                    if(msg.obj == null) {
                        ;
                    }

                    switch(((ErrorInfo)msg.obj).errorCode) {
                    case 110018:
                        e = "UE002";
                        break;
                    case 120001:
                        e = "UE004";
                        break;
                    case 120002:
                        e = "UE005";
                        break;
                    case 380045:
                    case 395410:
                        e = "UE101";
                        break;
                    case 395546:
                        e = "UE109";
                        break;
                    case 400002:
                        e = "UE006";
                        break;
                    case 400032:
                        e = "UE107";
                        break;
                    case 400034:
                        e = "UE103";
                        break;
                    case 400035:
                    case 400036:
                        e = "UE104";
                        break;
                    case 400901:
                        e = "UE102";
                        break;
                    case 400902:
                        e = "UE001";
                        break;
                    case 400903:
                        e = "UE106";
                        break;
                    case 410034:
                    default:
                        e = "UE105";
                    }

                    EZUIPlayer.this.stopPlay();
                    EZUIPlayer.this.mEzUIPlayerCallBack.onPlayFail(new EZUIError(e, ((ErrorInfo)msg.obj).errorCode));
                    EZUIPlayer.this.showPlayError(e + "(" + ((ErrorInfo)msg.obj).errorCode + ")");
                }
                break;
            case 134:
                LogUtil.d("EZUIPlayer", "MSG_VIDEO_SIZE_CHANGED");
                EZUIPlayer.this.dismissomLoading();

                try {
                    e = (String)msg.obj;
                    String[] strings = e.split(":");
                    EZUIPlayer.this.mEzUIPlayerCallBack.onVideoSizeChange(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
                    EZUIPlayer.this.mVideoWidth = Integer.parseInt(strings[0]);
                    EZUIPlayer.this.mVideoHeight = Integer.parseInt(strings[1]);
                    EZUIPlayer.this.setSurfaceSize(EZUIPlayer.this.mWidth, EZUIPlayer.this.mHeight);
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
                break;
            case 201:
                LogUtil.d("EZUIPlayer", "MSG_REMOTEPLAYBACK_PLAY_FINISH");
                EZUIPlayer.this.handlePlayFinish();
                break;
            case 8888:
                this.removeMessages(8888);
                if(EZUIPlayer.this.mEzUIPlayerCallBack != null && EZUIPlayer.this.mStatus == 3) {
                    EZUIPlayer.this.mSeekCalendar = EZUIPlayer.this.getOSDTime();
                    if(EZUIPlayer.this.mSeekCalendar != null) {
                        EZUIPlayer.this.mEzUIPlayerCallBack.onPlayTime((Calendar)EZUIPlayer.this.mSeekCalendar.clone());
                    }

                    this.sendEmptyMessageDelayed(8888, 1000L);
                }
            }

        }
    };

    private boolean mAutoPlay = false;
    public void setAutoPlay(boolean autoPlay){
        mAutoPlay = autoPlay;
    }

    public void setOpenSound(boolean openSound) {
        this.isOpenSound = openSound;
        this.optionSound();
    }

    public boolean isOpenSound() {
        return this.isOpenSound;
    }

    private void optionSound() {
        if(this.mEZPlayer != null) {
            if(this.isOpenSound()) {
                this.mEZPlayer.openSound();
            } else {
                this.mEZPlayer.closeSound();
            }
        }

    }

    public EZUIPlayer(Context context) {
        super(context);
        this.mContext = context;
        this.initSurfaceView();
    }

    public EZUIPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.initSurfaceView();
    }

    public EZUIPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.initSurfaceView();
    }

    private void initSurfaceView() {
        if(this.mSurfaceView == null) {
            this.mSurfaceView = new SurfaceView(this.mContext);
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.addRule(13);
            this.mSurfaceView.setLayoutParams(lp);
            this.addView(this.mSurfaceView);
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mDefaultWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.mDefaultHeight = MeasureSpec.getSize(heightMeasureSpec);
        android.view.ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        if(layoutParams.height == -2) {
            this.mDefaultHeight = (int)((double)this.mDefaultWidth * 0.562D);
        }

        LogUtil.d("EZUIPlayer", "onMeasure  mDefaultWidth = " + this.mDefaultWidth + "  mDefaultHeight= " + this.mDefaultHeight);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.mDefaultWidth, MeasureSpec.getMode(widthMeasureSpec));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mDefaultHeight, MeasureSpec.getMode(heightMeasureSpec));
        if(this.mSurfaceView != null && this.mHolder == null) {
            this.mHolder = this.mSurfaceView.getHolder();
            this.mHolder.addCallback(new Callback() {
                public void surfaceCreated(SurfaceHolder holder) {
                    if(EZUIPlayer.this.mEZPlayer != null) {
                        EZUIPlayer.this.mEZPlayer.setSurfaceHold(holder);
                        EZUIPlayer.this.mEZPlayer.setHandler(EZUIPlayer.this.mHandler);
                    }

                    EZUIPlayer.this.mHolder = holder;
                    if(!EZUIPlayer.this.isSurfaceInit.getAndSet(true) && mAutoPlay) {
                        EZUIPlayer.this.startPlay();
                    }

                }

                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                public void surfaceDestroyed(SurfaceHolder holder) {
                    if(EZUIPlayer.this.mEZPlayer != null) {
                        EZUIPlayer.this.mEZPlayer.setSurfaceHold((SurfaceHolder)null);
                    }

                }
            });
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public EZPlayer getEZUIPlayer(){
        return mEZPlayer;
    }

    public void setCallBack(EZUIPlayer.EZUIPlayerCallBack callBack) {
        this.mEzUIPlayerCallBack = callBack;
    }

    /** @deprecated */
    @Deprecated
    public void setPlayParams(String url, EZUIPlayer.EZUIPlayerCallBack callBack) {
        this.setCallBack(callBack);
        this.setUrl(url);
    }

    public void setUrl(String url) {
        if(this.mEZPlayer != null) {
            this.mEZPlayer.release();
            this.mEZPlayer = null;
            if(this.mRecordFiles != null) {
                this.mRecordFiles.clear();
            }
        }

        if(TextUtils.isEmpty(url)) {
            LogUtil.d("EZUIPlayer", "playUrl is null");
            if(this.mEzUIPlayerCallBack != null) {
                this.mEzUIPlayerCallBack.onPlayFail(new EZUIError("UE006", -1));
                this.showPlayError("UE006(-1)");
            }

        } else {
            String playUrl = url.trim();
            this.setStatus(0);

            try {
                this.ezPlayURLParams = EZUIKitUilts.analyzeParam(playUrl.trim());
            } catch (ParamException var4) {
                var4.printStackTrace();
                if(this.mEzUIPlayerCallBack != null) {
                    this.mEzUIPlayerCallBack.onPlayFail(new EZUIError(var4.getErrorString(), -1));
                    this.showPlayError(var4.getErrorString() + "(-1)");
                }

                return;
            }

            if(this.ezPlayURLParams == null) {
                if(this.mEzUIPlayerCallBack != null) {
                    this.mEzUIPlayerCallBack.onPlayFail(new EZUIError("UE007", -1));
                    this.showPlayError("UE007(-1)");
                }

            } else {
                this.isOpenSound = !this.ezPlayURLParams.mute;
                if(this.ezPlayURLParams.type == 1) {
                    this.isPlayBack = false;
                    if(this.mEZPlayer == null) {
                        this.setStatus(0);
                        this.mEZPlayer = EzvizAPI.getInstance().createPlayerWithUrl(url);
                        if(!TextUtils.isEmpty(this.ezPlayURLParams.verifyCode)) {
                            this.mEZPlayer.setPlayVerifyCode(this.ezPlayURLParams.verifyCode);
                        }

                        this.mEZPlayer.setHandler(this.mHandler);
                        if(this.mHolder != null) {
                            this.mEZPlayer.setSurfaceHold(this.mHolder);
                        }

                        if(this.mEzUIPlayerCallBack != null) {
                            this.mEzUIPlayerCallBack.onPrepared();
                        }
                    }

                } else {
                    if(this.ezPlayURLParams.type == 2) {
                        this.isPlayBack = true;
                        if(this.ezPlayURLParams.startTime == null) {
                            this.ezPlayURLParams.startTime = Calendar.getInstance();
                            this.ezPlayURLParams.startTime.set(HOUR_OF_DAY, 0);
                            this.ezPlayURLParams.startTime.set(MINUTE, 0);
                            this.ezPlayURLParams.startTime.set(SECOND, 0);
                        }

                        if(this.ezPlayURLParams.endTime == null) {
                            this.ezPlayURLParams.endTime = (Calendar)this.ezPlayURLParams.startTime.clone();
                            this.ezPlayURLParams.endTime.set(DAY_OF_MONTH, this.ezPlayURLParams.startTime.get(DAY_OF_MONTH) + 1);
                            this.ezPlayURLParams.endTime.set(HOUR_OF_DAY, 0);
                            this.ezPlayURLParams.endTime.set(MINUTE, 0);
                            this.ezPlayURLParams.endTime.set(SECOND, 0);
                        }

                        if(this.ezPlayURLParams.startTime != null && this.ezPlayURLParams.endTime != null && this.ezPlayURLParams.startTime.after(this.ezPlayURLParams.endTime)) {
                            if(this.mEzUIPlayerCallBack != null) {
                                this.mEzUIPlayerCallBack.onPlayFail(new EZUIError("UE006", -1));
                                this.showPlayError("UE006(-1)");
                            }

                            return;
                        }

                        if(this.mEZPlayer == null) {
                            this.mEZPlayer = EzvizAPI.getInstance().createPlayer(this.ezPlayURLParams.deviceSerial, this.ezPlayURLParams.cameraNo);
                            if(!TextUtils.isEmpty(this.ezPlayURLParams.verifyCode)) {
                                this.mEZPlayer.setPlayVerifyCode(this.ezPlayURLParams.verifyCode);
                            }

                            this.mEZPlayer.setHandler(this.mHandler);
                            if(this.mHolder != null) {
                                this.mEZPlayer.setSurfaceHold(this.mHolder);
                            }

                            this.showLoading();
                            (new Thread(new Runnable() {
                                public void run() {
                                    if(!TextUtils.isEmpty(EZUIPlayer.this.ezPlayURLParams.alarmId)) {
                                        EZUIPlayer.this.searchRecordFilesByAlarmId(EZUIPlayer.this.ezPlayURLParams.deviceSerial, EZUIPlayer.this.ezPlayURLParams.cameraNo, EZUIPlayer.this.ezPlayURLParams.alarmId);
                                    } else {
                                        searchDeviceFilesByTime( EZUIPlayer.this.ezPlayURLParams.deviceSerial, EZUIPlayer.this.ezPlayURLParams.cameraNo, EZUIPlayer.this.ezPlayURLParams.startTime.getTimeInMillis(), EZUIPlayer.this.ezPlayURLParams.endTime.getTimeInMillis(), EZUIPlayer.this.ezPlayURLParams.recodeType);
                                    }

                                }
                            })).start();
                        }
                    }

                }
            }
        }
    }

    public List<EZRecordFile> getPlayList() {
        return this.mRecordFiles;
    }

    private List<EZRecordFile> searchDeviceFilesByTime(String deviceSerial, int cameraNo, long startTime, long endTime, int recordtype) {
        try {
            ArrayList e = (ArrayList)EzvizAPI.getInstance().searchRecordFilesByTime(deviceSerial, cameraNo, startTime, endTime, recordtype);
            this.mPlayRecordList = new ArrayList();
            EZRecordFile var13 = null;
            if(this.mRecordFiles != null) {
                this.mRecordFiles.clear();
            } else {
                this.mRecordFiles = new ArrayList();
            }

            if(this.mPlayRecordList != null) {
                this.mPlayRecordList.clear();
            } else {
                this.mPlayRecordList = new ArrayList();
            }

            if(e != null && e.size() > 0) {
                this.mRecordFiles.addAll(e);
            }

            for(int var12 = 0; var12 < this.mRecordFiles.size(); ++var12) {
                if(var12 == 0) {
                    var13 = this.copyEZRecordFile((EZRecordFile)this.mRecordFiles.get(var12));
                } else {
                    if(var13.getRecType() == ((EZRecordFile)this.mRecordFiles.get(var12)).getRecType() && var13.getRecType() == 2) {
                        var13.setEndTime(((EZRecordFile)this.mRecordFiles.get(var12)).getEndTime());
                    } else {
                        this.mPlayRecordList.add(var13);
                        var13 = this.copyEZRecordFile((EZRecordFile)this.mRecordFiles.get(var12));
                    }

                    if(var12 == this.mRecordFiles.size() - 1) {
                        this.mPlayRecordList.add(var13);
                    }
                }
            }

            this.post(new Runnable() {
                public void run() {
                    if((EZUIPlayer.this.mRecordFiles == null || EZUIPlayer.this.mRecordFiles.size() <= 0) && EZUIPlayer.this.mEzUIPlayerCallBack != null) {
                        EZUIPlayer.this.mEzUIPlayerCallBack.onPlayFail(new EZUIError("UE108", -1));
                        EZUIPlayer.this.showPlayError("UE108(-1)");
                    }

                    if(EZUIPlayer.this.mEzUIPlayerCallBack != null) {
                        EZUIPlayer.this.mEzUIPlayerCallBack.onPrepared();
                    }

                }
            });
        } catch (BaseException var11) {
            Message message = this.mHandler.obtainMessage();
            message.what = 206;
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, var11.getErrorCode());
            message.arg1 = var11.getErrorCode();
            message.obj = errorInfo;
            this.mHandler.sendMessage(message);
            var11.printStackTrace();
        }

        return null;
    }

    private List<EZRecordFile> searchRecordFilesByAlarmId(String deviceSerial, int cameraNo, String alarmId) {
        try {
            EZRecordFile e = EzvizAPI.getInstance().searchRecordFileByAlarmId(deviceSerial, cameraNo, alarmId);
            if(this.mRecordFiles != null) {
                this.mRecordFiles.clear();
            } else {
                this.mRecordFiles = new ArrayList();
            }

            if(this.mPlayRecordList != null) {
                this.mPlayRecordList.clear();
            } else {
                this.mPlayRecordList = new ArrayList();
            }

            if(e != null) {
                this.mRecordFiles.add(e);
                this.mPlayRecordList.add(e);
            }

            this.post(new Runnable() {
                public void run() {
                    if((EZUIPlayer.this.mRecordFiles == null || EZUIPlayer.this.mRecordFiles.size() <= 0) && EZUIPlayer.this.mEzUIPlayerCallBack != null) {
                        EZUIPlayer.this.mEzUIPlayerCallBack.onPlayFail(new EZUIError("UE108", -1));
                        EZUIPlayer.this.showPlayError("UE108(-1)");
                    }

                    if(EZUIPlayer.this.mEzUIPlayerCallBack != null) {
                        EZUIPlayer.this.mEzUIPlayerCallBack.onPrepared();
                    }

                }
            });
            return this.mRecordFiles;
        } catch (BaseException var7) {
            var7.printStackTrace();
            Message message = this.mHandler.obtainMessage();
            message.what = 206;
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, var7.getErrorCode());
            message.arg1 = var7.getErrorCode();
            message.obj = errorInfo;
            this.mHandler.sendMessage(message);
            var7.printStackTrace();
            return null;
        }
    }

    private EZRecordFile copyEZRecordFile(EZRecordFile recordfile) {
        EZRecordFile recordFile = new EZRecordFile();
        recordFile.setEndTime(recordfile.getEndTime());
        recordFile.setRecType(recordfile.getRecType());
        recordFile.setCoverPic(recordfile.getCoverPic());
        recordFile.setFileId(recordfile.getFileId());
        recordFile.setDownloadPath(recordfile.getDownloadPath());
        recordFile.setEncryption(recordfile.getEncryption());
        recordFile.setStartTime(recordfile.getStartTime());
        return recordFile;
    }

    public int getStatus() {
        return this.mStatus;
    }

    private void setStatus(int status) {
        LogUtil.d("EZUIPlayer", "setStatus = " + status);
        this.mStatus = status;
    }

    public void startPlay() {
        if(!this.isPlayBack || this.mPlayRecordList != null && this.mPlayRecordList.size() != 0 && this.mCurrentIndex < this.mPlayRecordList.size()) {
            if(this.mStatus != 1 && this.mStatus != 3) {
                if(this.mEZPlayer == null) {
                    LogUtil.d("EZUIPlayer", "EZPlayer is null ,you can transfer createUIPlayer function");
                } else {
                    if(this.mEZPlayer != null && this.isSurfaceInit.get()) {
                        this.showLoading();
                        this.setStatus(1);
                        if(this.isPlayBack) {
                            EZRecordFile recordfile = (EZRecordFile)this.mPlayRecordList.get(this.mCurrentIndex);
                            Calendar start = Calendar.getInstance();
                            Calendar end = Calendar.getInstance();
                            start.setTimeInMillis(recordfile.getStartTime());
                            end.setTimeInMillis(recordfile.getEndTime());
                            if(recordfile.getRecType() == 1) {
                                EZCloudRecordFile e = new EZCloudRecordFile();
                                e.setDownloadPath(recordfile.getDownloadPath());
                                e.setEncryption(recordfile.getEncryption());
                                if(start.before(this.mSeekCalendar)) {
                                    start = (Calendar)this.mSeekCalendar.clone();
                                }

                                e.setStartTime(start);
                                e.setStopTime(end);
                                this.mEZPlayer.startPlayback(e);
                            } else if(recordfile.getRecType() == 2) {
                                if(start.before(this.mSeekCalendar)) {
                                    start = (Calendar)this.mSeekCalendar.clone();
                                }

                                this.mEZPlayer.startPlayback(start, end);
                            }
                        } else {
                            this.mEZPlayer.startRealPlay();
                        }
                    }

                }
            } else {
                LogUtil.d("EZUIPlayer", "status is start or play");
            }
        }
    }

    public void seekPlayback(Calendar calendar) {
        if(calendar != null) {
            Log.d("EZUIPlayer", "seekPlayback  = " + calendar.getTime().toString());
            if(this.mPlayRecordList != null && this.mPlayRecordList.size() > 0) {
                int index = this.getCurrentIndex(calendar);
                this.stopPlay();
                if(index < 0) {
                    this.dismissomLoading();
                    if(this.mEzUIPlayerCallBack != null) {
                        this.mEzUIPlayerCallBack.onPlayFinish();
                    }

                } else {
                    this.mSeekCalendar = (Calendar)calendar.clone();
                    this.mCurrentIndex = index;
                    this.startPlay();
                }
            }
        }
    }

    public Calendar getOSDTime() {
        return this.mEZPlayer != null?this.mEZPlayer.getOSDTime():null;
    }

    public void stopPlay() {
        this.mHandler.removeMessages(8888);
        if(this.mStatus != 2) {
            this.setStatus(2);
            if(this.mEZPlayer != null) {
                if(this.isPlayBack) {
                    this.mEZPlayer.stopPlayback();
                } else {
                    this.mEZPlayer.stopRealPlay();
                }
            }
        }

    }

    public void resumePlay() {
        if(!this.isPlayBack) {
            LogUtil.d("EZUIPlayer", "this is playback method");
        } else if(!this.isPlayBack || this.mPlayRecordList != null && this.mPlayRecordList.size() != 0 && this.mCurrentIndex < this.mPlayRecordList.size()) {
            if(this.mStatus != 1 && this.mStatus != 3) {
                if(this.mEZPlayer == null) {
                    LogUtil.d("EZUIPlayer", "EZPlayer is null ,you can transfer createUIPlayer function");
                } else {
                    LogUtil.debugLog("EZUIPlayer", "resumeRealPlay");
                    this.mHandler.sendEmptyMessage(8888);
                    this.mStatus = 3;
                    this.mEZPlayer.resumePlayback();
                }
            } else {
                LogUtil.d("EZUIPlayer", "status is start or play");
            }
        }
    }

    public void pausePlay() {
        LogUtil.debugLog("EZUIPlayer", "pausePlay");
        this.mHandler.removeMessages(8888);
        this.mStatus = 4;
        if(this.mEZPlayer != null) {
            this.mEZPlayer.pausePlayback();
        }

    }

    public void releasePlayer() {
        this.mHandler.removeMessages(8888);
        if(this.mEZPlayer != null) {
            this.mEZPlayer.release();
            this.mEZPlayer = null;
        }

    }

    private void handlePlayFinish() {
        this.mHandler.removeMessages(8888);
        ++this.mCurrentIndex;
        this.stopPlay();
        if(this.mCurrentIndex >= this.mPlayRecordList.size()) {
            if(this.mEzUIPlayerCallBack != null) {
                this.mEzUIPlayerCallBack.onPlayFinish();
            }
        } else {
            this.startPlay();
        }

    }

    public void setSurfaceSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        android.view.ViewGroup.LayoutParams lp = this.getLayoutParams();
        if(lp == null) {
            lp = new android.view.ViewGroup.LayoutParams(width, height);
        } else if(this.mWidth == 0 && this.mHeight == 0) {
            this.mWidth = lp.width;
            this.mHeight = lp.height;
            if(lp.width == -1) {
                this.mWidth = this.getMeasuredWidth();
            }

            if(lp.height == -1) {
                this.mHeight = this.getMeasuredHeight();
            }
        } else {
            lp.width = width;
            lp.height = height;
        }

        Log.d("EZUIPlayer", "setSurfaceSize  mWidth = " + this.mWidth + "  mHeight = " + this.mHeight);
        if(width == 0 && height != 0) {
            if(this.mVideoHeight != 0 && this.mVideoWidth != 0) {
                lp.width = (int)((float)(height * this.mVideoWidth) * 1.0F / (float)this.mVideoWidth);
            } else {
                lp.width = (int)((double)height * 1.1778D);
            }
        }

        if(width != 0 && height == 0) {
            if(this.mVideoHeight != 0 && this.mVideoWidth != 0) {
                lp.height = (int)((float)(width * this.mVideoHeight) * 1.0F / (float)this.mVideoWidth);
            } else {
                lp.height = (int)((double)width * 0.562D);
            }
        }

        this.setLayoutParams(lp);
        this.changeSurfaceSize(this.mSurfaceView, this.mVideoWidth, this.mVideoHeight);
    }

    private void showPlayError(String errorInfo) {
        if(this.mLoadView != null) {
            this.mLoadView.setVisibility(GONE);
        }

        if(this.mErrorTextView == null) {
            this.mErrorTextView = new TextView(this.mContext);
            this.mErrorTextView.setText(errorInfo + "\n" + "点击重新加载" );
            this.mErrorTextView.setTextColor(Color.rgb(255, 255, 255));
            mErrorTextView.setGravity(Gravity.CENTER);
            this.mErrorTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                       if (mEzUIPlayerCallBack!=null)
                           mEzUIPlayerCallBack.onRetryLoad();
                }
            });
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.addRule(13);

            this.mErrorTextView.setLayoutParams(lp);
            this.addView(this.mErrorTextView);
        }

        this.mErrorTextView.setVisibility(VISIBLE);
    }

    public void setLoadingView(View view) {
        this.mLoadView = view;
    }

    private void showLoading() {
        if(this.mErrorTextView != null) {
            this.mErrorTextView.setVisibility(GONE);
        }

        if(this.mLoadView != null) {
            if(this.mLoadView.getParent() == null) {
                this.addView(this.mLoadView);
            }

            this.mLoadView.setVisibility(VISIBLE);
        } else {
            this.mLoadView = new ProgressBar(this.mContext);
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.addRule(13);
            this.mLoadView.setLayoutParams(lp);
            this.addView(this.mLoadView);
            this.mLoadView.setVisibility(VISIBLE);
        }

    }

    private void dismissomLoading() {
        if(this.mLoadView != null) {
            this.mLoadView.setVisibility(GONE);
        }

    }

    private Point getSurfaceSize(SurfaceView surface, int videoWidth, int videoHeight) {
        Point pt = null;
        if(surface == null) {
            return pt;
        } else {
            if(videoWidth == 0 || videoHeight == 0) {
                videoWidth = 16;
                videoHeight = 9;
            }

            int sw = this.mWidth;
            int sh = this.mHeight;
            LogUtil.d("EZUIPlayer", "sw =  " + sw + "  sh = " + sh);
            double dw = (double)sw;
            double dh = (double)sh;
            if(dw * dh != 0.0D && videoWidth * videoHeight != 0) {
                double vw = (double)videoWidth;
                double ar = (double)videoWidth / (double)videoHeight;
                double dar = dw / dh;
                if(dar < ar) {
                    dh = dw / ar;
                } else {
                    dw = dh * ar;
                }

                int w = (int)Math.ceil(dw * (double)videoWidth / (double)videoWidth);
                int h = (int)Math.ceil(dh * (double)videoHeight / (double)videoHeight);
                pt = new Point(w, h);
                Log.d("EZUIPlayer", "Point w=  " + w + "  h = " + h);
                return pt;
            } else {
                return pt;
            }
        }
    }

    private void changeSurfaceSize(SurfaceView surface, int videoWidth, int videoHeight) {
        if(surface != null) {
            SurfaceHolder holder = surface.getHolder();
            Point size = this.getSurfaceSize(surface, videoWidth, videoHeight);
            if(size != null) {
                holder.setFixedSize(videoWidth, videoHeight);
                android.view.ViewGroup.LayoutParams lp = surface.getLayoutParams();
                int oldH = lp.height;
                lp.width = size.x;
                lp.height = size.y;
                Log.d("EZUIPlayer", "changeSurfaceSize  width =  " + lp.width + "  height = " + lp.height);
                surface.setLayoutParams(lp);
            }
        }
    }

    private int getCurrentIndex(Calendar calendar) {
        if(this.mPlayRecordList != null && this.mPlayRecordList.size() > 0) {
            for(int i = 0; i < this.mPlayRecordList.size(); ++i) {
                EZRecordFile recordFile = (EZRecordFile)this.mPlayRecordList.get(i);
                boolean var10000 = calendar.getTimeInMillis() > recordFile.getEndTime()?true:true;
                if(calendar.getTimeInMillis() < recordFile.getEndTime()) {
                    return i;
                }
            }

            return -1;
        } else {
            return -1;
        }
    }

    public static EZUIPlayer.EZUIKitPlayMode getUrlPlayType(String url) {
        return EZUIKitUilts.getUrlPlayType(url);
    }

    public interface EZUIPlayerCallBack {

        void onShowLoading();

        void onPlaySuccess();

        void onPlayFail(EZUIError var1);

        void onVideoSizeChange(int var1, int var2);

        void onPrepared();

        void onPlayTime(Calendar var1);

        void onPlayFinish();

        void onRetryLoad();
    }

    public static enum EZUIKitPlayMode {
        EZUIKIT_PLAYMODE_LIVE,
        EZUIKIT_PLAYMODE_REC,
        EZUIKIT_PLAYMODE_UNKOWN;

        private EZUIKitPlayMode() {
        }
    }
}
