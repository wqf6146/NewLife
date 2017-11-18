package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class IntLogBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"id":471,"type":4,"datetime":"2017-07-28 18:08:19","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":442,"type":1,"datetime":"2017-07-28 15:22:41","value":10,"intro":"成功购买了订单号：[20170728152237437087]中的商品,奖励积分10"},{"id":427,"type":4,"datetime":"2017-07-28 14:57:47","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":426,"type":3,"datetime":"2017-07-28 14:48:03","value":10,"intro":"进行积分抽奖获得10积分"},{"id":425,"type":4,"datetime":"2017-07-28 14:48:03","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":424,"type":4,"datetime":"2017-07-28 14:30:02","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":423,"type":3,"datetime":"2017-07-28 14:29:58","value":15,"intro":"进行积分抽奖获得15积分"},{"id":422,"type":4,"datetime":"2017-07-28 14:29:58","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":421,"type":4,"datetime":"2017-07-28 14:29:47","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":420,"type":4,"datetime":"2017-07-28 14:29:45","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":419,"type":4,"datetime":"2017-07-28 14:27:36","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":418,"type":3,"datetime":"2017-07-28 14:26:37","value":15,"intro":"进行积分抽奖获得15积分"},{"id":417,"type":4,"datetime":"2017-07-28 14:26:37","value":-5,"intro":"进行积分抽奖消费5积分"},{"id":379,"type":4,"datetime":"2017-07-26 11:56:47","value":-2,"intro":"进行积分抽奖消费2积分"},{"id":380,"type":3,"datetime":"2017-07-26 11:56:47","value":10,"intro":"进行积分抽奖获得10积分"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 471
             * type : 4
             * datetime : 2017-07-28 18:08:19
             * value : -5
             * intro : 进行积分抽奖消费5积分
             */

            private int id;
            private int type;
            private String datetime;
            private int value;
            private String intro;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }
        }
    }
}
