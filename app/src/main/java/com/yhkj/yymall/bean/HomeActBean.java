package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class HomeActBean {

    /**
     * code : 0
     * msg : 1
     * data : {"activity":{"url":"http://shop.aircheng.com","pic":"http://shop.aircheng.com/views/xiaomi/skin/default/image/logo.png","name":"年中大促"},"activity2":{"free":1160,"cleanSale":29,"flashSale":3600},"prizeDraw":{"url":"http://shop.aircheng.com","pic":"http://shop.aircheng.com/views/xiaomi/skin/default/image/logo.png"},"groupPurchase":[{"id":"2","price":"12.00","market_price":"50.00","sale":"5","img":"upload/2017/07/03/20170703161223998.png","name":"夏装日系连帽5五分袖t恤男短袖韩版潮流带帽中袖学生宽松半袖衣服"},{"id":"1","price":"20.00","market_price":"50.00","sale":"5","img":"upload/2017/07/03/20170703161223998.png","name":"夏装日系连帽5五分袖t恤男短袖韩版潮流带帽中袖学生宽松半袖衣服"}]}
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
         * activity : {"url":"http://shop.aircheng.com","pic":"http://shop.aircheng.com/views/xiaomi/skin/default/image/logo.png","name":"年中大促"}
         * activity2 : {"free":1160,"cleanSale":29,"flashSale":3600}
         * prizeDraw : {"url":"http://shop.aircheng.com","pic":"http://shop.aircheng.com/views/xiaomi/skin/default/image/logo.png"}
         * groupPurchase : [{"id":"2","price":"12.00","market_price":"50.00","sale":"5","img":"upload/2017/07/03/20170703161223998.png","name":"夏装日系连帽5五分袖t恤男短袖韩版潮流带帽中袖学生宽松半袖衣服"},{"id":"1","price":"20.00","market_price":"50.00","sale":"5","img":"upload/2017/07/03/20170703161223998.png","name":"夏装日系连帽5五分袖t恤男短袖韩版潮流带帽中袖学生宽松半袖衣服"}]
         */

        private ActivityBean activity;
        private Activity2Bean activity2;
        private PrizeDrawBean prizeDraw;
        private List<GroupPurchaseBean> groupPurchase;
        private int roll;
        private String keyWords;

        public void setKeyWords(String keyWords) {
            this.keyWords = keyWords;
        }

        public String getKeyWords() {
            return keyWords;
        }

        public void setRoll(int roll) {
            this.roll = roll;
        }

        public int getRoll() {
            return roll;
        }

        public ActivityBean getActivity() {
            return activity;
        }

        public void setActivity(ActivityBean activity) {
            this.activity = activity;
        }

        public Activity2Bean getActivity2() {
            return activity2;
        }

        public void setActivity2(Activity2Bean activity2) {
            this.activity2 = activity2;
        }

        public PrizeDrawBean getPrizeDraw() {
            return prizeDraw;
        }

        public void setPrizeDraw(PrizeDrawBean prizeDraw) {
            this.prizeDraw = prizeDraw;
        }

        public List<GroupPurchaseBean> getGroupPurchase() {
            return groupPurchase;
        }

        public void setGroupPurchase(List<GroupPurchaseBean> groupPurchase) {
            this.groupPurchase = groupPurchase;
        }

        public static class ActivityBean {
            /**
             * url : http://shop.aircheng.com
             * pic : http://shop.aircheng.com/views/xiaomi/skin/default/image/logo.png
             * name : 年中大促
             */

            private String url;
            private String pic;
            private String name;
            private int status;

            public void setStatus(int status) {
                this.status = status;
            }

            public int getStatus() {
                return status;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class Activity2Bean {
            /**
             * free : 1160
             * cleanSale : 29
             * flashSale : 3600
             */

            private int free;
            private int cleanSale;
            private int flashSale;

            public int getFree() {
                return free;
            }

            public void setFree(int free) {
                this.free = free;
            }

            public int getCleanSale() {
                return cleanSale;
            }

            public void setCleanSale(int cleanSale) {
                this.cleanSale = cleanSale;
            }

            public int getFlashSale() {
                return flashSale;
            }

            public void setFlashSale(int flashSale) {
                this.flashSale = flashSale;
            }
        }

        public static class PrizeDrawBean {
            /**
             * url : http://shop.aircheng.com
             * pic : http://shop.aircheng.com/views/xiaomi/skin/default/image/logo.png
             */

            private String url;
            private String pic;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }

        public static class GroupPurchaseBean {
            /**
             * id : 2
             * price : 12.00
             * market_price : 50.00
             * sale : 5
             * img : upload/2017/07/03/20170703161223998.png
             * name : 夏装日系连帽5五分袖t恤男短袖韩版潮流带帽中袖学生宽松半袖衣服
             */

            private String id;
            private String price;
            private String marketPrice;
            private String sale;
            private String img;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String market_price) {
                this.marketPrice = market_price;
            }

            public String getSale() {
                return sale;
            }

            public void setSale(String sale) {
                this.sale = sale;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
