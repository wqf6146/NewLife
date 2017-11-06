package com.yhkj.yymall.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.MainActivity;
import com.yhkj.yymall.bean.RankChartBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.SuitLines.SuitLines;
import com.yhkj.yymall.view.SuitLines.Unit;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * Created by Administrator on 2017/7/11.
 */

public class LevelUpFragment extends BaseFragment {

    public static LevelUpFragment getInstance(int exp,String desc,int level) {
        LevelUpFragment fragment = new LevelUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("exp",exp);
        bundle.putString("desc",desc);
        bundle.putInt("level",level);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.fl_suitlines)
    SuitLines mSuitLines;

    @Bind(R.id.fl_nestlistview_getrules)
    NestFullListView mNestGetRules;

    @Bind(R.id.fl_nestlistview_consumes)
    NestFullListView mNestConsumes;

    @Bind(R.id.fl_nestlistview_remark)
    NestFullListView mNestRemark;

    @Bind(R.id.fl_tv_gobuy)
    TextView mTvGoBuy;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_levelup;
    }

    private int mCurExp;
    private int mLevels;
    private String mDesc;

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mCurExp = getArguments().getInt("exp");
        mDesc = getArguments().getString("desc");
        mLevels = getArguments().getInt("level");
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void getData(){
        YYMallApi.getRankChartData(_mActivity, new YYMallApi.ApiResult<RankChartBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(RankChartBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                setUiData(dataBean);
            }
        });
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvGoBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YYApp.getInstance().setIndexShow(0);
                startActivity(MainActivity.class);
            }
        });
    }

    @Override
    protected void initData() {
        getData();
    }

    private void setUiData(RankChartBean.DataBean dataBean){
        mSuitLines.setXySize(8);
        initSuitLines(dataBean);
        int[] colors = new int[2];
        colors[0] = Color.rgb(171,152,244);
        colors[1] = Color.rgb(89,160,254);
        mSuitLines.setDefaultOneLineColor(colors);
        mSuitLines.setLineForm(!mSuitLines.isLineFill());

        mNestGetRules.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_tv_point,dataBean.getRule()) {
            @Override
            public void onBind(int pos, String str, NestFullViewHolder holder) {
                holder.setText(R.id.itp_tv_desc,str);
            }
        });

        mNestConsumes.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_tv_point,dataBean.getConsumes()) {
            @Override
            public void onBind(int pos, String str, NestFullViewHolder holder) {
                holder.setText(R.id.itp_tv_desc,str);
            }
        });

        mNestRemark.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_tv_pure,dataBean.getRemark()) {
            @Override
            public void onBind(int pos, String str, NestFullViewHolder holder) {
                TextView tv = holder.getView(R.id.itp_tv);
                ((RelativeLayout.LayoutParams)tv.getLayoutParams()).addRule(ALIGN_PARENT_LEFT);
                ((RelativeLayout.LayoutParams)tv.getLayoutParams()).addRule(CENTER_IN_PARENT,0);
                tv.setTextSize(12);
                holder.setText(R.id.itp_tv,str);
            }
        });
    }


    public void initSuitLines(RankChartBean.DataBean dataBean) {
        List<Unit> lines = new ArrayList<>();
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(1, "d1"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(3, "d2"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(5, "d3"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(7, "d4"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(9, "d5"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(11, "d6"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(13, "d7"));
        lines.add(new Unit(0, "d1"));
        lines.add(new Unit(15, "d8"));
        lines.add(new Unit(0, "d1"));
        mSuitLines.feed(lines,mCurExp, TextUtils.isEmpty(mDesc) ? "" : mDesc ,mLevels,dataBean.getBody());
    }
}
