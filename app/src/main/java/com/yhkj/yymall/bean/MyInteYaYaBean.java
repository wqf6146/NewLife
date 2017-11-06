package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MyInteYaYaBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"points":{"fill_my_info":"10","comment_point":"0"},"yy":{"fill_baby_info":"0","user_register":"0","invite_reward":"5","beinvited_reward":"5"}}}
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
         * info : {"points":{"fill_my_info":"10","comment_point":"0"},"yy":{"fill_baby_info":"0","user_register":"0","invite_reward":"5","beinvited_reward":"5"}}
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * points : {"fill_my_info":"10","comment_point":"0"}
             * yy : {"fill_baby_info":"0","user_register":"0","invite_reward":"5","beinvited_reward":"5"}
             */

            private PointsBean points;
            private YyBean yy;

            public PointsBean getPoints() {
                return points;
            }

            public void setPoints(PointsBean points) {
                this.points = points;
            }

            public YyBean getYy() {
                return yy;
            }

            public void setYy(YyBean yy) {
                this.yy = yy;
            }

            public static class PointsBean {
                /**
                 * fill_my_info : 10
                 * comment_point : 0
                 */

                private String fill_my_info;
                private String comment_point;

                public String getFill_my_info() {
                    return fill_my_info;
                }

                public void setFill_my_info(String fill_my_info) {
                    this.fill_my_info = fill_my_info;
                }

                public String getComment_point() {
                    return comment_point;
                }

                public void setComment_point(String comment_point) {
                    this.comment_point = comment_point;
                }
            }

            public static class YyBean {
                /**
                 * fill_baby_info : 0
                 * user_register : 0
                 * invite_reward : 5
                 * beinvited_reward : 5
                 */

                private String fill_baby_info;
                private String user_register;
                private String invite_reward;
                private String beinvited_reward;

                public String getFill_baby_info() {
                    return fill_baby_info;
                }

                public void setFill_baby_info(String fill_baby_info) {
                    this.fill_baby_info = fill_baby_info;
                }

                public String getUser_register() {
                    return user_register;
                }

                public void setUser_register(String user_register) {
                    this.user_register = user_register;
                }

                public String getInvite_reward() {
                    return invite_reward;
                }

                public void setInvite_reward(String invite_reward) {
                    this.invite_reward = invite_reward;
                }

                public String getBeinvited_reward() {
                    return beinvited_reward;
                }

                public void setBeinvited_reward(String beinvited_reward) {
                    this.beinvited_reward = beinvited_reward;
                }
            }
        }
    }
}
