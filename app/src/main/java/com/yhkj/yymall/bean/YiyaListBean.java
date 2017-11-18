package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/10.
 */

public class YiyaListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"goods":[{"id":93,"name":"小天才手表测试","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/08/08/20170808110405257.jpg","sale":168,"price":"100.00","promotions":[],"panicBuyItemId":0},{"id":109,"name":"（带赠品）gb好孩子儿童自行车 宝宝自行车童车 GB1256Q-P800Y 12寸","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/08/20/20170820091916930.jpg","sale":15,"price":"399.00","promotions":[],"panicBuyItemId":0},{"id":175,"name":"迷你宝马","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/09/22/20170922093935278.jpg","sale":72,"price":"90.00","promotions":[],"panicBuyItemId":0},{"id":181,"name":"智慧娃儿童18个月-72个月脚踏三轮手推车","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/10/09/20171009024017798.jpg","sale":6,"price":"185.00","promotions":[],"panicBuyItemId":0},{"id":185,"name":"智慧娃四合一儿童脚踏三轮手推车","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/10/09/20171009035031606.jpg","sale":0,"price":"290.00","promotions":[],"panicBuyItemId":0},{"id":187,"name":"儿童三轮手推车","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/10/09/20171009041437162.jpg","sale":0,"price":"300.00","promotions":[],"panicBuyItemId":0}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<GoodsBean> goods;

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class GoodsBean extends YiYaShopBean.DataBean.GoodsBean {

        }
    }
}
