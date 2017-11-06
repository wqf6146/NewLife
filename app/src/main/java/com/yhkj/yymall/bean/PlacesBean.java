package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */

public class PlacesBean {

    /**
     * code : 0
     * msg : 1
     * data : [{"area_id":"110000","area_name":"北京市"},{"area_id":"120000","area_name":"天津市"},{"area_id":"130000","area_name":"河北省"},{"area_id":"140000","area_name":"山西"},{"area_id":"150000","area_name":"内蒙古自治区"},{"area_id":"210000","area_name":"辽宁省"},{"area_id":"220000","area_name":"吉林省"},{"area_id":"230000","area_name":"黑龙江省"},{"area_id":"310000","area_name":"上海市"},{"area_id":"320000","area_name":"江苏省"},{"area_id":"330000","area_name":"浙江省"},{"area_id":"340000","area_name":"安徽省"},{"area_id":"350000","area_name":"福建省"},{"area_id":"360000","area_name":"江西省"},{"area_id":"370000","area_name":"山东省"},{"area_id":"410000","area_name":"河南省"},{"area_id":"420000","area_name":"湖北省"},{"area_id":"430000","area_name":"湖南省"},{"area_id":"440000","area_name":"广东省"},{"area_id":"450000","area_name":"广西壮族自治区"},{"area_id":"460000","area_name":"海南省"},{"area_id":"500000","area_name":"重庆市"},{"area_id":"510000","area_name":"四川省"},{"area_id":"520000","area_name":"贵州省"},{"area_id":"530000","area_name":"云南省"},{"area_id":"540000","area_name":"西藏自治区"},{"area_id":"610000","area_name":"陕西省"},{"area_id":"620000","area_name":"甘肃省"},{"area_id":"630000","area_name":"青海省"},{"area_id":"640000","area_name":"宁夏回族自治区"},{"area_id":"650000","area_name":"新疆维吾尔自治区"},{"area_id":"710000","area_name":"台湾省"},{"area_id":"810000","area_name":"香港特别行政区"},{"area_id":"820000","area_name":"澳门特别行政区"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * area_id : 110000
         * area_name : 北京市
         */

        private String area_id;
        private String area_name;

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }
    }
}
