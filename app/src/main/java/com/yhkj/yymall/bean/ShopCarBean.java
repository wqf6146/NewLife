package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class ShopCarBean {

    /**
     * code : 0
     * msg : 1
     * data : {"sellers":[{"id":123,"name":"yiyiyaya自营","goodses":[{"id":12,"name":"狗狗巡逻队滑行车停车场","spec":"8辆车+全套狗狗","price":24.9,"marketPrice":44.9,"nums":1,"img":"http://yiyiyayaoss-cn-zhangjiakou.aliyuncs.comupload/2017/07/04/20170704134928154.jpg","checked":1}],"checked":1}],"invalid":[{"id":12,"name":"狗狗巡逻队滑行车停车场","spec":"8辆车+全套狗狗"}],"total":24.9,"diffTotal":20,"allChecked":1,"checkedCount":2}
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
         * sellers : [{"id":123,"name":"yiyiyaya自营","goodses":[{"id":12,"name":"狗狗巡逻队滑行车停车场","spec":"8辆车+全套狗狗","price":24.9,"marketPrice":44.9,"nums":1,"img":"http://yiyiyayaoss-cn-zhangjiakou.aliyuncs.comupload/2017/07/04/20170704134928154.jpg","checked":1}],"checked":1}]
         * invalid : [{"id":12,"name":"狗狗巡逻队滑行车停车场","spec":"8辆车+全套狗狗"}]
         * total : 24.9
         * diffTotal : 20.0
         * allChecked : 1
         * checkedCount : 2
         */

        private double total;
        private double diffTotal;
        private int allChecked;
        private int checkedCount;
        private List<SellersBean> sellers;
        private List<InvalidBean> invalid;

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getDiffTotal() {
            return diffTotal;
        }

        public void setDiffTotal(double diffTotal) {
            this.diffTotal = diffTotal;
        }

        public int getAllChecked() {
            return allChecked;
        }

        public void setAllChecked(int allChecked) {
            this.allChecked = allChecked;
        }

        public int getCheckedCount() {
            return checkedCount;
        }

        public void setCheckedCount(int checkedCount) {
            this.checkedCount = checkedCount;
        }

        public List<SellersBean> getSellers() {
            return sellers;
        }

        public void setSellers(List<SellersBean> sellers) {
            this.sellers = sellers;
        }

        public List<InvalidBean> getInvalid() {
            return invalid;
        }

        public void setInvalid(List<InvalidBean> invalid) {
            this.invalid = invalid;
        }

        public static class SellersBean {
            /**
             * id : 123
             * name : yiyiyaya自营
             * goodses : [{"id":12,"name":"狗狗巡逻队滑行车停车场","spec":"8辆车+全套狗狗","price":24.9,"marketPrice":44.9,"nums":1,"img":"http://yiyiyayaoss-cn-zhangjiakou.aliyuncs.comupload/2017/07/04/20170704134928154.jpg","checked":1}]
             * checked : 1
             */

            private int id;
            private String name;
            private int checked;
            private List<GoodsesBean> goodses;

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

            public List<GoodsesBean> getGoodses() {
                return goodses;
            }

            public void setGoodses(List<GoodsesBean> goodses) {
                this.goodses = goodses;
            }

            public static class GoodsesBean {
                /**
                 * id : 12
                 * name : 狗狗巡逻队滑行车停车场
                 * spec : 8辆车+全套狗狗
                 * price : 24.9
                 * marketPrice : 44.9
                 * nums : 1
                 * img : http://yiyiyayaoss-cn-zhangjiakou.aliyuncs.comupload/2017/07/04/20170704134928154.jpg
                 * checked : 1
                 */

                private int id;
                private String name;
                private String spec;
                private double sell_price;
                private double price;
                private double marketPrice;
                private int nums;
                private String img;
                private int checked;
                private String goodsId;
                private List<GiftBean> gift;

                public void setGift(List<GiftBean> gift) {
                    this.gift = gift;
                }

                public List<GiftBean> getGift() {
                    return gift;
                }

                public void setSell_price(double sell_price) {
                    this.sell_price = sell_price;
                }

                public double getSell_price() {
                    return sell_price;
                }

                public String getGoodsId() {
                    return goodsId;
                }

                public void setGoodsId(String goodsId) {
                    this.goodsId = goodsId;
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

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public double getMarketPrice() {
                    return marketPrice;
                }

                public void setMarketPrice(double marketPrice) {
                    this.marketPrice = marketPrice;
                }

                public int getNums() {
                    return nums;
                }

                public void setNums(int nums) {
                    this.nums = nums;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public int getChecked() {
                    return checked;
                }

                public void setChecked(int checked) {
                    this.checked = checked;
                }

                public static class GiftBean {
                    /**
                     * img : http://oss.yiyiyaya.cc/upload/2017/07/22/20170722161237758.jpg
                     * name : 秋季童鞋
                     * price : 59.00
                     * goodsId : 43
                     * type : 0
                     * nums : 1
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
        }

        public static class InvalidBean {
            /**
             * id : 12
             * name : 狗狗巡逻队滑行车停车场
             * spec : 8辆车+全套狗狗
             */

            private int id;
            private String name;
            private int categoryId;
            private int goodsId;
            private String categoryName;
            private String spec;
            private String img;

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
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

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }
        }
    }
}
