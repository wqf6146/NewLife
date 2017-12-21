package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.videogo.openapi.EZConstants;
import com.videogo.realplay.RealPlayStatus;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/12/21.
 */

public class VideoControlPopupView extends BasePopupWindow {

    @Bind(R.id.vvc_rl_topclick)
    RelativeLayout mRlTopClick;

    @Bind(R.id.vvc_rl_leftclick)
    RelativeLayout mRlLeftClick;

    @Bind(R.id.vvc_rl_rightclick)
    RelativeLayout mRlRightClick;

    @Bind(R.id.vvc_rl_bottomclick)
    RelativeLayout mRlBottomClick;

    @Bind(R.id.vvc_tv_lasttime)
    TextView mTvLastTime;

    public VideoControlPopupView(Activity context){
        super(context);
        init();
    }

    private void init() {
        mRlTopClick.setOnTouchListener(mOnTouchListener);
        mRlLeftClick.setOnTouchListener(mOnTouchListener);
        mRlRightClick.setOnTouchListener(mOnTouchListener);
        mRlBottomClick.setOnTouchListener(mOnTouchListener);
    }

    public void setLastTime(int time){
        mTvLastTime.setText("剩余控制时间：" + CommonUtil.secToMills(time));
        mHandler.sendEmptyMessage(time-1);
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (!isShowing()) return false;
            if (msg.what > 0){
                mTvLastTime.setText("剩余控制时间 " + CommonUtil.secToMills(msg.what - 1));
                mHandler.sendEmptyMessageDelayed(msg.what-1,1000l);
            }else{
                //超过控制时间
                Toast.makeText(getContext(),"控制时间已到，请重新申请控制。",Toast.LENGTH_SHORT).show();
                if (mOnVideoControl!=null)
                    mOnVideoControl.onControlEnd();
                dismiss();
            }
            return false;
        }
    });

    @Override
    public View initAnimaView() {
        return findViewById(R.id.vps_rl_popview);
    }

    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(500 * 2, 0, 300);
        return animation;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation animation = getTranslateAnimation(0,500 * 2, 300);
        return animation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_video_control);
        ButterKnife.bind(this,view);
        return view;
    }

    private OnVideoControl mOnVideoControl;

    public void setOnVideoControl(OnVideoControl onVideoControl) {
        this.mOnVideoControl = onVideoControl;
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionevent) {
            int action = motionevent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    switch (view.getId()) {
                        case R.id.vvc_rl_topclick:
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionTop(true);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.vvc_rl_leftclick:
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionLeft(true);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.vvc_rl_rightclick:
//                            setPtzDirectionIv(RealPlayStatus.PTZ_LEFT);
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionRight(true);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        case R.id.vvc_rl_bottomclick:
//                            setPtzDirectionIv(RealPlayStatus.PTZ_RIGHT);
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionBottom(true);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTART);
                            break;
                        default:
                            break;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.vvc_rl_topclick:
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionTop(false);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandUp, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.vvc_rl_leftclick:
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionLeft(false);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandDown, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.vvc_rl_rightclick:
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionRight(false);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandLeft, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        case R.id.vvc_rl_bottomclick:
                            if (mOnVideoControl!=null)
                                mOnVideoControl.onActionBottom(false);
//                            ptzOption(EZConstants.EZPTZCommand.EZPTZCommandRight, EZConstants.EZPTZAction.EZPTZActionSTOP);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    public interface OnVideoControl {
        void onActionTop(boolean bDownOrUp);
        void onActionLeft(boolean bDownOrUp);
        void onActionRight(boolean bDownOrUp);
        void onActionBottom(boolean bDownOrUp);
        void onControlEnd();
    }
}
