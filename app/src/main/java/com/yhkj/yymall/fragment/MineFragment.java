package com.yhkj.yymall.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.EventSubscribe;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.AddressManagerActivity;
import com.yhkj.yymall.activity.AdvanceActivity;
import com.yhkj.yymall.activity.AfterMallListActivity;
import com.yhkj.yymall.activity.CollectActivity;
import com.yhkj.yymall.activity.InputCodeActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.MessageActivity;
import com.yhkj.yymall.activity.MineEvaActivity;
import com.yhkj.yymall.activity.MineGroupActivity;
import com.yhkj.yymall.activity.MineOrderActivity;
import com.yhkj.yymall.activity.MyIntegralActivity;
import com.yhkj.yymall.activity.MyLevelsActivity;
import com.yhkj.yymall.activity.MyPriceActivity;
import com.yhkj.yymall.activity.MyServiceActivity;
import com.yhkj.yymall.activity.MyYaYaActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.PledgeActivity;
import com.yhkj.yymall.activity.RegisterActivity;
import com.yhkj.yymall.activity.SetActivity;
import com.yhkj.yymall.activity.ShareCodeActivity;
import com.yhkj.yymall.activity.UserDataActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.MineBean;
import com.yhkj.yymall.bean.UnReadBean;
import com.yhkj.yymall.event.AvatarUpdateEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.BottomBarView.BottomBar;
import com.yhkj.yymall.view.BottomBarView.BottomBarTab;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.QrcodePopup;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;
import static com.yhkj.yymall.http.api.ApiService.SHARE_CODE_URL;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/6/19.
 */

public class MineFragment extends BaseFragment {

    @Bind(R.id.fm_img_code)
    ImageView mImgCode;

    @Bind(R.id.fm_img_message)
    ImageView mImgMessage;

    @Bind(R.id.fm_img_set)
    ImageView mImgSet;

//    @Bind(R.id.fm_rl_toolbar)
//    RelativeLayout mToolBar;

    @Bind(R.id.fm_xrefreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.fm_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.fm_view_status)
    View mViewStatus;

//    @Bind(R.id.fm_view_line)
//    View mViewLine;

    @Bind(R.id.fm_ll_topbar)
    LinearLayout mLlTopBar;

    private String mName;
    private String mPhone;

    public static MineFragment getInstance() {
        MineFragment fragment = new MineFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        setOnResumeRegisterBus(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @EventSubscribe
    public void avatarUpdate(AvatarUpdateEvent avatarUpdateEvent) {
        YYMallApi.getMyInfo(_mActivity, new YYMallApi.ApiResult<MineBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
//                filterHttpCallback(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(MineBean.DataBean dataBean) {
                mbLogin = Constant.LOGIN_STATUS.LOGIN;
                mHeadSubAdapter.setData(dataBean.getInfo());
                mWalletAdapter.setData(dataBean.getWallet());
                mOrderAdapter.setData(dataBean.getPending());
                YYApp.getInstance().setPhone(dataBean.getInfo().getPhone());
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    private int height = 0;// 滑动开始变色的高度
    private int overallXScroll = 0;
    private boolean mGraySet = false;

    private SubAdapter mHeadSubAdapter, mOrderAdapter, mWalletAdapter;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getData();
        if (!TextUtils.isEmpty(YYApp.getInstance().getToken())){
            YYMallApi.getUnReadMesTag(_mActivity, new YYMallApi.ApiResult<UnReadBean.DataBean>(_mActivity) {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    showToast(e.getMessage());
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(UnReadBean.DataBean dataBean) {
                    if (dataBean.getStatus()==1){
                        mImgMessage.setImageResource(R.mipmap.ic_nor_unmessage);
                    }else{
                        mImgMessage.setImageResource(R.mipmap.ic_nor_message);
                    }
                }
            });
        }else{
            mImgMessage.setImageResource(R.mipmap.ic_nor_message);
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog("正在加载，请稍后...");
                }
            });
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                }
            });
            showToast("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                }
            });
            showToast("分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                }
            });
//            showToast("取消分享");

        }
    };

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);

        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(_mActivity);
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (height == 0) {
                    height = mRecycleView.computeVerticalScrollRange() - mRecycleView.computeVerticalScrollExtent();
                }

                overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
                if (overallXScroll <= 0) {   //设置标题的背景颜色
                    mLlTopBar.setBackgroundColor(Color.argb((int) 0, 0, 124, 209));
                } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
//                    if (overallXScroll >= height / 2) {
//                        if (!mGraySet) {
//                            mGraySet = true;
//                            mViewLine.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        if (mGraySet) {
//                            mGraySet = false;
//                            mViewLine.setVisibility(View.GONE);
//
//                        }
//                    }
                    float scale = (float) overallXScroll / height;
                    float alpha = (255 * scale);
                    mLlTopBar.setBackgroundColor(Color.argb((int) alpha, 0, 124, 209));
                } else {
                    mLlTopBar.setBackgroundColor(Color.argb(255, 0, 124, 209));
                }
            }
        });
        mRecycleView.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecycleView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        final DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        mRecycleView.setAdapter(delegateAdapter);

        final List<DelegateAdapter.Adapter> adapters = new LinkedList<>();

        LinearLayoutHelper headlayouthelp = new LinearLayoutHelper();
        headlayouthelp.setMarginBottom(20);
        mHeadSubAdapter = new SubAdapter<MineBean.DataBean.InfoBean>(_mActivity, headlayouthelp, 1) {

            @Override
            public void onViewRecycled(MainViewHolder holder) {
                if (holder.itemView instanceof ViewPager) {
                    ((ViewPager) holder.itemView).setAdapter(null);
                }
            }

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater.from(_mActivity).inflate(R.layout.item_mine_head, parent, false));
            }

            @Override
            public int getItemViewType(int position) {
                return Constant.TYPE_MINE.HEAD;
            }

            @Override
            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {

            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                if (mDataBean != null) {
                    mImgCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                                showToast("请先登录");
                                startActivity(LoginActivity.class);
                                return;
                            }
                            new QrcodePopup(_mActivity,mDataBean).setCodePopCallback(new QrcodePopup.CodePopCallBack() {
                                @Override
                                public void doShare(final String code) {
                                    new ShareAction(_mActivity)
                                            .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                                    SHARE_MEDIA.QZONE)
                                            .setShareboardclickCallback(new ShareBoardlistener() {
                                                @Override
                                                public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                    String url = SHARE_CODE_URL + "#" + code;
                                                    UMWeb web = new UMWeb(url);
                                                    web.setTitle("YiYiYaYa,厂家直销专用app,更高质量，更低价格，宝宝开心，妈妈放心，我们更安心");//标题
                                                    web.setThumb( new UMImage(_mActivity, R.mipmap.ic_nor_whiteyiyiyaya));  //缩略图
                                                    web.setDescription("YiYiYaYa厂家直销各种儿童用品，玩具、童车、童装、家居等，高品质低价格，更有安全座椅0元领用，快来看看吧！");//描述
                                                    new ShareAction(_mActivity).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").setPlatform(share_media).withMedia(web).setCallback(shareListener).share();
                                                }
                                            })
                                            .open();
                                }
                            }).showPopupWindow();
                        }
                    });
                    mName = mDataBean.getName();
                    mPhone = mDataBean.getPhone();
                    holder.itemView.findViewById(R.id.imh_ll_unlogin).setVisibility(GONE);
                    holder.itemView.findViewById(R.id.imh_ll_userdata).setVisibility(View.VISIBLE);
                    ((TextView) holder.itemView.findViewById(R.id.imh_tv_username)).setText(mDataBean.getName());
                    switch (mDataBean.getLevel()) {
                        case 0:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv0);
                            break;
                        case 1:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv1);
                            break;
                        case 2:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv2);
                            break;
                        case 3:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv3);
                            break;
                        case 4:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv4);
                            break;
                        case 5:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv5);
                            break;
                        case 6:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv6);
                            break;
                        case 7:
                            ((ImageView) holder.itemView.findViewById(R.id.imh_img_mylevel)).setImageResource(R.mipmap.ic_nor_mv7);
                            break;
                    }
                    ((TextView) holder.itemView.findViewById(R.id.imh_tv_mylevel)).setText(String.format(getString(R.string.mylevels), String.valueOf(mDataBean.getLevel())));
                    final ImageView imguser = (ImageView) holder.itemView.findViewById(R.id.imh_img_user);

                    if (mDataBean.getHead_ico().equals("未设置")){
                        imguser.setImageResource(R.mipmap.ic_nor_srcheadimg);
                    }else{
                        if (imguser.getDrawable() !=null ){
                            Glide.with(_mActivity).load(mDataBean.getHead_ico()).asBitmap().centerCrop().placeholder(imguser.getDrawable()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imguser) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(_mActivity.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imguser.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }else{
                            Glide.with(_mActivity).load(mDataBean.getHead_ico()).asBitmap().thumbnail(0.1f).centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imguser) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(_mActivity.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    imguser.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        }
                    }
                    holder.itemView.findViewById(R.id.imh_tv_getintegral).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(UserDataActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imh_arrowright).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(UserDataActivity.class);
                        }
                    });

                    holder.itemView.findViewById(R.id.imh_ll_levels).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MyLevelsActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imh_ll_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(UserDataActivity.class);
                        }
                    });
                } else if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
//                    holder.itemView.findViewById(R.id.imh_img_mylevel).setVisibility(GONE);
//                    holder.itemView.findViewById(R.id.imh_tv_mylevel).setVisibility(GONE);
                    holder.itemView.findViewById(R.id.imh_ll_userdata).setVisibility(GONE);
                    holder.itemView.findViewById(R.id.imh_ll_unlogin).setVisibility(View.VISIBLE);
                    holder.itemView.findViewById(R.id.imh_ll_container).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imh_tv_login).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imh_tv_register).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(RegisterActivity.class);
                        }
                    });
                }
            }
        };
        adapters.add(mHeadSubAdapter);

        LinearLayoutHelper orderlayouthelp = new LinearLayoutHelper();
        orderlayouthelp.setMarginBottom(20);
        mOrderAdapter = new SubAdapter<MineBean.DataBean.PendingBean>(_mActivity, orderlayouthelp, 1) {
            @Override
            public void onViewRecycled(MainViewHolder holder) {

            }

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater.from(_mActivity).inflate(R.layout.item_mine_oweorder, parent, false));
            }

            @Override
            public int getItemViewType(int position) {
                return Constant.TYPE_MINE.ORDER;
            }

            @Override
            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {

            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                    final BottomBar bottomBar = ((BottomBar) holder.itemView.findViewById(R.id.imo_bottombar));
                    holder.itemView.findViewById(R.id.imo_rl_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    bottomBar.reInit();
                    bottomBar.addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_obligation, R.mipmap.ic_nor_obligation, "待付款").setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_ungroup, R.mipmap.ic_nor_ungroup, "待成团").setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_delivergoods, R.mipmap.ic_nor_delivergoods, "待收货").setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_evaluate, R.mipmap.ic_nor_evaluate, "待评价").setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_salesreturn, R.mipmap.ic_nor_salesreturn, "我的售后").setSelectTextColor(getResources().getColor(R.color.grayfont)));
                    bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
                        @Override
                        public boolean onTabSelected(int position, int prePosition) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                            return true;
                        }

                        @Override
                        public void onTabUnselected(int position) {
                        }

                        @Override
                        public void onTabReselected(int position) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                } else if (mDataBean != null) {
                    final BottomBar bottomBar = ((BottomBar) holder.itemView.findViewById(R.id.imo_bottombar));
                    holder.itemView.findViewById(R.id.imo_rl_order).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.ALL);
                            startActivity(MineOrderActivity.class, bundle);
                        }
                    });
                    bottomBar.reInit();
                    bottomBar.addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_obligation, R.mipmap.ic_nor_obligation, "待付款").setUnreadCount(mDataBean.getPayment()).setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_ungroup, R.mipmap.ic_nor_ungroup, "待成团").setUnreadCount(mDataBean.getGroup()).setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_delivergoods, R.mipmap.ic_nor_delivergoods, "待收货").setUnreadCount(mDataBean.getReceipt()).setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_evaluate, R.mipmap.ic_nor_evaluate, "待评价").setUnreadCount(mDataBean.getAssess()).setSelectTextColor(getResources().getColor(R.color.grayfont)))
                            .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_nor_salesreturn, R.mipmap.ic_nor_salesreturn, "我的售后").setUnreadCount(mDataBean.getReturnX()).setSelectTextColor(getResources().getColor(R.color.grayfont)));
                    bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
                        @Override
                        public boolean onTabSelected(int position, int prePosition) {
                            Bundle bundle = new Bundle();
                            switch (position) {
                                case 0:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNPAY);
                                    break;
                                case 1:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNGROUP);
                                    break;
                                case 2:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNTAKE);
                                    break;
                                case 3:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE);
                                    break;
                                case 4:
                                    startActivity(AfterMallListActivity.class);
                                    return true;
                            }
                            startActivity(MineOrderActivity.class, bundle);
                            return true;
                        }

                        @Override
                        public void onTabUnselected(int position) {
                        }

                        @Override
                        public void onTabReselected(int position) {
                            Bundle bundle = new Bundle();
                            switch (position) {
                                case 0:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNPAY);
                                    break;
                                case 1:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNGROUP);
                                    break;
                                case 2:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNTAKE);
                                    break;
                                case 3:
                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE);
                                    break;
                                case 4:
//                                    bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE);
                                    startActivity(AfterMallListActivity.class);
                                    return;
                            }
                            startActivity(MineOrderActivity.class, bundle);
                        }
                    });
                }
            }
        };
        adapters.add(mOrderAdapter);

        LinearLayoutHelper pricelayouthelplayout = new LinearLayoutHelper();
        pricelayouthelplayout.setMarginBottom(20);
        mWalletAdapter = new SubAdapter<MineBean.DataBean.WalletBean>(_mActivity, pricelayouthelplayout, 1) {
            @Override
            public void onViewRecycled(MainViewHolder holder) {
            }

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater.from(_mActivity).inflate(R.layout.item_mine_oweprice, parent, false));
            }

            @Override
            public int getItemViewType(int position) {
                return Constant.TYPE_MINE.PRICE;
            }

            @Override
            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {

                if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_price)).setText("0");
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_yy)).setText("0");
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_integral)).setText("0");
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_coupon)).setText("0");
                    holder.itemView.findViewById(R.id.imo_ll_myprince).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });

                    holder.itemView.findViewById(R.id.imo_ll_myyaya).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_ll_myintegral).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_ll_mypledge).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                } else if (mDataBean != null) {
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_price)).setText(String.valueOf(mDataBean.getBalance()));
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_yy)).setText(String.valueOf(mDataBean.getYab()));
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_integral)).setText(String.valueOf(mDataBean.getPoint()));
                    ((TextView) holder.itemView.findViewById(R.id.imop_tv_coupon)).setText(String.valueOf(mDataBean.getTicket()));
                    holder.itemView.findViewById(R.id.imo_ll_myprince).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(_mActivity, MyPriceActivity.class);
                            intent.putExtra("value", String.valueOf(mDataBean.getBalance()));
                            startActivity(intent);
                        }
                    });

                    holder.itemView.findViewById(R.id.imo_ll_myyaya).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(_mActivity, MyYaYaActivity.class);
                            intent.putExtra("value", String.valueOf(mDataBean.getYab()));
                            startActivity(intent);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_ll_myintegral).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(_mActivity, MyIntegralActivity.class);
                            intent.putExtra("value", String.valueOf(mDataBean.getPoint()));
                            startActivity(intent);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_ll_mypledge).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(PledgeActivity.class);
                        }
                    });
                }
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
            }
        };
        adapters.add(mWalletAdapter);

        LinearLayoutHelper otherlayouthelp = new LinearLayoutHelper();
        otherlayouthelp.setMarginBottom(20);
        adapters.add(new SubAdapter(_mActivity, otherlayouthelp, 1) {

            @Override
            public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MainViewHolder(LayoutInflater.from(_mActivity).inflate(R.layout.item_mine_other, parent, false));
            }

            @Override
            public int getItemViewType(int position) {
                return Constant.TYPE_MINE.OTHER;
            }

            @Override
            protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
                holder.itemView.findViewById(R.id.imo_rl_service).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(MyServiceActivity.class);
                    }
                });

                if (mbLogin != Constant.LOGIN_STATUS.LOGIN) {
                    holder.itemView.findViewById(R.id.imo_rl_collect).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_group).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_places).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_service).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_advance).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_invitefriend).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_Invitationcode).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_eva).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                        }
                    });
                } else {
                    holder.itemView.findViewById(R.id.imo_rl_collect).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(CollectActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_group).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MineGroupActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_places).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(AddressManagerActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_advance).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(_mActivity,AdvanceActivity.class);
                            intent.putExtra("name",mName);
                            intent.putExtra("phone",mPhone);
                            startActivity(intent);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_invitefriend).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(ShareCodeActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_Invitationcode).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(InputCodeActivity.class);
                        }
                    });
                    holder.itemView.findViewById(R.id.imo_rl_eva).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(MineEvaActivity.class);
                        }
                    });
                }
            }

            @Override
            public void onBindViewHolder(MainViewHolder holder, int position) {
            }
        });
        delegateAdapter.setAdapters(adapters);
    }



    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SetActivity.class);
            }
        });
        mImgMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YYApp.getInstance().getToken() != null ) {
                    Intent intent = new Intent(mContext, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请先登录");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mViewStatus.setVisibility(GONE);
        }
        mImgCode.setImageResource(R.mipmap.ic_nor_qrcode);
        mImgSet.setImageResource(R.mipmap.ic_nor_set);
        mImgMessage.setImageResource(R.mipmap.ic_nor_message);
    }

    private int mbLogin = Constant.LOGIN_STATUS.ZYGOTE;
    private void getData() {
//        Boolean updateTag = YYApp.getInstance().getUiUpdateTag(R.layout.fragment_mine);
        //        if ( (!TextUtils.isEmpty(YYApp.getInstance().getToken()) && mbLogin != Constant.LOGIN_STATUS.LOGIN) || (updateTag!=null && updateTag)) {
        if(!TextUtils.isEmpty(YYApp.getInstance().getToken())){
            initRefreshLayout(true);
            YYMallApi.getMyInfo(_mActivity,  new YYMallApi.ApiResult<MineBean.DataBean>(_mActivity) {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    ViseLog.e(e);
//                    filterHttpCallback(e);
                    showToast(e.getMessage());
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(MineBean.DataBean dataBean) {
                    mbLogin = Constant.LOGIN_STATUS.LOGIN;
                    mHeadSubAdapter.setData(dataBean.getInfo());
                    mWalletAdapter.setData(dataBean.getWallet());
                    mOrderAdapter.setData(dataBean.getPending());
                    YYApp.getInstance().setPhone(dataBean.getInfo().getPhone());
                }
            });
        } else if (TextUtils.isEmpty(YYApp.getInstance().getToken()) && mbLogin != Constant.LOGIN_STATUS.UNLOGIN) {
            initRefreshLayout(false);
            mbLogin = Constant.LOGIN_STATUS.UNLOGIN;
            mHeadSubAdapter.setData(null);
            mWalletAdapter.setData(null);
            mOrderAdapter.setData(null);
        }
    }

    private void initRefreshLayout(boolean bPull) {
        mRefreshLayout.setEnableRefresh(bPull);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                YYMallApi.getMyInfo(_mActivity,  new YYMallApi.ApiResult<MineBean.DataBean>(_mActivity) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ViseLog.e(e);
                        showToast(e.getMessage());
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(MineBean.DataBean dataBean) {
                        mbLogin = Constant.LOGIN_STATUS.LOGIN;
                        mHeadSubAdapter.setData(dataBean.getInfo());
                        mWalletAdapter.setData(dataBean.getWallet());
                        mOrderAdapter.setData(dataBean.getPending());
                        YYApp.getInstance().setPhone(dataBean.getInfo().getPhone());
                        refreshlayout.finishRefresh();
                    }
                });
            }
        });
    }


    /******
     *
     */

    static class PagerAdapter extends com.yhkj.yymall.adapter.RecyclablePagerAdapter<MainViewHolder> {
        public PagerAdapter(SubAdapter adapter, RecyclerView.RecycledViewPool pool) {
            super(adapter, pool);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void onBindViewHolder(MainViewHolder viewHolder, int position) {
            switch (position) {
                case 0:
                    ((ImageView) viewHolder.itemView.findViewById(R.id.vb_img)).setImageResource(R.mipmap.ic_nor_banner_1);
                    break;
                case 1:
                    ((ImageView) viewHolder.itemView.findViewById(R.id.vb_img)).setImageResource(R.mipmap.test_banner_2);
                    break;
                case 2:
                    ((ImageView) viewHolder.itemView.findViewById(R.id.vb_img)).setImageResource(R.mipmap.test_banner_1);
                    break;
            }

        }

        @Override
        public int getItemViewType(int position) {
            return 2;
        }
    }

    static class SubAdapter<T> extends DelegateAdapter.Adapter<MainViewHolder> {
        private Context mContext;
        private LayoutHelper mLayoutHelper;
        private VirtualLayoutManager.LayoutParams mLayoutParams;
        private int mCount = 0;
        protected T mDataBean;

        public void setData(T infoBean) {
            mDataBean = infoBean;
            notifyDataSetChanged();
        }

        public SubAdapter(Context context, LayoutHelper layoutHelper, int count) {
            this(context, layoutHelper, count, new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }

        public SubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull VirtualLayoutManager.LayoutParams layoutParams) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mCount = count;
            this.mLayoutParams = layoutParams;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.itemView.setLayoutParams(
                    new VirtualLayoutManager.LayoutParams(mLayoutParams));
        }


        @Override
        protected void onBindViewHolderWithOffset(MainViewHolder holder, int position, int offsetTotal) {
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
//        public static volatile int existing = 0;
//        public static int createdTimes = 0;

        public MainViewHolder(View itemView) {
            super(itemView);
//            createdTimes++;
//            existing++;
        }

        @Override
        protected void finalize() throws Throwable {
//            existing--;
            super.finalize();
        }
    }
}
