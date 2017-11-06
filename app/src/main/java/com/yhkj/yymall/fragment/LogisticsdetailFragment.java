package com.yhkj.yymall.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.LogisticsBean;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import java.util.Collections;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/9/5.
 */

public class LogisticsdetailFragment extends BaseFragment {

    @Bind(R.id.ald_tv_logisticsname)
    TextView mTvLogisticName;

    @Bind(R.id.ald_tv_logisticsnumb)
    TextView mTvLogisticsNumb;

    @Bind(R.id.ald_tv_logisticsphone)
    TextView mTvLogisticPhone;

    @Bind(R.id.ald_listview)
    NestFullListView mListView;

//    @Bind(R.id.ald_img_server)
//    ImageView mImgServer;

    @Bind(R.id.ald_img_shop)
    ImageView mImgShop;

//    @Bind(R.id.ald_tv_lineserver)
//    TextView mTvLineServer;

    @Bind(R.id.ald_tv_shopnumb)
    TextView mTvShopNumb;



//    @Bind(R.id.ald_ll_loginfo)
//    LinearLayout mLlLoginfo;
//
//    @Bind(R.id.al_ll_logdata)
//    LinearLayout mLlLogData;

    @Bind(R.id.ald_tv_status)
    TextView mTvStatus;

    public static LogisticsdetailFragment getInstance() {
        LogisticsdetailFragment fragment = new LogisticsdetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_logistics;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        initUidata((LogisticsBean.DataBean)getArguments().getParcelable("data"));
    }


    private String getStatusString(int status){
        switch (status){
            case 0:
                return "暂无信息";
            case 1:
                return "已揽收";
            case 2:
                return "在途中";
            case 3:
                return "已签收";
            default:
                return "问题件";
        }
    }

    private void initUidata(LogisticsBean.DataBean dataBean) {
        //物流状态: 0-无轨迹，1-已揽收，2-在途中 201-到达派件城市，3-签收,4-问题件
        mTvStatus.setText(getStatusString(dataBean.getStatus()));
        Glide.with(this).load(dataBean.getImg()).into(mImgShop);
        mTvLogisticName.setText(String.format(getString(R.string.logisticssrc),dataBean.getFreightCompany()));
        mTvLogisticsNumb.setText(String.format(getString(R.string.ordernumber),dataBean.getDeliveryCode()));
        mTvLogisticPhone.setText(dataBean.getOfficialCall());
        mTvShopNumb.setText(dataBean.getTotal() + "件商品");
//        Collections.reverse(dataBean.getTraces());
        mListView.setAdapter(new NestFullListViewAdapter<LogisticsBean.DataBean.TracesBean>(R.layout.item_logistics,dataBean.getTraces() ) {
            @Override
            public void onBind(int pos, LogisticsBean.DataBean.TracesBean bean, NestFullViewHolder holder) {
                if (pos == getDatas().size()-1 ){
                    holder.setVisible(R.id.il_view_line_top,true);
                    holder.setVisible(R.id.il_view_line_bottom,false);
                    holder.setImageResource(R.id.il_img_circle_2,R.mipmap.ic_nor_graycircle);
                }else if (pos == 0){
                    holder.setVisible(R.id.il_view_line_top,false);
                    holder.setVisible(R.id.il_view_line_bottom,true);
                    holder.setImageResource(R.id.il_img_circle_2,R.mipmap.ic_nor_2bluecircle);
                }else{
                    holder.setVisible(R.id.il_view_line_top,true);
                    holder.setVisible(R.id.il_view_line_bottom,true);
                    holder.setImageResource(R.id.il_img_circle_2,R.mipmap.ic_nor_graycircle);
                }
                holder.setText(R.id.il_tv_desc,bean.getAcceptStation());
                holder.setText(R.id.il_tv_time,bean.getAcceptTime());
            }
        });
    }
}
