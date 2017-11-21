package com.yhkj.yymall.adapter;

import android.content.Context;

import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public abstract class NewGoodsLikeAdapter<T> extends CommonAdapter<T> {

    public NewGoodsLikeAdapter(final Context context, final int itemlayoutId, List<T> datas){
        super(context,itemlayoutId,datas);
    }
}
