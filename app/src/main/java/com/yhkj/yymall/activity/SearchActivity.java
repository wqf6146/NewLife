package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.HotKeyBean;
import com.yhkj.yymall.bean.RecnetSearchBean;
import com.yhkj.yymall.bean.db.RecnetSearchBeanDao;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/26.
 */

public class SearchActivity extends BaseToolBarActivity {

    @Bind(R.id.as_recentflowlayout)
    TagFlowLayout mrecentFlowLayout;

    @Bind(R.id.as_allflowlayout)
    TagFlowLayout mAllFlowLayout;

    @Bind(R.id.as_rl_recent)
    RelativeLayout mRlRecent;

    @Bind(R.id.as_img_rubbish)
    ImageView mRubbish;

    @Bind(R.id.vt_btn_right_2)
    TextView mTvSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        String hotString = YYApp.getInstance().getmHotSearch();
        if (!TextUtils.isEmpty(hotString)){
            YYApp.getInstance().setmHotSearch(hotString);
            mEditSearch.setHint(hotString);
        }
    }

    @Override
    protected void initData() {
        setImgRightVisiable(GONE);
        setNetWorkErrShow(GONE);
        setToolBarColor(getResources().getColor(R.color.white));
        setImgBackResource(R.mipmap.ic_nor_gray_arrowleft);
        mTvSearch.setVisibility(View.VISIBLE);
        mTvSearch.setText("搜索");
        mTvSearch.setTextSize(14);
        mTvSearch.setTextColor(getResources().getColor(R.color.redfont));
        String value = getIntent().getStringExtra("value");
        if (!TextUtils.isEmpty(value))
            mEditSearch.setText(value);


        YYMallApi.getHotKey(this, new ApiCallback<HotKeyBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(HotKeyBean.DataBean dataBean) {

                mAllFlowLayout.setAdapter(new TagAdapter<String>(dataBean.getList()) {
                    @Override
                    public View getView(FlowLayout parent, int position, final String s) {
                        TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_flow_tv,
                                mAllFlowLayout, false);
                        tv.setText(s);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QueryBuilder<RecnetSearchBean> queryBuilder = DbHelper.getInstance().recnetSearchBeanLongDBManager().queryBuilder();
                                List<RecnetSearchBean> listRes = queryBuilder.where(RecnetSearchBeanDao.Properties.MText.eq(s)).list();
                                if (listRes.size() == 0){
                                    DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(s));
                                }else{
                                    RecnetSearchBean bean = listRes.get(0);
                                    DbHelper.getInstance().recnetSearchBeanLongDBManager().delete(bean);
                                    DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(s));
                                }

                                Intent intent = new Intent(SearchActivity.this,ShopListActivity.class);
                                intent.putExtra("value",s);
                                intent.putExtra("name",s);
                                intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                                startActivity(intent);
                            }
                        });
                        return tv;
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecentSearchShop();
    }
    private TagAdapter mTagAdapter;
    private void loadRecentSearchShop(){
        List<RecnetSearchBean> recnetSearchBeanList = DbHelper.getInstance().recnetSearchBeanLongDBManager().queryBuilder().orderDesc(RecnetSearchBeanDao.Properties.Id).limit(10)
                .build().list();
        if (recnetSearchBeanList.size() > 0){
            mRlRecent.setVisibility(View.VISIBLE);
            mrecentFlowLayout.setVisibility(View.VISIBLE);
            if (mTagAdapter == null){
                mTagAdapter = new TagAdapter<RecnetSearchBean>(recnetSearchBeanList) {
                    @Override
                    public View getView(FlowLayout parent, int position, final RecnetSearchBean bean) {
                        TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_flow_tv,
                                mrecentFlowLayout, false);
                        tv.setText(bean.getMText());
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QueryBuilder<RecnetSearchBean> queryBuilder = DbHelper.getInstance().recnetSearchBeanLongDBManager().queryBuilder();
                                List<RecnetSearchBean> listRes = queryBuilder.where(RecnetSearchBeanDao.Properties.MText.eq(bean.getMText())).list();
                                if (listRes.size() == 0){
                                    DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(bean.getMText()));
                                }else{
                                    RecnetSearchBean bean = listRes.get(0);
                                    DbHelper.getInstance().recnetSearchBeanLongDBManager().delete(bean);
                                    DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(bean.getMText()));
                                }
                                Intent intent = new Intent(SearchActivity.this,ShopListActivity.class);
                                intent.putExtra("value",bean.getMText());
                                intent.putExtra("name",bean.getMText());
                                intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                                startActivity(intent);
                            }
                        });
                        return tv;
                    }
                };
                mrecentFlowLayout.setAdapter(mTagAdapter);
            }else{
                mTagAdapter.setTagDatas(recnetSearchBeanList);
                mTagAdapter.notifyDataChanged();
            }
        }else{
            mRlRecent.setVisibility(GONE);
            mrecentFlowLayout.setVisibility(GONE);
        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();

        mRubbish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清楚最近搜索
                DbHelper.getInstance().recnetSearchBeanLongDBManager().deleteAll();
                loadRecentSearchShop();
            }
        });

        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH  ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)){
                    String entitytext = mEditSearch.getText().toString().trim();
                    String hinttext = mEditSearch.getHint().toString().trim();

                    String text = TextUtils.isEmpty(entitytext) ? (TextUtils.isEmpty(hinttext) ?  null : hinttext): entitytext;
                    if (TextUtils.isEmpty(text)){
                        mEditSearch.requestFocus();
                        mEditSearch.setError("不能为空");
                    }else {
                        QueryBuilder<RecnetSearchBean> queryBuilder = DbHelper.getInstance().recnetSearchBeanLongDBManager().queryBuilder();
                        List<RecnetSearchBean> listRes = queryBuilder.where(RecnetSearchBeanDao.Properties.MText.eq(text)).list();
                        if (listRes.size() == 0){
                            DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(text));
                        }else{
                            RecnetSearchBean bean = listRes.get(0);
                            DbHelper.getInstance().recnetSearchBeanLongDBManager().delete(bean);
                            DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(text));
                        }
                        mEditSearch.setText("");
                        Intent intent = new Intent(SearchActivity.this,ShopListActivity.class);
                        intent.putExtra("value",text);
                        intent.putExtra("name",text);
                        intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                        startActivity(intent);
                    }

                    return true;
                }

                return false;
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String entitytext = mEditSearch.getText().toString().trim();
                String hinttext = mEditSearch.getHint().toString().trim();

                String text = TextUtils.isEmpty(entitytext) ? (TextUtils.isEmpty(hinttext) ?  null : hinttext): entitytext;
                if (TextUtils.isEmpty(text)){
                    mEditSearch.requestFocus();
                    mEditSearch.setError("不能为空");
                }else {
                    QueryBuilder<RecnetSearchBean> queryBuilder = DbHelper.getInstance().recnetSearchBeanLongDBManager().queryBuilder();
                    List<RecnetSearchBean> listRes = queryBuilder.where(RecnetSearchBeanDao.Properties.MText.eq(text)).list();
                    if (listRes.size() == 0){
                        DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(text));
                    }else{
                        RecnetSearchBean bean = listRes.get(0);
                        DbHelper.getInstance().recnetSearchBeanLongDBManager().delete(bean);
                        DbHelper.getInstance().recnetSearchBeanLongDBManager().insert(new RecnetSearchBean(text));
                    }
                    mEditSearch.setText("");
                    Intent intent = new Intent(SearchActivity.this,ShopListActivity.class);
                    intent.putExtra("value",text);
                    intent.putExtra("name",text);
                    intent.putExtra(Constant.TOOLBAR_TYPE.TYPE, Constant.TOOLBAR_TYPE.SEARCH_TV);
                    startActivity(intent);
                }
            }
        });


    }
}
