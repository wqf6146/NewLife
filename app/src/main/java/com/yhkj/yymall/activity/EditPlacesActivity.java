package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.AddressListBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.PlacesBean;
import com.yhkj.yymall.bean.UpdateOrderAddressBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.addressselect.BottomDialog;
import com.yhkj.yymall.view.addressselect.OnAddressSelectedListener;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/12.
 */

public class EditPlacesActivity extends BaseToolBarActivity implements OnAddressSelectedListener {

    private int mType;

    @Bind(R.id.ae_rl_defalplace)
    RelativeLayout mRlDefalplace;

    @Bind(R.id.ae_tv_delplaces)
    TextView mTvDelPlaces;

    @Bind(R.id.ae_edit_name)
    EditText mEditName;

    @Bind(R.id.ae_edit_phone)
    EditText mEditPhone;

    @Bind(R.id.ae_tv_places)
    TextView mTvPlaces;

    @Bind(R.id.ae_edit_placesdesc)
    EditText mEditPlaceDesc;

    @Bind(R.id.ae_img_defaddress)
    ImageView mImgDefAddress;

    private int mSelectDefAddress = 0;

    private String mPlaces;

    private String mProvinsId,mCityId,mAreaId,mStreetId;

    private String mAddressId;

    private AddressListBean.DataBean.ListBean mEntityBean;

    private int mOrderGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplace);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        mType = getIntent().getIntExtra(Constant.TYPE_PLACES.TYPE,Constant.TYPE_PLACES.ADD);
    }

    @Override
    public void onAddressSelected(PlacesBean.DataBean province, PlacesBean.DataBean city, PlacesBean.DataBean county, PlacesBean.DataBean street) {
        mAddressdialog.dismiss();
        mProvinsId = province.getArea_id();
        mCityId = city.getArea_id();
        mAreaId = county.getArea_id();
        if (street != null) {
            mStreetId = street.getArea_id();
            mPlaces = province.getArea_name() + city.getArea_name() + county.getArea_name() + street.getArea_name();
        }else {
            mPlaces =  province.getArea_name() + city.getArea_name() + county.getArea_name();
        }
        mTvPlaces.setText(mPlaces);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvDelPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPlacesActivity.this);
                builder.setTitle("删除确认");
                builder.setMessage("确定要删除该收货地址么？");
                builder.setPositiveButton("取消", null);
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YYMallApi.delAddress(EditPlacesActivity.this, mEntityBean.getId(), new YYMallApi.ApiResult<CommonBean>(EditPlacesActivity.this) {
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
                                setResult(RESULT_OK);
                                AppManager.getInstance().finishActivity(EditPlacesActivity.this);
                            }
                        });
                    }
                });
                builder.show();
            }
        });
        mImgDefAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectDefAddress == 0){
                    mSelectDefAddress = 1;
                    mImgDefAddress.setImageResource(R.mipmap.ic_nor_bluenike);
                }else{
                    mSelectDefAddress = 0;
                    mImgDefAddress.setImageResource(R.mipmap.ic_nor_graycicle);
                }
            }
        });
        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mEditName.getText().toString();
                String phone = mEditPhone.getText().toString();
                String placedesc = mEditPlaceDesc.getText().toString();
                if (TextUtils.isEmpty(name) ){
                    showToast("请填写收货人");
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    showToast("请填写手机号码");
                    return;
                }
                if (phone.length() < 11){
                    showToast("手机号码格式不正确");
                    return;
                }
                if (TextUtils.isEmpty(mPlaces)){
                    showToast("请选择省市地区");
                    return;
                }
                if (TextUtils.isEmpty(placedesc) || (!TextUtils.isEmpty(placedesc) && (placedesc.length() <=4 || placedesc.length() >= 130))){
                    showToast("请输入正确的详细地址(5-120个字)");
                    return;
                }

                if (mType == Constant.TYPE_PLACES.ADD){
                        YYMallApi.addAddress(EditPlacesActivity.this, name, "", mProvinsId, mCityId, mAreaId, mStreetId,placedesc, phone,
                                String.valueOf(mSelectDefAddress), new YYMallApi.ApiResult<CommonBean>(EditPlacesActivity.this) {
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
                                showToast("添加成功");
                                setResult(RESULT_OK);
                                AppManager.getInstance().finishActivity(EditPlacesActivity.this);
                            }
                        });
                    }else if (mType == Constant.TYPE_PLACES.ORDER){
                        if (mOrderGroupId == 0){
                            showToast("订单编号错误");
                            return;
                        }
                        YYMallApi.updateOrderAddress(EditPlacesActivity.this,mOrderGroupId ,name, String.valueOf(mProvinsId),  String.valueOf(mCityId),
                                String.valueOf(mAreaId), placedesc, phone, new YYMallApi.ApiResult<CommonBean>(EditPlacesActivity.this) {
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
                                showToast("修改成功");
                                setResult(RESULT_OK);
                                AppManager.getInstance().finishActivity(EditPlacesActivity.this);
                            }
                        });
                    } else{
                        YYMallApi.updateAddress(EditPlacesActivity.this, name, mEntityBean.getId(), "123456",mProvinsId, mCityId, mAreaId, mStreetId,placedesc, phone,
                                new YYMallApi.ApiResult<CommonBean>(EditPlacesActivity.this) {
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
                                showToast("修改成功");
                                setResult(RESULT_OK);
                                AppManager.getInstance().finishActivity(EditPlacesActivity.this);
                            }
                        });
                    }
            }
        });
        mTvPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddressdialog = new BottomDialog(EditPlacesActivity.this);
                mAddressdialog.setOnAddressSelectedListener(EditPlacesActivity.this);
                mAddressdialog.show();
            }
        });
    }
    private BottomDialog mAddressdialog;
    @Override
    protected void initData() {
        mEntityBean = getIntent().getParcelableExtra(Constant.TYPE_PLACES.ENTITY);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvRightText("完成");
        if(mType == Constant.TYPE_PLACES.ADD){
            setTvTitleText("新增地址");
            mTvDelPlaces.setVisibility(View.INVISIBLE);
        }else if(mType == Constant.TYPE_PLACES.ORDER){
            setTvTitleText("修改地址");
            mRlDefalplace.setVisibility(GONE);
            mOrderGroupId = getIntent().getIntExtra("orderGroupId",0);
            mTvDelPlaces.setVisibility(View.INVISIBLE);

            YYMallApi.getOrderAddress(EditPlacesActivity.this, mOrderGroupId,
                    new YYMallApi.ApiResult<UpdateOrderAddressBean.DataBean>(EditPlacesActivity.this) {
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
                public void onNext(UpdateOrderAddressBean.DataBean updateOrderAddressBean) {
                    mEditName.setText(updateOrderAddressBean.getAccept_name());
                    mEditPhone.setText(updateOrderAddressBean.getMobile());
                    mPlaces  = updateOrderAddressBean.getContetn();
                    mTvPlaces.setText(mPlaces);
                    mEditPlaceDesc.setText(updateOrderAddressBean.getAddress());
                    mProvinsId = String.valueOf(updateOrderAddressBean.getProvince());
                    mCityId = String.valueOf(updateOrderAddressBean.getCity());
                    mAreaId = String.valueOf(updateOrderAddressBean.getArea());
//                    mStreetId = updateOrderAddressBean.get();
                }
            });

        }else{
            setTvTitleText("编辑地址");
            mRlDefalplace.setVisibility(View.INVISIBLE);
            if (mEntityBean!=null){
                mEditName.setText(mEntityBean.getAccept_name());
                mEditPhone.setText(mEntityBean.getMobile());
                mPlaces  = mEntityBean.getProvince()+mEntityBean.getCity()+mEntityBean.getArea()+mEntityBean.getStreet();
                mTvPlaces.setText(mPlaces);
                mEditPlaceDesc.setText(mEntityBean.getAddress());
                mProvinsId = mEntityBean.getProvince_id();
                mCityId = mEntityBean.getCity_id();
                mAreaId = mEntityBean.getArea_id();
                mStreetId = mEntityBean.getStreet_id();
            }
        }
    }
}
