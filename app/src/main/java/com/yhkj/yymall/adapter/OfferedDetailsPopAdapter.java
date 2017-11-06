package com.yhkj.yymall.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.OfferedDetailsBean;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class OfferedDetailsPopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<OfferedDetailsBean.DataBean.GoodsBean.SpecBean> allList;
    private Context mContext;
    private SparseArray<Integer> mSelectSpecArr = new SparseArray<>();
    private DetailsPopCall calls;
    private int mCurSelectNumb;
    private int mMaxLimit;
    private int mMaxLimitTag;
    public OfferedDetailsPopAdapter(Context context, List<OfferedDetailsBean.DataBean.GoodsBean.SpecBean> allList, SparseArray sparseArray,
                                    DetailsPopCall call, int curSelectNumb,int maxLimitTag, int maxLimit) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.allList = allList;
        this.mSelectSpecArr = sparseArray;
        this.calls = call;
        this.mCurSelectNumb = curSelectNumb;
        mMaxLimit = maxLimit;
        mMaxLimitTag = maxLimitTag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType != allList.size()) {
            view = mInflater.inflate(R.layout.pop_vps_group, parent, false);
            return new NormsViewHolder(view);
        } else {
            view = mInflater.inflate(R.layout.pop_vps_add, parent, false);
            return new NumbViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != allList.size()) {
            NormsViewHolder normsViewHolder = (NormsViewHolder) holder;
            normsViewHolder.bindView(allList.get(position), position);
        } else {
            NumbViewHolder numbViewHolder = (NumbViewHolder) holder;
            numbViewHolder.bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return allList.size() + 1;
    }

    public class NormsViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView pop_vps_classify;
        TagFlowLayout as_allflowlayouts;

        public NormsViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            pop_vps_classify = (TextView) view.findViewById(R.id.pop_vps_classify);
            as_allflowlayouts = (TagFlowLayout) view.findViewById(R.id.as_allflowlayouts);
        }

        public void bindView(final OfferedDetailsBean.DataBean.GoodsBean.SpecBean specBean, final int pos) {
            pop_vps_classify.setText(specBean.getName() + "");
            final String[] mTestVal = new String[specBean.getValue().size()];
            for (int i = 0; i < specBean.getValue().size(); i++) {
                mTestVal[i] = specBean.getValue().get(i).getName();
            }
            as_allflowlayouts.setAdapter(new TagAdapter<String>(mTestVal) {
                @Override
                public View getView(FlowLayout parent, final int position, String s) {
                    final TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_flow_tv,
                            as_allflowlayouts, false);
                    tv.setText(s);
                    if (position == mSelectSpecArr.get(pos)) {
                        tv.setTextColor(Color.parseColor("#ffffff"));
                        tv.setBackgroundResource(R.drawable.tag_checked_bg);
                    } else {
                        tv.setTextColor(Color.parseColor("#727070"));
                        tv.setBackgroundResource(R.drawable.tag_normal_bg);
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tv.setTextColor(Color.parseColor("#ffffff"));
                            tv.setBackgroundResource(R.drawable.tag_checked_bg);
                            mSelectSpecArr.put(pos, position);
                            notifyDataSetChanged();
                            if (calls != null) {
                                calls.send(mSelectSpecArr);
                            }
                        }
                    });
                    return tv;
                }
            });
        }
    }

    public class NumbViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_detailsPop_sub, tv_detailsPop_numb, tv_detailsPop_add,iva_tv_buynumb;

        public NumbViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_detailsPop_sub = (TextView) view.findViewById(R.id.tv_detailsPop_sub);
            tv_detailsPop_numb = (TextView) view.findViewById(R.id.tv_detailsPop_numb);
            tv_detailsPop_add = (TextView) view.findViewById(R.id.tv_detailsPop_add);
            iva_tv_buynumb = (TextView)view.findViewById(R.id.iva_tv_buynumb);
        }

        public void bindView(final int pos) {
            iva_tv_buynumb.setText("(团购每人限购" + mMaxLimitTag + "件)");
            iva_tv_buynumb.setVisibility(View.VISIBLE);
            tv_detailsPop_numb.setText(mCurSelectNumb + "");
            tv_detailsPop_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurSelectNumb != 1) {
                        mCurSelectNumb--;
                        tv_detailsPop_numb.setText(mCurSelectNumb + "");
                        if (calls != null) {
                            calls.send(mCurSelectNumb);
                        }
                    }
                }
            });
            tv_detailsPop_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurSelectNumb >= mMaxLimit){
                        Toast.makeText(mContext,"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCurSelectNumb++;
                    tv_detailsPop_numb.setText(mCurSelectNumb + "");
                    if (calls != null) {
                        calls.send(mCurSelectNumb);
                    }
                }
            });
        }
    }

    public interface DetailsPopCall {
        void send(SparseArray<Integer> sparseArray);
        void send(int numb);
    }

}
