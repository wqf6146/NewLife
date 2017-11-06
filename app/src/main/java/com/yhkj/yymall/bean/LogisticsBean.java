package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class LogisticsBean {

    /**
     * code : 0
     * msg : 1
     * data : [{"id":1,"freightCompany":"圆通速递","officialCall":null,"deliveryCode":"00000000201708283","img":"http://oss.yiyiyaya.cc/upload/2017/08/19/20170819082250235.jpg","total":1,"status":2,"traces":[{"AcceptStation":"呼和浩特市邮政速递物流分公司金川揽投部安排投递（投递员姓名：安某;联系电话：18800000000）","AcceptTime":"2017-08-29 12:34:25"},{"AcceptStation":"离开深圳市 发往广州市","AcceptTime":"2017-08-28 19:34:25"},{"AcceptStation":"深圳市横岗速递营销部已收件，（揽投员姓名：钟某某;联系电话：18000000000）","AcceptTime":"2017-08-28 15:34:25"}]},{"id":1,"freightCompany":"圆通速递","officialCall":null,"deliveryCode":"22","img":"http://oss.yiyiyaya.cc/upload/2017/08/19/20170819082250235.jpg","total":1,"status":0,"traces":[]}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 1
         * freightCompany : 圆通速递
         * officialCall : null
         * deliveryCode : 00000000201708283
         * img : http://oss.yiyiyaya.cc/upload/2017/08/19/20170819082250235.jpg
         * total : 1
         * status : 2
         * traces : [{"AcceptStation":"呼和浩特市邮政速递物流分公司金川揽投部安排投递（投递员姓名：安某;联系电话：18800000000）","AcceptTime":"2017-08-29 12:34:25"},{"AcceptStation":"离开深圳市 发往广州市","AcceptTime":"2017-08-28 19:34:25"},{"AcceptStation":"深圳市横岗速递营销部已收件，（揽投员姓名：钟某某;联系电话：18000000000）","AcceptTime":"2017-08-28 15:34:25"}]
         */

        private int id;
        private String freightCompany;
        private String officialCall;
        private String deliveryCode;
        private String img;
        private int total;
        private int status;
        private List<TracesBean> traces;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFreightCompany() {
            return freightCompany;
        }

        public void setFreightCompany(String freightCompany) {
            this.freightCompany = freightCompany;
        }

        public String getOfficialCall() {
            return officialCall;
        }

        public void setOfficialCall(String officialCall) {
            this.officialCall = officialCall;
        }

        public String getDeliveryCode() {
            return deliveryCode;
        }

        public void setDeliveryCode(String deliveryCode) {
            this.deliveryCode = deliveryCode;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<TracesBean> getTraces() {
            return traces;
        }

        public void setTraces(List<TracesBean> traces) {
            this.traces = traces;
        }

        public static class TracesBean implements Parcelable {
            /**
             * AcceptStation : 呼和浩特市邮政速递物流分公司金川揽投部安排投递（投递员姓名：安某;联系电话：18800000000）
             * AcceptTime : 2017-08-29 12:34:25
             */

            private String AcceptStation;
            private String AcceptTime;

            public String getAcceptStation() {
                return AcceptStation;
            }

            public void setAcceptStation(String AcceptStation) {
                this.AcceptStation = AcceptStation;
            }

            public String getAcceptTime() {
                return AcceptTime;
            }

            public void setAcceptTime(String AcceptTime) {
                this.AcceptTime = AcceptTime;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.AcceptStation);
                dest.writeString(this.AcceptTime);
            }

            public TracesBean() {
            }

            protected TracesBean(Parcel in) {
                this.AcceptStation = in.readString();
                this.AcceptTime = in.readString();
            }

            public static final Creator<TracesBean> CREATOR = new Creator<TracesBean>() {
                @Override
                public TracesBean createFromParcel(Parcel source) {
                    return new TracesBean(source);
                }

                @Override
                public TracesBean[] newArray(int size) {
                    return new TracesBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.freightCompany);
            dest.writeString(this.officialCall);
            dest.writeString(this.deliveryCode);
            dest.writeString(this.img);
            dest.writeInt(this.total);
            dest.writeInt(this.status);
            dest.writeList(this.traces);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.freightCompany = in.readString();
            this.officialCall = in.readString();
            this.deliveryCode = in.readString();
            this.img = in.readString();
            this.total = in.readInt();
            this.status = in.readInt();
            this.traces = new ArrayList<TracesBean>();
            in.readList(this.traces, TracesBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
