package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/13.
 */

public class AcountSafeActivity extends BaseToolBarActivity {

    @Bind(R.id.aa_rl_setpaypwd)
    RelativeLayout mRlSetPayPwd;

    @Bind(R.id.aa_rl_payset)
    RelativeLayout mRlPaySet;

    @Bind(R.id.aa_tv_bindphone)
    TextView mTvBindPhone;

    @Bind(R.id.aa_ll_updatephone)
    LinearLayout mLlUpdatePhone;

    @Bind(R.id.aa_tv_updatephone)
    TextView mTvUpdatePhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acountsafe);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRlSetPayPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UpdatepwdActivity.class);
                intent.putExtra(Constant.PWDTYPE.TYPE,Constant.PWDTYPE.LOGIN);
                startActivity(intent);
            }
        });
        mRlPaySet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UpdatepwdActivity.class);
                intent.putExtra(Constant.PWDTYPE.TYPE,Constant.PWDTYPE.PAY);
                startActivity(intent);
            }
        });
        mLlUpdatePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,UpdatePhoneActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("账户安全");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        mTvBindPhone.setText(YYApp.getInstance().getPhone());
        mTvUpdatePhone.setText(CommonUtil.getMarkPhone(YYApp.getInstance().getPhone()));
    }
}
