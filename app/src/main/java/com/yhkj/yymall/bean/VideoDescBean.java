package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class VideoDescBean {

    /**
     * code : 0
     * msg : 1
     * data : {"banner":[{"src":"http://oss.yiyiyaya.cc/upload/2017/12/09/20171209024734787.jpg"},{"src":"http://oss.yiyiyaya.cc/upload/2017/12/09/20171209032125382.jpg"}],"explain":""}
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
         * banner : [{"src":"http://oss.yiyiyaya.cc/upload/2017/12/09/20171209024734787.jpg"},{"src":"http://oss.yiyiyaya.cc/upload/2017/12/09/20171209032125382.jpg"}]
         * explain :
         */

        private String explain;
        private ArrayList<BannerBean> banner;

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public ArrayList<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(ArrayList<BannerBean> banner) {
            this.banner = banner;
        }

        public static class BannerBean implements Parcelable {
            /**
             * src : http://oss.yiyiyaya.cc/upload/2017/12/09/20171209024734787.jpg
             */

            private String src;

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.src);
            }

            public BannerBean() {
            }

            protected BannerBean(Parcel in) {
                this.src = in.readString();
            }

            public static final Parcelable.Creator<BannerBean> CREATOR = new Parcelable.Creator<BannerBean>() {
                @Override
                public BannerBean createFromParcel(Parcel source) {
                    return new BannerBean(source);
                }

                @Override
                public BannerBean[] newArray(int size) {
                    return new BannerBean[size];
                }
            };
        }
    }
}
