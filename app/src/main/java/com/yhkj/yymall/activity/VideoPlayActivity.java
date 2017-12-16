package com.yhkj.yymall.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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
import com.yhkj.yymall.view.ItemOffsetDecoration;
import android.support.v4.view.ViewPager;
import java.util.List;

import butterknife.Bind;

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

    @Bind(R.id.av_fl_videoplay)
    FrameLayout mFlVideoPlay;

    private ViewGroup.LayoutParams mVCLayouyParams;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.)
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
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
    }

    private List<VideoListBean.DataBean.ListBean> mListData;
    private String mToken;

    @Override
    protected void initData() {
        mListData = getIntent().getParcelableArrayListExtra("list");
        mToken = getIntent().getStringExtra("token");
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        mVCLayouyParams = mFlVideoPlay.getLayoutParams();
        getGoodsData(null);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getGoodsData(null);
    }

    private int mCurPage = 1;
    private CommonAdapter mAdapter;
    private HeaderAndFooterWrapper mWrapperAdapter;
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
                    View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.view_videoandcontrol,mRecycleView,false);

//                    ViewPager singleViewPager = (ViewPager)view.findViewById(R.id.vv_viewpager_single);

                    VideoFragment[] videoFragments = new VideoFragment[mListData.size()];
                    for (int i=0; i<mListData.size();i++){
                        videoFragments[i] = VideoFragment.getInstance(mToken,i,mListData.get(i));
                    }
                    NormalFragmentAdapter fragmentAdapter = new NormalFragmentAdapter(getSupportFragmentManager(),videoFragments);
                    mViewPagerSingle.setAdapter(fragmentAdapter);

                    mWrapperAdapter.addHeaderView(view);
                    mRecycleView.setAdapter(mWrapperAdapter);
                }
            }
        });
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
}
