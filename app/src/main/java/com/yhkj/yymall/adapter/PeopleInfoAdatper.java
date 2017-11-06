package com.yhkj.yymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.BayByInfoActivity;
import com.yhkj.yymall.activity.UserDataActivity;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.PersonBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.List;


/**
 * Created by Administrator on 2017/8/23.
 */

public class PeopleInfoAdatper extends BaseSwipeAdapter {

    List<PersonBean.DataBean.BabyInfoBean.InfoBean> mDatas;
    private Context mContext;

    public PeopleInfoAdatper(Context context,PersonBean.DataBean.BabyInfoBean babyInfoBean){
        mContext = context;
        mDatas = babyInfoBean.getInfo();
    }

    @Override
    public void fillValues(final int pos, View view) {
        final PersonBean.DataBean.BabyInfoBean.InfoBean infoBean = mDatas.get(pos);

        final ImageView imgPeople = (ImageView)view.findViewById(R.id.ip_img_people);

        TextView tvName = (TextView)view.findViewById(R.id.ip_tv_name);

        TextView tvYears = (TextView)view.findViewById(R.id.ip_tv_year);

        RelativeLayout rlContainer = (RelativeLayout)view.findViewById(R.id.ip_rl_container);

        LinearLayout llDel = (LinearLayout)view.findViewById(R.id.ll_messageitem_del);

        Glide.with(mContext).load(infoBean.getType() == 1 ? R.mipmap.ic_nor_midladybaby : R.mipmap.ic_nor_midbaby)
                .asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(imgPeople) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgPeople.setImageDrawable(circularBitmapDrawable);
            }
        });
        tvName.setText(infoBean.getType() == 1 ? "预产期" : infoBean.getSex().equals("男") ? "男孩" : "女孩");
        tvYears.setText(infoBean.getType() == 1 ? infoBean.getBrithday() : infoBean.getYears());
        rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑
                Intent intent = new Intent(mContext,BayByInfoActivity.class);
                intent.putExtra("id",infoBean.getId());
                mContext.startActivity(intent);
            }
        });
        llDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                YYMallApi.deleteBaByInfo(mContext, infoBean.getId(), new YYMallApi.ApiResult<CommonBean>(mContext) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        Toast.makeText(mContext,"删除成功",Toast.LENGTH_SHORT).show();
                        closeAllItems();
                        mDatas.remove(pos);
                        notifyDataSetChanged();
                        if (mDeleteClickLisiten != null){
                            mDeleteClickLisiten.onDeleteClickLisiten();
                        }
                    }
                });
            }
        });

    }

    public PeopleInfoAdatper setDeleteClickLisiten(OnDeleteEvent mDeleteClickLisiten) {
        this.mDeleteClickLisiten = mDeleteClickLisiten;
        return this;
    }

    private OnDeleteEvent mDeleteClickLisiten;
    public interface OnDeleteEvent {
        void onDeleteClickLisiten();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_people,viewGroup,false);
//        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.ip_swipelayout;
    }
}
