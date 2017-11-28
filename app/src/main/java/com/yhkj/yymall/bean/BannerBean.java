package com.yhkj.yymall.bean;


import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class BannerBean {


    /**
     * code : 0
     * msg : 1
     * data : {"slides":[{"img":"http://img.zcool.cn/community/01c74f558914040000001fa339ac4a.jpg","url":"http://www.gov.cn/"},{"img":"http://img.zcool.cn/community/016dc357a066130000018c1b2b998e.jpg","url":"http://yjbys.com/xuexi/xinde/667626.html"},{"img":"http://img.redocn.com/sheji/20160310/zuPRO5shoujibanner_5971244.jpg","url":"http://www.miit.gov.cn/n1146290/n1146397/c5712379/content.html"},{"img":"http://img.zcool.cn/community/017f5a566e45e432f8755ac6c96c98.gif","url":"http://www.gov.cn/premier/2017-06/29/content_5206812.htm"}]}
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
        private List<SlidesBean> slides;

        public List<SlidesBean> getSlides() {
            return slides;
        }

        public void setSlides(List<SlidesBean> slides) {
            this.slides = slides;
        }

        public static class SlidesBean {
            /**
             * img : http://img.zcool.cn/community/01c74f558914040000001fa339ac4a.jpg
             * url : http://www.gov.cn/
             */

            private String img;
            private String url;
            private String title;

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTitle() {
                return title;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
