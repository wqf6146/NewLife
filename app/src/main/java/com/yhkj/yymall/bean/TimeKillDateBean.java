package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class TimeKillDateBean {

    /**
     * code : 0
     * msg : 1
     * data : {"paniclBuy":[{"id":4,"start_time":"2017-07-26 12:44:10","status":1},{"id":6,"start_time":"2017-07-26 19:44:12","status":0},{"id":17,"start_time":"2017-07-26 20:37:11","status":0},{"id":18,"start_time":"2017-07-26 21:37:37","status":0},{"id":20,"start_time":"2017-08-03 08:58:04","status":0}]}
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
        private List<PaniclBuyBean> paniclBuy;

        public List<PaniclBuyBean> getPaniclBuy() {
            return paniclBuy;
        }

        public void setPaniclBuy(List<PaniclBuyBean> paniclBuy) {
            this.paniclBuy = paniclBuy;
        }

        public static class PaniclBuyBean {
            /**
             * id : 4
             * start_time : 2017-07-26 12:44:10
             * status : 1
             */

            private int id;
            private String start_time;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (! (obj instanceof PaniclBuyBean))
                    return false;
                PaniclBuyBean paniclBuyBean = ((PaniclBuyBean) obj);
                return id == paniclBuyBean.getId() && start_time.equals(paniclBuyBean.getStart_time()) && status == paniclBuyBean.getStatus();
            }
        }
    }
}
