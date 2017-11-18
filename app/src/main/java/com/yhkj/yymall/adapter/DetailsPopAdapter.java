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
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

public class DetailsPopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ShopDetailsBean.DataBean.SpecBean> allList;
    private Context mContext;
    private SparseArray<Integer> mListSelectSpec = new SparseArray<>();
    private DetailsPopCall calls;
    private int numb,allowMaxNum,limitnum;

    private int mStoreCount;
    public DetailsPopAdapter(Context context, List<ShopDetailsBean.DataBean.SpecBean> allList, SparseArray listSelectSpec, DetailsPopCall call, int numbs,int storeCount,
                             int allowMaxNum,int limitnum) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.allList = allList;
        this.mListSelectSpec = listSelectSpec;
        this.calls = call;
        this.numb = numbs;
        this.limitnum = limitnum;
        this.allowMaxNum = allowMaxNum;

        mStoreCount = storeCount;
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
            numbViewHolder.bindView();
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

        public void bindView(final ShopDetailsBean.DataBean.SpecBean specBean, final int pos) {
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
                    if (position == mListSelectSpec.get(pos)) {
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
                            mListSelectSpec.put(pos, position);
                            notifyDataSetChanged();
                            if (calls != null) {
                                calls.send(mListSelectSpec);
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
        private TextView tv_detailsPop_sub, tv_detailsPop_numb, tv_detailsPop_add,iva_tv_allowmaxnumb;

        public NumbViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_detailsPop_sub = (TextView) view.findViewById(R.id.tv_detailsPop_sub);
            tv_detailsPop_numb = (TextView) view.findViewById(R.id.tv_detailsPop_numb);
            tv_detailsPop_add = (TextView) view.findViewById(R.id.tv_detailsPop_add);
            iva_tv_allowmaxnumb = (TextView)view.findViewById(R.id.iva_tv_allowmaxnumb);
        }

        public void bindView() {

            if (limitnum != 0)
                iva_tv_allowmaxnumb.setText("(最多可购买" + limitnum + "件商品)");

            tv_detailsPop_numb.setText(numb + "");
            tv_detailsPop_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numb != 1) {
                        numb--;
                        tv_detailsPop_numb.setText(numb + "");
                        if (calls != null) {
                            calls.send(numb);
                        }
                    }
                }
            });
            tv_detailsPop_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mStoreCount < numb + 1) {
                        Toast.makeText(mContext,"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (limitnum !=0 && allowMaxNum < numb + 1) {
                        Toast.makeText(mContext,"超过最大可购买数量",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    numb++;
                    tv_detailsPop_numb.setText(numb + "");
                    if (calls != null) {
                        calls.send(numb);
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
