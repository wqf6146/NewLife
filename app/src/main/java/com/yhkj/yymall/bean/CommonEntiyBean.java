package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/29.
 */

public class CommonEntiyBean {

    /**
     * code : 0
     * msg : 1
     * data : {"comments":[{"id":46,"contents":"婆婆","imgs":["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/comment/1501234885WQ3esf.jpeg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/comment/15012348875lS6rV.jpeg"],"reContents":null,"goodsSpec":"儿童玩具颜色：蓝色","username":"18330273525","ico":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/headImg/1501225859NPMGPx.jpeg"},{"id":41,"contents":"条件","imgs":"","reContents":null,"goodsSpec":"儿童玩具颜色：蓝色","username":"18330273525","ico":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/headImg/1501225859NPMGPx.jpeg"}]}
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
        private List<CommentsBean> comments;

        public List<CommentsBean> getComments() {
            return comments;
        }

        public void setComments(List<CommentsBean> comments) {
            this.comments = comments;
        }

        public static class CommentsBean {
            /**
             * id : 46
             * contents : 婆婆
             * imgs : ["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/comment/1501234885WQ3esf.jpeg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/comment/15012348875lS6rV.jpeg"]
             * reContents : null
             * goodsSpec : 儿童玩具颜色：蓝色
             * username : 18330273525
             * ico : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/headImg/1501225859NPMGPx.jpeg
             */

            private int id;
            private String contents;
            private String reContents;
            private String goodsSpec;
            private String username;
            private String ico;
            private List<String> imgs;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public String getReContents() {
                return reContents;
            }

            public void setReContents(String reContents) {
                this.reContents = reContents;
            }

            public String getGoodsSpec() {
                return goodsSpec;
            }

            public void setGoodsSpec(String goodsSpec) {
                this.goodsSpec = goodsSpec;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getIco() {
                return ico;
            }

            public void setIco(String ico) {
                this.ico = ico;
            }

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }
        }
    }
}
