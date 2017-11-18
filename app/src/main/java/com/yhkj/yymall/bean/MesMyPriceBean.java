package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MesMyPriceBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"id":691,"intro":"进行积分抽奖消费10积分","type":2,"time":"2017-08-15","status":1},{"id":700,"intro":"进行积分抽奖消费10积分","type":2,"time":"2017-08-15","status":1},{"id":701,"intro":"进行积分抽奖消费10积分","type":2,"time":"2017-08-15","status":1},{"id":702,"intro":"进行积分抽奖消费10积分","type":2,"time":"2017-08-15","status":1},{"id":206,"intro":"使用余额支付购买，订单[20170812115534240002]，金额：348元","type":1,"time":"2017-08-12","status":1},{"id":210,"intro":"使用余额支付购买，订单[20170812120208834493]，金额：6000元","type":1,"time":"2017-08-12","status":1},{"id":222,"intro":"使用余额支付购买，订单[20170812145644345502]，金额：30000元","type":1,"time":"2017-08-12","status":1},{"id":652,"intro":"进行积分抽奖消费10积分","type":2,"time":"2017-08-12","status":1}]}
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
             * id : 691
             * intro : 进行积分抽奖消费10积分
             * type : 2
             * time : 2017-08-15
             * status : 1
             */

            private int id;
            private String intro;
            private int type;
            private String time;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
