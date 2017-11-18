package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class CheckOutBean {

    /**
     * code : 0
     * msg : 1
     * data : {"sellers":[{"id":1,"name":"李伟","checked":1,"goodses":[{"id":"112","goodsId":45,"name":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","spec":"儿童玩具颜色：蓝色","price":"132.05","weight":"0.00","is_delivery":1,"marketPrice":"299.00","img":"http://oss.yiyiyaya.cc/upload/2017/07/25/20170725165408781.jpg","nums":"1","checked":1,"gift":[{"img":"http://oss.yiyiyaya.cc/upload/2017/07/27/20170727181109858.jpg","name":"蜡笔小新手办","price":"5.00","goodsId":165,"type":0,"nums":97}]}],"weight":0,"realWeight":0,"freight":[{"firstPrice":"10.00","secondPrice":"10.00","firstWeight":1000,"secondWeight":1000,"deliveryId":5,"name":"快递【北京特别贵】￥0.00元","freightPrice":"0.00","type":0}],"code":0,"sellerPrice":132.05}],"distribution":"全场包邮","myYaya":19561,"yayaLimit":39,"goodsCount":1,"total":131.05}
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
         * sellers : [{"id":1,"name":"李伟","checked":1,"goodses":[{"id":"112","goodsId":45,"name":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","spec":"儿童玩具颜色：蓝色","price":"132.05","weight":"0.00","is_delivery":1,"marketPrice":"299.00","img":"http://oss.yiyiyaya.cc/upload/2017/07/25/20170725165408781.jpg","nums":"1","checked":1,"gift":[{"img":"http://oss.yiyiyaya.cc/upload/2017/07/27/20170727181109858.jpg","name":"蜡笔小新手办","price":"5.00","goodsId":165,"type":0,"nums":97}]}],"weight":0,"realWeight":0,"freight":[{"firstPrice":"10.00","secondPrice":"10.00","firstWeight":1000,"secondWeight":1000,"deliveryId":5,"name":"快递【北京特别贵】￥0.00元","freightPrice":"0.00","type":0}],"code":0,"sellerPrice":132.05}]
         * distribution : 全场包邮
         * myYaya : 19561
         * yayaLimit : 39
         * goodsCount : 1
         * total : 131.05
         */

        private String distribution;
        private int myYaya;
        private int yayaLimit;
        private int goodsCount;
        private double total;
        private double discount;
        private String grade;
        private String preferential;
        private String cut;
        private List<SellersBean> sellers;

        public void setCut(String cut) {
            this.cut = cut;
        }

        public String getCut() {
            return cut;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getGrade() {
            return grade;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public void setPreferential(String preferential) {
            this.preferential = preferential;
        }

        public String getPreferential() {
            return preferential;
        }

        public double getDiscount() {
            return discount;
        }

        public String getDistribution() {
            return distribution;
        }

        public void setDistribution(String distribution) {
            this.distribution = distribution;
        }

        public int getMyYaya() {
            return myYaya;
        }

        public void setMyYaya(int myYaya) {
            this.myYaya = myYaya;
        }

        public int getYayaLimit() {
            return yayaLimit;
        }

        public void setYayaLimit(int yayaLimit) {
            this.yayaLimit = yayaLimit;
        }

        public int getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(int goodsCount) {
            this.goodsCount = goodsCount;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public List<SellersBean> getSellers() {
            return sellers;
        }

        public void setSellers(List<SellersBean> sellers) {
            this.sellers = sellers;
        }

        public static class SellersBean {
            /**
             * id : 1
             * name : 李伟
             * checked : 1
             * goodses : [{"id":"112","goodsId":45,"name":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","spec":"儿童玩具颜色：蓝色","price":"132.05","weight":"0.00","is_delivery":1,"marketPrice":"299.00","img":"http://oss.yiyiyaya.cc/upload/2017/07/25/20170725165408781.jpg","nums":"1","checked":1,"gift":[{"img":"http://oss.yiyiyaya.cc/upload/2017/07/27/20170727181109858.jpg","name":"蜡笔小新手办","price":"5.00","goodsId":165,"type":0,"nums":97}]}]
             * weight : 0
             * realWeight : 0
             * freight : [{"firstPrice":"10.00","secondPrice":"10.00","firstWeight":1000,"secondWeight":1000,"deliveryId":5,"name":"快递【北京特别贵】￥0.00元","freightPrice":"0.00","type":0}]
             * code : 0
             * sellerPrice : 132.05
             */

            private int id;
            private String name;
            private int checked;
            private int weight;
            private double realWeight;
            private int code;
            private double sellerPrice;
            private double promotion;
            private List<GoodsesBean> goodses;
            private List<FreightBean> freight;

            public void setPromotion(double promotion) {
                this.promotion = promotion;
            }

            public double getPromotion() {
                return promotion;
            }

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

            public int getChecked() {
                return checked;
            }

            public void setChecked(int checked) {
                this.checked = checked;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public double getRealWeight() {
                return realWeight;
            }

            public void setRealWeight(double realWeight) {
                this.realWeight = realWeight;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public double getSellerPrice() {
                return sellerPrice;
            }

            public void setSellerPrice(double sellerPrice) {
                this.sellerPrice = sellerPrice;
            }

            public List<GoodsesBean> getGoodses() {
                return goodses;
            }

            public void setGoodses(List<GoodsesBean> goodses) {
                this.goodses = goodses;
            }

            public List<FreightBean> getFreight() {
                return freight;
            }

            public void setFreight(List<FreightBean> freight) {
                this.freight = freight;
            }

            public static class GoodsesBean {
                /**
                 * id : 112
                 * goodsId : 45
                 * name : 儿童电子琴钢琴麦克风宝宝益智启蒙玩具
                 * spec : 儿童玩具颜色：蓝色
                 * price : 132.05
                 * weight : 0.00
                 * is_delivery : 1
                 * marketPrice : 299.00
                 * img : http://oss.yiyiyaya.cc/upload/2017/07/25/20170725165408781.jpg
                 * nums : 1
                 * checked : 1
                 * gift : [{"img":"http://oss.yiyiyaya.cc/upload/2017/07/27/20170727181109858.jpg","name":"蜡笔小新手办","price":"5.00","goodsId":165,"type":0,"nums":97}]
                 */

                private String id;
                private int goodsId;
                private String name;
                private String spec;
                private String price;
                private String weight;
                private int is_delivery;
                private String sell_price;
                private String marketPrice;
                private String img;
                private String nums;
                private int checked;
                private List<GiftBean> gift;

                public String getSell_price() {
                    return sell_price;
                }

                public void setSell_price(String sell_price) {
                    this.sell_price = sell_price;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getGoodsId() {
                    return goodsId;
                }

                public void setGoodsId(int goodsId) {
                    this.goodsId = goodsId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public String getWeight() {
                    return weight;
                }

                public void setWeight(String weight) {
                    this.weight = weight;
                }

                public int getIs_delivery() {
                    return is_delivery;
                }

                public void setIs_delivery(int is_delivery) {
                    this.is_delivery = is_delivery;
                }

                public String getMarketPrice() {
                    return marketPrice;
                }

                public void setMarketPrice(String marketPrice) {
                    this.marketPrice = marketPrice;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getNums() {
                    return nums;
                }

                public void setNums(String nums) {
                    this.nums = nums;
                }

                public int getChecked() {
                    return checked;
                }

                public void setChecked(int checked) {
                    this.checked = checked;
                }

                public List<GiftBean> getGift() {
                    return gift;
                }

                public void setGift(List<GiftBean> gift) {
                    this.gift = gift;
                }

                public static class GiftBean {
                    /**
                     * img : http://oss.yiyiyaya.cc/upload/2017/07/27/20170727181109858.jpg
                     * name : 蜡笔小新手办
                     * price : 5.00
                     * goodsId : 165
                     * type : 0
                     * nums : 97
                     */

                    private String img;
                    private String name;
                    private String price;
                    private int goodsId;
                    private int type;
                    private int nums;

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

                    public String getPrice() {
                        return price;
                    }

                    public void setPrice(String price) {
                        this.price = price;
                    }

                    public int getGoodsId() {
                        return goodsId;
                    }

                    public void setGoodsId(int goodsId) {
                        this.goodsId = goodsId;
                    }

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public int getNums() {
                        return nums;
                    }

                    public void setNums(int nums) {
                        this.nums = nums;
                    }
                }
            }

            public static class FreightBean {
                /**
                 * firstPrice : 10.00
                 * secondPrice : 10.00
                 * firstWeight : 1000
                 * secondWeight : 1000
                 * deliveryId : 5
                 * name : 快递【北京特别贵】￥0.00元
                 * freightPrice : 0.00
                 * type : 0
                 */

                private String firstPrice;
                private String secondPrice;
                private int firstWeight;
                private int secondWeight;
                private int deliveryId;
                private String name;
                private String freightPrice;
                private int type;

                public String getFirstPrice() {
                    return firstPrice;
                }

                public void setFirstPrice(String firstPrice) {
                    this.firstPrice = firstPrice;
                }

                public String getSecondPrice() {
                    return secondPrice;
                }

                public void setSecondPrice(String secondPrice) {
                    this.secondPrice = secondPrice;
                }

                public int getFirstWeight() {
                    return firstWeight;
                }

                public void setFirstWeight(int firstWeight) {
                    this.firstWeight = firstWeight;
                }

                public int getSecondWeight() {
                    return secondWeight;
                }

                public void setSecondWeight(int secondWeight) {
                    this.secondWeight = secondWeight;
                }

                public int getDeliveryId() {
                    return deliveryId;
                }

                public void setDeliveryId(int deliveryId) {
                    this.deliveryId = deliveryId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getFreightPrice() {
                    return freightPrice;
                }

                public void setFreightPrice(String freightPrice) {
                    this.freightPrice = freightPrice;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }
}
