package com.yhkj.yymall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.viewpager.PagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public abstract class UltraBannerPagerAdapter<T> extends PagerAdapter {
    private boolean isMultiScr;

    private List<T> mData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    public UltraBannerPagerAdapter(Context context,List data, boolean isMultiScr) {
        this.isMultiScr = isMultiScr;
        mData = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup frameLayout = (ViewGroup) LayoutInflater.from(container.getContext()).inflate(R.layout.view_banner_item, null);
        bind(frameLayout,mData.get(position),position);

        container.addView(frameLayout);
        return frameLayout;
    }
    protected abstract void bind(ViewGroup container, T t, int position);
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FrameLayout view = (FrameLayout) object;
        container.removeView(view);
    }

    public void setOnItemCLickListener(OnItemClickListener onItemCLickListener ){
        mOnItemClickListener = onItemCLickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(ImageView imageView,int pos);
    }
}