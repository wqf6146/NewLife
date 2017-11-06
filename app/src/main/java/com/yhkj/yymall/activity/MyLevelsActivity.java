package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.RankBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/10.
 */

public class MyLevelsActivity extends BaseActivity {

    @Bind(R.id.am_progressbar)
    ProgressBar mProgressBar;

    @Bind(R.id.am_tv_howlevelup)
    TextView mTvLevelUp;

    @Bind(R.id.am_img_back)
    ImageView mImgBack;

    @Bind(R.id.am_img_avatar)
    ImageView mImgAvatar;

    @Bind(R.id.am_tv_expreience)
    TextView mTvExpreience;

    @Bind(R.id.am_tv_word)
    TextView mTvWord;

    @Bind(R.id.am_img_rank1)
    ImageView mImgRank1;

    @Bind(R.id.am_img_rank2)
    ImageView mImgRank2;

    @Bind(R.id.am_img_rank3)
    ImageView mImgRank3;

    @Bind(R.id.am_tv_rank1)
    TextView mTvRank1;

    @Bind(R.id.am_tv_rank2)
    TextView mTvRank2;

    @Bind(R.id.am_tv_rank3)
    TextView mTvRank3;

    ImageView[] mArrImgRank = new ImageView[3];
    TextView[] mArrTvRank = new TextView[3];

    @Bind(R.id.am_tv_privildege_title_1)
    TextView mTvPrivildegeTitle_1;

    @Bind(R.id.am_tv_privildege_tag_1)
    TextView mTvPrivildegeTag_1;

    @Bind(R.id.am_tv_privildege_tag_2)
    TextView mTvPrivildegeTag_2;

    @Bind(R.id.am_tv_privildege_content_1)
    TextView mTvPrivildegeContent_1;

    @Bind(R.id.am_tv_privildege_content_2)
    TextView mTvPrivildegeContent_2;

    @Bind(R.id.am_tv_privildege_title_2)
    TextView mTvPrivildegeTitle_2;

    @Bind(R.id.am_rl_privildege_2)
    RelativeLayout mRlPrivildege_2;

    @Bind(R.id.am_tv_allprivaledge)
    TextView mAllPrivaledge;

    @Bind(R.id.am_rl_privildege_1)
    RelativeLayout mRlPrivildege_1;

    @Bind(R.id.am_img_mylevel)
    ImageView mImgMyLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylevels);
    }

    @Override
    protected void initView() {
        setStatusViewVisiable(false);
    }

    @Override
    protected void bindEvent() {

        mAllPrivaledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mDesc))
                    return;
                Intent intent = new Intent(MyLevelsActivity.this,PrivilegeActivity.class);
                intent.putExtra("exp",mCurEx);
                intent.putExtra("desc",mDesc);
                intent.putExtra("level",mLevel);
                startActivity(intent);
            }
        });

        mTvLevelUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mDesc))
                    return;
                Intent intent = new Intent(MyLevelsActivity.this,PrivilegeActivity.class);
                intent.putExtra("exp",mCurEx);
                intent.putExtra("desc",mDesc);
                intent.putExtra("level",mLevel);
                intent.putExtra("index",1);
                startActivity(intent);
            }
        });

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(MyLevelsActivity.this);
            }
        });

    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        YYMallApi.getMyExperience(this, new YYMallApi.ApiResult<RankBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(RankBean.DataBean dataBean) {
                setData(dataBean);
            }
        });
    }

    private int mIntegral,mCurIntegra = 0,mInterval;
    private Handler mHandler = new Handler();
    @Override
    protected void initData() {

        mArrImgRank[0] = mImgRank1;
        mArrImgRank[1] = mImgRank2;
        mArrImgRank[2] = mImgRank3;
        mArrTvRank[0] = mTvRank1;
        mArrTvRank[1] = mTvRank2;
        mArrTvRank[2] = mTvRank3;
        // 15 50 85

    }

    private int[] mArrYellowLevelImg = new int[]{
            R.mipmap.ic_nor_mv0,
            R.mipmap.ic_nor_mv1,
            R.mipmap.ic_nor_mv2,
            R.mipmap.ic_nor_mv3,
            R.mipmap.ic_nor_mv4,
            R.mipmap.ic_nor_mv5,
            R.mipmap.ic_nor_mv6,
            R.mipmap.ic_nor_mv7,
    };

    private int[] mArrWhiteLevelImg = new int[]{
            R.mipmap.ic_nor_bv0,
            R.mipmap.ic_nor_bv1,
            R.mipmap.ic_nor_bv2,
            R.mipmap.ic_nor_bv3,
            R.mipmap.ic_nor_bv4,
            R.mipmap.ic_nor_bv5,
            R.mipmap.ic_nor_bv6,
            R.mipmap.ic_nor_bv7,
    };

    private int mLevel, mNextExPercent,mCurEx,mCurMinEx;
    private String mDesc;
    private void setData(RankBean.DataBean dataBean) {
        Glide.with(this).load(dataBean.getHead().getHead_ico()).asBitmap().centerCrop().placeholder(R.mipmap.ic_nor_srcheadimg)
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(mImgAvatar) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mImgAvatar.setImageDrawable(circularBitmapDrawable);
            }
        });
        mCurEx = dataBean.getHead().getNum();
        mTvExpreience.setText("经验值：" + dataBean.getHead().getNum());
        mDesc = dataBean.getHead().getWord();
        if (!TextUtils.isEmpty(dataBean.getHead().getWord()))
            mTvWord.setText(dataBean.getHead().getWord());
        else
            mTvWord.setVisibility(GONE);

        mLevel = Integer.parseInt(dataBean.getHead().getLv().substring(dataBean.getHead().getLv().length()-1));

        for (int i = 0; i<dataBean.getBody().size();i++){
            RankBean.DataBean.BodyBean bodyBean = dataBean.getBody().get(i);
            if (TextUtils.isEmpty(bodyBean.getGroup_name()) || bodyBean.getGroup_name().equals("0")){
                mArrImgRank[i].setVisibility(View.INVISIBLE);
                mArrTvRank[i].setVisibility(View.INVISIBLE);
            }else{
                mArrTvRank[i].setText("经验值"+ bodyBean.getMinexp() + "分");
                int level = Integer.parseInt(bodyBean.getGroup_name().substring(bodyBean.getGroup_name().length()-1));
                if (level == mLevel) {
                    mArrImgRank[i].setImageResource(mArrYellowLevelImg[level]);
                    mImgMyLevel.setImageResource(mArrYellowLevelImg[level]);
                    mNextExPercent = bodyBean.getMaxexp();
                    mCurMinEx = bodyBean.getMinexp();
                }else{
                    mArrImgRank[i].setImageResource(mArrWhiteLevelImg[level]);
                }
                mArrImgRank[i].setVisibility(View.VISIBLE);
            }
        }
        // 50
        mIntegral = getPercent(mLevel == 7);
        Log.e("percent",mIntegral+"");
        mInterval = mIntegral == 0 ? 0 : 1000/mIntegral;
        new ProgressThread().start();


        List<RankBean.DataBean.FootBean> footBeen = dataBean.getFoot();
        if (footBeen!=null){
            int i=0;
            for (i=0; i<footBeen.size(); i++){
                if (i == 0) {
                    mRlPrivildege_1.setVisibility(View.VISIBLE);
                    RankBean.DataBean.FootBean bean = footBeen.get(i);
                    mTvPrivildegeTitle_1.setText(bean.getTitle());
                    mTvPrivildegeTag_1.setText(bean.getTag());
                    mTvPrivildegeContent_1.setText(bean.getDes());
                }else if (i == 1){
                    mRlPrivildege_2.setVisibility(View.VISIBLE);
                    RankBean.DataBean.FootBean bean = footBeen.get(i);
                    mTvPrivildegeTitle_2.setText(bean.getTitle());
                    mTvPrivildegeTag_2.setText(bean.getTag());
                    mTvPrivildegeContent_2.setText(bean.getDes());
                }
            }
            if (i == 1){
                mRlPrivildege_2.setVisibility(GONE);
            }
        }
    }

    private int getPercent(boolean leftOrRight){
        float curPercent = (float)(mCurEx - mCurMinEx)/(mNextExPercent - mCurMinEx);
        if (leftOrRight){
            return Math.round(35 * 1) + 15;
        }else{
            return Math.round(35 * curPercent) + 50;
        }
    }

    class ProgressThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (mCurIntegra < mIntegral){
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(mCurIntegra);
                    }
                });
                mCurIntegra++;
                try{
                    if (mIntegral != 0)
                        sleep(mInterval);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

