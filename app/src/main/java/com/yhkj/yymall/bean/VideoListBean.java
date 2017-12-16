package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class VideoListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"token":"at.djw10bks6uufwpgiblbnj6jk2pa46rpv-5by7tssju6-1b74cym-sa6nyxtpm","list":[{"deviceSerial":"104084559","deviceName":"食堂摄像头1","model":"CS-C6-21WFR-B","status":1,"defence":1,"isEncrypt":0,"alarmSoundMode":0,"offlineNotify":0,"id":5,"validate_code":"QQQQ","title":"红太阳幼儿园-141班-食堂-食堂摄像头1","img":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75"},{"deviceSerial":"115940614","deviceName":"141班摄像头1","model":"CS-C6TC-32WFR","status":1,"defence":0,"isEncrypt":0,"alarmSoundMode":0,"offlineNotify":0,"id":6,"validate_code":"QQWWDS","title":"红太阳幼儿园-141班-141教室-141班摄像头1","img":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75"},{"deviceSerial":"737959033","deviceName":"131班摄像头","model":"DS-2DE2204IW-D3/W","status":1,"defence":0,"isEncrypt":0,"alarmSoundMode":0,"offlineNotify":0,"id":7,"validate_code":"GEBPDG","title":"红太阳幼儿园-131班-131班-131班摄像头","img":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75"}]}
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
         * token : at.djw10bks6uufwpgiblbnj6jk2pa46rpv-5by7tssju6-1b74cym-sa6nyxtpm
         * list : [{"deviceSerial":"104084559","deviceName":"食堂摄像头1","model":"CS-C6-21WFR-B","status":1,"defence":1,"isEncrypt":0,"alarmSoundMode":0,"offlineNotify":0,"id":5,"validate_code":"QQQQ","title":"红太阳幼儿园-141班-食堂-食堂摄像头1","img":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75"},{"deviceSerial":"115940614","deviceName":"141班摄像头1","model":"CS-C6TC-32WFR","status":1,"defence":0,"isEncrypt":0,"alarmSoundMode":0,"offlineNotify":0,"id":6,"validate_code":"QQWWDS","title":"红太阳幼儿园-141班-141教室-141班摄像头1","img":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75"},{"deviceSerial":"737959033","deviceName":"131班摄像头","model":"DS-2DE2204IW-D3/W","status":1,"defence":0,"isEncrypt":0,"alarmSoundMode":0,"offlineNotify":0,"id":7,"validate_code":"GEBPDG","title":"红太阳幼儿园-131班-131班-131班摄像头","img":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75"}]
         */

        private String token;
        private ArrayList<ListBean> list;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public ArrayList<ListBean> getList() {
            return list;
        }

        public void setList(ArrayList<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Parcelable {
            /**
             * deviceSerial : 104084559
             * deviceName : 食堂摄像头1
             * model : CS-C6-21WFR-B
             * status : 1
             * defence : 1
             * isEncrypt : 0
             * alarmSoundMode : 0
             * offlineNotify : 0
             * id : 5
             * validate_code : QQQQ
             * title : 红太阳幼儿园-141班-食堂-食堂摄像头1
             * img : https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=259501163,2193031492&fm=58&s=5384F60A2CEA7F20049D8B74030010B1&bpow=121&bpoh=75
             */

            private String deviceSerial;
            private String deviceName;
            private String model;
            private int status;
            private int defence;
            private int isEncrypt;
            private int alarmSoundMode;
            private int offlineNotify;
            private int id;
            private String validate_code;
            private String title;
            private String school_name;
            private String img;

            public void setSchool_name(String school_name) {
                this.school_name = school_name;
            }

            public String getSchool_name() {
                return school_name;
            }

            public String getDeviceSerial() {
                return deviceSerial;
            }

            public void setDeviceSerial(String deviceSerial) {
                this.deviceSerial = deviceSerial;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getDefence() {
                return defence;
            }

            public void setDefence(int defence) {
                this.defence = defence;
            }

            public int getIsEncrypt() {
                return isEncrypt;
            }

            public void setIsEncrypt(int isEncrypt) {
                this.isEncrypt = isEncrypt;
            }

            public int getAlarmSoundMode() {
                return alarmSoundMode;
            }

            public void setAlarmSoundMode(int alarmSoundMode) {
                this.alarmSoundMode = alarmSoundMode;
            }

            public int getOfflineNotify() {
                return offlineNotify;
            }

            public void setOfflineNotify(int offlineNotify) {
                this.offlineNotify = offlineNotify;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getValidate_code() {
                return validate_code;
            }

            public void setValidate_code(String validate_code) {
                this.validate_code = validate_code;
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.deviceSerial);
                dest.writeString(this.deviceName);
                dest.writeString(this.model);
                dest.writeInt(this.status);
                dest.writeInt(this.defence);
                dest.writeInt(this.isEncrypt);
                dest.writeInt(this.alarmSoundMode);
                dest.writeInt(this.offlineNotify);
                dest.writeInt(this.id);
                dest.writeString(this.validate_code);
                dest.writeString(this.title);
                dest.writeString(this.school_name);
                dest.writeString(this.img);
            }

            public ListBean() {
            }

            protected ListBean(Parcel in) {
                this.deviceSerial = in.readString();
                this.deviceName = in.readString();
                this.model = in.readString();
                this.status = in.readInt();
                this.defence = in.readInt();
                this.isEncrypt = in.readInt();
                this.alarmSoundMode = in.readInt();
                this.offlineNotify = in.readInt();
                this.id = in.readInt();
                this.validate_code = in.readString();
                this.title = in.readString();
                this.school_name = in.readString();
                this.img = in.readString();
            }

            public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
                @Override
                public ListBean createFromParcel(Parcel source) {
                    return new ListBean(source);
                }

                @Override
                public ListBean[] newArray(int size) {
                    return new ListBean[size];
                }
            };
        }
    }
}
