package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vise.xsnow.ui.adapter.listview.BaseAdapter;
import com.vise.xsnow.ui.adapter.listview.BaseViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.CollectClassifyBean;
import com.yhkj.yymall.util.CommonUtil;


public class ListPopView extends BasePopupWindow {

    private int mSelectPos = -1;
    private BaseAdapter mAdapter;
    private classifyItemClickEvent mOnClassifyItemClick;
    private CollectClassifyBean.DataBean mDataBean;
    public ListPopView(Activity context, CollectClassifyBean.DataBean dataBean, int selectpos) {
        super(context);
        mDataBean = dataBean;
        mSelectPos = selectpos;
        setBackPressEnable(false);
        setDismissWhenTouchOuside(true);

        ListView nestFullListView = (ListView)findViewById(R.id.vpl_nestlistview);

        mAdapter = new BaseAdapter<CollectClassifyBean.DataBean.CateListBean>(context,mDataBean.getCate_list(),R.layout.item_collectclassify) {
            @Override
            public <H extends BaseViewHolder> void convert(H viewHolder, int position, CollectClassifyBean.DataBean.CateListBean bean) {
                TextView tvClassifyName = viewHolder.getView(R.id.ic_tv_classifyname);
                tvClassifyName.setText(bean.getCate_name() + "(" + bean.getCate_nub() + ")");

                ImageView imgSelect = viewHolder.getView(R.id.ic_img_nike);
                if (mSelectPos == position){
                    imgSelect.setVisibility(View.VISIBLE);
                    imgSelect.setImageResource(R.mipmap.ic_nor_bnike);
                }else{
                    imgSelect.setVisibility(View.GONE);
                }
            }
        };
        nestFullListView.setAdapter(mAdapter);
        nestFullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnClassifyItemClick!=null)
                    mOnClassifyItemClick.onClassifyItemClick(position,mDataBean.getCate_list().get(position));
                ListPopView.this.dismiss();
            }
        });
    }

    public void setOnClassifyItemClick(classifyItemClickEvent classifyItemClick){
        mOnClassifyItemClick = classifyItemClick;
    }

    public interface classifyItemClickEvent {
        void onClassifyItemClick(int pos, CollectClassifyBean.DataBean.CateListBean bean);
    }

    @Override
    protected Animation initShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, -CommonUtil.dip2px(getContext(), 350f), 0);
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(1));
        return translateAnimation;
    }

    @Override
    protected Animation initExitAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0f, 0f, 0, -CommonUtil.dip2px(getContext(), 350f));
        translateAnimation.setDuration(450);
        translateAnimation.setInterpolator(new OvershootInterpolator(-4));
        return translateAnimation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.view_pop_list);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.vpl_ll_popview);
    }

}
