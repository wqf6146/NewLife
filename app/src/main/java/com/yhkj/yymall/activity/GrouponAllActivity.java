package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.GrouponListBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/31.
 */

public class GrouponAllActivity extends BaseToolBarActivity {

    @Bind(R.id.lv_grouponall)
    ListView lv_grouponall;

    private IndentCusAdapter adapter;
    private int page = 1;
    private Intent intent;
    private String groupId;
    private List<GrouponListBean.DataBean.ListBean> list;
    private int lastVisibleIndex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouponall);
        setOnResumeRegisterBus(true);
        list = new ArrayList<>();
        lv_grouponall.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastVisibleIndex == adapter.getCount()) {
                    page++;
                    YYMallApi.getGroupGroupList(GrouponAllActivity.this, groupId, page + "", new ApiCallback<GrouponListBean.DataBean>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(ApiException e) {

                        }

                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onNext(GrouponListBean.DataBean dataBean) {
                            for (int a = 0; a < dataBean.getList().size(); a++) {
                                list.add(dataBean.getList().get(a));
                            }
                            adapter.setDate(list);
                            adapter.notifyDataSetChanged();
                        }
                    });


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastVisibleIndex = firstVisibleItem + visibleItemCount;

            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("所有团购");
        setImgBackVisiable(View.VISIBLE);
        intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        getData();

    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void getData() {
        YYMallApi.getGroupGroupList(GrouponAllActivity.this, groupId, page + "", new ApiCallback<GrouponListBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GrouponListBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                for (int a = 0; a < dataBean.getList().size(); a++) {
                    list.add(dataBean.getList().get(a));
                }
                adapter = new IndentCusAdapter(list);
                lv_grouponall.setAdapter(adapter);
            }
        });
    }

    private class IndentCusAdapter extends BaseAdapter {

        private List<GrouponListBean.DataBean.ListBean> lists;


        public IndentCusAdapter(List<GrouponListBean.DataBean.ListBean> list) {
            lists = list;
        }

        public void setDate(List<GrouponListBean.DataBean.ListBean> list) {
            lists = list;
        }

        @Override
        public int getCount() {

            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Cache cache;
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_grouponall, null);
                cache = new Cache();
                cache.tv_grouponall_name = (TextView) convertView.findViewById(R.id.tv_grouponall_name);
                cache.ig_tv_groupsum = (TextView)convertView.findViewById(R.id.ig_tv_groupsum);
                cache.tv_grouponall_from = (TextView) convertView.findViewById(R.id.tv_grouponall_from);
                cache.tv_grouponall_offered = (TextView) convertView.findViewById(R.id.tv_grouponall_offered);
                cache.img_grouponall_head = (ImageView) convertView.findViewById(R.id.img_grouponall_head);
                convertView.setTag(cache);
            } else {
                cache = (Cache) convertView.getTag();
            }
            cache.ig_tv_groupsum.setText(lists.get(position).getNumber()+"");
            cache.tv_grouponall_name.setText(lists.get(position).getUser() + "");
            cache.tv_grouponall_from.setText(lists.get(position).getFrom() + "");
            Glide.with(GrouponAllActivity.this).load(list.get(position).getUserico())
                    .asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(cache.img_grouponall_head) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    cache.img_grouponall_head.setImageDrawable(circularBitmapDrawable);
                }
            });
            cache.tv_grouponall_offered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(getApplicationContext(), OfferedDetailsActivity.class);
                    intent.putExtra("groupId", lists.get(position).getId() + "");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    class Cache {
        TextView tv_grouponall_name, tv_grouponall_from, tv_grouponall_offered,ig_tv_groupsum;
        ImageView img_grouponall_head;
    }

}
