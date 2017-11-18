package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class AddressListBean  {


    /**
     * code : 0
     * data : {"list":[{"id":"34","accept_name":"楚小号小号","zip":"","province":"河北省","city":"秦皇岛市","area":"海港区","street":"文化路街道","address":"UC还出现","mobile":"343727","is_default":"1","province_id":"130000","city_id":"130300","area_id":"130302","street_id":"130302001"},{"id":"35","accept_name":"楚丑就丑","zip":"","province":"天津市","city":"市辖区","area":"和平区","street":"体育馆街道","address":"好的好的呵呵","mobile":"1836464","is_default":"0","province_id":"120000","city_id":"120100","area_id":"120101","street_id":"120101003"}]}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Parcelable {

            /**
             * id : 34
             * accept_name : 楚小号小号
             * zip :
             * province : 河北省
             * city : 秦皇岛市
             * area : 海港区
             * street : 文化路街道
             * address : UC还出现
             * mobile : 343727
             * is_default : 1
             * province_id : 130000
             * city_id : 130300
             * area_id : 130302
             * street_id : 130302001
             */

            private String id;
            private String accept_name;
            private String zip;
            private String province;
            private String city;
            private String area;
            private String street;
            private String address;
            private String mobile;
            private String is_default;
            private String province_id;
            private String city_id;
            private String area_id;
            private String street_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAccept_name() {
                return accept_name;
            }

            public void setAccept_name(String accept_name) {
                this.accept_name = accept_name;
            }

            public String getZip() {
                return zip;
            }

            public void setZip(String zip) {
                this.zip = zip;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getIs_default() {
                return is_default;
            }

            public void setIs_default(String is_default) {
                this.is_default = is_default;
            }

            public String getProvince_id() {
                return province_id;
            }

            public void setProvince_id(String province_id) {
                this.province_id = province_id;
            }

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public String getArea_id() {
                return area_id;
            }

            public void setArea_id(String area_id) {
                this.area_id = area_id;
            }

            public String getStreet_id() {
                return street_id;
            }

            public void setStreet_id(String street_id) {
                this.street_id = street_id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.accept_name);
                dest.writeString(this.zip);
                dest.writeString(this.province);
                dest.writeString(this.city);
                dest.writeString(this.area);
                dest.writeString(this.street);
                dest.writeString(this.address);
                dest.writeString(this.mobile);
                dest.writeString(this.is_default);
                dest.writeString(this.province_id);
                dest.writeString(this.city_id);
                dest.writeString(this.area_id);
                dest.writeString(this.street_id);
            }

            public ListBean() {
            }

            protected ListBean(Parcel in) {
                this.id = in.readString();
                this.accept_name = in.readString();
                this.zip = in.readString();
                this.province = in.readString();
                this.city = in.readString();
                this.area = in.readString();
                this.street = in.readString();
                this.address = in.readString();
                this.mobile = in.readString();
                this.is_default = in.readString();
                this.province_id = in.readString();
                this.city_id = in.readString();
                this.area_id = in.readString();
                this.street_id = in.readString();
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
