package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.util.luban.Luban;
import com.vise.xsnow.util.takephoto.app.TakePhoto;
import com.vise.xsnow.util.takephoto.app.TakePhotoImpl;
import com.vise.xsnow.util.takephoto.model.CropOptions;
import com.vise.xsnow.util.takephoto.model.InvokeParam;
import com.vise.xsnow.util.takephoto.model.TContextWrap;
import com.vise.xsnow.util.takephoto.model.TImage;
import com.vise.xsnow.util.takephoto.model.TResult;
import com.vise.xsnow.util.takephoto.permission.InvokeListener;
import com.vise.xsnow.util.takephoto.permission.PermissionManager;
import com.vise.xsnow.util.takephoto.permission.TakePhotoInvocationHandler;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.AfterMallBean;
import com.yhkj.yymall.bean.AfterMallPriceBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.FileUtils;
import com.yhkj.yymall.view.popwindows.ShopArgsPopupView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/1.
 */

public class ApplyAfterMallActivity extends BaseToolBarActivity  implements TakePhoto.TakeResultListener,InvokeListener {

    @Bind(R.id.aa_ed_desc)
    EditText mEdDesc;

    @Bind(R.id.aa_ll_reason)
    LinearLayout mLlReason;

    @Bind(R.id.as_tv_takephoto)
    TextView mTvTakePhoto;

    @Bind(R.id.aa_tv_reson)
    TextView mTvReson;

    @Bind(R.id.as_rl_upload_1)
    RelativeLayout mRlUpload1;

    @Bind(R.id.as_rl_upload_2)
    RelativeLayout mRlUpload2;

    @Bind(R.id.as_rl_upload_3)
    RelativeLayout mRlUpload3;

    @Bind(R.id.as_rl_upload_4)
    RelativeLayout mRlUpload4;

    @Bind(R.id.as_rl_upload_5)
    RelativeLayout mRlUpload5;

    @Bind(R.id.as_img_upload_1)
    ImageView mImgUpload1;

    @Bind(R.id.as_img_upload_2)
    ImageView mImgUpload2;

    @Bind(R.id.as_img_upload_3)
    ImageView mImgUpload3;

    @Bind(R.id.as_img_upload_4)
    ImageView mImgUpload4;

    @Bind(R.id.as_img_upload_5)
    ImageView mImgUpload5;

    @Bind(R.id.as_img_delete_1)
    ImageView mImgDelete1;

    @Bind(R.id.as_img_delete_2)
    ImageView mImgDelete2;

    @Bind(R.id.as_img_delete_3)
    ImageView mImgDelete3;

    @Bind(R.id.aa_ll_count)
    LinearLayout mLlCount;

    @Bind(R.id.aa_img_countarrow)
    ImageView mImgCountArrow;

    @Bind(R.id.as_img_delete_4)
    ImageView mImgDelete4;

    @Bind(R.id.as_img_delete_5)
    ImageView mImgDelete5;

    @Bind(R.id.aa_tv_submit)
    TextView mTvSubmit;

    @Bind(R.id.aa_ll_price)
    LinearLayout mLlPrice;

    @Bind(R.id.aa_tv_price)
    TextView mTvPrice;

    @Bind(R.id.aa_tv_numb)
    TextView mTvNumb;

    @Bind(R.id.aa_ll_service)
    LinearLayout mLlService;

    @Bind(R.id.aa_tv_service)
    TextView mTvService;

    @Bind(R.id.aa_ll_freightprice)
    LinearLayout mLlFreightPrice;

    @Bind(R.id.aa_tv_freightprice)
    TextView mTvFrightPrice;

    @Bind(R.id.aa_tv_freighttip)
    TextView mTvFreightTip;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type=PermissionManager.checkPermission(TContextWrap.of(this),invokeParam.getMethod());
        if(PermissionManager.TPermissionType.WAIT.equals(type)){
            this.invokeParam=invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type=PermissionManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PermissionManager.handlePermissionsResult(this,type,invokeParam,this);
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        ViseLog.e(result);
        File file = new File(image.getOriginalPath());
        compressWithRx(file);
    }


    //缩放图片
    private void compressWithRx(File file) {
        Observable.just(file)
                .map(new Func1<File, File>() {
                    @Override
                    public File call(File srcfile){
                        File thumbFile = null;
                        try{
                            thumbFile = Luban.with(ApplyAfterMallActivity.this).load(srcfile).get();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        return thumbFile;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File thumbfile) { // 参数类型 Bitmap
                        setPhotos(thumbfile);
                    }
                });
    }

    HashMap<Integer,File> mFilsMap = new HashMap<>();

    private void setPhotos(File fils){
        boolean bFind = false;
        for (int i=1;i<=5;i++){
            switch (i){
                case 1:
                    if (mRlUpload1.getVisibility() == GONE){
                        Glide.with(this).load(fils).into(mImgUpload1);
                        mRlUpload1.setVisibility(View.VISIBLE);
                        mFilsMap.put(1,fils);
                        bFind = true;
                    }
                    break;
                case 2:
                    if (mRlUpload2.getVisibility() == GONE){
                        Glide.with(this).load(fils).into(mImgUpload2);
                        mRlUpload2.setVisibility(View.VISIBLE);
                        mFilsMap.put(2,fils);
                        bFind = true;
                    }
                    break;
                case 3:
                    if (mRlUpload3.getVisibility() == GONE){
                        Glide.with(this).load(fils).into(mImgUpload3);
                        mRlUpload3.setVisibility(View.VISIBLE);
                        mFilsMap.put(3,fils);
                        bFind = true;
                    }
                    break;
                case 4:
                    if (mRlUpload4.getVisibility() == GONE){
                        Glide.with(this).load(fils).into(mImgUpload4);
                        mRlUpload4.setVisibility(View.VISIBLE);
                        mFilsMap.put(4,fils);
                        bFind = true;
                    }
                    break;
                case 5:
                    if (mRlUpload5.getVisibility() == GONE){
                        Glide.with(this).load(fils).into(mImgUpload5);
                        mRlUpload5.setVisibility(View.VISIBLE);
                        mFilsMap.put(5,fils);
                        bFind = true;
                    }
                    break;
            }
            if (bFind){
                break;
            }
        }
        if (mFilsMap.size() >= 5)
            mTvTakePhoto.setVisibility(GONE);
    }

    /**
     *  获取TakePhoto实例
     * @return
     */
    public TakePhoto getTakePhoto(){
        if (takePhoto==null){
            takePhoto= (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this,this));
        }
        return takePhoto;
    }

    public static MultipartBody filesToMultipartBody(HashMap<Integer,File> files) {
        if (files.size() == 0)
            return null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (Map.Entry<Integer, File> item : files.entrySet()){
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), item.getValue());
            builder.addFormDataPart("imgs[]",  item.getValue().getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyaftermall);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        mOrderGoodsId = getIntent().getIntExtra("orderGoodsId",0);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private int mAfterMallCount = 0;
    private AfterMallBean.DataBean mDataBean;
    private void getData() {
        YYMallApi.getAfterMallInfo(this, mOrderGoodsId, new YYMallApi.ApiResult<AfterMallBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                if (e.getCode() == 1001)
                    setNoDataView(R.mipmap.ic_nor_orderbg,e.getMessage());
                else
                    setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(AfterMallBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mDataBean = dataBean;
                mFreightPrice = Double.parseDouble(dataBean.getFrieght());
                mFreightStatus = dataBean.getFreightStatus();
                if (mDataBean.getIs_send() == 0){
                    //未发货
                    mImgCountArrow.setVisibility(GONE);
                    mAfterMallCount = dataBean.getGoodsNums();
                    mAfterPrice = dataBean.getAmount();
                    mAfterYy = dataBean.getYaya();
                    mYayaGive = dataBean.getYayaGive();
                    mIntegral = dataBean.getIntegral();
                    mTvNumb.setText(mAfterMallCount + "");
                    if (mIntegral != 0){
                        mTvPrice.setText(mIntegral + "积分");
                    }else {
                        if (mAfterYy!=0){
                            mTvPrice.setText("¥" + mAfterPrice +"+"+ mAfterYy + "丫丫");
                        }else{
                            mTvPrice.setText("¥" + mAfterPrice);
                        }

                    }
                    if (mFreightPrice == 0.0d){
                        mLlFreightPrice.setVisibility(GONE);
                        mTvFreightTip.setVisibility(GONE);
                    }else{
//                        mTvFrightPrice.setText("¥" + mFreightPrice);
//                        mLlFreightPrice.setVisibility(VISIBLE);
//                        mTvFreightTip.setVisibility(VISIBLE);
                    }

                    if (mServiceValue!=null && mServiceValue!=1)
                        mLlPrice.setVisibility(VISIBLE);
                    return;
                }
            }
        });
    }

    private Uri getLocalImgUri(){
        File file=new File(FileUtils.getCacheDirectory(this, Environment.DIRECTORY_PICTURES), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }
    //获取裁剪参数
    private CropOptions getCropOptions(){
        int height= 800;
        int width= 800;
        boolean withWonCrop=true;

        CropOptions.Builder builder=new CropOptions.Builder();

        builder.setAspectX(width).setAspectY(height);
//
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    int mOrderGoodsId;
//    String mPrice;

    private boolean mSelectRes =false;
//    private boolean mSelectService = false;
    private Integer mServiceValue = null;
    private AfterMallPriceBean.DataBean mPriceBean = null;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mLlService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list= new ArrayList<>();
                list.add("退货退款");
                list.add("换货");
                ShopArgsPopupView shopArgsPopupView = new ShopArgsPopupView<String>(ApplyAfterMallActivity.this,list) {
                    @Override
                    protected void bind(ViewHolder holder, final String bean, final int position) {
                        holder.setText(R.id.isa_tv_key,bean);
                        holder.setVisible(R.id.isa_tv_value,false);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //0-退货退款  1-换货
                                mServiceValue = position;
                                mTvService.setText(bean);
                                if (mServiceValue == 1){
                                    mLlPrice.setVisibility(GONE);
                                    mLlFreightPrice.setVisibility(GONE);
                                    mTvFreightTip.setVisibility(GONE);
                                }else{
                                    if (mDataBean.getIs_send() == 0){
                                        //未发货
                                        mLlPrice.setVisibility(VISIBLE);
                                        if (mFreightPrice != 0.0d) {
                                            mTvFrightPrice.setText("¥" + mFreightPrice);
                                            mLlFreightPrice.setVisibility(VISIBLE);
                                            mTvFreightTip.setVisibility(VISIBLE);
                                        }
                                    }else{
                                        //已发货
                                        if (mPriceBean!=null){
                                            mLlPrice.setVisibility(VISIBLE);
                                            if (mFreightPrice != 0.0d) {
                                                mTvFrightPrice.setText("¥" + mFreightPrice);
                                                mLlFreightPrice.setVisibility(VISIBLE);
                                                mTvFreightTip.setVisibility(VISIBLE);
                                            }
                                        }
                                    }
                                }
                                dismiss();
                            }
                        });
                    }
                };
                shopArgsPopupView.setTvTitle("请选择售后服务");
                shopArgsPopupView.showPopupWindow();
            }
        });
        mLlCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean.getGoodsNums()== 0 || mDataBean.getIs_send() == 0) return;

                final NumberPicker picker = new NumberPicker(ApplyAfterMallActivity.this);
                picker.setRange(1,mDataBean.getGoodsNums());
                picker.setAnimationStyle(R.style.Animation_CustomPopup);
                picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int index, final Number item) {
                        YYMallApi.getAfterMallPrice(ApplyAfterMallActivity.this, mOrderGoodsId, item.intValue(),
                                new YYMallApi.ApiResult<AfterMallPriceBean.DataBean>(ApplyAfterMallActivity.this) {
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
                            public void onNext(AfterMallPriceBean.DataBean dataBean) {
                                mPriceBean = dataBean;
                                mAfterMallCount = item.intValue();
                                mIntegral = dataBean.getIntegral();
                                mYayaGive = dataBean.getYayaGive();
                                updatePrice(dataBean);
                            }
                        });
                    }
                });
                picker.show();
            }
        });
        mLlReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list= new ArrayList<>();
                list.add("拍错/多拍/不喜欢");
                list.add("商品成分描述不符合");
                list.add("生产日期/保质期描述不符合");
                list.add("卖家发错货");
                list.add("收到商品少件或破损");
                list.add("七天无理由退货");
                list.add("其他");
                ShopArgsPopupView shopArgsPopupView = new ShopArgsPopupView<String>(ApplyAfterMallActivity.this,list) {
                    @Override
                    protected void bind(ViewHolder holder, final String bean, int position) {
                        holder.setText(R.id.isa_tv_key,bean);
                        holder.setVisible(R.id.isa_tv_value,false);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mSelectRes = true;
                                mTvReson.setText(bean);
                                dismissWithOutAnima();
//                                showToast(bean);
                            }
                        });
                    }
                };
                shopArgsPopupView.setTvTitle("请选择售后原因");
                shopArgsPopupView.showPopupWindow();
            }
        });
        mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceValue == null){
                    showToast("请选择售后服务");
                    return;
                }
                String desc = mEdDesc.getText().toString();
                if (!mSelectRes){
                    showToast("请选择售后原因");
                    return;
                }
                if (mAfterMallCount == 0){
                    showToast("请选择售后商品件数");
                    return;
                }
                HashMap hashMap = new HashMap();
                hashMap.put("orderGoodsId",mDataBean.getOrderId());
//                hashMap.put("type",mContents);
                hashMap.put("type","测试");
                if (!TextUtils.isEmpty(desc)) {
                    hashMap.put("contents",desc);
                }
                hashMap.put("amount",mAfterPrice);
                hashMap.put("wamount",mDataBean.getWamount());
                hashMap.put("type",mTvReson.getText());
                hashMap.put("count",mAfterMallCount);
                hashMap.put("yayaGive",mYayaGive);
                hashMap.put("integral",mIntegral);
                hashMap.put("yaya",mAfterYy);
                hashMap.put("refType",mServiceValue);
                YYMallApi.submitShopAfter(ApplyAfterMallActivity.this, hashMap, filesToMultipartBody(mFilsMap),
                        new YYMallApi.ApiResult<CommonBean>(ApplyAfterMallActivity.this) {
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
                        showToast("申请成功");
                        setResult(RESULT_OK);
                        AppManager.getInstance().finishActivity(PladgeDetailActivity.class);
                        AppManager.getInstance().finishActivity(ApplyAfterMallActivity.this);
                    }
                });
            }
        });

        mTvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApplyAfterMallActivity.this);
                builder.setTitle("设置头像");
                builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            getTakePhoto().onPickFromCaptureWithCrop(getLocalImgUri(),getCropOptions());
                        }else if (which == 1){
                            //相册
                            getTakePhoto().onPickFromGalleryWithCrop(getLocalImgUri(),getCropOptions());
                        }
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        });

        mImgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilsMap.remove(1);
                mRlUpload1.setVisibility(GONE);
                if (mFilsMap.size() < 5)
                    mTvTakePhoto.setVisibility(View.VISIBLE);
            }
        });
        mImgDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilsMap.remove(2);
                mRlUpload2.setVisibility(GONE);
                if (mFilsMap.size() < 5)
                    mTvTakePhoto.setVisibility(View.VISIBLE);
            }
        });
        mImgDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilsMap.remove(3);
                mRlUpload3.setVisibility(GONE);
                if (mFilsMap.size() < 5)
                    mTvTakePhoto.setVisibility(View.VISIBLE);
            }
        });
        mImgDelete4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilsMap.remove(4);
                mRlUpload4.setVisibility(GONE);
                if (mFilsMap.size() < 5)
                    mTvTakePhoto.setVisibility(View.VISIBLE);
            }
        });
        mImgDelete5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilsMap.remove(5);
                mRlUpload5.setVisibility(GONE);
                if (mFilsMap.size() < 5)
                    mTvTakePhoto.setVisibility(View.VISIBLE);
            }
        });
    }

    private int mAfterYy;
    private String mAfterPrice;
    private int mYayaGive;
    private int mIntegral;
    private double mFreightPrice;
    private int mFreightStatus;
    private void updatePrice(AfterMallPriceBean.DataBean dataBean) {
        mAfterPrice = dataBean.getAmount();
        mAfterYy = dataBean.getYaya();
        mTvNumb.setText(mAfterMallCount + "");
        if (mIntegral != 0){
            mTvPrice.setText(mIntegral + "积分");
        }else {
            if (mAfterYy!=0){
                mTvPrice.setText("¥" + mAfterPrice +"+"+ mAfterYy + "丫丫");
            }else{
                mTvPrice.setText("¥" + mAfterPrice);
            }
        }

//        mTvPrice.setText(mAfterPrice);
        if (mServiceValue!=null && mServiceValue!=1){
            mLlPrice.setVisibility(VISIBLE);
            if (mFreightPrice != 0.0d){
                mTvFrightPrice.setText("¥" + mFreightPrice);
                mLlFreightPrice.setVisibility(VISIBLE);
                mTvFreightTip.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("申请售后");
    }
}
