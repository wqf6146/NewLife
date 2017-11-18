package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusFactory;
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
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
import com.yhkj.yymall.adapter.PeopleInfoAdatper;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.PersonBean;
import com.yhkj.yymall.event.AvatarUpdateEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.util.FileUtils;

import java.io.File;

import butterknife.Bind;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vise.xsnow.util.InputMethodUtils.showInputMethod;

/**
 * Created by Administrator on 2017/7/3.
 */

public class UserDataActivity extends BaseToolBarActivity implements TakePhoto.TakeResultListener,InvokeListener {


    @Bind(R.id.au_rl_useravatar)
    RelativeLayout mRlUserAvatar;

    @Bind(R.id.au_img_user)
    ImageView mImgUser;

    @Bind(R.id.au_tv_name)
    TextView mTvName;

    @Bind(R.id.au_tv_sex)
    TextView mTvSex;

    @Bind(R.id.au_tv_viplevels)
    TextView mTvVipLevels;

    @Bind(R.id.au_rl_name)
    RelativeLayout mRlName;

    @Bind(R.id.au_rl_editname)
    RelativeLayout mRlEditName;

    @Bind(R.id.au_edit_name)
    EditText mEditName;

    @Bind(R.id.au_btn_clearname)
    ImageView mImgClearName;

    @Bind(R.id.au_rl_sex)
    RelativeLayout mRlSex;

    @Bind(R.id.au_ll_bg)
    LinearLayout mLLbg;

    @Bind(R.id.au_ll_addpeople)
    LinearLayout mLlAddPeople;

    @Bind(R.id.au_listview)
    ListView mListView;

    @Bind(R.id.au_rl_viplevel)
    RelativeLayout mRlVipLevels;


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

    private File mAvatarFile;

    private boolean bEditName = false;
    @Override
    public void takeSuccess(TResult result) {
        TImage image = result.getImage();
        ViseLog.e(result);
        mAvatarFile = new File(image.getOriginalPath());
        compressWithRx(mAvatarFile);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdata);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initView() {
        super.initView();


    }

    private void getFoucs(){
        if (bEditName){
            bEditName = false;
            mLLbg.setFocusable(true);
            mLLbg.setFocusableInTouchMode(true);
            mLLbg.requestFocus();
            InputMethodManager imm = (InputMethodManager) UserDataActivity.this.getSystemService(UserDataActivity.this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            imm.hideSoftInputFromWindow(mLLbg.getWindowToken(), 0);
        }
    }



    @Override
    protected void bindEvent() {
        super.bindEvent();
        mLlAddPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BayByInfoActivity.class);
            }
        });
        mLLbg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getFoucs();
                return false;
            }
        });
        mEditName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String name = mEditName.getText().toString();
                    if (!TextUtils.isEmpty(name)) {
                        mTvName.setText(name);
                    }
                    mRlEditName.setVisibility(GONE);
                    mRlName.setVisibility(View.VISIBLE);
                }
            }
        });
        mRlUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoucs();
                AlertDialog.Builder builder = new AlertDialog.Builder(UserDataActivity.this);
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

        mRlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bEditName = true;
                mRlEditName.setVisibility(View.VISIBLE);
                mRlName.setVisibility(GONE);
                mEditName.requestFocus();
                showInputMethod(UserDataActivity.this);
            }
        });
        mImgClearName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditName.setText("");
            }
        });

        mRlSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoucs();
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserDataActivity.this);
                builder.setTitle("选择性别");
                builder.setItems(new String[]{"男","女"},new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            mTvSex.setText("男");
                        }else if (which == 1){
                            mTvSex.setText("女");
                        }
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        });

        mRlVipLevels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MyLevelsActivity.class);
            }
        });

        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFoucs();
                String name = mTvName.getText().toString();
                String sex = mTvSex.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(sex)){
                    YYMallApi.updateUserInfo(UserDataActivity.this, mAvatarFile, name, sex.equals("男") ? 1 : 2, new YYMallApi.ApiResult<CommonBean>(UserDataActivity.this) {
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
                        public void onNext(CommonBean o) {
                            showToast("保存成功");
                            BusFactory.getBus().post(new AvatarUpdateEvent());
                            AppManager.getInstance().finishActivity(UserDataActivity.this);
                        }
                    });
                }else{
                    if (TextUtils.isEmpty(name)){
                        showToast("请填写昵称");
                        return;
                    }
                    if (TextUtils.isEmpty(sex)){
                        showToast("请选择性别");
                        return;
                    }
                }
            }
        });
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
    private Uri getLocalImgUri(){
        File file=new File(FileUtils.getCacheDirectory(this, Environment.DIRECTORY_PICTURES), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }

    @Override
    protected void initData() {
        setTvTitleText("个人资料");
        setTvRightText("保存");
        setTvRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    private void getData() {
        YYMallApi.getPersonalInfo(this, new YYMallApi.ApiResult<PersonBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final PersonBean.DataBean dataBean) {
                setData(dataBean);
            }
        });
    }

    private void setData(final PersonBean.DataBean dataBean){
        setNetWorkErrShow(GONE);
        if (mAvatarFile!= null){
            Glide.with(UserDataActivity.this).load(mAvatarFile).asBitmap().into(new BitmapImageViewTarget(mImgUser) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgUser.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else{
            Glide.with(UserDataActivity.this).load(dataBean.getHead_ico())
                    .asBitmap().thumbnail(0.1f).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(mImgUser) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgUser.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        mTvName.setText(dataBean.getName());
        mTvSex.setText(dataBean.getSex() == 0 ? "未设置" : dataBean.getSex() == 1 ? "男" : "女");
        mTvVipLevels.setText("LV" + dataBean.getLevel());
        if (dataBean.getBabyInfo().getCount() >=2 ){
            mLlAddPeople.setVisibility(GONE);
        }else{
            mLlAddPeople.setVisibility(VISIBLE);
        }
        mListView.setAdapter(new PeopleInfoAdatper(UserDataActivity.this,dataBean.getBabyInfo()).setDeleteClickLisiten(new PeopleInfoAdatper.OnDeleteEvent() {
            @Override
            public void onDeleteClickLisiten() {
                mLlAddPeople.setVisibility(VISIBLE);
            }
        }));
    }

    //缩放图片
    private void compressWithRx(File file) {
        Log.e("cur",file.length() / 1024 + "k");
        Log.e("cur xy",CommonUtil.computeImgFileSize(file)[0] + "*" + CommonUtil.computeImgFileSize(file)[1]);

        Observable.just(file)
                .map(new Func1<File, File>() {
                    @Override
                    public File call(File srcfile){
                        File thumbFile = null;
                        try{
                            thumbFile = Luban.with(UserDataActivity.this).load(srcfile).get();
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
                        mAvatarFile = thumbfile;
                        Glide.with(UserDataActivity.this).load(thumbfile).asBitmap().centerCrop().into(new BitmapImageViewTarget(mImgUser) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                mImgUser.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        Log.e("thumb",thumbfile.length() / 1024 + "k");
                        Log.e("thumb xy",CommonUtil.computeImgFileSize(thumbfile)[0] + "*" + CommonUtil.computeImgFileSize(thumbfile)[1]);
                    }
                });
    }
}
