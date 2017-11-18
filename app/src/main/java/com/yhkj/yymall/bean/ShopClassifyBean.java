package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ShopClassifyBean {

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
        private List<CategorysBean> categorys;

        public List<CategorysBean> getCategorys() {
            return categorys;
        }

        public void setCategorys(List<CategorysBean> categorys) {
            this.categorys = categorys;
        }

        public static class CategorysBean {
            /**
             * id : 4
             * name : 儿童推车
             * childs : [{"id":7,"name":"小推车","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/17/20170717204533625.jpg"},{"id":9,"name":"大推车","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718161907754.jpg"}]
             */

            private int id;
            private String name;
            private List<ChildsBean> childs;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ChildsBean> getChilds() {
                return childs;
            }

            public void setChilds(List<ChildsBean> childs) {
                this.childs = childs;
            }

            public static class ChildsBean {
                /**
                 * id : 7
                 * name : 小推车
                 * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/17/20170717204533625.jpg
                 */

                private int id;
                private String name;
                private String img;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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
}
