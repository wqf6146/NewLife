package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class LottoryInfoBean {

    /**
     * code : 0
     * msg : 1
     * data : {"rollinfo":{"rollid":"8","title":"测试积分"},"prizeinfo":[{"id":"35","title":"20积分","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/10/20170710151938267.jpg"},{"id":"34","title":"谢谢参与","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/10/20170710101735256.jpg"},{"id":"20","title":"童车","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/04/20170704134928154.jpg"},{"id":"17","title":"安全座椅","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708095602623.jpg"},{"id":"28","title":"3个丫丫","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708111524602.jpg"},{"id":"26","title":"10个丫丫","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708111405178.jpg"},{"id":"27","title":"5个丫丫","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708111442189.jpg"},{"id":"21","title":"高端安全座椅","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/11/20170711092247488.gif"}],"mypoint":"0"}
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
         * rollinfo : {"rollid":"8","title":"测试积分"}
         * prizeinfo : [{"id":"35","title":"20积分","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/10/20170710151938267.jpg"},{"id":"34","title":"谢谢参与","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/10/20170710101735256.jpg"},{"id":"20","title":"童车","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/04/20170704134928154.jpg"},{"id":"17","title":"安全座椅","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708095602623.jpg"},{"id":"28","title":"3个丫丫","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708111524602.jpg"},{"id":"26","title":"10个丫丫","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708111405178.jpg"},{"id":"27","title":"5个丫丫","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/08/20170708111442189.jpg"},{"id":"21","title":"高端安全座椅","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/11/20170711092247488.gif"}]
         * mypoint : 0
         */

        private RollinfoBean rollinfo;
        private String mypoint;
        private List<PrizeinfoBean> prizeinfo;

        public RollinfoBean getRollinfo() {
            return rollinfo;
        }

        public void setRollinfo(RollinfoBean rollinfo) {
            this.rollinfo = rollinfo;
        }

        public String getMypoint() {
            return mypoint;
        }

        public void setMypoint(String mypoint) {
            this.mypoint = mypoint;
        }

        public List<PrizeinfoBean> getPrizeinfo() {
            return prizeinfo;
        }

        public void setPrizeinfo(List<PrizeinfoBean> prizeinfo) {
            this.prizeinfo = prizeinfo;
        }

        public static class RollinfoBean {
            /**
             * rollid : 8
             * title : 测试积分
             */

            private String rollid;
            private String title;
            private int point;

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public String getRollid() {
                return rollid;
            }

            public void setRollid(String rollid) {
                this.rollid = rollid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public static class PrizeinfoBean {
            /**
             * id : 35
             * title : 20积分
             * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/10/20170710151938267.jpg
             */

            private String id;
            private String title;
            private String img;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
