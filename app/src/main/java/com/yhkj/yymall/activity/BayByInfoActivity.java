package com.yhkj.yymall.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.bean.BaByYaYaBean;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.BaByBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.http.YYMallApi;
import java.util.Calendar;
import butterknife.Bind;
import cn.qqtheme.framework.picker.DatePicker;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/23.
 */

public class BayByInfoActivity extends BaseToolBarActivity {

    @Bind(R.id.ab_ll_babylady)
    LinearLayout mLlBabylady;

    @Bind(R.id.ab_ll_lady)
    LinearLayout mLlLady;

    @Bind(R.id.ab_ll_babyedit)
    LinearLayout mLlBabyEdit;

    @Bind(R.id.ab_img_ladybaby)
    ImageView mImgLadyBaby;

    @Bind(R.id.ab_img_lady)
    ImageView mImgLady;

    @Bind(R.id.ab_tv_sex)
    TextView mTvSex;

    @Bind(R.id.ab_tv_birthday)
    TextView mTvBirthday;

    @Bind(R.id.ab_ll_babyexpected)
    LinearLayout mLlBabyExpected;

    @Bind(R.id.ab_tv_expecteddate)
    TextView mTvExpectedDate;

    @Bind(R.id.ab_tv_babyyaya)
    TextView mTvBabyYaYa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babyinfo);
    }

    private int mType = 0; //0孕妈 1宝宝

    @Override
    protected void initView() {
        super.initView();
//        setLoadViewShow(VISIBLE);
        mImgLadyBaby.setImageResource(R.mipmap.ic_nor_graycicle);
        mImgLady.setImageResource(R.mipmap.ic_nor_bluenike);
        mLlBabyEdit.setVisibility(View.GONE);
        mLlBabyExpected.setVisibility(VISIBLE);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        if (getIntent().getIntExtra("id",0) != 0){
            getData(getIntent().getIntExtra("id",0));
        }else{
            setNetWorkErrShow(GONE);
        }
    }

    private void getData(int id) {
        YYMallApi.getBaByInfo(this, id, new YYMallApi.ApiResult<BaByBean.DataBean>(BayByInfoActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(BaByBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mDataBean = dataBean;
                setUi(dataBean);
            }

        });
    }
    private BaByBean.DataBean mDataBean;
    private void setUi(BaByBean.DataBean dataBean) {
        if (dataBean.getInfo().getType() == 1){
            //怀孕
            mType = 0;
            String[] dateArr = dataBean.getInfo().getBrithday().split("-");
            showLadyPopView(dateArr);
            mTvExpectedDate.setText(dataBean.getInfo().getBrithday());
            mImgLady.setImageResource(R.mipmap.ic_nor_bluenike);
            mImgLadyBaby.setImageResource(R.mipmap.ic_nor_graycicle);
            mLlBabyEdit.setVisibility(GONE);
        }else {
            mType = 1;
            mLlBabyExpected.setVisibility(GONE);
            mLlBabyEdit.setVisibility(View.VISIBLE);
            mImgLadyBaby.setImageResource(R.mipmap.ic_nor_bluenike);
            mImgLady.setImageResource(R.mipmap.ic_nor_graycicle);
            mTvSex.setText(dataBean.getInfo().getSex());
            mTvBirthday.setText(dataBean.getInfo().getBrithday());
        }
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("宝宝资料");
        setTvRightText("保存");

        YYMallApi.getBaByInfoYaya(this, new ApiCallback<BaByYaYaBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(BaByYaYaBean.DataBean dataBean) {
                mTvBabyYaYa.setText("完善宝宝信息获得" + dataBean.getInfo() +  "丫丫");
            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
            }
        });
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setTvRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == 0){
                    String str = mTvExpectedDate.getText().toString();
                    if (getIntent().getIntExtra("id",0)!=0){
                        YYMallApi.updateBaByInfo(BayByInfoActivity.this,getIntent().getIntExtra("id",0), 3, str, 1, new YYMallApi.ApiResult<CommonBean>(BayByInfoActivity.this) {
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
                                showToast("修改成功");
                                AppManager.getInstance().finishActivity(BayByInfoActivity.class);
                            }
                        });
                    }else{
                        YYMallApi.addBaByInfo(BayByInfoActivity.this, 3, str, 1, new YYMallApi.ApiResult<CommonBean>(BayByInfoActivity.this) {
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
                                showToast("添加成功");
                                AppManager.getInstance().finishActivity(BayByInfoActivity.class);
                            }
                        });
                    }
                    return;
                }

                String sex = mTvSex.getText().toString();
                if (TextUtils.isEmpty(sex) || sex.equals("性别")){
                    showToast("请选择性别");
                    return;
                }
                String birthday = mTvBirthday.getText().toString();
                if (TextUtils.isEmpty(birthday) || birthday.equals("生日")){
                    showToast("请选择生日");
                    return;
                }
                if (getIntent().getIntExtra("id",0)!=0){
                    YYMallApi.updateBaByInfo(BayByInfoActivity.this,getIntent().getIntExtra("id",0), sex.equals("男") ? 1 : 2, birthday, 2, new YYMallApi.ApiResult<CommonBean>(BayByInfoActivity.this) {
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
                            showToast("修改成功");
                            AppManager.getInstance().finishActivity(BayByInfoActivity.class);
                        }
                    });
                }else{
                    YYMallApi.addBaByInfo(BayByInfoActivity.this, sex.equals("男") ? 1 : 2, birthday, 2, new YYMallApi.ApiResult<CommonBean>(BayByInfoActivity.this) {
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
                            showToast("添加成功");
                            AppManager.getInstance().finishActivity(BayByInfoActivity.class);
                        }
                    });
                }
            }
        });
        mLlLady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = 0;
                mLlBabyExpected.setVisibility(VISIBLE);
                mLlBabyEdit.setVisibility(GONE);
                mImgLady.setImageResource(R.mipmap.ic_nor_bluenike);
                mImgLadyBaby.setImageResource(R.mipmap.ic_nor_graycicle);
            }
        });

        mTvExpectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //预产期
                if (mDataBean != null && mDataBean.getInfo().getType() == 1) {
                    showLadyPopView(mDataBean.getInfo().getBrithday().split("-"));
                }else {
                    showLadyPopView(null);
                }
                mLlBabyEdit.setVisibility(GONE);
            }
        });

        mLlBabylady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已有孩子
                mType = 1;
                mImgLadyBaby.setImageResource(R.mipmap.ic_nor_bluenike);
                mImgLady.setImageResource(R.mipmap.ic_nor_graycicle);
                mLlBabyExpected.setVisibility(GONE);
                mLlBabyEdit.setVisibility(View.VISIBLE);
            }
        });
        mTvSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //性别
                final AlertDialog.Builder builder = new AlertDialog.Builder(BayByInfoActivity.this);
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
        mTvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //生日
                if (mDataBean != null && mDataBean.getInfo().getType() != 1) {
                    showBirthdayPopView(mDataBean.getInfo().getBrithday().split("-"));
                }else {
                    showBirthdayPopView(null);
                }
            }
        });
    }

    private void showBirthdayPopView(String[] dateArr) {
        final DatePicker picker = new DatePicker(this);
        picker.setTopPadding(2);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setRangeStart(year-50, month, day);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        picker.setRangeEnd(year, month, day);
        if (dateArr == null){
            picker.setSelectedItem(year, month, day);
        }else{
            picker.setSelectedItem(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String str = year+"-"+month+"-"+day;
                mTvBirthday.setText(str);
            }
        });
        picker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLlBabyEdit.setVisibility(View.VISIBLE);
                mImgLadyBaby.setImageResource(R.mipmap.ic_nor_bluenike);
                mImgLady.setImageResource(R.mipmap.ic_nor_graycicle);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText("生日：" + year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText("生日：" + picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText("生日：" + picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    /**
     *
     */
    private void showLadyPopView(String[] dateArr) {
        final DatePicker picker = new DatePicker(this);
        picker.setTopPadding(2);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setRangeStart(year, month, day);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        picker.setRangeEnd(year+1, month, day);
        if (dateArr == null){
            picker.setSelectedItem(year, month, day);
        }else{
            picker.setSelectedItem(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                String str = year+"-"+month+"-"+day;
                mTvExpectedDate.setText(str);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText("预产期：" +year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText("预产期：" +picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText("预产期：" +picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
}
