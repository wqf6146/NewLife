package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.videogo.openapi.EZConstants;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.NormalFragmentAdapter;
import com.yhkj.yymall.bean.GoodsLikeBean;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.fragment.VideoFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.EZUIkit.EZUIPlayer;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import android.support.v4.view.ViewPager;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by Administrator on 2017/12/16.
 */

public class VideoPlayActivity extends BaseToolBarActivity {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.av_viewpager_mult)
    ViewPager mViewPagerMult;

    @Bind(R.id.av_viewpager_single)
    ViewPager mViewPagerSingle;

    @Bind(R.id.av_Rl_videoplay)
    RelativeLayout mRlVideoPlay;

    @Bind(R.id.av_tv_places)
    TextView mTvPlaces;

    @Bind(R.id.av_img_fullscreen)
    ImageView mImgFullScreen;

    @Bind(R.id.av_rl_place)
    RelativeLayout mRlPlace;

    private ViewGroup.LayoutParams mVCLayouyParams;

    private MyOrientationDetector mOrientationDetector;

    private int mOrientation = Configuration.ORIENTATION_PORTRAIT;

    private Boolean m4BoxMode = false; //四分屏

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mOrientation = newConfig.orientation;
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

    private void updateOperatorUI() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            // 显示状态栏
            fullScreen(false);
            setStatusVisiable(VISIBLE);
            setToolbarVisiable(true);
            mRlPlace.setVisibility(GONE);
            mRlVideoPlay.setLayoutParams(mVCLayouyParams);
        } else {
            // 隐藏状态栏
            fullScreen(true);
            setStatusVisiable(GONE);
            setToolbarVisiable(false);
            mRlPlace.setVisibility(VISIBLE);
            LinearLayout.LayoutParams realPlayPlayRlLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            realPlayPlayRlLp.gravity = Gravity.CENTER;
            mRlVideoPlay.setLayoutParams(realPlayPlayRlLp);
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
        super.initView();
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText(getIntent().getStringExtra("title"));
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

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrientation();
            }
        });
    }

    private List<VideoListBean.DataBean.ListBean> mListData;
    private String mToken;

    @Override
    protected void initData() {
        mListData = getIntent().getParcelableArrayListExtra("list");
        mToken = getIntent().getStringExtra("token");
        mOrientationDetector = new MyOrientationDetector(this);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        mVCLayouyParams = mRlVideoPlay.getLayoutParams();
        getGoodsData(null);
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
                    mControlView = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.view_videoandcontrol,mRecycleView,false);
                    bindControlView(mControlView);
//                    ViewPager singleViewPager = (ViewPager)view.findViewById(R.id.vv_viewpager_single);

                    mVideoFragments = new VideoFragment[mListData.size()];
                    for (int i=0; i<mListData.size();i++){
                        mVideoFragments[i] = VideoFragment.getInstance(mToken,i,mListData.get(i)).setVideoParent(new OnVideoSelect() {
                            @Override
                            public void onVideoSelect(String placeStr) {
                                mTvPlaces.setText(placeStr);
                            }

                            @Override
                            public void onVideoPlayState(int state) {
//                                if (state == EZUIPlayer.STATUS_PLAY){
//                                    mImgStartVideo.setImageResource(R.mipmap.ic_nor_videostop);
//                                }else{
//                                    mImgStartVideo.setImageResource(R.mipmap.ic_nor_startvideo);
//                                }
                            }
                        });
                    }
                    NormalFragmentAdapter fragmentAdapter = new NormalFragmentAdapter(getSupportFragmentManager(),mVideoFragments);
                    mViewPagerSingle.setAdapter(fragmentAdapter);

                    mWrapperAdapter.addHeaderView(mControlView);
                    mRecycleView.setAdapter(mWrapperAdapter);
                }
            }
        });
    }

    private View mRlFullScreen,mRlMultScreen,mRlVideoQa;
    private ImageView mImgMultScreen;
    private TextView mTvVideoQa;
    private void bindControlView(final View view) {
        mRlMultScreen = view.findViewById(R.id.vv_rl_multscreen);
        mImgMultScreen = (ImageView)mRlMultScreen.findViewById(R.id.vv_img_multscreen);
        mRlMultScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分屏
                if (m4BoxMode != null){
                    showToast("请选择具体设备");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
                builder.setTitle("选择码流");
                builder.setItems(   new String[]{"高清","标清","流畅", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mEZPlayer == null) {
                            return;
                        }
                        int tag = -1;
                        // 视频质量，2-高清，1-标清，0-流畅
                        if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET) {
                            tag = 2;
                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED) {
                            tag = 1;
                        } else if (mCameraInfo.getVideoLevel() == EZConstants.EZVideoLevel.VIDEO_LEVEL_HD) {
                            tag = 0;
                        }
                        if (tag == position)
                            return;
                        switch (position){
                            case 0:
                                //高清
                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_HD);
                                break;
                            case 1:
                                //标清
                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_BALANCED);
                                break;
                            case 2:
                                //流畅
                                setQualityMode(EZConstants.EZVideoLevel.VIDEO_LEVEL_FLUNET);
                                break;
                        }
                    }
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        });

        mRlVideoQa = view.findViewById(R.id.vv_rl_videoqa);
        mTvVideoQa = (TextView) findViewById(R.id.vv_tv_videoqa);



        mRlFullScreen = view.findViewById(R.id.vv_rl_fullscreen);
        mRlFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrientation();
            }
        });
//        mRlStartVideo = view.findViewById(R.id.vv_rl_startvideo);
//        mImgStartVideo = (ImageView)mRlStartVideo.findViewById(R.id.vv_img_startvideo);
//        mRlStartVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //播放与暂停
//                VideoFragment fragment = getInTopVideo();
//                if (fragment!=null){
//                    Boolean res = fragment.startPlay();
//                    if (res!=null){
//                        if (res){
//                            mImgStartVideo.setImageResource(R.mipmap.ic_nor_videostop);
//                        }else{
//                            mImgStartVideo.setImageResource(R.mipmap.ic_nor_startvideo);
//                        }
//                    }
//                }
//            }
//        });
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
        void onVideoSelect(String placeStr);
        void onVideoPlayState(int state);
    }
}
