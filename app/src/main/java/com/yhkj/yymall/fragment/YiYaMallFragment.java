package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.MessageActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.ScanActivity;
import com.yhkj.yymall.activity.SearchActivity;
import com.yhkj.yymall.activity.ShopClassifyActivity;
import com.yhkj.yymall.adapter.YiYamallAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.UnReadBean;
import com.yhkj.yymall.bean.YiYaShopBean;
import com.yhkj.yymall.bean.YiyaListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/19.
 */

public class YiYaMallFragment extends BaseFragment {

    @Bind(R.id.fy_toolbar)
    LinearLayout mLlToolbar;

    @Bind(R.id.fy_view_status)
    View mViewStatus;

    @Bind(R.id.rv_yiyamall)
    RecyclerView rv_yiyamall;

    @Bind(R.id.fsc_refreshlayout)
    SmartRefreshLayout fsc_refreshlayout;

    @Bind(R.id.vt_btn_right)
    ImageView mImgRight;

    @Bind(R.id.vt_btn_left)
    ImageView mImgLeft;

    @Bind(R.id.vt_tv_search)
    TextView mTvSearch;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        String hotString = YYApp.getInstance().getmHotSearch();
        if (!TextUtils.isEmpty(hotString)){
            YYApp.getInstance().setmHotSearch(hotString);
            mTvSearch.setHint(hotString);
        }
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
                        mImgLeft.setImageResource(R.mipmap.ic_nor_unmessage);
                    }else{
                        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
                    }
                }
            });
        }else {
            mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        }
    }

    ApiCallback apiCallback = new ApiCallback<YiYaShopBean.DataBean>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onError(ApiException e) {
            fsc_refreshlayout.finishRefresh();
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(YiYaShopBean.DataBean dataBean) {
            fsc_refreshlayout.finishRefresh();
            final VirtualLayoutManager layoutManager = new VirtualLayoutManager(_mActivity);
            rv_yiyamall.setLayoutManager(layoutManager);
            final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            rv_yiyamall.setRecycledViewPool(viewPool);
            viewPool.setMaxRecycledViews(0, 20);
            adapter = new YiYamallAdapter(_mActivity, layoutManager, dataBean);
            rv_yiyamall.setAdapter(adapter);
        }
    };

    YiYamallAdapter adapter;

    public static YiYaMallFragment getInstance() {
        YiYaMallFragment fragment = new YiYaMallFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_yiyamall;
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchActivity.class, Constant.TOOLBAR_TYPE.SEARCH_EDIT);
            }
        });
        mTvSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDrawable.length < 2 || mDrawable[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > mTvSearch.getWidth() - mDrawable[2].getBounds().width()) {

                    startActivity(ScanActivity.class);
                    return true;
                }
                return false;
            }
        });
    }

    private int mCurPager = 1;
    private void initRefreshLayout() {
        fsc_refreshlayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        fsc_refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPager = 1;
                YYMallApi.getYiYaShop(_mActivity,apiCallback);
            }
        });
        fsc_refreshlayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPager++;
                getPageData();
            }
        });
    }

    private void getPageData() {
        YYMallApi.getYiYaShopList(_mActivity, mCurPager, new ApiCallback<YiyaListBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                fsc_refreshlayout.finishLoadmore();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(YiyaListBean.DataBean dataBean) {
                if (dataBean.getGoods() == null || dataBean.getGoods().size() == 0){
                    fsc_refreshlayout.finishLoadmore();
                    fsc_refreshlayout.setLoadmoreFinished(true);
                }else {
                    fsc_refreshlayout.finishLoadmore();
                }
                if (adapter!=null)
                    adapter.addDatas(dataBean.getGoods());
            }
        });
    }

    private Drawable[] mDrawable;
    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        mDrawable = mTvSearch.getCompoundDrawables();
        if (Build.VERSION.SDK_INT < KITKAT)
            mViewStatus.setVisibility(GONE);
    }

    @Override
    protected void initData() {
        mLlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_bule));
        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        mImgRight.setImageResource(R.mipmap.ic_nor_classily);
        mImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YYApp.getInstance().getToken() != null) {
                    Intent intent = new Intent(mContext, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                }
            }
        });
        mImgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShopClassifyActivity.class, Constant.TOOLBAR_TYPE.SEARCH_TV);
            }
        });
        initRefreshLayout();
        YYMallApi.getYiYaShop(_mActivity, apiCallback);
    }

}
