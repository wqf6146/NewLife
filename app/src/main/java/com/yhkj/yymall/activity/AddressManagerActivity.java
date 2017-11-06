package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.AddressListBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/12.
 */

public class AddressManagerActivity extends BaseToolBarActivity {

    @Bind(R.id.aa_recycleview)
    RecyclerView mRecycleView;

//    @Bind(R.id.aa_rl_none)
//    RelativeLayout mRlnone;

    @Bind(R.id.aa_ll_addplace)
    LinearLayout mLlAddPlace;

//    @Bind(R.id.aa_tv_addaddress)
//    TextView mTvAddAddress;

    private CommonAdapter mAdapter;

    private int mStatus = Constant.TYPE_PLACES.NORMAL;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressmanage);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean != null && mDataBean.getList().size() > 0)
                    setResult(Integer.valueOf(mDataBean.getList().get(0).getId()));
                AppManager.getInstance().finishActivity(AddressManagerActivity.this);
            }
        });
        mLlAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressManagerActivity.this,EditPlacesActivity.class);
                intent.putExtra(Constant.TYPE_PLACES.TYPE,Constant.TYPE_PLACES.ADD);
                startActivityForResult(intent,Constant.TYPE_RELUST.CODE);
            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        if (mDataBean != null && mDataBean.getList().size() > 0)
            setResult(Integer.valueOf(mDataBean.getList().get(0).getId()));
        super.onBackPressedSupport();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.TYPE_RELUST.CODE && resultCode == RESULT_OK){
            getData();
        }
    }

    @Override
    protected void initData() {
        mStatus = getIntent().getIntExtra(Constant.TYPE_PLACES.TYPE,Constant.TYPE_PLACES.NORMAL);
        setTvTitleText("地址管理");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        if (bNoSetAddress){
            Intent intent = new Intent(AddressManagerActivity.this,EditPlacesActivity.class);
            intent.putExtra(Constant.TYPE_PLACES.TYPE,Constant.TYPE_PLACES.ADD);
            startActivityForResult(intent,Constant.TYPE_RELUST.CODE);
        }else{
            getData();
        }
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private void getData(){
        YYMallApi.getAddressList(this, new YYMallApi.ApiResult<AddressListBean.DataBean>(AddressManagerActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNoDataView(R.mipmap.ic_nor_nonetwork,"无法连接","重新加载");
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final AddressListBean.DataBean dataBean) {
                mDataBean = dataBean;
                setListData(dataBean);
            }
        });
    }

    private AddressListBean.DataBean mDataBean;
    private boolean bNoSetAddress = false;
    private void setListData(final AddressListBean.DataBean dataBean){
        if (dataBean != null && dataBean.getList() !=null && dataBean.getList().size() > 0){
            bNoSetAddress =false;
            setNetWorkErrShow(GONE);
            mAdapter = new CommonAdapter<AddressListBean.DataBean.ListBean>(AddressManagerActivity.this,R.layout.item_address,dataBean.getList()) {

                // 设置默认地址
                private void updateOrder(String position){
                    int fromPosition=0;
                    for (int i=0; i<mDatas.size();i++){
                        AddressListBean.DataBean.ListBean bean = mDatas.get(i);
                        if (!bean.getId().equals(position)) {
                            bean.setIs_default("0");
                            notifyItemChanged(i);
                        } else {
                            mDatas.remove(i);
                            fromPosition = i;
                            bean.setIs_default("1");
                            notifyItemChanged(i);
                            mDatas.add(0,bean);
                        }
                    }
                    notifyItemMoved(fromPosition,0);
                    mRecycleView.scrollToPosition(0);
                }

                @Override
                protected void convert(ViewHolder holder, final AddressListBean.DataBean.ListBean listBean, final int position) {

                    holder.setOnClickListener(R.id.ia_ll_bg, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mStatus == Constant.TYPE_PLACES.SELECT){
                                setResult(Integer.valueOf(listBean.getId()));
                                AppManager.getInstance().finishActivity(AddressManagerActivity.this);
                            }
                        }
                    });

                    holder.setText(R.id.ia_tv_name,listBean.getAccept_name());
                    holder.setText(R.id.ia_tv_phone,listBean.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    holder.setText(R.id.ia_tv_address,listBean.getProvince()+listBean.getCity()+listBean.getArea()+listBean.getStreet()+listBean.getAddress());
                    if (listBean.getIs_default().equals("1")){
                        holder.setImageResource(R.id.ia_img_defaddress, R.mipmap.ic_nor_bluenike);
                        holder.setText(R.id.ia_tv_defalutaddress,"默认地址");
                    }else{
                        holder.setImageResource(R.id.ia_img_defaddress, R.mipmap.ic_nor_graycicle);
                        holder.setText(R.id.ia_tv_defalutaddress,"设为默认");
                    }


                    holder.setOnClickListener(R.id.ia_img_defaddress, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listBean.getIs_default().equals("1")) return;
                            YYMallApi.setDefAddress(AddressManagerActivity.this, listBean.getId(), new YYMallApi.ApiResult<CommonBean>(AddressManagerActivity.this) {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onError(ApiException e) {
                                    super.onError(e);
                                    ViseLog.e(e);
                                    showToast(e.getMessage());
                                }

                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onNext(CommonBean commonBean) {
                                    showToast("设置成功");
                                    updateOrder(listBean.getId());
                                }
                            });
                        }
                    });

                    holder.setOnClickListener(R.id.ia_tv_edit,new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AddressManagerActivity.this,EditPlacesActivity.class);
                            intent.putExtra(Constant.TYPE_PLACES.ENTITY,listBean);
                            intent.putExtra(Constant.TYPE_PLACES.TYPE,Constant.TYPE_PLACES.UPDATE);
                            startActivityForResult(intent,Constant.TYPE_RELUST.CODE);
                        }
                    });
                    holder.setOnClickListener(R.id.ia_tv_del,new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddressManagerActivity.this);
                            builder.setTitle("删除确认");
                            builder.setMessage("确定要删除该收货地址么？");
                            builder.setPositiveButton("取消", null);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    YYMallApi.delAddress(AddressManagerActivity.this, listBean.getId(), new YYMallApi.ApiResult<CommonBean>(AddressManagerActivity.this) {
                                        @Override
                                        public void onStart() {

                                        }

                                        @Override
                                        public void onError(ApiException e) {
                                            super.onError(e);
                                            ViseLog.e(e);
                                            showToast(e.getMessage());
                                        }

                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onNext(CommonBean commonBean) {
                                            showToast("删除成功");
                                            notifyItemRemoved(position);
                                            getDatas().remove(position);
                                            notifyItemChanged(position,getDatas().size()-1);
                                        }
                                    });
                                }
                            });
                            builder.show();
                        }
                    });
                }
            };
            mRecycleView.setAdapter(mAdapter);
        }else{
            bNoSetAddress = true;
            setNoDataView(R.mipmap.ic_nor_noaddress,"暂未设置\n没有收货地址寄不了快递哦","新增地址");
            return;
        }
    }
}
