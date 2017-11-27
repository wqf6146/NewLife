package com.yhkj.yymall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.UserDataActivity;
import com.yhkj.yymall.bean.CommonEntiyBean;
import com.yhkj.yymall.bean.ShopEvaTagBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;
import com.yhkj.yymall.view.ninegrid.ImageInfo;
import com.yhkj.yymall.view.ninegrid.NineGridView;
import com.yhkj.yymall.view.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/29.
 * 评论
 */

public class CommonEvaAdapter extends RecyclerView.Adapter {

    private List<CommonEntiyBean.DataBean.CommentsBean> mDatas;

    private List<ShopEvaTagBean.DataBean.ImpressionsBean> mTagDatas;

    private Context mContext;

    private String mSelect;

    public CommonEvaAdapter(Context context, List<CommonEntiyBean.DataBean.CommentsBean> datas){
        mContext = context;
        mDatas = datas;
    }

    public CommonEvaAdapter(Context context, List<ShopEvaTagBean.DataBean.ImpressionsBean> tagData,String select){
        mContext = context;
        mTagDatas = tagData;
        mSelect = select;
    }


    private boolean mIsTopRefresh = true;
    public void setEvaListData( List<CommonEntiyBean.DataBean.CommentsBean> datas,String select,boolean refreshTop){
        mDatas =datas;
        mSelect = select;
        mIsTopRefresh = refreshTop;
//        notifyItemRangeChanged(1,getItemCount());
        notifyDataSetChanged();
    }

    public void addEvaListData( List<CommonEntiyBean.DataBean.CommentsBean> datas){
        int count = getItemCount();
        mDatas.addAll(datas);
//        notifyDataSetChanged();
        notifyItemRangeInserted(count,getItemCount());
    }

    public void setEvaTagData(List<ShopEvaTagBean.DataBean.ImpressionsBean> tagData){
        mTagDatas = tagData;
        if (mTagDatas!=null && mTagDatas.size() >0)
            notifyItemChanged(0);
    }


    public CommonEvaAdapter setOnTagUpdateLisiten(OnTagUpdateLisiten onTagUpdateLisiten){
        this.mOnTagUpdateLisiten = onTagUpdateLisiten;
        return this;
    }

    OnTagUpdateLisiten mOnTagUpdateLisiten;
    public interface OnTagUpdateLisiten {
        void onUpdate(String imp,int select);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new CommonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commonhead,parent,false));
        return new CommonEntiyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commont,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0 ){
            if ( (mTagDatas == null || mTagDatas.size() == 0) || !mIsTopRefresh ) return;
            final TagFlowLayout tagFlowLayout = (TagFlowLayout) holder.itemView.findViewById(R.id.fc_flowlayout);
            final ImageView mArrow = (ImageView)holder.itemView.findViewById(R.id.fc_img_spread);

            if (mTagDatas.size() > 9){
                mArrow.setVisibility(View.VISIBLE);
                mArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mArrow.getTag() == null || (Boolean) mArrow.getTag() == false){
                            //关闭状态,此时正要打开
                            mArrow.setTag(true);
                            tagFlowLayout.showLine(-1);
                            mArrow.setImageResource(R.mipmap.arraw_top);
                        }else{
                            //打开状态，此时正要关闭
                            mArrow.setTag(false);
                            tagFlowLayout.showLine(2);
                            mArrow.setImageResource(R.mipmap.arraw_bottom);
                        }
                    }
                });
                mArrow.setImageResource(R.mipmap.arraw_top);

            }else{
                mArrow.setVisibility(View.GONE);
            }
            tagFlowLayout.showLine(-1);
            TagAdapter tagAdapter = new TagAdapter<ShopEvaTagBean.DataBean.ImpressionsBean>(mTagDatas) {
                @Override
                public View getView(FlowLayout parent, int position, ShopEvaTagBean.DataBean.ImpressionsBean bean) {
                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_flow_tv,
                            parent, false);
                    if (bean.getId() == -1){
                        tv.setText(bean.getImpression());
                    }else{
                        tv.setText(bean.getImpression() + "(" + bean.getTotal() + ")");
                    }
                    tv.setTag(String.valueOf(bean.getId()));
                    return tv;
                }
            };
            if (!TextUtils.isEmpty(mSelect)) {
                if (mSelect.equals("-1") ){
                    tagAdapter.setSelectedList(mSelect.equals("-1") ? 0 : Integer.parseInt(mSelect) + 1);
                    mOnTagUpdateLisiten.onUpdate("-1",-1);
                }else{
                    tagAdapter.setSelectedList(Integer.parseInt(mSelect) + 1);
                    mOnTagUpdateLisiten.onUpdate(String.valueOf(mTagDatas.get(Integer.parseInt(mSelect) + 1).getId()),Integer.parseInt(mSelect));
                }
            }
            tagFlowLayout.setAdapter(tagAdapter);
            tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    mSelect = null;
                    if (mOnTagUpdateLisiten!=null){
                        mOnTagUpdateLisiten.onUpdate((String) view.getTag(),position-1);
                    }
                    return false;
                }
            });

            tagFlowLayout.setMaxSelectCount(1);
//            tagFlowLayout.set
        }else{
            if (mDatas == null)return;

            final CommonEntiyHolder commonEntiyHolder = (CommonEntiyHolder) holder;
            CommonEntiyBean.DataBean.CommentsBean bean = mDatas.get(position - 1);
            if (position == mDatas.size()){
                commonEntiyHolder.mViewDivider.setVisibility(GONE);
            }
            Glide.with(mContext).load(bean.getIco())
                    .asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(commonEntiyHolder.ic_img_avatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    commonEntiyHolder.ic_img_avatar.setImageDrawable(circularBitmapDrawable);
                }
            });
            commonEntiyHolder.mTvContent.setText(bean.getContents());
            commonEntiyHolder.mTvName.setText(TextUtils.isEmpty(bean.getUsername()) ? "此用户没有填写评价" : bean.getUsername());
            if (!TextUtils.isEmpty(bean.getReContents())){
                commonEntiyHolder.mTvReply.setVisibility(View.VISIBLE);
                commonEntiyHolder.mTvReply.setText("掌柜回复：" + bean.getReContents());
            }else{
                commonEntiyHolder.mTvReply.setVisibility(GONE);
            }
            commonEntiyHolder.mTvSpec.setText(bean.getGoodsSpec());

            if (bean.getImgs() !=null && bean.getImgs().size() > 0) {
                ArrayList<ImageInfo> imageInfo = new ArrayList<>();

                for (int i = 0; i < bean.getImgs().size(); i++) {
                    ImageInfo info = new ImageInfo();
                    info.setBigImageUrl(bean.getImgs().get(i));
                    info.setThumbnailUrl(bean.getImgs().get(i));
                    imageInfo.add(info);
                }
                commonEntiyHolder.mNineGridView.setVisibility(View.VISIBLE);
                commonEntiyHolder.mNineGridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
            }else {
                commonEntiyHolder.mNineGridView.setVisibility(GONE);
            }
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null){
            return 1;
        }
        return mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }


    class CommonEntiyHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ic_img_avatar)
        ImageView ic_img_avatar;

        @Bind(R.id.ic_tv_name)
        TextView mTvName;

        @Bind(R.id.ic_tv_content)
        TextView mTvContent;

        @Bind(R.id.ic_tv_spec)
        TextView mTvSpec;

        @Bind(R.id.ic_tv_reply)
        TextView mTvReply;

        @Bind(R.id.ic_nineview)
        NineGridView mNineGridView;

        @Bind(R.id.ic_view_divider)
        View mViewDivider;

        public CommonEntiyHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    class CommonViewHolder extends RecyclerView.ViewHolder{
        public CommonViewHolder(View view){
            super(view);
        }
    }
}
