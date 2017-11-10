package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.MyServiceBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MyServiceActivity extends BaseToolBarActivity {

    @Bind(R.id.el_service)
    ExpandableListView el_service;

    @Bind(R.id.ll_service_qq)
    LinearLayout ll_service_qq;

    @Bind(R.id.ll_service_tel)
    LinearLayout ll_service_tel;

    @Bind(R.id.tv_service_tel)
    TextView tv_service_tel;


    private List<String> list;
    private List<List<HashMap<String, String>>> childlist;
    private HashMap<String, String> hashMap;
    private MyExpandableAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myservice);
    }

    @Override
    protected void initData() {
        setTvTitleText("客服");
        setImgBackVisiable(View.VISIBLE);
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
        YYMallApi.getArticleGetarticlelist(MyServiceActivity.this, new ApiCallback<MyServiceBean.DataBean>() {
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
            public void onNext(final MyServiceBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                tv_service_tel.setText(dataBean.getCustomer().getPhone());
                ll_service_qq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (CommonUtil.isQQClientAvailable(MyServiceActivity.this))
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + dataBean.getCustomer().getQq() + "")));
//                        else
//                            showToast("请先安装QQ");
                        Intent intent = new Intent();
                        intent.setClass(MyServiceActivity.this, ChatLoginActivity.class);
                        startActivity(intent);
                    }
                });
                ll_service_tel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent3 = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + dataBean.getCustomer().getPhone());
                        intent3.setData(data);
                        startActivity(intent3);
                    }
                });

                childlist = new ArrayList<>();
                list = new ArrayList<>();
                for (int i = 0; i < dataBean.getList().size(); i++) {
                    List<HashMap<String, String>> lists = new ArrayList<>();
                    list.add(dataBean.getList().get(i).getName() + "");
                    for (int a = 0; a < dataBean.getList().get(i).getList().size(); a++) {
                        hashMap = new HashMap<>();
                        hashMap.put("title", dataBean.getList().get(i).getList().get(a).getTitle() + "");
                        hashMap.put("articleId", dataBean.getList().get(i).getList().get(a).getArticleId() + "");
                        lists.add(hashMap);
                    }
                    childlist.add(lists);
                }
                el_service.setGroupIndicator(null);
                adapter = new MyExpandableAdapter(getApplicationContext(), list, childlist);
                el_service.setAdapter(adapter);
                el_service.expandGroup(0);
            }
        });
    }

    public class MyExpandableAdapter extends BaseExpandableListAdapter {
        private List<String> groupArray;
        private List<List<HashMap<String, String>>> childArray;
        private Context mContext;

        public MyExpandableAdapter(Context context, List<String> groupArray, List<List<HashMap<String, String>>> childArray) {
            mContext = context;
            this.groupArray = groupArray;
            this.childArray = childArray;
        }

        @Override
        public int getGroupCount() {
            return groupArray.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childArray.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupArray.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childArray.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder = null;
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_service_group, null);
                holder.img_servicegroup_right = (ImageView) convertView.findViewById(R.id.img_servicegroup_right);
                holder.tv_servicegroup_con = (TextView) convertView.findViewById(R.id.tv_servicegroup_con);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.tv_servicegroup_con.setText(groupArray.get(groupPosition));
            switch (groupPosition) {
                case 0:
                    Resources resources = mContext.getResources();
                    Drawable drawable = resources.getDrawable(R.mipmap.service_wen);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_servicegroup_con.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 1:
                    resources = mContext.getResources();
                    drawable = resources.getDrawable(R.mipmap.service_zl);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_servicegroup_con.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 2:
                    resources = mContext.getResources();
                    drawable = resources.getDrawable(R.mipmap.service_shopping);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_servicegroup_con.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 3:
                    resources = mContext.getResources();
                    drawable = resources.getDrawable(R.mipmap.service_wl);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_servicegroup_con.setCompoundDrawables(drawable, null, null, null);
                    break;
                case 4:
                    resources = mContext.getResources();
                    drawable = resources.getDrawable(R.mipmap.service_sh);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    holder.tv_servicegroup_con.setCompoundDrawables(drawable, null, null, null);
                    break;
                default:
                    break;
            }

            if (isExpanded) {
                holder.img_servicegroup_right.setBackgroundResource(R.mipmap.service_top);
            } else {
                holder.img_servicegroup_right.setBackgroundResource(R.mipmap.service_bottom);
            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder = null;
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_service_child, null);
                holder.tv_servicechild_con = (TextView) convertView.findViewById(R.id.tv_servicechild_con);
                holder.isc_ll_container = (LinearLayout) convertView.findViewById(R.id.isc_ll_container);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.tv_servicechild_con.setText(childArray.get(groupPosition).get(childPosition).get("title"));
            holder.isc_ll_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyServiceActivity.this, WebActivity.class);
                    //childArray.get(groupPosition).get(childPosition).get("title")
                    intent.putExtra("title","客服解答");
                    intent.putExtra(Constant.WEB_TAG.TAG, ApiService.SERVER_URL + childArray.get(groupPosition).get(childPosition).get("articleId"));
                    startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    class GroupHolder {
        public ImageView img_servicegroup_right;
        public TextView tv_servicegroup_con;
    }

    class ChildHolder {
        public LinearLayout isc_ll_container;
        public TextView tv_servicechild_con;
    }
}
