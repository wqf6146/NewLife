package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
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
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerItemBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.OrderEvaBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.FileUtils;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

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
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/27.
 */

public class ShopEvaluateActivity extends BaseToolBarActivity  implements TakePhoto.TakeResultListener,InvokeListener {

    @Bind(R.id.as_recycleview)
    RecyclerView mRecycleview;


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
        Log.e("取消了","取消了");
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeSuccess(TResult result) {
        mLazeStackVector.get(mCurEditTag).pop();
        TImage image = result.getImage();
        ViseLog.e(result);
        File file = new File(image.getOriginalPath());
        compressWithRx(file);
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
        setContentView(R.layout.activity_shopevaluate);
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
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
    }



    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交

                for (int i=0; i<mDatas.getGoodses().size();i++){
//                    String orderGoodsId,
//                    String contents,
//                    String point,
//                    String imgs,
//                    String impressions,
                    HashMap<String,Object> argsMap = new HashMap();
                    argsMap.put("orderGoodsId",String.valueOf(mDatas.getGoodses().get(i).getId()));

                    argsMap.put("contents",TextUtils.isEmpty(mContentList.get(i)) ? "此用户没有填写评价" : mContentList.get(i));
                    argsMap.put("point",String.valueOf(mScoreList.get(i)));

                    String[] strings = new String[mImpList.get(i).size()];
                    mImpList.get(i).toArray(strings);

                    try{
                        YYMallApi.submitShopEva(ShopEvaluateActivity.this,strings, argsMap, filesToMultipartBody(mImgList.get(i)),
                                new YYMallApi.ApiResult<CommonBean>(ShopEvaluateActivity.this) {
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
                                ViseLog.e(commonBean);
                                showToast("评论成功");
                                setResult(RESULT_OK);
                                AppManager.getInstance().finishActivity(ShopEvaluateActivity.this);
                            }
                        });
                    }catch (Exception e){
                        showToast(e.getMessage());
                        e.printStackTrace();
                    }


                }

            }
        });
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


    //缩放图片
    private void compressWithRx(File file) {
        Observable.just(file)
                .map(new Func1<File, File>() {
                    @Override
                    public File call(File srcfile){
                        File thumbFile = null;
                        try{
                            thumbFile = Luban.with(ShopEvaluateActivity.this).load(srcfile).get();
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

//    private int mPhotoCount = 0;
    private int[] mPhotoNumbArr; //记录当前各商品图片数量

    @Override
    protected void initData() {
        setTvTitleText("评价");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
       getData();

//        YYMallApi.getGoodsImp(this, getIntent().getStringExtra("id"), new ApiCallback<GoodsTagBean.DataBean>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onError(ApiException e) {
//                ViseLog.e(e);
//                showToast(e.getMessage());
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onNext(GoodsTagBean.DataBean dataBean) {
//                setData(dataBean);
//            }
//        });
    }

    private void getData() {
        YYMallApi.getOrderEva(this, getIntent().getStringExtra("orderid"), new YYMallApi.ApiResult<OrderEvaBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                setNetWorkErrShow(VISIBLE);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(OrderEvaBean.DataBean dataBean) {
                setData(dataBean);
                setNetWorkErrShow(GONE);
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Bind(R.id.as_tv_commit)
    TextView mTvCommit;

    private HashMap<Integer,String> mContentList = new HashMap<>(); //内容输入列表
    private HashMap<Integer,String> mScoreList = new HashMap<>(); //评分输入列表
    private LinkedHashMap<Integer,LinkedHashMap<Integer,File>> mImgList = new LinkedHashMap<>(); //图片输入列表
    private LinkedHashMap<Integer,List<String>> mImpList = new LinkedHashMap<>(); //印象输入列表

    Vector<Stack<Integer>> mLazeStackVector = new Vector<>();


    private int mCurEditTag; //当前编辑条目
    private ImageView mCurEditImageView;
    private View mCurTvTakePhoto;
    private RelativeLayout mCurEditLayout;

    private void setPhotos(File fils){
        mImgList.get(mCurEditTag).put(mCurPlaces,fils);
        mCurEditLayout.setVisibility(View.VISIBLE);
        Glide.with(ShopEvaluateActivity.this).load(fils).into(mCurEditImageView);

        mPhotoNumbArr[mCurEditTag]++;
        if (mPhotoNumbArr[mCurEditTag] >=5 )
            mCurTvTakePhoto.setVisibility(GONE);

        Log.e("img",new Gson().toJson(mImgList));
    }



    private int mCurPlaces;
    private OrderEvaBean.DataBean mDatas;
    private void setData(OrderEvaBean.DataBean dataBean) {
        mDatas = dataBean;
        mPhotoNumbArr = new int[dataBean.getGoodses().size()];
        for (int i=0;i<dataBean.getGoodses().size();i++){
            mImgList.put(i, new LinkedHashMap<Integer, File>());
            mImpList.put(i, new ArrayList<String>());
        }

        for (int i=0;i<dataBean.getGoodses().size();i++)
            mLazeStackVector.add(new Stack<Integer>());
        mRecycleview.setAdapter(new CommonAdapter<OrderEvaBean.DataBean.GoodsesBean>(this,R.layout.item_goodseva,dataBean.getGoodses()) {
            @Override
            protected void convert(final ViewHolder holder, OrderEvaBean.DataBean.GoodsesBean bean, final int position) {

                Glide.with(ShopEvaluateActivity.this).load(bean.getGoodsImg()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView)holder.getView(R.id.as_img_shop));
                mScoreList.put(position,"5");//默认好评
                holder.setOnClickListener(R.id.as_ll_goodface, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //笑脸
                        holder.setImageResource(R.id.as_img_midface,R.mipmap.strokemidface);
                        holder.setImageResource(R.id.as_img_badface,R.mipmap.strokebadface);
                        holder.setImageResource(R.id.as_img_goodface,R.mipmap.soildgoodface);
                        mScoreList.put(position,"5");

                    }
                });
                holder.setOnClickListener(R.id.as_ll_midface, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //中脸
                        holder.setImageResource(R.id.as_img_midface,R.mipmap.soildmidface);
                        holder.setImageResource(R.id.as_img_badface,R.mipmap.strokebadface);
                        holder.setImageResource(R.id.as_img_goodface,R.mipmap.strokegoodface);
                        mScoreList.put(position,"3");
                    }
                });
                holder.setOnClickListener(R.id.as_ll_badface, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //差评
                        holder.setImageResource(R.id.as_img_midface,R.mipmap.strokemidface);
                        holder.setImageResource(R.id.as_img_badface,R.mipmap.soildbadface);
                        holder.setImageResource(R.id.as_img_goodface,R.mipmap.strokegoodface);
                        mScoreList.put(position,"1");
                    }
                });
                final TagFlowLayout tagFlowLayout = holder.getView(R.id.as_flowlayout);
                tagFlowLayout.setAdapter(new TagAdapter<OrderEvaBean.DataBean.GoodsesBean.ImpressionBean>(bean.getImpression()) {
                    @Override
                    public View getView(FlowLayout parent, int position, OrderEvaBean.DataBean.GoodsesBean.ImpressionBean bean) {
                        final TextView tv = (TextView) LayoutInflater.from(ShopEvaluateActivity.this).inflate(R.layout.item_flow_tv,
                                tagFlowLayout, false);
                        tv.setBackgroundResource(R.drawable.tag_eva_bg);
                        tv.setText(bean.getImpression());
                        tv.setTag(String.valueOf(bean.getId()));
                        return tv;
                    }
                });
                tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int pos, FlowLayout parent) {
                        if (mImpList.get(position).contains(view.getTag())){
                            Iterator<String> sListIterator = mImpList.get(position).iterator();
                            while(sListIterator.hasNext()){
                                String e = sListIterator.next();
                                if(e.equals(view.getTag())){
                                    sListIterator.remove();
                                }
                            }
                        }else{
                            mImpList.get(position).add((String)view.getTag());
                        }
                        Log.e("imp",new Gson().toJson(mImpList));
                        return false;
                    }
                });
                EditText tvContent = holder.getView(R.id.as_et_content);
                tvContent.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mContentList.put(position,s.toString());
                        Log.e("img",new Gson().toJson(mContentList));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                holder.setOnClickListener(R.id.as_rl_upload_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildBannerBean(mImgList.get(position).get(0));
                        Intent intent = new Intent(ShopEvaluateActivity.this,ShopBannerActivity.class);
                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
                        intent.putExtra(Constant.BANNER.POSITION,0);
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.as_rl_upload_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildBannerBean(mImgList.get(position).get(1));
                        Intent intent = new Intent(ShopEvaluateActivity.this,ShopBannerActivity.class);
                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
                        intent.putExtra(Constant.BANNER.POSITION,0);
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.as_rl_upload_3, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildBannerBean(mImgList.get(position).get(2));
                        Intent intent = new Intent(ShopEvaluateActivity.this,ShopBannerActivity.class);
                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
                        intent.putExtra(Constant.BANNER.POSITION,0);
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.as_rl_upload_4, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildBannerBean(mImgList.get(position).get(3));
                        Intent intent = new Intent(ShopEvaluateActivity.this,ShopBannerActivity.class);
                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
                        intent.putExtra(Constant.BANNER.POSITION,0);
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.as_rl_upload_5, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buildBannerBean(mImgList.get(position).get(4));
                        Intent intent = new Intent(ShopEvaluateActivity.this,ShopBannerActivity.class);
                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
                        intent.putExtra(Constant.BANNER.POSITION,0);
                        startActivity(intent);
                    }
                });

                mLazeStackVector.get(position).push(4);
                mLazeStackVector.get(position).push(3);
                mLazeStackVector.get(position).push(2);
                mLazeStackVector.get(position).push(1);
                mLazeStackVector.get(position).push(0);
                holder.setOnClickListener(R.id.as_tv_takephoto, new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShopEvaluateActivity.this);
                        builder.setTitle("选择图片");
                        builder.setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCurEditTag = position;
                                mCurTvTakePhoto = v;
                                mCurPlaces = mLazeStackVector.get(position).peek();
                                //取得下一张图片放置的容器
                                switch (mCurPlaces){
                                    case 0:
                                        mCurEditImageView = holder.getView(R.id.as_img_upload_1);
                                        mCurEditLayout = holder.getView(R.id.as_rl_upload_1);
                                        break;
                                    case 1:
                                        mCurEditImageView = holder.getView(R.id.as_img_upload_2);
                                        mCurEditLayout = holder.getView(R.id.as_rl_upload_2);
                                        break;
                                    case 2:
                                        mCurEditImageView = holder.getView(R.id.as_img_upload_3);
                                        mCurEditLayout = holder.getView(R.id.as_rl_upload_3);
                                        break;
                                    case 3:
                                        mCurEditImageView = holder.getView(R.id.as_img_upload_4);
                                        mCurEditLayout = holder.getView(R.id.as_rl_upload_4);
                                        break;
                                    case 4:
                                        mCurEditImageView = holder.getView(R.id.as_img_upload_5);
                                        mCurEditLayout = holder.getView(R.id.as_rl_upload_5);
                                        break;
                                }
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

                holder.setOnClickListener(R.id.as_img_delete_1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mImgList.get(position).remove(0);
//                        Iterator<Map.Entry<Integer,File>> iterator =  mImgList.get(position).entrySet().iterator();
//                        for (int j=0; iterator.hasNext(); j++){
//                            Map.Entry<Integer,File> entry = iterator.next();
//                            if (entry.getKey() == 0) {
//                                iterator.remove();
//                                break;
//                            }
//                        }
                        mLazeStackVector.get(position).add(0);
                        mapDelete(position,0);
                        mPhotoNumbArr[position]--;
//                        diffFileList();
                        if (mImgList.get(position).size() < 5) holder.setVisible(R.id.as_tv_takephoto,true);
                        holder.setVisible(R.id.as_rl_upload_1,false);
                        Log.e("img",new Gson().toJson(mImgList));
                    }
                });

                holder.setOnClickListener(R.id.as_img_delete_2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mImgList.get(position).remove(1);
//                        Iterator<Map.Entry<Integer,File>> iterator =  mImgList.get(position).entrySet().iterator();
//                        for (int j=0; iterator.hasNext(); j++){
//                            Map.Entry<Integer,File> entry = iterator.next();
//                            if (entry.getKey() == 1) {
//                                iterator.remove();
//                                break;
//                            }
//                        }
                        mLazeStackVector.get(position).add(1);
                        mapDelete(position,1);
                        mPhotoNumbArr[position]--;
//                        diffFileList();
                        if (mImgList.get(position).size() < 5) holder.setVisible(R.id.as_tv_takephoto,true);
                        holder.setVisible(R.id.as_rl_upload_2,false);
                        Log.e("img",new Gson().toJson(mImgList));
                    }
                });

                holder.setOnClickListener(R.id.as_img_delete_3, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mImgList.get(position).remove(2);
//                        Iterator<Map.Entry<Integer,File>> iterator =  mImgList.get(position).entrySet().iterator();
//                        for (int j=0; iterator.hasNext(); j++){
//                            Map.Entry<Integer,File> entry = iterator.next();
//                            if (entry.getKey() == 2) {
//                                iterator.remove();
//                                break;
//                            }
//                        }
                        mLazeStackVector.get(position).add(2);
                        mapDelete(position,2);
                        mPhotoNumbArr[position]--;
//                        diffFileList();
                        if (mImgList.get(position).size() < 5) holder.setVisible(R.id.as_tv_takephoto,true);
                        holder.setVisible(R.id.as_rl_upload_3,false);
                        Log.e("img",new Gson().toJson(mImgList));
                    }
                });

                holder.setOnClickListener(R.id.as_img_delete_4, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mImgList.get(position).remove(3);
//                        Iterator<Map.Entry<Integer,File>> iterator =  mImgList.get(position).entrySet().iterator();
//                        for (int j=0; iterator.hasNext(); j++){
//                            Map.Entry<Integer,File> entry = iterator.next();
//                            if (entry.getKey() == 3) {
//                                iterator.remove();
//                                break;
//                            }
//                        }
                        mLazeStackVector.get(position).add(3);
                        mapDelete(position,3);
                        mPhotoNumbArr[position]--;
//                        diffFileList();
                        if (mImgList.get(position).size() < 5) holder.setVisible(R.id.as_tv_takephoto,true);
                        holder.setVisible(R.id.as_rl_upload_4,false);
                        Log.e("img",new Gson().toJson(mImgList));
                    }
                });

                holder.setOnClickListener(R.id.as_img_delete_5, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mImgList.get(position).remove(4);
//                        Iterator<Map.Entry<Integer,File>> iterator =  mImgList.get(position).entrySet().iterator();
//                        for (int j=0; iterator.hasNext(); j++){
//                            Map.Entry<Integer,File> entry = iterator.next();
//                            if (entry.getKey() == 4) {
//                                iterator.remove();
//                                break;
//                            }
//                        }
                        mLazeStackVector.get(position).add(4);
                        mapDelete(position,4);
                        mPhotoNumbArr[position]--;
//                        diffFileList();
                        if (mImgList.get(position).size() < 5) holder.setVisible(R.id.as_tv_takephoto,true);
                        holder.setVisible(R.id.as_rl_upload_5,false);
                        Log.e("img",new Gson().toJson(mImgList));
                    }
                });

            }
        });
    }

    private void mapDelete(int parentPos,int removetag){
        Iterator<Map.Entry<Integer,File>> iterator =  mImgList.get(parentPos).entrySet().iterator();
        for (;iterator.hasNext();){
            Map.Entry<Integer,File> entry = iterator.next();
            if (entry.getKey() == removetag) {
                Log.e("删除了",entry.getKey()+"");
                iterator.remove();
                break;
            }
        }
    }

    /**
     * 修正顺序
     */
    private void diffFileList(){
        Iterator<Map.Entry<Integer,LinkedHashMap<Integer,File>>> iterator= mImgList.entrySet().iterator();
        for (int i=0; iterator.hasNext() ; i++)
        {
            Map.Entry<Integer,LinkedHashMap<Integer,File>> entry = iterator.next();
            Iterator<Map.Entry<Integer,File>> entryIterator = entry.getValue().entrySet().iterator();
            for (int j=0; entryIterator.hasNext(); j++){
                Map.Entry<Integer,File> bean = entryIterator.next();
                if (bean.getKey() != j){
                    File file = bean.getValue();
                    mapDelete(i,bean.getKey());
//                    mImgList.get(i).remove(bean.getKey());
                    mImgList.get(i).put(j,file);
                }
            }
        }
    }
    ArrayList<BannerItemBean> mBannerItemBean;
    public void buildBannerBean(File file ){
        mBannerItemBean = new ArrayList<>();
        mBannerItemBean.add(new BannerItemBean(Constant.BANNER.FILE,file));
    }


}
