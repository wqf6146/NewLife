package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.MessageListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.MessagePopupView;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/24.
 */

public class MessageActivity extends BaseToolBarActivity {

    @Bind(R.id.lv_message)
    ListView mListView;

    @Bind(R.id.am_refreshview)
    SmartRefreshLayout mRefreshView;


    private MessageAdapter mAdapter;

    private MessagePopupView.MessageCall call = new MessagePopupView.MessageCall() {
        @Override
        public void send(boolean readAllOrdelAll) {
            mCurPage = 1;
            getData(readAllOrdelAll);
        }
    };
    private int mCurPage = 1;
    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPage = 1;
                mRefreshView.setLoadmoreFinished(false);
                getData(null);
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPage++;
                getData(null);
            }
        });

    }

    private void getData(final Boolean readAllOrDel){
        YYMallApi.getMessageList(MessageActivity.this, mCurPage, new ApiCallback<MessageListBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
                if (mRefreshView.getState() == RefreshState.Loading){
                    mRefreshView.finishLoadmore();
                    mCurPage--;
                }else if (mRefreshView.getState() == RefreshState.Refreshing){
                    mRefreshView.finishRefresh();
                    mRefreshView.setLoadmoreFinished(false);
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(MessageListBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (readAllOrDel != null){
                    if (dataBean.getList().size() == 0){
                        setImgRightVisiable(GONE);
                        setNoDataView(R.mipmap.ic_nor_orderbg,"暂无消息");
                        return;
                    }
                    mAdapter.setData(dataBean.getList());
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                if (mRefreshView.getState() == RefreshState.Loading){
                    mRefreshView.finishLoadmore();
                    if (dataBean.getList().size() == 0){
                        mRefreshView.setLoadmoreFinished(true);
                        return;
                    }
                    mAdapter.addData(dataBean.getList());
                    mAdapter.notifyDataSetChanged();
                    return;
                }else if (mRefreshView.getState() == RefreshState.Refreshing){
                    mRefreshView.finishRefresh();
                    mAdapter.setData(dataBean.getList());
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                if (dataBean.getList().size() == 0){
                    setImgRightVisiable(GONE);
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无消息");
                    return;
                }
                setImgRightVisiable(VISIBLE);
                if (mAdapter == null){
                    mAdapter = new MessageAdapter(dataBean.getList());
                    mListView.setAdapter(mAdapter);
                }else{
                    mAdapter.setData(dataBean.getList());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null);
    }

    @Override
    protected void initView() {
        super.initView();
        initRefreshLayout();
    }

    @Override
    protected void initData() {
        setTvTitleText("系统消息");
        setImgBackVisiable(View.VISIBLE);
        setImgRightResource(R.mipmap.details_dian);
        setImgRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MessagePopupView(MessageActivity.this, call).showPopupWindow(v);
            }
        });
    }

    private class MessageAdapter extends BaseSwipeAdapter {

        private List<MessageListBean.DataBean.ListBean> mDatas;

        public MessageAdapter(List<MessageListBean.DataBean.ListBean> datas) {
            mDatas = datas;
        }

        public void setData(List<MessageListBean.DataBean.ListBean> datas) {
            mDatas = datas;
        }

        public void addData(List<MessageListBean.DataBean.ListBean> data){
            mDatas.addAll(data);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe_message;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_message, null);
            return view;
        }

        @Override
        public void fillValues(final int position, View convertView) {
            LinearLayout ll_messageitem_del = (LinearLayout) convertView.findViewById(R.id.ll_messageitem_del);
            LinearLayout ll_messageitem_onClick = (LinearLayout) convertView.findViewById(R.id.ll_messageitem_onClick);
            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_message);
            TextView tv_message_title = (TextView) convertView.findViewById(R.id.tv_message_title);
            TextView tv_message_connet = (TextView) convertView.findViewById(R.id.tv_message_connet);
            TextView tv_message_time = (TextView) convertView.findViewById(R.id.tv_message_time);
            tv_message_title.setText(mDatas.get(position).getTitle() + "");
            tv_message_connet.setText(mDatas.get(position).getContent() + "");
            tv_message_time.setText(mDatas.get(position).getTime() + "");
            if (mDatas.get(position).getStatus().equals("0")) {
                tv_message_title.setTextColor(getResources().getColor(R.color.grayfont));
                tv_message_connet.setTextColor(getResources().getColor(R.color.grayfont));
                tv_message_time.setTextColor(getResources().getColor(R.color.grayfont));
            } else {
                tv_message_title.setTextColor(getResources().getColor(R.color.grayfont_1_5));
                tv_message_connet.setTextColor(getResources().getColor(R.color.grayfont_1_5));
                tv_message_time.setTextColor(getResources().getColor(R.color.grayfont_1_5));
            }

            ll_messageitem_onClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatas.get(position).getStatus().equals("0")) {
                        YYMallApi.readMessage(MessageActivity.this, mDatas.get(position).getId() + "",
                                new YYMallApi.ApiResult<CommonBean>(MessageActivity.this) {
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
                            public void onNext(CommonBean commonBean) {
                                Intent intent = new Intent(MessageActivity.this, MessageMinuteActivity.class);
                                intent.putExtra("content", mDatas.get(position).getContent());
                                intent.putExtra("time", mDatas.get(position).getTime());
                                startActivity(intent);
                            }
                        });
                    } else {
                        Intent intent = new Intent(MessageActivity.this, MessageMinuteActivity.class);
                        intent.putExtra("content", mDatas.get(position).getContent());
                        intent.putExtra("title", mDatas.get(position).getTitle());
                        intent.putExtra("id", String.valueOf(mDatas.get(position).getId()));
                        intent.putExtra("time", mDatas.get(position).getTime());
                        startActivity(intent);

                    }
                }
            });
            ll_messageitem_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeLayout.close();
                    YYMallApi.deleteMessage(MessageActivity.this, mDatas.get(position).getId() + "",
                            new YYMallApi.ApiResult<CommonBean>(MessageActivity.this) {
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
                        public void onNext(CommonBean commonBean) {
                            mCurPage=1;
                            getData(null);
                            showToast("删除成功");
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(null);
    }
}
