package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
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
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/5.
 */

public class AdvanceActivity extends BaseToolBarActivity  implements TakePhoto.TakeResultListener,InvokeListener {

    @Bind(R.id.aa_img_question1)
    ImageView mImgQuestion1;

    @Bind(R.id.aa_img_question2)
    ImageView mImgQuestion2;

    @Bind(R.id.aa_img_question3)
    ImageView mImgQuestion3;

    @Bind(R.id.aa_img_question4)
    ImageView mImgQuestion4;

    @Bind(R.id.as_et_content)
    EditText mEtContent;

//    @Bind(R.id.aa_tv_name)
//    TextView mTvName;

//    @Bind(R.id.aa_tv_phone)
//    TextView mTvPhone;

    @Bind(R.id.aa_tv_commit)
    TextView mTvCommit;


    @Bind(R.id.as_tv_takephoto)
    TextView mTvTakePhoto;

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

    @Bind(R.id.as_img_delete_4)
    ImageView mImgDelete4;

    @Bind(R.id.as_img_delete_5)
    ImageView mImgDelete5;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

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
                            thumbFile = Luban.with(AdvanceActivity.this).load(srcfile).get();
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
        setContentView(R.layout.activity_advance);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
//        mTvName.setText(getIntent().getStringExtra("name"));
//        mTvPhone.setText(getIntent().getStringExtra("phone"));
    }

    private void initAdvanceTab( int type){
        mImgQuestion1.setImageResource(R.mipmap.ic_nor_graycicle);
        mImgQuestion2.setImageResource(R.mipmap.ic_nor_graycicle);
        mImgQuestion3.setImageResource(R.mipmap.ic_nor_graycicle);
        mImgQuestion4.setImageResource(R.mipmap.ic_nor_graycicle);
        switch (type){
            case 1:
                mImgQuestion1.setImageResource(R.mipmap.ic_nor_bluenike);
                mType = "1";
                break;
            case 2:
                mImgQuestion2.setImageResource(R.mipmap.ic_nor_bluenike);
                mType = "2";
                break;
            case 3:
                mImgQuestion3.setImageResource(R.mipmap.ic_nor_bluenike);
                mType = "3";
                break;
            case 4:
                mImgQuestion4.setImageResource(R.mipmap.ic_nor_bluenike);
                mType = "4";
                break;
        }
    }

    private String mType = "1";
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgQuestion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initAdvanceTab(1);
                }
            }
        );
        mImgQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAdvanceTab(2);
            }}
        );
        mImgQuestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAdvanceTab(3);
            }}
        );
        mImgQuestion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAdvanceTab(4);
            }}
        );
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtContent.getText().toString();
                if (TextUtils.isEmpty(content)){
                    showToast("请填写意见");
                    return;
                }

                HashMap hashMap = new HashMap();
                hashMap.put("content",content);
                hashMap.put("type",mType);

                YYMallApi.submitAdvance(AdvanceActivity.this, hashMap, filesToMultipartBody(mFilsMap), new YYMallApi.ApiResult<CommonBean>(AdvanceActivity.this) {
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
                        showToast("提交成功");
                        AppManager.getInstance().finishActivity(AdvanceActivity.this);
                    }
                });
            }
        });

        mTvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceActivity.this);
                builder.setTitle("选取图片");
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

    @Override
    protected void initData() {
        setTvTitleText("意见反馈");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

    }
}

