package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class YaYaLogBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"id":125,"type":5,"datetime":"2017-07-26 11:48:36","value":2,"intro":"进行积分抽奖获得2个丫丫币"},{"id":124,"type":5,"datetime":"2017-07-26 11:38:13","value":2,"intro":"进行积分抽奖获得2个丫丫币"},{"id":121,"type":5,"datetime":"2017-07-25 14:50:01","value":2,"intro":"进行积分抽奖获得2个丫丫币"},{"id":120,"type":5,"datetime":"2017-07-25 14:49:53","value":2,"intro":"进行积分抽奖获得2个丫丫币"},{"id":119,"type":5,"datetime":"2017-07-25 14:49:45","value":2,"intro":"进行积分抽奖获得2个丫丫币"},{"id":109,"type":1,"datetime":"2017-07-23 10:38:44","value":3,"intro":"成功购买了订单号：[20170723092037404468]中的商品,消费3个丫丫币"},{"id":108,"type":1,"datetime":"2017-07-23 10:33:49","value":0,"intro":"成功购买了订单号：[20170723092037404468]中的商品,消费0个丫丫币"}]}
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
             * id : 125
             * type : 5
             * datetime : 2017-07-26 11:48:36
             * value : 2
             * intro : 进行积分抽奖获得2个丫丫币
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
