package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class RankChartBean {

    /**
     * code : 0
     * msg : 1
     * data : {"head":{"nowVal":"2","downVal":8,"name":"LV1"},"body":[{"id":"1","group_name":"LV0","maxexp":"10"},{"id":"2","group_name":"LV1","maxexp":"30"},{"id":"3","group_name":"LV2","maxexp":"50"},{"id":"4","group_name":"LV3","maxexp":"70"},{"id":"5","group_name":"LV4","maxexp":"100"},{"id":"6","group_name":"LV5","maxexp":"150"},{"id":"7","group_name":"LV6","maxexp":"250"},{"id":"8","group_name":"LV7","maxexp":"500"}],"rule":["1.完成一个商品对评价获得10个经验值（评论后24个小时内发放）。","2.购物获得经验值。","3.租赁获得经验值。"],"consumes":["1.购物退货减扣经验值。"],"remark":["1.以上规则自2017年7月1日生效。","2.会员达到相应经验即可升级，允许跨级升级。","3.有效订单指的是交易完成未退款的订单。"]}
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
        /**
         * head : {"nowVal":"2","downVal":8,"name":"LV1"}
         * body : [{"id":"1","group_name":"LV0","maxexp":"10"},{"id":"2","group_name":"LV1","maxexp":"30"},{"id":"3","group_name":"LV2","maxexp":"50"},{"id":"4","group_name":"LV3","maxexp":"70"},{"id":"5","group_name":"LV4","maxexp":"100"},{"id":"6","group_name":"LV5","maxexp":"150"},{"id":"7","group_name":"LV6","maxexp":"250"},{"id":"8","group_name":"LV7","maxexp":"500"}]
         * rule : ["1.完成一个商品对评价获得10个经验值（评论后24个小时内发放）。","2.购物获得经验值。","3.租赁获得经验值。"]
         * consumes : ["1.购物退货减扣经验值。"]
         * remark : ["1.以上规则自2017年7月1日生效。","2.会员达到相应经验即可升级，允许跨级升级。","3.有效订单指的是交易完成未退款的订单。"]
         */

        private HeadBean head;
        private List<BodyBean> body;
        private List<String> rule;
        private List<String> consumes;
        private List<String> remark;

        public HeadBean getHead() {
            return head;
        }

        public void setHead(HeadBean head) {
            this.head = head;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public List<String> getRule() {
            return rule;
        }

        public void setRule(List<String> rule) {
            this.rule = rule;
        }

        public List<String> getConsumes() {
            return consumes;
        }

        public void setConsumes(List<String> consumes) {
            this.consumes = consumes;
        }

        public List<String> getRemark() {
            return remark;
        }

        public void setRemark(List<String> remark) {
            this.remark = remark;
        }

        public static class HeadBean {
            /**
             * nowVal : 2
             * downVal : 8
             * name : LV1
             */

            private String nowVal;
            private int downVal;
            private String name;

            public String getNowVal() {
                return nowVal;
            }

            public void setNowVal(String nowVal) {
                this.nowVal = nowVal;
            }

            public int getDownVal() {
                return downVal;
            }

            public void setDownVal(int downVal) {
                this.downVal = downVal;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class BodyBean {
            /**
             * id : 1
             * group_name : LV0
             * maxexp : 10
             */

            private String id;
            private String group_name;
            private String maxexp;
            private String minexp;

            public void setMinexp(String minexp) {
                this.minexp = minexp;
            }

            public String getMinexp() {
                return minexp;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public String getMaxexp() {
                return maxexp;
            }

            public void setMaxexp(String maxexp) {
                this.maxexp = maxexp;
            }
        }
    }
}
