package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.listview.BaseAdapter;
import com.vise.xsnow.ui.adapter.listview.BaseViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.ShopClassifyBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ShopClassifyActivity extends BaseToolBarActivity {

    @Bind(R.id.asc_listview)
    ListView mListView;

    @Bind(R.id.asc_recycleview)
    RecyclerView mRecycleView;

    private int mSelectClassify = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopclassify);
    }

    @Override
    protected void initView() {
        super.initView();
        mPreSelect = getIntent().getIntExtra("select",-1);
        mDrawable = mTvSearch.getCompoundDrawables();
    }
    private Drawable[] mDrawable;
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

    private List<DelegateAdapter.Adapter> mLinkdapters = new LinkedList<>();
    private DelegateAdapter mDelegateAdapter;
    private GridLayoutHelper mGridhelper;
    private int mPreSelect;
    @Override
    protected void initData() {
        setImgRightVisiable(GONE);
        setImgBackResource(R.mipmap.ic_nor_arrowleft);
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecycleView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);
        mDelegateAdapter = new DelegateAdapter(layoutManager, false);
        mRecycleView.setAdapter(mDelegateAdapter);

        mGridhelper = new GridLayoutHelper(3);
        mGridhelper.setAutoExpand(false);

        String hotSearch = YYApp.getInstance().getmHotSearch();
        if (!TextUtils.isEmpty(hotSearch))
            mTvSearch.setHint(hotSearch);
//        mGridhelper.setGap(5);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private void getData() {
        YYMallApi.getShopClassify(this, new ApiCallback<ShopClassifyBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopClassifyBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setUiData(dataBean);
                handlerPreSelect(dataBean);
            }
        });
    }

    private void handlerPreSelect(ShopClassifyBean.DataBean dataBean) {
        if (mPreSelect != -1){
            for (int i=0; i<dataBean.getCategorys().size(); i++){
                ShopClassifyBean.DataBean.CategorysBean bean = dataBean.getCategorys().get(i);
                if (bean.getId() == mPreSelect){
                    mSelectClassify = i;
                    mParentAdapter.notifyDataSetChanged();
                    setChildData(bean);
                    return;
                }
            }
        }
    }

    private BaseAdapter mParentAdapter;
    private void setUiData(ShopClassifyBean.DataBean dataBean) {
        if (dataBean == null || dataBean.getCategorys() == null || dataBean.getCategorys().size() == 0) return;
        mParentAdapter = new BaseAdapter<ShopClassifyBean.DataBean.CategorysBean>(this,dataBean.getCategorys(),R.layout.item_classify_title) {
            @Override
            public <H extends BaseViewHolder> void convert(final H viewHolder, final int position, final ShopClassifyBean.DataBean.CategorysBean bean) {
                TextView tv = viewHolder.getView(R.id.ict_tv_name);
                tv.setText(bean.getName());
                View rootView = viewHolder.getView(R.id.ict_rl_bg);
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSelectClassify == position)
                            return;
                        mSelectClassify = position;
                        setChildData(bean);
                        notifyDataSetChanged();
                    }
                });

                if (mSelectClassify == position){
                    viewHolder.getView(R.id.ict_view_indicate).setBackgroundColor(getResources().getColor(R.color.theme_bule));
                    rootView.setBackgroundColor(getResources().getColor(R.color.white));
                    tv.setTextColor(getResources().getColor(R.color.theme_bule));
                }else{
                    viewHolder.getView(R.id.ict_view_indicate).setBackgroundColor(getResources().getColor(R.color.transparency));
                    rootView.setBackgroundColor(getResources().getColor(R.color.graybg));
                    tv.setTextColor(getResources().getColor(R.color.grayfont));
                }
            }
        };
        mListView.setAdapter(mParentAdapter);
        setChildData(dataBean.getCategorys().get(0));
    }


    private SubAdapter mSubAdapter;
    public void setChildData(ShopClassifyBean.DataBean.CategorysBean bean){
//        mLinkdapters.clear();
        if (mSubAdapter == null) {
            mSubAdapter = new SubAdapter(this, mGridhelper, bean.getChilds()) {
                @Override
                public void onBindViewHolder(final ClassifyChildViewHolder holder, int position) {
                    super.onBindViewHolder(holder, position);
                    ((VirtualLayoutManager.LayoutParams)holder.mLlBg.getLayoutParams()).mAspectRatio = 1;
                    final ShopClassifyBean.DataBean.CategorysBean.ChildsBean bean = mDatabean.get(position);
                    Glide.with(ShopClassifyActivity.this).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into(holder.mImgShop);
                    holder.mTvShop.setText(bean.getName());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ShopListActivity.class);
                            intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                            intent.putExtra("id", String.valueOf(bean.getId()));
                            intent.putExtra("name", bean.getName());
                            startActivity(intent);
                        }
                    });
                }
            };
            mLinkdapters.add(mSubAdapter);
            mDelegateAdapter.setAdapters(mLinkdapters);
        }else{
            mSubAdapter.setDatas(bean.getChilds());
        }
    }

    class SubAdapter extends DelegateAdapter.Adapter<ClassifyChildViewHolder> {

        private Context mContext;
        private LayoutHelper mLayoutHelper;
        protected List<ShopClassifyBean.DataBean.CategorysBean.ChildsBean> mDatabean;

        public void setDatas(List<ShopClassifyBean.DataBean.CategorysBean.ChildsBean> datas){
            mDatabean = datas;
            notifyDataSetChanged();
        }

        public SubAdapter(Context context, LayoutHelper layoutHelper, List<ShopClassifyBean.DataBean.CategorysBean.ChildsBean> bean) {
            this.mContext = context;
            this.mLayoutHelper = layoutHelper;
            this.mDatabean = bean;
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return mLayoutHelper;
        }

        @Override
        public ClassifyChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ClassifyChildViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify_shop, parent, false));
        }

        @Override
        public void onBindViewHolder(ClassifyChildViewHolder holder, int position) {
        }

        @Override
        protected void onBindViewHolderWithOffset(ClassifyChildViewHolder holder, int position, int offsetTotal) {
        }

        @Override
        public int getItemCount() {
            return mDatabean.size();
        }
    }

    class ClassifyChildViewHolder extends RecyclerView.ViewHolder {
        ImageView mImgShop;
        TextView mTvShop;
        LinearLayout mLlBg;
        public ClassifyChildViewHolder(View itemView) {
            super(itemView);
            mImgShop = (ImageView)itemView.findViewById(R.id.ics_img_shop);
            mTvShop = (TextView)itemView.findViewById(R.id.ics_tv_shopname);
            mLlBg = (LinearLayout)itemView.findViewById(R.id.ics_ll_bg);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
}
