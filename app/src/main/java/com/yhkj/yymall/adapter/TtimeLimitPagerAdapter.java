/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.yhkj.yymall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.TimeKillDateBean;
import com.yhkj.yymall.view.viewpager.PagerAdapter;

import java.util.List;

/**
 * Created by mikeafc on 15/11/26.
 */
public class TtimeLimitPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<TimeKillDateBean.DataBean.PaniclBuyBean> mDateBean;

    public TtimeLimitPagerAdapter(Context context,List<TimeKillDateBean.DataBean.PaniclBuyBean> bean) {
        mDateBean = bean;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDateBean.size();
    }

    public void setDateBean(List<TimeKillDateBean.DataBean.PaniclBuyBean> dateBean){
        mDateBean = dateBean;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        TimeKillDateBean.DataBean.PaniclBuyBean bean = mDateBean.get(position);
        String[] stringData = bean.getStart_time().split(" ");

        LinearLayout llContainer = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.item_timedate, null);
        TextView tvDate = (TextView) llContainer.findViewById(R.id.it_tv_date);
        tvDate.setText(stringData[0] + "\n" + stringData[1]);

        TextView tvDesc = (TextView) llContainer.findViewById(R.id.it_tv_desc);
        tvDesc.setText(bean.getStatus() == 0 ?  "即将开始" : "抢购进行中");

//        linearLayout.setId(R.id.item_id);
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnitemClickListen!=null)
                    mOnitemClickListen.onItemClick(position);
            }
        });
        container.addView(llContainer);
        return llContainer;
    }

    public interface OnItemClickListen{
        void onItemClick(int positon);
    }
    private OnItemClickListen mOnitemClickListen;
    public void setOnItemClickLisiten(OnItemClickListen onItemClickLisiten){
        mOnitemClickListen = onItemClickLisiten;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }
}
