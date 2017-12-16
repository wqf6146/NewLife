package com.yhkj.yymall.view.EZUIkit;

import android.view.View;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer.EZUIPlayerCallBack;
import java.util.Calendar;
import java.util.List;

interface EZUIPlayerInterface {
    void setCallBack(EZUIPlayerCallBack var1);

    void setUrl(String var1);

    int getStatus();

    void startPlay();

    void seekPlayback(Calendar var1);

    Calendar getOSDTime();

    void stopPlay();

    void pausePlay();

    void resumePlay();

    void releasePlayer();

    List getPlayList();

    void setSurfaceSize(int var1, int var2);

    void setLoadingView(View var1);
}
